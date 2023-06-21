package pja.kociespa;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

import org.hibernate.cfg.Configuration;
import pja.kociespa.model.Employee;

public class Main {
    public static void main(String[] args) {
        // Konfiguracja Hibernate
        Session session;
        try (SessionFactory sessionFactory = new Configuration()
                .configure("hibernate.cfg.xml")
                .buildSessionFactory()) {

            // Otwarcie sesji
            session = sessionFactory.openSession();
        }

        try {
            // Rozpoczęcie transakcji
            session.beginTransaction();

            // Wykonanie zapytania SELECT
            //   List<RetailCustomer> customers = session.createQuery("FROM RetailCustomer ", RetailCustomer.class).getResultList();
            //  List<Person> perosns = session.createQuery("FROM Person ", Person.class).getResultList();
            //  List<Employee> e = session.createQuery("FROM Employee", Employee.class).getResultList();
            // List<Specialization> s = session.createQuery("FROM Specialization ", Specialization.class).getResultList();
            // List<License> licenses = session.createQuery("FROM License ", License.class).getResultList();
            // List<Assembly> aServices = session.createQuery("FROM Assembly ", Assembly.class).getResultList();
            // List<Conservation> cServices = session.createQuery("FROM Conservation ", Conservation.class).getResultList();
            List<Employee> employees = session.createQuery("FROM Employee ", Employee.class).getResultList();
            //  List<WoodLikeMaterial> wlMaterial = session.createQuery("FROM WoodLikeMaterial ", WoodLikeMaterial.class).getResultList();

            // Wyświetlenie odczytanych danych
            for(Employee employee : employees){
                System.out.println(employee);
                //   System.out.println(service.getAssemblyList().stream().map(Assembly::getId).collect(Collectors.toList()));
            }
/*
            for (Conservation c : cServices) {
                System.out.println(c);
            }
 */
/*
            for (License c : licenses) {
               System.out.println(c.getEmployee());
                System.out.println(c.getSpecialization().getLicenses());
              //  System.out.println(List.of(s.get(c.getIdS().intValue()).getLicenses()));
            }
 */




            // Zakończenie transakcji
            session.getTransaction().commit();
        } catch (Exception e) {
            // Obsługa błędów
            e.printStackTrace();
            session.getTransaction().rollback();
        }
    }
}

/*
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}*/
