import java.io.IOException;
import java.net.UnknownHostException;

public class Main {

	public static void main(String[] args)
			throws InterruptedException, NumberFormatException, UnknownHostException, IOException {

		Arena arena = new Arena();
		Navigator navigator = new Navigator();
		ProgressControl control = new ProgressControl();
		ActionManager manager = new ActionManager(arena, navigator);
		manager.run();
		manager.join();

		System.out.println("All complete! Congratulations!");
	}

}
