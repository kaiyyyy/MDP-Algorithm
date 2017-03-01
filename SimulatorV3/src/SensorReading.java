/*
 * Sensor reading class convert the data from sensors to distance in the unit of block length
 * and store the data
 * Sensor reading class also implements the logic for sensor input in simulation
 */
public class SensorReading {
	/*
	 * Data from sensor readings in unit of block length A value of -1 means
	 * that no blocks within detectable range
	 */
	private int[] front; // can take value -1, 1 or 2
	private int[] left; // can take value -1, 1 or 2
	private int right; // long range sensor, can take value -1, 1, 2, 3, 4, 5

	/*
	 * Include arena and navigator to provide information
	 */
	private Navigator navigator;
	private Arena arena;

	public SensorReading(Navigator navigator, Arena arena) {
		this.navigator = navigator;
		this.arena = arena;
		this.front = new int[3];
		this.left = new int[2];
	}

	/*
	 * Functions for getting sensor readings in each directions
	 */
	public int[] getFrontReadings() {
		return this.front;
	}

	public int[] getLeftReadings() {
		return this.left;
	}

	public int getRightReadings() {
		return this.right;
	}

	/*
	 * Function that update the sensor readings, currently based on simulation
	 */
	public void getSensorReadings() {
		this.detectFront();
		this.detectLeft();
		this.detectRight();
	}

	/*
	 * Functions that get sensor reading in each direction, currently based on
	 * simulation
	 */
	public void detectFront() {
		int cur_height = this.navigator.getHeight();
		int cur_width = this.navigator.getWidth();
		for (int index = 0; index < 3; index++) {
			switch (navigator.getCurDirection()) {
			case Direction.NORTH:
				if (!arena.withinRange(cur_height + 2, cur_width - 1 + index)
						|| (this.arena.sensorGetState(cur_height + 2, cur_width - 1 + index) == BlockState.BLOCKED))
					front[index] = 1;
				else {
					if (!arena.withinRange(cur_height + 3, cur_width - 1 + index)
							|| (this.arena.sensorGetState(cur_height + 3, cur_width - 1 + index) == BlockState.BLOCKED))
						front[index] = 2;
					else
						front[index] = -1;
				}
				break;
			case Direction.SOUTH:
				if (!arena.withinRange(cur_height - 2, cur_width + 1 - index)
						|| (this.arena.sensorGetState(cur_height - 2, cur_width + 1 - index) == BlockState.BLOCKED))
					front[index] = 1;
				else {
					if (!arena.withinRange(cur_height - 3, cur_width + 1 - index)
							|| (this.arena.sensorGetState(cur_height - 3, cur_width + 1 - index) == BlockState.BLOCKED))
						front[index] = 2;
					else
						front[index] = -1;
				}
				break;
			case Direction.WEST:
				if (!arena.withinRange(cur_height - 1 + index, cur_width - 2)
						|| (this.arena.sensorGetState(cur_height - 1 + index, cur_width - 2) == BlockState.BLOCKED))
					front[index] = 1;
				else {
					if (!arena.withinRange(cur_height - 1 + index, cur_width - 3)
							|| (this.arena.sensorGetState(cur_height - 1 + index, cur_width - 3) == BlockState.BLOCKED))
						front[index] = 2;
					else
						front[index] = -1;
				}
				break;
			case Direction.EAST:
				if (!arena.withinRange(cur_height + 1 - index, cur_width + 2)
						|| (this.arena.sensorGetState(cur_height + 1 - index, cur_width + 2) == BlockState.BLOCKED))
					front[index] = 1;
				else {
					if (!arena.withinRange(cur_height + 1 - index, cur_width + 3)
							|| (this.arena.sensorGetState(cur_height + 1 - index, cur_width + 3) == BlockState.BLOCKED))
						front[index] = 2;
					else
						front[index] = -1;
				}
				break;
			}
		}
	}

