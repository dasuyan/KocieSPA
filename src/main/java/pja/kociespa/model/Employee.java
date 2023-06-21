package pja.kociespa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Employee extends Person {
    @Column(nullable = false)
    protected Date birthDate;
    @Column(nullable = false)
    protected long age;
    @Column(nullable = false)
    protected String pesel;
    @Column(nullable = false)
    protected Date hireDate;
    @Column(nullable = false)
    protected long tenure; // in months

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Employee_Stay",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "stay_id")
    )
    private List<Stay> stays = new ArrayList<>();


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Employee_Treatment",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "treatment_id")
    )
    private List<Treatment> treatments = new ArrayList<>();

    /**
     * If we remove an employee from the database, we remove their certificates as well.
     */
    @OneToMany(mappedBy="employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Certificate> certificates = new ArrayList<>();

    public Employee(String name, String surname, String sex, String phoneNumber,
                    Date birthDate, String pesel, Date hireDate) {
        super(name, surname, sex, phoneNumber);
        this.birthDate = birthDate;
        this.age = getAge();
        this.pesel = pesel;
        this.hireDate = hireDate;
        this.tenure = getTenure();
    }

    protected Employee() {
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public long getAge() {
        LocalDate currentDate = LocalDate.now();
        return ChronoUnit.YEARS.between(birthDate.toLocalDate(), currentDate);
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public long getTenure() {
        LocalDate currentDate = LocalDate.now();
        return ChronoUnit.MONTHS.between(hireDate.toLocalDate(), currentDate);
    }

    public List<Stay> getStays() {
        return stays;
    }

    public void addStay(Stay stay) {
        if (!stays.contains(stay)) {
            stays.add(stay);
            stay.addEmployee(this);
        }
    }

    public void removeStay(Stay stay) {
        if (stays.contains(stay)) {
            stays.remove(stay);
            stay.removeEmployee(this);
        }
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }
    public void addTreatment(Treatment treatment) {
        if (!treatments.contains(treatment)) {
            treatments.add(treatment);
            treatment.addEmployee(this);
        }
    }

    public void removeTreatment(Treatment treatment) {
        if (treatments.contains(treatment)) {
            treatments.remove(treatment);
            treatment.removeEmployee(this);
        }
    }

    public abstract void displayFullEmployeeInfo();

    @Override
    public String toString() {
        return name + " " + surname;
    }
}