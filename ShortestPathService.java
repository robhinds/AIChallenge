import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author robert.hinds
 *
 * This class will use Dijkstras algorithm (A* with out additional
 * heuristics) to find the shortest path between two tiles
 */
public class ShortestPathService {
	
	private int distance=0;
	
	//map of the shortest path
	private Map<Tile, Aim> directions = new HashMap<Tile, Aim>();
	private Map<Tile, Tile> prevTile = new HashMap<Tile, Tile>();

	
	public void search(Tile currentTile, Tile endTile, int rows, int cols, Ants ants){

		Collection<Tile> graph = new ArrayList<Tile>();
		for (int r=0; r<rows; r++){
			for (int c=0; c<cols; c++){
				Tile t = new Tile(r,c);
				graph.add(t);
			}
		}
		
		//Map of all the Tiles visited and the shortest distance to them from the source node
		Map<Tile, Integer> tileDistance = new HashMap<Tile, Integer>();
		
		//track which nodes we have visited
		Set<Tile> visitedTiles = new HashSet<Tile>();
		Set<Tile> unvisitedTiles = new HashSet<Tile>();
		unvisitedTiles.addAll(graph);
		initTileDistance(currentTile, tileDistance, visitedTiles, unvisitedTiles);
		
		tileDistance.put(currentTile, 0);
		int currentDistance = 0;
		while (!unvisitedTiles.isEmpty()){
			//update tentative distance to all current neighbours
			currentDistance = tileDistance.get(currentTile);
			
			
			for (Aim dir : Aim.values()){
				Tile newLoc = ants.getTile(currentTile, dir);
				Tile neighbour=null;
				if (ants.getIlk(newLoc).isUnoccupied()) {
					neighbour = newLoc;
				}
				
				if (unvisitedTiles.contains(neighbour)){
					int nDist = tileDistance.get(neighbour);
					if (nDist > (currentDistance + 1)){
						tileDistance.put(neighbour, (currentDistance + 1));
						directions.put(neighbour, dir);
						prevTile.put(neighbour, currentTile);
					}
				}
			}
			
			//mark current Tile as visited (if not starting step - this is to allow cyclic routes)
			visitedTiles.add(currentTile);
			unvisitedTiles.remove(currentTile);
			
			//get closest onward Tile from current location
			currentTile = getNearestUnvisitedTile(unvisitedTiles, tileDistance);
		}
		
		//set distance to target
		distance=tileDistance.get(endTile);
	}
	
	

	/**
	 * Method that returns the closest unvisited Tile in the entire graph
	 * 
	 * @param unvisitedTiles - complete set of unvisited Tiles 
	 * @param tileDistance - Map containing distances to Tiles
	 * @return
	 */
	private Tile getNearestUnvisitedTile(Set<Tile> unvisitedTiles, Map<Tile, Integer> tileDistance) {
		Tile closest = null;
		for (Tile t : unvisitedTiles){
			if (closest==null || (tileDistance.get(t) < tileDistance.get(closest))){
				closest = t;
			}
		}
		return closest;
	}



	/**
	 * Method used to initialise the distances to each node for the shortest path algorithm. Sets all
	 * distances to the max int size, with the exception of the starting node, which is set to 0.
	 * 
	 * @param startTile - starting point
	 * @param tileDistance - map of distances to all nodes
	 * @param visitedNodes - set of all nodes visited
	 * @param unvisitedNodes - set of all nodes yet to be visited
	 */
	private void initTileDistance(Tile startTile, Map<Tile, Integer> tileDistance, Set<Tile> visitedNodes, Set<Tile> unvisitedNodes) {
		for (Tile t : unvisitedNodes){
			tileDistance.put(t, Integer.MAX_VALUE);
		}
	}



	public int getDistance() {
		return distance;
	}
	
	
	public List<Aim> getDirections(Tile target) {
		List<Aim> dir = new ArrayList<Aim>();
		Tile t1000 = target;
		while (t1000!=null){
			dir.add(0, directions.get(t1000));
			t1000 = prevTile.get(t1000);
		}
		return dir;
	}

}
