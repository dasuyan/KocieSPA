package pja.kociespa.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Receptionist extends Employee {
    private static int workingHours = 4;

    @Column(nullable = false)
    private int workStartHour;

    @Column(nullable = false)
    private int workEndHour;

    @OneToMany(mappedBy = "receptionist", cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private List<Stay> stays = new ArrayList<>();

    public Receptionist(String name, String surname, String sex, String phoneNumber, Date birthDate, String pesel, Date hireDate,
                        int workStartHour) {
        super(name, surname, sex, phoneNumber, birthDate, pesel, hireDate);
        this.workStartHour = workStartHour;
        this.workEndHour = getWorkEndHour();
    }

    protected Receptionist() {}

    public int getWorkingHours() {
        return workingHours;
    }

    public static void setWorkingHours(int workingHours) {
        Receptionist.workingHours = workingHours;
    }

    public int getWorkStartHour() {
        return workStartHour;
    }

    public void setWorkStartHour(int workStartHour) {
        this.workStartHour = workStartHour;
    }

    public int getWorkEndHour() {
        int workEndHour = this.workStartHour + workingHours;
        if (workEndHour > 24) {
            workEndHour =- 24;
        }
        return workEndHour;
    }

    public void setWorkEndHour(int workEndHour) {
        this.workEndHour = workEndHour;
    }

    @Override
    public void displayFullEmployeeInfo() {

    }
}
