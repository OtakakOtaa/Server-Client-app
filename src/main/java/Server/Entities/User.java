package Server.Entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.util.Objects;

@Entity
@Table(name = "user")
public class User implements Serializable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "login")
    private String login;
    @Column(name = "password")
    private String password;
    @Column(name = "registration_date")
    private Date registrationDate;

    @Column(name = "account_validity")
    private int accountValidity;

    public User()
    {
    }

    public User(String login, String password)
    {
        this.login = login;
        this.password = password;
        accountValidity = 0;

        var currentDate = new java.util.Date();
        registrationDate = new Date(currentDate.getTime());
    }

    public User(String login, String password, Date registrationDate)
    {
        this.login = login;
        this.password = password;
        accountValidity = 0;
        this.registrationDate = registrationDate;
    }

    public User(String login, String password, Date registrationDate , int accountValidity)
    {
        this.login = login;
        this.password = password;
        this.accountValidity = accountValidity;
        this.registrationDate = registrationDate;
    }
    public User(int id, String login, String password, Date registrationDate , int accountValidity)
    {
        this.id = id;
        this.login = login;
        this.password = password;
        this.accountValidity = accountValidity;
        this.registrationDate = registrationDate;
    }

    public User(String login, String password, int accountValidity)
    {
        this.login = login;
        this.password = password;
        this.accountValidity = accountValidity;
        var currentDate = new java.util.Date();
        registrationDate = new Date(currentDate.getTime());
    }


    public String getLogin()
    {
        return login;
    }

    public String getPassword()
    {
        return password;
    }

    public Date getRegistrationDate()
    {
        return registrationDate;
    }

    public int getId()
    {
        return id;
    }


    public void setLogin(String login)
    {
         this.login = login;
    }

    public void setPassword(String password)
    {
         this.password = password;
    }

    public void setRegistrationDate(Date date)
    {
        this.registrationDate = date;
    }
    
    @Override

    public boolean equals(Object o)
    {
        User user = (User) o;
        return login.equals(user.getLogin()) && password.equals(user.getPassword());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(login, password);
    }

    @Override
    public String toString()
    {
        return "User{" + "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password +
                '\'' + ", registrationDate=" +
                registrationDate +
                ", accountValidity=" +
                accountValidity + '}';
    }

    public int getAccountValidity()
    {
        return accountValidity;
    }
}