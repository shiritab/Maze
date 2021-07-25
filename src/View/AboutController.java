package View;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class AboutController {

    @FXML
    public Tab AboutUs;

    public void aboutUs() {
        String image = getClass().getResource("/WE_ARE.png").toExternalForm();
        String style = "-fx-background-image: url('"+image+"');";
        AboutUs.getContent().setStyle(style);
    }
}
