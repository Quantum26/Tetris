/**
 * Tetrad class to be completed for Tetris project
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.*;

public class Tetrad
{
    private Block[] blocks;

    public Tetrad(BoundedGrid<Block> grid)
    {
        //Exercise 1.2  Insert code here to
        //                  instantiate blocks Block array (length 4)
        //                  initialize blocks array with new Block objects
        //                  declare color variable
        //                  declare and instantiate locs Location array (length 4)
        //                  declare shape variable and set equal to zero
        blocks = new Block[4];
        for(int i = 0; i<4; i++){
            blocks[i] = new Block();
        }
        Color color = Color.BLACK;
        Location[] locs = new Location[4];
        int shape = 0;

        //Exercise 2.0  Insert code here to
        //                  choose a random integer from 0 to 6
        shape = (int)(Math.random()*7);
        //Exercise 1.2  Insert code here to
        //                  branch (if statements) based on each shape number, to then
        //                      set the color variable for that shape
        //                      set the block locations for that shape
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
        }
        //Exercise 1.2  Insert code here (after the above if statements) to
        //                  loop through the blocks array to
        //                      set the color of each block
        //                  call addToLocations
        for(int i = 0; i < 4; i++){
            blocks[i].setColor(color);
        }
        this.addToLocations(grid, locs);
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
        for(int i = 0; i<4; i++){
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
        Location[] locs = new Location[4];
        for(int i = 0; i < 4; i++){
            locs[i] = blocks[i].getLocation();
            blocks[i].removeSelfFromGrid();
        }
        Location[] newLocs = new Location[4];
        for(int i = 0; i<4; i++){
            newLocs[i] = new Location(locs[i].getRow() + deltaRow, locs[i].getCol() + deltaCol);
        }
        boolean valid = true;
        for(int i = 0; i<4; i++){
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
        BoundedGrid<Block> g = blocks[0].getGrid();
        Location[] locs = new Location[4];
        for(int i = 0; i < 4; i++){
            locs[i] = blocks[i].getLocation();
            blocks[i].removeSelfFromGrid();
        }
        Location[] newLocs = new Location[4];
        for(int i = 0; i<4; i++){
            newLocs[i] = new Location(locs[1].getRow()-locs[1].getCol() + locs[i].getCol(), locs[1].getRow() +locs[1].getCol() - locs[i].getRow());
        }
        boolean valid = true;
        for(int i = 0; i<4; i++){
            valid = valid && g.isValid(newLocs[i]);
        }
        if(!(areEmpty(g,newLocs)&&valid)){
            this.addToLocations(g, locs);
            return false;
        }
        this.addToLocations(g, newLocs);
        return true;
    }
}