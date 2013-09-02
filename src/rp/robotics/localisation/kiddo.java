package rp.robotics.localisation;

import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
import lejos.util.Delay;
import movement.BreathS;
import movement.Node;
import movement.State;
import movement.newJunction;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.Heading;
import rp.robotics.mapping.LocalisationUtils;

/**
 * Renamed class that was originally MarkovLocalisationSkeleton
 * (which was a too long name)
 * This is the class that controls the main execution of the program.
 * @author Andreas
 *
 */
public class kiddo {
	//needs to be static because of it being used in main, would not be static
	//if an object of kiddo was created
	static newJunction junction = new newJunction();
	
	/**
	 * There is too much code in the main method. This could be improved by
	 * putting this code in a run method, and creating an object of the class
	 * and calling run.
	 * But we didn't have to time to do that without possibly breaking everything
	 * @param args command line arguments used
	 */
	public static void main(String[] args) {
		
		GridMap gridMap = LocalisationUtils.createTrainingMap();
		//color sensor unused
		ColorSensor color = new ColorSensor(SensorPort.S3);
		GridPoseDistribution distribution = new GridPoseDistribution(gridMap);

		// view the map with 2 pixels as 1 cm

		ActionModel actionModel = new PerfectActionModel();
		// ActionModel actionModel = new ActualPerfectActionModel();
		DummySensorModel sensorModel = new DummySensorModel();
		int count = 1;
		// Button.waitForAnyPress();
		Heading heading = Heading.PLUS_X;
		mainloop: while (true) {

			// Do some action
			// ...
			// I'm faking an action by waiting for some time
			Delay.msDelay(10);

			// Once completed, apply action model as appropriate
			//old code for updating action model on pc
			/*
			 * if (count % 4 == 0) distribution =
			 * actionModel.updateAfterMove(distribution, Heading.PLUS_X); else
			 * if (count % 4 == 1) distribution =
			 * actionModel.updateAfterMove(distribution, Heading.PLUS_Y); else
			 * if (count % 4 == 2) distribution =
			 * actionModel.updateAfterMove(distribution, Heading.MINUS_X); else
			 * if (count % 4 == 3) distribution =
			 * actionModel.updateAfterMove(distribution, Heading.MINUS_Y);
			 */int dist = junction.sonar.getDistance();
			RConsole.println("" + dist);
			if (count < 11) {
				heading = Heading.PLUS_X;

			} else if (count < 21) {
				heading = Heading.MINUS_X;
			} else if (count < 31) {
				heading = Heading.MINUS_Y;
			} else if (count < 41) {
				heading = Heading.PLUS_X;
			} else if (count < 51) {
				heading = Heading.PLUS_Y; // ?????????
			}
			if (dist > 370) {
				sensorModel.updateDistributionAfterSensing(distribution,
						heading, false);
				junction.run();
			} else {
				sensorModel.updateDistributionAfterSensing(distribution,
						heading, true);
			}

			if (count < 51) {
				distribution = actionModel.updateAfterMove(distribution,
						heading);
			}

			// Update visualisation. Only necessary if you want to visualise a
			// new object
			// mapVis.setDistribution(distribution);
			for (int y = 0; y < distribution.getGridHeight(); y++) {

				for (int x = 0; x < distribution.getGridWidth(); x++) {
					if (distribution.getProbability(x, y) > 0.01) {
						RConsole.println(x + "," + y + ": "
								+ distribution.getProbability(x, y));
						if (distribution.getProbability(x, y) == 1) {
							int dir = 0;
							if(Heading.toDegrees(heading) == 90){
								dir = 2;
							}
							if(Heading.toDegrees(heading) == 0){
								dir = 1;
							}
							if(Heading.toDegrees(heading) == -90){
								dir = 0;
							}
							if(Heading.toDegrees(heading) == 180){
								dir = 3;
							}
							searches(x, y, dir);
							break mainloop;
						}
					}
				}
			}
			RConsole.println("map sum: " + distribution.sumProbabilities());
			/*
			 * if (color.getColorID() == 1) { RConsole.print("le victory face");
			 * }
			 */
			// Do some sensing
			// ...
			// I'm faking sensing by waiting for some time
			// Delay.msDelay(1000);

			// Once completed apply sensor model as appropriate
			// .updateDistributionAfterSensing(distribution);

			// Note, as the sensor model changes the distribution directly, the
			// visualisation will update automatically
			if (count == 10) {
				junction.follow.chell.rotate(180);
			}
			if (count == 20) {
				junction.follow.chell.rotate(-90);
			}
			if (count == 30) {
				junction.follow.chell.rotate(-90);
			}
			if (count == 40) {
				junction.follow.chell.rotate(-90);
			}
			count++;
		}

	}

