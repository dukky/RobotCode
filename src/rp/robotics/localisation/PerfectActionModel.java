package rp.robotics.localisation;

import rp.robotics.mapping.Heading;

/**
 * Example structure for an action model that should move the probabilities 1
 * cell in the requested direction. In the case where the move would take the
 * robot into an obstacle or off the map, this model assumes the robot stayed in
 * one place. This is the same as the model presented in Robot Programming
 * Lecture 14.
 * 
 * Note that this class doesn't actually do this, instead it shows you a
 * <b>possible</b> structure for your action model.
 * 
 * @author nah
 * 
 */
public class PerfectActionModel implements ActionModel {

	@Override
	public GridPoseDistribution updateAfterMove(GridPoseDistribution _from,
			Heading _heading) {

		// Create the new distribution that will result from applying the action
		// model
		GridPoseDistribution to = new GridPoseDistribution(_from);

		// Move the probability in the correct direction for the action
		if (_heading == Heading.PLUS_X) {
			movePlusX(_from, to);
		} else if (_heading == Heading.PLUS_Y) {
			movePlusY(_from, to);
		} else if (_heading == Heading.MINUS_X) {
			moveMinusX(_from, to);
		} else if (_heading == Heading.MINUS_Y) {
			moveMinusY(_from, to);
		}

		return to;
	}


	/**
	 * Move probabilities from _from one cell in the plus x direction into _to
	 * 
	 * @param _from
	 * @param _to
	 */
	private void movePlusX(GridPoseDistribution _from, GridPoseDistribution _to) {

		// iterate through points updating as appropriate
		for (int y = 0; y < _to.getGridHeight(); y++) {

			for (int x = 0; x < _to.getGridWidth(); x++) {

				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y)) {

					if (!_to.getGridMap().isValidTransition(x, y, x + 1, y)) {

						if (_to.getGridMap().isValidTransition(x, y, x - 1, y)) {

							_to.setProbability(x, y, _from.getProbability(x, y)
									+ _from.getProbability(x - 1, y));

						}
					}

					else if (_to.getGridMap().isValidTransition(x, y, x - 1, y)) {

						_to.setProbability(x, y, _from.getProbability(x - 1, y));

					} else {

						_to.setProbability(x, y, 0);
					}
				}

			}

		}
	}

	private void moveMinusX(GridPoseDistribution _from, GridPoseDistribution _to) {
		for (int y = 0; y < _to.getGridHeight(); y++) {

			for (int x = 0; x < _to.getGridWidth(); x++) {

				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y)) {

					if (!_to.getGridMap().isValidTransition(x, y, x - 1, y)) {

						if (_to.getGridMap().isValidTransition(x, y, x + 1, y)) {

							_to.setProbability(x, y, _from.getProbability(x, y)
									+ _from.getProbability(x + 1, y));

						}
					}

					else if (_to.getGridMap().isValidTransition(x, y, x + 1, y)) {

						_to.setProbability(x, y, _from.getProbability(x + 1, y));

					} else {

						_to.setProbability(x, y, 0);

					}
				}

			}

		}
	}

	private void movePlusY(GridPoseDistribution _from, GridPoseDistribution _to) {

		// iterate through points updating as appropriate
		for (int y = 0; y < _to.getGridHeight(); y++) {

			for (int x = 0; x < _to.getGridWidth(); x++) {

				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y)) {

					if (!_to.getGridMap().isValidTransition(x, y, x, y + 1)) {

						if (_to.getGridMap().isValidTransition(x, y, x, y - 1)) {

							_to.setProbability(x, y, _from.getProbability(x, y)
									+ _from.getProbability(x, y - 1));

						}
					}

					else if (_to.getGridMap().isValidTransition(x, y, x, y - 1)) {

						_to.setProbability(x, y, _from.getProbability(x, y - 1));

					} else {

						_to.setProbability(x, y, 0);
					}
				}

			}

		}
	}
	
	private void moveMinusY(GridPoseDistribution _from, GridPoseDistribution _to) {

		// iterate through points updating as appropriate
		for (int y = 0; y < _to.getGridHeight(); y++) {

			for (int x = 0; x < _to.getGridWidth(); x++) {

				// make sure to respect obstructed grid points
				if (!_to.isObstructed(x, y)) {

					if (!_to.getGridMap().isValidTransition(x, y, x, y - 1)) {

						if (_to.getGridMap().isValidTransition(x, y, x, y + 1)) {

							_to.setProbability(x, y, _from.getProbability(x, y)
									+ _from.getProbability(x, y + 1));

						}
					}

					else if (_to.getGridMap().isValidTransition(x, y, x, y + 1)) {

						_to.setProbability(x, y, _from.getProbability(x, y + 1));

					} else {

						_to.setProbability(x, y, 0);
					}
				}

			}

		}
	}

}
