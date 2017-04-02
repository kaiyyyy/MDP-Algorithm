import java.io.IOException;
import java.net.UnknownHostException;

/*
 * Action manager manage the behavior of navigator on general
 * It executes the explore and find shortest path instruction from Android interface
 */
public class ActionManager extends Thread {
	private Arena arena;
	private Navigator navigator;
	private ProgressControl control;
	private IncomingMessageThread in;
	private OutgoingMessageThread out;

	public ActionManager(Arena arena, Navigator navigator)
			throws InterruptedException, NumberFormatException, UnknownHostException, IOException {
		this.arena = arena;
		this.navigator = navigator;
		new RealTimeGridding(this.arena);
		this.control = new ProgressControl();
		Tcp_socket socket = new Tcp_socket();
		this.out = new OutgoingMessageThread(Tcp_socket.client);
		this.in = new IncomingMessageThread(Tcp_socket.client, this.control);
	}

	public void run() {
		try {
			try {

				this.control.requestProcess();
				this.control.requstOutgoing();
				this.out.sendThisMessage("start");

				navigate();

				this.control.requestProcess();

				this.solveMaze();

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void navigate() throws InterruptedException, NumberFormatException, UnknownHostException, IOException {
		FLWSelector flwselector = new FLWSelector(this.arena, this.navigator, this.control, this.in, this.out);
		// phase 1 navigation
		flwselector.setTargets();

		while (!flwselector.checkCommpletion()) {

			//
			String action = flwselector.selectActions();
			System.out.println(action);
		}
		// temporary removed
		this.phaseIINavigate();
		//
		// Message for Android
		// this.out.sendThisMessage("a1f" +
		// arena.getMapDescriptor1Hex(arena.getMapDescriptor1Binary()));
		// Thread.sleep(GlobalVariables.outsleep);
		// this.out.sendThisMessage("a2f" +
		// arena.getMapDescriptor2Hex(arena.getMapDescriptor2Binary()));
		// Thread.sleep(GlobalVariables.outsleep);

	}

	private void phaseIINavigate()
			throws InterruptedException, NumberFormatException, UnknownHostException, IOException {

		PhaseIISelector selector = new PhaseIISelector(this.arena, this.navigator, this.control, this.in, this.out);

		while (selector.findNextTarget()) {
			System.out.println(selector.selectActions());
			selector.rotateAndDetect(4);
			// Message for Android
			this.out.sendThisMessage("a1" + arena.hexForAndroid1(arena.getMapDescriptor1Binary()));
			Thread.sleep(GlobalVariables.outsleep);
			this.out.sendThisMessage("a2" + arena.hexForAndroid2(arena.getMapDescriptor2Binary()));
			Thread.sleep(GlobalVariables.outsleep);
			this.out.sendThisMessage("ar" + navigator.getHeight() + "," + navigator.getWidth());
			Thread.sleep(GlobalVariables.outsleep);
			arena.printMap(navigator.getHeight(), navigator.getWidth());
		}

		System.out.println(selector.returnToOrigin());
		arena.printMap(navigator.getHeight(), navigator.getWidth());

		// Message for Android
		this.out.sendThisMessage("a1f" + arena.getMapDescriptor1Hex(arena.getMapDescriptor1Binary()));
		Thread.sleep(150);
		this.out.sendThisMessage("a2f" + arena.getMapDescriptor2Hex(arena.getMapDescriptor2Binary()));
		Thread.sleep(150);

		if (GlobalVariables.simulate == 1) {
			Thread.currentThread().stop();
		}
	}

	public void solveMaze() throws InterruptedException, NumberFormatException, UnknownHostException, IOException {
		shortestPath();
	}

	private void shortestPath() throws NumberFormatException, UnknownHostException, InterruptedException, IOException {
		SPSelector selector = new SPSelector(this.arena, this.navigator, this.control, this.in, this.out);
		System.out.println(selector.selectActions());
	}

}
