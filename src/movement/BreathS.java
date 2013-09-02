package movement;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.LocalisationUtils;

//Class to do breadth first search, essentially the same as the 
//depth first one, but we didn't manage to combine them in time.
public class BreathS {
	public int gridWidth;
	public int gridHeight;
	public Node goalNode;
	public Node startNode;

	// constants to make writing checking code easier
	public static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

	// start direction, probably should just be set in main so it's all
	// together(since not used in the object)...
	public static int START_DIRECTION = NORTH;

	// store the blockages (added manually)
	public ArrayList<Node> blockages;
	private Queue<Node> finalFrontier;
	private ArrayList<Node> exploredSet;

	// stack used for getting the path when the search is generated
	private Stack<Node> path;

	// stores object of junction following code
	private newJunction j;

	// constructor for the search object
	public BreathS(int width, int height, Node goal, Node start,
			int START_DIRECTION) {
		startNode = start;
		goalNode = goal;
		gridHeight = height;
		gridWidth = width;
		this.START_DIRECTION = START_DIRECTION;
		// initialise collections
		exploredSet = new ArrayList<Node>();
		finalFrontier = new Queue<Node>();
		blockages = new ArrayList<Node>();
	}
	public BreathS(int width, int height, Node goal, Node start,
			int START_DIRECTION, newJunction junction) {
		startNode = start;
		goalNode = goal;
		gridHeight = height;
		gridWidth = width;
		this.START_DIRECTION = START_DIRECTION;
		// initialise collections
		exploredSet = new ArrayList<Node>();
		finalFrontier = new Queue<Node>();
		blockages = new ArrayList<Node>();
		this.j = junction;
	}

	// main search code
	public int run() {

		// only bother searching if we're not already at the start
		if (!startNode.isGoal(goalNode)) {
			System.out.println("did we gethere1");
			// add first node to the explored set and generate children
			exploredSet.add(startNode);
			generateNodes(startNode);
			System.out.println(finalFrontier.size());
			// main search loop
			while (!finalFrontier.isEmpty()) {
				System.out.println("didwegethere2");
				// get a node from the frontier
				Node currNode = (Node) finalFrontier.pop();
				// check if it's the goal
				// if it is...
				if (currNode.isGoal(goalNode)) {
					System.out.println("Goal found! lololololol");
					// generate the path to follow
					generatePath(currNode);
					// create new junction finding object (should be done in
					// travelpath maybe?)
					//j = new newJunction();
					// travel the path that we just generated
					travelPath();
					Delay.msDelay(5000);
					// exit the program
					//System.exit(0);
					return currNode.getDirection();
					// if it wasn't the goal, generate children from the current
					// node and go back to the start of the loop
				} else {
					generateNodes(currNode);
				}
				// mainly used for debugging, also lets you see that the search
				// completed properly
				System.out.println(currNode.getLoc());
				// Delay.msDelay(200);
			}
			// code never reached?
			// just a delay to let you read the screen before the program ends
			Delay.msDelay(5000);

			// first node was the goal, have a go at the user
		} else {
			System.out.println("Goal found! It was the start node you idiot!");
			Delay.msDelay(5000);
			return startNode.getDirection();
			//System.exit(0);
		}
		return 0;
	}

	// code to travel the path
	private void travelPath() {
		// get the first current and next objects by popping the stack twice
		Node current = path.pop();
		Node next = path.pop();

		// movement loop
		while (!path.isEmpty()) {
			// rotate the correct amount depending on the current and next nodes
			j.follow.chell.rotate(calculateTurn(current, next));
			// go to the next junction
			j.run();
			// update the current and next nodes
			current = next;
			next = path.pop();
		}
		// needed to travel to the final node as loop ends one node early
		j.follow.chell.rotate(calculateTurn(current, next));
		j.run();

		// play some sounds to verify the robot has stopped and is at the goal
		Sound.beepSequenceUp();
		Sound.beepSequence();
		Sound.beepSequenceUp();
		Sound.beepSequence();
	}

