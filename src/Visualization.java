import javax.swing.*;
import java.awt.*;

public class Visualization extends JPanel {

	World world;

	public Visualization(World cellWorld) {
		world = cellWorld;
		setPreferredSize(new Dimension(800, 600));
	}

	public void paint(Graphics g) {
		super.paintComponents(g);
		Graphics2D gfx = (Graphics2D) g;
		gfx.setBackground(Color.WHITE);
		gfx.clearRect(0, 0, getWidth(), getHeight());
		world.drawTo(gfx);
	}
}
