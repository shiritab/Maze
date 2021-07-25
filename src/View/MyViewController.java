package View;

import ViewModel.MyViewModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;


import java.io.*;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class MyViewController implements IView, Observer {
    private MyViewModel mvm;
    private Maze maze;
    @FXML
    public TextField textField_mazeRows;
    @FXML
    public TextField textField_mazeCols;
    @FXML
    public MazeDisplayer mazeDisplayer;
    @FXML
    public Button buttonGenerate;
    public MenuItem buttonSolve,buttonUnSolve;
    public MenuItem buttonExit1,buttonSave;
    @FXML
    public ComboBox<String> combo_box;
    @FXML
    public Label alertLabel,wrongFile;
    public static int rowGlobal,colGlobal;
    public static int counter=0;

    StringProperty update_player_position_row = new SimpleStringProperty();
    StringProperty update_player_position_col = new SimpleStringProperty();

    //after exit is clicked
    public void CloseProgram(){
        System.exit(0);
        mvm.StopServers();
    }

    public void maze_Automatic(){
        String type = combo_box.getSelectionModel().getSelectedItem();
        Random rnd = new Random();
        int row,col;
        if(type.compareTo("Customize")!=0) {
            if (type.compareTo("Easy") == 0/*type=="Easy"*/) {
                rowGlobal = rnd.nextInt(15 - 2) + 15;
                colGlobal = rnd.nextInt(15 - 2) + 15;
            } else if (type == "Medium") {
                rowGlobal = rnd.nextInt(30 - 16) + 30;
                colGlobal = rnd.nextInt(30 - 16) + 30;
            } else {
                rowGlobal = rnd.nextInt(50 - 31) + 31;
                colGlobal = rnd.nextInt(50 - 31) + 31;
            }
            //generate(row,col);
            combo_box.setDisable(false);
            buttonGenerate.setDisable(false);
            textField_mazeRows.setDisable(true);
            textField_mazeCols.setDisable(true);
        }
        else if(type.compareTo("Please choose")==0){
            return;
        }
        else {//customize
            rowGlobal=0;colGlobal=0;
            textField_mazeCols.setDisable(false);
            textField_mazeRows.setDisable(false);
            buttonGenerate.setDisable(false);
        }
    }

    public void generate(int row,int col){
        mvm.generateMaze(row,col);
    }

    public void set_update_player_position_row(String update_player_position_row) {
        this.update_player_position_row.set(update_player_position_row);
    }

    public void set_update_player_position_col(String update_player_position_col) {
        this.update_player_position_col.set(update_player_position_col);
    }

    public void generateMaze()
    {
        if(rowGlobal==0 && colGlobal==0) {
            int rows,cols;
            try {
                rows = Integer.parseInt(textField_mazeRows.getText());//Integer.valueOf(textField_mazeRows.getText());
                cols = Integer.parseInt(textField_mazeCols.getText());//Integer.valueOf(textField_mazeCols.getText());
            }catch(NumberFormatException e){
                alertLabel.setText("Please enter a number for both fields");
                return;
            }
            if(rows<2 || cols <2) {
                if (rows < 2 && cols < 2) {
                    alertLabel.setText("Please enter a number for both fields greater-equal than 2");
                    return;
                }
                else if(rows<2){
                    alertLabel.setText("Please enter a number for rows field greater-equal than 2");
                    return;
                }
                else{
                    alertLabel.setText("Please enter a number for cols field greater-equal than 2");
                    return;
                }
            }
            else if(rows>1000 || cols>1000){
                alertLabel.setText("Please enter a number for both fields lower than 1000");
                return;
            }
            wrongFile.setText("");
            alertLabel.setText("");
            generate(rows,cols);
            textField_mazeCols.setDisable(true);
            textField_mazeRows.setDisable(true);
        }
        else{
            wrongFile.setText("");
            alertLabel.setText("");
            generate(rowGlobal,colGlobal);
        }
        combo_box.setDisable(true);
        //combo_box.getSelectionModel().clearSelection();
        buttonSave.setDisable(false);
        buttonSolve.setDisable(false);
        buttonUnSolve.setDisable(true);
        mazeDisplayer.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                keyPressed(event);
            }
        });
        buttonGenerate.setDisable(true);
    }


    public void helpUnSolve(){
        mazeDisplayer.setSolFormal(null);
        mazeDisplayer.drawMaze(maze);
        buttonUnSolve.setDisable(true);
        buttonSolve.setDisable(false);
    }

    public void mouseMoveCharacter(MouseEvent e){
        if(maze!=null) {
            double positionX = (e.getX() / (mazeDisplayer.getWidth() / mvm.getMaze().getColSize()));
            double positionY = (e.getY() / (mazeDisplayer.getHeight() / mvm.getMaze().getRowSize()));

            if (positionX > mvm.getColPlayer() + 1) {
                mvm.moveCharacter(KeyCode.NUMPAD6);
            } else if (positionY > mvm.getRowPlayer() + 1) {
                mvm.moveCharacter(KeyCode.NUMPAD2);
            } else if (positionX < mvm.getColPlayer()) {
                mvm.moveCharacter(KeyCode.NUMPAD4);
            } else if (positionY < mvm.getRowPlayer()) {
                mvm.moveCharacter(KeyCode.NUMPAD8);
            }
        }
    }

    public void propertiesGame(){
        Stage primaryStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Properties.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("Properties");
            primaryStage.setScene(new Scene(root, 1272, 708));
            primaryStage.initModality(Modality.APPLICATION_MODAL);

            String image = getClass().getResource("/properties.png").toExternalForm();
            String style = "-fx-background-image: url('"+image+"');";
            (root).setStyle(style);
            ((AnchorPane)root).setStyle(style);

            Properties pc = new Properties();
            fxmlLoader.setController(pc);
            primaryStage.setOnCloseRequest(new EventHandler() {
                @Override
                public void handle(Event event) {
                    primaryStage.close();
                }
            });
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void aboutGame(){
        Stage primaryStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/About.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("FAQ");
            primaryStage.setScene(new Scene(root, 1272, 708));
            primaryStage.initModality(Modality.APPLICATION_MODAL);

            AboutController ac = new AboutController();
            /*wc.setViewControl(this);*/
            fxmlLoader.setController(ac);
            //mazeDisplayer.clear();
            primaryStage.setOnCloseRequest(new EventHandler() {
                @Override
                public void handle(Event event) {
                    primaryStage.close();
                }
            });
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void helpSolve(){
        if(mvm.getSol()==null){mvm.solveMaze(maze);}
        buttonSolve.setDisable(true);
        buttonUnSolve.setDisable(false);
        Solution sol =mvm.getSol();
        mazeDisplayer.drawSolution(sol,maze);
        mazeDisplayer.setSolFormal(sol);
    }

    public void winGame(){
        //WINNING STAGE
        if(music==true) {
            music = false;
            mediaPlayer.stop();
            musicButton.setSelected(false);
        }
        Stage primaryStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/WinWindow.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("YOU WON!");
            primaryStage.setScene(new Scene(root, 900, 1000));
            primaryStage.initModality(Modality.APPLICATION_MODAL);

            String image = getClass().getResource("/spongebob_win_titled.jpg").toExternalForm();
            String style = "-fx-background-image: url('" + image + "');";
            (root).setStyle(style);
            WinController wc = new WinController();
            fxmlLoader.setController(wc);
            mazeDisplayer.clear();
            maze=null;
            mvm.clear();
            buttonSolve.setDisable(true);
            buttonUnSolve.setDisable(true);
            buttonSave.setDisable(true);
            primaryStage.setOnCloseRequest(new EventHandler() {
                @Override
                public void handle(Event event) {
                    wc.close();
                }
            });
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void newGame() {
        mazeDisplayer.clear();
        maze = null;
        mvm.clear();
        wrongFile.setText("");
        buttonSave.setDisable(true);
        buttonSolve.setDisable(true);
        buttonUnSolve.setDisable(true);
        combo_box.setDisable(false);
        combo_box.getSelectionModel().select(0);
        textField_mazeCols.setDisable(true);
        textField_mazeRows.setDisable(true);
        buttonGenerate.setDisable(true);
    }

    public void gameInfo(){
        Stage primaryStage = new Stage();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/Help.fxml"));
            Parent root = fxmlLoader.load();
            primaryStage.setTitle("Information of the game");
            primaryStage.setScene(new Scene(root, 1272, 708));
            primaryStage.initModality(Modality.APPLICATION_MODAL);

            HelpController hc = new HelpController();
            fxmlLoader.setController(hc);
            primaryStage.setOnCloseRequest(new EventHandler() {
                @Override
                public void handle(Event event) {
                    primaryStage.close();
                }
            });
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Maze File");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze", "*.txt")
        );
        File file_returned = fileChooser.showOpenDialog((Stage)mazeDisplayer.getScene().getWindow());
        System.out.println("reading picked file..");
        Boolean bool = mvm.loadGame(file_returned);
        if(bool){
            alertLabel.setText("");
            wrongFile.setText("");
            maze = mvm.getMaze();
            mazeDisplayer.clear();
            mazeDisplayer.drawMaze(maze);
            buttonSolve.setDisable(false);
            buttonUnSolve.setDisable(true);
            combo_box.setDisable(true);
            buttonSave.setDisable(false);
            mazeDisplayer.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    keyPressed(event);
                }
            });
        }
        else{
            wrongFile.setText("Wrong file! Please choose a maze file");
            return;
        }
    }

    public void saveGame(){
        FileChooser fileChooser = new FileChooser();
        //DirectoryChooser dir = new DirectoryChooser();
        fileChooser.setTitle("Open Maze File");
        fileChooser.setInitialFileName(counter+"_Maze"+maze.getRowSize()+"_"+maze.getColSize());
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Maze", "*.txt")
        );
        File file_returned =fileChooser.showSaveDialog((Stage)mazeDisplayer.getScene().getWindow());
        mvm.saveGame(file_returned);
    }

    @FXML
    private boolean music;
    private  static MediaPlayer mediaPlayer;
    private Media sound;
    @FXML
    ToggleButton musicButton;
    public void music_controller(ActionEvent e){
        if(e.getEventType()==ActionEvent.ACTION) {
            if (music == false) {
                String ssound = getClass().getResource("/SpongeBob Closing Theme Song.mp3").toExternalForm();
                sound = new Media(ssound);
                mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.setVolume(0.1);
                mediaPlayer.setOnEndOfMedia(new Runnable() {
                    @Override
                    public void run() {
                        mediaPlayer.seek(Duration.ZERO);
                    }

                });
                music=true;
                mediaPlayer.play();
            } else {
                music=false;
                mediaPlayer.pause();
            }
        }
    }

    public void keyPressed(KeyEvent keyEvent) {
        mvm.moveCharacter(keyEvent.getCode());
        keyEvent.consume();
    }

    public void zoomKey(){
        mazeDisplayer.getScene().setOnScroll(

                new EventHandler<ScrollEvent>() {
                    @Override
                    public void handle(ScrollEvent event) {
                        if(event.isControlDown()) {
                            double zoomFactor = 1.05;
                            double deltaY = event.getDeltaY();

                            if (deltaY < 0) {
                                zoomFactor = 0.95;
                            }
                            mazeDisplayer.setScaleX(mazeDisplayer.getScaleX() * zoomFactor);
                            mazeDisplayer.setScaleY(mazeDisplayer.getScaleY() * zoomFactor);
                            event.consume();
                        }
                    }
                });
    }

    public void mouseClicked(MouseEvent mouseEvent) {
        mazeDisplayer.requestFocus();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MyViewModel)
        {
            if(maze == null)//generateMaze
            {
                this.maze = mvm.getMaze();
                mazeDisplayer.drawMaze(maze);
            }
            else {
                Maze maze2 = mvm.getMaze();

                if (maze2 == this.maze)//Not generateMaze
                {
                    int rowChar = mazeDisplayer.getRow_player();
                    int colChar = mazeDisplayer.getCol_player();
                    int rowFromViewModel = mvm.getRowPlayer();
                    int colFromViewModel = mvm.getColPlayer();

                    if(rowFromViewModel==maze.getGoalPosition().getRowIndex() && colFromViewModel==maze.getGoalPosition().getColumnIndex()){
                        winGame();
                    }
                    else if(rowFromViewModel == rowChar && colFromViewModel == colChar)//Solve Maze
                    {
                    }
                    else//Update location
                    {
                        set_update_player_position_row(rowFromViewModel + "");
                        set_update_player_position_col(colFromViewModel + "");
                        Solution sol = new Solution();
                        sol.setSolutionPath(mvm.getPlayerSol());
                        mazeDisplayer.setSolPlayer(sol);
                        this.mazeDisplayer.set_player_position(rowFromViewModel,colFromViewModel);
                    }


                }
                else//GenerateMaze
                {
                    this.maze = maze;
                    mazeDisplayer.drawMaze(maze);
                }
            }
        }

    }

    public void setViewModel(MyViewModel viewModel) {
        this.mvm = viewModel;
    }
}



