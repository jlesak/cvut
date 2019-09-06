package controller;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Callback;
import model.entities.candidate.Candidate;
import model.entities.candidate.Skill;
import model.models.CandidateModel;
import model.models.SkillModel;
import model.models.factories.ModelFactory;

import java.net.URL;
import java.util.*;

/**
 * Created by lesy on 17.5.17.
 */
public class CandidatesMainController extends BaseController{
    @FXML
    public Button writeMessageBtn;
    public HBox buttonsHBox;
    private CandidateModel candidateModel;
    private SkillModel skillModel;
    private ArrayList<Candidate> candidates;
    private ProgressBar progressBar = new ProgressBar();

    @FXML
    private ListView<Skill> skillsListView;

    @FXML
    private ListView<Candidate> candidatesListView;

    @FXML
    private Text nameText;

    @FXML
    private Text jobTitleText;

    @FXML
    private Text descriptionText;

    private ArrayList<Skill> selectedSkills;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        candidateModel = new CandidateModel(ModelFactory.getDatabaseConnection());
        skillModel = new SkillModel(ModelFactory.getDatabaseConnection());
        candidates = new ArrayList<>();

        selectedSkills = new ArrayList<>();
        fillSkillsList();
        fillCandidatesList();
    }

    @Override
    public void afterRedirect() {
        if (company == null) writeMessageBtn.setVisible(false);
        else writeMessageBtn.setVisible(true);
    }

    /**
     * Fills SkillListView with all skills in database
     */
    private void fillSkillsList(){
        ArrayList<Skill> values = skillModel.getAllSkills();

        skillsListView.setItems(FXCollections.observableList(values));
        skillsListView.setCellFactory(CheckBoxListCell.forListView(new Callback<Skill, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(Skill item) {
                BooleanProperty observable = new SimpleBooleanProperty();
                observable.addListener((obs, wasSelected, isNowSelected) ->
                        {
                            if (isNowSelected) selectedSkills.add(item);
                            else if (wasSelected) selectedSkills.remove(item);
                        }
                );
                return observable ;
            }
        }));
    }

    /**
     * Fills CandidatesListView with candidates matching selected skills
     */
    private void fillCandidatesList(){
        candidatesListView.setCellFactory(cListView -> new CandidateListViewCell());
        candidatesListView.setItems(FXCollections.observableArrayList());
        candidatesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            descriptionText.setText(newValue.description + "\n");
            jobTitleText.setText(newValue.job_title);
            for (Skill skill : skillModel.getCandidatesSkills(newValue)) {
                descriptionText.setText(descriptionText.getText() + " " + skill.name);
            }
            nameText.setText(newValue.getUser().firstname + " " + newValue.getUser().lastname);
            msgCandidate = newValue;
        });
    }

    /**
     * Refresh candidates in ListView based on selected skills
     * @param actionEvent
     */
    @FXML
    public void onSearchBtnClick(ActionEvent actionEvent) {

        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                buttonsHBox.getChildren().add(progressBar);
            }
        });

        Task<Void> searchCandidates = new Task<Void>() {
            @Override
            protected Void call() throws Exception {

                return null;
            }
        };

        searchCandidates.setOnSucceeded(ee->{

        });

        searchCandidates.setOnFailed(eee->{

        });

        new Thread(searchCandidates).start();

        if (selectedSkills.isEmpty()) {
            candidatesListView.getItems().clear();
            return;
        }*/

        this.candidates = candidateModel.getCandidatesBySkills(selectedSkills);
        candidatesListView.getItems().setAll(this.candidates);

    }

    /**
     * Opens scene with form for new message
     * @param event
     */
    public void openNewMessageBtnClick(ActionEvent event) {
        if (msgCandidate != null){
            Router.redirect(Router.Views.NEW_MESSAGE);
        }
    }
}
