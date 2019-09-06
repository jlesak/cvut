package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.entities.candidate.Experience;
import model.models.SkillModel;
import model.models.factories.ModelFactory;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by lesy on 19.5.17.
 */
public class NewExperienceController extends BaseController{

    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private TextField companyTextField;
    @FXML
    private DatePicker fromDatePicker;
    @FXML
    private DatePicker toDatePicker;
    @FXML
    private TextField positionTextField;
    @FXML
    private ComboBox<String> typeComboBox;

    private ArrayList<String> experienceTypes;
    private SkillModel skillModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        skillModel = new SkillModel(ModelFactory.getDatabaseConnection());
        experienceTypes = skillModel.getAllExperienceTypes();

        typeComboBox.setItems(FXCollections.observableList(experienceTypes));
    }

    @FXML
    private void onSaveExperienceBtn(ActionEvent e){
        if (isAllSet()){
            Experience experience = new Experience();
            experience.title = nameTextField.getText();
            experience.description = descriptionTextArea.getText();
            experience.company = companyTextField.getText();

            LocalDate localDate = fromDatePicker.getValue();
            Instant instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            experience.date_from = Date.from(instant);
            localDate = toDatePicker.getValue();
            instant = Instant.from(localDate.atStartOfDay(ZoneId.systemDefault()));
            experience.date_to = Date.from(instant);

            experience.position = positionTextField.getText();
            experience.experience_type = typeComboBox.getValue().toString();

            experiences.add(experience);
            Router.redirect(Router.Views.NEW_CANDIDATE);

        }
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Není vše vyplněno");
            alert.setHeaderText(null);
            alert.setContentText("Musí být vyplněny všechny položky.");
            alert.show();
        }

    }

    /**
     * Returns to new candidate form
     * @param e
     */
    @FXML
    private void onCancelBtnClick(ActionEvent e){
        Router.redirect(Router.Views.NEW_CANDIDATE);
    }

    /**
     * Check if all inputs are set
     * @return
     */
    private boolean isAllSet(){
        boolean isSet = true;
        if (nameTextField.getText().isEmpty() ||
                descriptionTextArea.getText().isEmpty() ||
                companyTextField.getText().isEmpty() ||
                positionTextField.getText().isEmpty()) {
            isSet = false;
            showInputWarningAlert();
        }
        return isSet;
    }
}
