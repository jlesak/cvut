package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.entities.user.User;
import model.models.UserModel;
import model.models.factories.ModelFactory;

import javax.swing.*;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;


public class RegistrationController extends BaseController {

    @FXML
    private PasswordField passField;

    @FXML
    private PasswordField pass2Field;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField surnameTextField;

    @FXML
    private DatePicker birth;

    private UserModel userModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userModel = new UserModel(ModelFactory.getDatabaseConnection());
    }

    /**
     * Opens Login view
     * @param event
     */
    @FXML
    private void onBackBtnClick(ActionEvent event) {
        Router.redirect(Router.Views.LOGIN);
    }

    /**
     * Makes User instance and saves it into database
     * @param event
     */
    @FXML
    private void onRegisterBtnClick(ActionEvent event) {

        if (passField.getText().isEmpty() ||
                !Objects.equals(passField.getText(), pass2Field.getText()) ||
                !isValidEmailAddress(emailTextField.getText()) ||
                nameTextField.getText().isEmpty() ||
                surnameTextField.getText().isEmpty())
        {
            showInputWarningAlert();
            return;
        }

        String email = emailTextField.getText();
        String password = passField.getText();
        String password2 = pass2Field.getText();
        User user = new User();
        user.email = email;
        user.firstname = nameTextField.getText();
        user.lastname = surnameTextField.getText();

        LocalDate localDate = birth.getValue();
        Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
        user.birth = Date.from(instant);

        if(userModel.registerUser(user, password)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Uživatel úspěšně přidán.");
            alert.setHeaderText(null);
            alert.setContentText("Budete navráceni na přihlašovací obrazovku.");
            alert.showAndWait();
            Router.redirect(Router.Views.LOGIN);
        }

    }

    /**
     * Check if email is valid email address
     * @param email email address
     * @return true/false
     */
    public boolean isValidEmailAddress(String email) {
        // Stack Overflow regex
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }
}
