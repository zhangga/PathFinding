package com.abc.pathfinding.map;

import java.util.function.BiFunction;

public class DistanceAlgo {
	
	public static final int UNIT = 10;
	public static final int OPPO = 14;
	/** int型简化计算 */
	public static BiFunction<Integer, Integer, Integer> euclidean_int = (dx, dy) -> {
		if (dx == 0) {
			return dy*UNIT;
		}
		else if (dy == 0) {
			return dx*UNIT;
		}
		else if (dx == dy) {
			return dx*OPPO;
		}
		else {
			return (int) Math.sqrt(dx*dx + dy*dy);
		}
    };
    public static BiFunction<Integer, Integer, Integer> manhattan_int = (dx, dy) -> {
        return Math.abs(dx) + Math.abs(dy);
    };

    public static final double SQRT2 = Math.sqrt(2);

    public static final double F = SQRT2 - 1;

    /**
     * 曼哈顿距离，允许斜线走的时候，用
     */
    public static BiFunction<Integer, Integer, Double> manhattan = (dx, dy) -> {
        return (double) (Math.abs(dx) + Math.abs(dy));
    };

    /**
     * 欧几里得距离
     */
    public static BiFunction<Integer, Integer, Double> euclidean = (dx, dy) -> {
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    };

    /**
     * 启发式
     */
    public static BiFunction<Integer, Integer, Double> octile = (dx, dy) -> {
        int adx = Math.abs(dx);
        int ady = Math.abs(dy);
    	return (adx < ady) ? F * adx + ady : F * ady + adx;
    };

    /**
     * 切比雪夫
     */
    public static BiFunction<Integer, Integer, Double> chebyshev = (dx, dy) -> {
        return (double) Math.max(Math.abs(dx), Math.abs(dy));
    };

}
