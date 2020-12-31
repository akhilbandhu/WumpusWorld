import java.util.ArrayList;

public class WumpusState implements Comparable<WumpusState>{

	//TODO: 
	//Game tile 2d array 
	//Coin tracker 
	//question asker
	//for glitter and breeze 

	private GameTile [][] world;
	private int playerX;
	private int playerY;
	private AgentAction action;
	private WumpusState parent;
	private int coinCount=0;
	private int wumpusX;
	private int wumpusY;
	private boolean heardScream;
	private int goldX;
	private int goldY;
	private boolean goldCollected;
	private boolean isSafe;

	public WumpusState(GameTile [][] world, int playerX, int playerY, int wumpusX, int wumpusY) {
		this.world = new GameTile[world.length][world[0].length];
		for(int i=0; i<world.length; i++) {
			for(int j=0; j<world[i].length; j++) {
				this.world[i][j] = new GameTile(world[i][j]);
			}
		}
		this.playerX = playerX;
		this.playerY = playerY;

		//getting the position of the wumpus from the main AgentBrain class and nothing else.
		this.wumpusX = wumpusX;
		this.wumpusY = wumpusY;
		for(int k=0; k<world.length; k++) {
			for(int l=0; l<world[0].length; l++) {
				if(world[k][l].hasGlitter()) {
					this.goldX = k;
					this.goldY = l;
				}
			}
		}
		this.goldCollected = false;
		//		this.outOfCave = false;

		//finds the wumpus in the map and sets the wumpus x coordinate and wumpus y coordinate
		//		for(int k=0; k<world.length; k++) {
		//			for(int l=0; l<world[k].length; l++) {
		//				if(this.world[k][l].hasWumpus()) {
		//					this.wumpusX = k;
		//					this.wumpusY = l;
		//				}
		//			}
		//		}
	}

	public WumpusState(GameTile[][] world, int playerX, int playerY) {
		this.world = new GameTile[world.length][world[0].length];
		for(int i=0; i<world.length; i++) {
			for(int j=0; j<world[i].length; j++) {
				if(world[i][j] == null) {
					this.world[i][j] = null;
				}
				else {
					this.world[i][j] = new GameTile(world[i][j]);
				}
			}
		}
		this.playerX = playerX;
		this.playerY = playerY;
		this.goldCollected = false;
		//lets try this
		//if the tile the player is standing on is safe we set up a boolean to true
		updateSafety();
		

	}


	//i don't think we need this right now.
	//	public void findTheWumpus() {
	//		for(int i=0; i<world.length; i++) {
	//			for(int j=0; j<world[i].length; j++) {
	//				if(world[i][j].hasWumpus()) {
	//					this.setWumpusX(i);
	//					this.setWumpusY(j);
	//				}
	//			}
	//		}
	//	}

	public void setSafety(boolean isSafe) {
		this.isSafe = isSafe;
	}

	public boolean getSafety() {
		return this.isSafe;
	}

	public void setGoldCollected(boolean goldCollected) {
		this.goldCollected = goldCollected;
	}

	public boolean getGoldCollected() {
		return this.goldCollected;
	}

	public void setGoldX(int goldX) {
		this.goldX = goldX;
	}

	public int getGoldX() {
		return this.goldX;
	}

	public void setGoldY(int goldY) {
		this.goldY = goldY;
	}

	public int getGoldY() {
		return this.goldY;
	}

	public boolean getScream() {
		return this.heardScream;
	}

	public void setScream(boolean heardScream) {
		this.heardScream = heardScream;
	}
	//wom't ever need to set the position of the wumpus
	public void setWumpusX(int wumpusX) {
		this.wumpusX = wumpusX;
	}

	public int getWumpusX() {
		return this.wumpusX;
	}

	//won't need this either
	public void setWumpusY(int wumpusY) {
		this.wumpusY = wumpusY;
	}

	public int getWumpusY() {
		return this.wumpusY;
	}

	public GameTile getGameTile() {
		return world[playerX][playerY];
	}

	public GameTile[][] getWorld() {
		return world;
	}


	public void setWorld(GameTile[][] world, int playerX, int playerY) {
		this.world = new GameTile[world.length][world[0].length];
		for(int i=0; i<world.length; i++) {
			for(int j=0; j<world[i].length; j++) {
				this.world[i][j] = new GameTile(world[i][j]);
			}
		}		
		this.world[playerX][playerY].setPlayer(true);
	}


	public int getPlayerX() {
		return playerX;
	}


	public void setPlayerX(int playerX) {
		this.playerX = playerX;
		updateSafety();
	}


	public int getPlayerY() {
		return playerY;
	}


	public void setPlayerY(int playerY) {
		this.playerY = playerY;
		updateSafety();
	}


	public AgentAction getAction() {
		return action;
	}


	public void setAction(AgentAction action) {
		this.action = action;
	}


	public WumpusState getParent() {
		return parent;
	}


	public void setParent(WumpusState parent) {
		this.parent = parent;
	}


	public int getCoinCount() {
		return coinCount;
	}


	public void setCoinCount(int coinCount) {
		this.coinCount = coinCount;
	}

	//	public boolean hasStateHeardScream() {
	//		return world[playerX][playerY].heardScream();
	//	}

	public String toString() {
		String worldString = playerX +" "+playerY + " ";
		for(int i=0; i<world.length; i++) {
			for(int j=0; j<world[i].length; j++) {
				//add an if statement to protect against empty tiles
				if(world[i][j] == null) {
					worldString+="?";
				}
				else {
					worldString += world[i][j].toString();
				}

			}
		}
		return worldString;
	}

