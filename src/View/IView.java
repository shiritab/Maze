package View;

import ViewModel.MyViewModel;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;


public interface IView {
    void CloseProgram();
    void maze_Automatic();
    void generate(int row,int col);
    void set_update_player_position_row(String update_player_position_row);
    void set_update_player_position_col(String update_player_position_col);
    void generateMaze();
    void helpUnSolve();
    void mouseMoveCharacter(MouseEvent e);
    void propertiesGame();
    void aboutGame();
    void helpSolve();
    void winGame();
    void newGame();
    void gameInfo();
    void loadGame();
    void saveGame();
    void music_controller(ActionEvent e);
    void keyPressed(KeyEvent keyEvent);
    void zoomKey();
    void mouseClicked(MouseEvent mouseEvent);
    void setViewModel(MyViewModel viewModel);
}
