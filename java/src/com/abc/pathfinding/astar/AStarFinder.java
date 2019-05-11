package com.abc.pathfinding.astar;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import com.abc.pathfinding.Finder;
import com.abc.pathfinding.map.DistanceAlgo;
import com.abc.pathfinding.map.MapData;
import com.abc.pathfinding.map.Node;

/**
 * A*寻路
 * 性能测试：
 *  	次数		距离公式		启发函数		耗时ms	close大小		备注
 * 		10000	直接获得		manhattan	6000	1641		[1,39]->[99,65] 大概纵100*横25的距离，地形复杂，寻路节点80个。
 *
 * @author U-Demon
 */
public class AStarFinder extends Finder {
	
	private PriorityQueue<Node> openList = new PriorityQueue<>(new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return Double.compare(o1.f, o2.f);
		}
	});
	
	private Set<Integer> closed = new HashSet<>();

	public AStarFinder(MapData mapData) {
		super(mapData);
	}

	@Override
	public List<Node> findPath(Node start, Node end) {
		openList.add(start);
		while (!openList.isEmpty()) {
			Node node = openList.poll();
			// 找打路径
			if (node.equals(end)) {
				System.out.println(closed.size());
				return backtrace(node);
			}
			
			closed.add(node.hashCode);
			
			// 遍历八方向
			getNeighborsAll(node);
			for (int i = 0; i < neighbors.size; i++) {
				Node neighbor = neighbors.get(i);
				if (closed.contains(neighbor.hashCode)) {
					continue;
				}
				
				double ng = node.g + ((neighbor.x == node.x || neighbor.y == node.y) ? 1 : DistanceAlgo.SQRT2);
				if (!openList.contains(neighbor) || ng < neighbor.g) {
					neighbor.g = ng;
					double h = heuristic.apply(neighbor.x-end.x, neighbor.y-end.y);
					neighbor.f = neighbor.g + h;
					neighbor.pre = node;
					if (!openList.contains(neighbor)) {						
						openList.add(neighbor);
					}
				}
			}
		}
		clear();
		return null;
	}
	
	/**
	 * 构造路径，需要优化合并
	 * @param node
	 * @return
	 */
	private List<Node> backtrace(Node node) {
		if (node == null) {
			return null;
		}
		// 上一步的方向，方向相同的点，合并
		int pre_dx = 0, pre_dy = 0;
		// 路径
		var path = new LinkedList<Node>();
		path.addFirst(node);
		while (node.pre != null) {
			int dx = node.x - node.pre.x;
			int dy = node.y - node.pre.y;
			// 方向相同
			if (dx == pre_dx && dy == pre_dy) {
				node = node.pre;
				continue;
			}
			pre_dx = dx;
			pre_dy = dy;
			path.addFirst(node.pre);
			node = node.pre;
		}
		// 加入起点
		path.addFirst(node);
		clear();
		return path;
	}
	
	private void clear() {
		openList.clear();
		closed.clear();
	}
	
	private void calcF(Node curr, Node end) {
		curr.f = curr.g + getHeuristic(end.x - curr.x, end.y - curr.y);
	}

}
