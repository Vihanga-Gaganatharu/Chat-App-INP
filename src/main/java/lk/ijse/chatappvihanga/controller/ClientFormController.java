package lk.ijse.chatappvihanga.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ClientFormController {
    public TextField txtMsg;
    public Label lblName;
    public Button btnBack;

    private String displayMsg;

    @FXML
    private VBox msgVboxAp;

    private final byte[] emojiByteCode1 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x8D};
    private final byte[] emojiByteCode2 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x97};
    private final byte[] emojiByteCode3 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x82};
    private final byte[] emojiByteCode4 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0xB1};
    private final byte[] emojiByteCode5 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0xB4};
    private final byte[] emojiByteCode6 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0xB5};
    private final byte[] emojiByteCode7 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0xA1};
    private final byte[] emojiByteCode8 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0xAF};
    private final byte[] emojiByteCode9 = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x87};

    public final String emj1 = new String(emojiByteCode1, StandardCharsets.UTF_8);
    public final String emj2 = new String(emojiByteCode2, StandardCharsets.UTF_8);
    public final String emj3 = new String(emojiByteCode3, StandardCharsets.UTF_8);
    public final String emj4 = new String(emojiByteCode4, StandardCharsets.UTF_8);
    public final String emj5 = new String(emojiByteCode5, StandardCharsets.UTF_8);
    public final String emj6 = new String(emojiByteCode6, StandardCharsets.UTF_8);
    public final String emj7 = new String(emojiByteCode7, StandardCharsets.UTF_8);
    public final String emj8 = new String(emojiByteCode8, StandardCharsets.UTF_8);
    public final String emj9 = new String(emojiByteCode9, StandardCharsets.UTF_8);

    Socket socket;

    DataOutputStream dataOutputStream;

    DataInputStream dataInputStream;

    static String clientName;


    public void initialize() {
        lblName.setText(HomeFormController.name);
        clientName = lblName.getText() ;

        new Thread(() -> {
            try {
                socket = new Socket("localhost", 3002);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

                while (true) {
                    String message = dataInputStream.readUTF();
                    if (!message.startsWith(lblName.getText()) && !message.contains("-")) {
                        Platform.runLater(() -> {
                            Label msg = new Label();
                            String style = "-fx-background-color: rgba(0, 255, 0, 0.5); -fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 5px;";
                            msg.setStyle(style);
                            msg.setText(message);
                            Platform.runLater(() -> msgVboxAp.getChildren().addAll(msg));
                        });
                    }  else if (!message.startsWith(lblName.getText()) && message.contains("-")){
                        String modifiedMessage = message.substring(message.indexOf("-")+1);
                        String removedMessage = message.substring(0, message.indexOf("-"));
                        Image image = convertStringToImage(modifiedMessage);
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(100);
                        imageView.setFitHeight(100);
                        Label label = new Label(removedMessage);
                        HBox hBox = new HBox(12, label,imageView);
                        Platform.runLater(() -> msgVboxAp.getChildren().addAll(hBox));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void btnSendOnAction(ActionEvent actionEvent) {
        if (displayMsg()) {
            try {
                String sendMsg = lblName.getText() + " : " + txtMsg.getText();
                dataOutputStream.writeUTF(sendMsg);
                dataOutputStream.flush();
                txtMsg.clear();
                txtMsg.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean displayMsg(){
        if (validateMsg()) {
            String newMsg = txtMsg.getText();
            Label label = new Label(newMsg);
            HBox hBox = new HBox(label);
            String style = "-fx-background-color: #44BCFB; -fx-border-color: black; -fx-border-width: 1; -fx-border-radius: 5px;";
            label.setStyle(style);
            hBox.setAlignment(Pos.TOP_RIGHT);
            hBox.setStyle("-fx-border-radius: 5px");
            Platform.runLater(()->msgVboxAp.getChildren().addAll(hBox));
            return true;
        } else {
            return false;
        }
    }


    @FXML
    void btnFileOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        FileChooser.ExtensionFilter imageFilter =
                new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif");
        fileChooser.getExtensionFilters().add(imageFilter);
        File file = fileChooser.showOpenDialog(txtMsg.getScene().getWindow());

        if (file != null) {
            try {
                ImageView imageView = new ImageView(file.toURI().toString());
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);

                HBox hBox = new HBox(12, imageView);
                hBox.setAlignment(Pos.CENTER_RIGHT);
                msgVboxAp.setAlignment(Pos.TOP_LEFT);

                Platform.runLater(() -> msgVboxAp.getChildren().add(hBox));

                String imgText = convertImageToString(imageView.getImage());
                dataOutputStream.writeUTF(lblName.getText() + "-" + imgText);
                dataOutputStream.flush();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private String convertImageToString(Image image) throws IOException {
        double maxWidth = 600;
        double maxHeight = 400;
        double width = image.getWidth();
        double height = image.getHeight();

        if (width > maxWidth || height > maxHeight) {
            double scaleFactor = Math.min(maxWidth / width, maxHeight / height);
            width *= scaleFactor;
            height *= scaleFactor;
        }

        BufferedImage resizedImage = new BufferedImage((int) width, (int) height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(SwingFXUtils.fromFXImage(image, null), 0, 0, (int) width, (int) height, null);
        g.dispose();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, "jpg", outputStream);

        byte[] imageBytes = outputStream.toByteArray();
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private Image convertStringToImage(String imageAsString) throws IOException {
        byte[] imageBytes = Base64.getDecoder().decode(imageAsString);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        return new Image(inputStream);
    }

    private boolean validateMsg(){
        if (txtMsg.getText().trim().isEmpty()) {
            txtMsg.requestFocus();
            txtMsg.setStyle("-fx-border-color:#ff0000;");
            return false;
        } else {
            txtMsg.setStyle("-fx-border-color:black;");
            return true;
        }
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    public void emoji1OnAction(MouseEvent mouseEvent) {
        displayMsg = txtMsg.getText();
        txtMsg.setText(displayMsg +emj1);
    }

    public void emoji2OnAction(MouseEvent mouseEvent) {
        displayMsg = txtMsg.getText();
        txtMsg.setText(displayMsg +emj2);
    }

    public void emoji3OnAction(MouseEvent mouseEvent) {
        displayMsg = txtMsg.getText();
        txtMsg.setText(displayMsg +emj3);
    }

    public void emoji4OnAction(MouseEvent mouseEvent) {
        displayMsg = txtMsg.getText();
        txtMsg.setText(displayMsg +emj4);
    }

    public void emoji5OnAction(MouseEvent mouseEvent) {
        displayMsg = txtMsg.getText();
        txtMsg.setText(displayMsg +emj5);
    }

    public void emoji6OnAction(MouseEvent mouseEvent) {
        displayMsg = txtMsg.getText();
        txtMsg.setText(displayMsg +emj6);
    }

    public void emoji7OnAction(MouseEvent mouseEvent) {
        displayMsg = txtMsg.getText();
        txtMsg.setText(displayMsg +emj7);
    }

    public void emoji8OnAction(MouseEvent mouseEvent) {
        displayMsg = txtMsg.getText();
        txtMsg.setText(displayMsg +emj8);
    }

    public void emoji9OnAction(MouseEvent mouseEvent) {
        displayMsg = txtMsg.getText();
        txtMsg.setText(displayMsg +emj9);
    }
}
