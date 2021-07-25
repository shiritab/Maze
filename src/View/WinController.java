package View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class WinController{


    private MyViewController viewControl;
    @FXML
    public Button playAgain;
    @FXML
    public Pane pane;

    public void close(){
        System.exit(0);
    }

    public void playAgain(){
        ((Stage)pane.getScene().getWindow()).close();
    }
}
