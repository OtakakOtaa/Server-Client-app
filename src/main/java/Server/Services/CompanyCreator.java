package Server.Services;

import Infrastructure.DiContainer.Annotations.Construct;
import Server.Entities.Company;
import Server.Entities.Employee;
import Server.Entities.User;
import Server.Loop.SessionLoop;
import Server.PersistentProvider.PersistentProvider;
import Server.ServerContract;
import Server.Session.SessionSteam;
import Server.TransferMessageContainer;

import java.io.EOFException;
import java.io.IOException;
import java.net.SocketException;

public class CompanyCreator
{
    private final PersistentProvider persistentProvider;

    @Construct
    public CompanyCreator(PersistentProvider persistentProvider)
    {
        this.persistentProvider = persistentProvider;
    }

    public void start(SessionSteam sessionSteam) throws SessionLoop.ReturnException, SessionLoop.ExitException
    {
        System.out.println(CompanyCreator.class + "/" + "start");

        try
        {
            waitAction(sessionSteam);
        }
        catch (SessionLoop.ReturnException returnException) { throw new SessionLoop.ReturnException();}
        catch (EOFException | SocketException connectionException) { throw new SessionLoop.ExitException(); }
        catch (Exception e) { System.out.println(Authentication.class + "/" + e.getMessage());}
    }


    private void waitAction(SessionSteam sessionSteam)
            throws IOException, ClassNotFoundException, SessionLoop.ReturnException
    {
        while (true)
        {
            Object message = sessionSteam.accept();
            TransferMessageContainer operationMessage = null;

            boolean isReturn = message.getClass().equals(TransferMessageContainer.class);
            if (isReturn)
            {
                TransferMessageContainer actionKey = (TransferMessageContainer) message;
                if (actionKey.message().equals(ServerContract.Operations.RETURN)) throw new SessionLoop.ReturnException();
            }

            boolean isCreateRequest = message.getClass().equals(Employee.class);
            if(isCreateRequest)
            {
                Employee employee = (Employee) message;
                operationMessage =  tryCreate(employee);
            }

            assert operationMessage != null;
            if(operationMessage.Key().equals(ServerContract.Result.SUCCESS_KEY))
            {
                sessionSteam.startAction(operationMessage);
                return;
            }
            sessionSteam.startAction(operationMessage);
        }
    }

    private TransferMessageContainer tryCreate(Employee employee)
    {
        System.out.println(CompanyCreator.class + "/" + "try_create");

        Company fetchCompany = persistentProvider.getCompanyByName(employee.getCompany().getName());
        boolean isCompanyWithThisNameAlreadyExists = fetchCompany != null;
        User fetchUser = persistentProvider.getUserByLogin(employee.getUser().getLogin());
        boolean isUserWithThisNameAlreadyExists = fetchUser != null;

        if(isCompanyWithThisNameAlreadyExists) return new TransferMessageContainer(
                ServerContract.Result.FAILED_KEY,
                ServerContract.ResultDetails.COMPANY_FAILED
        );
        if(isUserWithThisNameAlreadyExists) return new TransferMessageContainer(
                ServerContract.Result.FAILED_KEY,
                ServerContract.ResultDetails.USER_FAILED
        );

        persistentProvider.add(employee);
        return new TransferMessageContainer(ServerContract.Result.SUCCESS_KEY, "");
    }
}
