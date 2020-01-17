package cs146F19.Bedi.project4;

public class RedBlackTree<Key extends Comparable<Key>> 
{	
	public Node<Key> root;

	//Made a new class file for Node and moved the code over because generic implementation

	public boolean isLeaf(Node<Key> n)
	{
		if (n.equals(root) && n.leftChild == null && n.rightChild == null)
		{
			return true;
		}
		if (n.equals(root)) 
		{
			return false;
		}
		if (n.leftChild == null && n.rightChild == null)
		{
			return true;
		}
		return false;
	}

	//Visitor interface moved to new interface file

	public void visit(Node<Key> n)
	{
		System.out.println(n.key);
	}

	public void printTree()
	{  //preorder: visit, go left, go right
		Node<Key> currentNode = root;	
		printTree(currentNode);
	}

	public void printTree(Node<Key> node)
	{
		System.out.print(node.key);
		if (isLeaf(node))
		{
			return;
		}
		printTree(node.leftChild);
		printTree(node.rightChild);
	}

	//place a new node in the RB tree with data the parameter and color it red. 
	public Node<Key> addNode(Key data)
	{//this < that  <0.  this > that  >0

		Node<Key> insertNode = new Node(data);

		if (root == null)
		{
			root = insertNode;
		}
		else
		{
			Node<Key> currentNode = root;
			Node<Key> previousNode = null;

			while (currentNode != null)
			{
				previousNode = currentNode;

				if (insertNode.compareTo(currentNode) >= 0)
				{
					currentNode = currentNode.rightChild;

					if (currentNode == null)
					{
						previousNode.rightChild = insertNode;
					}
				}
				else
				{
					currentNode = currentNode.leftChild;

					if (currentNode == null)
					{
						previousNode.leftChild = insertNode;
					}
				}
				insertNode.parent = previousNode;
			}
		}
		return insertNode;
	}	

	//this method inserts nodes into the RBTree and makes sure the BST properties are maintained
	public void insert(Key data)
	{
		Node<Key> currentNode = addNode(data);

		if (currentNode.compareTo(root) == 0)
		{
			colorNode(currentNode, false);
		}
		else
		{
			fixTree(currentNode);
		}
	}

	//searches for a key
	public Node<Key> lookup(Key k)
	{
		if (root == null)
		{
			return null;				
		}
		else
		{
			Node<Key> currentNode = root;
			while (currentNode != null && currentNode.key.compareTo(k) != 0)
			{
				if (k.compareTo(currentNode.key) >= 0)
				{
					currentNode = currentNode.rightChild;
				}
				else
				{
					currentNode = currentNode.leftChild;
				}
			}
			return currentNode;
		}
	}

	//returns sibling node of the parameter node, return null if sibling does not exist
	public Node<Key> getSibling(Node<Key> n)
	{
		if (n.parent == null)
		{
			return null;
		}
		else
		{
			if (isLeftChild(n.parent, n) == true)
			{
				return n.parent.rightChild;
			}
			else
			{
				return n.parent.leftChild;
			}
		}
	}

	//returns the aunt of the parameter or the sibling of the parent node
	//If the aunt node does not exist, then return null. 
	public Node<Key> getAunt(Node<Key> n)
	{
		Node<Key> grandparent = getGrandparent(n);
		if (grandparent == null)
		{
			return null;
		}
		else
		{
			if (isLeftChild(grandparent, n.parent) == true)
			{
				return grandparent.rightChild;
			}
			else
			{
				return grandparent.leftChild;
			}
		}
	}

	//returns the parent of your parent node. if it doesn't exist then return null
	public Node<Key> getGrandparent(Node<Key> n)
	{
		if (n.parent != null)
		{
			return n.parent.parent;
		}
		return null;
	}

	//used to set the color of the nodes
	private void colorNode(Node<Key> node, boolean color)
	{
		if (node != null)
		{
			node.isRed = color;
			if (color == true)
			{
				node.color = 0;
			}
			else
			{
				node.color = 1;
			}
		}
	}

	//Used code provided in slides by Prof Potika for left rotation
	public void rotateLeft(Node<Key> x)
	{
		Node<Key> y = x.rightChild;
		x.rightChild = y.leftChild;
		if (y.leftChild != null)
		{
			y.leftChild.parent = x;
		}
		y.parent = x.parent;
		if (x.parent == null)
		{
			root = y;
			colorNode(y, false);
		}
		else if (x.compareTo(x.parent.leftChild) == 0)
		{
			x.parent.leftChild = y;
		}
		else
		{
			x.parent.rightChild = y;
		}
		y.leftChild = x;
		x.parent = y;
	}

	//Used same code from slides of left Rotation and just flipped x and y
	public void rotateRight(Node<Key> y)
	{
		Node<Key> x = y.leftChild;
		y.leftChild = x.rightChild;
		if (x.rightChild != null)
		{
			x.rightChild.parent = y;
		}

		x.parent = y.parent;
		if (y.parent == null)
		{
			root = x;
			colorNode(x, false);
		}
		else if (y.compareTo(y.parent.rightChild) == 0)
		{
			y.parent.rightChild = x;
		}
		else
		{
			y.parent.leftChild = x;
		}
		x.rightChild = y;
		y.parent = x;
	}

	//fixes tree using the code discussed in class and found in slides for the first three cases
	public void fixTree(Node<Key> node)
	{
		Node<Key> x = node;
		Node<Key> y = getAunt(node);

		while (x.compareTo(root) != 0 && (x.parent.isRed == true && y != null && y.isRed == true))
		{
			//case 1: uncle is red
			colorNode(x.parent, false);
			colorNode(y, false);
			colorNode(x.parent.parent, true);
			x = x.parent.parent;

			if (x.compareTo(root) == 0)
			{
				colorNode(x, false);
			}
			y = getAunt(x);
		}

		if (x.compareTo(root) != 0 && x.parent.isRed == true && (y == null || y.isRed == false))
		{
			//case 3: uncle is black; node x is a left Child
			if (isLeftChild(x.parent.parent, x.parent) == true)
			{
				if (x.parent.rightChild != null && x.compareTo(x.parent.rightChild) == 0)
				{
					x = x.parent;
					rotateLeft(x);
				}
				colorNode(x.parent, false);
				colorNode(x.parent.parent, true);
				rotateRight(x.parent.parent);
			}
			//case 2: uncle is black; node x is right CHild
			else
			{
				if (x.parent.leftChild != null && x.compareTo(x.parent.leftChild) == 0)
				{
					x = x.parent;
					rotateRight(x);
				}
				colorNode(x.parent, false);
				colorNode(x.parent.parent, true);
				rotateLeft(x.parent.parent);
			}
		}
	}

	public boolean isEmpty(Node<Key> n)
	{
		if (n.key == null)
		{
			return true;
		}
		return false;
	}

	public boolean isLeftChild(Node<Key> parent, Node<Key> child)
	{
		if (child.compareTo(parent) < 0 )
		{//child is less than parent
			return true;
		}
		return false;
	}

	public void preOrderVisit(Visitor<Key> v)
	{
		preOrderVisit(root, v);
	}


	private void preOrderVisit(Node<Key> n, Visitor<Key> v)
	{
		if (n == null) 
		{
			return;
		}
		v.visit(n);
		preOrderVisit(n.leftChild, v);
		preOrderVisit(n.rightChild, v);
	}
}