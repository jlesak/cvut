package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import model.entities.company.Company;
import model.entities.company.Post;
import model.models.CompanyModel;
import model.models.PostModel;
import model.models.factories.ModelFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by lesy on 19.5.17.
 */
public class NewCompanyController extends BaseController {

    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField websiteTextField;
    @FXML
    private TextField icoTextField;
    @FXML
    private TextField postTitleTextField;
    @FXML
    private TextArea postDescriptionTextArea;

    @FXML
    private Button deleteBtn;

    private CompanyModel companyModel;
    private PostModel postModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        companyModel = new CompanyModel(ModelFactory.getDatabaseConnection());
        postModel = new PostModel(ModelFactory.getDatabaseConnection());
        icoTextField.setEditable(true);
        company = companyModel.getCompany(user);
    }

    @Override
    public void afterRedirect() {
        company = companyModel.getCompany(user);
        if(company != null) {
            deleteBtn.setDisable(false);
            nameTextField.setText(company.name);
            descriptionTextArea.setText(company.description);
            websiteTextField.setText(company.website);
            icoTextField.setText(company.ico);
            disableField(icoTextField);


            Post post = postModel.getCompanyPost(company);
            postTitleTextField.setText(post.title);
            postDescriptionTextArea.setText(post.description);
        } else {
            enableField(icoTextField);
            deleteBtn.setDisable(true);
        }
    }

    private void disableField(TextField node) {
        node.setDisable(true);
        node.setEditable(false);
        node.setStyle("-fx-opacity: 0.6;");
    }

    private void enableField(TextField node) {
        node.setDisable(false);
        node.setEditable(true);
        node.setStyle("-fx-opacity: 1.0;");
    }

    /**
     * Makes company instance and saves it to database
     * @param event
     */
    @FXML
    public void onSaveCompanyBtn(ActionEvent event) {

        if (nameTextField.getText().isEmpty() ||
                descriptionTextArea.getText().isEmpty() ||
                websiteTextField.getText().isEmpty() ||
                icoTextField.getText().isEmpty() ||
                postTitleTextField.getText().isEmpty() ||
                postDescriptionTextArea.getText().isEmpty()){
            showInputWarningAlert();
            return;
        }

        if (company != null){
            company.description = descriptionTextArea.getText();
            company.website = websiteTextField.getText();
            company.name = nameTextField.getText();
            Post post = new Post(postTitleTextField.getText(), postDescriptionTextArea.getText(), icoTextField.getText());
            post.date = new Date();
            companyModel.newCompanyToView(company, post);
        }

        else {
            Company company = new Company();
            Post post = new Post(postTitleTextField.getText(), postDescriptionTextArea.getText(), icoTextField.getText());
            post.date = new Date();

            company.ico = icoTextField.getText();
            company.description = descriptionTextArea.getText();
            company.website = websiteTextField.getText();
            company.name = nameTextField.getText();
            company.setUser(user);

            companyModel.newCompanyToView(company,post);
            deleteBtn.setDisable(false);
        }
        Router.redirect(Router.Views.DASHBOARD);
    }

    /**
     * Shows dashboard view
     * @param event
     */
    @FXML
    public void onBackBtnClick(ActionEvent event) {
        Router.redirect(Router.Views.DASHBOARD);
    }

    /**
     * Deletes company from database and shows alert
     * @param event
     */
    @FXML
    public void onDeleteBtnClick(ActionEvent event) {
        if (companyModel.getCompany(user) != null) {
            companyModel.deleteCompany(user.email);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informační dialog");
            alert.setHeaderText(null);
            alert.setContentText("Firma byl smazána!");
            alert.show();

            afterRedirect();
            clearForm();
        }
    }
    /**
     * Clears all inputs in form
     */
    private void clearForm() {
        nameTextField.clear();
        descriptionTextArea.clear();
        websiteTextField.clear();
        icoTextField.clear();
        postDescriptionTextArea.clear();
        postTitleTextField.clear();
    }
}
