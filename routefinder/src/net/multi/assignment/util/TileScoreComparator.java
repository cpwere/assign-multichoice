package net.multi.assignment.util;

import java.util.Comparator;

import net.multi.assignment.tiles.Tile;

public class TileScoreComparator implements Comparator<Tile>{

	
	
	@Override
	public int compare(Tile tile1, Tile tile2) {
		
		int tileScore1 = tile1.getTileScore();
		int tileScore2 = tile2.getTileScore();
		
		
		return getAscending(tileScore1, tileScore2);
//		return getDescending(tileScore1, tileScore2);
	}
	
	private int getAscending(int score1, int score2) {
		return (score1 - score2);
	}
	
	private int getDescending(int score1, int score2) {
		return (score2 - score1);
	}
}
