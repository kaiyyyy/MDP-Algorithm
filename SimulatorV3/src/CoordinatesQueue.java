import java.awt.Color;

class CoordinatesNode {
    private int height;
    private int width;

    public CoordinatesNode next;

    public CoordinatesNode(int height, int width) {
	this.height = height;
	this.width = width;
	this.next = null;
    }

    public void setNext(CoordinatesNode node) {
	this.next = node;
    }

    public int getHeight() {
	return this.height;
    }

    public int getWidth() {
	return this.width;
    }

    public String toString() {
	return "(" + this.height + "," + this.width + ") ";
    }

}

public class CoordinatesQueue {
    private int size;
    private CoordinatesNode front;
    private CoordinatesNode back;

    public CoordinatesQueue() {
	this.size = 0;
	this.front = null;
	this.back = null;
    }

    public String toString() {
	String result = "Coordinates: ";
	CoordinatesNode node = this.front;
	while (node != null) {
	    result = result + node.toString();
	    node = node.next;
	}
	result += "\n";
	return result;
    }

    public int size() {
	return this.size;
    }

    public boolean isEmpty() {
	return (this.size == 0);
    }

    public void enqueue(int height, int width) {
	CoordinatesNode node = new CoordinatesNode(height, width);
	if (this.isEmpty()) {
	    this.front = node;
	    this.back = node;
	    this.size++;
	} else {
	    this.back.setNext(node);
	    this.back = node;
	    this.size++;
	}
    }

    public void addFront(int height, int width) {
	CoordinatesNode node = new CoordinatesNode(height, width);

	if (this.isEmpty()) {
	    this.front = node;
	    this.back = node;
	    this.size++;
	} else {
	    node.setNext(this.front);
	    this.front = node;
	    this.size++;
	}
    }

    public CoordinatesNode dequeue() {
	CoordinatesNode result = this.front;

	if (this.size == 1) {
	    this.front = null;
	    this.back = null;
	    this.size--;
	} else {
	    this.front = this.front.next;
	    this.size--;
	}

	return result;
    }

    public MovementSequence findMovements(int cur_direction) {
	MovementSequence movements = new MovementSequence();

	if (this.size == 1) {
	    return movements;
	} else {
	    while (this.size > 1) {
		CoordinatesNode cur_node = this.dequeue();
		CoordinatesNode next_node = cur_node.next;
		System.out.println(cur_node.getHeight() + " " + cur_node.getWidth());
		if(GlobalVariables.simulate == 1 && GlobalVariables.shortestrun == 1)
		{
			GlobalVariables.showMappingofPath.grids[cur_node.getHeight()][cur_node.getWidth()].setBackground(Color.MAGENTA);
		}

		// Move to North
		if ((next_node.getHeight() - cur_node.getHeight()) == 1) {
		    switch (cur_direction) {
		    case (Direction.NORTH):
			break;
		    case (Direction.SOUTH):
			movements.addMovement(Movement.TURN_LEFT);
			movements.addMovement(Movement.TURN_LEFT);
			break;
		    case (Direction.WEST):
			movements.addMovement(Movement.TURN_RIGHT);
			break;
		    case (Direction.EAST):
			movements.addMovement(Movement.TURN_LEFT);
			break;
		    }
		    movements.addMovement(Movement.MOVE);
		    cur_direction = Direction.NORTH;
		}

		// Move to South
		if ((next_node.getHeight() - cur_node.getHeight()) == -1) {
		    switch (cur_direction) {
		    case (Direction.NORTH):
			movements.addMovement(Movement.TURN_LEFT);
			movements.addMovement(Movement.TURN_LEFT);
			break;
		    case (Direction.SOUTH):
			break;
		    case (Direction.WEST):
			movements.addMovement(Movement.TURN_LEFT);
			break;
		    case (Direction.EAST):
			movements.addMovement(Movement.TURN_RIGHT);
			break;
		    }
		    movements.addMovement(Movement.MOVE);
		    cur_direction = Direction.SOUTH;
		}

		// Move to West
		if ((next_node.getWidth() - cur_node.getWidth()) == -1) {
		    switch (cur_direction) {
		    case (Direction.NORTH):
			movements.addMovement(Movement.TURN_LEFT);
			break;
		    case (Direction.SOUTH):
			movements.addMovement(Movement.TURN_RIGHT);
			break;
		    case (Direction.WEST):
			break;
		    case (Direction.EAST):
			movements.addMovement(Movement.TURN_LEFT);
			movements.addMovement(Movement.TURN_LEFT);
			break;
		    }
		    movements.addMovement(Movement.MOVE);
		    cur_direction = Direction.WEST;
		}

		// Move to East
		if ((next_node.getWidth() - cur_node.getWidth()) == 1) {
		    switch (cur_direction) {
		    case (Direction.NORTH):
			movements.addMovement(Movement.TURN_RIGHT);
			break;
		    case (Direction.SOUTH):
			movements.addMovement(Movement.TURN_LEFT);
			break;
		    case (Direction.WEST):
			movements.addMovement(Movement.TURN_LEFT);
			movements.addMovement(Movement.TURN_LEFT);
			break;
		    case (Direction.EAST):
			break;
		    }
		    movements.addMovement(Movement.MOVE);
		    cur_direction = Direction.EAST;
		}

		// update the two nodes
		cur_node = next_node;
		next_node = cur_node.next;
	    }
	}

	return movements;
    }

}