package Server.Bootstrap;

import Infrastructure.DiContainer.Bootstrap.Container;
import Infrastructure.DiContainer.Interfaces.IInstaller;
import Server.Bootstrap.Gate.IServerGate;
import Server.Bootstrap.Gate.ServerGate;
import Server.Session.SessionFactory;

public class ServerInstaller implements IInstaller
{

    @Override
    public void InstallBindings() throws Exception
    {

        Container.Bind(SessionFactory.class)
                .AsSingle();
        Container.Bind(ServerGate.class)
                .ToInterface(IServerGate.class)
                .AsSingle();
    }
}
