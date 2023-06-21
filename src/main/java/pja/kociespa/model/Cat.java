package pja.kociespa.model;

import jakarta.persistence.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Cat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Date birthDate;
    @Column(nullable = false)
    private int age;
    @Column(nullable = false)
    private String sex;
    @Column(nullable = false)
    private String breed;

    @OneToMany(mappedBy = "cat", fetch = FetchType.EAGER)
    private List<Stay> stays = new ArrayList<>();

    /**
     * If we remove a cat from the database, the cat's personal belongings will also be removed.
     */
    @OneToMany(mappedBy = "cat", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<PersonalBelonging> personalBelongings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "behaviorist_id")
    private Behaviorist behaviorist;

    public Cat(String name, Date birthDate, String sex, String breed) {
        this.name = name;
        this.birthDate = birthDate;
        this.age = getAge();
        this.sex = sex;
        this.breed = breed;
    }
    protected Cat() {

    }

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

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthDate.toLocalDate(), currentDate).getYears();
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public Customer getCustomer() {
        return customer;
    }
    public void setCustomer(Customer customer) {
        if (customer != null && this.customer != customer) {
            this.customer = customer;
            customer.addCat(this);
        }
    }

    public Behaviorist getBehaviorist() {
        return behaviorist;
    }

    public void setBehaviorist(Behaviorist behaviorist) {
        if (behaviorist != null && this.behaviorist != behaviorist) {
            this.behaviorist = behaviorist;
            behaviorist.addCat(this);
        }
    }

    public List<Stay> getStays() {
        return stays;
    }
    public void addStay(Stay stay) {
        if (!stays.contains(stay)) {
            stays.add(stay);
            stay.setCat(this);
        }
    }

    public void removeStay(Stay stay) {
        if (stays.contains(stay)) {
            stays.remove(stay);
            stay.setCat(null);
        }
    }

    @Override
    public String toString() {
        return name + " (owner: " + customer.getName() + " " + customer.getSurname() + ")";
    }
}
