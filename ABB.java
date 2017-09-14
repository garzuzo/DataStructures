import java.util.ArrayList;
import java.util.Collection;


public class ABB<K extends Comparable, V> implements IAbb<K, V> {

	private Node root;
	private int tamano;

	public ABB() {

		root = null;
		tamano = 0;

	}

	@Override
	public void insert(K key, V value) {
		Node nuevo = new Node(key, value);
		if (root == null) {
			root = nuevo;
			tamano++;
		} else {
			Node padre = root;
			root.insert(padre, nuevo);
			tamano++;
		}
	}

	@Override
	public void inorder() {
		if (root != null)
			inorder(root);
	}

	public void inorder(Node x) {

		if (x != null) {
			inorder(x.getLeftChild());
			System.out.println(x.getKey());
			inorder(x.getRightChild());
		}

	}

	@Override
	public void delete(K key) {

		if (root != null) {
			Node del = search(key);

			if (del != null) {
				if (del != root) {
					root.delete(del);
					tamano--;
				} else if (tamano != 0) {

					if (tamano != 1) {
						root.delete(del);
						tamano--;
					} else {
						root = null;
						tamano--;
					}
				} else {
					root = null;
					tamano--;
				}
			}
		}

	}

	@Override
	public Node getMinimum(Node nodo) {

		return root.getMinimum(nodo);
	}

	public Node getMinimum() {

		return root.getMinimum(root);
	}

	@Override
	public Node getMaximum(Node nodo) {

		return root.getMaximum(nodo);
	}

	public Node getMaximum() {

		return root.getMaximum(root);
	}

	@Override
	public Node search(K key) {

		if (root == null) {
			return null;
		} else {
			return root.search(root, key);
		}
	}

	@Override
	public int NumNodes() {

		return tamano;
	}

}

 interface IAbb<K extends Comparable,V> {

	public void insert(K key,V value);
	public void delete(K key);
	public Node getMinimum(Node nodo);
	public Node getMaximum(Node nodo);
	public Node search(K key);
	public int NumNodes();
	public void inorder();

}

 class Node<K extends Comparable, V> {

	private Node leftChild;
	private Node rightChild;
	private K key;
	private V value;
	private Node parent;

	public Node() {

		key = null;
		value = null;
		leftChild = null;
		rightChild = null;
		parent = null;
	}

	public Node(K k) {

		key = k;
		value = null;
		leftChild = null;
		rightChild = null;
		parent = null;
	}

	public Node(K k, V v) {

		key = k;
		value = v;
		leftChild = null;
		rightChild = null;
		parent = null;
	}

	public Node(V v) {

		key = null;
		value = v;
		leftChild = null;
		rightChild = null;
		parent = null;
	}

	public boolean isLeaf(Node x) {

		if (x.leftChild == null && x.rightChild == null)
			return true;

		return false;
	}

	public void insert(Node actual, Node x) {
		// TODO Auto-generated method stub
		// Node padre=actual;
		if (x.key.compareTo(actual.key) < 0) {
			if (actual.leftChild == null) {
				actual.leftChild = x;
				x.setParent(actual);
			} else {
				insert(actual.leftChild, x);
			}
		} else {
			if (actual.rightChild == null) {
				actual.rightChild = x;
				x.setParent(actual);
			} else {
				insert(actual.rightChild, x);
			}
		}

	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node getSuccessor(Node x) {

		if (x.rightChild != null) {
			return getMinimum(x.rightChild);
		}
		Node y = x.parent;
		while (y != null && x == y.rightChild) {
			x = y;
			y = y.parent;
		}

		return y;
	}

	public void delete(Node x) {
		// TODO Auto-generated method stub

		// caso 1
		if (isLeaf(x)) {

			if (x == x.parent.leftChild) {
				x.parent.leftChild = null;
			} else {
				x.parent.rightChild = null;
			}

			// caso 2
		} else {

			if (x.leftChild == null) {
				if (x.parent != null) {
					x.rightChild.parent = x.parent;

					if (x == x.parent.leftChild) {
						x.parent.leftChild = x.rightChild;
					} else {
						x.parent.rightChild = x.rightChild;
					}
				} else {
					x = x.rightChild;
				}
			} else if (x.rightChild == null) {
				if (x.parent != null) {
					x.leftChild.parent = x.parent;
					if (x == x.parent.leftChild) {
						x.parent.leftChild = x.rightChild;
					} else {
						x.parent.rightChild = x.rightChild;
					}

				} else {

					x = x.leftChild;
				}
				// caso 3: dos hijos
			} else {
				Node y = getSuccessor(x);
				x.setKey(y.key);
				x.setValue(y.value);
				delete(y);
			}

		}
	}

	public Node getMinimum(Node x) {

		while (x.leftChild != null) {
			x = x.leftChild;
		}

		return x;
	}

	public Node getMaximum(Node x) {
		// TODO Auto-generated method stub
		while (x.rightChild != null) {
			x = x.rightChild;
		}

		return x;
	}

	public Node search(Node x, K k) {

		if (x == null || k == x.key) {
			return x;
		} else if (k.compareTo(x.key) < 0)
			return search(x.leftChild, k);
		else

			return search(x.rightChild, k);
	}

	public int NumNodes() {

		return 0;
	}

	public Node getLeftChild() {
		return leftChild;
	}

	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}

	public Node getRightChild() {
		return rightChild;
	}

	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
