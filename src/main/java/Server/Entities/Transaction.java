package Server.Entities;

import Server.Entities.StaticEnumerable.Condition;
import Server.Entities.StaticEnumerable.Operation;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Entity @Table(name = "transaction")
public class Transaction implements Serializable
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private int id;

    @Column(name = "commit_date")
    private Date date;
    @Column(name = "description")
    private String description;
    @Column(name = "condition_1")
    private Condition condition;
    @Column(name = "value")
    private float value;
    @Column(name = "operation")
    private Operation operation;

    public Transaction() {}

    public Transaction(String description, Condition condition, float value, Operation operation)
    {
        this.description = description;
        this.condition = condition;
        this.value = value;
        this.operation = operation;

        var currentDate = new java.util.Date();
        date = new Date(currentDate.getTime());
    }

    public String getDescription() { return description; }

    public Condition getCondition() { return condition; }

    public float getValue() { return value; }

    public Operation getOperation() { return operation; }
    public Date getDate() { return date; }



    public void setId(int id) { this.id = id; }

    public void setDate(Date date) { this.date = date; }

    public void setDescription(String description) { this.description = description; }

    public void setCondition(Condition condition) { this.condition = condition; }

    public void setValue(float value) { this.value = value; }

    public void setOperation(Operation operation) { this.operation = operation; }



    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", condition=" + condition +
                ", value=" + value +
                ", operation=" + operation +
                '}';
    }
}
