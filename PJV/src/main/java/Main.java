import controller.Router;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static Router router;

    @Override
    public void start(Stage primaryStage) throws Exception {
        router = new Router();
        Router.setMainStage(primaryStage);

        primaryStage.setTitle("Personální agentura");
        Router.redirect(Router.Views.LOGIN);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}