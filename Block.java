/**
 * Block class to be completed for Tetris project
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
import java.awt.Color;

public class Block
{
    private BoundedGrid<Block> grid;
    private Location location;
    private Color color;

    //constructs a blue block, because blue is the greatest color ever!
    public Block()
    {
        color = Color.BLUE;//color of the block
        grid = null;//makes the grid nonexistent, like the block
        location = null;//the block has no location yet
    }

    //gets the color of this block
    public Color getColor()
    {
        return color;    //returns the color
    }

    //sets the color of this block to newColor.
    public void setColor(Color newColor)
    {
        color = newColor;    //sets color to what is desired
    }

    //gets the grid of this block, or null if this block is not contained in a grid
    public BoundedGrid<Block> getGrid()
    {
        return grid;    //returns grid of what the block is
    }

    //gets the location of this block, or null if this block is not contained in a grid
    public Location getLocation()
    {
        return location;    //returns location of the block
    }

    //removes this block from its grid
    //precondition:  this block is contained in a grid
    public void removeSelfFromGrid()
    {
        //First, using the grid object, call the remove method (pass it this location)
        //Second, set this location to null
        //Third, set this grid to null
        grid.remove(location);    //removes the location from the grid
        location = null;            //location is gone now, so it's null
        grid = null;            //grid is gone now, so it's null
    }

    //puts this block into location loc of grid gr
    //if there is another block at loc, it is removed
    //precondition:  (1) this block is not contained in a grid
    //               (2) loc is valid in gr
    public void putSelfInGrid(BoundedGrid<Block> gr, Location loc)
    {
        Block block = gr.get(loc);
        if (block != null)
            block.removeSelfFromGrid();
        gr.put(loc, this);
        grid = gr;
        location = loc;
    }

    //moves this block to newLocation
    //if there is another block at newLocation, it is removed
    //precondition:  (1) this block is contained in a grid
    //               (2) newLocation is valid in the grid of this block
    public void moveTo(Location newLocation)
    {
        grid.remove(location);
        Block other = grid.get(newLocation);
        if (other != null)
            other.removeSelfFromGrid();
        location = newLocation;
        grid.put(location,this);
    }

        
    //returns a string with the location and color of this block
    public String toString()
    {
        return "Block[location=" + location + ",color=" + color + "]";
    }
}