package net.multi.assignment.tiles;

public class FlatLandTile extends Tile {

	public FlatLandTile() {
		name = "Flat-land tile";
	}
	
	public FlatLandTile(int x, int y) {
		this();
		xAxis = x;
		yAxis = y;
	}
	
	@Override
	public char getImageIcon() {
		
//		if (isStartTile()) {
//			imageIcon = TileType.WALKABLE_FLATLAND_START;
//			
//		} else if (isGoalTile()) {
//			imageIcon = TileType.WALKABLE_FLATLAND_GOAL;
//		} else {
//			imageIcon = TileType.WALKABLE_FLATLAND_NORMAL;
//		}
		return imageIcon;
	}
	
//	public 
}
