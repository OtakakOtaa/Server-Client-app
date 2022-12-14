package Server;

import Server.Entities.Company;
import Server.Entities.Employee;
import Server.Entities.Transaction;

import java.io.Serializable;
import java.util.List;

public class TransferMessageContainer implements Serializable {

    private final String key;
    private final String message;

    public EmployeeList EmployeeList;
    public TransactionList TransactionList;

    public Employee Employee;
    public Transaction transaction;
    public Company company;


    public TransferMessageContainer(String Key, String message)
    {
        this.key = Key;
        this.message = message;
    }

    public String Key() { return key;}
    public String message() { return message;}


    @Override
    public String toString() {
        return "TransferMessageContainer{" +
                "message='" + message + '\'' +
                '}';
    }

    public static class EmployeeList implements Serializable
    {
        public EmployeeList(List<Employee> employeeList) { this.value = employeeList; }
        public List<Employee> value;
    }
    public static class TransactionList implements Serializable
    {
        public TransactionList(List<Transaction> value) { this.value = value; }
        public List<Transaction> value;
    }
}
