package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.entities.company.Message;
import model.models.MessageModel;
import model.models.factories.ModelFactory;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by lesy on 19.5.17.
 */
public class NewMessageController extends BaseController {
    @FXML
    public TextField nameTextField;
    @FXML
    public TextArea contentTextArea;
    @FXML
    public Text toText;

    private MessageModel messageModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        messageModel = new MessageModel(ModelFactory.getDatabaseConnection());

        toText.setText(msgCandidate.getUser().firstname + " " + msgCandidate.getUser().lastname);
    }

    public void onSaveMessageBtn(ActionEvent event) {
        if (nameTextField.getText().isEmpty() ||
                contentTextArea.getText().isEmpty()) {
            showInputWarningAlert();
            return;
        }

        Message message = new Message();
        message.name = nameTextField.getText();
        message.content = contentTextArea.getText();
        message.date = new Date(System.currentTimeMillis());
        message.company_ico = company.ico;
        message.candidate_id = msgCandidate.id;

        messageModel.sendMessage(message);

        nameTextField.clear();
        contentTextArea.clear();
        toText.setText("");
    }

    public void onBackBtnClick(ActionEvent event) {
        Router.redirect(Router.Views.CANDIDATES_MAIN);
    }
}
