package project;

import dataStructures.*;

import java.util.Random;
import java.util.StringTokenizer;
import java.io.*;

/**
 * A maze is n x n grid of rooms where n is positive integer specified by user.
 * Has start room and goal room. Start room is at upper-left corner of the maze
 * and goal room is at bottom-right of the maze.
 * 
 * @author Vincent Stowbunenko
 *
 */
public class Maze {

	// Instance variables
	protected int n; // n x n maze
	protected int N; // N = n*2;

	protected Room start;
	private Room goal;
	protected Room[][] rooms;

	protected String generationAlgorithm;
	protected String solvingAlgorithm;

	protected Pair[] openingDoorSequence;

	protected int[] solvingProcedure;
	protected int[] solutionPath;
	protected int[] directions;

	// Maze constructors
	public Maze(int n) {

		// Maze size
		this.n = n;
		N = n * n;
		setup();

	}

	public Maze() {
	}

	public void setup() {
		// Instantiate rooms in a 2D array.
		rooms = new Room[n][];
		for (int row = 0; row < n; row++) {
			rooms[row] = new Room[n];
			for (int col = 0; col < n; col++)
				rooms[row][col] = new Room();
		}

		// Specify locations of start and goal rooms
		start = rooms[0][0];
		goal = rooms[n - 1][n - 1];
	}

	// Generates a random maze of size n x n.
	public void generate() {

		// Store opening sequence for printing and drawing
		Queue openingDoorSequence = new Queue();

		// Set up disjoint sets (of room positions 0, 1, 2, ... N - 1)
		Set[] set = new Set[N];
		for (int idx = 0; idx < N; idx++)
			set[idx] = new Set();

		// For door number:
		// 0 - north
		// 1 - south
		// 2 - east
		// 3 - west
		int doorNumber;

		// Initial variables for picking a random room pair (i,j)
		int i = 0, j = 0;

		// Generate maze using randomized version of imperfect Kruskal's
		// algorithm (the algorithm to use for this project) but this algorithm will
		// also
		// generate isolated rooms.

		// Generate a list of all possible pairs of adjacent rooms (room number
		// i and j,
		// and door number)
		Pair[] pairs = new Pair[2 * n * (n - 1)];

		// Collect vertical pairs
		doorNumber = 1;
		for (int row = 0; row < n - 1; row++)
			for (int col = 0; col < n; col++) {
				i = row * n + col;
				j = (row + 1) * n + col;
				pairs[row * n + col] = new Pair(i, j, doorNumber);
			}

		// Collect horizontal pairs
		doorNumber = 2;
		for (int row = 0; row < n; row++)
			for (int col = 0; col < n - 1; col++) {
				i = row * n + col;
				j = row * n + col + 1;
				pairs[n * (n - 1) + row * (n - 1) + col] = new Pair(i, j, doorNumber);
			}

		// Then shuffle the list of all possible pairs of adjacent rooms
		shuffle(pairs);

		// Stay in loop if there is still no path between room number 0 to N-1
		// (imperfect Kruskal's)
		// or go through whole list of possible pairs of adjacent rooms
		// (perfect Kruskal's)
		int idx = 0; // iterator for list of shuffled pairs
		while (Partition.find(set[0]) != Partition.find(set[N - 1])) {

			// Pick a random pair of adjacent rooms
			i = pairs[idx].i;
			j = pairs[idx].j;
			doorNumber = pairs[idx].doorNumber;

			// Check if room i and room j is connected
			if (Partition.find(set[i]) != Partition.find(set[j])) {

				// Open a door between those rooms
				openDoor(rooms[i / n][i % n], rooms[j / n][j % n], doorNumber);

				// Save those info for drawing maze with graphics
				openingDoorSequence.enqueue(pairs[idx]);

				// Then union those position in the disjoint set structure
				Partition.union(Partition.find(set[i]), Partition.find(set[j]));

			}

			idx++;

		}

		// Transfer saved opening door sequence to an array
		this.openingDoorSequence = new Pair[openingDoorSequence.list.length()];
		for (i = 0; i < this.openingDoorSequence.length; i++)
			this.openingDoorSequence[i] = (Pair) openingDoorSequence.dequeue();

	}

