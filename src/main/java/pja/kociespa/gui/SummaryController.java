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

import static pja.kociespa.util.GeneralUtil.switchToNextScene;
import static pja.kociespa.util.HibernateUtil.getSessionFactory;

public class summaryController {

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

    private void calculateTotalPrice() {
        int days = Period.between(startDate, endDate).getDays();
        if (days == 0) {
            days = 1;
        }
        totalPrice = days * serviceClass.pricePerDay;

        for (Pair<Treatment, Employee> pair : treatmentsAndEmployees) {
            totalPrice += pair.getKey().getPrice();
        }

        totalPriceTextField.setText(String.valueOf(totalPrice));
    }

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

    private void createStay() {
        try (Session session = getSessionFactory().openSession()) {
            session.beginTransaction();

            Stay stay = new Stay(Date.valueOf(startDate), Date.valueOf(endDate), serviceClass);
            for (Pair<Treatment, Employee> pair : treatmentsAndEmployees) {
                stay.addTreatment(pair.getKey());
                stay.addEmployee(pair.getValue());
            }

            stay.setCaretaker(caretaker);
            stay.setCat(cat);
            stay.setReceptionist(receptionist);
            stay.setTotalPrice(totalPrice);

            session.persist(stay);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
