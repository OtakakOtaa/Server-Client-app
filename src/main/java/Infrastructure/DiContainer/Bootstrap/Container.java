package Infrastructure.DiContainer.Bootstrap;

import Infrastructure.DiContainer.Interfaces.Builder.IBindingInterfaceScope;

public class Container
{
    public static IBindingInterfaceScope Bind(Class<?> Contract) throws Exception
    {
        var binding = new Binding<>(Contract, Contract);
        
        if (BindingStorage.BindingExists(Contract))
        {
            throw new Exception("[Container] The " + Contract.getName() + " binding already exists!");
        }

        BindingStorage.AddBinding(binding);

        return binding;
    }

    public static <IInterface, IContract> IContract Resolve(IInterface Interface) 
    {
        try 
        {
            return BindingStorage.GetBindingInstance(Interface);
        } 
        catch (Exception exception)
        {
            return null;
        }
    }
}

