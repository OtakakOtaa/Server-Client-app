package Server.PersistentProvider.SQL;

import Infrastructure.DiContainer.Annotations.Construct;
import Server.Entities.*;
import Server.PersistentProvider.PersistentProvider;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class SQLProvider implements PersistentProvider
{
    private final DBConnectionFactory dbConnectionFactory;

    @Construct
    public SQLProvider(DBConnectionFactory dbConnectionFactory)
    {
        this.dbConnectionFactory = dbConnectionFactory;
    }

    @Override
    public User getUserByLogin(String login)
    {
        User user = null;
        Session session = null;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("login"), login));
            user = session.createQuery(criteriaQuery).getSingleResult();

            transaction.commit();
        }
        catch (NoResultException e) { System.out.println("Exception: " + e.getMessage()); }
        catch (Exception e){ throw new RuntimeException(); }
        finally
        {
            assert session != null;
            session.close();
        }
        return user;
    }
    @Override
    public boolean isUserExist(String login)
    {
        User user = null;
        Session session = null;
        boolean operationFlag = false;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("login"), login));
            user = session.createQuery(criteriaQuery).getSingleResult();

            transaction.commit();
            operationFlag = true;
        }
        catch (NoResultException e) {System.out.println("Exception: " + e.getMessage());}
        catch (Exception e){ throw new RuntimeException(); }
        finally
        {
            assert session != null;
            session.close();
        }
        return operationFlag;
    }

    @Override
    public Company getCompanyByName(String name)
    {
        Company company = null;
        Session session = null;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Company> criteriaQuery = criteriaBuilder.createQuery(Company.class);
            Root<Company> root = criteriaQuery.from(Company.class);

            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("name"), name));
            company = session.createQuery(criteriaQuery).getSingleResult();

            transaction.commit();
        }
        catch (NoResultException e) { System.out.println("Exception: " + e.getMessage()); }
        catch (Exception e){ throw new RuntimeException(); }
        finally
        {
            assert session != null;
            session.close();
        }
        return company;
    }

    @Override
    public Employee getEmployeeByLogin(String login)
    {
        Employee employee = null;
        Session session = null;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();

            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
            Root<User> root = criteriaQuery.from(User.class);

            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("login"), login));
            User user = session.createQuery(criteriaQuery).getSingleResult();

            String allRecordQuery = "FROM " + Employee.class.getName();
            List<Employee> employees = new ArrayList<Employee>(session.createQuery(allRecordQuery).list());
            for (var record : employees)
                if(record.getUser().getId() == user.getId()) employee = record;

            transaction.commit();
        }
        catch (NoResultException e) { System.out.println("Exception: " + e.getMessage()); }
        catch (Exception e){ throw new RuntimeException(); }
        finally
        {
            assert session != null;
            session.close();
        }
        return employee;
    }

    @Override
    public void updateEmployee(Employee employee)
    {
        Session session = null;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();
            session.update(employee);
            transaction.commit();
        }
        catch (Exception e){ throw new RuntimeException(); }
        finally
        {
            assert session != null;
            session.close();
        }
    }


    @Override
    public List<Employee> allEmployee()
    {
        ArrayList<Employee> records = null;
        Session session = null;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();

            String allRecordQuery = "FROM " + Employee.class.getName();
            records = new ArrayList<Employee>(session.createQuery(allRecordQuery).list());
            transaction.commit();
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e);
        }
        finally
        {
            assert session != null;
            session.close();
        }
        return records;
    }


    public void updateUser(User user)
    {
        Session session = null;
        boolean operationFlag = false;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();

            session.update(user);

            transaction.commit();
            operationFlag = true;
        }
        catch (Exception e){ throw new RuntimeException(); }
        finally
        {
            assert session != null;
            session.close();
        }
    }

    @Override
    public void updateCompany(Company company)
    {
        Session session = null;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();
            session.update(company);
            transaction.commit();
        }
        catch (Exception e){ throw new RuntimeException(); }
        finally
        {
            assert session != null;
            session.close();
        }
    }

    @Override
    public void addTransaction(Server.Entities.Transaction budgetTransaction, Company company)
    {
        Session session = null;
        boolean operationFlag = false;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();

            session.save(budgetTransaction);
            session.save(new History(company, budgetTransaction));

            transaction.commit();
            operationFlag = true;
        }
        catch (Exception e) { System.out.println("Exception: " + e); }
        finally
        {
            assert session != null;
            session.close();
        }
    }

    @Override
    public List<History> allHistory()
    {
        ArrayList<History> records = null;
        Session session = null;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();

            String allRecordQuery = "FROM " + History.class.getName();
            records = new ArrayList<History>(session.createQuery(allRecordQuery).list());
            transaction.commit();
        }
        catch (Exception e)
        {
            System.out.println("Exception: " + e);
        }
        finally
        {
            assert session != null;
            session.close();
        }
        return records;
    }


    @Override
    public <TRecord> boolean add(TRecord record)
    {
        Session session = null;
        boolean operationFlag = false;
        try
        {
            session = dbConnectionFactory.create().openSession();
            Transaction transaction = session.beginTransaction();

            session.save(record);

            transaction.commit();
            operationFlag = true;
        }
        catch (Exception e) { System.out.println("Exception: " + e); }
        finally
        {
            assert session != null;
            session.close();
        }
        return operationFlag;
    }

}
