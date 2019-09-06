package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import model.entities.candidate.Candidate;
import model.entities.company.Company;
import model.entities.company.Message;
import model.models.CandidateModel;
import model.models.CompanyModel;
import model.models.MessageModel;
import model.models.SkillModel;
import model.models.factories.ModelFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by lesy on 17.5.17.
 */
public class CompaniesMainController extends BaseController {

    @FXML
    private ListView<Company> companiesListView;

    @FXML
    private ListView<Message> messagesListView;

    @FXML
    private Text nameText;

    @FXML
    private Hyperlink webHyperlink;

    @FXML
    private Text descriptionText;
    private ArrayList<Company> companies;
    private MessageModel messageModel;
    private CompanyModel companyMdodel;
    private ArrayList<Message> messages;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        companyMdodel = new CompanyModel(ModelFactory.getDatabaseConnection());
        messageModel = new MessageModel(ModelFactory.getDatabaseConnection());
        companies = companyMdodel.getAllCompanies();
        if (company != null){
            messages = messageModel.getMessages(company);
            fillMessagesList();
        }

        fillCompaniesList();
    }

    @Override
    public void afterRedirect() {
        fillCompaniesList();
        if (company != null){
            fillMessagesList();
        }
    }

    /**
     * Fills MessagesListView with all candidates messages
    */
    private void fillMessagesList() {
        messagesListView.setItems(FXCollections.observableArrayList(messages));
        messagesListView.setCellFactory(cListView -> new MessageListViewCell());
    }

    /**
     * Fills CompaniesListView with all Companies in database
     */
    private void fillCompaniesList() {
        companiesListView.setItems(FXCollections.observableArrayList(companies));
        companiesListView.setCellFactory(cListView -> new CompanyListViewCell());
        companiesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Company>() {
            @Override
            public void changed(ObservableValue<? extends Company> observable, Company oldValue, Company newValue) {
                descriptionText.setText(newValue.description);
                webHyperlink.setText(newValue.website);
                nameText.setText(newValue.name);
            }
        });
    }
}
