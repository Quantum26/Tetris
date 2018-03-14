/**
 * Tetris class to be completed for Tetris project
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tetris
{
    public static void main(String[] args)
    {
        Tetris tetris = new Tetris();
        tetris.play();
    }

    private BoundedGrid<Block> grid;
    private BlockDisplay display;
    private Tetrad activeTetrad;

    public Tetris()
    {
        grid = new BoundedGrid<Block>(20, 10);
        display = new BlockDisplay(grid);
        display.setTitle("Tetris");
        activeTetrad = new Tetrad(grid);
    }

    public void upPressed()
    {
        //Insert Exercise 2.3 code here
    }

    public void downPressed()
    {
        //Insert Exercise 2.3 code here
    }

    public void leftPressed()
    {
        //Insert Exercise 2.3 code here
    }

    public void rightPressed()
    {
        //Insert Exercise 2.3 code here
    }

    public void spacePressed()
    {
        //Insert Exercise 2.3 code here
    }

    public void play()
    {
        while (true)
        {
            try { Thread.sleep(1000); } catch(Exception e) {}

            //Insert Exercise 3.2 code here
            //Insert Exercise 3.3 code here

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