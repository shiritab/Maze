package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;

public class MazeDisplayer extends Canvas {
    private Maze maze;
    //private Canvas canvas;
    private int Row_player=-1;
    private int Col_player=-1;
    private Solution solPlayer;
    private Solution solFormal;

    public void clear(){
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0,0,getWidth(),getHeight());
        maze=null;
        Row_player = -1;
        Col_player = -1;
        solPlayer = null;
        solFormal =null;
    }

    public void drawSolution(Solution s, Maze maze){
        if(Row_player==0 && Col_player==0){
            Row_player = maze.getStartPosition().getRowIndex();
            Col_player = maze.getStartPosition().getColumnIndex();
        }
        double canvasHeight = getHeight();
        double canvasWidth = getWidth();
        int numOfRows = maze.getRowSize();
        int numOfCols = maze.getColSize();
        double heightOfCell = canvasHeight/numOfRows;
        double widthOfCell = canvasWidth/numOfCols;
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0,0,canvasWidth,canvasHeight);
        double x,y;

        int[][] matrix = maze.getTable();

        //gc.setFill(Color.LIGHTYELLOW);
        Image im3 = new Image(getClass().getResource("/Krabby_Patty.png").toExternalForm());
        for(int i=0;i<numOfRows;i++){
            for(int j=0;j<numOfCols;j++){
                x = i*heightOfCell;
                y = j*widthOfCell;
                if(matrix[i][j]==1){
                    gc.drawImage(im3,y,x,widthOfCell,heightOfCell);
                }
                else{
                    //
                }
            }
        }

        ArrayList<AState> lst = s.getSolutionPath();
        gc.setFill(Color.LIGHTGREEN);
        int startRow = maze.getStartPosition().getRowIndex();
        int startCol = maze.getStartPosition().getColumnIndex();
        int goalRow = maze.getGoalPosition().getRowIndex();
        int goalCol = maze.getGoalPosition().getColumnIndex();
        for(AState state : lst){
            state = (MazeState)state;
            x=((MazeState) state).getRow()*heightOfCell;
            y = ((MazeState) state).getCol()*widthOfCell;
            if(solPlayer ==null){
                gc.fillRect(y,x,widthOfCell,heightOfCell);}
            else{
                gc.fillRect(y,x,widthOfCell,heightOfCell);
            }
        }
        x = Row_player*heightOfCell;
        y = Col_player*widthOfCell;
        Image im = new Image(getClass().getResource("/Spongebob_photo.png").toExternalForm());
        gc.drawImage(im,y,x,widthOfCell,heightOfCell);
        //for end poing
        x = maze.getGoalPosition().getRowIndex()*heightOfCell;
        y = maze.getGoalPosition().getColumnIndex()*widthOfCell;
        //flag https://static.thenounproject.com/png/89993-200.png
        //krusty krab https://www.pngkit.com/png/full/325-3257729_the-krusty-krab-transparent-krusty-krab-no-background.png
        Image im1 = new Image(getClass().getResource("/Patric.jpg").toExternalForm());
        gc.drawImage(im1,y,x,widthOfCell,heightOfCell);
        this.solPlayer = s;
    }

    public void drawMaze(Maze maze) {
        if(maze==null){return;}

        if(Row_player==-1 && Col_player==-1){
            Row_player = maze.getStartPosition().getRowIndex();
            Col_player = maze.getStartPosition().getColumnIndex();
        }

        this.maze = maze;

        int numOfRows = maze.getRowSize();
        int numOfCols = maze.getColSize();

        double canvasHeight = /*canvas.*/getHeight();
        double canvasWidth = /*canvas.*/getWidth();

        double heightOfCell = canvasHeight/numOfRows;
        double widthOfCell = canvasWidth/numOfCols;

        GraphicsContext gc = /*canvas.*/getGraphicsContext2D();
        gc.clearRect(0,0,canvasWidth,canvasHeight);
        //gc.setFill(Color.AQUAMARINE);

        int[][] matrix = maze.getTable();
        double x,y;
        ArrayList<AState> solArr=new ArrayList<AState>();
        if(solFormal!=null){
            solArr = solFormal.getSolutionPath();
        }
        else if(solPlayer !=null){
            solArr = solPlayer.getSolutionPath();}

        Image im5 = new Image(getClass().getResource("/Krabby_Patty.png").toExternalForm());
        Image gary = new Image(getClass().getResource("/gary.png").toExternalForm());
        for(int i=0;i<numOfRows;i++){
            for(int j=0;j<numOfCols;j++){
                x = i*heightOfCell;
                y = j*widthOfCell;
                AState ms = new MazeState(new Position(i,j));
                if(solArr.size()>0){
                    if(solArr.contains(ms)) {
                        if(solPlayer.getSolutionPath().size()>0) {
                            if (solPlayer.getSolutionPath().get(0).equals(ms)) {
                                gc.drawImage(gary, y, x, widthOfCell, heightOfCell);
                            }
                            else {
                                gc.setFill(Color.LIGHTGREEN);
                                gc.fillRect(y, x, widthOfCell, heightOfCell);
                            }
                        }
                    }
                }
                if((i==maze.getStartPosition().getRowIndex() && j==maze.getStartPosition().getColumnIndex()) || (i==maze.getGoalPosition().getRowIndex() && j==maze.getGoalPosition().getColumnIndex())){continue;}
                if(matrix[i][j]==1){
                    //gc.setFill(Color.SANDYBROWN);
                    gc.drawImage(im5,y,x,widthOfCell,heightOfCell);
                }
                else{
                    //
                }
            }
        }
        //for start point
        x = this.Row_player*heightOfCell;
        y = this.Col_player*widthOfCell;
        Image im = new Image(getClass().getResource("/Spongebob_photo.png").toExternalForm());
        gc.drawImage(im,y,x,widthOfCell,heightOfCell);
        //for end poing
        x = maze.getGoalPosition().getRowIndex()*heightOfCell;
        y = maze.getGoalPosition().getColumnIndex()*widthOfCell;
        //flag https://static.thenounproject.com/png/89993-200.png
        //krusty krab https://www.pngkit.com/png/full/325-3257729_the-krusty-krab-transparent-krusty-krab-no-background.png

        Image im1 = new Image(getClass().getResource("/Patric.jpg").toExternalForm());
        gc.drawImage(im1,y,x,widthOfCell,heightOfCell);


    }

    public int getRow_player() {
        return Row_player;
    }

    public int getCol_player() {
        return Col_player;
    }

    public void setSolPlayer(Solution solPlayer) {
        this.solPlayer = solPlayer;
    }

    public void set_player_position(int player_row_position, int player_col_position) {
        Row_player = player_row_position;
        Col_player = player_col_position;
        drawMaze(this.maze);
    }

    public void setSolFormal(Solution sol) {
        this.solFormal = sol;
    }
}
