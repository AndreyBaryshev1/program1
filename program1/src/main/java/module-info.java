module com.pp.program1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;
    requires javafx.swing;



    opens com.pp.program1 to javafx.fxml;
    exports com.pp.program1;
    exports Iterator;
    opens Iterator to javafx.fxml;
}