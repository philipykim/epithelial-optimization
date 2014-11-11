import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;


public class SimulationFrame extends JFrame {
	
	World world;
	JButton step;
	
	public SimulationFrame(World world) {
		super("Cell Simulation");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.world = world;
		setupLayout();
		pack();
	}
	
	void setupLayout() {
		// Create Elements
		final JPanel visualization = new Visualization(world);
		
		step = new JButton("Step");
		step.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				world.step();
				visualization.update(visualization.getGraphics());
			}
		});
		
		// Setup Layout
		
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		
		Container layout = new Container();
		layout.setLayout(new BoxLayout(layout, BoxLayout.Y_AXIS));
		panel.add(layout);
		
			layout.add(visualization);
		
			Container bottomRow = new Container();
			bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.X_AXIS));
			layout.add(bottomRow);
				
				bottomRow.add(createContainer(step));
	}

	public static Container createContainer(Container field) {
		Container container = new Container();
		container.setLayout(new FlowLayout());
		container.add(field);
		container.setMaximumSize(new Dimension(field.getPreferredSize().width + 6, field.getPreferredSize().height + 6));
		return container;
	}

}
