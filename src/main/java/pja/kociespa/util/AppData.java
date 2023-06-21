package pja.kociespa.util;

import javafx.collections.ObservableList;
import javafx.util.Pair;
import org.hibernate.Session;
import pja.kociespa.model.*;

import java.sql.Date;
import java.time.LocalDate;

import static pja.kociespa.util.HibernateUtil.getSessionFactory;

/**
 * A singleton class to store data and share it across scenes
 */
public class AppData {
    private static AppData instance;

    private Cat cat;
    private Caretaker caretaker;
    private ServiceClass serviceClass;
    private LocalDate startDate;
    private LocalDate endDate;

    // The actor and user in this use case
    private Receptionist receptionist;

    private ObservableList<Pair<Treatment, Employee>> treatmentsAndEmployees;

    private AppData() {
        // Private constructor to enforce singleton pattern
    }

    public static AppData getInstance() {
        if (instance == null) {
            instance = new AppData();
        }
        return instance;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public Caretaker getCaretaker() {
        return caretaker;
    }

    public void setCaretaker(Caretaker caretaker) {
        this.caretaker = caretaker;
    }

    public ServiceClass getServiceClass() {
        return serviceClass;
    }

    public void setServiceClass(ServiceClass serviceClass) {
        this.serviceClass = serviceClass;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public ObservableList<Pair<Treatment, Employee>> getTreatmentsAndEmployees() {
        return treatmentsAndEmployees;
    }

    public void setTreatmentsAndEmployees(ObservableList<Pair<Treatment, Employee>> treatmentsAndEmployees) {
        this.treatmentsAndEmployees = treatmentsAndEmployees;
    }

    public Receptionist getReceptionist() {
        return receptionist;
    }

    // Not making use of the parameter, because we assume
    // that the only existing receptionist is the current "acting" user
    public void setReceptionist() {
        try (Session session = getSessionFactory().openSession()) {
            this.receptionist = session.createQuery("from Receptionist", Receptionist.class).getSingleResult();
        }
    }
}
