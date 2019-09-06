package controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;

public class Router {

    protected static Stage mainStage;

    private static HashMap<String, FXMLLoader> rootInstances = new HashMap<String,FXMLLoader>();

    public static void setMainStage(Stage mainStage) {
        Router.mainStage = mainStage;
    }

    public enum Views {
        LOGIN("/LoginView.fxml"),
        REGISTRATION("/RegistrationView.fxml"),
        DASHBOARD("/DashboardView.fxml"),
        CANDIDATES_MAIN("/CandidatesMainView.fxml"),
        NEW_CANDIDATE("/NewCandidateView.fxml"),
        NEW_EXPERIENCE("/NewExperienceView.fxml"),
        COMPANIES_MAIN("/CompaniesMainView.fxml"),
        NEW_COMPANY("/NewCompanyView.fxml"),
        POSTS("/PostsMainView.fxml"),
        LOADING("/LoadingView.fxml"),
        NEW_MESSAGE("/NewMessageView.fxml");

        private final String text;

        Views(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }

    public static Parent redirect(Views view) {
        String fxml = view.getText();
        Parent page = null;
        FXMLLoader loader;
        //FXMLLoader loader = rootInstances.getOrDefault(fxml, null);

        //if(loader == null) {
            URL resource = Router.class.getResource(fxml);
            loader = new FXMLLoader(resource, null, new JavaFXBuilderFactory());
        //}

        try {
            page = (Parent) loader.load();
            //rootInstances.put(fxml, loader);
            BaseController ctrl = loader.getController();
            ctrl.afterRedirect();
        } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException("No such resource file as '" + fxml + "'.");
        }

        page = refreshScene(page);
        return page;
    }

    private static Parent refreshScene(Parent page) {
        Scene scene = mainStage.getScene();
        if (scene == null) {
            scene = new Scene(page);
            mainStage.setScene(scene);
            mainStage.setResizable(true);
        } else {
            mainStage.getScene().setRoot(page);
        }
        mainStage.sizeToScene();

        return page;
    }

}
