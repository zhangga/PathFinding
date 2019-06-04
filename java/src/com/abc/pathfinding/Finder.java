package com.abc.pathfinding;

import java.util.List;
import java.util.function.BiFunction;

import com.abc.pathfinding.map.DistanceAlgo;
import com.abc.pathfinding.map.MapData;
import com.abc.pathfinding.map.Node;

/**
 * 寻路基类
 *
 * @author U-Demon
 */
public abstract class Finder {
	
	/** 地图数据 */
	protected MapData mapData;
	
	/** 距离起点策略 */
	protected BiFunction<Integer, Integer, Double> distance = DistanceAlgo.octile;
	/** 距离终点策略 */
	protected BiFunction<Integer, Integer, Double> heuristic = DistanceAlgo.manhattan;
	
	public Finder(MapData mapData) {
		this.mapData = mapData;
	}
	
	/** 寻路 */
	public abstract List<Node> findPath(Node start, Node end);
	
	/**
     * Given two nodes, returns the estimated distance between them. Optimizing this is the best way to improve
     * performance of your search time.
     * @return Estimated distance between the two given nodes.
     */
    public double getDistance(Integer dx, Integer dy) { return distance.apply(dx, dy); }

    /**
     * Given two nodes, returns the estimated distance between them. Optimizing this is the best way to improve
     * performance of your search time.
     * @return Estimated distance between the two given nodes.
     */
    public double getHeuristic(Integer dx, Integer dy) { return heuristic.apply(dx, dy); }
    
    /**
	 * 判断是否可到达
	 * @param node
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean isWalkableAt(Node node, int x, int y) {
		// 可行走。还可以加上高度差的判断，这里a*数据里没有高度。
		if (mapData.isWalkable(x, y) /*&& 高度差符合*/) {
			return true;
		}
		return false;
	}
	
	/**
	 * from.to是否可达
	 * ------------
	 * | 障碍 | TO |
	 * ------------
	 * |FROM| 障碍 |
	 * ------------
	 * @param from
	 * @param x
	 * @param y
	 * @return
	 */
	protected boolean reachable(Node from, int x, int y) {
		if (!isWalkableAt(from, x, y))
			return false;
		// 横、纵
		if (from.x == x || from.y == y)
			return true;
		// 避免斜线穿墙
		if (!isWalkableAt(from, x, from.y) && !isWalkableAt(from, from.x, y))
			return false;
		return true;
	}
	
	/** 中间计算使用 */
	protected Neighbors neighbors = new Neighbors();
	
	/**
	 * 获取八方向的邻居
	 * @param node
	 * @return
	 */
	protected void getNeighborsAll(Node node) {
		neighbors.clear();
		// left
		if (reachable(node, node.x, node.y-1)) {
			neighbors.add(new Node(node.x, node.y-1));
		}
		// down
		if (reachable(node, node.x+1, node.y)) {
			neighbors.add(new Node(node.x+1, node.y));
		}
		// right
		if (reachable(node, node.x, node.y+1)) {
			neighbors.add(new Node(node.x, node.y+1));
		}
		// up
		if (reachable(node, node.x-1, node.y)) {
			neighbors.add(new Node(node.x-1, node.y));
		}
		// left_up
		if (reachable(node, node.x-1, node.y-1)) {
			neighbors.add(new Node(node.x-1, node.y-1));
		}
		// left_down
		if (reachable(node, node.x+1, node.y-1)) {
			neighbors.add(new Node(node.x+1, node.y-1));
		}
		// right_down
		if (reachable(node, node.x+1, node.y+1)) {
			neighbors.add(new Node(node.x+1, node.y+1));
		}
		// right_up
		if (reachable(node, node.x-1, node.y+1)) {
			neighbors.add(new Node(node.x-1, node.y+1));
		}
	}
	
	/**
	 * Node数组，不清空，标志位
	 *
	 * @author U-Demon
	 */
	protected static class Neighbors {
		public Node[] datas = new Node[8];
		public int size = 0;
		
		public void clear() {
			this.size = 0;
		}
		
		public void deleteAll() {
			this.datas = null;
			this.size = 0;
		}
		
		public void add(Node node) {
			this.datas[size++] = node;
		}
		
		public Node get(int index) {
			return this.datas[index];
		}
	}

}
