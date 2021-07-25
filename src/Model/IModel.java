package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;

import java.io.File;
import java.util.ArrayList;
import java.util.Observer;

public interface IModel {
    public void generate(int row, int col);
    public void solveMaze(Maze maze);
    public void assignObserver(Observer o);
    public Maze getMaze();
    public Solution getSol();
    public int getRowPlayer();
    public int getColPlayer();
    public void updatePlayerLocation(int direction);
    public ArrayList<AState> getPlayerSol();
    public void StopServers();
    public void clear();
    public Boolean loadGame(File file);
    public void saveGame(File file);

}
