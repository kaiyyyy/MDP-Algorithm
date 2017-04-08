import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Gridding extends JPanel {

	public int hori;
	public int vert;
	private String label;
	JLabel theLabel = new JLabel("", JLabel.CENTER);
	
	public Gridding(int h, int w,String label) {
		this.hori = h;
		this.vert = w;
		this.label = label;
		if(label.equals("0") == false)
		{
			theLabel.setText(label);
		}
		this.add(theLabel);
		setBackground(Color.WHITE);
		
	}
	
	public int getHori(){
		return this.hori;
	}
	
	public int getVert(){
		return this.vert;
	}
	
	public String getLabel(){
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
		theLabel.setText(label);
	}
	
	
	public void clearLabels() {
		this.label = "0";
		if(theLabel.getText().equals("S") == false && theLabel.getText().equals("E") == false)
			theLabel.setText("");
	}
	
	//Theoretically will never be used
	public void overrideGridHori(int w)
	{
		this.hori = w;
	}
	
	public void overrideGridVert(int h)
	{
		this.vert = h;
	}

}
