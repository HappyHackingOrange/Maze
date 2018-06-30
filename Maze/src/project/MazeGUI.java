/**
 * MazeGUI.java
 */
package project;

import javax.swing.*;

/**
 * Set up and run a GUI to display the maze.
 * 
 * @author Vincent Stowbunenko
 *
 */
public class MazeGUI extends JFrame implements Runnable {

	private static final long serialVersionUID = 1L;

	// Instance variables
	MazePanel mazePanel;

	// Constructor
	public MazeGUI(MazePanel mazePanel) {
		this.mazePanel = mazePanel;
	}

	// Runs the frame.
	@Override
	public void run() {
		// Sets look and feel to the system look and feel.
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		JFrame frame;

		if (mazePanel.maze.generationAlgorithm == null)
			frame = new JFrame(String.format("Generating %d x %d maze...", mazePanel.maze.n, mazePanel.maze.n,
					mazePanel.maze.solvingAlgorithm));
		else if (mazePanel.maze.generationAlgorithm == "read") 
			frame = new JFrame(String.format("Reading %d x %d maze...", mazePanel.maze.n, mazePanel.maze.n,
					mazePanel.maze.solvingAlgorithm));
		else
			frame = new JFrame(String.format("Generating %d x %d maze using %s algorithm...", mazePanel.maze.n,
						mazePanel.maze.n, mazePanel.maze.generationAlgorithm));


		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mazePanel);
		frame.pack();
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}
