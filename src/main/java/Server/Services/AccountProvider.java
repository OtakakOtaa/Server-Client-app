package Server.Services;

import Infrastructure.DiContainer.Annotations.Construct;
import Server.Entities.Company;
import Server.Entities.Employee;
import Server.Entities.Person;
import Server.Entities.User;
import Server.Loop.SessionLoop;
import Server.PersistentProvider.PersistentProvider;
import Server.ServerContract;
import Server.Session.SessionSteam;
import Server.TransferMessageContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountProvider
{
    private final PersistentProvider persistentProvider;

    @Construct
    public AccountProvider(PersistentProvider persistentProvider)
    {
        this.persistentProvider = persistentProvider;
    }

    public void approveAccount(SessionSteam sessionSteam, String login) throws IOException
    {
        User user = persistentProvider.getUserByLogin(login);

        boolean isUserExist = user != null;
        if(!isUserExist)
        {
            sessionSteam.startAction(ServerContract.Result.FAILED_KEY);
            return;
        }

        user = new User(user.getId(), user.getLogin(), user.getPassword(), user.getRegistrationDate(),1);
        persistentProvider.updateUser(user);

        sessionSteam.startAction(ServerContract.Result.SUCCESS_KEY);
    }

    public void banAccount(SessionSteam sessionSteam, String login) throws IOException
    {
        User user = persistentProvider.getUserByLogin(login);

        boolean isUserExist = user != null;
        if(!isUserExist)
        {
            sessionSteam.startAction(ServerContract.Result.FAILED_KEY);
            return;
        }

        user = new User(user.getId(), user.getLogin(), user.getPassword(), user.getRegistrationDate(),0);
        persistentProvider.updateUser(user);

        sessionSteam.startAction(ServerContract.Result.SUCCESS_KEY);
    }

    public void sendNotApproveAccountsList(SessionSteam sessionSteam, String adminLogin) throws IOException
    {
        List<Employee> notApproveEmployee = new ArrayList<Employee>();
        List<Employee> allEmployees = null;
        allEmployees = persistentProvider.allEmployee();

        Employee admin = persistentProvider.getEmployeeByLogin(adminLogin);

        boolean isAdminNotExist = admin == null;
        boolean isEmployeesNotExist = allEmployees == null;

        if(isEmployeesNotExist || isAdminNotExist)
        {
            sessionSteam.startAction(ServerContract.Result.FAILED_KEY);
            return;
        }

        int adminIndex = -1;
        String adminCompanyName = admin.getCompany().getName();
        for (var employee : allEmployees)
        {
            String employeeCompanyName = employee.getCompany().getName();
            boolean isEmployeePossessAminCompany = employeeCompanyName.equals(adminCompanyName);
            boolean isAmin = employee.getUser().getLogin().equals(adminLogin);
            boolean isEmployeeBlocked = employee.getUser().getAccountValidity() == 0;

            if(isEmployeePossessAminCompany && isEmployeeBlocked && !isAmin) notApproveEmployee.add(employee);
        }
        if(notApproveEmployee.isEmpty())
        {
            sessionSteam.startAction(ServerContract.Result.FAILED_KEY);
            return;
        }

        TransferMessageContainer dto
                = new TransferMessageContainer(ServerContract.Result.SUCCESS_KEY, ServerContract.Result.SUCCESS_KEY);
        dto.EmployeeList = new TransferMessageContainer.EmployeeList(notApproveEmployee);

        sessionSteam.startAction(dto);
    }

    public void sendAccountValidateInfo(SessionSteam sessionSteam, String login) throws SessionLoop.ExitException
    {
        try
        {
            User user = persistentProvider.getUserByLogin(login);

            boolean isUserExist = user != null;
            if(!isUserExist) return;

            if(user.getAccountValidity() == 0)
                sessionSteam.startAction(ServerContract.Result.FAILED_KEY);
            if(user.getAccountValidity() == 1)
                sessionSteam.startAction(ServerContract.Result.SUCCESS_KEY);
        }
        catch (IOException connectionException) { throw new SessionLoop.ExitException(); }
    }

    public void updateAccount(SessionSteam sessionSteam, Employee employee) throws IOException
    {
        persistentProvider.updateEmployee(employee);
        sessionSteam.startAction(ServerContract.Result.SUCCESS_KEY);
    }

    public void getAllEmployees(SessionSteam sessionSteam, String adminLogin) throws IOException
    {
        List<Employee> employeesOneCompany = new ArrayList<Employee>();
        List<Employee> allEmployees = null;

        allEmployees = persistentProvider.allEmployee();

        Employee admin = persistentProvider.getEmployeeByLogin(adminLogin);

        boolean isAdminNotExist = admin == null;
        boolean isEmployeesNotExist = allEmployees == null;

        if(isEmployeesNotExist || isAdminNotExist)
        {
            sessionSteam.startAction(ServerContract.Result.FAILED_KEY);
            return;
        }

        String adminCompanyName = admin.getCompany().getName();
        for (var employee : allEmployees)
        {
            String employeeCompanyName = employee.getCompany().getName();
            boolean isEmployeePossessAminCompany = employeeCompanyName.equals(adminCompanyName);
            boolean isAmin = employee.getUser().getLogin().equals(adminLogin);

            if(isEmployeePossessAminCompany  && !isAmin) employeesOneCompany.add(employee);
        }

        if(employeesOneCompany.isEmpty())
        {
            sessionSteam.startAction(ServerContract.Result.FAILED_KEY);
            return;
        }

        TransferMessageContainer dto
                = new TransferMessageContainer(ServerContract.Result.SUCCESS_KEY, ServerContract.Result.SUCCESS_KEY);
        dto.EmployeeList = new TransferMessageContainer.EmployeeList(employeesOneCompany);

        sessionSteam.startAction(dto);
    }


}
