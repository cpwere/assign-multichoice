package net.multi.assignment.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import net.multi.assignment.tiles.Tile;
import net.multi.assignment.tiles.TileFactory;
import net.multi.assignment.tiles.TileType;
import net.multi.assignment.util.DistanceUtil;
import net.multi.resources.ResourceFileDefinitionUtil;
import static java.lang.System.out;
import static java.lang.System.err;

public class TerrainLayoutProcessor {

	private List<String> mapContentList = new ArrayList<String>();
	private Tile startTile = null;
	private Tile goalTile = null;
	private Tile[][] tileGrid = null;
//	private final String fileName = "./testinput_map.txt";
//	private final String fileName = "./verify_testinput_map.txt";

	private final String fileName = ResourceFileDefinitionUtil.inputFileName;
	private int rows = -1;

	private int columns = -1;
	
	private static Properties properties = null;
	private static final String propertiesfileName = "./terrain.properties";
	
	private static Map<String, Integer> cacheProps = new HashMap<String, Integer>();
	
	private static TerrainLayoutProcessor instance = null;
	
	private TerrainLayoutProcessor() {
		init();
	}
	
	public static TerrainLayoutProcessor getInstance() {
		if (instance==null) {
			instance = new TerrainLayoutProcessor();
		}
		return instance;
	}

	
	private void printTiles() {
		out.println("\n ---------------------------------------------------------");
		
		if (getTileGrid()!=null && getTileGrid().length>1) {
			
			for(int row=0; row<getRows(); row++) {
				out.println("\t");
				for(int col=0; col < getColumns(); col++) {
					
					if (col==0) {out.print("\t");}
					out.print(String.format("%s   ", getTileGrid()[row][col] ));
				}
			out.println("\t");	
			}
			
		}
		
		out.println("\n ---------------------------------------------------------");
	}

	private void init() {
		/**
		 * read file map content into List<String>
		 * 		
		 * declares a 2D array of Tile[][] objects
		 */
		boolean doneInput = false;
		int RETRY_COUNT=0, RETRY_LIMIT = 3;
		
		while((RETRY_COUNT <RETRY_LIMIT) && (!(doneInput=readInputSource()))) {
			RETRY_COUNT +=1;
			
			if (RETRY_COUNT < RETRY_LIMIT)
				err.println(String.format("%s >>>> [%s] RETRYING  to read the input file: %s", this.getClass().getSimpleName(), (RETRY_COUNT+1), fileName));
		};
		
		if (RETRY_COUNT == RETRY_LIMIT) {
			err.println(String.format("%s FAILED: Could not load the specified input file: %s", this.getClass().getSimpleName(), fileName));
			return;
		}
		
		
		if (doneInput) {
			initFromProperties(propertiesfileName);
			
			initTileGrid();
		}
	}