	// Reads a maze from file
	public void read(String filename) {

		generationAlgorithm = "read";

		// Store opening sequence for printing and drawing
		Queue openingDoorSequence = new Queue();

		try {
			FileReader file = new FileReader(filename);
			BufferedReader buff = new BufferedReader(file);
			int counter = 0;
			int iroom, jroom;
			int irow, icol, jrow, jcol;
			String temp;
			boolean DEBUG = true;
			boolean eof = false;

			// Keep in loop until buffer reaches end of line
			while (!eof) {

				// Get whole line before the newline, consume newline, increase
				// mark for next read
				String line = buff.readLine();
				counter++;

				// Null is considered end of line in buffered input
				if (line == null)
					eof = true;
				else {

					// Print whole line if in DEBUG mode
					if (DEBUG)
						System.out.println("Reading " + line);

					// Get total number of rooms in the maze on the first line
					if (counter == 1) {
						temp = line;
						n = Integer.parseInt(temp);
						N = n * n;
						setup();
					}

					// Read the rest of the content in the file
					if (counter > 1) {

						// Tokenize each line using StringTokenizer Object
						StringTokenizer st = new StringTokenizer(line);
						int[] doorValues = new int[4];

						// Have to do something with collected values
						iroom = counter - 2;
						irow = iroom / n;
						icol = iroom % n;

						// Each token is converted from String to Integer using
						// parseInt method
						for (int i = 0; i < 4; i++) {
							doorValues[i] = Integer.parseInt(st.nextToken());

							// Don't include first door in start room and 2nd
							// door in last room
							if (doorValues[i] == 0 && !(iroom == 0 && i == 0) && !(iroom == N - 1 && i == 1)
									&& !isDoorOpen(iroom, i)) {

								jroom = getNextRoom(iroom, i);
								jrow = jroom / n;
								jcol = jroom % n;
								openDoor(rooms[irow][icol], rooms[jrow][jcol], i);

								// Save those info for drawing maze with graphics
								openingDoorSequence.enqueue(new Pair(iroom, jroom, i));

							}
						}

					}

				}
			}
			buff.close();
		} catch (IOException e) {
			// If error if found, print out what error it was, and keep going
			System.out.println("Error -- " + e.toString());
		}

		// Transfer saved opening door sequence to an array
		this.openingDoorSequence = new Pair[openingDoorSequence.list.length()];
		for (int i = 0; i < this.openingDoorSequence.length; i++)
			this.openingDoorSequence[i] = (Pair) openingDoorSequence.dequeue();

	}

	// Write the maze to file, to test the read() method
	public void write(String filepath) {

		BufferedWriter buff = null;
		FileWriter file = null;
		StringBuilder strbldr = new StringBuilder();

		try {

			System.out.printf("Writing maze to %s...", filepath);

			file = new FileWriter(filepath);
			buff = new BufferedWriter(file);

			buff.write("" + n);

			for (int row = 0; row < n; row++)
				for (int col = 0; col < n; col++) {
					buff.newLine();
					strbldr.append((rooms[row][col].getNorth() == null && rooms[row][col] != start) ? "1 " : "0 ");
					strbldr.append((rooms[row][col].getSouth() == null && rooms[row][col] != goal) ? "1 " : "0 ");
					strbldr.append((rooms[row][col].getEast() == null) ? "1 " : "0 ");
					strbldr.append((rooms[row][col].getWest() == null) ? "1" : "0");
					buff.write(strbldr.toString());
					strbldr.setLength(0);
				}

			System.out.println("done!");

		} catch (IOException e) {

			System.out.println("Error -- " + e.toString());

		} finally {

			try {

				if (buff != null)
					buff.close();

				if (file != null)
					file.close();

			} catch (IOException e) {
				System.out.println("Error -- " + e.toString());
			}

		}

	}

