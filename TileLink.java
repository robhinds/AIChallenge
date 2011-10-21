import java.util.List;

/**
 * 
 */

/**
 * @author robert.hinds
 *
 * Object to store a link between two tiles including directions
 * to get from one tile to another.
 * 
 * This is used for tracking the distance between every ant and
 * every piece of food.  
 */
public class TileLink implements Comparable{
	
	public TileLink(Tile current, Tile target, List<Aim> dir, int dist){
		this.currentLoc=current;
		this.targetLoc=target;
		this.directions=dir;
		this.distance=dist;
	}

	private Tile currentLoc;
	
	private Tile targetLoc;
	
	private List<Aim> directions;
	
	private int distance;

	/**
	 * @return the currentLoc
	 */
	public Tile getCurrentLoc() {
		return currentLoc;
	}

	/**
	 * @param currentLoc the currentLoc to set
	 */
	public void setCurrentLoc(Tile currentLoc) {
		this.currentLoc = currentLoc;
	}

	/**
	 * @return the targetLog
	 */
	public Tile getTargetLog() {
		return targetLoc;
	}

	/**
	 * @param targetLog the targetLog to set
	 */
	public void setTargetLog(Tile targetLog) {
		this.targetLoc = targetLog;
	}

	/**
	 * @return the directions
	 */
	public List<Aim> getDirections() {
		return directions;
	}

	/**
	 * @param directions the directions to set
	 */
	public void setDirections(List<Aim> directions) {
		this.directions = directions;
	}
	
	/**
	 * @return distance between two locations
	 */
	public Integer getDistance(){
		return this.distance;
	}

	@Override
	public int compareTo(Object o) {
		TileLink other = (TileLink) o;
		return this.getDistance().compareTo(other.getDistance());
	}
}
