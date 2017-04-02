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
		int height = super.navigator.getHeight();
		int width = super.navigator.getWidth();
		int direction = super.navigator.getCurDirection();

		/*
		 * Request for reading input before update. Current thread running
		 * action selection process will be blocked until readings from sensors
		 * come in
		 */
		System.out.println("requesting incoming.");
		try {
			control.requestIncoming();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// update the map
		super.readAndUpdate();

		// try align itself

		if (super.tryAlignmentFront()) {
			control.requstOutgoing();
			this.messageSendingThread.sendThisMessage("ml");
			this.control.requestIncoming();
		}

		/*
		 * boolean test = super.readAndOverwrite();
		 * 
		 * 
		 * //if apply, turn left and overwrite
		 * 
		 * if (test) { if (super.testLeftBottom()) { this.navigator.turnLeft();
		 * 
		 * control.requstOutgoing();
		 * this.messageSendingThread.sendThisMessage("ma");
		 * this.control.requestIncoming();
		 * 
		 * // align control.requstOutgoing();
		 * this.messageSendingThread.sendThisMessage("ml");
		 * this.control.requestIncoming(); super.readAndOverwrite();
		 * 
		 * this.navigator.turnRight(); // turn right control.requstOutgoing();
		 * this.messageSendingThread.sendThisMessage("md");
		 * this.control.requestIncoming();
		 * 
		 * } }
		 * 
		 * 
		 * //If only got one block on the left lower position then turn left
		 * //and update
		 */

		/*
		 * if (super.tryAlignmentLeft()) { control.requstOutgoing();
		 * this.messageSendingThread.sendThisMessage("ml1");
		 * this.control.requestIncoming(); }
		 */

		/*
		 * Replace left alignment with front alignment
		 */

		if (super.tryAlignmentLeft()) {

			this.navigator.turnLeft();

			if (super.tryAlignmentFront()) {
				// turn left
				control.requstOutgoing();
				this.messageSendingThread.sendThisMessage("ma");
				this.control.requestIncoming();

				// align
				control.requstOutgoing();
				this.messageSendingThread.sendThisMessage("ml");
				this.control.requestIncoming();

				// turn right
				control.requstOutgoing();
				this.messageSendingThread.sendThisMessage("md");
				this.control.requestIncoming();
			}

			this.navigator.turnRight();

		}

		// print the and update the real time

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

			// message for android
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

			this.control.requestIncoming();
			super.readAndUpdate();

			// super.navigator.showBot(arena, navigator);
			super.navigator.move();
			this.control.requstOutgoing();
			this.messageSendingThread.sendThisMessage("mw 10");
			
			// message for android
			// the next message is for Android
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
				
				//if(S.charAt(0) == 'm'){
				System.out.println("delay :" + Long.toString(System.currentTimeMillis() - GlobalVariables.delayMeasure));
				GlobalVariables.delayMeasure = System.currentTimeMillis();
			//}

				// message for android
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

				// message for andriod
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
