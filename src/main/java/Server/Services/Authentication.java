package Server.Services;

import Infrastructure.DiContainer.Annotations.Construct;
import Server.Config;
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

public class Authentication
{
    private final PersistentProvider persistentProvider;

    @Construct
    public Authentication(PersistentProvider persistentProvider)
    {
        this.persistentProvider = persistentProvider;
    }

    public void start(SessionSteam sessionSteam, String operationType) throws EOFException, SessionLoop.ReturnException, SocketException
    {
        System.out.println(Authentication.class + "/" + "start");

        try
        {
           waitAction(sessionSteam, operationType);
        }
        catch (SessionLoop.ReturnException returnException) { throw new SessionLoop.ReturnException();}
        catch (EOFException | SocketException connectionException) { throw new SessionLoop.ExitException(); }
        catch (Exception e) { System.out.println(Authentication.class + "/" + e.getMessage());}
    }

    private void waitAction(SessionSteam sessionSteam, String operationKey)
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

            boolean isSignIn =
                    message.getClass().equals(User.class) &&
                            operationKey.equals(ServerContract.Operations.SIGN_IN);
            if (isSignIn)
            {
                User user = (User) message;
                operationMessage = tryAuthorization(user);
            }

            boolean isSignUp =
                    message.getClass().equals(Employee.class) &&
                            operationKey.equals(ServerContract.Operations.SIGN_UP);
            if (isSignUp)
            {
                Employee employee = (Employee) message;
                operationMessage = tryRegistration(employee);
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

    private void tryPutAdminUser()
    {
        System.out.println(Authentication.class + "/" + "try_put_admin_user");

        boolean adminIsExist = persistentProvider.isUserExist(Config.admin.getLogin());
        if (adminIsExist) return;
        persistentProvider.add(Config.admin);
    }

    private TransferMessageContainer tryAuthorization(User user) throws IOException
    {
        System.out.println(Authentication.class + "/" + "authorization");

        String login = user.getLogin();
        User fetchUser = persistentProvider.getUserByLogin(login);

        if (fetchUser == null || !fetchUser.equals(user))
            return new TransferMessageContainer(ServerContract.Result.FAILED_KEY, ServerContract.ResultDetails.USER_FAILED);

        Employee employee = persistentProvider.getEmployeeByLogin(login);

        TransferMessageContainer dto = new TransferMessageContainer(ServerContract.Result.SUCCESS_KEY, "");
        dto.Employee = employee;
        return  dto;
    }

    private TransferMessageContainer tryRegistration(Employee employee)
    {
        System.out.println(Authentication.class + "/" + "registration");

        String login = employee.getUser().getLogin();

        boolean isUserAlreadyExist = persistentProvider.isUserExist(login);
        Company fetchCompany = persistentProvider.getCompanyByName(employee.getCompany().getName());
        boolean isCompanyExist = fetchCompany != null;

        if(isUserAlreadyExist)
            return new TransferMessageContainer(ServerContract.Result.FAILED_KEY, ServerContract.ResultDetails.USER_FAILED);

        if(!isCompanyExist)
            return new TransferMessageContainer(ServerContract.Result.FAILED_KEY, ServerContract.ResultDetails.COMPANY_FAILED);

        employee = new Employee(employee.getPosition(), employee.getPerson(), employee.getUser(), fetchCompany);

        persistentProvider.add(employee);
        return new TransferMessageContainer(ServerContract.Result.SUCCESS_KEY, "");
    }
}
