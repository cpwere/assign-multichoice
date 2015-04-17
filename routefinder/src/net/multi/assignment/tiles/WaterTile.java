package net.multi.assignment.tiles;

public class WaterTile extends Tile {

	public WaterTile() {
		name = "Water tile";
		isWalkable = false;			// Water Tile is not walkable
		imageIcon = '~';
	}
	
	public WaterTile(int x, int y) {
		this();
		xAxis = x;
		yAxis = y;
	}
}
