import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

public class Visualization extends JPanel {

	World world;

	public Visualization(World cellWorld) {
		world = cellWorld;
		setPreferredSize(new Dimension(600, 400));
	}

	public void paint(Graphics g) {
		super.paintComponents(g);
		Graphics2D gfx = (Graphics2D) g;
		world.drawTo(gfx);
	}

}
