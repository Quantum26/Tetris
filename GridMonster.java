/**
 * GridMonster tester class provided for Tetris project
 */

import java.awt.Color;
import java.util.ArrayList;

public class GridMonster
{
	public static void main(String[] args)
	{
		System.out.println("Level 0:  getting number of rows and columns");

		BoundedGrid<String> grid = new BoundedGrid<String>(2, 1);

		if (grid.getNumRows() != 2)
			throw new RuntimeException("getNumRows is dumb");
		if (grid.getNumCols() != 1)
			throw new RuntimeException("getNumCols is dumb");

		System.out.println("Level 1:  testing if valid");

		if (!grid.isValid(new Location(0, 0)))
			throw new RuntimeException("isValid is dumb");
		if (!grid.isValid(new Location(1, 0)))
			throw new RuntimeException("isValid is dumb");
		if (grid.isValid(new Location(2, 0)))
			throw new RuntimeException("isValid is dumb");
		if (grid.isValid(new Location(0, 1)))
			throw new RuntimeException("isValid is dumb");
		if (grid.isValid(new Location(0, -1)))
			throw new RuntimeException("isValid is dumb");
		if (grid.isValid(new Location(-1, 0)))
			throw new RuntimeException("isValid is dumb");

		System.out.println("Level 2:  getting, putting, and removing");

		Location loc = new Location(1, 0);
		String firstValue = "first";
		String secondValue = "second";

		String value = grid.get(loc);
		if (value != null)
			throw new RuntimeException("get is dumb");
		value = grid.put(loc, firstValue);
		if (value != null)
			throw new RuntimeException("put is dumb");
		value = grid.get(loc);
		if (value != firstValue)
			throw new RuntimeException("put/get is dumb");
		value = grid.put(loc, secondValue);
		if (value != firstValue)
			throw new RuntimeException("put is dumb");
		value = grid.get(loc);
		if (value != secondValue)
			throw new RuntimeException("put is dumb");
		value = grid.remove(loc);
		if (value != secondValue)
			throw new RuntimeException("remove is dumb");
		value = grid.get(loc);
		if (value != null)
			throw new RuntimeException("remove is dumb");
		value = grid.remove(loc);
		if (value != null)
			throw new RuntimeException("remove is dumb");

		System.out.println("Level 3:  getting occupied locations");

		ArrayList<Location> locs = grid.getOccupiedLocations();
		if (locs.size() != 0)
			throw new RuntimeException("getOccupiedLocations is dumb");
		grid.put(new Location(1, 0), firstValue);
		locs = grid.getOccupiedLocations();
		if (locs.size() != 1)
			throw new RuntimeException("getOccupiedLocations is dumb");
		if (!locs.get(0).equals(new Location(1, 0)))
			throw new RuntimeException("getOccupiedLocations is dumb");
		grid.put(new Location(0, 0), secondValue);
		locs = grid.getOccupiedLocations();
		if (locs.size() != 2)
			throw new RuntimeException("getOccupiedLocations is dumb");
		if (!(
			(locs.get(0).equals(new Location(0, 0)) && locs.get(1).equals(new Location(1, 0)))
			||
			(locs.get(0).equals(new Location(1, 0)) && locs.get(1).equals(new Location(0, 0)))))
			throw new RuntimeException("getOccupiedLocations is dumb");

		System.out.println("Level 4:  putting into an empty location");

		Block h = new Block();

		Color red = Color.RED;
		h.setColor(red);
		if (h.getColor() != red)
			throw new RuntimeException("setColor/getColor is dumb");

		BoundedGrid<Block> grid2 = new BoundedGrid<Block>(4, 5);
		BlockDisplay display = new BlockDisplay(grid2);

		h.putSelfInGrid(grid2, new Location(0, 2));
		display.showBlocks();
		if (grid2.get(new Location(0, 2)) != h)
			throw new RuntimeException("putSelfInGrid is dumb");
		if (h.getGrid() != grid2)
			throw new RuntimeException("putSelfInGrid is dumb");
		if (!h.getLocation().equals(new Location(0, 2)))
			throw new RuntimeException("putSelfInGrid is dumb");

		System.out.println("Level 5:  removing from grid");

		h.removeSelfFromGrid();
		display.showBlocks();
		if (grid2.get(new Location(0, 2)) != null)
			throw new RuntimeException("removeSelfFromGrid is dumb");
		if (h.getGrid() != null)
			throw new RuntimeException("removeSelfFromGrid is dumb");
		if (h.getLocation() != null)
			throw new RuntimeException("removeSelfFromGrid is dumb");

		System.out.println("Level 6:  putting into an occupied location");

		h.putSelfInGrid(grid2, new Location(0, 2));
		display.showBlocks();
		Block r = new Block();
		r.putSelfInGrid(grid2, new Location(0, 2));
		display.showBlocks();
		if (h.getGrid() != null)
			throw new RuntimeException("putSelfInGrid is dumb");
		if (h.getLocation() != null)
			throw new RuntimeException("putSelfInGrid is dumb");
		if (r.getGrid() != grid2)
			throw new RuntimeException("putSelfInGrid is dumb");
		if (!r.getLocation().equals(new Location(0, 2)))
			throw new RuntimeException("putSelfInGrid is dumb");
		if (grid2.get(new Location(0, 2)) != r)
			throw new RuntimeException("putSelfInGrid is dumb");

		System.out.println("Level 7:  moving to an empty location");

		r.moveTo(new Location(0, 4));
		display.showBlocks();
		if (grid2.get(new Location(0, 2)) != null)
			throw new RuntimeException("moveTo is dumb");
		if (!r.getLocation().equals(new Location(0, 4)))
			throw new RuntimeException("moveTo is dumb");
		if (grid2.get(new Location(0, 4)) != r)
			throw new RuntimeException("moveTo is dumb");

		System.out.println("Level 8:  moving to an occupied location");
		h.putSelfInGrid(grid2, new Location(0, 1));
		display.showBlocks();
		r.moveTo(new Location(0, 1));
		display.showBlocks();
		if (grid2.get(new Location(0, 4)) != null)
			throw new RuntimeException("moveTo is dumb");
		if (h.getGrid() != null)
			throw new RuntimeException("moveTo is dumb");
		if (h.getLocation() != null)
			throw new RuntimeException("moveTo is dumb");
		if (r.getGrid() != grid2)
			throw new RuntimeException("moveTo is dumb");
		if (!r.getLocation().equals(new Location(0, 1)))
			throw new RuntimeException("moveTo is dumb");
		if (grid2.get(new Location(0, 1)) != r)
			throw new RuntimeException("moveTo is dumb");

		System.out.println("Level 9:  moving to its own location");
		r.moveTo(new Location(0, 1));
		display.showBlocks();
		if (r.getGrid() != grid2)
			throw new RuntimeException("moveTo is dumb");
		if (!r.getLocation().equals(new Location(0, 1)))
			throw new RuntimeException("moveTo is dumb");
		if (grid2.get(new Location(0, 1)) != r)
			throw new RuntimeException("moveTo is dumb");

		Block block = new Block();
		block.putSelfInGrid(grid2, new Location(0, 3));
		block = new Block();
		block.setColor(Color.RED);
		block.putSelfInGrid(grid2, new Location(2, 0));
		block = new Block();
		block.setColor(Color.RED);
		block.putSelfInGrid(grid2, new Location(2, 4));
		block = new Block();
		block.setColor(Color.RED);
		block.putSelfInGrid(grid2, new Location(3, 1));
		block = new Block();
		block.setColor(Color.RED);
		block.putSelfInGrid(grid2, new Location(3, 2));
		block = new Block();
		block.setColor(Color.RED);
		block.putSelfInGrid(grid2, new Location(3, 3));
		display.showBlocks();

            System.out.println("Congratulations!  You have satisfied the GridMonster.");
	}
}