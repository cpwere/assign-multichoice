package net.multi.assignment.tiles;

public abstract class Tile {

	protected int xAxis = 0;
	protected int yAxis = 0;
	
	protected String name = "";
	protected int cost = 0;
	protected int distanceToGoal = -1;
	protected int tileScore = -1;
	protected boolean isWalkable = true;
	protected boolean isGoalTile = false;			// set after Terrain layout on init
	protected boolean isStartTile = false;			// set after Terrain layout on init 
	protected char imageIcon;
	
	protected boolean checked = false; 
	
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public int getxAxis() {
		return xAxis;
	}
	public void setxAxis(int xAxis) {
		this.xAxis = xAxis;
	}
	public int getyAxis() {
		return yAxis;
	}
	public void setyAxis(int yAxis) {
		this.yAxis = yAxis;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getDistanceToGoal() {
		return distanceToGoal;
	}
	public void setDistanceToGoal(int distanceToGoal) {
		this.distanceToGoal = distanceToGoal;
	}
	public int getTileScore() {
		return tileScore;
	}
	public void setTileScore(int tileScore) {
		this.tileScore = tileScore;
	}
	public boolean isWalkable() {
		return isWalkable;
	}
	public void setWalkable(boolean isWalkable) {
		this.isWalkable = isWalkable;
	}
	public char getImageIcon() {
		return imageIcon;
	}
	public void setImageIcon(char img) {
		this.imageIcon = img;
	}
	public boolean isGoalTile() {
		return isGoalTile;
	}
	public void setGoalTile(boolean isGoalTile) {
		this.isGoalTile = isGoalTile;
	}
	public boolean isStartTile() {
		return isStartTile;
	}
	public void setStartTile(boolean isStartTile) {
		this.isStartTile = isStartTile;
	}
	
	@Override
	public String toString() {
//		return String.valueOf(getImageIcon()) + String.format("(%s,%s): [%s ::: %s / %s] ", getxAxis(), getyAxis(), getCost(), getDistanceToGoal(), getTileScore()); 
		return String.valueOf(getImageIcon());
			// + String.format("[%s]", isWalkable);
	}
}
