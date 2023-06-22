package pja.kociespa.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Behaviorist extends Employee {
    @Column(nullable = false)
    private boolean handlesAggressiveCats;

    @OneToMany(mappedBy = "behaviorist", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Cat> cats = new ArrayList<>();

    public Behaviorist(String name, String surname, String sex, String phoneNumber, Date birthDate, String pesel, Date hireDate, boolean handlesAggressiveCats) {
        super(name, surname, sex, phoneNumber, birthDate, pesel, hireDate);
        this.handlesAggressiveCats = handlesAggressiveCats;
    }

    protected Behaviorist() {

    }

    public boolean handlesAggressiveCats() {
        return handlesAggressiveCats;
    }

    public void setHandlesAggressiveCats(boolean handlesAggressiveCats) {
        this.handlesAggressiveCats = handlesAggressiveCats;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }

    public void addCat(Cat cat) {
        if (!cats.contains(cat)) {
            cats.add(cat);
            cat.setBehaviorist(this);
        }
    }

    @Override
    public void displayFullEmployeeInfo() {
    }

}
