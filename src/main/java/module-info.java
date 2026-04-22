module org.example.gestionprojetacademique {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    opens org.example.gestionprojetacademique to javafx.fxml;

    opens org.example.gestionprojetacademique.controller to javafx.fxml;

    exports org.example.gestionprojetacademique;
}