	// Solves the maze either using BFS (breadth-first search) or DFS
	// (depth-first search) algorithm
	public void solve(String algorithm) {

		solvingAlgorithm = algorithm;
		Queue solvingSteps = new Queue(); // Save steps for animation later
		Stack solvingStack = new Stack(); // To get the best path from beginning to end
		Queue backtrackingSteps = new Queue(); // To see the arrows back to the beginning
		int counter;

		// For door number:
		// 0 - north
		// 1 - south
		// 2 - east
		// 3 - west
		int doorNumber;

		// Initial variables for picking a random room pair (i,j)
		SinglyLinkedListNode i = null, j = null;
		int jRow, jCol;

		// Reset all room as unvisited
		for (int row = 0; row < n; row++)
			for (int col = 0; col < n; col++)
				rooms[row][col].setVisited(false);

		switch (algorithm) {

		case "breadth-first search (BFS)":

			Queue queue = new Queue();

			// Enqueue the start room number (0)
			queue.enqueue(new SinglyLinkedListNode(0));

			// Mark the start room as visited
			start.setVisited(true);

			// Keep in loop while queue is not empty
			while (!queue.isEmpty()) {

				// Dequeue element and store it to room number i
				i = (SinglyLinkedListNode) queue.dequeue();
				solvingSteps.enqueue((int) i.getItem());
				if (i.getNext() != null)
					backtrackingSteps.enqueue(getDoorNumber((int) i.getItem(), (int) i.getNext().getItem()));
				else
					backtrackingSteps.enqueue(-1);

				// If room number i is the goal room (N-1), this algorithm is
				// done
				if ((int) i.getItem() == N - 1)
					break;

				// Get door number to the next unvisited room
				doorNumber = getNextDoor((int) i.getItem());

				// For each room j adjacent to room i such that there is a
				// passage
				while (doorNumber != -1) {

					j = new SinglyLinkedListNode(getNextRoom((int) i.getItem(), doorNumber), i);
					queue.enqueue(j);
					// solvingSteps.enqueue((int) j.getItem());

					// Mark room j as visited
					jRow = (int) j.getItem() / n;
					jCol = (int) j.getItem() % n;
					rooms[jRow][jCol].setVisited(true);

					doorNumber = getNextDoor((int) i.getItem());

				}

			}

			// Store solving steps to animate solving procedures
			solvingProcedure = new int[solvingSteps.list.length()];
			for (int idx = 0; idx < solvingProcedure.length; idx++)
				solvingProcedure[idx] = (int) solvingSteps.dequeue();

			// Store backtracking steps to show the backtracking arrows on the maze
			directions = new int[backtrackingSteps.list.length()];
			for (int idx = 0; idx < directions.length; idx++)
				directions[idx] = (int) backtrackingSteps.dequeue();

			// Generate solving path from in-tree SinglyLinkedListNodes
			counter = 0;
			while (i != null) {
				solvingStack.push(i.getItem());
				i = i.getNext();
				counter++;
			}

			// Store the path solution in an array for displaying solution path
			solutionPath = new int[counter];
			for (int idx = 0; idx < counter; idx++)
				solutionPath[idx] = (int) solvingStack.pop();

			break;

		case "depth-first search (DFS)":

			Stack stack = new Stack();

			// Push the start room number (0) to the stack
			stack.push(new SinglyLinkedListNode(0));

			// Mark the start room as visited
			start.setVisited(true);

			// Keep in loop while stack is not empty
			while (!stack.isEmpty()) {

				// Pop element and store it to room number i
				i = (SinglyLinkedListNode) stack.pop();
				solvingSteps.enqueue((int) i.getItem());
				if (i.getNext() != null)
					backtrackingSteps.enqueue(getDoorNumber((int) i.getItem(), (int) i.getNext().getItem()));
				else
					backtrackingSteps.enqueue(-1);

				// If room number i is the goal room (N-1), this algorithm is
				// done
				if ((int) i.getItem() == N - 1)
					break;

				// Get door number to the next unvisited room
				doorNumber = getNextDoor((int) i.getItem());

				// For each room j adjacent to room i such that there is a
				// passage
				while (doorNumber != -1) {

					j = new SinglyLinkedListNode(getNextRoom((int) i.getItem(), doorNumber), i);
					stack.push(j);
					// solvingSteps.enqueue((int) j.getItem());

					// Mark room j as visited
					jRow = (int) j.getItem() / n;
					jCol = (int) j.getItem() % n;
					rooms[jRow][jCol].setVisited(true);

					doorNumber = getNextDoor((int) i.getItem());

				}

			}

			// Store solving steps to animate solving procedures
			solvingProcedure = new int[solvingSteps.list.length()];
			for (int idx = 0; idx < solvingProcedure.length; idx++)
				solvingProcedure[idx] = (int) solvingSteps.dequeue();

			// Store backtracking steps to show the backtracking arrows on the maze
			directions = new int[backtrackingSteps.list.length()];
			for (int idx = 0; idx < directions.length; idx++)
				directions[idx] = (int) backtrackingSteps.dequeue();

			// Generate solving path from in-tree SinglyLinkedListNodes
			counter = 0;
			while (i != null) {
				solvingStack.push(i.getItem());
				i = i.getNext();
				counter++;
			}

			// Store the path solution in an array for displaying solution path
			solutionPath = new int[counter];
			for (int idx = 0; idx < counter; idx++)
				solutionPath[idx] = (int) solvingStack.pop();

			break;

		}

	}

