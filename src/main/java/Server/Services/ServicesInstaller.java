package Server.Services;

import Infrastructure.DiContainer.Bootstrap.Container;
import Infrastructure.DiContainer.Interfaces.IInstaller;
import Server.Services.Transaction.TransactionApprover;

public class ServicesInstaller implements IInstaller
{
    @Override
    public void InstallBindings() throws Exception {
        Container.Bind(Authentication.class).AsSingle();
        Container.Bind(AccountProvider.class).AsSingle();
        Container.Bind(CompanyCreator.class).AsSingle();
        Container.Bind(TransactionApprover.class).AsSingle();
    }
}
