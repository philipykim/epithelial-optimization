public class Application {

	public static void main(String args[]) {
		World world = new World();
		populate(world);
		world.optimize();

		SimulationFrame app = new SimulationFrame(world);
		app.setVisible(true);

		while (true) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {}
			app.step.doClick();
		}
	}

	public static void populate(World world) {
		Vertex v0 = new Vertex("0", 300, 300);
		Vertex v1 = new Vertex("1", 250, 300);
		Vertex v2= new Vertex("2", 200, 300);
		Vertex v3 = new Vertex("3", 200, 200);
		Vertex v4 = new Vertex("4", 250, 200);
		Vertex v5 = new Vertex("5", 300, 200);
		Vertex v6 = new Vertex("6", 350, 200);
		Vertex v7 = new Vertex("7", 400, 300);
		Vertex v8 = new Vertex("8", 350, 300);
		Vertex v9 = new Vertex("9", 350, 350);
		Vertex v10 = new Vertex("10", 300, 350);
		Vertex v11 = new Vertex("11", 250, 350);

		Vertex[] cell1 = {v0, v1, v2, v3, v4, v5};
		world.add(new Cell(cell1));

		Vertex[] cell2 = {v0, v5, v6, v7, v8};
		world.add(new Cell(cell2));

		Vertex[] cell3 = {v0, v8, v9, v10, v11, v1};
		world.add(new Cell(cell3));
	}
}
