package Server.Session;

import Infrastructure.DiContainer.Annotations.Construct;
import Infrastructure.DiContainer.Bootstrap.Container;
import Server.Bootstrap.Token.CancellationSessionToken;
import Server.Loop.SessionLoop;
import Server.Services.AccountProvider;
import Server.Services.Authentication;
import Server.Services.CompanyCreator;
import Server.Services.Transaction.TransactionApprover;
import Server.Services.Transaction.TransactionCreator;

import java.net.Socket;

public class SessionFactory
{

    @Construct
    public SessionFactory()
    {
    }

    public Session create(Socket clientSocket)
    {
        try
        {
            Authentication authorization = Container.Resolve(Authentication.class);
            AccountProvider accountChanger = Container.Resolve(AccountProvider.class);
            CompanyCreator companyCreator = Container.Resolve(CompanyCreator.class);
            TransactionApprover transactionApprover = Container.Resolve(TransactionApprover.class);
            Session session = new Session();

            session.sessionLoop = new SessionLoop(
                    authorization,
                    accountChanger,
                    companyCreator,
                    new SessionSteam(clientSocket),
                    transactionApprover
            );
            session.cancellationSessionToken = new CancellationSessionToken();

            return session;
        }
        catch (Exception e)
        {
            System.out.println(SessionFactory.class.getName() + "create failed/" + e.getLocalizedMessage());
            return null;
        }
    }

    public class Session
    {
        public SessionLoop sessionLoop;
        public CancellationSessionToken cancellationSessionToken;
    }
}
