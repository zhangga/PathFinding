package com.abc.pathfinding;

import java.util.List;

import com.abc.pathfinding.astar.AStarFinder;
import com.abc.pathfinding.jps.JPSFinder;
import com.abc.pathfinding.map.MapData;
import com.abc.pathfinding.map.Node;

public class PathFinder {

	public static void main(String[] args) {
		int h = 2 << 16 + 1;
		System.out.println(h);
		h = (1 << 16) + 2;
		System.out.println(h);
		// 地图数据
		MapData mapData = new MapData();
		// JPS
		Finder finder = new JPSFinder(mapData);
		// 1-map
//		long curr = System.currentTimeMillis();
//		for (int i = 0; i < 10000; i++) {			
//			List<Node> paths = finder.findPath(new Node(1, 39), new Node(99, 65));
//		}
//		System.out.println("耗时：" + (System.currentTimeMillis() - curr));
		// 2-map
		List<Node> paths = finder.findPath(new Node(7, 1), new Node(5, 5));
		// 3-map
//		List<Node> paths = finder.findPath(new Node(6, 2), new Node(3, 6));
		System.out.println(paths);
	}
	
}