	public boolean spaceNotVisited() {
		if(world[playerX][playerY] == null) {
			return true;
		}
		return false;
	}

	public boolean spaceVisited() {
		if(world[playerX][playerY]!=null) {
			return true;
		}
		return false;
	}

	public ArrayList<AgentAction> getAllActions(){
		ArrayList<AgentAction> allActions = new ArrayList<AgentAction>();
		if(parent==null) {
			allActions.add(action);
			return allActions;
		}
		else {
			allActions = parent.getAllActions();
			allActions.add(action);
			return allActions;
		}
	}

	public void updateSafety() {
		if(this.world[playerX][playerY] == null) {
			this.isSafe = true;
		}
		else if(this.world[playerX][playerY].hasBreeze() || this.world[playerX][playerY].hasStench()) {
			this.isSafe = false;
		}
		else {
			this.isSafe = true;
		}
	}

	//maybe create 4 new states if the current tile has a stench
	//that will help in the future.
	//do not necessarily need to manually find the position of the wumpus
	//maybe have a boolean which just toggles when the wumpus has been killed
	public WumpusState[] generateChildrenStates() {
		WumpusState[] childrenStates = new WumpusState[5];
		if(spaceNotVisited()) {
			return childrenStates;
		}
		for(int i=0; i<5; i++) {
			//			childrenStates[i] = new WumpusState(world, playerX, playerY, wumpusX, wumpusY);
			childrenStates[i] = new WumpusState(world, playerX, playerY);
			childrenStates[i].setParent(this);
			System.out.println(childrenStates[i].toString());
		}

		if(world[playerX][playerY] != null && world[playerX][playerY].hasGlitter()) {
			childrenStates[0].coinCount++;
			childrenStates[0].setAction(AgentAction.pickupSomething); 
			childrenStates[0].world[playerX][playerY].setGlitter(false);
			childrenStates[0].setGoldCollected(true);
			for(int i=1; i<childrenStates.length; i++) {
				childrenStates[i] = null;
			}

		}

		else {
			//set the first child to 0, don't need to use that anymore.
			//this might cause problems later
			//don't need any of this sadly
			childrenStates[0] = null;
			if(!this.getSafety()) {
				if(childrenStates[1].world[playerX-1][playerY] == null || childrenStates[1].world[playerX-1][playerY].isWall()) {
					childrenStates[1] = null;
				}
				else {
					childrenStates[1].setAction(AgentAction.moveUp); 
					childrenStates[1].setPlayerX(playerX-1);	
				}
				if(childrenStates[2].world[playerX][playerY+1] == null || childrenStates[2].world[playerX][playerY+1].isWall()) {
					childrenStates[2] = null;
				}
				else {
					childrenStates[2].setAction(AgentAction.moveRight); 
					childrenStates[2].setPlayerY(playerY+1);	
				}
				if(childrenStates[3].world[playerX+1][playerY] == null || childrenStates[3].world[playerX+1][playerY].isWall()) {
					childrenStates[3] = null;
				}
				else {
					childrenStates[3].setAction(AgentAction.moveDown); 
					childrenStates[3].setPlayerX(playerX+1);	
				}
				if(childrenStates[4].world[playerX][playerY-1] == null || childrenStates[4].world[playerX][playerY-1].isWall()) {
					childrenStates[4] = null;
				}
				else {
					childrenStates[4].setAction(AgentAction.moveLeft); 
					childrenStates[4].setPlayerY(playerY-1);	
				}
				return childrenStates;
			}

			//create the states if not on gold
			//move up 1
			//checking if the states after going up is going to hit a wall
			//if it does then don't create this state 
			if(childrenStates[1].world[playerX-1][playerY]!=null && childrenStates[1].world[playerX-1][playerY].isWall()) {
				childrenStates[1] = null;
			}
			//lets get this shit done
			//something needs to be added here just to check if you have visited this spot or not
			if(childrenStates[1] != null && childrenStates[1].getPlayerX()-1 > 0) {
				childrenStates[1].setAction(AgentAction.moveUp); 
				childrenStates[1].setPlayerX(playerX-1);
			}

			//move right 1
			//same with this one, check to see if you agent is about to hit a wall
			if(childrenStates[2].world[playerX][playerY+1]!= null && childrenStates[2].world[playerX][playerY+1].isWall()) {
				childrenStates[2] = null;
			}
			if(childrenStates[2] != null && childrenStates[2].getPlayerY()+1 < 5) {
				childrenStates[2].setPlayerY(playerY+1);
				childrenStates[2].setAction(AgentAction.moveRight); 
			}

			//move down 1
			if(childrenStates[3].world[playerX+1][playerY]!= null && childrenStates[3].world[playerX+1][playerY].isWall()) {
				childrenStates[3] = null;
			}
			if(childrenStates[3] != null && childrenStates[3].getPlayerX()+1 < 5) {
				childrenStates[3].setPlayerX(playerX+1);
				childrenStates[3].setAction(AgentAction.moveDown);
			}

			//move left 1
			//set the child to null if state cannot be made
			//change to what you did for first child
			if(childrenStates[4].world[playerX][playerY-1]!=null && childrenStates[4].world[playerX][playerY-1].isWall()) {
				childrenStates[4] = null;
			}
			if(childrenStates[4] != null && childrenStates[4].getPlayerY()-1 > 0) {
				childrenStates[4].setPlayerY(playerY-1);
				childrenStates[4].setAction(AgentAction.moveLeft);
			}
		}
		return childrenStates;
	}

	@Override
	public int compareTo(WumpusState o) {
		// TODO Auto-generated method stub
		return 0;
	}

}
