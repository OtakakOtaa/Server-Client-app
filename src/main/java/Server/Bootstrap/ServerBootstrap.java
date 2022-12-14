package Server.Bootstrap;

import Infrastructure.DiContainer.Bootstrap.Container;
import Server.Bootstrap.Gate.IServerGate;
import Server.MainInstaller;

public class ServerBootstrap
{
    public static void main(String[] args)
    {
        MainInstaller.installBindings();

        IServerGate serverGate = Container.Resolve(IServerGate.class);
        assert serverGate != null;
        try
        {
            serverGate.listenGate();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

}

