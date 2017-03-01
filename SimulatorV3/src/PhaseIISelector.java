public class PhaseIISelector extends ActionSelector {
    private int direction;
    int target_height;
    int target_width;
    private int move_direction[][]; // 1 - up | 2 - down | 3 - left | 4 - right

    public PhaseIISelector(Arena arena, Navigator navigator) {
		super(arena, navigator);
		this.direction = Direction.NORTH;
		this.target_height = 0;
		this.target_width = 0;
		this.move_direction = new int[20][15];
		for (int row = 0; row < 20; row++) {
		    for (int col = 0; col < 15; col++) {
			this.move_direction[row][col] = 0;
		    }
		}
    }

    // Use breadth-first-search to find the nearest unexplored block
    public boolean findNextTarget() {
		boolean[][] visited = new boolean[20][15];
		for (int row = 0; row < 19; row++) {
		    for (int col = 0; col < 15; col++) {
			visited[row][col] = false;
		    }
	}

	int cur_height = super.navigator.getHeight();
	int cur_width = super.navigator.getWidth();

	CoordinatesQueue queue = new CoordinatesQueue();
	queue.enqueue(cur_height, cur_width);

	while (!queue.isEmpty()) {
	    CoordinatesNode node = queue.dequeue();
	    int height = node.getHeight();
	    int width = node.getWidth();

	    // north
	    if (arena.withinRange(height + 1, width)) {
		if ((!visited[height + 1][width]) && arena.isMovableNorth(height, width)) {
		    this.move_direction[height + 1][width] = Direction.NORTH;
		    if (arena.unexploredInDetection(height + 1, width)) {
			this.target_height = height + 1;
			this.target_width = width;
			return true;
		    }
		    visited[height + 1][width] = true;
		    queue.enqueue(height + 1, width);
		}
	    }

	    // east
	    if (arena.withinRange(height, width + 1)) {
		if ((!visited[height][width + 1]) && arena.isMovableEast(height, width)) {
		    this.move_direction[height][width + 1] = Direction.EAST;
		    if (arena.unexploredInDetection(height, width + 1)) {
			this.target_height = height;
			this.target_width = width + 1;
			return true;
		    }
		    visited[height][width + 1] = true;
		    queue.enqueue(height, width + 1);
		}
	    }

	    // west
	    if (arena.withinRange(height, width - 1)) {
		if ((!visited[height][width - 1]) && arena.isMovableWest(height, width)) {
		    this.move_direction[height][width - 1] = Direction.WEST;
		    if (arena.unexploredInDetection(height, width - 1)) {
			this.target_height = height;
			this.target_width = width - 1;
			return true;
		    }
		    visited[height][width - 1] = true;
		    queue.enqueue(height, width - 1);
		}
	    }

	    // south
	    if (arena.withinRange(height - 1, width)) {
		if ((!visited[height - 1][width]) && arena.isMovableSouth(height, width)) {
		    this.move_direction[height - 1][width] = Direction.SOUTH;
		    if (arena.unexploredInDetection(height - 1, width)) {
			this.target_height = height - 1;
			this.target_width = width;
			return true;
		    }
		    visited[height - 1][width] = true;
		    queue.enqueue(height - 1, width);
		}
	    }

	}
	return false;

    }

    public CoordinatesQueue findCoordiatesSequeunce() {
	CoordinatesQueue queue = new CoordinatesQueue();

	int cur_height = target_height;
	int cur_width = target_width;

	int sta_height = super.navigator.getHeight();
	int sta_width = super.navigator.getWidth();
	queue.addFront(cur_height, cur_width);

	while ((cur_height != sta_height) || (cur_width != sta_width)) {
	    int d = this.move_direction[cur_height][cur_width];

	    switch (d) {
		    case Direction.NORTH: // moved up
				queue.addFront(cur_height - 1, cur_width);
				cur_height--;
				break;
		    case Direction.SOUTH: // moved down
				queue.addFront(cur_height + 1, cur_width);
				cur_height++;
				break;
		    case Direction.WEST: // moved left
				queue.addFront(cur_height, cur_width + 1);
				cur_width++;
				break;
		    case Direction.EAST: // moved right
				queue.addFront(cur_height, cur_width - 1);
				cur_width--;
				break;
	    }

	}

	return queue;
    }

    // generate a queue of node to the unexplored points
    @Override
    public String selectActions() {
	CoordinatesQueue queue = this.findCoordiatesSequeunce();
	MovementSequence mQueue = queue.findMovements(navigator.getCurDirection());
	
	for (MovementNode m : mQueue.moveList) {
	    switch (m.getMovement()) {
	    case Movement.MOVE:
	    	//super.navigator.showBot(arena, navigator);
	    	navigator.move();
	    	if(GlobalVariables.simulate == 1)
		    {
	    		arena.printMap(navigator.getHeight(), navigator.getWidth());
		    
	    	
				try {
					Thread.currentThread();
					Thread.sleep(GlobalVariables.time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		break;
	    case Movement.TURN_LEFT:
	    	//super.navigator.showBot(arena, navigator);
	    	navigator.turnLeft();
	    	if(GlobalVariables.simulate == 1)
		    {
	    		arena.printMap(navigator.getHeight(), navigator.getWidth());
		    
	    	
				try {
					Thread.currentThread();
					Thread.sleep(GlobalVariables.time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		break;
	    case Movement.TURN_RIGHT:
	    	//super.navigator.showBot(arena, navigator);
	    	navigator.turnRight();
	    	if(GlobalVariables.simulate == 1)
		    {
	    		arena.printMap(navigator.getHeight(), navigator.getWidth());
		    
	    	
				try {
					Thread.currentThread();
					Thread.sleep(GlobalVariables.time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		break;
	    }
	    super.readAndUpdate();
	}

	String result = mQueue.outputMovements();
	return result;
    }

    public void rotateAndDetect(int repetition) {
	if (repetition == 0)
	    return;
	else {
		    super.readAndUpdate();
		    //super.navigator.showBot(arena, navigator);
		    navigator.turnRight();
		    if(GlobalVariables.simulate == 1)
		    {
			    	arena.printMap(navigator.getHeight(), navigator.getWidth());
			    
				
			    try {
					Thread.currentThread();
					Thread.sleep(GlobalVariables.time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    rotateAndDetect(repetition - 1);
		}
    }

    public String returnToOrigin() {
		this.target_height = 1;
		this.target_width = 1;

		return this.selectActions();
    }
    
    public String goToGoal() {
    	this.target_height = 18;
    	this.target_width = 14;
    	return this.selectActions();
    }

}
