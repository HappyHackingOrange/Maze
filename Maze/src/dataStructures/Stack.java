package dataStructures;
/* Stack.java */

/**
 * Implements a stack class.
 * 
 * @author Vincent Stowbunenko
 *
 */
public class Stack {

	public SinglyLinkedList list;

	/**
	 * Constructs an empty queue structure.
	 * 
	 * @param list
	 */
	public Stack() {
		list = new SinglyLinkedList();
	}

	/**
	 * Adds entry to the top.
	 * 
	 * @param entry
	 *            The entry to be added to the top of the stack.
	 */
	public void push(Object entry) {
		list.insertFront(entry);
	}

	/**
	 * Removes the entry from the top.
	 * 
	 * @return the entry that is being removed. If empty, it will return null.
	 */
	public Object pop() {
		return list.removeFront();
	}
	
	/**
	 * Checks if the stack is empty.
	 * 
	 * @return boolean value whether the stack is empty or not.
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}

}
