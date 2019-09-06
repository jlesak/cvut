package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.entities.company.Post;

import java.io.IOException;


/**
 * Created by lesy on 19.5.17.
 */
public class PostListViewCell extends ListCell<Post> {
    @FXML
    private Text nameText;
    @FXML
    private Text dateText;
    @FXML
    private Text descriptionText;

    private FXMLLoader loader;

    @FXML
    private GridPane gridPane;


    @Override
    protected void updateItem(Post post, boolean empty) {
        super.updateItem(post, empty);
        if(empty || post == null) {
            setText(null);
            setGraphic(null);
        }
        else if (loader == null) {
            loader = new FXMLLoader(getClass().getResource("/PostListPane.fxml"));
            loader.setController(this);

            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            nameText.setText(post.title);
            dateText.setText(post.date.toString());
            descriptionText.setText(post.description);

            setText(null);
            setGraphic(gridPane);

        }
    }
}
