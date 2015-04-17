package net.multi.assignment.tiles;

public class TileFactory {

	private static TileFactory instance = null;
	
	public static TileFactory getInstance() {
		
		if (instance == null) {
			instance = new TileFactory();
		}
		return instance;
	}
	
	public Tile getTile(char tileType, int x, int y) {
		
		Tile resTile = null;
		
		if (tileType==TileType.NON_WARLKABLE_WATER) {
			resTile = new WaterTile(x, y);
			resTile.setWalkable(false);
		} else if (tileType == TileType.WALKABLE_FLATLAND_START) {
			resTile = new FlatLandTile(x, y);
			resTile.setImageIcon(TileType.WALKABLE_FLATLAND_START);
			resTile.setStartTile(true);
		} else if (tileType == TileType.WALKABLE_FLATLAND_NORMAL) {
			resTile = new FlatLandTile(x, y);
			resTile.setImageIcon(TileType.WALKABLE_FLATLAND_NORMAL);
		} else if (tileType == TileType.WALKABLE_FLATLAND_GOAL) {
			resTile = new FlatLandTile(x, y);
			resTile.setGoalTile(true);
			resTile.setImageIcon(TileType.WALKABLE_FLATLAND_GOAL);
		} else if (tileType == TileType.WALKABLE_FOREST) {
			resTile = new ForestTile(x,  y);
		} else if (tileType == TileType.WALKABLE_MOUNTAIN) {
			resTile = new MountainTile(x, y);
		}
		
		return resTile;
	}
}
