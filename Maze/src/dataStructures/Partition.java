package dataStructures;

/**
 * Union-by-size tree-based disjoint set with quick-union algorithm.
 * 
 * @author Vincent Stowbunenko
 */
public class Partition {

	/**
	 * Finds and returns the set of the set's root.
	 */
	public static Set find(Set A) {
		if (A.getParent() != A)
			A.setParent(find(A.getParent()));
		return A.getParent();
	}

	/**
	 * Merges sets A and B into one set.
	 */
	public static void union(Set A, Set B) {

		Set rootA = find(A);
		Set rootB = find(B);

		if (rootA != rootB)
			if (rootA.getSize() > rootB.getSize()) {
				rootB.setParent(rootA);
				rootA.setSize(rootA.getSize() + rootB.getSize());
			} else {
				rootA.setParent(rootB);
				rootB.setSize(rootB.getSize() + rootA.getSize());
			}

	}

}
