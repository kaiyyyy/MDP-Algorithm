import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class AlgoSimulator {

	public static final Color UNEXPLORED = Color.WHITE;
	public static final Color UNKNOWN_BLOCK = Color.BLACK;
	public static final Color KNOWN_BLOCK = Color.RED;
	public static final Color WALL = Color.GRAY;
	public static final Color PATH = new Color(255, 123, 123);
	public static final Color BORDER = Color.BLACK;
	public static final Color STARTING = Color.GREEN;
	public static final Color END = Color.CYAN;
	public static final Color BOTHEADCENTER = new Color(123, 255, 123);
	public static final Color BOTBODY = Color.ORANGE;
	private static ActionManager explore;
	static Navigator navi = null;

	public static Timer time;
	static Thread tExplore;
	// static Thread retExplore;
	static JFrame frame;
	static boolean startRun = false;
	public static String fastestPath = "";

	static String path = "";

	// function to map Mapping to Arena

	public static void main(String[] args) {

		// GUI items
		final JButton preloadBlocks;
		final JButton start;
		final JButton stopAll = new JButton("stop");
		final JButton shortestPathButton = new JButton("Pathing");
		final JToggleButton addBlock;
		final JButton removeBlocks;
		final JButton MD1;
		final JButton MD2;

		Gridding newGrid;
		frame = new JFrame();
		frame.setTitle("Maze Explorer Simulator");
		frame.setSize(950, 950);

		// TODO: Communicator here

		Container content = frame.getContentPane();

		JPanel mapP = new JPanel();
		mapP.setPreferredSize(new Dimension(600, 700));

		final Arena map = new Arena();
		map.setName("map");
		map.setPreferredSize(new Dimension(600, 700));

		final Arena rTimeMap = new Arena(); // initialize real time map
		rTimeMap.setName("maze2");
		rTimeMap.setBorder(new EmptyBorder(15, 20, 0, 20));
		rTimeMap.setPreferredSize(new Dimension(525, 700));
		rTimeMap.setVisible(false);

		JPanel buttonPanel = new JPanel(new GridBagLayout()); // initialize
																// panel for all
																// buttons
		buttonPanel.setBorder(new EmptyBorder(0, 0, 0, 20));

		GridBagConstraints c = new GridBagConstraints();

		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10, 0, 0, 0);
		c.ipady = 8;
		c.ipadx = 10;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 0;

		JPanel timerP = new JPanel(new GridBagLayout()); // initialize panel for
															// all buttons
		GridBagConstraints timerPC = new GridBagConstraints();
		timerPC.ipady = 10;
		timerPC.ipadx = 20;
		timerPC.fill = GridBagConstraints.HORIZONTAL;
		timerPC.gridx = 10;
		timerPC.gridy = 0;
		timerPC.gridwidth = 2;

		final JLabel timerL = new JLabel("06:00", JLabel.CENTER);
		timerL.setFont(timerL.getFont().deriveFont(50.0f));
		timerL.setBackground(Color.ORANGE);
		timerL.setOpaque(true);

		final JLabel timerL2 = new JLabel("06:00", JLabel.CENTER);
		timerL2.setFont(timerL2.getFont().deriveFont(50.0f));
		timerL2.setBackground(Color.ORANGE);
		timerL2.setOpaque(true);
		timerL2.setVisible(false);
		timerP.add(timerL, timerPC);
		timerP.add(timerL2, timerPC);

		buttonPanel.add(timerP, c);

		// Navigator Navi = new Navigator(map);

		c.gridx = 0;
		c.gridy = 1;
		addBlock = new JToggleButton("Add Blocks");
		startRun = false;
		final ChangeListener addObsListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				ButtonModel buttonModel = addBlock.getModel();
				boolean selected = buttonModel.isEnabled();
				if (selected) {
					for (int i = 19; i >= 0; i--) {
						for (int j = 0; j < 15; j++) {
							final int i2 = i;
							final int j2 = j;
							if (map.grids[i][j].getBackground() == UNEXPLORED
									|| map.grids[i][j].getBackground().equals(KNOWN_BLOCK)
									|| map.grids[i][j].getBackground() == UNKNOWN_BLOCK) {
								map.grids[i][j].addMouseListener(new MouseAdapter() {
									@Override
									public void mousePressed(MouseEvent e) {
										if (map.grids[i2][j2].getBackground() == UNKNOWN_BLOCK && !startRun) {
											map.grids[i2][j2].setBackground(UNEXPLORED);
											map.blocks[i2][j2] = BlockState.UNEXPLORED;

										} else if (map.grids[i2][j2].getBackground() != UNKNOWN_BLOCK && !startRun) {
											map.grids[i2][j2].setBackground(UNKNOWN_BLOCK);
											map.blocks[i2][j2] = BlockState.BLOCKED;
										}
									}
								});
							}
						}
					}
				}
			}
		};

		addBlock.addChangeListener(addObsListener);
		buttonPanel.add(addBlock, c);

		c.gridx = 0;
		c.gridy = 3;
		removeBlocks = new JButton("Remove Blocks");
		removeBlocks.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				for (int i = 19; i >= 0; i--) {
					for (int j = 0; j < 15; j++) {
						map.grids[i][j].setBackground(UNEXPLORED);
						map.blocks[i][j] = BlockState.UNEXPLORED;
						map.grids[i][j].setBorder(BorderFactory.createLineBorder(BORDER, 1));
						map.grids[i][j].clearLabels();
					}
				}

				map.startEndPoint();
			}
		});

		buttonPanel.add(removeBlocks, c);

		c.gridx = 0;
		c.gridy = 4;
		preloadBlocks = new JButton("Preload Blocks");
		final JFileChooser f = new JFileChooser();
		preloadBlocks.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				try {
					int retVal = f.showOpenDialog(frame);
					if (retVal == JFileChooser.APPROVE_OPTION) {

						// reset map
						for (int i = 19; i >= 0; i--) {
							for (int j = 0; j < 15; j++) {
								map.grids[i][j].setBackground(UNEXPLORED);
								map.blocks[i][j] = BlockState.UNEXPLORED;
								map.grids[i][j].setBorder(BorderFactory.createLineBorder(BORDER, 1));
								map.grids[i][j].clearLabels();
							}
						}

						map.startEndPoint();

						// get select file and load obstacles in
						File selectedFile = f.getSelectedFile();
						FileInputStream fi = new FileInputStream(selectedFile);
						FileReader fr = new FileReader(selectedFile);
						BufferedReader bufferedReader = new BufferedReader(fr);
						String text = "";
						String line = "";
						while ((line = bufferedReader.readLine()) != null) {
							text = text + line;
						}
						int count = 0;
						String textA[] = text.split("");
						for (int i = 19; i >= 0; i--) {
							for (int j = 0; j < 15; j++) {
								if (Integer.parseInt(textA[count]) == 2) {
									map.grids[i][j].setBackground(UNKNOWN_BLOCK);
									map.blocks[i][j] = BlockState.BLOCKED;
								}
								count++;
							}

						}

						fr.close();
					}
				} catch (Exception f) {
					f.printStackTrace();
				}
			}
		});
		buttonPanel.add(preloadBlocks, c);

		c.gridx = 0;
		c.gridy = 3;
		c.insets = new Insets(20, 0, 0, 0);
		c.gridwidth = 0;
		c.ipady = 3;
		JLabel speedLabel = new JLabel("Steps Per Second: ");
		buttonPanel.add(speedLabel, c);

		c.gridx = 1;
		c.gridy = 5;
		final JTextField steps = new JTextField("1", 2);
		buttonPanel.add(steps, c);

		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridwidth = 1;
		JLabel percentObstaclesLabel = new JLabel("Percentage of maze to explore: ");
		buttonPanel.add(percentObstaclesLabel, c);

		c.gridx = 1;
		c.gridy = 6;
		final JTextField percentObstacles = new JTextField("100", 3);
		buttonPanel.add(percentObstacles, c);

		c.gridx = 0;
		c.gridy = 7;
		c.gridwidth = 1;
		JLabel duration = new JLabel("Duration :");
		buttonPanel.add(duration, c);

		c.gridx = 1;
		c.gridy = 7;
		final JTextField timeField = new JTextField("06:00", 5);
		buttonPanel.add(timeField, c);

		c.gridx = 0;
		c.gridy = 9;
		c.ipady = 30;
		c.insets = new Insets(5, 0, 0, 0);
		start = new JButton("START");

		final MouseAdapter exploreListener = new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				startRun = true;

				timerL.setText(timeField.getText());
				time = new Timer(1000, new ActionListener() {

					private long time = (Integer.parseInt(timeField.getText().split(":")[0]) * 60000)
							+ (Integer.parseInt(timeField.getText().split(":")[1]) * 1000) - 1000;
					private String sPadding, mPadding;

					public void actionPerformed(ActionEvent e) {

						if (time >= 0 && tExplore.isAlive()) {
							long s = ((time / 1000) % 60);
							long m = (((time / 1000) / 60) % 60);

							if (s < 10)
								sPadding = "0";
							else
								sPadding = "";
							if (m < 10)
								mPadding = "0";
							else
								mPadding = "";

							timerL.setText(mPadding + m + ":" + sPadding + s);
							time -= 1000;

						} else {
							tExplore.stop();
							shortestPathButton.setEnabled(true);
						}
					}
				});
				time.start();

				// disable other buttons
				addBlock.setSelected(false);
				addBlock.setEnabled(false);
				addBlock.removeChangeListener(addObsListener);
				start.setEnabled(false);
				start.removeMouseListener(this);
				removeBlocks.setEnabled(false);
				preloadBlocks.setEnabled(false);
				percentObstacles.setEnabled(false);
				steps.setEnabled(false);
				stopAll.setEnabled(true);
				timeField.setEnabled(false);
				GlobalVariables.simulate = 1;
				GlobalVariables.percentage = Double.parseDouble(percentObstacles.getText()) / 100;
				GlobalVariables.time = (1000 / Integer.parseInt(steps.getText()));
				navi = new Navigator();
				ActionManager Am = null;
				try {
					Am = new ActionManager(map, navi);
				} catch (NumberFormatException | InterruptedException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				explore = Am;
				tExplore = new Thread(explore);
				tExplore.start();

			}
		};

		start.addMouseListener(exploreListener);
		buttonPanel.add(start, c);

		c.gridx = 1;
		c.gridy = 9;
		c.gridx = 0;
		c.gridy = 10;
		c.ipady = 5;
		c.insets = new Insets(0, 0, 0, 0);
		c.gridwidth = 2;

		stopAll.setEnabled(false);
		stopAll.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				startRun = false;
				ButtonModel terminateButtonModel = stopAll.getModel();
				boolean selected = terminateButtonModel.isEnabled();

				if (selected) {
					time.stop();
					timerL.setText(timeField.getText());

					addBlock.setEnabled(true);
					removeBlocks.setEnabled(true);
					preloadBlocks.setEnabled(true);
					percentObstacles.setEnabled(true);
					steps.setEnabled(true);
					stopAll.setEnabled(false);
					shortestPathButton.setEnabled(false);
					timeField.setEnabled(true);
					start.setEnabled(true);
					start.addMouseListener(exploreListener);

					for (int i = 19; i >= 0; i--) {
						for (int j = 0; j < 15; j++) {
							map.map[i][j] = BlockState.UNEXPLORED;
							if (map.grids[i][j].getBackground() == UNEXPLORED
									|| map.grids[i][j].getBackground() == Color.MAGENTA
									|| map.grids[i][j].getBackground().equals(PATH)
									|| map.grids[i][j].getBackground().equals(BOTBODY)
									|| map.grids[i][j].getBackground().equals(BOTHEADCENTER)) {
								map.grids[i][j].setBackground(UNEXPLORED);
								map.grids[i][j].setBorder(BorderFactory.createLineBorder(BORDER, 1));
							} else if (map.grids[i][j].getBackground().equals(KNOWN_BLOCK)) {
								map.grids[i][j].setBackground(UNKNOWN_BLOCK);
							}
							map.grids[i][j].clearLabels();
						}
					}

					map.map[0][0] = BlockState.REACHABLE;
					map.map[0][1] = BlockState.REACHABLE;
					map.map[0][2] = BlockState.REACHABLE;
					map.map[1][0] = BlockState.REACHABLE;
					map.map[1][1] = BlockState.REACHABLE;
					map.map[1][2] = BlockState.REACHABLE;
					map.map[2][0] = BlockState.REACHABLE;
					map.map[2][1] = BlockState.REACHABLE;
					map.map[2][2] = BlockState.REACHABLE;

					map.map[19][14] = BlockState.REACHABLE;
					map.map[19][13] = BlockState.REACHABLE;
					map.map[19][12] = BlockState.REACHABLE;
					map.map[18][14] = BlockState.REACHABLE;
					map.map[18][13] = BlockState.REACHABLE;
					map.map[18][12] = BlockState.REACHABLE;
					map.map[17][14] = BlockState.REACHABLE;
					map.map[17][13] = BlockState.REACHABLE;
					map.map[17][12] = BlockState.REACHABLE;

					map.startEndPoint();
					addBlock.addChangeListener(addObsListener);
					tExplore.stop();
				}
			}
		});
		buttonPanel.add(stopAll, c);

		c.gridx = 0;
		c.gridy = 11;
		c.ipady = 35;
		c.insets = new Insets(20, 0, 0, 0);
		shortestPathButton.setEnabled(false);
		shortestPathButton.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				tExplore.stop();
				System.out.println(navi.getHeight() + " " + navi.getWidth());
				GlobalVariables.showMappingofPath = map;
				GlobalVariables.shortestrun = 1;
				ActionManager Sp = null;
				try {
					Sp = new ActionManager(map, navi);
				} catch (NumberFormatException | InterruptedException | IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					Sp.solveMaze();
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NumberFormatException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				GlobalVariables.shortestrun = 0;
			}
		});
		buttonPanel.add(shortestPathButton, c);

		c.gridx = 0;
		c.gridy = 16;
		c.ipady = 40;
		MD1 = new JButton("Description 1");
		MD1.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				GlobalVariables.MD1String = map.getMapDescriptor1Hex(map.getMapDescriptor1Binary());
				System.out.println("Map Descriptor 1 in Hex : " + GlobalVariables.MD1String);
			}
		});
		buttonPanel.add(MD1, c);

		c.gridx = 0;
		c.gridy = 20;
		c.ipady = 44;
		MD2 = new JButton("Description 2");
		MD2.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				GlobalVariables.MD2String = map.getMapDescriptor2Hex(map.getMapDescriptor2Binary());
				System.out.println("Map Descriptor 2 in Hex : " + GlobalVariables.MD2String);
			}
		});
		buttonPanel.add(MD2, c);

		JPanel legendPanel = new JPanel(new GridBagLayout());
		GridBagConstraints legendC = new GridBagConstraints();
		legendC.fill = GridBagConstraints.HORIZONTAL;
		legendC.gridx = 0;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 30, 0, 0);
		newGrid = new Gridding(10, 10, "");
		newGrid.setBackground(END);
		legendPanel.add(newGrid, legendC);

		legendC.gridx = 1;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 0, 0, 0);
		JLabel startlabel = new JLabel("End");
		legendPanel.add(startlabel, legendC);

		legendC.gridx = 2;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 32, 0, 0);
		newGrid = new Gridding(10, 10, "");
		newGrid.setPreferredSize(new Dimension(12, 12));
		newGrid.setBackground(STARTING);
		legendPanel.add(newGrid, legendC);

		legendC.gridx = 3;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 2, 0, 0);
		JLabel exploreStart = new JLabel("Start");
		legendPanel.add(exploreStart, legendC);

		legendC.gridx = 4;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 32, 0, 0);
		newGrid = new Gridding(10, 10, "");
		newGrid.setBorder(BorderFactory.createLineBorder(BORDER, 1));
		newGrid.setBackground(UNKNOWN_BLOCK);
		legendPanel.add(newGrid, legendC);

		legendC.gridx = 5;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 0, 0, 0);
		JLabel unconfirmObs = new JLabel("Unconfirmed Block");
		legendPanel.add(unconfirmObs, legendC);

		legendC.gridx = 6;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 32, 0, 0);
		newGrid = new Gridding(10, 10, "");
		newGrid.setBackground(KNOWN_BLOCK);
		legendPanel.add(newGrid, legendC);

		legendC.gridx = 7;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 0, 0, 0);
		JLabel confirmObs = new JLabel("Confirmed Block");
		legendPanel.add(confirmObs, legendC);

		legendC.gridx = 8;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 32, 0, 0);
		newGrid = new Gridding(10, 10, "");
		newGrid.setBackground(PATH);
		legendPanel.add(newGrid, legendC);

		legendC.gridx = 9;
		legendC.gridy = 0;
		legendC.insets = new Insets(5, 0, 0, 0);
		JLabel explored = new JLabel("Explored Area");
		legendPanel.add(explored, legendC);


		mapP.add(legendPanel);
		mapP.add(map);

		content.add(mapP, BorderLayout.WEST);
		content.add(buttonPanel, BorderLayout.EAST);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

}
