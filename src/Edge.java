import java.awt.Graphics2D;


public class Edge {
	
//	private static int count;
	
	double tension = 1;
	Vertex start, end;
	int id;
	
	public Edge(Vertex start, Vertex end) {
		if (start.equals(end))
			throw new IllegalArgumentException();
		this.start = start;
		this.end = end;
//		id = count++;
	}
	
	public boolean equals(Edge other) {
		return (start.equals(other.start) && end.equals(other.end)) || (start.equals(other.end) && end.equals(other.start));
	}
	
	public void drawTo(Graphics2D gfx) {
		gfx.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
	}
	
	public double getEnergy() {
		return tension * start.distance(end);
	}
	
	public double getEnergy(double[] v) {
		double dist = Math.hypot(v[2 * start.id] - v[2 * end.id], v[2 * start.id + 1] - v[2 * end.id + 1]);
		return tension * dist;
	}

}
