import project.*;

import java.awt.EventQueue;
import java.util.Scanner;

/**
 * The driver class that runs everything.
 * 
 * @author Vincent Stowbunenko
 *
 */
public class Driver {

	public static void main(String[] args) {

		Maze2 maze;
		
		// Read the maze file if given
		if (args.length > 0) {
			maze = new Maze2();
			maze.read(args[0]);

		// Otherwise generate a random maze with specified size n x n
		} else {
			Scanner input = new Scanner(System.in);
			System.out.print("Enter size of maze (1-269): ");
			maze = new Maze2(input.nextInt());
			System.out.println();

			System.out.println("1. Imperfect Kruskal's (algorithm for this project)");
			System.out.println("2. Perfect Kruskal's");
			System.out.println("3. Recursive Backtracking (DFS)");
			System.out.println("4. Eller's");
			System.out.print("Choose a maze generation algorithm above (1-4): ");

			switch (input.nextInt()) {
			case 1:
				maze.generate(); 
				break;
			case 2:
				maze.generate("Kruskal's");
				break;
			case 3:
				maze.generate("Recursive Backtracking");
				break;
			case 4:
				maze.generate("Eller's");
				break;
			}
			input.close();
		}

		MazePanel mazePanel = new MazePanel(maze);
		MazeGUI mazeGUI = new MazeGUI(mazePanel);
		EventQueue.invokeLater(mazeGUI);

	}

}
