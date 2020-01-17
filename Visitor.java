package cs146F19.Bedi.project4;

//Professor asked to separate files if generic implementation used
public interface Visitor<Key extends Comparable<Key>> 
{
	/**
	 * This method is called at each node.
	 * @param n the visited node
	 * 
	 */
	void visit(Node<Key> n); 
}