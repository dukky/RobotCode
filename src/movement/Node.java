package movement;
//class to represent a node of search
public class Node {
	
	//the current state, the parent node (used for path generation) and the direction facing
	private State currentState;
	private Node parentNode;
	private int Direction;

	//constructor, node can just be set to null if you are making a "dummy node"
	public Node(State currentState, Node parentNode) {

		this.currentState = currentState;
		this.parentNode = parentNode;

	}

	//getters and setters
	public State getCurrentState() {
		return currentState;
	}

	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}

	public Node getParentNode() {
		return parentNode;
	}

	public void setParentNode(Node parentNode) {
		this.parentNode = parentNode;
	}

	//check if the current node is the goal (given the goal node_
	public boolean isGoal(Node goalNode) {
		boolean goal = false;

		if (nodeEquals(goalNode)) {
			goal = true;
		}

		return goal;
	}

	//check if the current node's position equals the given node
	public boolean nodeEquals(Node compNode) {
		boolean equal = false;
		
		if ((this.getCurrentState().getxCoord() == compNode.getCurrentState()
				.getxCoord())
				&& (this.getCurrentState().getyCoord() == compNode
						.getCurrentState().getyCoord())) {
			equal = true;
		}

		return equal;
	}
	
	//print the location of the node
	public String getLoc() {
		return "x:" + currentState.getxCoord() + " y:" + currentState.getyCoord();
	}

	//getters and setters again
	public int getDirection() {
		return Direction;
	}

	public void setDirection(int direction) {
		Direction = direction;
	}
	

	// gotta go fast
}
