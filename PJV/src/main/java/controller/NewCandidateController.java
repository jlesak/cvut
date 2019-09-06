package controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.entities.candidate.Candidate;
import model.entities.candidate.Experience;
import model.entities.candidate.Skill;
import model.models.CandidateModel;
import model.models.SkillModel;
import model.models.factories.ModelFactory;


import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by lesy on 18.5.17.
 */
public class NewCandidateController extends BaseController{
    @FXML
    private TextField jobTitleTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private CheckBox lookingForJobCheckbox;
    @FXML
    private ListView<Skill> skillsListView;
    @FXML
    private ListView<Experience> experiencesListView;

    private CandidateModel candidateModel;
    private SkillModel skillModel;
    private ArrayList<Skill> selectedSkills;
    private model.entities.candidate.File cv;
    private ArrayList<Skill> skills;


    @FXML
    private Button deleteBtn;
    private static Candidate newCandidate;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        candidateModel = new CandidateModel(ModelFactory.getDatabaseConnection());
        skillModel = new SkillModel(ModelFactory.getDatabaseConnection());
        if (experiences == null){
            experiences = new ArrayList<>();
        }
        if (candidate != null && experiences.size() == 0){
            experiences = skillModel.getCandidatesExperiences(candidate);
        }

        selectedSkills = new ArrayList<>();
        fillSkillsList();
        fillExperiencesList();
    }

    @Override
    public void afterRedirect() {

        Candidate candidate = candidateModel.getCandidate(user);
        if (candidate == null) {
            deleteBtn.setDisable(true);
        }
        else
        {
            deleteBtn.setDisable(false);
            lookingForJobCheckbox.setSelected(candidate.looking_for_job);
            jobTitleTextField.setText(candidate.job_title);
            descriptionTextArea.setText(candidate.description);
        }
        if (newCandidate != null){
            lookingForJobCheckbox.setSelected(newCandidate.looking_for_job);
            jobTitleTextField.setText(newCandidate.job_title);
            descriptionTextArea.setText(newCandidate.description);
        }
    }

    /**
     * Fills SkillListView with all skills in database
     */
    private void fillSkillsList(){
        skills = skillModel.getAllSkills();
        ArrayList<String> values = new ArrayList<>();
        for (Skill skill :
                skills) {
            values.add(skill.name);
        }

        skillsListView.setItems(FXCollections.observableList(skills));
        skillsListView.setCellFactory(CheckBoxListCell.forListView(item -> {
            BooleanProperty observable = new SimpleBooleanProperty();
            observable.addListener((obs, wasSelected, isNowSelected) ->
                    {
                        if (isNowSelected) selectedSkills.add(item);
                        else if (wasSelected) selectedSkills.remove(item);
                    }
            );
            return observable ;
        }));
    }

    /**
     * Fills ExperiencesListView with all candidates experiences
     */
    private void fillExperiencesList(){
        experiencesListView.setItems(FXCollections.observableArrayList(experiences));
        experiencesListView.setCellFactory(cListView -> new ExperienceListViewCell());
    }

    /**
     * Opens form fro new Experience
     * @param e
     */
    @FXML
    private void onNewExperienceBtn(ActionEvent e){
        newCandidate = new Candidate();
        newCandidate.description = descriptionTextArea.getText();
        newCandidate.job_title = jobTitleTextField.getText();
        newCandidate.looking_for_job = lookingForJobCheckbox.isSelected();
        Router.redirect(Router.Views.NEW_EXPERIENCE);
    }

    /**
     * Refresh ExperiencesListView
     * @param e
     */
    @FXML
    private void onRefreshExperiences(ActionEvent e){
        experiencesListView.setItems(FXCollections.observableList(experiences));
    }

    /**
     * Opens open file dialog for select CV in PDF
     * @param e
     */
    @FXML
    private void onUploadFileBtn(ActionEvent e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Nahrajte soubor");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        File selectedFile = fileChooser.showOpenDialog(Router.mainStage);

        if (selectedFile != null){
            cv = new model.entities.candidate.File();
            cv.name = selectedFile.getName();
            cv.filename = selectedFile.getName();
            cv.path = selectedFile.getPath();
            cv._user = user.id;
        }

    }
    /**
     * Makes candidate instance and saves it to database
     * @param event
     */
    @FXML
    private void onSaveCandidateBtn(ActionEvent event) {
        if (jobTitleTextField.getText().isEmpty() || descriptionTextArea.getText().isEmpty())
        {
            showInputWarningAlert();
            return;
        }

        if (candidate != null){
            candidate.job_title = jobTitleTextField.getText();
            candidate.description = descriptionTextArea.getText();
            candidate.looking_for_job = lookingForJobCheckbox.isSelected();
            candidateModel.editCandidate(candidate);
            skillModel.saveExperiences(candidate, experiences);
            skillModel.refreshCandidateSkills(candidate, selectedSkills);
        }
        else {
            Candidate candidate = new Candidate();
            candidate.job_title = jobTitleTextField.getText();
            candidate.description = descriptionTextArea.getText();
            candidate.looking_for_job = lookingForJobCheckbox.isSelected();
            candidate.setUser(user);

            if(!candidateModel.newCandidate(candidate)){
                return;
            }

            candidate = candidateModel.getCandidate(user);
            skillModel.saveSkills(candidate, selectedSkills);
            skillModel.saveExperiences(candidate, experiences);
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informační dialog");
        alert.setHeaderText(null);
        alert.setContentText("Kandidát byl uložen!");

        clearForm();
        deleteBtn.setDisable(false);
        alert.showAndWait();

        Router.redirect(Router.Views.DASHBOARD);
    }

    /**
     * Deletes candidate from database and shows alert
     * @param event
     */
    @FXML
    public void onDeleteBtnClick(ActionEvent event) {
        if (candidateModel.getCandidate(user) != null){
            candidateModel.deleteCandidate(user.email);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Informační dialog");
            alert.setHeaderText(null);
            alert.setContentText("Kandidát byl smazán!");
            alert.showAndWait();

            deleteBtn.setDisable(true);
            clearForm();
        }
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
     * Clears all inputs in form
     */
    private void clearForm(){
        jobTitleTextField.clear();
        descriptionTextArea.clear();
        lookingForJobCheckbox.setSelected(false);
        experiences.clear();
    }
}
