package de.ngloader.leben.core.minecraft.util;

import org.bukkit.Location;
import org.bukkit.Material;

public class MathUtil {

	/**
	 * Calculating a box
	 * from x min to x max
	 * from y min to y max
	 * from z min to z max
	 * 
	 * @param location
	 * @return
	 */
	public static int[] getBox(Location location) {
		int[] result = checkSide(location, new int[] { 0, 0, 0 }, new int[] { 1, 1, 1 }, false, 20);
		result = checkSide(location, result, new int[] { 1, 0, 0 }, true, 10);
		result = checkSide(location, result, new int[] { 0, 0, 1 }, true, 10);
		result = checkSide(location, result, new int[] { 0, 1, 0 }, true, 10);
		return result;
	}

	public static int[] checkSide(Location location, int[] size, int[] add, boolean checkSide, int maxDeep) {
		boolean running = true;
		int deep = 0;
		while (running) {
			if (deep++ > maxDeep) {
				running = false;
			}

			for (int y = checkSide ? add[1] != 0 ? size[1] - 1 : 0 : 0; y < size[1] && running; y++) {
				for (int x = checkSide ? add[0] != 0 ? size[0] - 1 : 0 : 0; x < size[0] && running; x++) {
					for (int z = checkSide ? add[2] != 0 ? size[2] - 1 : 0 : 0; z < size[2]; z++) {
						Location check = location.clone().add(x, y, z);
						Material material = check.getBlock().getType();
						if (material != Material.AIR) {
							running = false;
							break;
						}
					}
				}
			}

			for (int i = 0; i < size.length; i++) {
				if (add[i] != 0) {
					if (running) {
						size[i]++;
					} else {
						size[i]--;
					}
				}
			}
		}
		return size;
	}
}
