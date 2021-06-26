package de.ngloader.leben.minecraft.test;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

public class Test extends JavaPlugin implements Listener {

	public List<Location> locations = new ArrayList<>();

	public BukkitTask task;

	public int index = 0;
	public Location last = null;

	@Override
	public void onEnable() {
		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		this.task = Bukkit.getScheduler().runTaskTimer(this, () -> {
			if (locations.isEmpty()) {
				return;
			}

			if (index < this.locations.size()) {
				Location current = this.locations.get(index++);
				if (last == null) {
					last = current;
					return;
				}

				this.drawLine(last, current, 0.15);
				last = current;
			} else {
				index = 0;
			}
		}, 5, 5);
	}

	@Override
	public void onDisable() {
		this.task.cancel();
	}

	private Set<TestBox> display = new HashSet<>();

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if ((event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_BLOCK)
				&& event.getClickedBlock() != null
				&& event.getItem() != null) {
			if (event.getItem().getType() == Material.STICK) {
				event.setCancelled(true);
				Location location = event.getClickedBlock().getLocation();
				locations.add(location);
				location.getWorld().spawnParticle(Particle.BARRIER, location.add(.5, .5, .5), 1);
			} else if (event.getItem().getType() == Material.CARROT_ON_A_STICK) {
				event.setCancelled(true);
				Location[] locations = new Location[this.locations.size()];
				for (int i = 0; i < this.locations.size(); i++) {
					locations[i] = this.locations.get(i);
				}
				Location check = event.getPlayer().getLocation();

				int intersections = 0;
				Location prev = locations[locations.length - 1];
				for (Location next : locations) {
					if ((prev.getBlockZ() <= check.getBlockZ() && check.getBlockZ() < next.getBlockZ()) || (prev.getBlockZ() >= check.getBlockZ() && check.getBlockZ() > next.getBlockZ())) {
						int dz = next.getBlockZ() - prev.getBlockZ();
						int dx = next.getBlockX() - prev.getBlockX();
						int x = (check.getBlockZ() - prev.getBlockZ()) / dz * dx + prev.getBlockX();
						if (x > check.getBlockX()) {
							intersections++;
						}
					}
					prev = check;
				}
				if (intersections % 2 == 1) {
					check.getWorld().spawnParticle(Particle.BARRIER, check.add(.5, .5, .5), 1);
				} else {
					check.getWorld().spawnParticle(Particle.CLOUD, check.add(.5, .5, .5), 1);
				}
			} else if (event.getItem().getType() == Material.BEDROCK) {
				event.setCancelled(true);
				long timems = System.currentTimeMillis();
				Location location = event.getClickedBlock().getLocation().add(0, 1, 0);
				int x = location.getBlockX();
				int y = location.getBlockY();
				int z = location.getBlockZ();

				int i = 0;
				boolean found = false;
				while (i < 10000) {
					i++;
					for (TestBox test : this.display) {
						if (test.contains(x, y, z)) {
							found = true;
							break;
						}
					}
				}

				if (found) {
					location.getWorld().spawnParticle(Particle.BARRIER, location.clone().add(.5, .5, .5), 1);
				} else {
					location.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, location.clone().add(.5, .5, .5), 1);
				}
				Bukkit.broadcastMessage("Executed in: " + (System.currentTimeMillis() - timems) + "ms | Found: " + found);
			} else if (event.getItem().getType() == Material.NOTE_BLOCK) {
				event.setCancelled(true);
				
				Location location = event.getClickedBlock().getLocation().add(0, 1, 0);
				int[] box = this.getBox(location);
				for (int y = 0; y < box[1]; y++) {
					for (int x = 0; x < box[0]; x++) {
						for (int z = 0; z < box[2]; z++) {
							location.getWorld().spawnParticle(Particle.BARRIER, location.clone().add(x, y, z).add(.5, .5, .5), 1);
						}
					}
				}
			} else if (event.getItem().getType() == Material.NETHERITE_BLOCK) {
				event.setCancelled(true);

				Location location = event.getClickedBlock().getLocation().add(0, 1, 0);
				Set<Location> locations = new HashSet<>();
				this.getBlocks(locations, location);
				this.locations.forEach(test -> test.getWorld().spawnParticle(Particle.BARRIER, test.clone().add(.5, .5, .5), 1));

				Bukkit.broadcastMessage("Finished!");
				int maxSteps = locations.size();
				new Thread(() -> {
					while (!locations.isEmpty()) {
						Bukkit.broadcastMessage("Step: " + maxSteps + "/" + locations.size());
						Location check = locations.stream()
								.sorted((loc1, loc2) -> Integer.compare(loc1.getBlockX(), loc2.getBlockX()))
								.sorted((loc1, loc2) -> Integer.compare(loc1.getBlockZ(), loc2.getBlockZ()))
								.min((loc1, loc2) -> Integer.compare(loc1.getBlockY(), loc2.getBlockY())).get();
						int[] box = this.getBox(check);

						Set<Location> test = new HashSet<>();
						for (int y = 0; y < box[1]; y++) {
							for (int x = 0; x < box[0]; x++) {
								for (int z = 0; z < box[2]; z++) {
									Location current = check.clone().add(x, y, z);
									locations.remove(current);
									test.add(current);
									current.getWorld().spawnParticle(Particle.NOTE, current.clone().add(.5, .5, .5), 1);
								}
							}
						}
						display.add(new TestBox(check.getBlockX(), check.getBlockY(), check.getBlockZ(), check.getBlockX() + box[0], check.getBlockY() + box[1], check.getBlockZ() + box[2]));
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

//					Random random = new Random();
//					for (Set<Location> dis : display) {
//						int r = random.nextInt(256);
//						int g = random.nextInt(256);
//						int b = random.nextInt(256);
//
//						Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
//
//							int steps = 0;
//
//							@Override
//							public void run() {
//								if (steps++ > 10 * 60 * 20) {
//									Bukkit.getScheduler().cancelTasks(Test.this);
//								}
//
//								for (Location loc : dis) {
//									Location l = loc.clone().add(.5, .5, .5);
//									Particle.DustOptions dust = new Particle.DustOptions(Color.fromRGB(r, g, b), 1);
//									location.getWorld().spawnParticle(Particle.REDSTONE, l.getX(), l.getY(), l.getZ(), 0, 0, 0, 0, dust);
//								}
//							}
//						}, 0, 10);
//					}
				}).start();
			}
		}
	}

	public int[] getBox(Location location) {
		int[] result = this.checkSide(location, new int[] { 0, 0, 0 }, new int[] { 1, 1, 1 }, false, 20);
		result = this.checkSide(location, result, new int[] { 1, 0, 0 }, true, 10);
		result = this.checkSide(location, result, new int[] { 0, 0, 1 }, true, 10);
		result = this.checkSide(location, result, new int[] { 0, 1, 0 }, true, 10);
		return result;
	}

	public int[] checkSide(Location location, int[] size, int[] add, boolean checkSide, int maxDeep) {
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

	private EnumSet<BlockFace> faces = EnumSet.of(BlockFace.DOWN, BlockFace.UP, BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);

	public void getBlocks(Set<Location> locations, Location start) {
		if (locations.size() > 5000) {
			return;
		}

		for (BlockFace face : faces) {
			Location check = start.clone().add(face.getDirection());
			if (check.getBlock().getType() == Material.AIR && !locations.contains(check)) {
				locations.add(check);

				getBlocks(locations, check);
			}
		}
	}

	public void drawLine(Location point1, Location point2, double space) {
		World world = point1.getWorld();
		double distance = point1.distance(point2);
		Vector p1 = point1.toVector();
		Vector p2 = point2.toVector();
		Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
		double length = 0;
		for (; length < distance; p1.add(vector)) {
			world.spawnParticle(Particle.FIREWORKS_SPARK, p1.getX(), p1.getY(), p1.getZ(), 1, 0, 0, 0, 0);
			length += space;
		}
	}
}