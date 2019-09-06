
package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import model.entities.candidate.Candidate;
import model.entities.company.Company;
import model.models.CandidateModel;
import model.models.CompanyModel;
import model.models.factories.ModelFactory;

import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController extends BaseController {
    private CandidateModel candidateModel;

    private CompanyModel companyModel;

    @FXML
    private javafx.scene.control.Button newCandidateBtn;

    @FXML
    private javafx.scene.control.Button newCompanyBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        candidateModel = new CandidateModel(ModelFactory.getDatabaseConnection());
        companyModel = new CompanyModel(ModelFactory.getDatabaseConnection());

        //Candidate candidate = candidateModel.getCandidate(user);
        //Company company = companyModel.getCompany(user);

        if(candidate == null) {
            newCandidateBtn.setText("Vytvořit profil");
        } else {
            newCandidateBtn.setText("Upravit profil");
        }

        if(company == null) {
            newCompanyBtn.setText("Vytvořit firmu");

        } else {
            newCompanyBtn.setText("Upravit firmu");
        }
    }

    /**
     * Opens candidates main view
     * @param e
     */
    @FXML
    protected void onCandidateBtnClick(ActionEvent e) {

        Router.redirect(Router.Views.CANDIDATES_MAIN);
    }

    /**
     * opens form for new candidate
     * @param e
     */
    @FXML
    protected void onNewCandidateBtnClick(ActionEvent e) {
        Router.redirect(Router.Views.NEW_CANDIDATE);
    }

    /**
     * Opens companies main view
     * @param e
     */
    @FXML
    protected void onCompanyBtnClick(ActionEvent e) {
        Router.redirect(Router.Views.COMPANIES_MAIN);
    }

    /**
     * Opens view with posts from companies
     * @param e
     */
    @FXML
    protected void onOffersBtnClick(ActionEvent e) {
        Router.redirect(Router.Views.POSTS);
    }

    /**
     * Opens form for creating new Company
     * @param event
     */
    @FXML
    public void onNewCompanyBtnClick(ActionEvent event) {
        Router.redirect(Router.Views.NEW_COMPANY);
    }
}

