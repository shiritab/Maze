package ViewModel;

import Model.IModel;
import Model.MyModel;
import View.MyViewController;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {
    private IModel model;
    //private MyViewController mvc;
    private int rowPlayer;
    private int colPlayer;
    private Maze maze;

    public MyViewModel(IModel model) {
        this.model =model;
        this.model.assignObserver(this);
    }

    public void clear(){
        model.clear();
    }

    public Boolean loadGame(File file){
        return model.loadGame(file);
    }

    public void saveGame(File file){
        model.saveGame(file);
    }

    public void StopServers(){
        model.StopServers();
    }

    public void generateMaze(int row, int col){
        model.generate(row,col);
    }

    public ArrayList<AState> getPlayerSol(){
        return model.getPlayerSol();
    }

    public int getRowPlayer() {
        return model.getRowPlayer();
    }

    public int getColPlayer() {
        return model.getColPlayer();
    }

    public Solution getSol() {
        return model.getSol();
    }

    public Maze getMaze() {
        return maze;
    }

    public void solveMaze(Maze maze){
        model.solveMaze(maze);
    }
    public void moveCharacter(KeyCode keyCode) {
        int direction = -1;

        switch (keyCode) {
            case NUMPAD8://up
                direction = 8;
                model.updatePlayerLocation(direction);
                break;
            case NUMPAD2://down
                direction = 2;
                model.updatePlayerLocation(direction);
                break;
            case NUMPAD4://left
                direction = 4;
                model.updatePlayerLocation(direction);
                break;
            case NUMPAD6://right
                direction = 6;
                model.updatePlayerLocation(direction);
                break;
            case NUMPAD9://diagonal right up
                direction = 9;
                model.updatePlayerLocation(direction);
                break;
            case NUMPAD7://diagonal left up
                direction = 7;
                model.updatePlayerLocation(direction);
                break;
            case NUMPAD3://diagonal right down
                direction = 3;
                model.updatePlayerLocation(direction);
                break;
            case NUMPAD1://diagonal left down
                direction = 1;
                model.updatePlayerLocation(direction);
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel){
            if(o instanceof IModel)
            {
                if(maze == null)//generateMaze
                {
                    this.maze = model.getMaze();
                }
                else {
                    Maze maze2 = model.getMaze();

                    if (maze2 == this.maze)//Not generateMaze
                    {
                        int rowChar = model.getRowPlayer();
                        int colChar = model.getColPlayer();
                        if(rowChar==maze.getGoalPosition().getRowIndex() && colChar==maze.getGoalPosition().getColumnIndex()){
                            //we reached the end.
                            model.getPlayerSol();
                        }
                        if(this.colPlayer == colChar && this.rowPlayer == rowChar)//Solve Maze
                        {
                            model.getSol();
                        }
                        else//Update location
                        {
                            this.rowPlayer = rowChar;
                            this.colPlayer = colChar;

                        }


                    }
                    else//GenerateMaze
                    {
                        this.maze = maze2;
                    }
                }

                setChanged();
                notifyObservers();
            }

        }
    }
}
