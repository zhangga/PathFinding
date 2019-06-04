package com.abc.pathfinding.jps;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;

import com.abc.pathfinding.Finder;
import com.abc.pathfinding.map.MapData;
import com.abc.pathfinding.map.Node;

/**
 * JPS寻路
 * 算法解析：https://zhuanlan.zhihu.com/p/25093275
 * 性能测试：
 *  	次数		距离公式		启发函数		耗时ms	close大小		备注
 * 		10000	octile		manhattan	1600	419			[1,39]->[99,65] 大概纵100*横25的距离，地形复杂，寻路节点80个。
 *
 * @author U-Demon
 */
public class JPSFinder extends Finder {
	
	// 使用优先级队列
	private PriorityQueue<Node> openList = new PriorityQueue<>(new Comparator<Node>() {
		@Override
		public int compare(Node o1, Node o2) {
			return Double.compare(o1.f, o2.f);
		}
	});
	// 使用TreeSet
//	private TreeSet<Node> openList = new TreeSet<>();
	
	private Set<Integer> closed = new HashSet<>();

	public JPSFinder(MapData mapData) {
		super(mapData);
	}

	@Override
	public List<Node> findPath(Node start, Node end) {
		closed.clear();
		neighbors.clear();
		calcF(start, end);
		// 将起点加入openList
		openList.add(start);
		
		Node top = null;
		while (!openList.isEmpty()) {
			// 将openList中f值最小的节点弹出
			// 优先级队列api
			top = openList.poll();
			// TreeSet api
//			top = openList.first();
//			openList.remove(top);
			
			closed.add(top.hashCode());
			
			// 到达终点
			if (top.equals(end)) {
				return backtrace(top);
			}
			// add all possible next steps from the current node
			identifySuccessors(top, end);
		}
		// failed to find a path
		return null;
	}
	
	/**
     * Identify successors for the given node. Runs a JPS in direction of each available neighbor, adding any open
     * nodes found to the open list.
     * @return All the nodes we have found jumpable from the current node
     */
	private void identifySuccessors(Node node, Node end) {
		// 获取当前节点的有效邻接点
		findNeighbors(node);
		
		double new_jump_g = 0;
  		for (int i = 0; i < neighbors.size; i++) {
 			Node neighbor = neighbors.get(i);
			
			// jump in the direction of our neighbor
			Node jumpNode = jump(neighbor, node, end);
			
			// don't add a node we have already gotten to quicker
			if (jumpNode == null || closed.contains(jumpNode.hashCode()))
				continue;
			// arrived
			if (jumpNode.equals(end)) {
				jumpNode.pre = node;
				jumpNode.f = -1;
				openList.add(jumpNode);
				return;
			}
			
			new_jump_g = node.g + getDistance(jumpNode.x-node.x, jumpNode.y-node.y);
			
			if (!openList.contains(jumpNode) || new_jump_g < jumpNode.g) {
				jumpNode.pre = node;
				jumpNode.g = new_jump_g;
				calcF(jumpNode, end);
				// 移除不移除都可以，有close表
				if (openList.contains(jumpNode)) {					
					openList.remove(jumpNode);
				}
				openList.add(jumpNode);
			}
		}
	}
	
	private Node jump(Node neighbor, Node current, Node end) {
		// 一定可达
		if (neighbor == null)
			return null;
		if (neighbor.equals(end))
			return neighbor;
		
		int dx = neighbor.x - current.x;
		int dy = neighbor.y - current.y;
		// 检查强制邻接点
		// 检查斜线
		if (dx != 0 && dy != 0) {
			// 与横、纵向探测情况相同，在当前节点（neighbor）的左（或下）侧探测到障碍时，无法直接从母节点（current）纵向走至母节点的上上格（横向走至母节点的右右格），
            // 而是需先经过当前节点再沿斜对角走至上上格（右右格），这样的障碍也被称为强制邻点。
			if ((isWalkableAt(neighbor, neighbor.x-dx, neighbor.y+dy) 		/**x垂反y水正 可行走*/
					&& !isWalkableAt(neighbor, neighbor.x-dx, neighbor.y) 	/**x垂反y不变 不可行走*/)
				|| (isWalkableAt(neighbor, neighbor.x+dx, neighbor.y-dy) 	/**x垂正y水反 可行走*/
					&& !isWalkableAt(neighbor, neighbor.x, neighbor.y-dy)	/**x不变y水反 不可行走*/)
				) {
				return neighbor;
			}
			// 斜线移动时，必须检查垂直/水平的跳点
			// 垂直
			if (isWalkableAt(neighbor, neighbor.x+dx, neighbor.y)) {
				if (jump(new Node(neighbor.x+dx, neighbor.y), neighbor, end) != null) {
					return neighbor;
				}
			}
			// 水平
			if (isWalkableAt(neighbor, neighbor.x, neighbor.y+dy)) {
				if (jump(new Node(neighbor.x, neighbor.y+dy), neighbor, end) != null) {
					return neighbor;
				}
			}
		}
		// 检查垂直方向
		else if (dx != 0) {
			// 在当前节点（neighbor）的上（或下）侧探测到障碍时，如果斜上（或斜下）可行走，这样的障碍被称为强制邻点。
			if ((isWalkableAt(neighbor, neighbor.x+dx, neighbor.y+1) 		/**斜右上(斜右下) 可行走*/
					&& !isWalkableAt(neighbor, neighbor.x, neighbor.y+1) 	/**水平右 不可行走*/)
				|| (isWalkableAt(neighbor, neighbor.x+dx, neighbor.y-1) 	/**斜左上(斜左下) 可行走*/
					&& !isWalkableAt(neighbor, neighbor.x, neighbor.y-1)	/**水平左 不可行走*/)
				) {
				return neighbor;
			}
		}
		// 检查水平方向
		else {
			// 与纵向探测情况相同，在当前节点（neighbor）的左（或右）侧探测到障碍时，如果斜左（或斜右）可行走，这样的障碍被称为强制邻点。
			if ((isWalkableAt(neighbor, neighbor.x+1, neighbor.y+dy) 		/**斜左下(斜右下) 可行走*/
					&& !isWalkableAt(neighbor, neighbor.x+1, neighbor.y) 	/**垂直下 不可行走*/)
				|| (isWalkableAt(neighbor, neighbor.x-1, neighbor.y+dy) 	/**斜左上(斜右上) 可行走*/
					&& !isWalkableAt(neighbor, neighbor.x-1, neighbor.y)	/**垂直上 不可行走*/)
				) {
				return neighbor;
			}
		}
		
		// 超目标方向检查跳点
		if (!isWalkableAt(neighbor, neighbor.x+dx, neighbor.y+dy)) {
			return null;
		}
		return jump(new Node(neighbor.x+dx, neighbor.y+dy), neighbor, end);
	}
	
