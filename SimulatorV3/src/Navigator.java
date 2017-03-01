import java.awt.Color;

import javax.swing.BorderFactory;

import org.json.JSONException;
import org.json.JSONObject;
/*
 * Navigator class keeps the data associated with the current center position and current direction
 */
public class Navigator {

    private int D;
    private int curHeight;
    private int curWidth;
    private int topLeft_height;
    private int topLeft_width;

    public Navigator() {
    	this.D = Direction.NORTH;
    	curHeight = 1;
    	curWidth = 1;
        }


    public int getCurDirection() {
	return this.D;
    }

    public void DirectionChange(int Dir) {
	this.D = Dir;
    }

    public int getHeight() {
	return this.curHeight;
    }

    public int getWidth() {
	return this.curWidth;
    }

    public void setHeight(int h) {
	this.curHeight = h;
    }

    public void setWidth(int w) {
	this.curWidth = w;
    }

    public void move() {
		switch (D) {
		case Direction.NORTH:
		    curHeight += 1;
		    setTopLeft();
		    break;
		case Direction.SOUTH:
		    curHeight -= 1;
		    setTopLeft();
		    break;
		case Direction.EAST:
		    curWidth += 1;
		    setTopLeft();
		    break;
		case Direction.WEST:
		    curWidth -= 1;
		    setTopLeft();
		    break;
		}
		
    }

    public void turnLeft() {
	this.D = (this.D + 3) % 4;
    }

    public void turnRight() {
	this.D = (this.D + 1) % 4;
    }

    public void setTopLeft() {
		this.topLeft_height = this.curHeight - 1;
		this.topLeft_width = this.curWidth - 1;
    }

    /*public int getTopLeftHeight() {
    	return 19 - this.topLeft_height;
    }*/

    /*public int getTopLeftWidth() {
    	return 14 - this.topLeft_width;
    }*/

    public void getSensorReadings() {
    }
    
    //Imported BotClass
    public static final Color UNEXPLORED = Color.WHITE;
	public static final Color UNKNOWN_BLOCK = Color.BLACK;
	public static final Color KNOWN_BLOCK = Color.RED;
	public static final Color WALL = Color.GRAY;
	public static final Color PATH = new Color(255,123,123);
	public static final Color BORDER = Color.BLACK;
	public static final Color BOTHEADCENTER = new Color(123,255,123);
	public static final Color BOTBODY = Color.ORANGE;
	public static final Color SENSED = new Color(255,123,123);
	
	
	
	public void showBot(Arena arena, Navigator navi) 
	{
		int x = curHeight;
		int y = curWidth;
		
		
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if(arena.grids[x+i][y+j].getBackground() != UNKNOWN_BLOCK) {
					arena.grids[x+i][y+j].setBackground(BOTBODY);
					arena.grids[x+i][y+j].setBorder(BorderFactory.createLineBorder(BORDER, 1));
				}
			}
		}
		
		switch(this.D) {
			case 0: 
				if(arena.grids[x+1][y].getBackground() != UNKNOWN_BLOCK)
					arena.grids[x][y].setBackground(BOTHEADCENTER);
				break;
			case 2:
				if(arena.grids[x-1][y].getBackground() != UNKNOWN_BLOCK)
					arena.grids[x-1][y].setBackground(BOTHEADCENTER);
				break;
			case 1:
				if(arena.grids[x][y+1].getBackground() != UNKNOWN_BLOCK)
					arena.grids[x][y+1].setBackground(BOTHEADCENTER);
				break;
			case 3:
				if(arena.grids[x][y-1].getBackground() != UNKNOWN_BLOCK)
					arena.grids[x][y-1].setBackground(BOTHEADCENTER);
				break;		
		}
		

	}
	


	
}
	