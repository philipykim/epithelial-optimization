import java.awt.Graphics2D;


public class Edge {
	Vertex start, end;
	int id;
	
	public Edge(Vertex start, Vertex end) {
		if (start.equals(end))
			throw new IllegalArgumentException();
		this.start = start;
		this.end = end;
	}

	public void drawTo(Graphics2D gfx) {
		gfx.drawLine((int) start.x, (int) start.y, (int) end.x, (int) end.y);
	}

	public double getEnergy() {
		return Constants.tension * start.distance(end);
	}

	public double getEnergy(double[] v) {
		double dist = Math.hypot(v[2 * start.id] - v[2 * end.id], v[2 * start.id + 1] - v[2 * end.id + 1]);
		return Constants.tension * dist;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;

		Edge edge = (Edge) o;

		return (end.equals(edge.end) && start.equals(edge.start)) || (end.equals(edge.start) && start.equals(edge.end));
	}

	@Override
	public int hashCode() {
		int result = start.hashCode();
		result = 31 * result + end.hashCode();
		return result;
	}
}
