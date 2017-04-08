/*
 * Way points are the target points that the navigator must reach
 * Way points must be cleared before termination of a process
 */
public class WayPoint {

	private int height;
	private int width;

	public WayPoint(int height, int width) {
		this.height = height;
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
}
