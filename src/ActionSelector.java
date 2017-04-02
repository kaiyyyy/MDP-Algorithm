import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Stack;

/*
 * Action selector super class
 * Action selector and its child classes implements the logic used in difference phase of the movement
 */
public abstract class ActionSelector {
	protected Arena arena;
	protected Navigator navigator;
	protected SensorReading readings;
	protected ProgressControl control;
	protected OutgoingMessageThread messageSendingThread;
	protected IncomingMessageThread messageReceivingThread;
	/*
	 * Stack of way points that must be cleared
	 */

	private Stack<WayPoint> targets = new Stack<WayPoint>();

	/*
	 * Abstract method to be implemented by each subclass This method will
	 * return the string sent to other components
	 */
	public abstract String selectActions() throws InterruptedException;

	// cost of actions, to be modified based on real time taken to perform
	// actions
	protected double move_cost = 1;
	protected double turn_left_cost = 50;
	protected double turn_right_cost = 50;

	/*
	 * Limit for sensor update
	 */
	private int updateLimitFront = 2;
	private int updateLimitLeft = 2;
	private int updateLimitRight = 3;

	/*
	 * Count the step before last left adjustment
	 */
	protected int moveCount;
	protected int leftAlignmentMoveCount = 1;

	public ActionSelector(Arena arena, Navigator navigator, ProgressControl control, IncomingMessageThread in,
			OutgoingMessageThread out)
			throws NumberFormatException, UnknownHostException, InterruptedException, IOException {
		this.arena = arena;
		this.navigator = navigator;
		this.readings = new SensorReading(navigator, arena);
		this.messageReceivingThread = in;
		this.messageSendingThread = out;
		this.control = control;
		this.moveCount = 0;
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
	 * Try to align itself with the current position and direction
	 */
	public boolean tryAlignmentFront() {
		int height = this.navigator.getHeight();
		int width = this.navigator.getWidth();
		int direction = this.navigator.getCurDirection();

		switch (direction) {
		case Direction.NORTH:
			for (int index = 0; index < 3; index++) {
				if (arena.withinRange(height + 2, width - 1 + index)
						&& (arena.getState(height + 2, width - 1 + index) != BlockState.BLOCKED))
					return false;
			}
			break;
		case Direction.SOUTH:
			for (int index = 0; index < 3; index++) {
				if (arena.withinRange(height - 2, width - 1 + index)
						&& (arena.getState(height - 2, width - 1 + index) != BlockState.BLOCKED))
					return false;
			}
			break;
		case Direction.WEST:
			for (int index = 0; index < 3; index++) {
				if (arena.withinRange(height - 1 + index, width - 2)
						&& (arena.getState(height - 1 + index, width - 2) != BlockState.BLOCKED))
					return false;
			}
			break;
		case Direction.EAST:
			for (int index = 0; index < 3; index++) {
				if (arena.withinRange(height + 1 - index, width + 2)
						&& (arena.getState(height + 1 - index, width + 2) != BlockState.BLOCKED))
					return false;
			}
			break;
		}
		return true;
	}

	public boolean tryAlignmentLeft() {
		this.moveCount++;
		if (this.moveCount >= this.leftAlignmentMoveCount) {
			boolean result = readings.alignmentCheckLeft();
			if (result)
				this.moveCount = 0;
			return result;
		}
		return false;
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

	public boolean readAndOverwrite() {
		this.readings.getSensorReadings();
		this.overwriteFront();
		boolean result = this.overwriteLeft();
		this.overwriteRight();

		return result;
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
					for (int distance = 1; distance <= this.updateLimitFront; distance++) {
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
				arena.confirmMap(height + 2, width - 1 + index);
			}
			break;
		case Direction.SOUTH:
			for (int index = 0; index < 3; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance <= this.updateLimitFront; distance++) {
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
				arena.confirmMap(height - 2, width + 1 - index);
			}
			break;
		case Direction.WEST:
			for (int index = 0; index < 3; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance <= this.updateLimitFront; distance++) {
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
				arena.confirmMap(height - 1 + index, width - 2);
			}
			break;
		case Direction.EAST:
			for (int index = 0; index < 3; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance <= this.updateLimitFront; distance++) {
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
				arena.confirmMap(height + 1 - index, width + 2);
			}
			break;
		}
	}

	public void overwriteFront() {
		// update the information about blocks in front
		int[] distances = readings.getFrontReadings();

		int height = navigator.getHeight();
		int width = navigator.getWidth();
		int direction = navigator.getCurDirection();

		switch (direction) {
		case Direction.NORTH:
			for (int index = 0; index < 3; index++) {
				if (distances[index] != 1) {
					this.arena.overwiteState(height + 2, width - 1 + index, BlockState.REACHABLE);
				} else {
					this.arena.overwiteState(height + 2, width - 1 + index, BlockState.BLOCKED);
				}
				arena.confirmMap(height + 2, width - 1 + index);
			}
			break;
		case Direction.SOUTH:
			for (int index = 0; index < 3; index++) {
				if (distances[index] != 1) {
					this.arena.overwiteState(height - 2, width + 1 - index, BlockState.REACHABLE);
				} else {
					this.arena.overwiteState(height - 2, width + 1 - index, BlockState.BLOCKED);
				}
				arena.confirmMap(height - 2, width + 1 - index);
			}
			break;
		case Direction.WEST:
			for (int index = 0; index < 3; index++) {
				if (distances[index] != 1) {
					this.arena.overwiteState(height - 1 + index, width - 2, BlockState.REACHABLE);
				} else {
					this.arena.overwiteState(height - 1 + index, width - 2, BlockState.BLOCKED);
				}
				arena.confirmMap(height - 1 + index, width - 2);
			}
			break;
		case Direction.EAST:
			for (int index = 0; index < 3; index++) {
				if (distances[index] != 1) {
					this.arena.overwiteState(height + 1 - index, width + 2, BlockState.REACHABLE);
				} else {
					this.arena.overwiteState(height + 1 - index, width + 2, BlockState.BLOCKED);
				}
				arena.confirmMap(height + 1 - index, width + 2);
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
					for (int distance = 1; distance <= this.updateLimitLeft; distance++) {
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
				arena.confirmMap(height + 1 - index, width - 2);
			}
			break;
		case Direction.SOUTH:
			for (int index = 0; index < 2; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance <= this.updateLimitLeft; distance++) {
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
				arena.confirmMap(height - 1 + index, width + 2);
			}
			break;
		case Direction.WEST:
			for (int index = 0; index < 2; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance <= this.updateLimitFront; distance++) {
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
				arena.confirmMap(height - 2, width - 1 + index);
			}
			break;
		case Direction.EAST:
			for (int index = 0; index < 2; index++) {
				if (distances[index] == -1) {
					for (int distance = 1; distance <= this.updateLimitFront; distance++) {
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
				arena.confirmMap(height + 2, width + 1 - index);
			}
			break;
		}
	}

	public boolean overwriteLeft() {
		// update the information about blocks in front
		int[] distances = readings.getLeftReadings();

		int height = navigator.getHeight();
		int width = navigator.getWidth();
		int direction = navigator.getCurDirection();

		switch (direction) {
		case Direction.NORTH:
			for (int index = 0; index < 2; index++) {
				if (distances[index] != 1) {
					this.arena.overwiteState(height + 1 - index, width - 2, BlockState.REACHABLE);
				} else {
					this.arena.overwiteState(height + 1 - index, width - 2, BlockState.BLOCKED);
				}
				arena.confirmMap(height + 1 - index, width - 2);
			}
			break;
		case Direction.SOUTH:
			for (int index = 0; index < 2; index++) {
				if (distances[index] != 1) {
					this.arena.overwiteState(height - 1 + index, width + 2, BlockState.REACHABLE);
				} else {
					this.arena.overwiteState(height - 1 + index, width + 2, BlockState.BLOCKED);
				}
				arena.confirmMap(height - 1 + index, width + 2);
			}
			break;
		case Direction.WEST:
			for (int index = 0; index < 2; index++) {
				if (distances[index] != 1) {
					this.arena.overwiteState(height - 2, width - 1 + index, BlockState.REACHABLE);
				} else {
					this.arena.overwiteState(height - 2, width - 1 + index, BlockState.BLOCKED);
				}
				arena.confirmMap(height - 2, width - 1 + index);
			}
			break;
		case Direction.EAST:
			for (int index = 0; index < 2; index++) {
				if (distances[index] != 1) {
					this.arena.overwiteState(height + 2, width + 1 - index, BlockState.REACHABLE);
				} else {
					this.arena.overwiteState(height + 2, width + 1 - index, BlockState.BLOCKED);
				}
				arena.confirmMap(height + 2, width + 1 - index);
			}
			break;
		}

		if ((distances[0] != 1) && (distances[1] != 1))
			return true;
		else {
			return false;
		}
	}

	public boolean testLeftBottom() {
		int direction = this.navigator.getCurDirection();
		int height = this.navigator.getHeight();
		int width = this.navigator.getWidth();

		int state = BlockState.BLOCKED;
		switch (direction) {
		case Direction.NORTH:
			state = this.arena.getState(height - 1, width - 2);
			break;
		case Direction.SOUTH:
			state = this.arena.getState(height + 1, width + 2);
			break;
		case Direction.WEST:
			state = this.arena.getState(height - 2, width + 1);
			break;
		case Direction.EAST:
			state = this.arena.getState(height + 2, width - 1);
			break;
		}

		if (state == BlockState.BLOCKED)
			return true;
		else
			return false;
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
				for (int inc = 1; inc <= this.updateLimitRight; inc++) {
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
			arena.confirmMap(height, width + 2);
			break;
		case Direction.SOUTH:
			if (distance == -1) {
				for (int inc = 1; inc <= this.updateLimitRight; inc++) {
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
			arena.confirmMap(height, width - 2);
			break;
		case Direction.WEST:
			if (distance == -1) {
				for (int inc = 1; inc <= this.updateLimitRight; inc++) {
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
			arena.confirmMap(height + 2, width);
			break;
		case Direction.EAST:
			if (distance == -1) {
				for (int inc = 1; inc <= this.updateLimitRight; inc++) {
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
			arena.confirmMap(height - 2, width);
			break;
		}
	}

	public void overwriteRight() {
		// update the information about blocks in front
		int distance = readings.getRightReadings();

		int height = navigator.getHeight();
		int width = navigator.getWidth();
		int direction = navigator.getCurDirection();

		switch (direction) {
		case Direction.NORTH:
			if (distance != 1) {
				this.arena.overwiteState(height, width + 2, BlockState.REACHABLE);
			} else {
				this.arena.overwiteState(height, width + 2, BlockState.BLOCKED);
			}
			arena.confirmMap(height, width + 2);
			break;
		case Direction.SOUTH:
			if (distance != 1) {
				this.arena.overwiteState(height, width - 2, BlockState.REACHABLE);
			} else {
				this.arena.overwiteState(height, width - 2, BlockState.BLOCKED);
			}
			arena.confirmMap(height, width - 2);
			break;
		case Direction.WEST:
			if (distance != 1) {
				this.arena.overwiteState(height + 2, width, BlockState.REACHABLE);
			} else {
				this.arena.overwiteState(height + 2, width, BlockState.BLOCKED);
			}
			arena.confirmMap(height + 2, width);
			break;
		case Direction.EAST:
			if (distance != 1) {
				this.arena.overwiteState(height - 2, width, BlockState.REACHABLE);
			} else {
				this.arena.overwiteState(height - 2, width, BlockState.BLOCKED);
			}
			arena.confirmMap(height - 2, width);
			break;
		}
	}

}
