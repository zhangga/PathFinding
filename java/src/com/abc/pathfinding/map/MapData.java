package com.abc.pathfinding.map;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 地图数据
 *
 * @author U-Demon
 */
public class MapData {
	
	/** 文件来源于：https://github.com/SteveRabin/JPSPlusWithGoalBounding/blob/master/JPSPlusGoalBounding/Maps/maze-100-1.map */
	private static final String FILE_NAME = "./maps/maze-100-1.map";
	
	/** 阻挡标志量 */
	private static final int OBSTACLE = 0;
	private static final int ACCESS = 1;
	
	private int[][] datas;
	
	private int height;
	private int width;
	
	public MapData() {
		init();
	}
	
	private void init() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(FILE_NAME));
			String line = null;
			int row = -1;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("height")) {
					height = Integer.valueOf(line.split(" ")[1]);
					if (width > 0) datas = new int[height][width];
				}
				else if (line.startsWith("width")) {
					width = Integer.valueOf(line.split(" ")[1]);
					if (height > 0) datas = new int[height][width];
				}
				// map行下是地图数据
				else if (line.trim().toLowerCase().equals("map")) {
					row = 0;
				}
				// 开始加载地图数据
				else if (row >= 0) {
					for (int col = 0; col < line.length(); col++) {
						char ch = line.charAt(col);
						datas[row][col] = ch == '@' ? OBSTACLE : ACCESS;
					}
					row++;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("地图文件错误：读取文件错误。文件:" + FILE_NAME);
		} finally {
			if (br == null)
				return;
			try {
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 是否可行走
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isWalkable(int x, int y) {
		if (x < 0 || x >= height || y < 0 || y >= width)
			return false;
		return datas[x][y] == ACCESS;
	}

	public int[][] getDatas() {
		return datas;
	}
	
}
