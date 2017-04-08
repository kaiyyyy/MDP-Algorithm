import java.util.Stack;

/*
 * Action selector super class
 * Action selector and its child classes implements the logic used in difference phase of the movement
 */
public abstract class ActionSelector {
	protected Arena arena;
	protected Navigator navigator;
	private SensorReading readings;
	/*
	 * Stack of way points that must be cleared
	 */

	private Stack<WayPoint> targets = new Stack<WayPoint>();

	/*
	 * Abstract method to be implemented by each subclass This method will
	 * return the string sent to other components
	 */
	public abstract String selectActions();

	// cost of actions, to be modified based on real time taken to perform
	// actions
	protected double move_cost = 1;
	protected double turn_left_cost = 50;
	protected double turn_right_cost = 50;

	public ActionSelector(Arena arena, Navigator navigator) {
		this.arena = arena;
		this.navigator = navigator;
		this.readings = new SensorReading(navigator, arena);
	}

	/*
	 * Update the stack of way points
	 */
	public boolean isWayPointsEmpty() {
		return (this.targets.size() == 0);
	}

	public void addWayPoint(int height, int width) {
		this.targets.push(new WayPoint(height, width));
	}

	public boolean compareWayPoints(int height, int width) {
		WayPoint target = this.targets.lastElement();
		if (target.getHeight() == height && target.getWidth() == width) {
			targets.pop();
			return true;
		} else
			return false;
	}

	// organize and pass sensor readings in terms of number of blocks between
	// the navigator and nearest
	// obstacle in three directions
	public int[] getSensorReading() {
		this.readings.getSensorReadings();
		return this.readings.getFrontReadings();
	}

	/*
	 * Function that check whether the navigator can move in each direction
	 * based on current direction. Directions include: index 0: front index 1:
	 * left index 2: right
	 */
	// distance is greater than zero does not mean the position is movable
	public boolean[] isMovable() {
		boolean[] result = new boolean[3];
		int cur_height = navigator.getHeight();
		int cur_width = navigator.getWidth();
		// result[0] - front
		switch (navigator.getCurDirection()) {
		case Direction.NORTH:
			if (this.arena.isMovable(cur_height + 1, cur_width)) {
				result[0] = true;
			} else {
				result[0] = false;
			}
			break;
		case Direction.SOUTH:
			if (this.arena.isMovable(cur_height - 1, cur_width)) {
				result[0] = true;
			} else {
				result[0] = false;
			}
			break;
		case Direction.EAST:
			if (this.arena.isMovable(cur_height, cur_width + 1)) {
				result[0] = true;
			} else {
				result[0] = false;
			}
			break;
		case Direction.WEST:
			if (this.arena.isMovable(cur_height, cur_width - 1)) {
				result[0] = true;
			} else {
				result[0] = false;
			}
			break;
		}
		// result[1] - left
		switch (navigator.getCurDirection()) {
		case Direction.NORTH:
			if (this.arena.isMovable(cur_height, cur_width - 1)) {
				result[1] = true;
			} else {
				result[1] = false;
			}
			break;
		case Direction.SOUTH:
			if (this.arena.isMovable(cur_height, cur_width + 1)) {
				result[1] = true;
			} else {
				result[1] = false;
			}
			break;
		case Direction.EAST:
			if (this.arena.isMovable(cur_height + 1, cur_width)) {
				result[1] = true;
			} else {
				result[1] = false;
			}
			break;
		case Direction.WEST:
			if (this.arena.isMovable(cur_height - 1, cur_width)) {
				result[1] = true;
			} else {
				result[1] = false;
			}
			break;
		}
		// result[2] - right
		switch (navigator.getCurDirection()) {
		case Direction.NORTH:
			if (this.arena.isMovable(cur_height, cur_width + 1)) {
				result[2] = true;
			} else {
				result[2] = false;
			}
			break;
		case Direction.SOUTH:
			if (this.arena.isMovable(cur_height, cur_width - 1)) {
				result[2] = true;
			} else {
				result[2] = false;
			}
			break;
		case Direction.EAST:
			if (this.arena.isMovable(cur_height - 1, cur_width)) {
				result[2] = true;
			} else {
				result[2] = false;
			}
			break;
		case Direction.WEST:
			if (this.arena.isMovable(cur_height + 1, cur_width)) {
				result[2] = true;
			} else {
				result[2] = false;
			}
			break;
		}
		return result;
	}

