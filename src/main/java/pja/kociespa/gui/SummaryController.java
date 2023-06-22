package pja.kociespa.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hibernate.Session;
import pja.kociespa.model.*;
import pja.kociespa.util.AppData;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static pja.kociespa.util.GeneralUtil.switchToNextScene;
import static pja.kociespa.util.HibernateUtil.getSessionFactory;

public class SummaryController {

    @FXML
    private ComboBox<Cat> catComboBox;

    @FXML
    private ComboBox<Caretaker> caretakerComboBox;

    @FXML
    private ComboBox<ServiceClass> serviceClassComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TableView<Pair<Treatment, Employee>> chosenTreatmentTableView;

    @FXML
    private TableColumn<Pair<Treatment, Employee>, String> chosenTreatmentColumn;

    @FXML
    private TableColumn<Pair<Treatment, Employee>, String> chosenEmployeeColumn;

    @FXML
    private TextField totalPriceTextField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    private final AppData appData = AppData.getInstance();

    private final Receptionist receptionist = appData.getReceptionist();
    private final Cat cat = appData.getCat();
    private final Caretaker caretaker = appData.getCaretaker();
    private final ServiceClass serviceClass = appData.getServiceClass();
    private final LocalDate startDate = appData.getStartDate();
    private final LocalDate endDate = appData.getEndDate();
    private final ObservableList<Pair<Treatment, Employee>> treatmentsAndEmployees = appData.getTreatmentsAndEmployees();
    private double totalPrice = 0.0;
    public void initialize() {
        buildSummary();
        calculateTotalPrice();
        initConfirmButton();
        initCancelButton();
    }

    /**
     * Builds the summary of the stay. The data is retrieved from the AppData singleton and put into the appropriate components.
     */
    private void buildSummary() {
        catComboBox.setValue(cat);
        caretakerComboBox.setValue(caretaker);
        serviceClassComboBox.setValue(serviceClass);

        startDatePicker.setValue(startDate);
        endDatePicker.setValue(endDate);

        chosenTreatmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().toString()));
        chosenEmployeeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().toString()));
        chosenTreatmentTableView.setItems(treatmentsAndEmployees);
    }

    /**
     * Calculates the total price of the stay, based on the number of days, the service class and price of the chosen treatments.
     */
    private void calculateTotalPrice() {
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        totalPrice = days * serviceClass.pricePerDay;

        for (Pair<Treatment, Employee> pair : treatmentsAndEmployees) {
            totalPrice += pair.getKey().getPrice();
        }
        totalPriceTextField.setText(String.valueOf(totalPrice));
    }

    /**
     * Creates the confirm button. Allows the user to confirm adding the stay to the database. Then returns the user to the main view.
     */
    private void initConfirmButton() {
        confirmButton.setOnAction(e -> {
            try {
                createStay();

                Stage currentStage = (Stage) confirmButton.getScene().getWindow();
                switchToNextScene(currentStage, "main.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Creates the cancel button. Allows the user to abandon the stay adding process and return to the main view.
     */
    private void initCancelButton() {
        cancelButton.setOnAction(e -> {
            try {
                Stage currentStage = (Stage) cancelButton.getScene().getWindow();
                switchToNextScene(currentStage, "main.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Creates the stay in the database.
     */
    private void createStay() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();

            Stay stay = new Stay(Date.valueOf(startDate), Date.valueOf(endDate), serviceClass);

            for (Pair<Treatment, Employee> pair : treatmentsAndEmployees) {
                Treatment mergedTreatment = session.merge(pair.getKey());
                stay.addTreatment(mergedTreatment);

                // We need to avoid trying to add the same employee twice.
                boolean employeeAdded = false;
                for (Employee employee : stay.getEmployees()) {
                    if (employee.getId().equals(pair.getValue().getId())) {
                        employeeAdded = true;
                        break;
                    }
                }

                if (!employeeAdded) {
                    Employee mergedEmployee = session.merge(pair.getValue());
                    stay.addEmployee(mergedEmployee);
                }
            }

            Caretaker mergedCaretaker = session.merge(caretaker);
            Cat mergedCat = session.merge(cat);
            Receptionist mergedReceptionist = session.merge(receptionist);

            stay.setCaretaker(mergedCaretaker);
            stay.setCat(mergedCat);
            stay.setReceptionist(mergedReceptionist);
            stay.setTotalPrice(totalPrice);

            session.persist(stay);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
