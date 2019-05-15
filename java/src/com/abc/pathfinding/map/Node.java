package com.abc.pathfinding.map;

public class Node implements Comparable<Node> {
	
	public int x;
	public int y;
	
	public int hashCode;
	
	public double g;
	public double f;
	
	public Node pre;
	
	public Node() {
		
	}
	
	public Node(int x, int y) {
		this.x = x;
		this.y = y;
		this.hashCode = (x << 16) + y;
	}
	
	@Override
	public int hashCode() {
		return this.hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		// 直接判断
		Node other = (Node) obj;
		return x == other.x && y == other.y;
	}
	
	@Override
	public int compareTo(Node o) {
		if (this == o)
			return 0;
		int v = Integer.compare(this.hashCode, o.hashCode);
		if (v == 0)
			return 0;
		if (Math.abs(this.f - o.f) < 0.01 ) {
			// 接着比较，保证compareTo和equals一致(即comparaTo返回0的情况，equals一定返回true，TreeSet要求的)
			return v;
		}
		else if (this.f < o.f) {
			return -1;
		}
		else {
			return 1;
		}
	}

	@Override
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

}
