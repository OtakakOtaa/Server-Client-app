package Client;


import Server.Session.SessionSteam;

import java.net.Socket;

public class ServerProvider extends SessionSteam
{
    private static final int SERVER_PORT = 4006;

    private ServerProvider() throws Exception
    {
        super(new Socket("localhost", SERVER_PORT));
    }

    public static ServerProvider connect() throws Exception
    {
        return new ServerProvider();
    }

}
