package pja.kociespa.util;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import pja.kociespa.model.*;

import java.sql.Date;

/**
 * Class that holds a static SessionFactory, allows to open sessions and populate database with test data.
 */
public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                sessionFactory = new Configuration()
                        .configure("hibernate.cfg.xml")
                        .buildSessionFactory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sessionFactory;
    }
    public static void createTestData() {
        try (Session session = getSessionFactory().openSession()) {

            session.beginTransaction();

            // Cats and their required associations
            Customer customer1 = new Customer("Mateusz", "Drabarek", "male", "797120633", "mdrabarek@gmail.com");
            Customer customer2 = new Customer("Artur", "Konopka", "male", "123456789", "akonopka@gmail.com");
            Customer customer3 = new Customer("Julia", "Rej", "female", "555666777", "jrej@gmail.com");

            Behaviorist behaviorist1 = new Behaviorist("Jan", "Żyto", "male", "123456789", Date.valueOf("2000-01-11"), "98564376542", Date.valueOf("2020-03-20"), true);
            Behaviorist behaviorist2 = new Behaviorist("Ola", "Warczygłowa", "female", "987654321", Date.valueOf("1920-11-24"), "09897865461", Date.valueOf("2014-06-05"), false);

            Cat cat1 = new Cat("Pruti", Date.valueOf("2020-03-20"), "female", "Tricolore");
            cat1.setCustomer(customer1);
            cat1.setBehaviorist(behaviorist1);

            Cat cat2 = new Cat("Leo", Date.valueOf("2019-02-01"), "male", "Orange tabby");
            cat2.setCustomer(customer2);
            cat2.setBehaviorist(behaviorist2);

            Cat cat3 = new Cat("Edek", Date.valueOf("2015-10-30"), "male", "Big boss");
            cat3.setCustomer(customer3);
            cat3.setBehaviorist(behaviorist2);

            // Caretakers
            Caretaker caretaker1 = new Caretaker("Oompa", "Loompa", "male", "123456789", Date.valueOf("2000-01-11"), "98564376542", Date.valueOf("2020-03-20"));
            Caretaker caretaker2 = new Caretaker("Kim", "Tong", "female", "123456789", Date.valueOf("2000-01-11"), "98564376542", Date.valueOf("2020-03-20"));
            Caretaker caretaker3 = new Caretaker("John", "Bartko", "male", "123456789", Date.valueOf("2000-01-11"), "98564376542", Date.valueOf("2020-03-20"));

            // Treatments
            Treatment treatment1 = new Treatment("Manicure", "Precise nail trimming and cleaning", 100.0);
            Treatment treatment2 = new Treatment("Head massage", "Thorough and delicate head massage with scratching in all the right places", 50.0);
            Treatment treatment3 = new Treatment("Fur trim", "First a careful wash in lukewarm water, then a professional haircut", 350.0);

            treatment1.addEmployee(caretaker1);
            treatment1.addEmployee(caretaker2);

            treatment2.addEmployee(caretaker2);
            treatment2.addEmployee(caretaker3);

            treatment3.addEmployee(caretaker3);
            treatment3.addEmployee(caretaker1);

            // Receptionist (the actor for the main use case as well)
            Receptionist receptionist = new Receptionist("Sephiroth", "Cloud", "male", "123456789", Date.valueOf("2000-01-11"), "98564376542", Date.valueOf("2020-03-20"), 10);

            // exemplary Stay
            Stay stay = new Stay(Date.valueOf("2020-03-20"), Date.valueOf("2020-03-25"), ServiceClass.CARE_PLUS);
            stay.addTreatment(treatment1);
            stay.addTreatment(treatment2);
            stay.addEmployee(caretaker1);
            stay.addEmployee(caretaker2);
            stay.setCat(cat1);
            stay.setCaretaker(caretaker3);
            stay.setReceptionist(receptionist);
            stay.setTotalPrice(500.0);

            // Persist
            session.persist(customer1);
            session.persist(customer2);
            session.persist(customer3);

            session.persist(behaviorist1);
            session.persist(behaviorist2);

            session.persist(cat1);
            session.persist(cat2);
            session.persist(cat3);

            session.persist(treatment1);
            session.persist(treatment2);
            session.persist(treatment3);

            session.persist(caretaker1);
            session.persist(caretaker2);
            session.persist(caretaker3);

            session.persist(receptionist);

            session.persist(stay);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
