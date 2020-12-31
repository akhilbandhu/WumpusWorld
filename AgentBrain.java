import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class AgentBrain {

	//Don't delete this variable
	private AgentAction nextMove;

	private LinkedList<AgentAction> nextMoves;
	private boolean wumpusHunted = false;
	
	//We reload the brain each time, so this variable needs to be static
	private static int numGamesPlayed = 0;
	private static boolean keyboardPlayOnly = false;

	private int currentNumMoves;
	
	private int playerX=0;
	private int playerY=0;
	private static int wumpusX=0;
	private static int wumpusY=0;
	private boolean goldCollected = false;
	
	public AgentBrain() {
		nextMove = null;
		nextMoves = new LinkedList<AgentAction>();

		numGamesPlayed++;

		currentNumMoves = 0;
	}

	public void setNextMove(AgentAction m) {
//		if(nextMove != null) {
//			System.out.println("Trouble adding move, only allowed to add 1 at a time");
//		}
//		else {
			nextMove = m;
//		}
	}
	
	//probably won't have to use this function either
	public static void findWumpus(GameTile[][] visibleMap) {
		//method to find the position of the Wumpus
		//this will be used to determine where the beast is and to kill it
		for(int  i=0; i<visibleMap.length; i++) {
			for(int j=0; j<visibleMap[i].length; j++) {
				if(visibleMap[i][j].hasWumpus()) {
					wumpusX = i;
					wumpusY = j;
				}
			}
		}
	}
	
	//probably won't have to use this function. Need to change what arguments go into the wumpus state  class
	public void setStartingPosition(GameTile [][] visibleMap) {
		//method to set the starting position of our player.
		for(int i=0; i<visibleMap.length; i++) {
			for(int j=0; j<visibleMap[i].length; j++) {
				if(visibleMap[i][j] != null && visibleMap[i][j].hasPlayer()) {
					playerX = i;
					playerY = j;
				}
			}
		}
	}
	
	//new search function for its in here
	//our goal state would be getting the closest unknown state
	//before this, need to probably check if the the tile were standing on is safe.
	//If it is safe, then generate children.
	//Our first state tile will be known(starting position)
	//lets see what we can do.
	
	public WumpusState wellItsDarkNow(GameTile[][] visibleMap) {
		Queue<WumpusState> queue = new LinkedList<WumpusState>();
		HashMap<String, Boolean> visitedStates = new HashMap<String, Boolean>();	
		WumpusState state = new WumpusState(visibleMap, playerX, playerY);
		long nodesExpanded = 0;
		queue.add(state);
		if(state.getParent()!=null) {	
			queue.add(state.getParent());
		}
		while(!queue.isEmpty()) {
			WumpusState currentState = queue.remove();
			System.out.println(currentState.toString());
			
			if(currentState.getGoldCollected()) {
				ArrayList<AgentAction> actions = currentState.getAllActions();
				for(int i=1;i<actions.size(); i++) {
					System.out.println("gold");
					System.out.println(actions.get(i));
					nextMoves.add(actions.get(i));
				}
				goldCollected = true;
				return currentState;
			}
			
			if(currentState.spaceNotVisited()) {
				ArrayList<AgentAction> actions = currentState.getAllActions();
				for(int i=1;i<actions.size(); i++) {
					System.out.println("null");
					System.out.println(actions.get(i));
					nextMoves.add(actions.get(i));
				}
				return currentState;
			}
			
			if(visitedStates.containsKey(currentState.toString())) {
				continue;
			}
			else {
				visitedStates.put(currentState.toString(), true);
				WumpusState[] childrenOfCurrentChild = currentState.generateChildrenStates();
				for(int j=0; j<childrenOfCurrentChild.length; j++) {
					if(childrenOfCurrentChild[j]!=null) {
						queue.add(childrenOfCurrentChild[j]);
					}
				}
			}
		}
		goldCollected = true;
		return state;
		
	}
	
	//maybe a search method which adds everything to the next moves linked list.
	//And then use that in this method just to remove it one at a time.
	
	public void addToNextMoves(GameTile[][] visibleMap) {
		
		//Breadth First Search Code
//		WumpusState state = new WumpusState(visibleMap, playerX, playerY, wumpusX, wumpusY);

		Queue<WumpusState> queue = new LinkedList<WumpusState>();				//use the queue in the same way	
		HashMap<String, Boolean> visitedStates = new HashMap<String, Boolean>();	//add all children to the queue, 
																	//add all the children to the hash and set values to true.
																	//once all the children have been visited, generate children sequentially for the children that already been added  
		WumpusState state = new WumpusState(visibleMap, playerX, playerY);
		long nodesExpanded = 0;
		
		queue.add(state);
		while(!queue.isEmpty()) {
			WumpusState currentState = queue.remove();
			System.out.println(currentState.toString());
			if(currentState.getPlayerX() == 4 && currentState.getPlayerY() == 1) {
				nodesExpanded = visitedStates.size();
				System.out.println("Nodes Expanded: "+nodesExpanded);
				ArrayList<AgentAction> actions = currentState.getAllActions();
				for(int i=1;i<actions.size(); i++) {
					System.out.println(actions.get(i));
					nextMoves.add(actions.get(i));
				}
				nextMoves.add(AgentAction.declareVictory);
				return;
			}
			if(visitedStates.containsKey(currentState.toString())) {
				continue;
			}
			else {
				visitedStates.put(currentState.toString(), true);
				WumpusState[] childrenOfCurrentChild = currentState.generateChildrenStates();
				for(int j=0; j<childrenOfCurrentChild.length; j++) {
					if(childrenOfCurrentChild[j]!=null) {
						queue.add(childrenOfCurrentChild[j]);
					}
				}
			}
		}	
	}
	
	//breakthrough
	//maybe since its a doing a new search every single time it is
	//putting a new player every time 
	//take a look at that
	//try making the search return only one move at a time.
	
	//hunting the wumpus function
	//copy of the function above. Need to return only one function and then call the earlier search function
	//so it is shooting the wumpus but it isn't finding it's way back
	public WumpusState huntTheWumpus(GameTile[][] visibleMap) {
		WumpusState state = new WumpusState(visibleMap, playerX, playerY, wumpusX, wumpusY);

		Queue<WumpusState> queue = new LinkedList<WumpusState>();				//use the queue in the same way	
		HashMap<String, Boolean> visitedStates = new HashMap<String, Boolean>();	//add all children to the queue, 
		
		queue.add(state);
		
		while(!queue.isEmpty()) {
			WumpusState currentState = queue.remove();
			System.out.println(currentState.toString());
			//check whether the wumpus can be shot
			if(canShootWumpus(currentState)) {
				//need a function to check where the agent needs to shoot his arrow
				ArrayList<AgentAction> actions = currentState.getAllActions();
				//maybe just not run this for loop
				//we don't know
				for(int i=1;i<actions.size(); i++) {
					System.out.println(actions.get(i));
					nextMoves.add(actions.get(i));
				}
				nextMoves.add(shootTheWumpus(currentState));
				wumpusHunted = true;
				return currentState;
			}
			if(visitedStates.containsKey(currentState.toString())) {
				continue;
			}
			else {
				visitedStates.put(currentState.toString(), true);
				WumpusState[] childrenOfCurrentChild = currentState.generateChildrenStates();
				for(int j=0; j<childrenOfCurrentChild.length; j++) {
					if(childrenOfCurrentChild[j]!=null) {
						queue.add(childrenOfCurrentChild[j]);
					}
				}
			}
		}	
		return null;
	}
	
	public WumpusState findTheGold(WumpusState state) {

		Queue<WumpusState> queue = new LinkedList<WumpusState>();				//use the queue in the same way	
		HashMap<String, Boolean> visitedStates = new HashMap<String, Boolean>();	//add all children to the queue, 
		
		queue.add(state);
		
		while(!queue.isEmpty()) {
			WumpusState currentState = queue.remove();
			System.out.println(currentState.toString());
			//check whether the wumpus can be shot
			if(currentState.getGoldCollected()) {
				//need a function to check where the agent needs to shoot his arrow
				ArrayList<AgentAction> actions = currentState.getAllActions();
				//maybe just not run this for loop
				//we don't know
				for(int i=1;i<actions.size(); i++) {
					System.out.println(actions.get(i));
					nextMoves.add(actions.get(i));
				}
				return currentState;
			}
			if(visitedStates.containsKey(currentState.toString())) {
				continue;
			}
			else {
				visitedStates.put(currentState.toString(), true);
				WumpusState[] childrenOfCurrentChild = currentState.generateChildrenStates();
				for(int j=0; j<childrenOfCurrentChild.length; j++) {
					if(childrenOfCurrentChild[j]!=null) {
						queue.add(childrenOfCurrentChild[j]);
					}
				}
			}
		}	
		return null;
	}
	
	public boolean canShootWumpus(WumpusState currentState) {
		if(currentState.getPlayerX() == currentState.getWumpusX() || currentState.getPlayerY() == currentState.getWumpusY()) {
			return true;
		}
		return false;

	}
	public AgentAction shootTheWumpus(WumpusState currentState) {

		if(currentState.getPlayerX() == currentState.getWumpusX()) {
			if(currentState.getPlayerY() < currentState.getWumpusY()) {
				return AgentAction.shootArrowEast;
			}
			else {
				return AgentAction.shootArrowWest;
			}
		}
		
		if(currentState.getPlayerY() == currentState.getWumpusY()) {
			if(currentState.getPlayerX() < currentState.getPlayerX()) {
				return AgentAction.shootArrowSouth;
			}
			else {
				return AgentAction.shootArrowNorth;
			}
		}
		return null;
	}
	
	//For wumpus world, we do one move at a time
	public AgentAction getNextMove(GameTile [][] visibleMap) {
		//Possible things to add to your moves
//		nextMove = AgentAction.doNothing;
//		nextMove = AgentAction.moveDown;
//		nextMove = AgentAction.moveUp;
//		nextMove = AgentAction.moveUp;
//		nextMove = AgentAction.moveLeft;
//		nextMove = AgentAction.pickupSomething;
//		nextMove = AgentAction.declareVictory;
//
//		nextMove = AgentAction.shootArrowNorth;
//		nextMove = AgentAction.shootArrowSouth;
//		nextMove = AgentAction.shootArrowEast;
//		nextMove = AgentAction.shootArrowWest;
//		nextMove = AgentAction.quit
		
		

		//Ideally you would remove all this code, but I left it in so the keylistener would work
		if(keyboardPlayOnly) {
			if(nextMove == null) {
				return AgentAction.doNothing;
			}
			else {
				AgentAction tmp = nextMove;
				nextMove = null;
				return tmp;
			}

		}
		else {
			//This code plays 5 "games" and then quits
			//Just does random things
			if(numGamesPlayed > 19) {
				return AgentAction.quit;
			}
			else {
				if(nextMoves.isEmpty()) {
					setStartingPosition(visibleMap);
//					findWumpus(visibleMap);
//					findWumpus(visibleMap);
//					WumpusState currentState = huntTheWumpus(visibleMap);
//					System.out.println(wumpusHunted);
//					if(wumpusHunted) {
//						currentState.setParent(null);
//						addToNextMoves(currentState);
//					}
					
					//this is the code to collect the gold
//					currentState.setParent(null);
//					currentState = findTheGold(currentState);
//					if(currentState.getGoldCollected()) {
//						currentState.setParent(null);
//						addToNextMoves(currentState);
//					}
					if(!goldCollected) {
						wellItsDarkNow(visibleMap);
					}
					if(goldCollected) {
//						currentState.setParent(null);
						addToNextMoves(visibleMap);
//						goldCollected = false;
					}
					
					
				}
				if(!nextMoves.isEmpty()) {
					System.out.println(nextMoves.peek());
					setNextMove(nextMoves.remove());
//					System.out.println(nextMove);
//					return nextMove;
				}
				return nextMove;
//				currentNumMoves++;
//				if(currentNumMoves < 20) {
//					return AgentAction.randomAction();
//				}
//				else {
//					return AgentAction.declareVictory;
//				}
			}
		}

	}

}
