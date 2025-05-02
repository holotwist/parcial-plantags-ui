module co.edu.uniquindio.poo.parcial_gsplanta_ui {
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
    requires static lombok;

    opens co.edu.uniquindio.poo.parcial_gsplanta_ui to javafx.fxml;
    opens co.edu.uniquindio.poo.parcial_gsplanta_ui.model to javafx.base;

    exports co.edu.uniquindio.poo.parcial_gsplanta_ui;
}