	/*
	 * Get sensor readings and update the map accordingly
	 */

	public void readAndUpdate() {
		this.readings.getSensorReadings();
		this.updateFront();
		this.updateLeft();
		this.updateRight();
	}

	public void updateFront() {
		// update the information about blocks in front
		int[] distances = readings.getFrontReadings();

		int height = navigator.getHeight();
		int width = navigator.getWidth();
		int direction = navigator.getCurDirection();

		switch (direction) {
		case Direction.NORTH:
			for (int index = 0; index < 3; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance < 4; distance++) {
						// update unblocked
						arena.markState(height + 1 + distance, width - 1 + index, BlockState.REACHABLE);
					}
				} else {
					int distance = 1;
					for (distance = 1; distance < distances[index]; distance++) {
						// update unblocked
						arena.markState(height + 1 + distance, width - 1 + index, BlockState.REACHABLE);
					}
					// update blocked
					if (arena.withinRange(height + 1 + distance, width - 1 + index))
						arena.markState(height + 1 + distance, width - 1 + index, BlockState.BLOCKED);
				}
			}
			break;
		case Direction.SOUTH:
			for (int index = 0; index < 3; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance < 4; distance++) {
						// update unblocked
						arena.markState(height - 1 - distance, width + 1 - index, BlockState.REACHABLE);
					}
				} else {
					int distance = 1;
					for (distance = 1; distance < distances[index]; distance++) {
						// update unblocked
						arena.markState(height - 1 - distance, width + 1 - index, BlockState.REACHABLE);
					}
					// update blocked
					if (arena.withinRange(height - 1 - distance, width + 1 - index))
						arena.markState(height - 1 - distance, width + 1 - index, BlockState.BLOCKED);
				}
			}
			break;
		case Direction.WEST:
			for (int index = 0; index < 3; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance < 4; distance++) {
						// update unblocked
						arena.markState(height - 1 + index, width - 1 - distance, BlockState.REACHABLE);
					}
				} else {
					int distance = 1;
					for (distance = 1; distance < distances[index]; distance++) {
						// update unblocked
						arena.markState(height - 1 + index, width - 1 - distance, BlockState.REACHABLE);
					}
					// update blocked
					if (arena.withinRange(height - 1 + index, width - 1 - distance))
						arena.markState(height - 1 + index, width - 1 - distance, BlockState.BLOCKED);
				}
			}
			break;
		case Direction.EAST:
			for (int index = 0; index < 3; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance < 4; distance++) {
						// update unblocked
						arena.markState(height + 1 - index, width + 1 + distance, BlockState.REACHABLE);
					}
				} else {
					int distance = 1;
					for (distance = 1; distance < distances[index]; distance++) {
						// update unblocked
						arena.markState(height + 1 - index, width + 1 + distance, BlockState.REACHABLE);
					}
					// update blocked
					if (arena.withinRange(height + 1 - index, width + 1 + distance))
						arena.markState(height + 1 - index, width + 1 + distance, BlockState.BLOCKED);
				}
			}
			break;
		}
	}

	public void updateLeft() {
		// update the information about blocks in front
		int[] distances = readings.getLeftReadings();

		int height = navigator.getHeight();
		int width = navigator.getWidth();
		int direction = navigator.getCurDirection();

		switch (direction) {
		case Direction.NORTH:
			for (int index = 0; index < 2; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance < 3; distance++) {
						// update unblocked
						arena.markState(height + 1 - index, width - 1 - distance, BlockState.REACHABLE);
					}
				} else {
					int distance = 1;
					for (distance = 1; distance < distances[index]; distance++) {
						// update unblocked
						arena.markState(height + 1 - index, width - 1 - distance, BlockState.REACHABLE);
					}
					// update blocked
					if (arena.withinRange(height + 1 - index, width - 1 - distance))
						arena.markState(height + 1 - index, width - 1 - distance, BlockState.BLOCKED);
				}
			}
			break;
		case Direction.SOUTH:
			for (int index = 0; index < 2; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance < 3; distance++) {
						// update unblocked
						arena.markState(height - 1 + index, width + 1 + distance, BlockState.REACHABLE);
					}
				} else {
					int distance = 1;
					for (distance = 1; distance < distances[index]; distance++) {
						// update unblocked
						arena.markState(height - 1 + index, width + 1 + distance, BlockState.REACHABLE);
					}
					// update blocked
					if (arena.withinRange(height - 1 + index, width + 1 + distance))
						arena.markState(height - 1 + index, width + 1 + distance, BlockState.BLOCKED);
				}
			}
			break;
		case Direction.WEST:
			for (int index = 0; index < 2; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance < 3; distance++) {
						// update unblocked
						arena.markState(height - 1 - distance, width - 1 + index, BlockState.REACHABLE);
					}
				} else {
					int distance = 1;
					for (distance = 1; distance < distances[index]; distance++) {
						// update unblocked
						arena.markState(height - 1 - distance, width - 1 + index, BlockState.REACHABLE);
					}
					// update blocked
					if (arena.withinRange(height - 1 - distance, width - 1 + index))
						arena.markState(height - 1 - distance, width - 1 + index, BlockState.BLOCKED);
				}
			}
			break;
		case Direction.EAST:
			for (int index = 0; index < 2; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance < 3; distance++) {
						// update unblocked
						arena.markState(height + 1 + distance, width + 1 - index, BlockState.REACHABLE);
					}
				} else {
					int distance = 1;
					for (distance = 1; distance < distances[index]; distance++) {
						// update unblocked
						arena.markState(height + 1 + distance, width + 1 - index, BlockState.REACHABLE);
					}
					// update blocked
					if (arena.withinRange(height + 1 + distance, width + 1 - index))
						arena.markState(height + 1 + distance, width + 1 - index, BlockState.BLOCKED);
				}
			}
			break;
		}
	}

	public void updateRight() {
		// update the information about blocks in front
		int distance = readings.getRightReadings();

		int height = navigator.getHeight();
		int width = navigator.getWidth();
		int direction = navigator.getCurDirection();

		switch (direction) {
		case Direction.NORTH:
			if (distance == -1) {
				for (int inc = 1; inc < 6; inc++) {
					arena.markState(height, width + 1 + inc, BlockState.REACHABLE);
				}
			} else {
				int inc = 1;
				for (inc = 1; inc < distance; inc++) {
					arena.markState(height, width + 1 + inc, BlockState.REACHABLE);
				}
				if (arena.withinRange(height, width + 1 + inc))
					arena.markState(height, width + 1 + inc, BlockState.BLOCKED);
			}
			break;
		case Direction.SOUTH:
			if (distance == -1) {
				for (int inc = 1; inc < 6; inc++) {
					arena.markState(height, width - 1 - inc, BlockState.REACHABLE);
				}
			} else {
				int inc = 1;
				for (inc = 1; inc < distance; inc++) {
					arena.markState(height, width - 1 - inc, BlockState.REACHABLE);
				}
				if (arena.withinRange(height, width - 1 - inc))
					arena.markState(height, width - 1 - inc, BlockState.BLOCKED);
			}
			break;
		case Direction.WEST:
			if (distance == -1) {
				for (int inc = 1; inc < 6; inc++) {
					arena.markState(height + 1 + inc, width, BlockState.REACHABLE);
				}
			} else {
				int inc = 1;
				for (inc = 1; inc < distance; inc++) {
					arena.markState(height + 1 + inc, width, BlockState.REACHABLE);
				}
				if (arena.withinRange(height + 1 + inc, width))
					arena.markState(height + 1 + inc, width, BlockState.BLOCKED);
			}
			break;
		case Direction.EAST:
			if (distance == -1) {
				for (int inc = 1; inc < 6; inc++) {
					arena.markState(height - 1 - inc, width, BlockState.REACHABLE);
				}
			} else {
				int inc = 1;
				for (inc = 1; inc < distance; inc++) {
					arena.markState(height - 1 - inc, width, BlockState.REACHABLE);
				}
				if (arena.withinRange(height - 1 - inc, width))
					arena.markState(height - 1 - inc, width, BlockState.BLOCKED);
			}
			break;
		}
	}

}
