package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.entities.candidate.Candidate;
import model.entities.company.Message;

import java.io.IOException;

/**
 * Created by lesy on 18.5.17.
 */
public class MessageListViewCell extends ListCell<Message> {
    @FXML
    private Text nameText;

    @FXML
    private Text fromText;

    @FXML
    private Text contentText;

    @FXML
    private Text dateText;

    private FXMLLoader loader;

    @FXML
    private GridPane gridPane;

    @Override
    protected void updateItem(Message message, boolean empty) {
        super.updateItem(message, empty);

        if(empty || message == null) {
            setText(null);
            setGraphic(null);
        }
        else if (loader == null) {
            loader = new FXMLLoader(getClass().getResource("/MessageListPane.fxml"));
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            nameText.setText(message.name);
            if(message.getCompany() != null) {
                fromText.setText(message.getCompany().name);
            }
            dateText.setText(message.date.toString());
            contentText.setText(message.content);
            setText(null);
            setGraphic(gridPane);
        }
    }
}
