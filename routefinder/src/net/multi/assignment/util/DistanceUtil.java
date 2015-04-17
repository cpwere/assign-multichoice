package net.multi.assignment.util;

public class DistanceUtil {
	
	public static final int ERROR_INVALID_CODE = -999999999; 

	private static DistanceUtil instance = null;
	
	private DistanceUtil() {
	}
	
	public static DistanceUtil getInstance() {
		if (instance == null) {
			instance = new DistanceUtil();
		}
		
		return instance;
	}
	
	/**
	 * calculates the L1 distance between 2 points using Manhattan distance 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return 
	 */
	public int calculateDistance(int x1, int y1, int x2, int y2) {
		int result = ERROR_INVALID_CODE;
		
		result = (Math.abs(x1 - x2) + Math.abs(y1 - y2));
		
		return result;
	}
	

}
