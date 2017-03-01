import java.util.LinkedList;

class MovementNode {
    int move;

    public MovementNode(int m) {
	this.move = m;
    }

    public String toString() {
	String result = "";
	switch (this.move) {
	case Movement.MOVE:
	    result = "Move forward -> ";
	    break;
	case Movement.TURN_LEFT:
	    result = "Turn left -> ";
	    break;
	case Movement.TURN_RIGHT:
	    result = "Turn right -> ";
	}
	return result;
    }

    public int getMovement() {
	return this.move;
    }
}

public class MovementSequence {
    private int tail;
    public LinkedList<MovementNode> moveList;

    public MovementSequence() {
	this.tail = 0;
	this.moveList = new LinkedList<MovementNode>();
    }

    public void addMovement(int movement) {
	MovementNode node = new MovementNode(movement);
	moveList.add(tail, node);
	this.tail++;
    }

    public String outputMovements() {
	String result = "";
	for (MovementNode move : this.moveList) {
	    switch (move.move) {
	    case Movement.MOVE:
		result = result + "Move forward ->";
		break;
	    case Movement.TURN_LEFT:
		result = result + "Turn Left ->";
		break;
	    case Movement.TURN_RIGHT:
		result = result + "Turn right ->";
		break;
	    }
	}
	return result;
    }

}
