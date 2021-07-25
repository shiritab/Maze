package View;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class HelpController {
    @FXML
    public Tab howToPlay, Target,gameOption;

    public void howToPlay() {
        String image = getClass().getResource("/how_to_play.png").toExternalForm();
        String style = "-fx-background-image: url('" + image + "');";
        howToPlay.getContent().setStyle(style);
    }

    public void gameTarget() {
        String image = getClass().getResource("/games_target.png").toExternalForm();
        String style = "-fx-background-image: url('" + image + "');";
        Target.getContent().setStyle(style);
    }

    public void gameOptions(){
        String image = getClass().getResource("/symbols.png").toExternalForm();
        String style = "-fx-background-image: url('" + image + "');";
        gameOption.getContent().setStyle(style);
    }

}
