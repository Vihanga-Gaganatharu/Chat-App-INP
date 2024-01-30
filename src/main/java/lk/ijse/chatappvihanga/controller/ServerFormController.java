package lk.ijse.chatappvihanga.controller;

import com.jfoenix.controls.JFXTextArea;
import javafx.event.ActionEvent;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerFormController {
    public JFXTextArea txtArea;
    private ServerSocket serverSocket;

    private DataOutputStream dataOutputStream;

    private static final List<DataOutputStream> clients = new ArrayList<>();

    public void initialize() {
        try {
            serverSocket = new ServerSocket(3002);
            txtArea.setText("Server started waiting for client connection...");

            new Thread(() -> {
                while (true) {
                    try {
                        Socket clientSocket = serverSocket.accept();
                        txtArea.appendText("\n"+HomeFormController.name + " connected ");
                        dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
                        clients.add(dataOutputStream);
                        new Thread(() -> handleClient(clientSocket)).start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleClient(Socket clientSocket) {
        try {
            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());

            while (true) {
                String message = inputStream.readUTF();
                for (DataOutputStream client : clients) {
                    client.writeUTF(message);
                    client.flush();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        System.exit(0);
    }
}
