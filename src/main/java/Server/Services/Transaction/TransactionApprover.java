package Server.Services.Transaction;

import Infrastructure.DiContainer.Annotations.Construct;
import Server.Entities.Company;
import Server.Entities.History;
import Server.Entities.Transaction;
import Server.PersistentProvider.PersistentProvider;
import Server.ServerContract;
import Server.Session.SessionSteam;
import Server.TransferMessageContainer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransactionApprover
{
     private final PersistentProvider persistentProvider;

     @Construct
    public TransactionApprover(PersistentProvider persistentProvider)
    {
        this.persistentProvider = persistentProvider;
    }

    public void approveTransaction(SessionSteam sessionSteam, String companyName, Transaction transaction)
    {
        try
        {
            Company company = persistentProvider.getCompanyByName(companyName);
            boolean isCompanyExist = company != null;

            assert company != null;
            boolean isTransactionValid = tryApplyTransaction(transaction, company);

            if(isTransactionValid)
            {
                persistentProvider.addTransaction(transaction, company);
                persistentProvider.updateCompany(company);
                sessionSteam.startAction(ServerContract.Result.SUCCESS_KEY);
                return;
            }
            sessionSteam.startAction(ServerContract.Result.FAILED_KEY);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void sendAllTransaction(SessionSteam sessionSteam, String companyName)
    {
        try
        {
            Company company = persistentProvider.getCompanyByName(companyName);
            List<Transaction> transactions = new ArrayList<Transaction>();

            assert company != null;
            List<History> histories = persistentProvider.allHistory();

            for (var item : histories)
                if(item.getCompany().getName().equals(company.getName()))
                    transactions.add(item.getTransaction());


            if(transactions.isEmpty())
            {
                sessionSteam.startAction(ServerContract.Result.FAILED_KEY);
                return;
            }

            TransferMessageContainer dto = new TransferMessageContainer(ServerContract.Result.SUCCESS_KEY, "");
            dto.TransactionList = new Server.TransferMessageContainer.TransactionList(transactions);
            sessionSteam.startAction(dto);

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static boolean tryApplyTransaction(Transaction transaction, Company company)
    {
        float amount = ApplyTransaction(transaction, company);
        boolean isBudgetValid = amount >= 0;

        if(isBudgetValid) company.setBudget(amount);
        return isBudgetValid;
    }

    public static float ApplyTransaction(Transaction transaction, Company company)
    {
        float budget = company.getBudget();
        switch (transaction.getOperation())
        {
            case Assign -> budget = transaction.getValue();
            case Addition -> budget += transaction.getValue();
            case Multiplication -> budget *= transaction.getValue();
        }
        return budget;
    }
}
