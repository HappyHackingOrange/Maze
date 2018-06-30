/**
 * Pair.java
 */
package dataStructures;

/**
 * Stores data the positions of the adjacent rooms i and j and the the door
 * number towards room j from room i.
 * 
 * @author Vincent Stowbunenko
 *
 */
public class Pair {

	public final int i;
	public final int j;
	public final int doorNumber;

	public Pair(int i, int j, int doorNumber) {
		this.i = i;
		this.j = j;
		this.doorNumber = doorNumber;
	}

}
