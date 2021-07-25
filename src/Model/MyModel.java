package Model;

import Client.*;
import IO.MyDecompressorInputStream;
import Server.*;
import ViewModel.MyViewModel;
import algorithms.mazeGenerators.IMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
/*import org.apache.log4j.*;*/

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements IModel {
    private MyViewModel mvm;
    private IMazeGenerator mazeGenerator;
    private int rowPlayer=-1;
    private int colPlayer=-1;
    private Maze maze;
    private Solution sol;
    private Server serverGenerate;
    private Server serverSolver;
    private ArrayList<AState> playerSol;
    private Client clientGenerate;
    private Client clientSolve;
    //private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MyModel.class);

    public MyModel() {
        serverGenerate = new Server(5413, 1000, new ServerStrategyGenerateMaze());
        serverSolver = new Server(5414, 1000, new ServerStrategySolveSearchProblem());
        serverGenerate.start();
        serverSolver.start();
        playerSol= new ArrayList<AState>();
        /*SimpleLayout sl = new SimpleLayout();
        Appender a = null;
        try {
            a = new FileAppender(sl, "Logs/Log.txt");

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.setLevel(Level.INFO);
        logger.info("info msg");
        logger.trace("trace msg");
        logger.fatal("fatal msg");
        logger.error("error msg");
        logger.debug("debug msg");
        logger.addAppender(a);*/
    }



    public void clear(){
        rowPlayer=-1;
        colPlayer=-1;
        maze=null;
        playerSol = new ArrayList<AState>();
        sol = null;
    }

    public void StopServers(){
        serverGenerate.stop();
        //logger.info("Sever Startegy Generate Maze has stopped.");
        serverSolver.stop();
        //logger.info("Sever Startegy Solve Search Problem has stopped.");
    }

    @Override
    public void generate(final int row, final int col) {
        if(mazeGenerator==null){mazeGenerator=new MyMazeGenerator();}
        try {
            clientGenerate = new Client(InetAddress.getLocalHost(), 5413, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        int[] mazeDimensions = new int[]{row, col};
                        toServer.writeObject(mazeDimensions);
                        toServer.flush();
                        byte[] compressedMaze = (byte[]) ((byte[]) fromServer.readObject());
                        InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                        byte[] decompressedMaze = new byte[compressedMaze.length * 8];
                        is.read(decompressedMaze);
                        maze = new Maze(decompressedMaze);
                        rowPlayer=maze.getStartPosition().getRowIndex();
                        colPlayer=maze.getStartPosition().getColumnIndex();
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }

            });
            //logger.info(String.format("Maze rows: %d, cols: %d",row,col));
            clientGenerate.communicateWithServer();
            //logger.info(String.format("%s Client has been connected to Generate Maze Server",clientGenerate.toString()));
        }catch(IOException e){

        }
        setChanged();
        notifyObservers();
    }

    public Maze getMaze() {
        return maze;
    }

    @Override
    public void solveMaze(final Maze maze) {
        try {
            clientSolve = new Client(InetAddress.getLocalHost(), 5414, new IClientStrategy() {
                public void clientStrategy(InputStream inFromServer, OutputStream outToServer) {
                    try {
                        ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                        ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                        toServer.flush();
                        toServer.writeObject(maze);
                        toServer.flush();
                        sol = (Solution) fromServer.readObject();
                        System.out.println(String.format("Solution steps: %s", sol));
                        ArrayList<AState> mazeSolutionSteps = sol.getSolutionPath();

                        for (int i = 0; i < mazeSolutionSteps.size(); ++i) {
                            System.out.println(String.format("%s. %s", i, ((AState) mazeSolutionSteps.get(i)).toString()));
                        }
                    } catch (Exception var10) {
                        var10.printStackTrace();
                    }

                }
            });
            clientSolve.communicateWithServer();
            //logger.info(String.format("%s Client has been connected to Solve Search Server",clientSolve.toString()));
        }catch (IOException e){

        }
        setChanged();
        notifyObservers();
    }

    public Solution getSol() {
        return sol;
    }

    public int getRowPlayer() {
        return rowPlayer;
    }

    public int getColPlayer() {
        return colPlayer;
    }

    public ArrayList<AState> getPlayerSol() {
        return playerSol;
    }

    public void updatePlayerLocation(int direction)
    {
        playerSol.add(new MazeState(new Position(rowPlayer,colPlayer)));
        switch(direction)
        {
            case 8: //Up
                if(rowPlayer>0 && maze.getTable()[rowPlayer-1][colPlayer]!=1)
                {rowPlayer--;}
                break;

            case 2: //Down
                if(rowPlayer<maze.getRowSize()-1 && maze.getTable()[rowPlayer+1][colPlayer]!=1)
                {rowPlayer++;}
                break;
            case 4: //Left
                if(colPlayer>0 && maze.getTable()[rowPlayer][colPlayer-1]!=1)
                {colPlayer--;}
                break;
            case 6: //Right
                if(colPlayer<maze.getColSize()-1 && maze.getTable()[rowPlayer][colPlayer+1]!=1)
                {colPlayer++;}
                break;
            case 9://diagonal right up
                if((colPlayer<maze.getColSize()-1 && rowPlayer>0) && (maze.getTable()[rowPlayer-1][colPlayer]!=1 || maze.getTable()[rowPlayer][colPlayer+1]!=1) && ( maze.getTable()[rowPlayer-1][colPlayer+1]!=1))
                {colPlayer++;rowPlayer--;}
                break;
            case 7://diagonal left up
                if((colPlayer>0 && rowPlayer>0) && (maze.getTable()[rowPlayer-1][colPlayer]!=1 || maze.getTable()[rowPlayer][colPlayer-1]!=1) && ( maze.getTable()[rowPlayer-1][colPlayer-1]!=1))
                {colPlayer--;rowPlayer--;}
                break;
            case 3://diagonal right down
                if((colPlayer<maze.getColSize()-1 && rowPlayer<maze.getRowSize()-1) && (maze.getTable()[rowPlayer+1][colPlayer]!=1 || maze.getTable()[rowPlayer][colPlayer+1]!=1) && ( maze.getTable()[rowPlayer+1][colPlayer+1]!=1))
                {colPlayer++;rowPlayer++;}
                break;
            case 1://diagonal left down
                if((colPlayer>0 && rowPlayer<maze.getRowSize()-1) && (maze.getTable()[rowPlayer+1][colPlayer]!=1 || maze.getTable()[rowPlayer][colPlayer-1]!=1) && ( maze.getTable()[rowPlayer+1][colPlayer-1]!=1))
                {colPlayer--;rowPlayer++;}
                break;
        }

        setChanged();
        notifyObservers();
    }

    public Boolean loadGame(File file){
        String file_name = file.getAbsolutePath();
        try {
            FileInputStream fileIn = new FileInputStream(new File(file_name));
            ObjectInputStream file_Reader = new ObjectInputStream(fileIn);
            Object obj = file_Reader.readObject();
            if(obj instanceof Maze){
                maze = (Maze)obj;
                rowPlayer=maze.getStartPosition().getRowIndex();
                colPlayer=maze.getStartPosition().getColumnIndex();
                setChanged();
                notifyObservers();
                return true;
            }
            else{
                return false;
            }
        }catch(IOException | ClassNotFoundException e){
            return false;
        }
    }

    public void saveGame(File file){
        String file_name = file.getName();
        String dir_name = file.getParent();

        try {
            File newFileMaze = new File(new File(dir_name), file_name);
            newFileMaze.createNewFile();
            FileOutputStream outputFileMaze = new FileOutputStream(newFileMaze);
            ObjectOutputStream curMaze = new ObjectOutputStream(outputFileMaze);
            curMaze.writeObject(maze);
            curMaze.flush();
            curMaze.close();
            outputFileMaze.close();
        }catch(IOException e){

        }
    }

    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }
}
