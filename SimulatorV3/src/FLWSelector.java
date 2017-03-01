/*
 * Implement the logic used in the first stage of exploration
 * The navigator will follow the left wall until it return to the starting zone again 
 */
public class FLWSelector extends ActionSelector {

    public FLWSelector(Arena arena, Navigator navigator) {
    	super(arena, navigator);
    }

    public void setTargets() {
		super.addWayPoint(1, 1);
		// the upper-right corner
		super.addWayPoint(18, 13);
    }
    
    /*
	 * Check whether phase I of exploration is completed
	 */
    public boolean checkCommpletion() {
		int height = super.navigator.getHeight();
		int width = super.navigator.getWidth();
	
		super.compareWayPoints(height, width);
	
		if (super.isWayPointsEmpty())
		    return true;
		else
		    return false;
    }
    
    /*
	 * (non-Javadoc)
	 * 
	 * @see ActionSelector#selectActions() String returned will be sent to other
	 * components
	 */
    
    @Override
    public String selectActions() {
    	// get navigator information
		int[] distances = super.getSensorReading();
		int height = super.navigator.getHeight();
		int width = super.navigator.getWidth();
		int direction = super.navigator.getCurDirection();
		
		// update the map
		super.readAndUpdate();
		
		// print the map
		System.out.println();
		arena.printMap(navigator.getHeight(), navigator.getWidth());
	
		boolean movable[] = super.isMovable();
		// select the next move
		if (movable[1] == true) {
		    //update navigator information
		    //super.navigator.showBot(arena, navigator);
		    super.navigator.turnLeft();
		    if(GlobalVariables.simulate == 1)
		    {
			    GlobalVariables.earlyCompletion(arena, GlobalVariables.percentage);
			    try {
					Thread.currentThread();
					Thread.sleep(GlobalVariables.time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		    //super.navigator.showBot(arena, navigator);
		    super.navigator.move();
		    if(GlobalVariables.simulate == 1)
			{
			    GlobalVariables.earlyCompletion(arena, GlobalVariables.percentage);
			    try {
					Thread.currentThread();
					Thread.sleep(GlobalVariables.time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			    // generate return for the robot
		    }
		    return "turn left and move forward for one block";
		} else {
		    if (movable[0] == true) {
		    //super.navigator.showBot(arena, navigator);
			super.navigator.move();
			if(GlobalVariables.simulate == 1)
		    {
				GlobalVariables.earlyCompletion(arena, GlobalVariables.percentage);
					try {
						Thread.currentThread();
						Thread.sleep(GlobalVariables.time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    }
			return "move forward";
		    } else {
		    //super.navigator.showBot(arena, navigator);
			super.navigator.turnRight();
			if(GlobalVariables.simulate == 1)
		    {
				GlobalVariables.earlyCompletion(arena, GlobalVariables.percentage);
					try {
						Thread.currentThread();
						Thread.sleep(GlobalVariables.time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    }
			return "turn right";
		    }
		}
    }
}
