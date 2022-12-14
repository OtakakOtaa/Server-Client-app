package Server.PersistentProvider;

import Server.Entities.*;

import java.util.List;

public interface PersistentProvider {
    User getUserByLogin(String login);
    boolean isUserExist(String login);

    Company getCompanyByName(String name);

    Employee getEmployeeByLogin(String login);

    void updateEmployee(Employee employee);

    List<Employee> allEmployee();
    void updateUser(User user);

    void updateCompany(Company company);

    void addTransaction(Transaction budgetTransaction, Company company);

    List<History> allHistory();

    <TRecord> boolean add(TRecord record);
}
