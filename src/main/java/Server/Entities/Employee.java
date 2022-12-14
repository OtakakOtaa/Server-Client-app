package Server.Entities;

import Server.Entities.StaticEnumerable.Position;

import javax.persistence.*;
import java.io.Serializable;

@Entity @Table(name = "employee")
public class Employee implements Serializable
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private int id;
    @Column(name = "position")
    private Position position;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Person person;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Company company;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private User user;

    public Employee(){}

    public Employee(Position position, Person person, User user, Company company)
    {
        this.position = position;
        this.user = user;
        this.company = company;
        this.person = person;
    }
    public Employee(int id, Position position, Person person, User user, Company company)
    {
        this.id = id;
        this.position = position;
        this.user = user;
        this.company = company;
        this.person = person;
    }

    public Position getPosition() { return position; }

    public int getId() {return  id; }

    public Person getPerson() { return person; }

    public User getUser() { return user; }

    public Company getCompany() { return company; }

    public void setPosition(Position position) { this.position = position; }

    @Override
    public String toString()
    {
        return "Employee{" +
                "id=" + id +
                ", position=" + position +
                ", person=" + person +
                ", user=" + user +
                '}';
    }


}
