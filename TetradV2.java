/**
 * Tetrad class to be completed for Tetris project
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.*;

public class TetradV2
{
    private Block[] blocks;
    private int shape;
    private BoundedGrid<Block> g;
    private Color color;
    public TetradV2(BoundedGrid<Block> grid)
    {
        blocks = new Block[4];
        for(int i = 0; i<4; i++){
            blocks[i] = new Block();
        }
        g = grid;
        shape = 7;
        //Exercise 2.0  Insert code here to
        //                  choose a random integer from 0 to 6
        shape = (int)(Math.random()*7);
    }

    public void SpawnTetrad(){
        Location[] locs = new Location[4];
        color = Color.BLACK;
        if(shape == 0){ //I
            color = Color.BLUE;
            locs[0] = new Location(0,3);
            locs[1] = new Location(0,4);
            locs[2] = new Location(0,5);
            locs[3] = new Location(0,6);
        }else if(shape == 1){ //T
            color = new Color (127,255,212);
            locs[0] = new Location(0,3);
            locs[1] = new Location(0,4);
            locs[2] = new Location(0,5);
            locs[3] = new Location(1,4);
        }else if(shape == 2){ //O
            color = new Color (173,216,230);
            locs[0] = new Location(1,5);
            locs[1] = new Location(0,4);
            locs[2] = new Location(0,5);
            locs[3] = new Location(1,4);
        }else if(shape == 3){ //L
            color = new Color (135,206,235);
            locs[0] = new Location(0,4);
            locs[1] = new Location(1,4);
            locs[2] = new Location(2,4);
            locs[3] = new Location(2,5);
        }else if(shape == 4){ //J
            color = new Color (30,144,255);
            locs[0] = new Location(0,5);
            locs[1] = new Location(1,5);
            locs[2] = new Location(2,5);
            locs[3] = new Location(2,4);
        }else if(shape == 5){ //S
            color = new Color (70,130,180);
            locs[0] = new Location(1,5);
            locs[1] = new Location(0,5);
            locs[2] = new Location(0,6);
            locs[3] = new Location(1,4);
        }else if(shape == 6){ //Z
            color = new Color (176,224,230);
            locs[0] = new Location(0,3);
            locs[1] = new Location(0,4);
            locs[2] = new Location(1,5);
            locs[3] = new Location(1,4);
        }else if(shape == 7){

            color = Color.BLACK;
            blocks = new Block[30];
            for(int i = 0; i < blocks.length; i++)
            {
                blocks[i] = new Block();
            }
            locs = new Location[30];
            locs[0] = new Location(0, 4);
            locs[1] = new Location(0, 5);
            locs[2] = new Location(1, 2);
            locs[3] = new Location(1, 3);
            locs[4] = new Location(1, 6);
            locs[5] = new Location(1, 7);
            locs[6] = new Location(2, 1);
            locs[7] = new Location(2, 8);
            locs[8] = new Location(3, 1);
            locs[9] = new Location(3, 3);
            locs[10] = new Location(3, 6);
            locs[11] = new Location(3, 8);
            locs[12] = new Location(4, 0);
            locs[13] = new Location(4, 9);
            locs[14] = new Location(5, 0);
            locs[15] = new Location(5, 3);
            locs[16] = new Location(5, 6);
            locs[17] = new Location(5, 9);
            locs[18] = new Location(6, 1);
            locs[19] = new Location(6, 4);
            locs[20] = new Location(6, 5);
            locs[21] = new Location(6, 8);
            locs[22] = new Location(7, 1);
            locs[23] = new Location(7, 8);
            locs[24] = new Location(8, 2);
            locs[25] = new Location(8, 3);
            locs[26] = new Location(8, 6);
            locs[27] = new Location(8, 7);
            locs[28] = new Location(9, 4);
            locs[29] = new Location(9, 5);

        }
        //Exercise 1.2  Insert code here (after the above if statements) to
        //                  loop through the blocks array to
        //                      set the color of each block
        //                  call addToLocations
        for(int i = 0; i < blocks.length; i++){
            blocks[i].setColor(color);
        }
        this.addToLocations(g, locs);
    }

    public int getShape(){
        return shape;
    }
    //precondition:  blocks are not in any grid;
    //               blocks.length = locs.length = 4.
    //postcondition: The locations of blocks match locs,
    //               and blocks have been put in the grid.
    private void addToLocations(BoundedGrid<Block> grid, Location[] locs)
    {
        for(int i = 0; i<locs.length; i++){
            blocks[i].putSelfInGrid(grid, locs[i]);
        }
    }

    //precondition:  Blocks are in the grid.
    //postcondition: Returns old locations of blocks;
    //               blocks have been removed from grid.
    private Location[] removeBlocks()
    {
        Location[] locs = new Location[4];
        for(int i = 0; i<blocks.length; i++){
            locs[i] = blocks[i].getLocation();
            blocks[i].removeSelfFromGrid();
        }
        return locs;
    }

    public Location[] getLocations()
    {
        Location[] l = new Location[4];
        for(int i = 0; i<blocks.length; i++){
            l[i] = blocks[i].getLocation();
        }
        return l;
    }

    //postcondition: Returns true if each of locs is
    //               valid (on the board) AND empty in
    //               grid; false otherwise.
    private boolean areEmpty(BoundedGrid<Block> grid,
    Location[] locs)
    {
        for(Location l: locs){
            if(grid.isValid(l))
                if(grid.get(l)!=null)
                    return false;
        }
        return true;
    }

    //postcondition: Attempts to move this tetrad deltaRow
    //               rows down and deltaCol columns to the
    //               right, if those positions are valid
    //               and empty; returns true if successful
    //               and false otherwise.
    public boolean translate(int deltaRow, int deltaCol)
    {
        //Exercise 2.2    Insert code here to
        //              ask any block for its grid and store value
        //              remove the blocks (but save the locations)
        //              create an array of the new (possible) locations
        //              check if the new locations are empty
        //              replace the tetrad in the proper place (translated)
        //              return true if moved, false if not moved
        BoundedGrid<Block> g = blocks[0].getGrid();
        Location[] locs = new Location[blocks.length];
        for(int i = 0; i < blocks.length; i++){
            locs[i] = blocks[i].getLocation();
            blocks[i].removeSelfFromGrid();
        }
        Location[] newLocs = new Location[blocks.length];
        for(int i = 0; i<blocks.length; i++){
            newLocs[i] = new Location(locs[i].getRow() + deltaRow, locs[i].getCol() + deltaCol);
        }
        boolean valid = true;
        for(int i = 0; i<blocks.length; i++){
            valid = valid && g.isValid(newLocs[i]);
        }
        if(!(areEmpty(g,newLocs)&&valid)){
            this.addToLocations(g, locs);
            return false;
        }
        this.addToLocations(g, newLocs);
        return true;
    }

    //postcondition: Attempts to rotate this tetrad
    //               clockwise by 90 degrees about its
    //               center, if the necessary positions
    //               are empty; returns true if successful
    //               and false otherwise.
    public boolean rotate()
    {
        //Exercise 3.0  Insert code here to
        //              ask any block for its grid and store value
        //              remove the blocks (but save the locations)
        //              check if the new locations are empty
        //              replace the tetrad in the proper place (rotated)
        if(shape == 2){
            return false;
        }
        BoundedGrid<Block> g = blocks[0].getGrid();
        Location[] locs = new Location[blocks.length];
        for(int i = 0; i < blocks.length; i++){
            locs[i] = blocks[i].getLocation();
            blocks[i].removeSelfFromGrid();
        }
        Location[] newLocs = new Location[blocks.length];
        for(int i = 0; i<blocks.length; i++){
            newLocs[i] = new Location(locs[1].getRow()-locs[1].getCol() + locs[i].getCol(), locs[1].getRow() +locs[1].getCol() - locs[i].getRow());
        }
        boolean valid = true;
        for(int i = 0; i<blocks.length; i++){
            valid = valid && g.isValid(newLocs[i]);
        }
        if(!areEmpty(g,newLocs)||!valid){
            this.addToLocations(g, locs);
            return false;
        }
        this.addToLocations(g, newLocs);
        return true;
    }
    public void resetColor(){
        for(int i = 0; i<blocks.length; i++){
            blocks[i].setColor(color);
        }
    }
}