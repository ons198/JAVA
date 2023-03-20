/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoxpress.GUI;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MenuconducteurController implements Initializable {

    @FXML
    private Pane rootPane;
    @FXML
    private Button btnrec;
    @FXML
    private Button btnoff;
    @FXML
    private Button btnres;
    @FXML
    private Button btnuti2;
    @FXML
    private Button btnveh;
    @FXML
    private Button btnlivr;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void reclamation(ActionEvent event) {
               try {
            Parent avisParent = FXMLLoader.load(getClass().getResource("RecConducteur.fxml"));
            Scene avisScene = new Scene(avisParent);
            Stage window = (Stage) (((Button) event.getSource()).getScene().getWindow());
            window.setScene(avisScene);
            window.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void switchForm(ActionEvent event) {
    }

    @FXML
    private void click(MouseDragEvent event) {
    }

    @FXML
    private void movetohome(ActionEvent event) {
    }

    @FXML
    private void btn_aff_livconducteur(ActionEvent event) {
         try {
            Parent avisParent = FXMLLoader.load(getClass().getResource("LivraisonCondu.fxml"));
            Scene avisScene = new Scene(avisParent);
            Stage window = (Stage) (((Button) event.getSource()).getScene().getWindow());
            window.setScene(avisScene);
            window.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void menumessage(ActionEvent event) {
          try {
            Parent avisParent = FXMLLoader.load(getClass().getResource("sendmessage.fxml"));
            Scene avisScene = new Scene(avisParent);
            Stage window = (Stage) (((Button) event.getSource()).getScene().getWindow());
            window.setScene(avisScene);
            window.show();
        } catch (IOException e) {
        }
    }

    @FXML
    private void offre_cond(ActionEvent event) {
        try {
            Parent avisParent = FXMLLoader.load(getClass().getResource("AjouterOffre.fxml"));
            Scene avisScene = new Scene(avisParent);
            Stage window = (Stage) (((Button) event.getSource()).getScene().getWindow());
            window.setScene(avisScene);
            window.show();
        } catch (IOException e) {
        }
    }
    
}
