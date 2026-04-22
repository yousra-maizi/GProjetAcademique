module ps.eheio.gestionprojetacademique {
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

    opens ps.eheio.gestionprojetacademique to javafx.fxml;

    opens ps.eheio.gestionprojetacademique.controller to javafx.fxml;

    exports ps.eheio.gestionprojetacademique;
}