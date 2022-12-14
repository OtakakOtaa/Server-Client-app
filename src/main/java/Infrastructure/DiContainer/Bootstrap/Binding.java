package Infrastructure.DiContainer.Bootstrap;

import Infrastructure.DiContainer.Annotations.Construct;
import Infrastructure.DiContainer.Base.BindingBase;
import Infrastructure.DiContainer.Interfaces.Builder.IBindingScope;
import Infrastructure.DiContainer.Interfaces.Builder.ILazyBinding;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Binding<IContract> extends BindingBase<IContract>
{
    private IContract instance;
    private boolean isLazy;
    private boolean AsSingle;
    
    private boolean isInitialized;

    protected Binding(Class<?> Interface, Class<IContract> Contract) 
    {
        super(Interface, Contract);
    }

    public IContract getInstance() 
            throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException 
    {
        if (AsSingle) 
        {
            if (isLazy || !isInitialized) 
            {
                isLazy = false;
                instance = CreateAndResolve();
            }
            return instance;
        }
        else return CreateAndResolve();
    }

    @Override
    public IBindingScope ToInterface(Class<?> Interface)
    {
        if (BindingStorage.BindingExists(Interface))
        {
            throw new RuntimeException("[Container] The " + Interface.getName() + " binding already exists!");
        }
        
        this.Interface = Interface;
        return this;
    }

    @Override
    public void Lazy()
    {
        isLazy = true;
    }

    @Override
    public void NonLazy()
    {
        isLazy = false;
    }
    
    @Override
    public void Initialize() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException
    {
        if (AsSingle)
            instance = CreateAndResolve();
    }
    
    private IContract CreateAndResolve() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException
    {
        var constructs = Contract.getDeclaredConstructors();
        
        Constructor<IContract> injectConstruct = null;
        
        for (var construct: constructs) 
        {
            if (construct.isAnnotationPresent(Construct.class))
            {
                injectConstruct = (Constructor<IContract>) construct;
            }
        }
        
        if (injectConstruct == null) return Contract.getConstructor().newInstance();
        
        var argumentTypes = injectConstruct.getParameterTypes();
        var argumentCount = argumentTypes.length;
        
        if (argumentCount == 0) 
            return Contract.getDeclaredConstructor().newInstance();
        
        var arguments = new Object[argumentCount];
        
        var index = 0;
        
        for (var type: argumentTypes)
        {
            arguments[index] = Container.Resolve(type);
            index++;
        }
        
        isInitialized = true;
        
        return injectConstruct.newInstance(arguments);
    }

    @Override
    public ILazyBinding AsSingle()
    {
        AsSingle = true;
        return this;
    }

    @Override
    public void AsTransient() 
    {
        isInitialized = true;
        AsSingle = false;
    }
}
