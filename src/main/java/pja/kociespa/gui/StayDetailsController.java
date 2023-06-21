package pja.kociespa.gui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import pja.kociespa.model.Caretaker;
import pja.kociespa.model.Cat;
import pja.kociespa.model.ServiceClass;
import pja.kociespa.util.AppData;

import java.io.IOException;
import java.time.LocalDate;

import static pja.kociespa.util.GeneralUtil.showErrorAlert;
import static pja.kociespa.util.GeneralUtil.switchToNextScene;
import static pja.kociespa.util.HibernateUtil.getSessionFactory;

public class StayDetailsController {
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
    private Button toTreatmentsButton;

    ObservableList<Cat> cats = FXCollections.observableArrayList();
    ObservableList<Caretaker> caretakers = FXCollections.observableArrayList();
    ObservableList<ServiceClass> serviceClasses = FXCollections.observableArrayList();

    private final AppData appData = AppData.getInstance();

    public void initialize() {
        loadDatabaseData();
        initComboBoxes();
        initDatePickers();
        initToTreatmentsButton();
    }

    /**
     * Loads the database data into the ObservableLists
     */
    private void loadDatabaseData() {
        try (Session session = getSessionFactory().openSession()) {
            cats.addAll(session.createQuery("FROM Cat", Cat.class).getResultList());
            caretakers.addAll(session.createQuery("FROM Caretaker", Caretaker.class).getResultList());
            serviceClasses.addAll(ServiceClass.values());
        }
    }

    private void initComboBoxes() {
        catComboBox.setItems(cats);
        caretakerComboBox.setItems(caretakers);
        serviceClassComboBox.setItems(serviceClasses);

        catComboBox.setPromptText("Choose a cat");
        caretakerComboBox.setPromptText("Choose a caretaker");
        serviceClassComboBox.setPromptText("Choose a service class");
    }

    private void initDatePickers() {
        startDatePicker.setPromptText("Select a start date");
        endDatePicker.setPromptText("Select an end date");

        // Disable past in the startDatePicker
        startDatePicker.setDayCellFactory(picker -> new DateCell() {
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date.isBefore(LocalDate.now())) {
                    setDisable(true);
                }
            }
        });

        // Only let the user pick the end date if the start date is selected
        endDatePicker.setDisable(true);

        // Add a listener to startDatePicker's value property
        startDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            endDatePicker.setDisable(false);

            // Set the minimum date for endDatePicker to be the selected date from startDatePicker
            endDatePicker.setDayCellFactory(picker -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);

                    // Disable dates before the selected date in startDatePicker
                    if (date.isBefore(newValue)) {
                        setDisable(true);
                    }
                }
            });
        });
    }

    private void initToTreatmentsButton() {
        toTreatmentsButton.setOnAction(event -> {
            if (verifyProceedConditions()) {
                setAppData();
                try {
                    Stage currentStage = (Stage) toTreatmentsButton.getScene().getWindow();
                    switchToNextScene(currentStage, "treatmentDetails.fxml");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    /**
     * @return true if all the stay details are filled in, otherwise false.
     */
    private boolean verifyProceedConditions() {
        if (catComboBox.getSelectionModel().getSelectedItem() == null) {
            showErrorAlert("Lack of required details", "Choose a cat.");
            return false;
        }
        if (caretakerComboBox.getSelectionModel().getSelectedItem() == null) {
            showErrorAlert("Lack of required details", "Choose a caretaker.");
            return false;
        }
        if (serviceClassComboBox.getSelectionModel().getSelectedItem() == null) {
            showErrorAlert("Lack of required details", "Choose a service class.");
            return false;
        }
        if (startDatePicker.getValue() == null) {
            showErrorAlert("Lack of required details", "Choose a start date.");
            return false;
        }
        if (endDatePicker.getValue() == null) {
            showErrorAlert("Lack of required details", "Choose an end date.");
            return false;
        }
        return true;
    }

    /**
     * Saves the stay details in the AppData singleton, to make it accessible in the summary view.
     */
    private void setAppData() {
        Cat cat = catComboBox.getSelectionModel().getSelectedItem();
        Caretaker caretaker = caretakerComboBox.getSelectionModel().getSelectedItem();
        ServiceClass serviceClass = serviceClassComboBox.getSelectionModel().getSelectedItem();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        appData.setCat(cat);
        appData.setCaretaker(caretaker);
        appData.setServiceClass(serviceClass);
        appData.setStartDate(startDate);
        appData.setEndDate(endDate);
    }

}
