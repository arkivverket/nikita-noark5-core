package nikita.extraction.run;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MainController {

    @FXML
    TextField serverAddress;
    @FXML
    TextField username;
    @FXML
    TextField password;
    @FXML
    Button testConnectionButton;
    @FXML
    Button loginButton;
    @FXML
    Label usernameLbl;
    private App mainApp;

    public MainController() {
    }

    public void handleTestConnection() {
        System.out.println("handleTestConnection pressed");
    }

    public void handleLogin() {
        System.out.println("handleLogin pressed");
    }

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
    }
}
