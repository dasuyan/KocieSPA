package pja.kociespa.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

public class StayDataController {
    @FXML
    private ComboBox<?> catComboBox;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private ComboBox<?> serviceClassComboBox;

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private Button toTreatmentsButton;

    void initialize() {

    }
}
