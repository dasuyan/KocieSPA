package pja.kociespa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
@MappedSuperclass
public abstract class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false)
    protected String name;

    @Column(nullable = false)
    protected String surname;
    @Column(nullable = false)
    protected String sex;

    @Column(nullable = false)
    protected String phoneNumber;

    public Person(String name, String surname, String sex, String phoneNumber) {
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.phoneNumber = phoneNumber;
    }

    protected Person(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}