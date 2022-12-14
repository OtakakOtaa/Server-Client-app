package Server.Bootstrap.Gate;

import Infrastructure.DiContainer.Annotations.Construct;
import Server.Bootstrap.Token.CancellationSessionToken;
import Server.Session.SessionFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerGate implements IServerGate
{
    private static final int PORT = 4006;
    private ArrayList<CancellationSessionToken> sessionTokens;
    private ServerSocket server;

    private final SessionFactory sessionFactory;

    private boolean isGateOpen;

    @Construct
    public ServerGate(SessionFactory sessionFactory)
    {
        System.out.println(ServerGate.class + "/" + "server_init");
        this.sessionFactory = sessionFactory;

        try
        {
            server = new ServerSocket(PORT);
            sessionTokens = new ArrayList<>();
            isGateOpen = true;
        }
        catch (IOException e)
        {
            System.out.println(ServerGateEx.serverInitEx.getMessage());
        }
    }

    public void listenGate() throws Exception
    {
        System.out.println(ServerGate.class + "/" + "server_start_listen");
        while (isGateOpen)
        {
            var clientSocket = server.accept();

            getSession(clientSocket).start();
        }
    }

    public void dispose() throws Exception
    {
        System.out.println(ServerGate.class + "/" + "server_drop");
        try
        {
            for (var token : sessionTokens)token.cancel();
            isGateOpen = false;
            server.close();
        }
        catch (IOException e)
        {
            System.out.println(ServerGateEx.serverDropEx.getMessage());
        }
    }

    private Thread getSession(Socket clientSocket)
    {
        System.out.println(ServerGate.class + "/" + "new_session_init");
        return new Thread(() ->
        {
            SessionFactory.Session session = sessionFactory.create(clientSocket);
            sessionTokens.add(session.cancellationSessionToken);
            session.sessionLoop.startSession(session.cancellationSessionToken);
        });
    }

    public static class ServerGateEx
    {
        private final static String context = ServerGate.class.getName() + "/";

        public final static Exception sessionOpenEx = new Exception(context + "Session open failed!");
        public final static Exception serverInitEx = new Exception(context + "Server init failed!");
        public final static Exception serverDropEx = new Exception(context + "Server drop failed!");
        public final static Exception sessionCloseEx = new Exception(context + "Session close failed!");
    }
}