	public void detectLeft() {
		int cur_height = this.navigator.getHeight();
		int cur_width = this.navigator.getWidth();
		for (int index = 0; index < 2; index++) {
			switch (navigator.getCurDirection()) {
			case Direction.NORTH:
				if (!arena.withinRange(cur_height + 1 - index, cur_width - 2)
						|| (this.arena.sensorGetState(cur_height + 1 - index, cur_width - 2) == BlockState.BLOCKED))
					left[index] = 1;
				else {
					if (!arena.withinRange(cur_height + 1 - index, cur_width - 3)
							|| (this.arena.sensorGetState(cur_height + 1 - index, cur_width - 3) == BlockState.BLOCKED))
						left[index] = 2;
					else
						left[index] = -1;
				}
				break;
			case Direction.SOUTH:
				if (!arena.withinRange(cur_height - 1 + index, cur_width + 2)
						|| (this.arena.sensorGetState(cur_height - 1 + index, cur_width + 2) == BlockState.BLOCKED))
					left[index] = 1;
				else {
					if (!arena.withinRange(cur_height - 1 + index, cur_width + 3)
							|| (this.arena.sensorGetState(cur_height - 1 + index, cur_width + 3) == BlockState.BLOCKED))
						left[index] = 2;
					else
						left[index] = -1;
				}
				break;
			case Direction.WEST:
				if (!arena.withinRange(cur_height - 2, cur_width - 1 + index)
						|| (this.arena.sensorGetState(cur_height - 2, cur_width - 1 + index) == BlockState.BLOCKED))
					left[index] = 1;
				else {
					if (!arena.withinRange(cur_height - 3, cur_width - 1 + index)
							|| (this.arena.sensorGetState(cur_height - 3, cur_width - 1 + index) == BlockState.BLOCKED))
						left[index] = 2;
					else
						left[index] = -1;
				}
				break;
			case Direction.EAST:
				if (!arena.withinRange(cur_height + 2, cur_width + 1 - index)
						|| (this.arena.sensorGetState(cur_height + 2, cur_width + 1 - index) == BlockState.BLOCKED))
					left[index] = 1;
				else {
					if (!arena.withinRange(cur_height + 3, cur_width + 1 - index)
							|| (this.arena.sensorGetState(cur_height + 3, cur_width + 1 - index) == BlockState.BLOCKED))
						left[index] = 2;
					else
						left[index] = -1;
				}
				break;
			}
		}
	}

	public void detectRight() {
		int cur_height = navigator.getHeight();
		int cur_width = navigator.getWidth();

		int direction = navigator.getCurDirection();
		boolean block_detected;
		switch (direction) {
		case Direction.NORTH:
			block_detected = false;
			for (int distance = 1; distance < 6; distance++) {
				if (!arena.withinRange(cur_height, cur_width + 1 + distance)
						|| (this.arena.sensorGetState(cur_height, cur_width + 1 + distance) == BlockState.BLOCKED)) {
					block_detected = true;
					this.right = distance;
					break;
				}
			}
			if (!block_detected)
				this.right = -1;
			break;
		case Direction.SOUTH:
			block_detected = false;
			for (int distance = 1; distance < 6; distance++) {
				if (!arena.withinRange(cur_height, cur_width - 1 - distance)
						|| (this.arena.sensorGetState(cur_height, cur_width - 1 - distance) == BlockState.BLOCKED)) {
					block_detected = true;
					this.right = distance;
					break;
				}
			}
			if (!block_detected)
				this.right = -1;
			break;
		case Direction.WEST:
			block_detected = false;
			for (int distance = 1; distance < 6; distance++) {
				if (!arena.withinRange(cur_height + 1 + distance, cur_width)
						|| (this.arena.sensorGetState(cur_height + 1 + distance, cur_width) == BlockState.BLOCKED)) {
					block_detected = true;
					this.right = distance;
					break;
				}
			}
			if (!block_detected)
				this.right = -1;
			break;
		case Direction.EAST:
			block_detected = false;
			for (int distance = 1; distance < 6; distance++) {
				if (!arena.withinRange(cur_height - 1 - distance, cur_width)
						|| (this.arena.sensorGetState(cur_height - 1 - distance, cur_width) == BlockState.BLOCKED)) {
					block_detected = true;
					this.right = distance;
					break;
				}
			}
			if (!block_detected)
				this.right = -1;
			break;
		}
	}
}
