package net.multi.assignment.tiles;

public class ForestTile extends Tile {

	public ForestTile() {
		name = "Forest tile";
		imageIcon = TileType.WALKABLE_FOREST;
	}
	
	public ForestTile(int x, int y) {
		this();
		xAxis = x;
		yAxis = y;
	}
}
