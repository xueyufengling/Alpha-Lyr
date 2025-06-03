package lyra.alpha.struct;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class Node<K, V> {
	public K name;
	public V value;
	private Node<K, V> parent = null;
	private int depth = 1;
	private HashMap<K, Node<K, V>> children = null;

	public Node() {

	}

	public Node(K name) {
		this(name, null, null);
	}

	public Node(K name, V value) {
		this(name, value, null);
	}

	public Node(K name, Node<K, V> parent) {
		this(name, null, parent);
	}

	public Node(K name, V value, Node<K, V> parent) {
		this.name = name;
		this.value = value;
		if (parent != null)
			this.attachTo(parent);
	}

	public Node<K, V> attachChildNode(Node<K, V> child) {
		child.parent = this;
		child.depth = depth + 1;
		if (children == null)
			children = new HashMap<>();
		children.put(child.name, child);
		return this;
	}

	public Node<K, V> attachChildNode(K name, V value) {
		return attachChildNode(new Node<K, V>(name, value));
	}

	/**
	 * 将当前节点添加为指定节点的子节点
	 * 
	 * @param parent
	 * @return
	 */
	public Node<K, V> attachTo(Node<K, V> parent) {
		this.parent = parent;
		depth = parent.depth + 1;
		if (parent.children == null)
			parent.children = new HashMap<>();
		parent.children.put(name, this);
		return this;
	}

	public Node<K, V> getParent() {
		return parent;
	}

	/**
	 * 是否有子节点
	 * 
	 * @return
	 */
	public boolean hasChildren() {
		return children == null || children.isEmpty();
	}

	public Node<K, V> getChild(K name) {
		if (children == null)
			return null;
		return children.get(name);
	}

	@SuppressWarnings("unchecked")
	public Node<K, V> findChildNode(K[] names_chain, boolean create_if_not_exist) {
		int residual_keys_len = names_chain.length - 1;
		K[] residual_keys = null;
		if (residual_keys_len > 0) {
			residual_keys = (K[]) new Object[residual_keys_len];
			System.arraycopy(names_chain, 1, residual_keys, 0, residual_keys_len);
		} else
			return new Node<K, V>(names_chain[0], this);// 最后一个节点
		if (children == null || (!children.containsKey(names_chain[0]))) {
			if (create_if_not_exist)
				attachChildNode(new Node<K, V>(names_chain[0]));
			else
				return null;
		}
		Node<K, V> child = children.get(names_chain[0]);
		return child.findChildNode(residual_keys, create_if_not_exist);
	}

	public int getDepth() {
		return depth;
	}

	public Node<K, V>[] getChildren() {
		if (children == null)
			return null;
		@SuppressWarnings("unchecked")
		Node<K, V>[] c = new Node[children.size()];
		Set<Entry<K, Node<K, V>>> children_set = children.entrySet();
		int idx = 0;
		for (Entry<K, Node<K, V>> child : children_set) {
			c[idx] = child.getValue();
			++idx;
		}
		return c;
	}

	@Override
	public String toString() {
		return "{name=" + name.toString() + ", value=" + value.toString() + "}";
	}
}
