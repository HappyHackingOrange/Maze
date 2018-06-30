package dataStructures;
/* SinglyLinkedListNode.java */

/**
 * Implements a node that stores an item and a pointer to the next node in the
 * singly linked list.
 * 
 * @author Vincent Stowbunenko
 * 
 */

public class SinglyLinkedListNode {

	// Instance variables
	Object item;
	SinglyLinkedListNode next;

	// Constructors
	
	// Creates a node containing an item and not pointing to next root.
	public SinglyLinkedListNode(Object obj) {
		item = obj;
		next = null;
	}

	// Creates a node containing an item and is pointing to next node.
	public SinglyLinkedListNode(Object obj, SinglyLinkedListNode next) {
		item = obj;
		this.next = next;
	}

	// Instance methods
	
	// Item getter 
	public Object getItem() {
		return item;
	}

	// Next getter
	public SinglyLinkedListNode getNext() {
		return next;
	}

}