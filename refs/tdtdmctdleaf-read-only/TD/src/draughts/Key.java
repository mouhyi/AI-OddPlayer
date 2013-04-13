package draughts;

import draughts.moves.Node;

public class Key {

	private final Node node;

	// private final int depth;

	public Key(Node node, int depth) {
		this.node = new Node(node.getBoard());
		// this.depth = depth;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		// result = prime * result + depth;
		result = prime * result + ((node == null) ? 0 : node.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Key other = (Key) obj;
		// if (depth != other.depth)
		// return false;
		if (node == null) {
			if (other.node != null)
				return false;
		} else if (!node.equals(other.node))
			return false;
		return true;
	}

}
