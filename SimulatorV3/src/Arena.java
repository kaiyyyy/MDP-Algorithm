import java.awt.Color;
import java.awt.GridLayout;
import java.math.BigInteger;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/*
 * Arena keeps the state of each blocks
 * For simulation purpose, a copy of real map is kept
 */
public class Arena extends JPanel {
	public static final Color UNEXPLORED = Color.WHITE;
	public static final Color UNKNOWN_BLOCK = Color.BLACK;
	public static final Color KNOWN_BLOCK = Color.RED;
	public static final Color WALL = Color.GRAY;
	public static final Color PATH = new Color(255, 123, 123);
	public static final Color BORDER = Color.BLACK;
	public static final Color START = Color.GREEN;
	public static final Color END = Color.CYAN;
	private static final int HEIGHT = 20;
	private static final int WIDTH = 15;
	public static final Color BOTHEADCENTER = new Color(123, 255, 123);
	public static final Color BOTBODY = Color.ORANGE;
	public boolean md1 = false;
	public boolean md2 = false;
	public boolean md3 = true;

	Gridding[][] grids = new Gridding[HEIGHT][WIDTH];

	public int blocks[][];
	public int map[][];

	/*
	 * Adjustment constants identifying the adjustment for coordinates to find
	 * the 12 blocks next to the navigator body
	 */

	private static int[] heightAdjustment = { 2, 2, 2, 1, 0, -1, -2, -2, -2, -1, 0, 1 };
	private static int[] widthAdjustment = { -1, 0, 1, 2, 2, 2, 1, 0, -1, -2, -2, -2 };

	private static int[] level2HeightAdjustment = { 3, 3, 3, 1, 0, -1, -3, -3, -3, -1, 0, 1 };
	private static int[] level2WidthAdjustment = { -1, 0, 1, 3, 3, 3, 1, 0, -1, -3, -3, -3 };

	private static int[] level3HeightAdjustment = { 4, 4, 4, 1, 0, -1, -4, -4, -4, -1, 0, 1 };
	private static int[] level3WidthAdjustment = { -1, 0, 1, 4, 4, 4, 1, 0, -1, -4, -4, -4 };

	/*
	 * Initiate the arena
	 */
	public Arena() {
		this.blocks = new int[20][15];
		this.map = new int[20][15];
		setLayout(new GridLayout(HEIGHT, WIDTH));
		for (int i = HEIGHT - 1; i >= 0; i--) {
			for (int j = 0; j < WIDTH; j++) {
				Gridding newGrid = new Gridding(i, j, "0");
				newGrid.setBorder(BorderFactory.createLineBorder(BORDER, 1));
				grids[i][j] = newGrid;
				add(newGrid);

			}
		}
		startEndPoint();

		// mark all blocks as unexplored
		for (int height = 0; height < 20; height++) {
			for (int width = 0; width < 15; width++) {
				map[height][width] = BlockState.UNEXPLORED;
			}
		}

		// mark the blocks in the starting zone and ending zone as reachable
		map[0][0] = BlockState.REACHABLE;
		map[0][1] = BlockState.REACHABLE;
		map[0][2] = BlockState.REACHABLE;
		map[1][0] = BlockState.REACHABLE;
		map[1][1] = BlockState.REACHABLE;
		map[1][2] = BlockState.REACHABLE;
		map[2][0] = BlockState.REACHABLE;
		map[2][1] = BlockState.REACHABLE;
		map[2][2] = BlockState.REACHABLE;

		map[19][14] = BlockState.REACHABLE;
		map[19][13] = BlockState.REACHABLE;
		map[19][12] = BlockState.REACHABLE;
		map[18][14] = BlockState.REACHABLE;
		map[18][13] = BlockState.REACHABLE;
		map[18][12] = BlockState.REACHABLE;
		map[17][14] = BlockState.REACHABLE;
		map[17][13] = BlockState.REACHABLE;
		map[17][12] = BlockState.REACHABLE;

		// mannually inputed arena for testing purpose
		int[][] s_arena = { // array
				// 0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 0
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 1
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 2
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 3
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 4
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 5
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 6
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 7
				{ 0, 0, 0, 0, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0 }, // row 8
				{ 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0 }, // row 9
				{ 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0 }, // row 10
				{ 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0 }, // row 11
				{ 0, 0, 0, 0, 2, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0 }, // row 12
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 13
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 14
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 15
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 16
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 17
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 }, // row 18
				{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 } // row 29
		};

