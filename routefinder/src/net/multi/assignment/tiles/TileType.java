package net.multi.assignment.tiles;

public class TileType {

	public static final char NON_WARLKABLE_WATER = '~';
	public static final char WALKABLE_FLATLAND_START = '@';
	public static final char WALKABLE_FLATLAND_GOAL = 'X';
	public static final char WALKABLE_FLATLAND_NORMAL = '.';
	public static final char WALKABLE_FOREST = '*';
	public static final char WALKABLE_MOUNTAIN = '^';
	public static final char WALKABLE_BEST_PATH = '#';
	
	
	// .properties keys
	public static final String MOVEMENT_WALK_FLATLAND = "movement.walkable.flatlands" ;
	public static final String MOVEMENT_WALK_FOREST = "movement.walkable.forest" ;
	public static final String MOVEMENT_WALK_MOUNTAIN = "movement.walkable.mountain" ;
	public static final String MOVEMENT_NON_WALK_WATER = "movement.nonwalkable.water" ;
}
