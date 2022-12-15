package Client.Loop;

import Client.Regular.InputRestriction;
import Client.ServerProvider;
import Client.View.Scenes.AccountInfo.AccountInfoLayer;
import Client.View.Scenes.AccountSettings.AccountSettingsLayer;
import Client.View.Scenes.AccountSettings.AccountSettingsView;
import Client.View.Scenes.AccountValidity.AccountValidityLayer;
import Client.View.Scenes.AccountValidity.AccountValidityView;
import Client.View.Scenes.AddTransaction.AddTransactionLayer;
import Client.View.Scenes.AddTransaction.AddTransactionView;
import Client.View.Scenes.Admin.AdminScene;
import Client.View.Scenes.Admin.AdminSceneView;
import Client.View.Scenes.ApproveAccountsList.ApproveAccountsListLayer;
import Client.View.Scenes.ApproveAccountsList.ApproveAccountsListView;
import Client.View.Scenes.BanAccountList.BanAccountsListLayer;
import Client.View.Scenes.BanAccountList.BanAccountsListView;
import Client.View.Scenes.Behaviors.Exit.ExitView;
import Client.View.Scenes.Behaviors.Notification.NotificationLayer;
import Client.View.Scenes.Behaviors.UnDo.ReturnView;
import Client.View.Scenes.BudgetAmount.BudgetAmountLayer;
import Client.View.Scenes.CompanyCreate.CompanyCreateScene;
import Client.View.Scenes.CompanyCreate.CompanyCreateView;
import Client.View.Scenes.EmployeesList.EmployeesListLayer;
import Client.View.Scenes.First.FirstScene;
import Client.View.Scenes.First.FirstSceneView;
import Client.View.Scenes.Layer;
import Client.View.Scenes.SetPosition.SetPositionLayer;
import Client.View.Scenes.SetPosition.SetPositionView;
import Client.View.Scenes.SignIn.SignInScene;
import Client.View.Scenes.SignIn.SignInSceneView;
import Client.View.Scenes.SingUp.SignUpScene;
import Client.View.Scenes.SingUp.SignUpSceneView;
import Client.View.Scenes.TransactionList.TransactionListLayer;
import Client.View.Scenes.TransactionList.TransactionListView;
import Client.View.Window.Window;
import Infrastructure.Disposable;
import Server.Entities.*;
import Server.Entities.StaticEnumerable.Position;
import Server.ServerContract;
import Server.Services.Transaction.TransactionApprover;
import Server.Services.Transaction.TransactionCreator;
import Server.TransferMessageContainer;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class ClientLoop implements Disposable,
        ExitView, ReturnView,
        FirstSceneView, SignInSceneView, SignUpSceneView, CompanyCreateView,
        AccountValidityView,
        AdminSceneView,
        ApproveAccountsListView, AccountSettingsView, BanAccountsListView, SetPositionView,
        AddTransactionView, TransactionListView
{
    private final ServerProvider serverProvider;
    private final Window window;

    private Employee employee;

    private final Stack<Layer> returnLayerRoadMap;


    public ClientLoop(ServerProvider serverProvider, Window window)
    {
        this.serverProvider = serverProvider;
        this.window = window;
        returnLayerRoadMap = new Stack<Layer>();
    }

    @Override
    public void dispose()
    {
        System.out.println(ClientLoop.class + "/dispose");
        try
        {
            window.destroy();
            serverProvider.dispose();
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/dispose/" + e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // FirstScene View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onSignInButtonClick()
    {
        try
        {
            window.switchLayer(new SignInScene(this));
            returnLayerRoadMap.push(new FirstScene(this));

            serverProvider.startAction(ServerContract.Operations.SIGN_IN);
        }
        catch (IOException ignored) {}
    }

    @Override
    public void onSignUpButtonClick()
    {
        try
        {
            window.switchLayer(new SignUpScene(this));
            returnLayerRoadMap.push(new FirstScene(this));

            serverProvider.startAction(ServerContract.Operations.SIGN_UP);
        }
        catch (IOException ignored) {}
    }

    @Override
    public void onCreateCompanyButtonClick()
    {
        try
        {
            window.switchLayer(new CompanyCreateScene(this));
            returnLayerRoadMap.push(new FirstScene(this));

            serverProvider.startAction(ServerContract.Operations.CREATE_NEW_COMPANY);
        }
        catch (IOException ignored) {}
    }

    @Override
    public void onSetColorButtonDown(float[] color)
    {
        window.switchColor(color);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Sign-In View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onEndButtonClick(SignInScene.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField =
                    InputRestriction.textFieldIsValid(container.Password.toString()) &&
                    InputRestriction.textFieldIsValid(container.Login.toString());
            if(!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect Field Data";
                return;
            }

            serverProvider.send(new User(container.Login.toString(), container.Password.toString()));

             TransferMessageContainer answer = serverProvider.waitMessage();
            boolean isUserExist = answer.Key().equals(ServerContract.Result.SUCCESS_KEY);

            if(!isUserExist)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "user not found";
                return;
            }

            employee = answer.Employee;
            if(answer.Employee.getPosition().equals(Position.High))
                window.switchLayer(new AdminScene(this, employee.getPosition()));
            else
                window.switchLayer(new AccountValidityLayer(this));

            returnLayerRoadMap.push(new SignInScene(this));
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/SignInScene/" + e.getMessage());
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Create Company View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onEndCreateButtonClick(CompanyCreateScene.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField =
                    InputRestriction.textFieldIsNotEmpty(container.FirstName.toString()) &&
                            InputRestriction.textFieldIsNotEmpty(container.MiddleName.toString()) &&
                            InputRestriction.textFieldIsNotEmpty(container.LastName.toString()) &&
                            InputRestriction.ageFieldIsValid(container.Age.get()) &&
                            InputRestriction.textFieldIsValid(container.Password.toString()) &&
                            InputRestriction.textFieldIsValid(container.Login.toString()) &&
                            InputRestriction.textFieldIsNotEmpty(container.CompanyName.toString()) &&
                            InputRestriction.amountIsValid(container.StartBudget.get());

            if(!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect Field Data";
                return;
            }

            Employee employee = new Employee(
                    Position.High,
                    new Person(
                            container.FirstName.toString(),
                            container.MiddleName.toString(),
                            container.LastName.toString(),
                            container.Gender,
                            container.Age.get()
                    ),
                    new  User(
                            container.Login.toString(),
                            container.Password.toString(),
                            1
                    ),
                    new Company(
                            container.CompanyName.toString(),
                            container.StartBudget.get()
                    ));

            serverProvider.send(employee);
            TransferMessageContainer message = serverProvider.waitMessage();
            boolean isUserExist =
                    message.Key().equals(ServerContract.Result.FAILED_KEY) &&
                            message.message().equals(ServerContract.ResultDetails.USER_FAILED);
            boolean isCompanyAlreadyExist =
                    message.Key().equals(ServerContract.Result.FAILED_KEY) &&
                            message.message().equals(ServerContract.ResultDetails.COMPANY_FAILED);


            if(isCompanyAlreadyExist)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Company with this name already exists";
                return;
            }
            if(isUserExist)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "User with this login already exists";
                return;
            }

            this.employee = employee;
            window.switchLayer(new AdminScene(this, employee.getPosition()));
            returnLayerRoadMap.push(new CompanyCreateScene(this));
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/Create Company/" + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Exit View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onButtonExit()
    {
        try
        {
            serverProvider.startAction(ServerContract.Operations.EXIT);
            dispose();
        }
        catch (IOException e) {throw new RuntimeException(e);}

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Return View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onReturnButtonClick()
    {
        try
        {
            Layer layer = returnLayerRoadMap.pop();

            if(layer.getClass().equals(SignInScene.class))
                layer = new FirstScene(this);
            if(layer.getClass().equals(SignUpScene.class))
                layer = new FirstScene(this);
            if(layer.getClass().equals(CompanyCreateScene.class))
                layer = new FirstScene(this);
            if(layer.getClass().equals(AccountValidityLayer.class))
                layer = new FirstScene(this);


            serverProvider.startAction(new TransferMessageContainer(
                    ServerContract.Operations.RETURN,
                    ServerContract.Operations.RETURN
                    )
            );

            window.switchLayer(layer);
        }
        catch (IOException e) {throw new RuntimeException(e);}
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Sign-Up View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onEndRegistrationButtonClick(SignUpScene.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField =
                InputRestriction.textFieldIsNotEmpty(container.FirstName.toString()) &&
                InputRestriction.textFieldIsNotEmpty(container.MiddleName.toString()) &&
                InputRestriction.textFieldIsNotEmpty(container.LastName.toString()) &&
                InputRestriction.ageFieldIsValid(container.Age.get()) &&
                InputRestriction.textFieldIsValid(container.Password.toString()) &&
                InputRestriction.textFieldIsValid(container.Login.toString()) &&
                InputRestriction.textFieldIsNotEmpty(container.CompanyName.toString());

            if(!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect Field Data";
                return;
            }

            Employee employee = new Employee(
                    container.Position,
                    new Person(
                            container.FirstName.toString(),
                            container.MiddleName.toString(),
                            container.LastName.toString(),
                            container.Gender,
                            container.Age.get()
                    ),
                    new  User(
                            container.Login.toString(),
                            container.Password.toString()
                    ),
                    new Company(
                            container.CompanyName.toString(),
                            0
                    ));

            serverProvider.send(employee);
            TransferMessageContainer message = serverProvider.waitMessage();
            boolean isUserExist =
                    message.Key().equals(ServerContract.Result.FAILED_KEY) &&
                            message.message().equals(ServerContract.ResultDetails.USER_FAILED);
            boolean isCompanyNotExist =
                    message.Key().equals(ServerContract.Result.FAILED_KEY) &&
                            message.message().equals(ServerContract.ResultDetails.COMPANY_FAILED);


            if(isCompanyNotExist)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Company with this name not exists";
                return;
            }
            if(isUserExist)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "User with this login already exists";
                return;
            }

            this.employee = employee;
            window.switchLayer(new AccountValidityLayer(this));
            returnLayerRoadMap.push(new SignUpScene(this));
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/SignUnScene/" + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Account Validity View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void observeState()
    {
        try
        {
            serverProvider.startAction(new TransferMessageContainer(ServerContract.Operations.ACCOUNT_VALIDATE_INFO, employee.getUser().getLogin()));
            String keyResult = serverProvider.waitAction();
            if(keyResult.equals(ServerContract.Result.FAILED_KEY)) return;

            window.switchLayer(new AdminScene(this, employee.getPosition()));
            returnLayerRoadMap.push(new AccountValidityLayer(this));
        }
        catch (Exception ignored) { }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Admin View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onRequestsToJoinCompanyButtonClick(NotificationLayer notificationLayer)
    {
        try
        {
            serverProvider.startAction(new TransferMessageContainer(ServerContract.Operations.ACCOUNT_ACK, employee.getUser().getLogin()));
            TransferMessageContainer message = serverProvider.waitMessage();

            if(message.Key().equals(ServerContract.Result.FAILED_KEY))
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "no requests";
                return;
            }

            window.switchLayer(new ApproveAccountsListLayer(this, message.EmployeeList.value));
            returnLayerRoadMap.push(new AdminScene(this, employee.getPosition()));
        }
        catch (Exception ignored) { }
    }

    @Override
    public void onAccountSettingsButtonClick(NotificationLayer notificationLayer)
    {
        window.switchLayer(new AccountSettingsLayer(this));
        returnLayerRoadMap.push(new AdminScene(this, employee.getPosition()));
    }

    @Override
    public void onAccountInfoButtonClick()
    {
        window.switchLayer(new AccountInfoLayer(this, employee));
        returnLayerRoadMap.push(new AdminScene(this, employee.getPosition()));
    }

    @Override
    public void onGetAllEmployeesInfoButtonClick(NotificationLayer notificationLayer)
    {
        try
        {
            serverProvider.startAction(new TransferMessageContainer(
                    ServerContract.Operations.GET_ALL_EMPLOYEES,
                    employee.getUser().getLogin()
            ));
            TransferMessageContainer message = serverProvider.waitMessage();

            if(message.Key().equals(ServerContract.Result.FAILED_KEY))
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "not employees";
                return;
            }

            window.switchLayer(new EmployeesListLayer(this, message.EmployeeList.value));
            returnLayerRoadMap.push(new AdminScene(this, employee.getPosition()));
        }
        catch (Exception ignored) { }
    }

    @Override
    public void onBanAccountButtonClick(NotificationLayer notificationLayer)
    {
        try
        {
            serverProvider.startAction(new TransferMessageContainer(
                    ServerContract.Operations.GET_ALL_EMPLOYEES,
                    employee.getUser().getLogin()
            ));
            TransferMessageContainer message = serverProvider.waitMessage();

            List<Employee> employees = new ArrayList<Employee>();
            for (var item : message.EmployeeList.value)
            {
                if(item.getUser().getAccountValidity() != 0)
                    employees.add(item);
            }
            if(message.Key().equals(ServerContract.Result.FAILED_KEY) || employees.isEmpty())
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "only you are in the company";
                return;
            }


            window.switchLayer(new BanAccountsListLayer(this, employees));
            returnLayerRoadMap.push(new AdminScene(this, employee.getPosition()));
        }
        catch (Exception ignored) { }
    }

    @Override
    public void onSetPosition(NotificationLayer notificationLayer)
    {
        try
        {
            serverProvider.startAction(new TransferMessageContainer(
                    ServerContract.Operations.GET_ALL_EMPLOYEES,
                    employee.getUser().getLogin()
            ));
            TransferMessageContainer message = serverProvider.waitMessage();

            if (message.Key().equals(ServerContract.Result.FAILED_KEY))
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "only you are in the company";
                return;
            }

            window.switchLayer(new SetPositionLayer(this, message.EmployeeList.value));
            returnLayerRoadMap.push(new AdminScene(this, employee.getPosition()));
        }
        catch (Exception ignored) {}
    }

    @Override
    public void onAddTransactionButtonClick()
    {
        window.switchLayer(new AddTransactionLayer(this, employee.getCompany().getBudget()));
        returnLayerRoadMap.push(new AdminScene(this, employee.getPosition()));
    }

    @Override
    public void onBudgetAmountButtonClick()
    {
        try
        {
            serverProvider.startAction(new TransferMessageContainer(ServerContract.Operations.ALL_TRANSACTION, employee.getCompany().getName()));
            TransferMessageContainer dto = serverProvider.waitMessage();

            List<Transaction> transaction = dto.TransactionList.value;
            float[] commits =new float[transaction.size()];

            float budget = employee.getCompany().getBudget();

            for (int i = transaction.size() - 1; i >= 0; i--)
            {
                switch (transaction.get(i).getOperation())
                {
                    case Multiplication -> budget /= transaction.get(i).getValue();
                    case Assign -> budget = transaction.get(i).getValue();
                    case Addition -> budget -= transaction.get(i).getValue();
                }
                commits[i] = budget;
            }

            window.switchLayer(new BudgetAmountLayer(this, employee.getCompany(), commits));
            returnLayerRoadMap.push(new AdminScene(this, employee.getPosition()));
        }
        catch (Exception ignored) {}
    }

    @Override
    public void onTransactionListAmountButtonClick()
    {
        try
        {
            serverProvider.startAction(new TransferMessageContainer(
                    ServerContract.Operations.ALL_TRANSACTION,
                    employee.getCompany().getName()
            ));
            TransferMessageContainer dto = serverProvider.waitMessage();
            window.switchLayer(new TransactionListLayer(this, dto.TransactionList.value));
            returnLayerRoadMap.push(new AdminScene(this, employee.getPosition()));
        }
        catch (Exception ignored) {}
    }

    @Override
    public void onFileWriteButtonClick(NotificationLayer notificationLayer)
    {
        try
        {
            serverProvider.startAction(new TransferMessageContainer(
                    ServerContract.Operations.ALL_TRANSACTION,
                    employee.getCompany().getName()
            ));
            TransferMessageContainer dto = serverProvider.waitMessage();

            try(FileWriter writer = new FileWriter("notes.txt", true))
            {
                char[] breakLine = new char[50];
                Arrays.fill(breakLine, '=');
                breakLine[breakLine.length - 1] = '\n';
                writer.write(breakLine);
                writer.write(new Date(new java.util.Date().getTime()) + "\n");

                String budgetInfo =  employee.getCompany().getName() + " : " + employee.getCompany().getBudget() + "\n";
                writer.write(budgetInfo);

                for (var transaction : dto.TransactionList.value)
                    writer.write(transaction.toString() + "\n");

                writer.flush();

                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "info added in notes.txt file";
            }
            catch(IOException ignored) {}
        }
        catch (Exception ignored) {}
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Approve Accounts View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onApproveButtonClick(ApproveAccountsListLayer.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField = InputRestriction.amountIsValid(
                    container.SelectedUser.get(),
                    0,
                    container.EmployeeList.size() -1
            );

            if(!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect index input";
                return;
            }

            serverProvider.startAction(new TransferMessageContainer(
                    ServerContract.Operations.UN_BLOCK_ACCOUNT,
                    container.EmployeeList.get(container.SelectedUser.get()).getUser().getLogin()
            ));

            String operationResult = serverProvider.waitAction();
            if(operationResult.equals(ServerContract.Result.SUCCESS_KEY))
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Success";

                serverProvider.startAction(new TransferMessageContainer(ServerContract.Operations.ACCOUNT_ACK, employee.getUser().getLogin()));
                TransferMessageContainer message = serverProvider.waitMessage();

                if(message.Key().equals(ServerContract.Result.FAILED_KEY))
                {
                    notificationLayer.Message = "no requests";
                    window.switchLayer(new AdminScene(this, employee.getPosition()));
                    return;
                }
                container.EmployeeList = message.EmployeeList.value;
            }
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/Approve/" + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Accounts Setting View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onEditNameButtonClick(AccountSettingsLayer.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField =
                    InputRestriction.textFieldIsNotEmpty(container.FirstName.toString()) &&
                            InputRestriction.textFieldIsNotEmpty(container.MiddleName.toString()) &&
                            InputRestriction.textFieldIsNotEmpty(container.LastName.toString());

            if (!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect Field Data";
                return;
            }

            Employee employee = new Employee(
                    this.employee.getId(),
                    this.employee.getPosition(),
                    new Person(
                            this.employee.getPerson().getId(),
                            container.FirstName.toString(),
                            container.MiddleName.toString(),
                            container.LastName.toString(),
                            this.employee.getPerson().getGender(),
                            this.employee.getPerson().getAge()
                    ),
                    new User(
                            this.employee.getUser().getId(),
                            this.employee.getUser().getLogin(),
                            this.employee.getUser().getPassword(),
                            this.employee.getUser().getRegistrationDate(),
                            this.employee.getUser().getAccountValidity()
                    ),
                    new Company(
                            this.employee.getCompany().getId(),
                            this.employee.getCompany().getName(),
                            this.employee.getCompany().getBudget()
                    )
            );

            TransferMessageContainer dto = new TransferMessageContainer(ServerContract.Operations.UPDATE_ACCOUNT_INFO, "");
            dto.Employee = employee;
            serverProvider.startAction(dto);
            String message = serverProvider.waitAction();

            if(message.equals(ServerContract.Result.SUCCESS_KEY))
            {
                this.employee = employee;
            }
            else
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "ERROR";
                return;
            }
            container.nameFlag = false;
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/AccountSettings/" + e.getMessage());
        }
    }

    @Override
    public void onEditPasswordButtonClick(AccountSettingsLayer.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField =
                    InputRestriction.textFieldIsValid(container.Password.toString());

            if (!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect Field Data";
                return;
            }

            Employee employee = new Employee(
                    this.employee.getId(),
                    this.employee.getPosition(),
                    new Person(
                            this.employee.getPerson().getId(),
                            this.employee.getPerson().getFistName(),
                            this.employee.getPerson().getMiddleName(),
                            this.employee.getPerson().getLastName(),
                            this.employee.getPerson().getGender(),
                            this.employee.getPerson().getAge()
                    ),
                    new User(
                            this.employee.getUser().getId(),
                            this.employee.getUser().getLogin(),
                            container.Password.toString(),
                            this.employee.getUser().getRegistrationDate(),
                            this.employee.getUser().getAccountValidity()
                    ),
                    new Company(
                            this.employee.getCompany().getId(),
                            this.employee.getCompany().getName(),
                            this.employee.getCompany().getBudget()
                    )
            );

            TransferMessageContainer dto = new TransferMessageContainer(ServerContract.Operations.UPDATE_ACCOUNT_INFO, "");
            dto.Employee = employee;
            serverProvider.startAction(dto);
            String message = serverProvider.waitAction();

            if(message.equals(ServerContract.Result.SUCCESS_KEY))
            {
                this.employee = employee;
            }
            else
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "ERROR";
                return;
            }
            container.passFlag = false;
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/AccountSettings/" + e.getMessage());
        }
    }

    @Override
    public void onEditLoginButtonClick(AccountSettingsLayer.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField =
                    InputRestriction.textFieldIsValid(container.Login.toString());

            if (!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect Field Data";
                return;
            }

            Employee employee = new Employee(
                    this.employee.getId(),
                    this.employee.getPosition(),
                    new Person(
                            this.employee.getPerson().getId(),
                            this.employee.getPerson().getFistName(),
                            this.employee.getPerson().getMiddleName(),
                            this.employee.getPerson().getLastName(),
                            this.employee.getPerson().getGender(),
                            this.employee.getPerson().getAge()
                    ),
                    new User(
                            this.employee.getUser().getId(),
                            container.Login.toString(),
                            this.employee.getUser().getPassword(),
                            this.employee.getUser().getRegistrationDate(),
                            this.employee.getUser().getAccountValidity()
                    ),
                    new Company(
                            this.employee.getCompany().getId(),
                            this.employee.getCompany().getName(),
                            this.employee.getCompany().getBudget()
                    )
            );

            TransferMessageContainer dto = new TransferMessageContainer(ServerContract.Operations.UPDATE_ACCOUNT_INFO, "");
            dto.Employee = employee;
            serverProvider.startAction(dto);
            String message = serverProvider.waitAction();

            if(message.equals(ServerContract.Result.SUCCESS_KEY))
            {
                this.employee = employee;
            }
            else
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "ERROR";
                return;
            }
            container.loginFlag = false;
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/AccountSettings/" + e.getMessage());
        }
    }

    @Override
    public void onEditAgeNButtonClick(AccountSettingsLayer.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField =
                    InputRestriction.ageFieldIsValid(container.Age.get());

            if (!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect Field Data";
                return;
            }

            Employee employee = new Employee(
                    this.employee.getId(),
                    this.employee.getPosition(),
                    new Person(
                            this.employee.getPerson().getId(),
                            this.employee.getPerson().getFistName(),
                            this.employee.getPerson().getMiddleName(),
                            this.employee.getPerson().getLastName(),
                            this.employee.getPerson().getGender(),
                            container.Age.get()
                    ),
                    new User(
                            this.employee.getUser().getId(),
                            this.employee.getUser().getLogin(),
                            this.employee.getUser().getPassword(),
                            this.employee.getUser().getRegistrationDate(),
                            this.employee.getUser().getAccountValidity()
                    ),
                    new Company(
                            this.employee.getCompany().getId(),
                            this.employee.getCompany().getName(),
                            this.employee.getCompany().getBudget()
                    )
            );

            TransferMessageContainer dto = new TransferMessageContainer(ServerContract.Operations.UPDATE_ACCOUNT_INFO, "");
            dto.Employee = employee;
            serverProvider.startAction(dto);
            String message = serverProvider.waitAction();

            if(message.equals(ServerContract.Result.SUCCESS_KEY))
            {
                this.employee = employee;
            }
            else
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "ERROR";
                return;
            }
            container.ageFlag = false;
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/AccountSettings/" + e.getMessage());
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Ban Accounts View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onBanButtonClick(BanAccountsListLayer.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField = InputRestriction.amountIsValid(
                    container.SelectedUser.get(),
                    0,
                    container.EmployeeList.size() -1
            );

            if(!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect index input";
                return;
            }

            serverProvider.startAction(new TransferMessageContainer(
                    ServerContract.Operations.BLOCK_ACCOUNT,
                    container.EmployeeList.get(container.SelectedUser.get()).getUser().getLogin()
            ));

            String operationResult = serverProvider.waitAction();
            if(operationResult.equals(ServerContract.Result.SUCCESS_KEY))
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Success";

                serverProvider.startAction(
                        new TransferMessageContainer(ServerContract.Operations.GET_ALL_EMPLOYEES, employee.getUser().getLogin()));
                TransferMessageContainer message = serverProvider.waitMessage();

                List<Employee> employees = new ArrayList<Employee>();
                for (var item : message.EmployeeList.value)
                {
                    if(item.getUser().getAccountValidity() != 0)
                        employees.add(item);
                }

                if(message.Key().equals(ServerContract.Result.FAILED_KEY) || employees.isEmpty())
                {
                    notificationLayer.Message = "no requests";
                    window.switchLayer(new AdminScene(this, employee.getPosition()));
                    return;
                }
                container.EmployeeList = employees;
            }
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/Approve/" + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Set Position View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onSetButtonClick(SetPositionLayer.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField = InputRestriction.amountIsValid(
                    container.SelectedUser.get(),
                    0,
                    container.EmployeeList.size() -1
            );

            if(!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect index input";
                return;
            }

            Employee selectedEmp = container.EmployeeList.get(container.SelectedUser.get());
            selectedEmp.setPosition(container.Position);

            TransferMessageContainer dto =
                    new TransferMessageContainer(ServerContract.Operations.UPDATE_ACCOUNT_INFO, "");
            dto.Employee = selectedEmp;

            serverProvider.startAction(dto);

            String operationResult = serverProvider.waitAction();
            if(operationResult.equals(ServerContract.Result.SUCCESS_KEY))
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Success";

                serverProvider.startAction(new TransferMessageContainer(
                        ServerContract.Operations.GET_ALL_EMPLOYEES,
                        employee.getUser().getLogin()
                ));

                TransferMessageContainer message = serverProvider.waitMessage();
                if(message.Key().equals(ServerContract.Result.FAILED_KEY))
                {
                    notificationLayer.Message = "ERROR";
                    window.switchLayer(new AdminScene(this, employee.getPosition()));
                    return;
                }
                container.EmployeeList = message.EmployeeList.value;
            }
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/Approve/" + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  Add Transaction View
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onAddTransactionButton(AddTransactionLayer.Container container, NotificationLayer notificationLayer)
    {
        try
        {
            boolean isCorrectField =
                    InputRestriction.amountIsNotNegative(container.Value.get());

            if(!isCorrectField)
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Incorrect field value";
                return;
            }

            TransferMessageContainer dto = new TransferMessageContainer(
                    ServerContract.Operations.ADD_TRANSACTION,
                    employee.getCompany().getName()
            );
            switch (container.OperationType)
            {
                case Add -> dto.transaction = TransactionCreator.Add(
                        container.Condition,
                        container.Description.get(),
                        container.Value.get()
                );
                case Assign -> dto.transaction = TransactionCreator.Assign(
                        container.Condition,
                        container.Description.get(),
                        container.Value.get()
                );
                case Scale -> dto.transaction = TransactionCreator.Scale(
                        container.Condition,
                        container.Description.get(),
                        container.Value.get()
                );
                case Subtract -> dto.transaction = TransactionCreator.Subtract(
                        container.Condition,
                        container.Description.get(),
                        container.Value.get()
                );
            }

            serverProvider.startAction(dto);
            String operationResult = serverProvider.waitAction();

            if(operationResult.equals(ServerContract.Result.SUCCESS_KEY))
            {
                notificationLayer.NotificationFlag = true;
                notificationLayer.Message = "Success";

                window.switchLayer(new AdminScene(this, employee.getPosition()));
                employee.getCompany().setBudget(
                        TransactionApprover.ApplyTransaction(
                        dto.transaction,
                        employee.getCompany()
                ));
                return;
            }
            notificationLayer.Message = "transaction cant perform";
            window.switchLayer(new AdminScene(this, employee.getPosition()));
        }
        catch (Exception e)
        {
            System.out.println(ClientLoop.class + "/Add transaction/" + e.getMessage());
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  All Transaction List
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void onUpdateButtonClick(TransactionListLayer.Container container, NotificationLayer notificationLayer)
    {

        try
        {
            switch (container.Operation)
            {
                case TransactionListLayer.Container.OperationType.ALL ->
                {
                    serverProvider.startAction(new TransferMessageContainer(ServerContract.Operations.ALL_TRANSACTION, employee.getCompany().getName()));
                    TransferMessageContainer dto = serverProvider.waitMessage();
                    container.TransactionList = dto.TransactionList.value;
                }
                case TransactionListLayer.Container.OperationType.BY_DATE ->
                {
                    boolean isCorrectField = InputRestriction.dateIsValid(container.Year.get(), container.Month.get(), container.Day.get());
                    List<Transaction> transactions = new ArrayList<Transaction>();

                    if(!isCorrectField)
                    {
                        notificationLayer.NotificationFlag = true;
                        notificationLayer.Message = "incorrect date";
                        return;
                    }

                    for (var item : container.AllTransactionList)
                    {
                        Date inputDate = new Date(container.Year.get(), container.Month.get(), container.Day.get());
                        if (item.getDate().after(inputDate)) transactions.add(item);
                    }
                    container.TransactionList = transactions;
                }
                case TransactionListLayer.Container.OperationType.BY_CONDITION ->
                {
                    List<Transaction> transactionList = new ArrayList<Transaction>();
                    for (var item : container.AllTransactionList)
                    {
                        if (item.getCondition().equals(container.Condition)) transactionList.add(item);
                    }
                    container.TransactionList = transactionList;
                }
            }
        }

        catch (IOException | ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}
