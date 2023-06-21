package pja.kociespa.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Stay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceClass serviceClass;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StayStatus stayStatus = StayStatus.BOOKED;

    @Column(nullable = false)
    private Double totalPrice = 0.0;

    @ManyToMany(mappedBy = "stays", fetch = FetchType.EAGER)
    private List<Employee> employees = new ArrayList<>();

    @ManyToMany(mappedBy = "stays", fetch = FetchType.EAGER)
    private List<Treatment> treatments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @ManyToOne
    @JoinColumn(name = "caretaker_id")
    private Caretaker caretaker;

    @ManyToOne
    @JoinColumn(name = "receptionist_id")
    private Receptionist receptionist;


    public Stay(Date startDate, Date endDate, ServiceClass serviceClass) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.serviceClass = serviceClass;
    }

    protected Stay() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ServiceClass getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(ServiceClass serviceClass) {
        this.serviceClass = serviceClass;
    }

    public StayStatus getStayStatus() {
        return stayStatus;
    }

    public void setStayStatus(StayStatus stayStatus) {
        this.stayStatus = stayStatus;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void addTreatment(Treatment treatment) {
        if (!treatments.contains(treatment)) {
            treatments.add(treatment);
            treatment.addStay(this);
        }
    }

    public void removeTreatment(Treatment treatment) {
        if (treatments.contains(treatment)) {
            treatments.remove(treatment);
            treatment.removeStay(this);
        }
    }

    public void addEmployee(Employee employee) {
        if (!employees.contains(employee)) {
            employees.add(employee);
            employee.addStay(this);
        }
    }

    public void removeEmployee(Employee employee) {
        if (employees.contains(employee)) {
            employees.remove(employee);
            employee.removeStay(this);
        }
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        if (cat != null && this.cat != cat) {
            this.cat = cat;
            cat.addStay(this);
        }
    }

    public Caretaker getCaretaker() {
        return caretaker;
    }

    public void setCaretaker(Caretaker caretaker) {
        if (caretaker != null && this.caretaker != caretaker) {
            this.caretaker = caretaker;
            caretaker.addStay(this);
        }
    }

    public void setReceptionist(Receptionist receptionist) {
        if (receptionist != null && this.receptionist != receptionist) {
            this.receptionist = receptionist;
            receptionist.addStay(this);
        }
    }
}
