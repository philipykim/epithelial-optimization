import java.awt.Graphics2D;


public class Edge {
	
	Vertex start, end;
	
	public Edge(Vertex start, Vertex end) {
		if (start.equals(end))
			throw new IllegalArgumentException();
		this.start = start;
		this.end = end;
	}
	
	public boolean equals(Edge other) {
		return (start.equals(other.start) && end.equals(other.end)) || (start.equals(other.end) && end.equals(other.start));
	}
	
	public void drawTo(Graphics2D gfx) {
		gfx.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
	}

}
