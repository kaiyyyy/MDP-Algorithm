/*
 * Dijkastra's Algorithm find a path from the starting point to the ending point
 * 
 */

class Node {
    public double estimate;
    public boolean found;
    public int pre_height;
    public int pre_width;
    public int cur_direction;

    public Node(boolean found) {
	this.found = found;
	this.estimate = 99999;
	this.pre_height = -1;
	this.pre_width = -1;
	this.cur_direction = Direction.NORTH;
    }
}

public class SPSelector extends ActionSelector {
    private int target_height;
    private int target_width;
    private double cur_shortest_estimate;

    private Node blocks[][];

    public SPSelector(Arena arena, Navigator navigator) {
	super(arena, navigator);
	this.target_height = 0;
	this.target_width = 0;
	this.cur_shortest_estimate = 99999;

	// initiate the array of nodes to record data
	this.blocks = new Node[20][15];
	for (int height = 19; height >= 0; height--) {
	    for (int width = 14; width >= 0; width--) {
		int state = arena.getState(height, width);
		if (!arena.withinCenterRange(height, width) || state == BlockState.BLOCKED
			|| state == BlockState.UNREACHABLE) {
		    blocks[height][width] = new Node(true);
		} else {
		    blocks[height][width] = new Node(false);
		}
	    }
	}

	blocks[1][1].estimate = 0;
    }

    private void findNextTarget() {
		this.cur_shortest_estimate = 99999;
		for (int height = 18; height >= 1; height--) {
		    for (int width = 13; width >= 1; width--) {
				if (!this.blocks[height][width].found) {
				    if (this.cur_shortest_estimate > this.blocks[height][width].estimate) {
					this.cur_shortest_estimate = this.blocks[height][width].estimate;
					this.target_height = height;
					this.target_width = width;
				    }
				}
		    }
		}
		
		this.blocks[target_height][target_width].found = true;
	
    }

    private void updateEstimate() {
	// update the block to the left
	if (arena.withinCenterRange(target_height, target_width - 1)
		&& !this.blocks[target_height][target_width - 1].found
		&& arena.isMovable(target_height, target_width - 1)) {
	    int r_turns = Direction.WEST - blocks[target_height][target_width].cur_direction;
	    int l_turns = 4 - r_turns;
	    double l_cost = Math.min(l_turns * super.turn_left_cost, r_turns * super.turn_right_cost) + super.move_cost
		    + this.blocks[target_height][target_width].estimate;
	    if (l_cost < this.blocks[target_height][target_width - 1].estimate) {
		this.blocks[target_height][target_width - 1].estimate = l_cost;
		this.blocks[target_height][target_width - 1].cur_direction = Direction.WEST;
		this.blocks[target_height][target_width - 1].pre_height = target_height;
		this.blocks[target_height][target_width - 1].pre_width = target_width;
	    }
	}
	// update the block to the right
	if (arena.withinCenterRange(target_height, target_width + 1)
		&& !this.blocks[target_height][target_width + 1].found
		&& arena.isMovable(target_height, target_width + 1)) {
	    int r_turns = (Direction.EAST >= this.blocks[target_height][target_width].cur_direction)
		    ? (Direction.EAST - this.blocks[target_height][target_width].cur_direction)
		    : (4 + Direction.EAST - this.blocks[target_height][target_width].cur_direction);
	    int l_turns = 4 - r_turns;
	    double r_cost = Math.min(l_turns * super.turn_left_cost, r_turns * super.turn_right_cost) + super.move_cost
		    + this.blocks[target_height][target_width].estimate;
	    if (r_cost < this.blocks[target_height][target_width + 1].estimate) {
		this.blocks[target_height][target_width + 1].estimate = r_cost;
		this.blocks[target_height][target_width + 1].cur_direction = Direction.EAST;
		this.blocks[target_height][target_width + 1].pre_height = target_height;
		this.blocks[target_height][target_width + 1].pre_width = target_width;
	    }
	}
	// update the block above
	if (arena.withinCenterRange(target_height + 1, target_width)
		&& !this.blocks[target_height + 1][target_width].found
		&& arena.isMovable(target_height + 1, target_width)) {
	    int r_turns = 4 - (this.blocks[target_height][target_width].cur_direction - Direction.NORTH);
	    int l_turns = 4 - r_turns;
	    double u_cost = Math.min(l_turns * super.turn_left_cost, r_turns * super.turn_right_cost) + super.move_cost
		    + this.blocks[target_height][target_width].estimate;
	    if (u_cost < this.blocks[target_height + 1][target_width].estimate) {
		this.blocks[target_height + 1][target_width].estimate = u_cost;
		this.blocks[target_height + 1][target_width].cur_direction = Direction.NORTH;
		this.blocks[target_height + 1][target_width].pre_height = target_height;
		this.blocks[target_height + 1][target_width].pre_width = target_width;
	    }
	}
	// update the block below
	if (arena.withinCenterRange(target_height - 1, target_width)
		&& !this.blocks[target_height - 1][target_width].found
		&& arena.isMovable(target_height - 1, target_width)) {
	    int r_turns = (Direction.SOUTH >= this.blocks[target_height - 1][target_width].cur_direction)
		    ? (Direction.SOUTH - this.blocks[target_height - 1][target_width].cur_direction)
		    : (4 + Direction.SOUTH - this.blocks[target_height - 1][target_width].cur_direction);
	    int l_turns = 4 - r_turns;
	    double d_cost = Math.min(l_turns * super.turn_left_cost, r_turns * super.turn_right_cost) + super.move_cost
		    + this.blocks[target_height][target_width].estimate;
	    if (d_cost < this.blocks[target_height + 1][target_width].estimate) {
		this.blocks[target_height - 1][target_width].estimate = d_cost;
		this.blocks[target_height - 1][target_width].cur_direction = Direction.SOUTH;
		this.blocks[target_height - 1][target_width].pre_height = target_height;
		this.blocks[target_height - 1][target_width].pre_width = target_width;
	    }
	}
    }

    @Override
    public String selectActions() {
	// find the shortest path
	while (!this.blocks[18][13].found) {
	    this.findNextTarget();
	    this.updateEstimate();
	}
	// generate coordinates queue
	int cur_height = 18;
	int cur_width = 13;
	int pre_height, pre_width;

	CoordinatesQueue cQueue = new CoordinatesQueue();
	cQueue.addFront(cur_height, cur_width);
	while ((cur_height != 1) || (cur_width != 1)) {
	    pre_height = blocks[cur_height][cur_width].pre_height;
	    pre_width = blocks[cur_height][cur_width].pre_width;

	    cQueue.addFront(pre_height, pre_width);

	    cur_height = pre_height;
	    cur_width = pre_width;
	}

	MovementSequence mQueue = cQueue.findMovements(navigator.getCurDirection());
	return mQueue.outputMovements();

    }

}