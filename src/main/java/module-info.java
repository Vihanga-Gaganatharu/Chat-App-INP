module lk.ijse.chatappvihanga {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.jfoenix;
    requires javafx.swing;


    opens lk.ijse.chatappvihanga to javafx.fxml;
    exports lk.ijse.chatappvihanga;
    exports lk.ijse.chatappvihanga.controller;
    opens lk.ijse.chatappvihanga.controller to javafx.fxml;
}