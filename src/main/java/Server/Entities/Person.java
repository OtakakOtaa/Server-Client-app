package Server.Entities;

import Server.Entities.StaticEnumerable.Gender;

import javax.persistence.*;
import java.io.Serializable;

@Entity @Table(name = "person")
public class Person implements Serializable
{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private int id;

    @Column(name = "fist_name")
    private String fistName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "gender")
    private Gender gender;
    @Column(name = "age")
    private int age;

    public Person() {}

    public Person(String fistName, String middleName, String lastName, Gender gender, int age)
    {
        this.fistName = fistName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
    }
    public Person(int id, String fistName, String middleName, String lastName, Gender gender, int age)
    {
        this.id = id;
        this.fistName = fistName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
    }


    public int getId() { return id; }
    public String getFistName() { return fistName; }

    public String getMiddleName() { return middleName; }

    public String getLastName() { return lastName; }

    public Gender getGender() { return gender; }

    public int getAge() { return age; }

    public void setFistName(String fistName) { this.fistName = fistName; }

    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setGender(Gender gender) { this.gender = gender; }

    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", fistName='" + fistName + '\'' +
                ", middleName='" + middleName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", age=" + age +
                '}';
    }

}
