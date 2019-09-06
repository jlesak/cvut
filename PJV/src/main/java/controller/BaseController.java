package controller;


import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.candidate.Candidate;
import model.entities.candidate.Experience;
import model.entities.company.Company;
import model.entities.user.User;

import javax.jws.soap.SOAPBinding;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {

    protected static User user;
    protected static Candidate candidate;
    protected static Candidate msgCandidate;
    protected static Company company;

    protected static ArrayList<Experience> experiences;
    @FXML
    protected Node mainPane;
    @FXML
    private void onMenuCloseBtn(ActionEvent e){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Zavřít aplikaci");
        alert.setHeaderText(null);
        alert.setContentText("Opravdu chcete ukončit aplikaci?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage mainStage = (Stage)mainPane.getScene().getWindow();
            mainStage.close();
        }
    }
    @FXML
    public void onMenuLogoutBtn(ActionEvent e){
        user = null;
        Router.redirect(Router.Views.LOGIN);
    }
    @FXML
    public void onMainPageBtn(ActionEvent e){
        Router.redirect(Router.Views.DASHBOARD);
    }

    public void afterRedirect() {
    }

    protected void showInputWarningAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Chyba!");
        alert.setHeaderText(null);
        alert.setContentText("Vplňte všechny údaje!");
        alert.showAndWait();
    }

}
