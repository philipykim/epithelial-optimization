import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;


public class World {
	
	List<Cell> cells;
	List<Edge> edges;
	List<Vertex> vertices;
	
	public World() {
		cells = new ArrayList<Cell>();
		edges = new ArrayList<Edge>();
		vertices = new ArrayList<Vertex>();
	}
	
	public void add(Cell c) {
		if (!cells.contains(c)) {
			cells.add(c);
			Vertex last = c.vertices.get(c.vertices.size() - 1);
			for (int i = 0; i < c.vertices.size(); i++) {
				Vertex v = c.vertices.get(i);
				if (!vertices.contains(v))
					vertices.add(v);
				Edge e = new Edge(v, last);
				if (!edges.contains(e))
					edges.add(e);
				last = v;
			}
		}
	}
	
	public void drawTo(Graphics2D gfx) {
		gfx.setColor(Color.BLACK);
		for (Vertex v: vertices) {
			v.drawTo(gfx);
		}
		for (Edge e : edges) {
			e.drawTo(gfx);
		}
	}
	
	public void step() {
		System.out.println("Step");
	}

}
