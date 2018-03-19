import java.util.*;
import java.awt.Color;
/**
 * Tetris class to be completed for Tetris project
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tetris implements ArrowListener
{
    public static void main(String[] args)
    {
        Tetris tetris = new Tetris();
        tetris.play();
    }
    int time;
    int gameTime;
    private int level;
    private int score;
    private int rowsDone;
    private boolean gotTetris;
    private BoundedGrid<Block> grid;
    private BlockDisplay display;
    private Tetrad activeTetrad;
    private TetrisScore scoreBoard;

    public Tetris()
    {
        grid = new BoundedGrid<Block>(20, 10);
        display = new BlockDisplay(grid);
        display.setArrowListener(this);
        display.setTitle("Tetris");
        activeTetrad = new Tetrad(grid);
        scoreBoard = new TetrisScore();
        time = 1000;
        gameTime = 1000;
        level = 0;
        score = 0;
        gotTetris = false;
        rowsDone = 0;
    }

    public void upPressed()
    {
        activeTetrad.rotate();
        display.showBlocks();
    }

    public void downPressed()
    {
        activeTetrad.translate(1, 0);
        display.showBlocks();
    }

    public void leftPressed()
    {
        activeTetrad.translate(0, -1);
        display.showBlocks();
    }

    public void rightPressed()
    {
        activeTetrad.translate(0, 1);
        display.showBlocks();
    }

    public void spacePressed()
    {
        while(activeTetrad.translate(1,0)){}
        gameTime = 0;
        display.showBlocks();
    }

    public void play()
    {
        boolean game = true;
        while (game)
        {
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}

            if(!activeTetrad.translate(1,0)&&topRowsEmpty()){
                gameTime = time;
                Location[] l = activeTetrad.getLocations();
                for(int i = 0; i<4; i++){
                    if(l[i].getRow() == 0){
                        game = false;
                        break;
                    }
                }
                if(game){
                    clearCompletedRows();
                    activeTetrad = new Tetrad(grid);
                }
            }
            if(game==false){
                break;
            }
            display.showBlocks();
            display.setTitle("Level "+level+", Score: "+score);
        }

    }


    //precondition:  0 <= row < number of rows
    //postcondition: Returns true if every cell in the
    //               given row is occupied;
    //               returns false otherwise.
    private boolean isCompletedRow(int row)
    {
        boolean full = true;
        for(int i = 0; i < 10; i++){
            Location l = new Location(row, i);
            if(grid.get(l)==null){
                full = false;
            }
        }
        return full;
    }

    //precondition:  0 <= row < number of rows;
    //               given row is full of blocks
    //postcondition: Every block in the given row has been
    //               removed, and every block above row
    //               has been moved down one row.
    private void clearRow(int row)
    {
        for(int i = 0; i<10; i++){
            Location l = new Location(row, i);
            grid.remove(l).removeSelfFromGrid();

        }
    }

    //postcondition: All completed rows have been cleared.
    private void clearCompletedRows()
    {
        int rowsBroke = 0;
        for(int i = 19; i > -0; i--){
            if(isCompletedRow(i)){
                flashRow(i);
                clearRow(i);
                moveDownAbove(i);
                i = 20;
                rowsBroke++;
                rowsDone++;
            }

        }

        int scor = (gotTetris)?((rowsBroke==4)?1600:rowsBroke*100):((rowsBroke==4)?800:rowsBroke*100);
        score+= scor;
        if(rowsDone==10){
            if(time>200){
                level++;
                time-= 200;
                rowsDone = 0;
            }else if (time>25){
                level++;
                time-= 25;
                rowsDone = 0;
            }else{
                level++;
                rowsDone = 0;
            }
        }
        display.showBlocks();
    }

    private void moveDownAbove(int r){
        if(r<1){
            return;
        }
        List<Location> locs = grid.getRow(r);
        for(Location l: locs){
            Location x = new Location(l.getRow()+1, l.getCol());
            if(grid.isValid(x)&&grid.get(x)==null){
                grid.remove(l).moveTo(x);
            }
        }
        moveDownAbove(r-1);
    }

    public void flashRow(int row){
        Color[] colors = new Color[10];
        for(int k = 0; k<2; k++){
            for(int i = 0; i< 10; i++){
                Location l = new Location(row, i);
                colors[i] = grid.get(l).getColor();
                grid.get(l).setColor(new Color(0, 0, 0));

            }
            display.showBlocks();
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
            for(int i = 0; i< 10; i++){
                Location l = new Location(row, i);
                if(colors[i]!=null && grid.get(l)!=null)
                    grid.get(l).setColor(colors[i]);

            }
            display.showBlocks();
            try { Thread.sleep(gameTime/10); } catch(Exception e) {}
        }
    }

    //returns true if top two rows of the grid are empty (no blocks), false otherwise
    private boolean topRowsEmpty()
    {
        boolean top = true;
        boolean atop = true;
        for(int i = 0; i<10; i++){
            Location l = new Location(0, i);
            if(grid.get(l)!=null){
                top = false;
            }
        }
        for(int i = 0; i<10; i++){
            Location l = new Location(1, i);
            if(grid.get(l)!=null){
                atop = false;
            }
        }
        return top &&atop;
    }

}