module pja.kociespa {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
            requires net.synedra.validatorfx;
                    
    opens pja.kociespa to javafx.fxml;
    exports pja.kociespa;
}