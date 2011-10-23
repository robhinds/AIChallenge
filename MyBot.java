import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Starter bot implementation.
 */
public class MyBot extends Bot {
    /**
     * Main method executed by the game engine for starting the bot.
     * 
     * @param args command line arguments
     * 
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        new MyBot().readSystemInput();
    }
	
	
	/**
	 * Keep track of a ants and food
	 */
	private HashMap<Tile, Tile> targets;
	
	
	/**
	 * Attempts to move the current tile (Ant) towards a target destination
	 * @param orders 
	 */
	public boolean doMoveTowardsTarget(Tile currentLoc, Tile target, HashMap<Tile, Tile> orders){
		for (Aim dir : getAnts().getDirections(currentLoc, target)){
			if (doMoveDirection(getAnts(), orders, currentLoc, dir)){
				targets.put(target, currentLoc);
				return true;
			}
		}
		
		//if not successfully moved towards target return false
		return false;
	}
	


	
	public boolean doMoveDirection(Ants ants, HashMap<Tile, Tile> orders, Tile antLoc, Aim direction) {
		// Track all moves, prevent collisions
		Tile newLoc = ants.getTile(antLoc, direction);
		if (ants.getIlk(newLoc).isUnoccupied() && !orders.containsKey(newLoc)) {
			ants.issueOrder(antLoc, direction);
			orders.put(newLoc, antLoc);
			return true;
		} else {
			return false;
		}
	}
    
    /**
     * For every ant check every direction in fixed order (N, E, S, W) and move it if the tile is
     * passable.
     */
    @Override
    public void doTurn() {
		HashMap<Tile, Tile> orders = new HashMap<Tile, Tile>();
		targets = new HashMap<Tile, Tile>();
		Ants ants = getAnts();
		ArrayList<TileLink> antDistances = new ArrayList<TileLink>();
		// add dummy entries for each of the ant hills to the orders map
		for (Tile hill : ants.getMyHills()){
			orders.put(hill, null);
		}

		//Pass 1 - build a map of all our ants, all the food and the distance between each
		for (Tile food : ants.getFoodTiles()){
			for (Tile ant : ants.getMyAnts()){
				List<Aim> directions = ants.getDirections(ant, food);
				int dist = ants.getDistance(ant, food);
				antDistances.add(new TileLink(ant, food, directions, dist));
			}
		}

		//Sort the list based on distance
		Collections.sort(antDistances);
		
		// attempt to move ants towards foods
		for (TileLink t : antDistances){
			if (!targets.containsKey(t.getTargetLog()) && !targets.containsValue(t.getCurrentLoc())){
				doMoveTowardsTarget(t.getCurrentLoc(), t.getTargetLog(), orders);
			}
		}
		
		//explore unseen areas
		List<Tile> seen = new ArrayList<Tile>();
		for (Tile t : unseenLocations){
			if (ants.isVisible(t)){
				seen.add(t);
			}
		}
		unseenLocations.remove(seen);

		for (Tile ant : ants.getMyAnts()){
			if (!orders.containsValue(ant)){
				List<TileLink> unseenDistances = new ArrayList<TileLink>();
				for (Tile t : unseenLocations){
					List<Aim> directions = ants.getDirections(ant, t);
					int dist = ants.getDistance(ant, t);
					unseenDistances.add(new TileLink(ant, t, directions, dist));
				}

				//Sort the list based on distance
				Collections.sort(unseenDistances);

				// attempt to move ants towards unseen locations
				for (TileLink t : unseenDistances){
					if (doMoveTowardsTarget(ant, t.getTargetLog(), orders)){
						break;
					}
				}

			}
		}
		
		//move any remaining ants still on the hill
		for (Tile hill : ants.getMyHills()){
			if (ants.getMyAnts().contains(hill) && !orders.containsValue(hill)){
				for (Aim dir : Aim.values()){
					if (doMoveDirection(ants, orders, hill, dir)){
						break;
					}
				}
			}
		}
	}
}