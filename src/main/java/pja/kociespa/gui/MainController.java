package pja.kociespa.gui;

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
import org.hibernate.Session;
import org.hibernate.query.Query;
import pja.kociespa.model.Cat;
import pja.kociespa.model.ServiceClass;
import pja.kociespa.model.Stay;
import pja.kociespa.model.StayStatus;
import pja.kociespa.util.AppData;

import java.io.IOException;
import java.time.LocalDate;

import static pja.kociespa.util.GeneralUtil.showErrorAlert;
import static pja.kociespa.util.GeneralUtil.switchToNextScene;
import static pja.kociespa.util.HibernateUtil.createTestData;
import static pja.kociespa.util.HibernateUtil.getSessionFactory;

public class MainController {
    @FXML
    private Button addTestDataButton;

    @FXML
    private Button addStayButton;

    @FXML
    private TableView<Cat> catTableView;

    @FXML
    private TableColumn<Cat, String> catNameColumn;

    @FXML
    private TableColumn<Cat, String> catOwnerColumn;

    @FXML
    private TableView<Stay> stayTableView;

    @FXML
    private TableColumn<Stay, LocalDate> stayStartDateColumn;

    @FXML
    private TableColumn<Stay, LocalDate> stayEndDateColumn;

    @FXML
    private TableColumn<Stay, ServiceClass> stayServiceClassColumn;

    @FXML
    private TableColumn<Stay, Double> stayTotalPriceColumn;

    @FXML
    private TableColumn<Stay, StayStatus> stayStatusColumn;

    @FXML
    private TableColumn<Stay, String> stayCaretakerColumn;

    ObservableList<Cat> cats = FXCollections.observableArrayList();
    ObservableList<Stay> stays = FXCollections.observableArrayList();
    private final AppData appData = AppData.getInstance();

    public void initialize() {
        initTableViews();
        initAddTestDataButton();
        initAddStayButton();
    }

    /**
     * Initializes the table views
     */
    @SuppressWarnings("DuplicatedCode")
    private void initTableViews() {
        catNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        catOwnerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
        // More verbose way to fill in the Customer column, without relying on toString()
        /*catOwnerColumn.setCellValueFactory(cellData -> {
            Cat cat = cellData.getValue();
            Customer customer = cat.getCustomer();

            String ownerName = customer.getName() + " " + customer.getSurname();
            return new SimpleStringProperty(ownerName);
        });*/
        catTableView.setItems(cats);

        stayStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        stayEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
        stayServiceClassColumn.setCellValueFactory(new PropertyValueFactory<>("serviceClass"));
        stayTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        stayStatusColumn.setCellValueFactory(new PropertyValueFactory<>("stayStatus"));
        stayCaretakerColumn.setCellValueFactory(new PropertyValueFactory<>("caretaker"));
        // More verbose way to fill in the Caretaker column, without relying on toString()
        /*stayCaretakerColumn.setCellValueFactory(cellData -> {
            Stay stay = cellData.getValue();
            Caretaker caretaker = stay.getCaretaker();

            String stayCaretaker = caretaker.getName() + " " + caretaker.getSurname();
            return new SimpleStringProperty(stayCaretaker);
        });*/
        stayTableView.setItems(stays);

        Label customMessage = new Label("This cat has no stays yet.");
        customMessage.setWrapText(true);
        customMessage.setTextAlignment(TextAlignment.CENTER);
        stayTableView.setPlaceholder(customMessage);

        try (Session session = getSessionFactory().openSession()) {
            cats.addAll(session.createQuery("FROM Cat ", Cat.class).getResultList());

            catTableView.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                stayTableView.getItems().clear();
                stayTableView.getItems().addAll(newValue.getStays());
            });
        }
    }


    /**
     * trying to click this one with no data in the database, will throw an Alert
     */
    private void initAddStayButton() {
        addStayButton.setOnAction(e -> {
            if (verifyStartConditions()) {
                try {
                    appData.setReceptionist();
                    Stage currentStage = (Stage) addStayButton.getScene().getWindow();
                    switchToNextScene(currentStage, "stayDetails.fxml");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                showErrorAlert("Lack of required data", "Make sure there's at least one Caretaker, Cat and Treatment in the database.");
            }
        });
    }

    /**
     * only makes the button clickable if there's no data in the database
     */
    private void initAddTestDataButton() {
        if (catTableView.getItems().size() > 0) {
            addTestDataButton.setDisable(true);
        } else {
            addTestDataButton.setOnAction(e -> {
                try {
                    createTestData();
                    initTableViews();
                    addTestDataButton.setDisable(true);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    /**
     * @return true if all conditions described in the use case documentation are met
     */
    private boolean verifyStartConditions() {
        try (Session session = getSessionFactory().openSession()) {
            // Also covers the condition of at least 1 employee
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Caretaker", Long.class);
            Long caretakerCount = query.uniqueResult();

            query = session.createQuery("SELECT COUNT(*) FROM Cat", Long.class);
            Long catCount = query.uniqueResult();

            query = session.createQuery("SELECT COUNT(*) FROM Treatment", Long.class);
            Long treatmentCount = query.uniqueResult();

            session.close();

            return caretakerCount > 0
                    && catCount > 0
                    && treatmentCount > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
