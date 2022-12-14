package Infrastructure.DiContainer.Bootstrap;


import Infrastructure.DiContainer.Base.BindingBase;

import java.util.ArrayList;
import java.util.List;

public class BindingStorage 
{
    public static final List<BindingBase<?>> Bindings = new ArrayList<>();
    
    public static void AddBinding(BindingBase<?> binding)
    {
        Bindings.add(binding);
    }
    
    public static boolean BindingExists(Class<?> Interface)
    {
       return Bindings
               .stream()
               .anyMatch(x -> x.Interface == Interface);
    }
    
    public static <IInterface, IContract> IContract GetBindingInstance(IInterface Interface) throws Exception {

        for (var bind : Bindings) 
        {
            if (Interface == bind.Interface) 
            {
                return (IContract) bind.getInstance();
            }
        }
        throw new Exception("No contract was found for: " + Interface.getClass().getName());
    }
}
