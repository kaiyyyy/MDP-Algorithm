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
		MovementNode move;
		while (this.moveList.size() > 0) {
			move = this.moveList.removeFirst();
			switch (move.move) {
			case Movement.MOVE:
				int moveDistance = 1;
				if (this.moveList.size() > 0) {
					move = this.moveList.peek();
					while (move.move == Movement.MOVE) {
						this.moveList.removeFirst();
						moveDistance++;
						if (this.moveList.size() > 0) {
							move = this.moveList.peek();
						} else {
							break;
						}
					}
				}
				result = result += "Move Forward " + moveDistance + " blocks ->";
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
