import java.util.ArrayList;
import java.util.List;


public class Cell {
	
	String name;
	List<Vertex> vertices;
	double areaElasticity = 1;
	double perimeterElasticity = 1;
	double preferredArea = 1;
	
	/**
	 * defines a cell by its vertices listed in clockwise direction
	 * @param vertexList
	 */
	public Cell(Vertex[] vertexList) {
		vertices = new ArrayList<Vertex>();
		for (Vertex v : vertexList)
			vertices.add(v);
		preferredArea = getArea(); // unless we know a better prefferedArea
	}
	
	public double getArea() {
		double area = 0;
		int j = vertices.size() - 1;
		for (int i = 0; i < vertices.size(); i++) {
			area += (vertices.get(j).x + vertices.get(i).x) * (vertices.get(j).y - vertices.get(i).y);
			j = i;
		}
		return Math.abs(area / 2);
		
	}

	
	public double getArea(double[] v) {
		double area = 0;
		int j = vertices.size() - 1;
		for (int i = 0; i < vertices.size(); i++) {
			int id = vertices.get(i).id;
			int jid = vertices.get(j).id;
			area += (v[2 * jid] + v[2 * id]) * (v[2 * jid + 1] - v[2 * id + 1]);
			j = i;
		}
		return Math.abs(area / 2);
	}
	
	public double getPerimeter() {
		double perimeter = 0;
		int j = vertices.size() - 1;
		for (int i = 0; i < vertices.size(); i++) {
			perimeter += vertices.get(i).distance(vertices.get(j));
			j = i;
		}
		return perimeter;
	}
	
	public double getPerimeter(double[] v) {
		double perimeter = 0;
		int j = vertices.size() - 1;
		for (int i = 0; i < vertices.size(); i++) {
			int id = vertices.get(i).id;
			int jid = vertices.get(j).id;
			perimeter += Math.hypot(v[2 * jid] - v[2 * id], v[2 * jid + 1] - v[2 * id + 1]);
			j = i;
		}
		return perimeter;
	}
	
	public double getEnergy() {
		double dA = getArea() - preferredArea;
		double L = getPerimeter();
		return areaElasticity * dA * dA + perimeterElasticity * L * L;
	}
	
	public double getEnergy(double[] v) {
		double dA = getArea(v) - preferredArea;
		double L = getPerimeter(v);
		return areaElasticity * dA * dA + perimeterElasticity * L * L;
	}
	
}
