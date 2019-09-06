package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.entities.candidate.Candidate;
import model.entities.candidate.Skill;
import model.models.SkillModel;
import model.models.factories.ModelFactory;

import java.io.IOException;

/**
 * Created by lesy on 17.5.17.
 */
public class CandidateListViewCell extends ListCell<Candidate> {
    @FXML
    private Text fullNameTextFiled;

    @FXML
    private Text birthTextFiled;

    @FXML
    private Text skills;
    @FXML
    private Text smallDescriptionTextFiled;

    private FXMLLoader loader;

    @FXML
    private GridPane gridPane;

    private SkillModel skillModel = new SkillModel(ModelFactory.getDatabaseConnection());

    @Override
    protected void updateItem(Candidate candidate, boolean empty) {
        super.updateItem(candidate, empty);

        if(empty || candidate == null) {
            setText(null);
            setGraphic(null);
            return;
        }

        if (loader == null) {
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/CandidateListPane.fxml"));
            loader.setController(this);
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        fullNameTextFiled.setText(candidate.getUser().firstname + " " + candidate.getUser().lastname);
        birthTextFiled.setText(candidate.getUser().birth.toString());
        smallDescriptionTextFiled.setText(candidate.description);
        skills.setText("");
        for (Skill skill : skillModel.getCandidatesSkills(candidate)) {
            skills.setText(skills.getText() + " " + skill.name);
        }

        setText(null);
        setGraphic(gridPane);


    }
}
