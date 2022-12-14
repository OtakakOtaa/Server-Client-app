package Server.PersistentProvider;

import Infrastructure.DiContainer.Bootstrap.Container;
import Infrastructure.DiContainer.Interfaces.IInstaller;
import Server.PersistentProvider.SQL.DBConnectionFactory;
import Server.PersistentProvider.SQL.SQLProvider;

public class PersistentProviderInstaller implements IInstaller
{
    @Override
    public void InstallBindings() throws Exception
    {
        Container.Bind(SQLProvider.class).ToInterface(PersistentProvider.class).AsSingle();
        Container.Bind(DBConnectionFactory.class).AsSingle();
    }
}
