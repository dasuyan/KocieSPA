package pja.kociespa.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.hibernate.Session;
import pja.kociespa.model.Employee;
import pja.kociespa.model.Treatment;
import pja.kociespa.util.AppData;

import java.io.IOException;

import static pja.kociespa.util.GeneralUtil.switchToNextScene;
import static pja.kociespa.util.HibernateUtil.getSessionFactory;

public class TreatmentDetailsController {
    @FXML
    private TableView<Treatment> treatmentTableView;

    @FXML
    private TableColumn<Treatment, String> treatmentNameColumn;

    @FXML
    private TableColumn<Treatment, Double> treatmentPriceColumn;

    @FXML
    private TableView<Employee> employeeTableView;

    @FXML
    private TableColumn<Employee, String> employeeNameColumn;

    @FXML
    private TableColumn<Employee, String> employeeSurnameColumn;

    @FXML
    private TableView<Pair<Treatment, Employee>> chosenTreatmentTableView;

    @FXML
    private TableColumn<Pair<Treatment, Employee>, String> chosenTreatmentColumn;

    @FXML
    private TableColumn<Pair<Treatment, Employee>, String> chosenEmployeeColumn;

    @FXML
    private Button addTreatmentButton;

    @FXML
    private Button removeTreatmentButton;

    @FXML
    private Button confirmButton;

    ObservableList<Treatment> treatments = FXCollections.observableArrayList();
    ObservableList<Employee> employees = FXCollections.observableArrayList();
    ObservableList<Pair<Treatment, Employee>> treatmentsAndEmployees = FXCollections.observableArrayList();

    private final AppData appData = AppData.getInstance();

    public void initialize() {
        initTableViews();
        initAddTreatmentButton();
        initRemoveTreatmentButton();
        initConfirmButton();
    }

    /**
     * Loads the database data into the ObservableLists
     */
    @SuppressWarnings("DuplicatedCode")
    private void initTableViews() {
        treatmentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        treatmentPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        treatmentTableView.setItems(treatments);

        employeeNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        employeeSurnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        employeeTableView.setItems(employees);

        Label customMessage = new Label("To view employees who can perform the treatment, select the treatment first.");
        customMessage.setWrapText(true);
        customMessage.setTextAlignment(TextAlignment.CENTER);
        employeeTableView.setPlaceholder(customMessage);

        try (Session session = getSessionFactory().openSession()) {
            treatments.addAll(session.createQuery("FROM Treatment ", Treatment.class).getResultList());

            treatmentTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                employeeTableView.getItems().clear();
                employeeTableView.getItems().addAll(newValue.getEmployees());
            });
        }

        chosenTreatmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getKey().toString()));
        chosenEmployeeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getValue().toString()));
        chosenTreatmentTableView.setItems(treatmentsAndEmployees);
    }

    /**
     * Initializes the add treatment button, lets the user add a treatment to a list of treatments to be added to the stay.
     * Only works if both the treatment and employee to perform it were selected.
     */
    private void initAddTreatmentButton() {
        addTreatmentButton.setOnAction(e -> {
            if (treatmentTableView.getSelectionModel().getSelectedItem() == null
                    || employeeTableView.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            treatmentsAndEmployees.add(new Pair<>(treatmentTableView.getSelectionModel().getSelectedItem(), employeeTableView.getSelectionModel().getSelectedItem()));
        });
    }

    /**
     * Removes the selected treatment&employee pair from the list of selected treatments for the stay.
     */
    private void initRemoveTreatmentButton() {
        removeTreatmentButton.setOnAction(e -> {
            if (chosenTreatmentTableView.getSelectionModel().getSelectedItem() == null) {
                return;
            }
            treatmentsAndEmployees.remove(chosenTreatmentTableView.getSelectionModel().getSelectedItem());
        });
    }

    /**
     * Initializes the confirm button. Allows to save the treatments for the stay (there can be none as well).
     */
    private void initConfirmButton() {
        confirmButton.setOnAction(e -> {
            setAppData();
            try {
                Stage currentStage = (Stage) confirmButton.getScene().getWindow();
                switchToNextScene(currentStage, "summary.fxml");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Persist the chosen treatments and employees.
     */
    private void setAppData() {
        appData.setTreatmentsAndEmployees(treatmentsAndEmployees);
    }

}
