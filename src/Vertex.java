import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class Vertex extends Point2D.Double {
	
//	private static int count = 0;
	
	String name;
	List<Vertex> connected;
	int id;
	
	public Vertex(String name, double x, double y) {
		super(x, y);
		this.name = name;
		connected = new ArrayList<Vertex>();
//		id = count++;
	}
	
	public void drawTo(Graphics2D gfx) {
		gfx.fillOval((int) x - 2, (int) y - 2, 4, 4);
		gfx.drawString(name, (int) x, (int) y - 10);
	}

}
