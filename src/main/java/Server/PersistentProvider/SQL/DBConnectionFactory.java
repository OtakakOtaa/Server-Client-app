package Server.PersistentProvider.SQL;

import Server.Entities.*;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class DBConnectionFactory
{
    private static SessionFactory sessionFactory = null;

    public SessionFactory create()
    {
        if (sessionFactory != null) return sessionFactory;

        try
        {
            Configuration configuration = new Configuration().configure();

            configuration.addAnnotatedClass(Company.class);
            configuration.addAnnotatedClass(Employee.class);
            configuration.addAnnotatedClass(Person.class);
            configuration.addAnnotatedClass(Transaction.class);
            configuration.addAnnotatedClass(User.class);
            configuration.addAnnotatedClass(History.class);

            StandardServiceRegistryBuilder builder
                    = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
            sessionFactory = configuration.buildSessionFactory(builder.build());

            return sessionFactory;
        }
        catch (Exception e)
        {
            System.out.println(SessionFactory.class + "/ connection open error/" + e.getMessage());
            return null;
        }
    }
}
