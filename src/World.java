import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
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
				if (!vertices.contains(v)) {
					v.id = vertices.size();
					vertices.add(v);
				}
				Edge e = new Edge(v, last);
				if (!edges.contains(e)) {
					e.id = edges.size();
					edges.add(e);
				}
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
	
	public double computeEnergyFunction() {
		double energy = 0;
		for (Cell c : cells) {
			energy += c.getEnergy();
		}
		for (Edge e : edges) {
			energy += e.getEnergy();
		}
		return energy;
	}
	
	public double computeEnergyFunctionParametrized(double[] v) {
		double energy = 0;
		for (Cell c : cells) {
			energy += c.getEnergy(v);
		}
		for (Edge e : edges) {
			energy += e.getEnergy(v);
		}
		return energy;
	}
	
	public void step() {
//		System.out.println("A = " + cells.get(1).getArea() + "; pref = " +  cells.get(1).preferredArea);
		
		int triesLeft = 1;
		double lowestEnergy = Double.MAX_VALUE;
		double[] bestConfiguration = null;
		
		double[] next = new double[vertices.size() * 2];
		while (triesLeft-- > 0) {
			double length = 0;
			for (int i = 0; i < next.length; i++) {
				next[i] = Math.random() - .5;
				length += next[i] * next[i];
			}
			for (int i = 0; i < next.length; i++) {
				next[i] /= length;
			}
			for (int j = 0; j < vertices.size(); j++) {
				next[2 * j] = next[2 * j] / length + vertices.get(j).x;
				next[2 * j + 1] = next[2 * j + 1] / length + vertices.get(j).y;
			}
			
			double energy = computeEnergyFunctionParametrized(next);
			if (energy < lowestEnergy) {
				lowestEnergy = energy;
				bestConfiguration = next.clone();
				triesLeft = 1000;
			}
		}
		
		update(bestConfiguration);
	}
	
	public void update(double[] v) {
		for (int i = 0; i < vertices.size(); i++) {
			vertices.get(i).setLocation(new Point2D.Double(v[2 * i], v[2 * i + 1]));
		}
	}

}
