package pja.kociespa.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Treatment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Double price;

    @ManyToMany(mappedBy = "treatments", fetch = FetchType.EAGER)
    private List<Employee> employees = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Treatment_Stay",
            joinColumns = @JoinColumn(name = "treatment_id"),
            inverseJoinColumns = @JoinColumn(name = "stay_id")
    )
    private List<Stay> stays = new ArrayList<>();

    public Treatment(String name, String description, Double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }

    protected Treatment() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<Stay> getStays() {
        return stays;
    }

    public void addStay(Stay stay) {
        if (!stays.contains(stay)) {
            stays.add(stay);
            stay.addTreatment(this);
        }
    }

    public void removeStay(Stay stay) {
        if (stays.contains(stay)) {
            stays.remove(stay);
            stay.removeTreatment(this);
        }
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            employees.add(employee);
            employee.addTreatment(this);
        }
    }

    public void removeEmployee(Employee employee) {
        if (employees.contains(employee)) {
            employees.remove(employee);
            employee.removeTreatment(this);
        }
    }

    @Override
    public String toString() {
        return name + " " + price;
    }
}
