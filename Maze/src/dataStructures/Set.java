/**
 * 
 */
package dataStructures;

/**
 * Individual disjoint set as a part of a tree-based partition. Stores tree
 * size and parent set.
 * 
 * @author Vincent Stowbunenko
 *
 */
public class Set {

	private int size;
	private Set parent;

	public Set() {
		size = 1;
		parent = this; // Starts as a root
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Set getParent() {
		return parent;
	}

	public void setParent(Set parent) {
		this.parent = parent;
	}

}