	// calculate the turn needed
	private int calculateTurn(Node current, Node next) {
		// calculate the difference in coords between the current and the next
		// nodes
		int xDiff = current.getCurrentState().getxCoord()
				- next.getCurrentState().getxCoord();
		int yDiff = current.getCurrentState().getyCoord()
				- next.getCurrentState().getyCoord();

		// get the directions
		int currDir = current.getDirection();
		int nextDir = currDir;

		// set the new direction
		if (xDiff == -1) {
			nextDir = EAST;
		}
		if (xDiff == 1) {
			nextDir = WEST;
		}
		if (yDiff == -1) {
			nextDir = SOUTH;
		}
		if (yDiff == 1) {
			nextDir = NORTH;
		}
		// printouts were for debugging
		System.out.println(currDir);
		System.out.println(nextDir);
		// assign the direction to the node
		next.setDirection(nextDir);
		// return the angle we need to turn
		return calculateAngle(currDir, nextDir);
	}

	// calculate the angle needed to turn based on the current direction and
	// the next direction
	private int calculateAngle(int currDir, int nextDir) {
		int dirDiff = currDir - nextDir;
		int angle = 0;

		// using the difference between the directions
		// they are stored as ints, 0,1,2,3 (see the fields)
		if (dirDiff == 1) {
			angle = 90;
		}
		if (dirDiff == -1) {
			angle = -90;
		}
		if ((dirDiff == 2) || (dirDiff == -2)) {
			angle = 180;
		}
		if (dirDiff == 3) {
			angle = -90;
		}
		if (dirDiff == -3) {
			angle = 90;
		}
		// once again for debugging
		System.out.println(angle);
		// return the angle
		return angle;

	}

	// this was used only for debugging so we knew that the robot
	// was finding the search correctly
	public void printPath() {
		while (!path.isEmpty()) {
			RConsole.println(path.pop().getLoc());
			Delay.msDelay(500);
		}
		Button.waitForAnyPress();
	}

	// generate the path starting from the final node
	private void generatePath(Node finalNode) {

		Node currNode = finalNode;
		// instantiate stack that we store the path in
		path = new Stack<Node>();
		// add final node as the (last) node of the stack
		path.push(finalNode);

		// main loop for making the path
		while (!(currNode.getParentNode() == null)) {
			// follow the pointers to the parent node in each node object
			path.push(currNode.getParentNode());
			RConsole.println(currNode.getParentNode().getCurrentState().getxCoord() + " "+ currNode.getParentNode().getCurrentState().getyCoord());
			// set current node to the parent and run again
			currNode = currNode.getParentNode();
		}
	}

	// main method, used to make the objects and set start, goal and blockages
	public static void main(String[] args) {
		GridMap gridMap = LocalisationUtils.createTrainingMap();
		// generate a "dummy" node that is the goal
		Node goal = new Node(new State(6, 6), null);
		// generate a "dummy" node that is the start
		// null is given to both of these because there is no parent
		Node start = new Node(new State(3, 2), null);
		// set the facing direction of the start node
		start.setDirection(0);
		// make new search object
		BreathS thisSearch = new BreathS(11, 7, goal, start, 0);

		// add the blockages, these are the blockages that were required for the
		// marking session
		// it's quite a verbose way of adding them because each blockage
		// requires up to two lines of code
		// as each blockage is between two nodes so the relevant direction for
		// each node must be set
		boolean north;
		boolean east;
		boolean south;
		boolean west;
		System.out.println("building map");
		for (int y = 0; y < gridMap.getGridHeight(); y++) {
			
			for (int x = 0; x < gridMap.getGridWidth(); x++) {
				north = true;
				east = true;
				south = true;
				west = true;
				if(gridMap.isValidTransition(x, y, x, y-1)){
					north = false;
				}
				if(gridMap.isValidTransition(x, y, x+1, y)){
					east = false;
				}
				if(gridMap.isValidTransition(x, y, x, y+1)){
					south = false;
				}
				if(gridMap.isValidTransition(x, y, x-1, y)){
					west = false;
				}
				if(x == 0){
					if (y == 0){
						System.out.println(north + " " + east + " " +south + " " + west);
					}
				}
				thisSearch.blockages.add(new Node(new State(x, y, north, east, south, west), null));
			}
		}
		// run the search
		System.out.println("running");
		thisSearch.run();
	}

