package nikita.extraction.run;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {

    private FXMLLoader loader;
    private BorderPane rootNode;

    public static void main(String[] args) {
        launch(App.class, args);
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Noark 5 Uttrekk");
        stage.setScene(new Scene(rootNode, 800, 600));

        // Give the controller access to the main app
        MainController controller = loader.getController();
        controller.setMainApp(this);
        stage.show();
    }

    @Override
    public void init() throws Exception {
        loader = new FXMLLoader(getClass().getResource("main.fxml"));
        rootNode = loader.load();
    }
}
