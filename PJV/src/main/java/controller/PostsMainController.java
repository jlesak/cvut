package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import model.entities.candidate.Candidate;
import model.entities.company.Post;
import model.models.PostModel;
import model.models.factories.ModelFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by lesy on 19.5.17.
 */
public class PostsMainController extends BaseController{

    @FXML
    public Text nameTextFiled;
    @FXML
    public Hyperlink websiteHyperLink;
    @FXML
    public Text descriptionText;
    @FXML
    public ListView<Post> postsListView;

    private ArrayList<Post> posts;
    private PostModel postModel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        postModel = new PostModel(ModelFactory.getDatabaseConnection());
        posts = postModel.getPosts();
        fillPostsList();
    }

    @Override
    public void afterRedirect() {
        posts = postModel.getPosts();
    }

    public void onBackBtnClick(ActionEvent event) {
        Router.redirect(Router.Views.DASHBOARD);
    }

    private void fillPostsList(){
        postsListView.setCellFactory(cListView -> new PostListViewCell());
        postsListView.setItems(FXCollections.observableArrayList(posts));
        postsListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            nameTextFiled.setText(newValue.getCompany().name);
            websiteHyperLink.setText(newValue.getCompany().website);
            descriptionText.setText(newValue.getCompany().description);
        });
    }
}
