package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import model.entities.user.User;
import model.models.CandidateModel;
import model.models.CompanyModel;
import model.models.UserModel;
import model.models.factories.ModelFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController extends BaseController {
    @FXML
    public VBox buttonsVBox;
    private UserModel userModel;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;
    private ProgressBar progressBar = new ProgressBar();

    private CandidateModel candidateModel;
    private CompanyModel companyModel;
    private User loggedUser;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userModel = new UserModel(ModelFactory.getDatabaseConnection());
        candidateModel = new CandidateModel(ModelFactory.getDatabaseConnection());
        companyModel = new CompanyModel(ModelFactory.getDatabaseConnection());
        buttonsVBox.setAlignment(Pos.TOP_CENTER);


        usernameField.setText("email@seznam.cz");
        passwordField.setText("123456");
    }

    /**
     * Login user if input values are valid. Otherwise alert is shown.
     * @param e
     */
    @FXML
    protected void onLoginBtnClick(ActionEvent e) {

        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()){
            showInputWarningAlert();
            return;
        }

        Platform.runLater(() -> buttonsVBox.getChildren().add(progressBar));

        Task<Void> loginUser = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                String email = usernameField.getText();
                String password = passwordField.getText();
                validateUser(email, password);
                return null;
            }
        };

        loginUser.setOnSucceeded(ee->{
            buttonsVBox.getChildren().remove(progressBar);
            user = this.loggedUser;
            candidate = candidateModel.getCandidate(this.loggedUser);
            company = companyModel.getCompany(this.loggedUser);
            passwordField.clear();
            Router.redirect(Router.Views.DASHBOARD);
        });

        loginUser.setOnFailed(eee->{
            buttonsVBox.getChildren().remove(progressBar);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Chybné údaje");
            alert.setHeaderText(null);
            alert.setContentText("Špatně zadané jméno či heslo.");
            alert.showAndWait();
        });

        new Thread(loginUser).start();
    }



    private void validateUser(String email, String password){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = userModel.loginUser(email, password);
        if (user == null){
            new Alert(Alert.AlertType.ERROR);
        }
        this.loggedUser =  user;
    }

    /**
     * Opens form for registration
     * @param e
     */
    @FXML
    protected void onRegistrationBtnClick(ActionEvent e) {
        Router.redirect(Router.Views.REGISTRATION);
    }



}