	/**
	 * Code to run the searches when we are localised
	 * @param x2 current x coord
	 * @param y2 current y coord
	 * @param direction the direction the robot was facing when we were localised
	 */
	private static void searches(int x2, int y2, int direction) {
		// TODO Auto-generated method stub
		//so that we know we were localised
		RConsole.println("OMOMGOMOGMOGMOGMG");
	
		Sound.playTone(2000, 3000);
		Sound.beepSequenceUp();
		Sound.beepSequence();
		Sound.beepSequenceUp();
		Sound.beepSequence();
		Sound.beepSequenceUp();
		Sound.beepSequence();

		// map

		boolean north;
		boolean east;
		boolean south;
		boolean west;
		System.out.println("building map");

		//
		//set up first search
		Node start = new Node(new State(x2, y2), null);
		start.setDirection(direction);
		GridMap gridMap = LocalisationUtils.createTrainingMap();
		BreathS thisSearch = new BreathS(11, 7,
				new Node(new State(0, 6), null), start, direction, junction);
	
		//set up blockages 
		for (int y = 0; y < gridMap.getGridHeight(); y++) {

			for (int x = 0; x < gridMap.getGridWidth(); x++) {
				north = true;
				east = true;
				south = true;
				west = true;
				if (gridMap.isValidTransition(x, y, x, y - 1)) {
					north = false;
				}
				if (gridMap.isValidTransition(x, y, x + 1, y)) {
					east = false;
				}
				if (gridMap.isValidTransition(x, y, x, y + 1)) {
					south = false;
				}
				if (gridMap.isValidTransition(x, y, x - 1, y)) {
					west = false;
				}
				if (x == 0) {
					if (y == 0) {
						System.out.println(north + " " + east + " " + south
								+ " " + west);
					}
				}
				thisSearch.blockages.add(new Node(new State(x, y, north, east,
						south, west), null));
			}
		}
		//run first search, this returns the final direction
		int dir2 = thisSearch.run();
		
		//set up the second search
		Node start2 = new Node(new State(0, 6), null);
		start2.setDirection(dir2);
		BreathS thisSearch2 = new BreathS(11, 7,
				new Node(new State(10, 6), null), start2, dir2, junction);
		
		//generate blockages
		for (int y = 0; y < gridMap.getGridHeight(); y++) {

			for (int x = 0; x < gridMap.getGridWidth(); x++) {
				north = true;
				east = true;
				south = true;
				west = true;
				if (gridMap.isValidTransition(x, y, x, y - 1)) {
					north = false;
				}
				if (gridMap.isValidTransition(x, y, x + 1, y)) {
					east = false;
				}
				if (gridMap.isValidTransition(x, y, x, y + 1)) {
					south = false;
				}
				if (gridMap.isValidTransition(x, y, x - 1, y)) {
					west = false;
				}
				if (x == 0) {
					if (y == 0) {
						System.out.println(north + " " + east + " " + south
								+ " " + west);
					}
				}
				thisSearch2.blockages.add(new Node(new State(x, y, north, east,
						south, west), null));
			}
		}
		
		//run second search
		thisSearch2.run();
		
		//finished!
		Sound.beepSequence();
		Sound.beepSequence();
	}
}
