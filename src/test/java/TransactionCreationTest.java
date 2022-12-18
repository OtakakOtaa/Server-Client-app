import Server.Entities.StaticEnumerable.Condition;
import Server.Entities.StaticEnumerable.Operation;
import Server.Entities.Transaction;
import Server.Services.Transaction.TransactionCreator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

import java.util.Arrays;
import java.util.List;

@RunWith(Theories.class)
public class TransactionCreationTest
{
    @Test
    public void isTransactionCreatedCorrect()
    {
        // A
        int value = 100;
        Condition condition = Condition.Investment;
        String description = " -)_0";
        Operation expectedOperation = Operation.Addition;


        // Action
        Transaction transaction = TransactionCreator.Add(condition, description, value);

        boolean expected = true;
        boolean actual = transaction.getCondition().equals(condition) &&
                transaction.getValue() == value &&
                transaction.getDescription().equals(description) &&
                transaction.getOperation().equals(expectedOperation);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Theory
    public void isInvalidTransactionApproved(final Object... testData)
    {
        try
        {
            switch ((String) testData[1])
            {
                case "Add" -> TransactionCreator.Add(Condition.Deal, " ", (float) testData[0]);
                case "Sub" -> TransactionCreator.Subtract(Condition.Deal, " ", (float) testData[0]);
                case "Scale" -> TransactionCreator.Scale(Condition.Deal, " ", (float) testData[0]);
                case "Assig"-> TransactionCreator.Assign(Condition.Deal, " ", (float) testData[0]);
            }
        }
        catch (Exception e)
        {
            Assert.assertEquals(e.getClass(), RuntimeException.class);
        }
    }

    @DataPoints
    public static List<Object[]> incorrectDataSource() {
        return Arrays.asList(new Object[][] {
                { -100.0f, "Add"},
                { -100.0f, "Sud"},
                { 0.0f, "Scale"},
                { 0.0f, "Add"},
                { 0.0f, "Sub"},
                { -100.0f, "Scale"},
                { -100.0f, "Assig"},
        });
    }


}
