package Server.Entities;

import javax.persistence.*;

@Entity @Table(name = "history")
public class History
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Company company;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Transaction transaction;

    public History(){}

    public History(Company company, Transaction transaction)
    {
        this.company = company;
        this.transaction = transaction;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }


    @Override
    public String toString()
    {
        return "History{" +
                "id=" + id +
                ", company=" + company +
                ", transaction=" + transaction +
                '}';
    }
}
