import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.WindowConstants;


public class Application {
	
	public static void main(String args[]) {
		World world = new World();
		populate(world);
		
		JFrame app = new SimulationFrame(world);
		app.setVisible(true);
	}
	
	public static void populate(World world) {
		Vertex a = new Vertex("A", 100, 100);
		Vertex b = new Vertex("B", 200, 100);
		Vertex c = new Vertex("C", 100, 200);
		Vertex d = new Vertex("D", 200, 200);
		Vertex[] list1 = {a, b, c};
		Cell c1 = new Cell(list1);
		world.add(c1);
		Vertex[] list2 = {b, c, d};
		Cell c2 = new Cell(list2);
		world.add(c2);
	}

}
