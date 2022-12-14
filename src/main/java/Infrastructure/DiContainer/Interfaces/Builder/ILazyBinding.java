package Infrastructure.DiContainer.Interfaces.Builder;

import java.lang.reflect.InvocationTargetException;

public interface ILazyBinding 
{
    void Lazy();
    void NonLazy() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException;
}
