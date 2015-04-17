package net.multi.assignment.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.multi.assignment.tiles.Tile;
import net.multi.assignment.tiles.TileType;
import net.multi.assignment.util.DistanceUtil;
import net.multi.assignment.util.TileScoreComparator;
import net.multi.resources.ResourceFileDefinitionUtil;
import static java.lang.System.out;
import static java.lang.System.err;

/**
 * RouteFinder allows to find/set the current Tile and set the neighbour tiles
 * or choice tiles. It uses calculation to find the best possible choice tile
 * to move to next and ultimately map path to Goal tile
 * @author Siphiwe.Madi
 */
public class RouteFinderApp {

	private Tile bestTileFound = null;
	private static TerrainLayoutProcessor terrainLayout = null;
	private Tile[][] tileGrid = null;
	
	public RouteFinderApp() {
		terrainLayout = TerrainLayoutProcessor.getInstance();
		if (terrainLayout!=null && terrainLayout.getTileGrid()!=null) {
			tileGrid = new Tile[terrainLayout.getRows()][terrainLayout.getColumns()];
			tileGrid = terrainLayout.getTileGrid();
		}
	}

	public static void main(String[] args) {
		out.println(String.format("%s main() : START", RouteFinderApp.class.getSimpleName()));
		RouteFinderApp finder = new RouteFinderApp();
	
		if (finder.processTerrainLayout()) {
			
			//TODO: to printout to System.out un-comment line below
			//finder.printTerrainLayout();
			
			finder.createOuputFile();
			
		}
	
		out.println(String.format("%s main() : END", RouteFinderApp.class.getSimpleName()));
	}
	
