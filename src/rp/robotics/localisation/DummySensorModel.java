package rp.robotics.localisation;

import java.util.Random;

import rp.robotics.mapping.Heading;

/**
 * An example of how you could start writing an action model given the available
 * classes.
 * 
 * @author nah
 * 
 */
/**
 * Still called the dummy sensor model, but we are using it as our main sensor model
 * @author Andreas
 *
 */
public class DummySensorModel {
	
	/**
	 * updating the distribution
	 * @param _dist the distribution we are updating
	 * @param heading the direction that the robot is heading
	 * @param wall 
	 */
	public void updateDistributionAfterSensing(GridPoseDistribution _dist,
			Heading heading, boolean wall) {

		// Commented out the random code to stop people using it without looking

		Random rand = new Random();

		// //float prob;
		// iterate through points updating as appropriate

		for (int x = 0; x < _dist.getGridWidth(); x++) {

			for (int y = 0; y < _dist.getGridHeight(); y++) {

				// make sure to respect obstructed grid points

				if (wall) {
					if (!_dist.isObstructed(x, y)) {

						// prob = _dist.getProbability(x, y);
						if (heading == Heading.PLUS_X) {
							if ((_dist.getGridMap().isValidTransition(x, y,
									x + 1, y))) {
								_dist.setProbability(x, y, 0);
							}
						}
						if (heading == Heading.MINUS_X) {
							if ((_dist.getGridMap().isValidTransition(x, y,
									x - 1, y))) {
								_dist.setProbability(x, y, 0);
							}
						}
						if (heading == Heading.PLUS_Y) {
							if ((_dist.getGridMap().isValidTransition(x, y, x,
									y + 1))) {
								_dist.setProbability(x, y, 0);
							}
						}
						if (heading == Heading.MINUS_Y) {
							if ((_dist.getGridMap().isValidTransition(x, y, x,
									y - 1))) {
								_dist.setProbability(x, y, 0);
							}
						}
					}
				} else {
					if (!_dist.isObstructed(x, y)) {

						// prob = _dist.getProbability(x, y);
						if (heading == Heading.PLUS_X) {
							if (!(_dist.getGridMap().isValidTransition(x, y,
									x + 1, y))) {
								_dist.setProbability(x, y, 0);
							}
						}
						if (heading == Heading.MINUS_X) {
							if (!(_dist.getGridMap().isValidTransition(x, y,
									x - 1, y))) {
								_dist.setProbability(x, y, 0);
							}
						}
						if (heading == Heading.PLUS_Y) {
							if (!(_dist.getGridMap().isValidTransition(x, y, x,
									y + 1))) {
								_dist.setProbability(x, y, 0);
							}
						}
						if (heading == Heading.MINUS_Y) {
							if (!(_dist.getGridMap().isValidTransition(x, y, x,
									y - 1))) {
								_dist.setProbability(x, y, 0);
							}
						}
					}
				}
			}
		}

		_dist.normalise();
	}
}
