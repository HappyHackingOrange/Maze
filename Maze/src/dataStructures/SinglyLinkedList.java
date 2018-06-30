package dataStructures;
/* SinglyLinkedList.java */

/**
 * Creates a singly linked list which each node only points to next node.
 *
 * @author Vincent Stowbunenko
 * 
 **/

public class SinglyLinkedList {

	// Instance variables
	private SinglyLinkedListNode head;
	private SinglyLinkedListNode tail; 
	private int size;

	// Instance methods

	// Checks if the list is empty.
	public boolean isEmpty() {
		return size == 0;
	}

	// Gets the size of the list.
	public int length() {
		return size;
	}

	// Adds a node to the front of the list.
	public void insertFront(Object obj) {
		head = new SinglyLinkedListNode(obj, head);
		if (tail == null) 
			tail = head;
		size++;
	}

	// Adds a node to the back of the list.
	public void insertEnd(Object obj) {
		if (head == null) 
			tail = head = new SinglyLinkedListNode(obj);
		else {
			tail.next = new SinglyLinkedListNode(obj); 
			tail = tail.next; 
		}
		size++;
	}

	// Removes a node from the front of the list.
	public Object removeFront() {
		if (isEmpty())
			return null;
		SinglyLinkedListNode temp = head;
		head = (size == 1) ? null : temp.next;
		size--;
		return temp.item;
	}


}