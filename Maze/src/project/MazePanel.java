/**
 * MazePanel.java
 */
package project;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Animates maze generation and solving procedures, and draws solution line from
 * starting to end.
 * 
 * @author Vincent Stowbunenko
 *
 */
public class MazePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	// Instance variables
	Maze2 maze;
	private int pixelSize;
	private int delay = 1;
	private ArrayList<AnimatedLine> listAdjacentRooms = new ArrayList<>();
	private ArrayList<Arrow> listSolvingProcedure = new ArrayList<>();
	private ArrayList<Rectangle> listBacktracking = new ArrayList<>();
	private ArrayList<AnimatedLine> listSolutionLines = new ArrayList<>();
	private int a, b; // constants to determine preferred size
	private int state0;
	private int state1;
	private int idx;
	private boolean instantGeneration;
	private boolean instantSolving;
	private boolean instantBacktracking;

	// Initializations
	public MazePanel(Maze2 maze) {

		instantGeneration = false;
		instantSolving = false;
		instantBacktracking = false;

		this.maze = maze;
		state0 = 0;
		state1 = 0;
		idx = 0;

		// Calculates constants a and b for determining window size, so that number
		// divides up nicely without any rounding errors.
		getPreferredSize();
		paintMaze();

		setBackground(Color.BLACK);
		timer.start();

	}

	// Make it so the panel can be displayed at its preferred size
	@Override
	public Dimension getPreferredSize() {

		// Not all screen resolutions are the same in every PC...
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		// Calculate a preferred image size

		for (b = 6; b > 0; b--) {
			int iter = 1;
			int temp = 0;
			pixelSize = 0;
			while (temp < screenSize.height) {
				pixelSize = temp;
				temp = iter++ * (b * maze.n + 1);
			}
			if ((iter & 1) == 0) {
				a = iter - 2;
				if (a > 0)
					break;
			} else {
				a = iter - 3;
				pixelSize = a * (b * maze.n + 1);
				if (a > 0)
					break;
			}
		}

		return new Dimension(pixelSize, pixelSize);
	}

	// Create timer with initial delay
	protected Timer timer = new Timer(delay, new ActionListener() {
		// Handles action event
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (state0) {
			case 0: // Maze generation
				if (instantGeneration) {
					for (idx = 0; idx < listAdjacentRooms.size(); idx++)
						listAdjacentRooms.get(idx).update();
					// Check if all rooms are finished connecting
					int counter = 0;
					for (idx = 0; idx < listAdjacentRooms.size(); idx++)
						if (listAdjacentRooms.get(idx).getDx() == 0 && listAdjacentRooms.get(idx).getDy() == 0)
							counter++;
					if (counter == listAdjacentRooms.size())
						state0 = 5;
				} else if (idx < listAdjacentRooms.size()) {
					listAdjacentRooms.get(idx).update();
					// Go on to next adjacent room after animation of opening between current pair
					// of adjacent rooms is done
					if (listAdjacentRooms.get(idx).getDx() == 0 && listAdjacentRooms.get(idx).getDy() == 0)
						idx++;
				} else {
					state0 = 5;
				}
				break;
			case 1: // Solving maze
				switch (state1) {
				case 0:
					maze.solve("breadth-first search (BFS)");
					((JFrame) SwingUtilities.getWindowAncestor(MazePanel.this))
							.setTitle(String.format("Solving using %s...", maze.solvingAlgorithm));
					paintSolution(maze.solutionPath);
					idx = 0;
					state0 = 3;
					break;
				case 1:
					maze.solve("depth-first search (DFS)");
					((JFrame) SwingUtilities.getWindowAncestor(MazePanel.this))
							.setTitle(String.format("Solving using %s...", maze.solvingAlgorithm));
					paintSolution(maze.solutionPath);
					idx = 0;
					state0 = 3;
					break;
				case 2:
					((JFrame) SwingUtilities.getWindowAncestor(MazePanel.this))
							.setTitle(String.format("%d x %d maze solved!", maze.n, maze.n));
					state0 = -1;
					break;
				}
				break;
			case 2: // Drawing solution path
				if (idx < listSolutionLines.size()) {
					listSolutionLines.get(idx).update();
					if (listSolutionLines.get(idx).getDx() == 0 && listSolutionLines.get(idx).getDy() == 0)
						idx++;
				} else {
					maze.printPath();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
					}
					state0 = 1;
				}
				break;
			case 3: // Animate solving procedure
				if (instantSolving) {
					for (idx = 0; idx < maze.solvingProcedure.length; idx++)
						paintArrows(maze.solvingProcedure, maze.directions, listSolvingProcedure, idx);
					state0 = 6;
				} else if (idx < maze.solvingProcedure.length) {
					paintArrows(maze.solvingProcedure, maze.directions, listSolvingProcedure, idx);
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
					}
					idx++;
				} else {
					state0 = 6;
				}
				break;
			case 4: // Backtrack to beginning
				if (instantBacktracking) {
					for (; idx >= 0; idx--)
						paintSteps(maze.solutionPath, listBacktracking, idx);
					state0 = 7;
				} else if (idx >= 0) {
					paintSteps(maze.solutionPath, listBacktracking, idx);
					try {
						Thread.sleep(100);
					} catch (InterruptedException ex) {
					}
					idx--;
				} else {
					state0 = 7;
				}
				break;
			case 5: // Update states and print maze
				idx = 0;
				state0 = 1;
				maze.print();
				break;
			case 6: // Transit from solving to backtracking
				((JFrame) SwingUtilities.getWindowAncestor(MazePanel.this))
						.setTitle("Backtracking to the beginning of the maze...");
				state0 = 4;
				maze.printSolvingProcedures();
				idx = maze.solutionPath.length - 1;
				break;
			case 7: // Transit from backtracking to drawing solution line
				listSolvingProcedure.clear();
				listBacktracking.clear();
				((JFrame) SwingUtilities.getWindowAncestor(MazePanel.this)).setTitle("Drawing solution path...");
				idx = 0;
				state0 = 2;
				state1++;
				maze.printBacktracking();
				break;
			default:
				break;
			}
			repaint();
		}
	});

	// Paint a maze component to pass to the panel for drawing.
	@Override
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		for (AnimatedLine line : listAdjacentRooms)
			line.paint(this, g2d);

		for (Rectangle block : listBacktracking) {
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fill(block);
		}

		for (Arrow arrow : listSolvingProcedure) {
			arrow.draw(g2d);
		}

		for (AnimatedLine line : listSolutionLines)
			line.paint(this, g2d);

	}

	// Paint a line.
	public void addLine(int x1, int y1, int x2, int y2, int lineWidth, int lineHeight, int direction, boolean overlap,
			Color color, ArrayList<AnimatedLine> list) {

		switch (direction) {
		case 0: // north
			if (overlap)
				list.add(new AnimatedLine(x2 - lineWidth / 2, y2 - lineHeight / 2, lineWidth, y1 - y2 + lineHeight, 0,
						color));
			else
				list.add(new AnimatedLine(x2 - lineWidth / 2, y2 - lineHeight / 2, lineWidth, y1 - y2, 0, color));
			break;
		case 1: // south
			if (overlap)
				list.add(new AnimatedLine(x1 - lineWidth / 2, y1 - lineHeight / 2, lineWidth, y2 - y1 + lineHeight, 1,
						color));
			else
				list.add(new AnimatedLine(x1 - lineWidth / 2, y1 + lineHeight / 2, lineWidth, y2 - y1, 1, color));
			break;
		case 2: // east
			if (overlap)
				list.add(new AnimatedLine(x1 - lineWidth / 2, y1 - lineHeight / 2, x2 - x1 + lineWidth, lineHeight, 2,
						color));
			else
				list.add(new AnimatedLine(x1 + lineWidth / 2, y1 - lineHeight / 2, x2 - x1, lineHeight, 2, color));
			break;
		case 3: // west
			if (overlap)
				list.add(new AnimatedLine(x2 - lineWidth / 2, y2 - lineHeight / 2, x1 - x2 + lineWidth, lineHeight, 3,
						color));
			else
				list.add(new AnimatedLine(x2 - lineWidth / 2, y2 - lineHeight / 2, x1 - x2, lineHeight, 3, color));
			break;
		}

	}

	// Paints the maze. All open pathways between adjacent rooms are just big fat
	// white lines, drawn on a black background.
	public void paintMaze() {

		int unitLength = a * b;
		int offset = a * b / 2;
		int lineWidth = a * (b - 1);
		int pad = a / 2;
		int direction, x1, y1, x2, y2;
		boolean overlap = (maze.generationAlgorithm == "Recursive Backtracking") ? false : true;

		listAdjacentRooms.clear();

		// Open north door of start room
		listAdjacentRooms.add(new AnimatedLine(a, 0, lineWidth, unitLength, 1, Color.WHITE));

		// Fill up maze with room pairs
		for (int i = 0; i < maze.openingDoorSequence.length; i++) {
			direction = maze.openingDoorSequence[i].doorNumber;
			x1 = maze.openingDoorSequence[i].i % maze.n * unitLength + offset + pad;
			y1 = maze.openingDoorSequence[i].i / maze.n * unitLength + offset + pad;
			x2 = maze.openingDoorSequence[i].j % maze.n * unitLength + offset + pad;
			y2 = maze.openingDoorSequence[i].j / maze.n * unitLength + offset + pad;
			addLine(x1, y1, x2, y2, lineWidth, lineWidth, direction, overlap, Color.WHITE, listAdjacentRooms);
		}

		// Open south door of goal room
		int x = (maze.n - 1) * unitLength + pad;
		int y;
		if (maze.generationAlgorithm == "Recursive Backtracking")
			y = maze.n * unitLength - pad;
		else
			y = (maze.n - 1) * unitLength + pad;
		listAdjacentRooms.add(new AnimatedLine(x + pad, y + pad, lineWidth, pixelSize - y, 1, Color.WHITE));

	}

	// Draws the maze solution (red line).
	public void paintSolution(int[] solutionPath) {

		int unitLength = a * b;
		int offset = a * b / 2;
		int lineWidth = a * (b - 1);
		int pad = a / 2;
		int direction, x1, y1, x2, y2;

		// Paint the path solution
		int i, j;

		// Update some variables for drawing red solution path
		lineWidth = a * b / 2;

		listSolutionLines.clear();

		// Draw starting red line
		listSolutionLines.add(new AnimatedLine(offset - lineWidth / 2 + pad, 0, lineWidth, pad + offset + lineWidth / 2,
				1, Color.RED));

		// Draw the whole red solution path
		for (int idx = 0; idx < solutionPath.length - 1; idx++) {

			i = solutionPath[idx];
			j = solutionPath[idx + 1];

			direction = maze.getDoorNumber(i, j);
			x1 = i % maze.n * unitLength + offset + pad;
			y1 = i / maze.n * unitLength + offset + pad;
			x2 = j % maze.n * unitLength + offset + pad;
			y2 = j / maze.n * unitLength + offset + pad;

			addLine(x1, y1, x2, y2, lineWidth, lineWidth, direction, false, Color.RED, listSolutionLines);

		}

		// Draw red path going out from maze
		int x = (maze.n - 1) * unitLength + offset - lineWidth / 2;
		int y = (maze.n - 1) * unitLength + offset + lineWidth / 2;
		listSolutionLines.add(new AnimatedLine(x + pad, y + pad, lineWidth, pixelSize - y, 1, Color.RED));

	}

	// Paints the arrows.
	public void paintArrows(int[] steps, int[] direction, ArrayList<Arrow> list, int idx) {

		int unitLength = a * b;
		int offset = a * b / 2;
		int lineWidth = a * (b - 1);
		int pad = a / 2;
		int roomNumber, x, y;

		roomNumber = steps[idx];
		x = roomNumber % maze.n * unitLength + offset - lineWidth / 2 + pad;
		y = roomNumber / maze.n * unitLength + offset - lineWidth / 2 + pad;
		list.add(new Arrow(x, y, lineWidth, direction[idx]));

	}

	// Paints the solving procedure.
	public void paintSteps(int[] steps, ArrayList<Rectangle> list, int idx) {

		int unitLength = a * b;
		int offset = a * b / 2;
		int lineWidth = a * (b - 1);
		int pad = a / 2;
		int roomNumber, x, y;

		roomNumber = steps[idx];
		x = roomNumber % maze.n * unitLength + offset - lineWidth / 2 + pad;
		y = roomNumber / maze.n * unitLength + offset - lineWidth / 2 + pad;
		list.add(new Rectangle(x, y, lineWidth, lineWidth));

	}
}
