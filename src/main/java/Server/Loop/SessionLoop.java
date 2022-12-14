package Server.Loop;

import Server.Bootstrap.Token.CancellationSessionToken;
import Server.ServerContract;
import Server.Services.AccountProvider;
import Server.Services.Authentication;
import Server.Services.CompanyCreator;
import Server.Services.Transaction.TransactionApprover;
import Server.Session.SessionSteam;
import Server.TransferMessageContainer;

import java.io.EOFException;
import java.net.SocketException;

public class SessionLoop implements ISessionLoop
{
    private final Authentication authorization;
    private final AccountProvider accountProvider;
    private final CompanyCreator companyCreator;
    private final TransactionApprover transactionApprover;

    private final SessionSteam sessionSteam;
    private CancellationSessionToken token;

    public SessionLoop(
            Authentication authorization,
            AccountProvider accountChanger,
            CompanyCreator companyCreator,
            SessionSteam sessionSteam,
            TransactionApprover transactionApprover
    )
    {
        this.transactionApprover = transactionApprover;
        this.accountProvider = accountChanger;
        this.companyCreator = companyCreator;
        System.out.println(SessionLoop.class + "/" + "session_open");
        this.authorization = authorization;
        this.sessionSteam = sessionSteam;
    }

    public void startSession(CancellationSessionToken token)
    {
        System.out.println(SessionLoop.class + "/" + "session_run");

        this.token = token;
        while (!token.isCancel())
        {
            try
            {
                TransferMessageContainer operation = sessionSteam.waitMessage();
                switch (operation.Key())
                {
                    case ServerContract.Operations.SIGN_IN
                            -> authorization.start(sessionSteam, ServerContract.Operations.SIGN_IN);
                    case ServerContract.Operations.SIGN_UP
                            -> authorization.start(sessionSteam, ServerContract.Operations.SIGN_UP);
                    case ServerContract.Operations.CREATE_NEW_COMPANY
                            -> companyCreator.start(sessionSteam);
                    case ServerContract.Operations.ACCOUNT_VALIDATE_INFO
                            -> accountProvider.sendAccountValidateInfo(sessionSteam, operation.message());
                    case ServerContract.Operations.ACCOUNT_ACK
                            -> accountProvider.sendNotApproveAccountsList(sessionSteam, operation.message());
                    case ServerContract.Operations.UN_BLOCK_ACCOUNT
                            -> accountProvider.approveAccount(sessionSteam, operation.message());
                    case ServerContract.Operations.BLOCK_ACCOUNT
                            -> accountProvider.banAccount(sessionSteam, operation.message());
                    case ServerContract.Operations.UPDATE_ACCOUNT_INFO
                            -> accountProvider.updateAccount(sessionSteam, operation.Employee);
                    case ServerContract.Operations.GET_ALL_EMPLOYEES
                            -> accountProvider.getAllEmployees(sessionSteam, operation.message());
                    case ServerContract.Operations.ADD_TRANSACTION
                            -> transactionApprover.approveTransaction(sessionSteam, operation.message(), operation.transaction);
                    case ServerContract.Operations.ALL_TRANSACTION
                            -> transactionApprover.sendAllTransaction(sessionSteam, operation.message());

                    case ServerContract.Operations.RETURN
                            -> throw new ReturnException();
                    case ServerContract.Operations.EXIT
                            -> dispose();
                }
            }
            catch (ReturnException returnE) { System.out.println(SessionLoop.class + "/start_session/" + "return"); }
            catch (EOFException | SocketException exitE) { dispose(); }
            catch (Exception e) { System.out.println(SessionLoop.class + "/" + e.getClass() + "/" + e.getMessage()); }
        }
    }


    public void dispose()
    {
        System.out.println(SessionLoop.class + "/" + "session_dispose");

        try
        {
            token.cancel();
            sessionSteam.dispose();
        }
        catch (Exception ignored) {}
    }

    public static class ExitException extends EOFException { }
    public static class ReturnException extends Exception { }
}
