package com.abc.pathfinding.map;

public class Node {
	
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
	public String toString() {
		return "[x=" + x + ", y=" + y + "]";
	}

}
