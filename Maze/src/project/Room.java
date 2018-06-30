package project;

/**
 * A room is part of a maze structure which have at most 4 doors, each of which
 * can lead to the adjacent room to north, south, east, or west.
 * 
 * @author Vincent Stowbunenko
 *
 */
public class Room {

	private Room north;
	private Room south;
	private Room east;
	private Room west;
	private boolean isVisited;
	
	public Room getNorth() {
		return north;
	}

	public void setNorth(Room north) {
		this.north = north;
	}

	public Room getSouth() {
		return south;
	}

	public void setSouth(Room south) {
		this.south = south;
	}

	public Room getEast() {
		return east;
	}

	public void setEast(Room east) {
		this.east = east;
	}

	public Room getWest() {
		return west;
	}

	public void setWest(Room west) {
		this.west = west;
	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

}
