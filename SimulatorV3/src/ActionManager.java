/*
 * Action manager manage the behavior of navigator on general
 * It executes the explore and find shortest path instruction from Android interface
 */
public class ActionManager implements Runnable{
    private Arena arena;
    private Navigator navigator;

    public ActionManager(Arena arena, Navigator navigator) {
		this.arena = arena;
		this.navigator = navigator;
    }
    
    public void run()
    {
    	navigate();
    }
    
    public void navigate() {
	FLWSelector flwselector = new FLWSelector(this.arena, this.navigator);
	// phase 1 navigation
	flwselector.setTargets();

	while (!flwselector.checkCommpletion()) {
	    String action = flwselector.selectActions();
	    System.out.println(action);

	    // send message to navigator to move
	}

	System.out.println("PhaseIComplete*******************");

		this.phaseIINavigate();
    }

    private void phaseIINavigate() {
		PhaseIISelector selector = new PhaseIISelector(this.arena, this.navigator);
	
		while (selector.findNextTarget()) {
		    System.out.println(selector.selectActions());
		    selector.rotateAndDetect(4);
		    arena.printMap(navigator.getHeight(), navigator.getWidth());
		}
	
			System.out.println(selector.returnToOrigin());
			arena.printMap(navigator.getHeight(), navigator.getWidth());
			
			if(GlobalVariables.simulate == 1)
			{
				Thread.currentThread().stop();
			}

    }
    
    public void solveMaze(){
    	PhaseIISelector selector = new PhaseIISelector(this.arena, this.navigator);
    	System.out.println(selector.goToGoal());
    }

    private void shortestPath() {
		SPSelector selector = new SPSelector(this.arena, this.navigator);
		selector.selectActions();
    }
}