	/**
	 * 查找强制邻点（Forced Neighbor）
	 * @param node
	 * @param pre
	 */
	private void findNeighbors(Node node) {
		neighbors.clear();
		// 没有前节点的时候，直接返回八方向
		if (node.pre == null) {
			getNeighborsAll(node);
			return;
		}
		// 有前节点的时候，忽略劣质节点
		int dx = 0;
		if (node.x > node.pre.x) dx = 1;
		else if (node.x < node.pre.x) dx = -1;
		int dy = 0;
		if (node.y > node.pre.y) dy = 1;
		else if (node.y < node.pre.y) dy = -1;
		// 斜线搜索
		if (dx != 0 && dy != 0) {
			// 斜线的水平分量
			if (reachable(node, node.x, node.y+dy)) {
				neighbors.add(new Node(node.x, node.y+dy));
			}
			// 斜线的垂直分量
			if (reachable(node, node.x+dx, node.y)) {
				neighbors.add(new Node(node.x+dx, node.y));
			}
			// 斜线同方向
			if (reachable(node, node.x+dx, node.y+dy)) {
				neighbors.add(new Node(node.x+dx, node.y+dy));
			}
			// 斜线的垂直反方向
			if (!reachable(node, node.x-dx, node.y)) {
				// 斜线的垂直反方向和水平正方向(例:斜线为↗时，该点为↘右下点)
				if (reachable(node, node.x-dx, node.y+dy)) {					
					neighbors.add(new Node(node.x-dx, node.y+dy));
				}
			}
			// 斜线的水平反方向
			if (!reachable(node, node.x, node.y-dy)) {
				// 斜线的垂直正方向和水平反方向(例:斜线为↗时，该点为↖左上点)
				if (reachable(node, node.x+dx, node.y-dy)) {					
					neighbors.add(new Node(node.x+dx, node.y-dy));
				}
			}
		}
		// 垂直水平搜索
		else {
			// 水平
			if (dx == 0) {
				// 水平和原来同方向
				if (reachable(node, node.x, node.y+dy)) {
					neighbors.add(new Node(node.x, node.y+dy));
				}
				// 下面阻挡
				if (!reachable(node, node.x+1, node.y)) {
					// 和原来水平同方向的斜下
					if (reachable(node, node.x+1, node.y+dy)) {						
						neighbors.add(new Node(node.x+1, node.y+dy));
					}
				}
				// 上面阻挡
				if (!reachable(node, node.x-1, node.y)) {
					// 和原来水平同方向的斜上
					if (reachable(node, node.x-1, node.y+dy)) {						
						neighbors.add(new Node(node.x-1, node.y+dy));
					}
				}
			}
			// 垂直
			else {
				// 垂直和原来同方向
				if (reachable(node, node.x+dx, node.y)) {
					neighbors.add(new Node(node.x+dx, node.y));
				}
				// 右面阻挡
				if (!reachable(node, node.x, node.y+1)) {
					// 和原来垂直同方向的偏右
					if (reachable(node, node.x+dx, node.y+1)) {						
						neighbors.add(new Node(node.x+dx, node.y+1));
					}
				}
				// 左面阻挡
				if (!reachable(node, node.x, node.y-1)) {
					// 和原来垂直同方向的偏左
					if (reachable(node, node.x+dx, node.y-1)) {						
						neighbors.add(new Node(node.x+dx, node.y-1));
					}
				}
			}
		}
	}
	
	private void calcF(Node curr, Node end) {
		curr.f = curr.g + getHeuristic(end.x - curr.x, end.y - curr.y);
	}
	
	private void calcG(Node curr) {
		if (curr.pre == null) {
			curr.g = 0;
		}
		else {
			curr.g = getDistance(curr.x - curr.pre.x, curr.y - curr.pre.y) + curr.pre.g;
		}
	}
	
	private List<Node> backtrace(Node node) {
		if (node == null)
			return null;
		LinkedList<Node> paths = new LinkedList<>();
		paths.addFirst(node);
		while (node.pre != null) {
			paths.addFirst(node.pre);
			node = node.pre;
		}
		return paths;
	}

}
