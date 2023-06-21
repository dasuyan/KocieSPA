package pja.kociespa.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Customer extends Person {
    @Column(nullable = false)
    private String emailAddress;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Cat> cats = new ArrayList<>();

    public Customer(String name, String surname, String sex, String phoneNumber, String emailAddress) {
        super(name, surname, sex, phoneNumber);
        this.emailAddress = emailAddress;
    }

    protected Customer() {

    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public List<Cat> getCats() {
        return cats;
    }
    public void addCat(Cat cat) {
        if (!cats.contains(cat)) {
            cats.add(cat);
            cat.setCustomer(this);
        }
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}