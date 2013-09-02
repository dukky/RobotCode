package movement;
//represents a point on the grid
public class State {
	
	//information that needs to be stored about the state
	private int xCoord;
	private int yCoord;
	private boolean blockedN = false;
	private boolean blockedE = false;
	private boolean blockedS = false;
	private boolean blockedW = false;
	
	//main constructor to create a state and not set blockages
	public State(int x, int y) {

		this.xCoord = x;
		this.yCoord = y;
		
	}
	
	//automatically generated getters and setters for all of the fields
	
	public int getxCoord() {
		return xCoord;
	}



	public void setxCoord(int xCoord) {
		this.xCoord = xCoord;
	}



	public int getyCoord() {
		return yCoord;
	}



	public void setyCoord(int yCoord) {
		this.yCoord = yCoord;
	}



	public boolean isBlockedN() {
		return blockedN;
	}



	public void setBlockedN(boolean blockedN) {
		this.blockedN = blockedN;
	}



	public boolean isBlockedE() {
		return blockedE;
	}



	public void setBlockedE(boolean blockedE) {
		this.blockedE = blockedE;
	}



	public boolean isBlockedS() {
		return blockedS;
	}



	public void setBlockedS(boolean blockedS) {
		this.blockedS = blockedS;
	}



	public boolean isBlockedW() {
		return blockedW;
	}



	public void setBlockedW(boolean blockedW) {
		this.blockedW = blockedW;
	}


	//second constructor for the class, allows you to set the blockages when you make the object
	public State(int x, int y, boolean blockedN, boolean blockedE, boolean blockedS, boolean blockedW) {
		
		this.xCoord = x;
		this.yCoord = y;
		
		this.blockedN = blockedN;
		this.blockedE = blockedE;
		this.blockedS = blockedS;
		this.blockedW = blockedW;
		
	}

}
