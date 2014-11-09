import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;


public class Vertex extends Point2D.Double {
	
	String name;
	List<Vertex> connected;
	
	public Vertex(String name, double x, double y) {
		super(x, y);
		this.name = name;
		connected = new ArrayList<Vertex>();
	}
	
	public void drawTo(Graphics2D gfx) {
		gfx.fillOval((int) x - 2, (int) y - 2, 4, 4);
		gfx.drawString(name, (int) x, (int) y - 10);
	}

}
