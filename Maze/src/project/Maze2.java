/**
 * Maze2.java
 */
package project;

import dataStructures.*;
import dataStructures.Queue;
import dataStructures.Set;
import dataStructures.Stack;

import java.math.BigInteger;
import java.util.*;
import java.util.Map.Entry;

/**
 * Maze2 class extends the Maze class where Maze2 contains extra features which
 * are not required for the project.
 * 
 * @author Vincent Stowbunenko
 *
 */
public class Maze2 extends Maze {

	// Constructors
	public Maze2(int n) {
		super(n);
		generationAlgorithm = null;
	}

	public Maze2() {
	}

	// Generates a random maze of size n x n using a specified algorithm
	public void generate(String algorithm) {

		generationAlgorithm = algorithm;

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
		int i = 0, iRow = 0, iCol = 0;
		int j = 0, jRow = 0, jCol = 0;

		switch (algorithm) {

		// Kruskal's algorithm will connect ALL rooms, generating a perfect maze.
		case "Kruskal's":

			// Generate a list of all possible pairs of adjacent rooms (room number i and j,
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
			while (idx < pairs.length) {

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

			break;

		// Generate maze using recursive backtracking (DFS)
		case "Recursive Backtracking":

			Stack stack = new Stack();

			// Start with the start room
			start.setVisited(true);
			stack.push(i);

			// Randomly choose a door to unvisited adjacent room, open the door, and go to
			// next room
			while (!stack.isEmpty()) {

				// Randomly choose door to unvisited adjacent room
				doorNumber = chooseRandomDoor(i);

				// If all adjacent rooms have been visited, go back to last room
				if (doorNumber == -1) {
					i = (int) stack.pop();
					iRow = i / n;
					iCol = i % n;
					continue;
				}

				// Get the adjacent room which door number leads to, and its position on the
				// maze
				j = getNextRoom(i, doorNumber);
				jRow = j / n;
				jCol = j % n;

				// Open a door between those rooms
				openDoor(rooms[iRow][iCol], rooms[jRow][jCol], doorNumber);

				// Save those info for drawing maze with graphics
				openingDoorSequence.enqueue(new Pair(i, j, doorNumber));

				// Add new room to the stack and mark it visited
				stack.push(j);
				i = j;
				iRow = jRow;
				iCol = jCol;
				rooms[iRow][iCol].setVisited(true);
			}

			break;

		// Generate maze using Eller's algorithm
		case "Eller's":

			// To keep track of how many rooms share same set in a row
			Map<Set, BigInteger> freqTable = new HashMap<>();

			for (iRow = 0; iRow < n - 1; iRow++) {

				// Randomly connect adjacent rooms in that row, but only if they are not in the
				// same set.
				doorNumber = 2;
				jRow = iRow;
				for (iCol = 0, jCol = 1; iCol < n - 1; iCol++, jCol++) {
					i = iRow * n + iCol;
					if (Partition.find(set[i]) != Partition.find(set[i + 1]) && new Random().nextBoolean()) {
						// Open a door between those rooms
						openDoor(rooms[iRow][iCol], rooms[jRow][jCol], doorNumber);
						// Save those info for drawing maze with graphics
						j = jRow * n + jCol;
						openingDoorSequence.enqueue(new Pair(i, j, doorNumber));
						// Then union those position in the disjoint set structure
						Partition.union(set[i], set[i + 1]);
					}
				}

				// For each set, randomly create vertical connections downward to next row. Each
				// remaining set must have at least one vertical connection.
				// This is done by bit manipulation.
				doorNumber = 1;
				jRow = iRow + 1;

				// Frequency table stores all sets in that row. For each set, their value starts
				// at 2, and is shifted to the left by one if other element is found within same
				// set.
				//
				// Example:
				// 1 element in set -> 2
				// 2 elements in set -> 2^2 (2 << 1)
				// 3 elements in set -> 2^3 (2 << 2)
				// ...
				// etc.
				//
				freqTable.clear();
				for (iCol = 0; iCol < n; iCol++) {
					i = iRow * n + iCol;
					if (freqTable.containsKey(Partition.find(set[i])))
//						freqTable.put(Partition.find(set[i]), freqTable.get(Partition.find(set[i])) << 1);
						freqTable.put(Partition.find(set[i]), freqTable.get(Partition.find(set[i])).add(BigInteger.ONE));
					else
//						freqTable.put(Partition.find(set[i]), 2);
						freqTable.put(Partition.find(set[i]), BigInteger.ONE);
				}

				// Iterate through the list of sets, get a random value between
				// 1 to 2^(number of elements in that set)-1
				// This is done to get a random m-bit value for m elements in a set, excluding
				// value 0.
				//
				// Example: if this set contains 3 elements
				// 3 elements in set -> rand(2^3-1)+1 -> rand(0b111)+1
				// -> random number between 0b001 to 0b111
				// -> {0b001, 0b010, 0b011, 0b100, 0b101, 0b110, 0b111}
				//
				Iterator<Entry<Set, BigInteger>> it = freqTable.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry<Set, BigInteger> pair = (Map.Entry<Set, BigInteger>) it.next();
//					freqTable.put(pair.getKey(), new Random().nextInt(pair.getValue() - 1) + 1);
					freqTable.put(pair.getKey(), randomForBitsNonZero(pair.getValue().intValue(), new Random()));
				}
				
				// Go through each column in that row again. Find the set number in each element
				// and if the last bit of the corresponding value in the frequency table is 1,
				// open the room below. Whether the last bit is 0 or 1, remove the last bit by
				// shifting the value to the right.
				for (iCol = jCol = 0; iCol < n; iCol++, jCol++) {
					i = iRow * n + iCol;
					Set tempSet = Partition.find(set[i]);
					if (freqTable.get(tempSet).testBit(0)) {
						// Open a door between those rooms
						openDoor(rooms[iRow][iCol], rooms[jRow][jCol], doorNumber);
						// Save those info for drawing maze with graphics
						j = jRow * n + jCol;
						openingDoorSequence.enqueue(new Pair(i, j, doorNumber));
						// Then union those position in the disjoint set structure
						Partition.union(set[i], set[j]);
						if (tempSet != Partition.find(set[i])) {
							freqTable.put(Partition.find(set[j]), freqTable.get(tempSet));
							freqTable.remove(tempSet);
						}
					}
					freqTable.put(Partition.find(set[i]), freqTable.get(Partition.find(set[i])).shiftRight(1));
				}

			}

			// For the last room, all rooms must be connected to the same set
			doorNumber = 2;
			jRow = iRow;
			for (iCol = 0, jCol = 1; iCol < n - 1; iCol++, jCol++) {
				i = iRow * n + iCol;
				if (Partition.find(set[i]) != Partition.find(set[i + 1])) {
					// Open a door between those rooms if they are not connected yet
					openDoor(rooms[iRow][iCol], rooms[jRow][jCol], doorNumber);
					// Save those info for drawing maze with graphics
					j = jRow * n + jCol;
					openingDoorSequence.enqueue(new Pair(i, j, doorNumber));
				}
			}

			break;

		// Generate maze using Prim's algorithm
		case "Prim's":
			// TODO
			break;

		// Generate maze using recursive division algorithm
		case "Recursive Division":
			// TODO
			break;

		// Generate maze using Aldous-Broder algorithm
		case "Aldous-Broder":
			// TODO
			break;

		// Generate maze using Wilson's algorithm
		case "Wilson's":
			// TODO
			break;

		// Generate maze using Hunt-and-Kill algorithm
		case "Hunt-and-Kill":
			// TODO
			break;

		// Generate maze using Binary Tree algorithm
		case "Binary Tree":
			// TODO
			break;

		// Generate maze using Sidewinder algorithm
		case "Sidewinder":
			// TODO
			break;

		}

		// Transfer saved opening door sequence to an array
		this.openingDoorSequence = new Pair[openingDoorSequence.list.length()];
		for (i = 0; i < this.openingDoorSequence.length; i++)
			this.openingDoorSequence[i] = (Pair) openingDoorSequence.dequeue();

	}

	// Randomly chooses next door to adjacent room. If all adjacent rooms have
	// been visited, return -1.
	private int chooseRandomDoor(int i) {

		int j;
		Object[] array = getDoorCandidates(i);

		shuffle(array);

		for (int idx = 0; idx < array.length; idx++) {
			j = getNextRoom(i, (int) array[idx]);
			if (!rooms[j / n][j % n].isVisited())
				return (int) array[idx];
		}

		return -1;
	}

	// Generates random N bit, ensuring that there is at least one bit equals 1 for all N bits
	public static BigInteger randomForBitsNonZero(int N, Random r) {
		BigInteger candidate = new BigInteger(N, r);
		while (candidate.equals(BigInteger.ZERO)) {
			candidate = new BigInteger(N, r);
		}
		return candidate;
	}

}