	private void createOuputFile() {
	
		String outputFileName = ResourceFileDefinitionUtil.outputfile;
		
		FileOutputStream fileOutStream = null;
		OutputStreamWriter osWriter = null;
		
		try {
		
			boolean done = false;
			File file = new File(outputFileName);
			
			fileOutStream = new FileOutputStream(file);
			osWriter = new OutputStreamWriter(fileOutStream);
			
			if (getTerrainLayout()!=null && getTerrainLayout().length>0) {
				
				for(int row=0; row<terrainLayout.getRows(); row++) {
					for(int col=0; col < terrainLayout.getColumns(); col++) {
						
						Tile printTile = getTerrainLayout()[row][col];
						osWriter.write(String.format("%s  ", printTile));
						done=true;
					}
					osWriter.write("\n");
				}	
			}
			
			if (done) {
				out.println(String.format("\n %s createOutputFile() FILE output created in %s , %s", this.getClass().getSimpleName(), 
										file.getAbsoluteFile().getName(), file.getAbsolutePath()));
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
		    err.format("\n Error while writing to file: %s \t error: %s", outputFileName, e.getMessage());
		    e.printStackTrace();
		    
		} finally {
			if (osWriter!=null) {
				try {
					osWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			if (fileOutStream!=null) {
				try {
					fileOutStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void printTerrainLayout() {
		out.println("\n ---------------------------------------------------------");
		
		if (getTerrainLayout()!=null && getTerrainLayout().length>0) {
			
			for(int row=0; row<terrainLayout.getRows(); row++) {
				out.println("\t");
				for(int col=0; col < terrainLayout.getColumns(); col++) {
					
					Tile printTile = getTerrainLayout()[row][col];
							
					if (col==0) {out.print("\t");}
					out.print(String.format("%s   ",  printTile));
				}
			out.println("\t");	
			}
			
		}
		
		out.println("\n ---------------------------------------------------------");
	}
	
	/**
	 * does the actual processing of the layout to locate the best path to the goal tile X
	 * @return
	 */
	public boolean processTerrainLayout() {
		out.format("\n%s processTerrainLayout() START", this.getClass().getSimpleName());
		
		if (getTerrainLayout()!=null) {

			bestTileFound = terrainLayout.getStartTile();
			//bestTileFound.setImageIcon(TileType.WALKABLE_BEST_PATH);

			for(int row=terrainLayout.getStartTile().getxAxis(); 
					row<terrainLayout.getRows() ; row++) {
				
				for(int col=terrainLayout.getStartTile().getyAxis(); 
								col<terrainLayout.getColumns(); col++) {

					List<Tile> choiceTiles = findWalkableChoices(bestTileFound);
					
					// find walkable choices for current Best path Tile
					if (choiceTiles!=null && !choiceTiles.isEmpty()) {
						bestTileFound = findBestTile(bestTileFound, choiceTiles);
						if (bestTileFound!=null) {
							bestTileFound.setImageIcon(TileType.WALKABLE_BEST_PATH);
							row = bestTileFound.getxAxis();
							col = bestTileFound.getyAxis();
							continue;
	
						}
					}
					
				}	// end for
			}	// end for
		}

		out.format("\n%s processTerrainLayout() START", this.getClass().getSimpleName());
		return true;
	}
	
	private Tile findBestTile(Tile bestTile, List<Tile> list) {

		int totalCost = 0;
		totalCost+=bestTile.getCost();
		
		if (bestTile.isStartTile()) {
			bestTile.setCost(0);
		}
		
		final Tile goalTile = terrainLayout.getGoalTile();
		
		for(Tile tileItem: list) {
			int distanceToGoal = DistanceUtil.getInstance().calculateDistance(tileItem.getxAxis(), tileItem.getyAxis(), 
					goalTile.getxAxis(), goalTile.getyAxis());
				tileItem.setDistanceToGoal(distanceToGoal);

				// set choice tile score
			tileItem.setTileScore(Math.abs(totalCost + tileItem.getCost()) + Math.abs(tileItem.getDistanceToGoal()));
		}
		
		if (!list.isEmpty()) {
			Collections.sort(list, new TileScoreComparator());
			return list.get(0);
		}
		
		return null;
	}

	private List<Tile> findWalkableChoices(Tile bestTile) {

		List<Tile> choiceTiles = new ArrayList<Tile>();
		/**
		 * workout the choice tiles i.e. surrounding the current tile
		 * 1st they must be walkable
		 *  - current row neigbhour
		 *  - next row neighboughs 
		 */
		// cannot be checking the neighboughs of non-walkable tile
		
		if (!bestTile.isWalkable()) {
			return choiceTiles;
		}

		
		bestTile.setImageIcon(TileType.WALKABLE_BEST_PATH);
		// current Tile indexes
		int neighX = bestTile.getxAxis(), neighY = bestTile.getyAxis();
		
		// not circling back
		//addChoiceTile(choiceTiles, getNeighbourTile((neighX-1), neighY));
		//addChoiceTile(choiceTiles, getNeighbourTile((neighX-1), (neighY-1)));
		//addChoiceTile(choiceTiles, getNeighbourTile((neighX-1), (neighY+1)));
		
		
		addChoiceTile(choiceTiles, getNeighbourTile(neighX, (neighY-1)));
		addChoiceTile(choiceTiles, getNeighbourTile(neighX, (neighY+1)));
		
		addChoiceTile(choiceTiles, getNeighbourTile((neighX+1), neighY));
		addChoiceTile(choiceTiles, getNeighbourTile((neighX+1), (neighY-1)));
		addChoiceTile(choiceTiles, getNeighbourTile((neighX+1), (neighY+1)));
		
		return choiceTiles;
	}
	
	private void addChoiceTile(List<Tile> choiceTiles, Tile neighbourTile) {
		
		if (neighbourTile!=null && neighbourTile.isWalkable()) { 
					// && (!neighbourTile.isChecked())) {
			
			if (neighbourTile.getImageIcon()!=TileType.WALKABLE_BEST_PATH) {
				choiceTiles.add(neighbourTile);
				neighbourTile.setChecked(true);
			}
		}
	}

	private Tile getNeighbourTile(int row, int col) {

		// check the boundries of the row & column
		if ((row>-1) && row<terrainLayout.getRows() && (col>-1 && col<terrainLayout.getColumns())) {
			
			Tile checkTile = getTerrainLayout()[row][col]; 
				return checkTile;
		}
		
		return null;
	}

	public Tile[][] getTerrainLayout() {
		return tileGrid;
	}
}