	// generate the nodes from the current node
	public void generateNodes(Node datNode) {
		// list of the new nodes
		ArrayList<Node> newNodes = new ArrayList<Node>();

		// check the current node against the list of blockages
		// if the current node's position matches that of one of the blockages
		// we set up the blocked information required
		for (Node blockage : blockages) {
			if (datNode.nodeEquals(blockage)) {
				datNode.getCurrentState().setBlockedN(
						blockage.getCurrentState().isBlockedN());
				datNode.getCurrentState().setBlockedE(
						blockage.getCurrentState().isBlockedE());
				datNode.getCurrentState().setBlockedS(
						blockage.getCurrentState().isBlockedS());
				datNode.getCurrentState().setBlockedW(
						blockage.getCurrentState().isBlockedW());

			}
		}

		// for each direction, check if the node in that direction is valid, and
		// if so
		// add it.
		if (checkValid(datNode.getCurrentState(), NORTH)) {
			newNodes.add(new Node(new State(datNode.getCurrentState()
					.getxCoord(), datNode.getCurrentState().getyCoord() - 1),
					datNode));
		}

		if (checkValid(datNode.getCurrentState(), EAST)) {
			newNodes.add(new Node(new State(datNode.getCurrentState()
					.getxCoord() + 1, datNode.getCurrentState().getyCoord()),
					datNode));
		}

		if (checkValid(datNode.getCurrentState(), SOUTH)) {
			newNodes.add(new Node(new State(datNode.getCurrentState()
					.getxCoord(), datNode.getCurrentState().getyCoord() + 1),
					datNode));
		}

		if (checkValid(datNode.getCurrentState(), WEST)) {
			newNodes.add(new Node(new State(datNode.getCurrentState()
					.getxCoord() - 1, datNode.getCurrentState().getyCoord()),
					datNode));
		}
		System.out.println("inside generateNodes");
		// unused? not sure but don't want to delete
		ArrayList<Node> validNodes = new ArrayList<Node>();

		// if nodes aren't duplicates, then add them to the frontier and
		// explored set
		for (Node generated : newNodes) {
			if (!checkDupe(generated)) {
				finalFrontier.push(generated);
				exploredSet.add(generated);
			}
		}
	}

	// check if the move is valid
	public boolean checkValid(State currentState, int direction) {
		boolean valid = true;

		// for each direction, check if it's blocked or off the grid and
		// disallow the movement
		if (direction == NORTH) {

			if (currentState.isBlockedN()) {
				valid = false;
			}

			if (currentState.getyCoord() == 0) {
				valid = false;
			}
		}

		if (direction == EAST) {

			if (currentState.isBlockedE()) {
				valid = false;
			}

			if (currentState.getxCoord() == gridWidth - 1) {
				valid = false;
			}

		}

		if (direction == SOUTH) {

			if (currentState.isBlockedS()) {
				valid = false;
			}

			if (currentState.getyCoord() == gridHeight - 1) {
				valid = false;
			}
		}

		if (direction == WEST) {

			if (currentState.isBlockedW()) {
				valid = false;
			}

			if (currentState.getxCoord() == 0) {
				valid = false;
			}
		}
		// return true or false
		return valid;
	}

	// check for duplicates
	public boolean checkDupe(Node checkNode) {
		boolean dupe = false;
		for (Node exploredNode : exploredSet) {
			if (exploredNode.nodeEquals(checkNode)) {
				dupe = true;
			}
		}
		return dupe;
	}

}
