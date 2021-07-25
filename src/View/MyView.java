package View;

import Model.IModel;
import Model.MyModel;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class MyView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Welcome to Bikini Bottom");
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.setMaximized(true);
        primaryStage.show();
        String image = getClass().getResource("/bikini_bottom_border.png").toExternalForm();
        String style = "-fx-background-image: url('"+image+"');";
        //https://cdn.timesmedia.co.id/images/2020/03/07/bikini-bottom.jpg
        (root).setStyle(style);
        root.getStylesheets().add(getClass().getResource("/View/Style.css").toExternalForm());
        ((BorderPane)root).setBackground(Background.EMPTY);


        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model);
        MyViewController view = fxmlLoader.getController();
        view.setViewModel(viewModel);
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            view.CloseProgram();
        });
        viewModel.addObserver(view);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
