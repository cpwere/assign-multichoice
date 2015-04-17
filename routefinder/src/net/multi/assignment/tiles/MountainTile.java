package net.multi.assignment.tiles;

public class MountainTile extends Tile {

	public MountainTile() {
		name = "Mountain tile";
		imageIcon = TileType.WALKABLE_MOUNTAIN;
	}
	
	public MountainTile(int x, int y) {
		this();
		xAxis = x;
		yAxis = y;
	}
}
