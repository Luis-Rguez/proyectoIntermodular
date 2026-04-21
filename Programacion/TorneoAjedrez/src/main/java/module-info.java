module org.example.torneoajedrez {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires static lombok;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;


    opens org.example.torneoajedrez to javafx.fxml;
    exports org.example.torneoajedrez;
    exports org.example.torneoajedrez.controller;
    opens org.example.torneoajedrez.controller to javafx.fxml;

    opens org.example.torneoajedrez.controller.admin to javafx.fxml;
    exports org.example.torneoajedrez.controller.admin;

    opens org.example.torneoajedrez.controller.user to javafx.fxml;
    exports org.example.torneoajedrez.controller.user;

    opens org.example.torneoajedrez.controller.staff to javafx.fxml;
    exports org.example.torneoajedrez.controller.staff;

    opens org.example.torneoajedrez.DataSet to javafx.fxml;
    exports org.example.torneoajedrez.DataSet;

    opens org.example.torneoajedrez.model to javafx.fxml;
    exports org.example.torneoajedrez.model;
}