	private boolean readInputSource() {
		out.println(String.format("%s readInputSource(): START", this.getClass().getSimpleName()));
		boolean success = false;
		
		InputStream iStream = null;
		BufferedReader buffReader = null; 
		
		try {
			iStream    = ResourceFileDefinitionUtil.class.getResourceAsStream(fileName);
			if (iStream.available()>-1) {
				buffReader = new BufferedReader(new InputStreamReader(iStream));
				
				String line = buffReader.readLine();
				while(line!=null) {
					mapContentList.add(line);
					line = buffReader.readLine();
				}	
			}
			if (!mapContentList.isEmpty()) {
				success = true;
				out.println(String.format("%s rows read = [%s] ; columns read = [%s]", this.getClass().getSimpleName(), 
													mapContentList.size(), mapContentList.get(0).toCharArray().length));
			}
			
		} catch (Exception e) {
			err.println(String.format("%s ERROR while trying to read the Input map file: %s \t\t Check error details ...", this.getClass().getSimpleName(), fileName));
			e.printStackTrace();
		} finally {
			if (iStream!=null) {
				try {
					iStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (buffReader!=null) {
				try {
					buffReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		out.println(String.format("%s readInputSource(): END", this.getClass().getSimpleName()));
		return success;
	}


	private void initTileGrid() {
		out.println(String.format("\n%s initializing the 2D Array of Tile references: START", this.getClass().getSimpleName()));
		
		/**
		 *  - traverse the list
		 * 		- determine startTile '@' and get index
		 * 		- determine Goal Tile 'X' and get index
		 * 
		 */
		if (!mapContentList.isEmpty()) {

			setRows(mapContentList.size());
			setColumns(mapContentList.get(0).length());
			
			tileGrid = new Tile[getRows()][getColumns()];			// 2D array init
			
			TileFactory factory = TileFactory.getInstance();
			
			int x = 0;
			for(String str: mapContentList) {

				char[] rowContent = str.toCharArray();
				int y=0;
				for(char chrVal: rowContent) {
				
					Tile tile = factory.getTile(chrVal, x, y);
					tileGrid[x][y] = tile;
					
					// can the user start from anywhere else besides (0, 0)
					//if (tile.getxAxis()==0 && tile.getyAxis()==0) {
					//	tile.setCost(0);
					//}
					
					if (tile.isStartTile()) {
						setStartTile(tile);
					}
					
					if (tile.isGoalTile()) {
						setGoalTile(tile);
					}
					
					if (tile.isWalkable()) {
						tile.setCost(getTileValue(tile));
					}
					
					y++;
				}

				x++;	// increment x for rows
			}
			
			//initDistanceValues();
		}

		out.println(String.format("\n%s initializing the 2D Array of Tile references: END", this.getClass().getSimpleName()));
	}
	
	private void initDistanceValues() {
		
		for(int row=0; row<getRows(); row++) {
			for(int col=0; col<getColumns(); col++) {
				Tile tileItem = tileGrid[row][col];
				int distanceToGoal = DistanceUtil.getInstance().calculateDistance(tileItem.getxAxis(), tileItem.getyAxis(), 
						getGoalTile().getxAxis(), getGoalTile().getyAxis());
					tileItem.setDistanceToGoal(distanceToGoal);

			}
		}
	}

	
	private void initFromProperties(String propsfileName) {
		/**
		 * read .properties file for cost properties
		 */
		properties = new Properties();
		InputStream iStream = ResourceFileDefinitionUtil.class.getResourceAsStream(propsfileName);

		try {
			properties.load(iStream);
			
			out.println(String.format("%s initFromProperies() Successfully loaded the .properties file contents: %s ", this.getClass().getSimpleName(), propsfileName));
		} catch (IOException e) {
			out.println(String.format("%s initFromProperies() ERROR while trying to read the .properties file: %s", this.getClass().getSimpleName(), propsfileName));
			e.printStackTrace(err);
		}	finally {
			
			if (iStream!=null) {
				 try {
					 iStream.close();
				 } catch (Exception e) {
					 e.printStackTrace(err);
				 }
			}
		}
		
		if (properties!= null) {
			
			if (cacheProps.isEmpty()) {
				for(Object key: properties.keySet()) {
					
					if (!key.toString().equalsIgnoreCase(TileType.MOVEMENT_NON_WALK_WATER)) {
						cacheProps.put(key.toString(), Integer.valueOf(properties.get(key).toString()));
					}
					
					out.println(String.format("%s initFromProperies() \t key:%s ::: value[%s]", this.getClass().getSimpleName(), key, cacheProps.get(key.toString())));
				}
			}
			
		}
		
	}
	
	private int getTileValue(Tile tile) {
		int val = 0;
		
		if (tile.isWalkable()) {
			switch (tile.getImageIcon()) {
				case TileType.WALKABLE_FLATLAND_START: 
				case TileType.WALKABLE_FLATLAND_NORMAL:
				case TileType.WALKABLE_FLATLAND_GOAL:
					
					val = cacheProps.get(TileType.MOVEMENT_WALK_FLATLAND);
					break;
				case TileType.WALKABLE_FOREST:
				
					val = cacheProps.get(TileType.MOVEMENT_WALK_FOREST);
				break;
				case TileType.WALKABLE_MOUNTAIN:
			
					val = cacheProps.get(TileType.MOVEMENT_WALK_MOUNTAIN);
					break;
	
			default:
				err.println(String.format("%s getTileValue() Value not found for key: [%s]", this.getClass().getSimpleName(), tile.getImageIcon()));
				break;
			}
		}
		
		return val;
	}
	
	public int getRows() {
		return rows;
	}

	private void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	private void setColumns(int columns) {
		this.columns = columns;
	}
	
	public Tile[][] getTileGrid() {
		return tileGrid;
	}

	public void setTileGrid(Tile[][] tileGrid) {
		this.tileGrid = tileGrid;
	}
	
	public Tile getTile(int x, int y) {
		Tile tile = null;
		if (getTileGrid()!=null && getTileGrid().length>-1) {
			tile = getTileGrid()[x][y];
		}
		return tile;
	}

	public Tile getStartTile() {
		return startTile;
	}

	private void setStartTile(Tile startTile) {
		this.startTile = startTile;
	}

	public Tile getGoalTile() {
		return goalTile;
	}

	private void setGoalTile(Tile goalTile) {
		this.goalTile = goalTile;
	}

}
