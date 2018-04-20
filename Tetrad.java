/**
 * Tetrad class to be completed for Tetris project
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.*;
import javafx.util.Duration;

public class Tetrad
{
    private Block[] blocks;
    private int shape;//94 indicates custom shape
    private int r;
    private int c;
    private BoundedGrid<Block> g;
    private Color color;
    private boolean spawned;
    boolean hasShape;
    Location[] lol;
    public Tetrad(BoundedGrid<Block> grid)
    {
        blocks = new Block[4];
        for(int i = 0; i<4; i++){
            blocks[i] = new Block();
        }
        g = grid;
        //shape = 7;
        //Exercise 2.0  Insert code here to
        //                  choose a random integer from 0 to 6
        spawned = false;
        shape = (int)(Math.random()*8);
        hasShape = true;
        r = g.getNumRows();
        c = g.getNumCols();
    }

    public Tetrad(BoundedGrid<Block> grid, Location[] locs, Color co){
        blocks = new Block[locs.length];
        lol = new Location[locs.length];

        for(int i = 0; i<blocks.length; i++){
            blocks[i] = new Block();
            lol[i] = locs[i];
        }

        g = grid;
        color = co;
        shape = 94;
        spawned = false;
        hasShape = false;
        r = g.getNumRows();
        c = g.getNumCols();
    }

    public void SpawnTetrad(){
        if(hasShape){
            Location[] locs = new Location[4];
            color = Color.BLACK;
            switch(shape){ 
                case 0://I
                color = Color.BLUE;
                locs[0] = new Location(0,c/2-1);
                locs[1] = new Location(1,c/2-1);
                locs[2] = new Location(2,c/2-1);
                locs[3] = new Location(3,c/2-1);
                break;

                case 1: //T
                color = Color.MAGENTA;
                locs[0] = new Location(0,c/2-2);
                locs[1] = new Location(0,c/2-1);
                locs[2] = new Location(0,c/2);
                locs[3] = new Location(1,c/2-1);
                break;

                case 2: //O
                color = Color.RED;
                locs[0] = new Location(1,c/2);
                locs[1] = new Location(0,c/2-1);
                locs[2] = new Location(0,c/2);
                locs[3] = new Location(1,c/2-1);
                break;

                case 3://L
                color = Color.ORANGE;
                locs[0] = new Location(0,c/2-1);
                locs[1] = new Location(1,c/2-1);
                locs[2] = new Location(2,c/2-1);
                locs[3] = new Location(2,c/2);
                break;

                case 4://J
                color = Color.YELLOW;
                locs[0] = new Location(0,c/2);
                locs[1] = new Location(1,c/2);
                locs[2] = new Location(2,c/2);
                locs[3] = new Location(2,c/2-1);
                break;

                case 5://S
                color = Color.GREEN;
                locs[0] = new Location(1,c/2);
                locs[1] = new Location(0,c/2);
                locs[2] = new Location(0,c/2+1);
                locs[3] = new Location(1,c/2-1);
                break;

                case 6://Z
                color = Color.CYAN;
                locs[0] = new Location(0,c/2-2);
                locs[1] = new Location(0,c/2-1);
                locs[2] = new Location(1,c/2);
                locs[3] = new Location(1,c/2-1);
                break;

                case 7://smiley face death tetrad

                color = Color.WHITE;
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
                break;

                case 8://a full 4 rows
                color = Color.BLUE;
                blocks = new Block[40];
                for(int i = 0; i < blocks.length; i++)
                {
                    blocks[i] = new Block();
                }
                locs = new Location[40];
                int i = 0;
                for(int r= 0; r<4;r++){
                    for(int c = 0; c<10;c++){
                        locs[i] = new Location(r, c);
                        i++;
                    }
                }
                break;

                case 9://a meteor
                color = Color.BLUE;
                locs[0] = new Location(0, 4);
                locs[1] = new Location(1, 4);
                locs[2] = new Location(2, 4);
                locs[3] = new Location(3, 4);
                break;

                case 10://block for controlling movement at bottom
                color = Color.WHITE;
                locs[0] = new Location(19,4);
                locs[1] = new Location(19,5);
                locs[2] = new Location(18,4);
                locs[3] = new Location(18,5);
                break;

                case 11://half control block for deja vu mode
                color = Color.WHITE;
                blocks = new Block[2];
                blocks[0] = new Block();
                blocks[1] = new Block();
                locs = new Location[2];
                locs[0] = new Location (19,1);
                locs[1] = new Location (18,1);
                break;
                
                case 12://other half of the control block for deja vu mode
                color = Color.WHITE;
                blocks = new Block[2];
                blocks[0] = new Block();
                blocks[1] = new Block();
                locs = new Location[2];
                locs[0] = new Location (19,8);
                locs[1] = new Location (18,8);
                break;
                
                case 13://galaga main players ship
                color = color.WHITE;
                locs[0] = new Location(19,3);
                locs[1] = new Location(19,4);
                locs[2] = new Location(19,5);
                locs[3] = new Location(18,4);
                break;
                
                case 14://galaga bullet
                blocks = new Block[1];
                blocks[0] = new Block();
                color = Color.GREEN;
                locs = new Location[1];
                locs[0] = new Location(17, 4);
                break;
                
                default://default tetrad, should never occur
                color = Color.WHITE;
                locs[0] = new Location(0,3);
                locs[1] = new Location(0,4);
                locs[2] = new Location(1,5);
                locs[3] = new Location(1,4);
                break;

            }
            //Exercise 1.2  Insert code here (after the above if statements) to
            //                  loop through the blocks array to
            //                      set the color of each block
            //                  call addToLocations
            for(int i = 0; i < blocks.length; i++){//sets block colors
                blocks[i].setColor(color);//sets current color
                blocks[i].setOriginal(color);//sets original color
            }
            if(shape == 9){//sets meteors color to look like a meteor
                blocks[0].setColor(Color.YELLOW);blocks[0].setOriginal(Color.YELLOW);
                blocks[1].setColor(Color.ORANGE);blocks[0].setOriginal(Color.ORANGE);
                blocks[2].setColor(Color.RED);blocks[0].setOriginal(Color.RED);
                Color brown = new Color(166, 91, 41);
                blocks[3].setColor(brown);blocks[0].setOriginal(brown);
            }
            this.addToLocations(g, locs);//adds tetrad to grid
            spawned = true;//tetrad is spawned
        }else{
            for(int i = 0; i < blocks.length; i++){
                blocks[i].setColor(color);
                blocks[i].setOriginal(color);
            }
            spawned = true;
            this.addToLocations(g, lol);
        }
    }
    
    public void setColor(Color c){
        for(Block b: blocks){
            b.setColor(c);
        }
    }
    
    public void setColor(Color[] c){
        for(int i = 0; i<blocks.length; i++){
            blocks[i].setColor(c[i]);
        }
    }

    public void setShape(int s){
        shape = s;
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
    public Location[] removeBlocks()
    {
        spawned = false;
        Location[] locs = new Location[blocks.length];
        for(int i = 0; i<blocks.length; i++){
            locs[i] = blocks[i].getLocation();
            blocks[i].removeSelfFromGrid();
        }
        return locs;
    }

    public Location[] getLocations()
    {
        Location[] l = new Location[blocks.length];
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
            if(locs[i]==null)
                return false;
            blocks[i].destroySelfFromGrid();
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
        if(shape == 2||shape == 10){
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

    public boolean isOnGround(){

        for(Block b:blocks){
            if(b.getLocation()==null || b.getLocation().getRow()==g.getNumRows()-1){
                return true;
            }
        }

        return false;
    }

    public boolean isNextToSomething(){
        Block b = blocks[blocks.length-1];
        Location l = b.getLocation();
        if(l==null)
            return false;
        Location right = new Location(l.getRow(), l.getCol()-1);
        Location left = new Location(l.getRow(), l.getCol()+1);
        if(g.isValid(right)&&g.get(right)!=null){
            return true;
        }
        if(g.isValid(left)&&g.get(left)!=null){
            return true;
        }

        return false;
    }

    public void translateToCol(int col){
        int trans = col-blocks[0].getLocation().getCol();
        translate(0, trans);
    }

    public boolean getSpawned(){
        return spawned;
    }

    public Tetrad getEquivTetrad(){
        Tetrad t = new Tetrad(g);
        t.setShape(shape);
        return t;
    }
}