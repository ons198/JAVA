/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoxpress.gui;

import autoxpress.entities.Client;
import autoxpress.entities.Conducteur;
import autoxpress.entities.Messages;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import autoxpress.services.ClientCRUD;
import autoxpress.services.ConducteurCRUD;
import autoxpress.services.MessageCRUD;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseDragEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author BKHmidi
 */
public class SendmessageclientController implements Initializable {

    @FXML
    private Button btn_msgcl;
    @FXML
    private TextField tfcontenucl;
    @FXML
    private TextField tfsearch_conducteur;
    @FXML
    private Button serachButton;
    @FXML
    private VBox conducteurList;
    @FXML
    private VBox tabmessagecl;
    @FXML
    private ComboBox<String> cbclient;
    private ComboBox<String> cbconducteur;
    @FXML
    private TextField tfdestinataire;
    @FXML
    private Button btnrec;
    @FXML
    private Button btnoff;
    @FXML
    private Button btnres;
    @FXML
    private Button btncontrat;
    @FXML
    private Button btnuti2;
    @FXML
    private Button btnveh;
    @FXML
    private Button btnlivr;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ClientCRUD cd = new ClientCRUD();
        List<Client> clientData = cd.ClientList();

        // create a list of conducteur names
        List<String> conducteurNames = new ArrayList<>();
        for (Client c : clientData) {
            conducteurNames.add(c.getPrenom_client());
        }

        // set the list of conducteur names as the items of the ComboBox
        cbclient.setItems(FXCollections.observableArrayList(conducteurNames));

        // add an event handler to the ComboBox
        cbclient.setOnAction(event -> selectclient(event));

        ConducteurCRUD c = new ConducteurCRUD();
        List<Conducteur> condData = c.ConducteurList();

// loop through the list and create a label for each user
        for (Conducteur cdn : condData) {
            Label label = new Label(cdn.getPrenom_conducteur());
            conducteurList.getChildren().add(label);
            label.setOnMouseClicked(event -> tfdestinataire.setText(label.getText()));;
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void sendMessagecl(ActionEvent event) {
        String destinataire = (String) tfdestinataire.getText();
        String expediteur = (String) cbclient.getValue();
        String message = tfcontenucl.getText();

        ClientCRUD cd = new ClientCRUD();
        ConducteurCRUD cdn = new ConducteurCRUD();

        if (destinataire.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "conducteur non sélectionné", "Veuillez sélectionner un conducteur.");
            return;
        }

        Messages m = new Messages();
        MessageCRUD pcd = new MessageCRUD();
        int id_client = pcd.getIdClient(expediteur);
        int id_conducteur = pcd.getIdConducteur(destinataire);

        m.setId_client(id_client);
        m.setId_conducteur(id_conducteur);
        m.setContenu(message);

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

// Set the date_message field of the Message object
        m.setDate_message(timestamp);

        if (expediteur == null || destinataire.isEmpty() || message.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Champ(s) vide(s)", "Veuillez remplir tous les champs.");
            return;
        }

        pcd.AddMessage(m); // save the message to the database

        String phoneNumber = cdn.getConductorPhoneNumber(destinataire);

// Send SMS notification
        sendSms(phoneNumber);

        // Create a Label for the new message and add it to the VBox
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        String messageText = String.format("%s -> %s : %s (%s)", expediteur, destinataire, message, m.getDate_message() != null
                ? formatter.format(m.getDate_message().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime())
                : formatter.format(LocalDateTime.now()));
        Label messageLabel = new Label(messageText);
        tabmessagecl.getChildren().add(messageLabel);

        // Clear input fields
        clean(event);
    }

    @FXML
    private void search(ActionEvent event) {
    }

    public String sendSms(String phoneNumber) {
        // Retrieve phone number associated with client from database

        // Set up the API endpoint and credentials
        String apiUrl = "https://api.twilio.com/2010-04-01/Accounts/ACe56a03d46e756dd1a33ebd2e4bd447e6/Messages.json";
        String fromNumber = "+15673717172";
        String body = "AutoXpress:You have a new message";
        String authToken = "2f57fc64326ce163f13a9c8f98bae4fd"; // Replace with your actual auth token

        // Set up the cURL command to send the SMS message
        String[] cmd = {
            "curl",
            "-X",
            "POST",
            apiUrl,
            "--data-urlencode",
            "Body=" + body,
            "--data-urlencode",
            "From=" + fromNumber,
            "--data-urlencode",
            "To=" + "+216" + phoneNumber,
            "-u",
            "ACe56a03d46e756dd1a33ebd2e4bd447e6:" + authToken
        };

        // Execute the cURL command and retrieve the output
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(cmd);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Command executed successfully");
                System.out.println(output.toString());
            } else {
                System.out.println("Error executing cURL command");
            }
        } catch (IOException e) {
            System.out.println("Error executing cURL command: " + e.getMessage());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // restore interrupted status
            System.out.println("Interrupted while waiting for cURL command to complete");
        }

        return null;

    }

    private void clean(ActionEvent event) {
        tfcontenucl.setText(null);
    }

    @FXML
    private void selectclient(ActionEvent event) {
        // get the selected conducteur from the conducteurComboBox
        String selectedClientName = (String) cbconducteur.getValue();

        // do something with the selected conducteur
        System.out.println("Selected client: " + selectedClientName);
    }

    @FXML
    private void reclamation(ActionEvent event) {
    }

    @FXML
    private void switchForm(ActionEvent event) {
    }

   

    @FXML
    private void click(MouseDragEvent event) {
    }

    @FXML
    private void btn_aff_livconducteur(ActionEvent event) {
        try {
            Parent avisParent = FXMLLoader.load(getClass().getResource("LivraisonClient.fxml"));
            Scene avisScene = new Scene(avisParent);
            Stage window = (Stage) (((Button) event.getSource()).getScene().getWindow());
            window.setScene(avisScene);
            window.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void movetohome(ActionEvent event) {
    }

}
