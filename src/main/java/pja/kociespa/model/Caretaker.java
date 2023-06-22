package pja.kociespa.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Caretaker extends Employee {
    @OneToMany(mappedBy = "caretaker", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Stay> stays = new ArrayList<>();

    public Caretaker(String name, String surname, String sex, String phoneNumber, Date birthDate, String pesel, Date hireDate) {
        super(name, surname, sex, phoneNumber, birthDate, pesel, hireDate);
    }

    protected Caretaker() {

    }

    @Override
    public void displayFullEmployeeInfo() {

    }
}