	// Stringizes the maze
	@Override
	public String toString() {

		// Clever way of duplicating "_" n times to make the upper edge of whole maze
		StringBuilder strbldr = new StringBuilder("  ");
		if (n > 1)
			strbldr.append(String.format("%0" + (n - 1) + "d", 0).replace("0", " _") + "\n");

		// For each room just look at west door then north door. If it's closed
		// then add a boundary. Otherwise leave it blank
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				strbldr.append((rooms[i][j].getWest() == null) ? "|" : " ");
				strbldr.append((rooms[i][j].getSouth() == null && rooms[i][j] != goal) ? "_" : " ");
			}
			strbldr.append("|\n");
		}

		return strbldr.toString();
	}

	// Prints the maze in ASCII
	public void print() {
		System.out.println(this);
	}

	// Print out solving procedures
	public void printSolvingProcedures() {

		// So that str is just "BFS" or "DFS"
		String str = null;
		switch (solvingAlgorithm) {
		case "breadth-first search (BFS)":
			str = "BFS";
			break;
		case "depth-first search (DFS)":
			str = "DFS";
			break;
		}

		// Print out the solving procedures first
		System.out.print("Rooms visited by " + str + ":");
		for (int i = 0; i < solvingProcedure.length; i++)
			System.out.print(" " + solvingProcedure[i]);

		System.out.println();

	}

	// Print out backtracking steps
	public void printBacktracking() {

		// Use loop to print out individual values
		System.out.print("This is the path (in reverse):");
		for (int i = solutionPath.length - 1; i >= 0; i--) {
			System.out.print(" " + solutionPath[i]);
		}
		System.out.println();

	}

	// Prints the path from beginning to end.
	public void printPath() {

		System.out.println("This is the path.");
		StringBuilder[] strbldr = new StringBuilder[n];

		// Create blank double spaces for n rows
		for (int i = 0; i < n; i++) {
			String str = String.format("%0" + n + "d", 0).replace("0", "  ");
			strbldr[i] = new StringBuilder(str);
		}

		// Replace first space to "X" if it is a path
		int row, col;
		for (int i = 0; i < solutionPath.length; i++) {
			row = solutionPath[i] / n;
			col = solutionPath[i] % n;
			strbldr[row].setCharAt(2 * col, 'X');
		}

		// Print each line in StringBuilder array
		for (int i = 0; i < n; i++)
			System.out.println(strbldr[i]);

		System.out.println();

	}

	// Implements Fisher-Yates shuffle
	protected static void shuffle(Object[] array) {

		Random rand = new Random();
		int iRand;
		Object temp;

		// For all array elements from last to first (index i)
		for (int i = array.length - 1; i > 0; i--) {

			// Pick a random number from 0 to i
			iRand = rand.nextInt(i + 1);

			// Swap the randomly chosen element and put it to ith position (so
			// that this position will not be chosen again in the next loop)
			temp = array[iRand];
			array[iRand] = array[i];
			array[i] = temp;

		}

	}

	// Gets next room number given current room number and door number.
	protected int getNextRoom(int i, int doorNumber) {

		int iRow = i / n;
		int iCol = i % n;

		// Get all adjacent rooms of room i
		int[] jCandidates = new int[] { (iRow - 1) * n + iCol, (iRow + 1) * n + iCol, iRow * n + (iCol + 1),
				iRow * n + (iCol - 1) };

		return jCandidates[doorNumber];

	}

	// Generates list of possible door numbers given room number.
	protected Object[] getDoorCandidates(int i) {

		Object[] array;
		int iRow = i / n;
		int iCol = i % n;

		// For corner rooms
		if (iRow == 0 && iCol == 0)// Top-left room
			array = new Object[] { 1, 2 };
		else if (iRow == 0 && iCol == n - 1)// Top-right room
			array = new Object[] { 1, 3 };
		else if (iRow == n - 1 && iCol == 0) // Bottom-left room
			array = new Object[] { 0, 2 };
		else if (iRow == n - 1 && iCol == n - 1) // Bottom-right room
			array = new Object[] { 0, 3 };

		// For edge rooms
		else if (iRow == 0) // Rooms at top edge of the maze
			array = new Object[] { 1, 2, 3 };
		else if (iRow == n - 1) // Rooms at bottom edge of the maze
			array = new Object[] { 0, 2, 3 };
		else if (iCol == n - 1) // Rooms at the right edge of the maze
			array = new Object[] { 0, 1, 3 };
		else if (iCol == 0) // Rooms at the left edge of the maze
			array = new Object[] { 0, 1, 2 };

		// For all other rooms
		else
			array = new Object[] { 0, 1, 2, 3 };

		return array;

	}

	// Gets a door number to next unvisited adjacent room. If all adjacent rooms
	// have been visited, return -1.
	private int getNextDoor(int i) {

		Object[] array = getDoorCandidates(i);
		int iRow = i / n;
		int iCol = i % n;

		for (int idx = 0; idx < array.length; idx++) {
			switch ((int) array[idx]) {
			case 0:
				if (rooms[iRow][iCol].getNorth() != null && !rooms[iRow][iCol].getNorth().isVisited())
					return 0;
				break;
			case 1:
				if (rooms[iRow][iCol].getSouth() != null && !rooms[iRow][iCol].getSouth().isVisited())
					return 1;
				break;
			case 2:
				if (rooms[iRow][iCol].getEast() != null && !rooms[iRow][iCol].getEast().isVisited())
					return 2;
				break;
			case 3:
				if (rooms[iRow][iCol].getWest() != null && !rooms[iRow][iCol].getWest().isVisited())
					return 3;
				break;
			}
		}

		return -1;

	}

	// Open specified door number of room number i to room number j
	protected static void openDoor(Room i, Room j, int doorNumber) {

		switch (doorNumber) {
		case 0:
			i.setNorth(j);
			j.setSouth(i);
			break;
		case 1:
			i.setSouth(j);
			j.setNorth(i);
			break;
		case 2:
			i.setEast(j);
			j.setWest(i);
			break;
		case 3:
			i.setWest(j);
			j.setEast(i);
			break;
		}

	}

	// Get door number from room i to room j. If rooms are not adjacent, return
	// -1.
	protected int getDoorNumber(int i, int j) {

		int diff = j - i;

		if (diff == -n)
			return 0;
		if (diff == n)
			return 1;
		if (diff == 1)
			return 2;
		if (diff == -1)
			return 3;

		return -1;
	}

	// Checks if door is open in room i.
	protected boolean isDoorOpen(int i, int doorNumber) {
		switch (doorNumber) {
		case 0:
			return (rooms[i / n][i % n].getNorth() != null);
		case 1:
			return (rooms[i / n][i % n].getSouth() != null);
		case 2:
			return (rooms[i / n][i % n].getEast() != null);
		case 3:
			return (rooms[i / n][i % n].getWest() != null);
		}
		return false;
	}
}
