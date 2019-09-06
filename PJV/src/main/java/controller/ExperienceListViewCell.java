package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.entities.candidate.Candidate;
import model.entities.candidate.Experience;

import java.io.IOException;

/**
 * Created by lesy on 18.5.17.
 */
public class ExperienceListViewCell extends ListCell<Experience> {
    @FXML
    private Text typePositionText;
    @FXML
    private Text dateText;
    @FXML
    private Text descriptionText;
    @FXML
    private Text companyText;
    @FXML
    private Text titleText;

    private FXMLLoader loader;

    @FXML
    private GridPane gridPane;

    @Override
    protected void updateItem(Experience experience, boolean empty) {
        super.updateItem(experience, empty);

        if (empty || experience == null) {
            setText(null);
            setGraphic(null);
        } else if (loader == null) {
            loader = new FXMLLoader(getClass().getResource("/ExperienceListPane.fxml"));
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            typePositionText.setText(experience.experience_type);
            titleText.setText(experience.title);
            dateText.setText("od" + experience.date_from.toString() + " do " + experience.date_to.toString());
            descriptionText.setText(experience.description);
            companyText.setText(experience.company);
            setText(null);
            setGraphic(gridPane);
        }
    }
}
