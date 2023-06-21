module pja.kociespa {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.hibernate.orm.core;
    requires jakarta.persistence;
    requires org.hibernate.validator;
    requires jakarta.validation;
    requires java.naming;

    opens pja.kociespa to javafx.fxml, org.hibernate.orm.core;
    exports pja.kociespa;

    opens pja.kociespa.model to javafx.fxml, org.hibernate.orm.core;
    exports pja.kociespa.model;

    opens pja.kociespa.gui to javafx.fxml, org.hibernate.orm.core;
    exports pja.kociespa.gui;
}