		// this.blocks = s_arena;
	}

	public void setMD(int which, boolean onOff) {
		switch (which) {
		case 1:
			md1 = onOff;
			break;
		case 2:
			md2 = onOff;
			break;
		case 3:
			md3 = onOff;
			break;
		}
	}

	public String toHex(String bin) {
		String hexNum = "";
		try {
			BigInteger b = new BigInteger(bin, 2);
			hexNum = b.toString(16);

			int binaryLength = bin.length() * 2;
			int hexLength = hexNum.length() * 8;
			if (binaryLength - hexLength > 0) {
				for (int i = 0; i < ((binaryLength - hexLength) / 8); i++) {
					hexNum = "0" + hexNum;
				}
			}
		} catch (Exception e) {
			System.out.println("string is empty");
		}
		return hexNum;
	}

	public String toBinary(String hex) {
		return new BigInteger("1" + hex, 16).toString(2).substring(1);
	}

	public String[] toStringArr(String bin) {
		return bin.split("");
	}

	public void startEndPoint() {
		for (int vert = 0; vert <= 2; vert++) {
			for (int hort = 0; hort <= 2; hort++) {
				changeState(vert, hort, "S", START);
			}
		}
		for (int vert = 17; vert <= 19; vert++) {
			for (int hort = 12; hort <= 14; hort++) {
				changeState(vert, hort, "E", END);
			}
		}
	}

	public void changeState(int x, int y, String text, Color color) {
		this.grids[x][y].setBackground(color);
		this.grids[x][y].setBorder(BorderFactory.createLineBorder(color, 1));
		this.grids[x][y].setLabel(text);
		if (color == PATH) {
			this.grids[x][y].setBorder(BorderFactory.createLineBorder(BORDER, 1));
		}

	}

	public void printMap(int height, int width) {
	
			updateArena(height, width);
	
			for (int row = 19; row >= 0; row--) 
				for (int col = 0; col < 15; col++) {
					if ((col <= width + 1) && (col >= width - 1) && (row >= height - 1) && (row <= height + 1)) {
						System.out.print("n|");
					} else {
						switch (map[row][col]) {
						case BlockState.BLOCKED:
							System.out.print("b ");
							break;
						case BlockState.REACHABLE:
							System.out.print("r ");
							break;
						case BlockState.UNEXPLORED:
							System.out.print("? ");
							break;
						}
					}
				}
				System.out.println();
			
		
	}

	/*
	 * Two functions that checks whether a pair of coordinates is: 1. within the
	 * valid range of the arena 2. within the range reachable by the center of
	 * navigator
	 */
	public boolean withinRange(int height, int width) {
		if ((height >= 0) && (height <= 19) && (width >= 0) && (width <= 14)) {
			return true;
		}
		return false;
	}

	public boolean withinCenterRange(int height, int width) {
		if ((height > 0) && (height < 19) && (width > 0) && (width < 14)) {
			return true;
		}
		return false;
	}

	/*
	 * Check whether the center of navigator can move to a block based on
	 * current knowledge of the map
	 */
	public boolean isMovable(int height, int width) {
		// If the block is not in the range of reachable by center, cannot reach
		if (!this.withinCenterRange(height, width))
			return false;
		// If any of the blocks with in the 3*3 block is blocked or unexplored,
		// cannot move
		for (int height_inc = -1; height_inc <= 1; height_inc++) {
			for (int width_inc = -1; width_inc <= 1; width_inc++) {
				if (this.getState(height + height_inc, width + width_inc) != BlockState.REACHABLE)
					return false;
			}
		}
		// Otherwise, the block can be reached
		return true;
	}

	/*
	 * Check whether there is an unexplored block next to the navigator when its
	 * center is at the given position
	 */
	public boolean unexploredInDetection(int height, int width) {
		for (int index = 0; index < 12; index++) {
			if (this.withinRange(height + Arena.heightAdjustment[index], width + this.widthAdjustment[index])) {
				if (this.getState(height + Arena.heightAdjustment[index],
						width + Arena.widthAdjustment[index]) == BlockState.UNEXPLORED) {
					return true;
				}
				if (this.getState(height + Arena.heightAdjustment[index],
						width + Arena.widthAdjustment[index]) == BlockState.REACHABLE) {
					if (this.withinRange(height + Arena.level2HeightAdjustment[index],
							width + Arena.level2WidthAdjustment[index])) {
						if (this.getState(height + Arena.level2HeightAdjustment[index],
								width + Arena.level2WidthAdjustment[index]) == BlockState.UNEXPLORED)
							return true;
						else {
							if ((this.getState(height + Arena.level2HeightAdjustment[index],
									width + Arena.level2WidthAdjustment[index]) == BlockState.REACHABLE)
									&& this.withinRange(height + Arena.level2HeightAdjustment[index],
											width + Arena.level3WidthAdjustment[index])
									&& (this.getState(height + Arena.level2HeightAdjustment[index],
											width + Arena.level3WidthAdjustment[index]) == BlockState.UNEXPLORED))
								return true;
						}
					}
				}

			}
		}
		return this.longRangeInDetection(height, width);
	}

	private boolean longRangeInDetection(int height, int width) {
		// NORTH
		for (int height_inc = 2; height_inc < 7; height_inc++) {
			if (this.withinRange(height + height_inc, width)
					&& this.getState(height + height_inc, width) != BlockState.BLOCKED) {
				if (this.getState(height + height_inc, width) == BlockState.UNEXPLORED) {
					return true;
				}
			} else {
				break;
			}
		}
		// EAST
		for (int width_inc = 2; width_inc < 7; width_inc++) {
			if (this.withinRange(height, width + width_inc)
					&& this.getState(height, width + width_inc) != BlockState.BLOCKED) {
				if (this.getState(height, width + width_inc) == BlockState.UNEXPLORED) {
					return true;
				}
			} else {
				break;
			}
		}

		// SOUTH
		for (int height_inc = 2; height_inc < 7; height_inc++) {
			if (this.withinRange(height - height_inc, width)
					&& this.getState(height - height_inc, width) != BlockState.BLOCKED) {
				if (this.getState(height - height_inc, width) == BlockState.UNEXPLORED) {
					return true;
				}
			} else {
				break;
			}
		}

		// WEST
		for (int width_inc = 2; width_inc < 7; width_inc++) {
			if (this.withinRange(height, width - width_inc)
					&& this.getState(height, width - width_inc) != BlockState.BLOCKED) {
				if (this.getState(height, width - width_inc) == BlockState.UNEXPLORED) {
					return true;
				}
			} else {
				break;
			}
		}

		return false;
	}

	/*
	 * Check whether the navigator can move to each direction from the given
	 * position
	 */
	public boolean isMovableNorth(int height, int width) {
		if (!this.withinCenterRange(height + 1, width))
			return false;
		return isMovable(height + 1, width);
	}

	public boolean isMovableSouth(int height, int width) {
		if (!this.withinCenterRange(height - 1, width))
			return false;
		return isMovable(height - 1, width);
	}

	public boolean isMovableWest(int height, int width) {
		if (!this.withinCenterRange(height, width - 1))
			return false;
		return isMovable(height, width - 1);
	}

	public boolean isMovableEast(int height, int width) {
		if (!this.withinCenterRange(height, width + 1))
			return false;
		return isMovable(height, width + 1);
	}

	/*
	 * Get and set the current state of a block in the map produced by
	 * navigation
	 */

	public int getState(int height, int width) {
		return map[height][width];
	}

	/*
	 * Sensor get the state of blocks, implemented for simulation
	 */
	public int sensorGetState(int height, int width) {
		return blocks[height][width];
	}

	public void markState(int height, int width, int state) {
		this.map[height][width] = state;
	}

	// update when a block is newly marked as BLOCKED

	// update every time when bot moves
	public void updateArena(int height, int width) {
		for (int i = 19; i >= 0; i--) {
			for (int j = 0; j < 15; j++) {

				if (this.grids[i][j].getBackground() == UNKNOWN_BLOCK && this.map[i][j] != BlockState.BLOCKED
						&& this.grids[i][j].getBackground() != BOTBODY) {
					continue;
				} else {
					switch (map[i][j]) {
					case BlockState.BLOCKED:
						// System.out.print("b ");
						grids[i][j].setBackground(KNOWN_BLOCK);
						break;
					case BlockState.REACHABLE:
						grids[i][j].setBackground(PATH);
						// System.out.print("r ");
						break;
					case BlockState.UNEXPLORED:
						grids[i][j].setBackground(UNEXPLORED);
						// System.out.print("? ");
						break;

					}
				}

				if ((j <= width + 1) && (j >= width - 1) && (i >= height - 1) && (i <= height + 1)) {
					grids[i][j].setBackground(BOTBODY);
				}
			}

		}

	}

	public int[][] getMapDescriptor1Binary() {
		int[][] D1Bin = new int[20][15];
		for (int i = 19; i >= 0; i--) {
			for (int j = 0; j < 15; j++) {
				switch (this.map[i][j]) {

				case BlockState.BLOCKED:
				case BlockState.REACHABLE:
					D1Bin[i][j] = 1;
					break;
				case BlockState.UNEXPLORED:
				case BlockState.UNREACHABLE:
					D1Bin[i][j] = 0;
				}
				System.out.print(D1Bin[i][j]);

			}

			System.out.println();

		}
		return D1Bin;

	}

	public String getMapDescriptor1Hex(int[][] D1Bin) {
		String S = "11";
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 15; j++) {
				S = S + String.valueOf(D1Bin[i][j]);
			}

		}
		S = S + "11";
		S = toHex(S);
		return S;
	}

	public int[][] getMapDescriptor2Binary() {
		int[][] D2Bin = new int[20][15];
		for (int i = 19; i >= 0; i--) {
			for (int j = 0; j < 15; j++) {
				switch (this.map[i][j]) {

				case BlockState.BLOCKED:
					D2Bin[i][j] = 1;
					break;
				case BlockState.REACHABLE:
					D2Bin[i][j] = 0;
					break;
				case BlockState.UNEXPLORED:
					D2Bin[i][j] = 2;
					break;
				}
				System.out.print(D2Bin[i][j]);

			}

			System.out.println();

		}
		return D2Bin;

	}

	public String getMapDescriptor2Hex(int[][] D2Bin) {
		String S = "";
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 15; j++) {
				if (D2Bin[i][j] != 2) {
					S = S + String.valueOf(D2Bin[i][j]);
				}
			}

		}

		while (S.length() % 8 != 0) {
			S = S + "0";
		}
		S = toHex(S);
		return S;
	}

	public String hexForAndroid1(int[][] D1Bin) {
		String S = "";
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 15; j++) {
				S = S + String.valueOf(D1Bin[i][j]);
			}

		}
		S = toHex(S);
		return S;
	}

	public String hexForAndroid2(int[][] D2Bin) {
		String S = "";
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 15; j++) {
				if (D2Bin[i][j] != 2) {
					S = S + String.valueOf(D2Bin[i][j]);
				}
			}

		}
		S = toHex(S);
		return S;
	}

}
