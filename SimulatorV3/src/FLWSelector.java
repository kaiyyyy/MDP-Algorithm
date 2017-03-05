import java.io.IOException;
import java.net.UnknownHostException;

/*
 * Implement the logic used in the first stage of exploration
 * The navigator will follow the left wall until it return to the starting zone again 
 */
public class FLWSelector extends ActionSelector {

	public FLWSelector(Arena arena, Navigator navigator, ProgressControl control, IncomingMessageThread in,
			OutgoingMessageThread out)
			throws NumberFormatException, UnknownHostException, InterruptedException, IOException {
		super(arena, navigator, control, in, out);
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
	public String selectActions() throws InterruptedException {
		// get navigator information
		int[] distances = super.getSensorReading();
		int height = super.navigator.getHeight();
		int width = super.navigator.getWidth();
		int direction = super.navigator.getCurDirection();

		/*
		 * Request for reading input before update. Current thread running
		 * action selection process will be blocked until readings from sensors
		 * come in
		 */
		try {
			control.requestIncoming();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// update the map
		super.readAndUpdate();

		// print the map
		System.out.println();
		arena.printMap(navigator.getHeight(), navigator.getWidth());

		boolean movable[] = super.isMovable();
		// select the next move
		if (movable[1] == true) {
			// update navigator information
			// super.navigator.showBot(arena, navigator);
			super.navigator.turnLeft();
			this.control.requstOutgoing();
			this.messageSendingThread.sendThisMessage("ma");
			

			//YAMAN TAKE NOTE
			//the next message is for Android
			this.messageSendingThread.sendThisMessage("a1" + arena.hexForAndroid1(arena.getMapDescriptor1Binary()));
			Thread.sleep(GlobalVariables.outsleep);
			this.messageSendingThread.sendThisMessage("a2" + arena.hexForAndroid2(arena.getMapDescriptor2Binary()));
			Thread.sleep(GlobalVariables.outsleep);
			this.messageSendingThread.sendThisMessage("ar" + navigator.getHeight() + "," + navigator.getWidth());
			Thread.sleep(GlobalVariables.outsleep);
			
			if (GlobalVariables.simulate == 1) {
				GlobalVariables.earlyCompletion(arena, GlobalVariables.percentage);
				try {
					Thread.currentThread();
					Thread.sleep(GlobalVariables.time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// super.navigator.showBot(arena, navigator);
			super.navigator.move();
			this.control.requstOutgoing();
			this.messageSendingThread.sendThisMessage("mw 10");
			
			//YAMAN TAKE NOTE
			//the next message is for Android
			this.messageSendingThread.sendThisMessage("a1" + arena.hexForAndroid1(arena.getMapDescriptor1Binary()));
			Thread.sleep(GlobalVariables.outsleep);
			this.messageSendingThread.sendThisMessage("a2" + arena.hexForAndroid2(arena.getMapDescriptor2Binary()));
			Thread.sleep(GlobalVariables.outsleep);
			this.messageSendingThread.sendThisMessage("ar" + navigator.getHeight() + "," + navigator.getWidth());
			Thread.sleep(GlobalVariables.outsleep);
			
			if (GlobalVariables.simulate == 1) {
				GlobalVariables.earlyCompletion(arena, GlobalVariables.percentage);
				try {
					Thread.currentThread();
					Thread.sleep(GlobalVariables.time);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// generate return for the robot

				/*
				 * Send out message
				 */
			}
			return "mamw 10";
		} else {
			if (movable[0] == true) {
				// super.navigator.showBot(arena, navigator);
				super.navigator.move();
				if (GlobalVariables.simulate == 1) {
					GlobalVariables.earlyCompletion(arena, GlobalVariables.percentage);
					try {
						Thread.currentThread();
						Thread.sleep(GlobalVariables.time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				this.control.requstOutgoing();
				this.messageSendingThread.sendThisMessage("mw 10");
				
				//YAMAN TAKE NOTE
				//the next message is for Android
				this.messageSendingThread.sendThisMessage("a1" + arena.hexForAndroid1(arena.getMapDescriptor1Binary()));
				Thread.sleep(GlobalVariables.outsleep);
				this.messageSendingThread.sendThisMessage("a2" + arena.hexForAndroid2(arena.getMapDescriptor2Binary()));
				Thread.sleep(GlobalVariables.outsleep);
				this.messageSendingThread.sendThisMessage("ar" + navigator.getHeight() + "," + navigator.getWidth());
				Thread.sleep(GlobalVariables.outsleep);
				
				
				return "mw 10";
			} else {
				// super.navigator.showBot(arena, navigator);
				super.navigator.turnRight();
				if (GlobalVariables.simulate == 1) {
					GlobalVariables.earlyCompletion(arena, GlobalVariables.percentage);
					try {
						Thread.currentThread();
						Thread.sleep(GlobalVariables.time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				this.control.requstOutgoing();
				this.messageSendingThread.sendThisMessage("md");
				
				//YAMAN TAKE NOTE
				//the next message is for Android
				this.messageSendingThread.sendThisMessage("a1" + arena.hexForAndroid1(arena.getMapDescriptor1Binary()));
				Thread.sleep(GlobalVariables.outsleep);
				this.messageSendingThread.sendThisMessage("a2" + arena.hexForAndroid2(arena.getMapDescriptor2Binary()));
				Thread.sleep(GlobalVariables.outsleep);
				this.messageSendingThread.sendThisMessage("ar" + navigator.getHeight() + "," + navigator.getWidth());
				Thread.sleep(GlobalVariables.outsleep);
				
				
				return "md";
			}
		}
	}
}
