package com.abc.pathfinding;

import java.util.List;
import java.util.PriorityQueue;
import java.util.TreeSet;

import com.abc.pathfinding.astar.AStarFinder;
import com.abc.pathfinding.jps.JPSFinder;
import com.abc.pathfinding.map.MapData;
import com.abc.pathfinding.map.Node;

public class PathFinder {

	public static void main(String[] args) {
		// 测试
		test();
		// 地图数据
		MapData mapData = new MapData();
		// JPS
		Finder finder = new JPSFinder(mapData);
		// 1-map
//		long curr = System.currentTimeMillis();
//		for (int i = 0; i < 10000; i++) {			
//			List<Node> paths = finder.findPath(new Node(1, 39), new Node(99, 65));
//			List<Node> paths = finder.findPath(new Node(5, 2), new Node(5, 6));
//		}
//		System.out.println("耗时：" + (System.currentTimeMillis() - curr));
		// 1-map
//		List<Node> paths = finder.findPath(new Node(1, 39), new Node(99, 65));
		// 2-map
		List<Node> paths = finder.findPath(new Node(4, 1), new Node(4, 7));
		// 3-map
//		List<Node> paths = finder.findPath(new Node(6, 2), new Node(3, 6));
		System.out.println(paths);
	}
	
	private static void test() {
		// 优先级队列
		PriorityQueue<Node> queue = new PriorityQueue<>();
		Node node1 = new Node(1, 1);
		node1.f = 2;
		Node node2 = new Node(2, 2);
		node2.f = 1;
		Node node3 = new Node(3, 3);
		node3.f = 3;
		queue.add(node1);
		queue.add(node2);
		queue.add(node3);
		node2.f = 4;
		queue.remove(node2);
		queue.add(node2);
		System.out.println(queue.peek());
		
		// TreeSet
		TreeSet<Node> treeset = new TreeSet<>();
		treeset.add(node1);
		treeset.add(node2);
		treeset.add(node3);
		System.out.println(treeset.first());
		node2.f = 1;
		treeset.add(node2);
		System.out.println(treeset.first());
	}
	
}
