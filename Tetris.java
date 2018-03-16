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
        boolean x;
        Scanner s = new Scanner(System.in);
        System.out.println("Would you like to enable chain reactions?(y/n)");
        x = s.nextLine().equals("y");
        
        Tetris tetris = new Tetris();
        if(x){
            tetris.switchMode();
        }
        System.out.println(tetris.getChain());
        tetris.play();
    }
    int time = 1000;
    int gameTime = time;
    private boolean chain;
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
        chain = false;
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
        while(activeTetrad.translate(1,0))
            activeTetrad.translate(1, 0);
        gameTime = 0;
        display.showBlocks();
    }

    public void switchMode(){
        chain = !chain;
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
                clearCompletedRows();
                activeTetrad = new Tetrad(grid);
            }

            display.showBlocks();

        }

    }
    
    public boolean getChain(){
        return chain;
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
        for(int i = 19; i > -0; i--){
            if(isCompletedRow(i)){
                flashRow(i);
                clearRow(i);
                if(chain){
                    chainDown(i);
                }else{
                    moveDownAbove(i);
                }
                i = 20;

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
    
    private void moveDownAboveCont(int r){
        if(r<1){
            return;
        }
        List<Location> locs = grid.getOccupiedLocations();
        for(Location l: locs){
            Location x = grid.getLocBelow(l);
            if(grid.isValid(x)&&grid.get(x)==null){
                grid.remove(l).moveTo(x);
            }
        }
        moveDownAbove(r-1);
    }
    
    private void chainDown(int r){
        moveDownAbove(r);
        while(activeTetrad.translate(1,0)){}
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