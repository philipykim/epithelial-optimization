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
		}
		return Math.abs(area / 2);
	}
	
	
	
}
