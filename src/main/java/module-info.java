module es.iesalquerias.barometerfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens es.iesalquerias.barometerfx to javafx.fxml;
    exports es.iesalquerias.barometerfx;
    requires com.google.gson;
}
