import java.awt.Color;

public class GlobalVariables {
	public static Arena showMappingofPath = null;
	public static int shortestrun = 0;
	public static int time = 0;
	public static double percentage = 0;
	public static String MD1String = "";
	public static String MD2String = "";
	public static final Color EXPLORED = new Color(255, 123, 123);
	public static int simulate = 0;
	public static int pauseExplore = 0;
	public static String sensorInput;
	public static int outsleep = 50;

	public static void earlyCompletion(Arena map, double percentage) {
		int num = 0;
		for (int i = 19; i >= 0; i--) {
			for (int j = 0; j < 15; j++) {
				if (map.grids[i][j].getBackground().equals(EXPLORED))
					num++;
			}
		}
		if (num >= ((percentage * 300) - 9) && percentage < 1.0) {
			Thread.currentThread().stop();
		}
	}

}
