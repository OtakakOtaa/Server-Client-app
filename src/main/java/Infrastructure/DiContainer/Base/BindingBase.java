package Infrastructure.DiContainer.Base;

import Infrastructure.DiContainer.Interfaces.Builder.IBindingInterfaceScope;
import Infrastructure.DiContainer.Interfaces.Builder.ILazyBinding;

import java.lang.reflect.InvocationTargetException;

public abstract class BindingBase<IContract> implements ILazyBinding, IBindingInterfaceScope
{
    public Class<?> Interface;
    public Class<IContract> Contract;
    
    public BindingBase(Class<?> Interface, Class<IContract> Contract)
    {
        this.Interface = Interface;
        this.Contract = Contract;
    }
    
    public abstract IContract getInstance() 
            throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException;

    public abstract void Initialize()
            throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException;
}
