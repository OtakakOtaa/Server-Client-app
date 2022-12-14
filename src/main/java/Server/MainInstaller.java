package Server;

import Infrastructure.DiContainer.Bootstrap.BindingStorage;
import Infrastructure.DiContainer.Interfaces.IInstaller;
import Server.Bootstrap.ServerInstaller;
import Server.PersistentProvider.PersistentProviderInstaller;
import Server.Services.ServicesInstaller;

import java.util.ArrayList;
import java.util.List;

public class MainInstaller
{
    private static final List<Class<? extends IInstaller>> installers = new ArrayList<>();

    private static void declareInstallers()
    {
        installers.add(PersistentProviderInstaller.class);
        installers.add(ServerInstaller.class);
        installers.add(ServicesInstaller.class);
    }

    public static void installBindings()
    {
        declareInstallers();
        try
        {
            for (var installer : installers)
            {
                installer
                        .getDeclaredConstructor()
                        .newInstance()
                        .InstallBindings();
            }

            for (var binding : BindingStorage.Bindings)
            {
                binding.Initialize();
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
