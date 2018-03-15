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
    int time = 1000;
    int gameTime = time;
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
            activeTetrad.translate(1,0);
            if(activeTetrad.translate(1,0)!=true){
                gameTime = time;
                Location[] l = activeTetrad.getLocations();
                for(int i = 0; i<4; i++){
                    if(l[i].getRow() == 0){
                    game = false;
                    break;
                }
                }
                activeTetrad = new Tetrad(grid);
            }
            display.showBlocks();
        }
        
    }

    //precondition:  0 <= row < number of rows
    //postcondition: Returns true if every cell in the
    //               given row is occupied;
    //               returns false otherwise.
    private boolean isCompletedRow(int row)
    {
        throw new RuntimeException("Insert Exercise 4.0 code here");    // replace this line
    }

    //precondition:  0 <= row < number of rows;
    //               given row is full of blocks
    //postcondition: Every block in the given row has been
    //               removed, and every block above row
    //               has been moved down one row.
    private void clearRow(int row)
    {
        throw new RuntimeException("Insert Exercise 4.0 code here");    // replace this line
    }

    //postcondition: All completed rows have been cleared.
    private void clearCompletedRows()
    {
        throw new RuntimeException("Insert Exercise 4.0 code here");    // replace this line
    }

    //returns true if top two rows of the grid are empty (no blocks), false otherwise
    private boolean topRowsEmpty()
    {
        throw new RuntimeException("Insert Exercise 4.1 code here");    // replace this line
    }

}