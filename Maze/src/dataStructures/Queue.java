package dataStructures;
/* Queue.java */

/**
 * Implements a queue class (an abstract data type) using SinglyLinkedList class
 * 
 * @author Vincent Stowbunenko
 *
 */
public class Queue {

	public SinglyLinkedList list;

	/**
	 * Constructs an empty queue structure.
	 * 
	 * @param list
	 */
	public Queue() {
		list = new SinglyLinkedList();
	}

	/**
	 * Adds entry to the back.
	 * 
	 * @param The
	 *            entry to be added to the front of the queue.
	 */
	public void enqueue(Object entry) {
		list.insertEnd(entry);
	}

	/**
	 * Removes the entry at the front.
	 * 
	 * @return the entry that is being removed. If empty, it will return null.
	 */
	public Object dequeue() {
		return list.removeFront();
	}
	
	/**
	 * Checks if the queue is empty.
	 * 
	 * @return boolean value whether the queue is empty or not.
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}

}
