package Server.Services.Transaction;

import Server.Entities.StaticEnumerable.Condition;
import Server.Entities.StaticEnumerable.Operation;
import Server.Entities.Transaction;

import java.sql.Date;

public class TransactionCreator
{

    public static Transaction Add(Condition condition, String description, float value)
    {
        if(value <= 0 ) throw new RuntimeException();

        Transaction transaction = create(condition, description);
        transaction.setOperation(Operation.Addition);
        transaction.setValue(value);

        return  transaction;
    }
    public static Transaction Subtract(Condition condition, String description, float value)
    {
        if(value <= 0 ) throw new RuntimeException();

        Transaction transaction = create(condition, description);
        transaction.setOperation(Operation.Addition);
        transaction.setValue(-value);

        return  transaction;
    }

    public static Transaction Scale(Condition condition, String description, float value)
    {
        if(value <= 0 ) throw new RuntimeException();

        Transaction transaction = create(condition, description);
        transaction.setOperation(Operation.Multiplication);
        transaction.setValue(value);

        return  transaction;
    }

    public static Transaction Assign(Condition condition, String description, float value)
    {
        if(value < 0 ) throw new RuntimeException();

        Transaction transaction = create(condition, description);
        transaction.setOperation(Operation.Assign);
        transaction.setValue(value);

        return  transaction;
    }

    private static Transaction create(Condition condition, String description)
    {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setCondition(condition);
        transaction.setDate(new Date(new java.util.Date().getTime()));
        return transaction;
    }


    public enum OperationType
    {
        Assign,
        Add,
        Subtract,
        Scale
    }
}


