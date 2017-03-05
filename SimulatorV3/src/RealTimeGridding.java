import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class RealTimeGridding {
	
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
	static JFrame frame;
	
	public RealTimeGridding(Arena map) {
	
		frame = new JFrame();
		frame.setTitle("Maze Explorer RealTime");
		frame.setSize(950, 950);
		Container content = frame.getContentPane();
		JPanel mapP = new JPanel();
		mapP.setPreferredSize(new Dimension(600, 700));
		map.setName("map");
		map.setPreferredSize(new Dimension(600, 700));
		Gridding newGrid; 
		
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
		frame.setVisible(true);
		
	}
}
