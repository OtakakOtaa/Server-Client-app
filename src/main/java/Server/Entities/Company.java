package Server.Entities;
import javax.persistence.*;
import java.io.Serializable;

@Entity @Table(name = "company")
public class Company implements Serializable
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "budget")
    private float budget;


    public Company(){}

    public Company(String name, float budget)
    {
        this.name = name;
        this.budget = budget;
    }

    public Company(int id, String name, float budget)
    {
        this.id = id;
        this.name = name;
        this.budget = budget;
    }


    public void setBudget(float budget) { this.budget = budget; }

    public void setName(String name) { this.name = name; }

    public float getBudget() { return budget; }

    public String getName() { return name; }

    public int getId() { return id; }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", budget=" + budget +
                '}';
    }
}
