package de.ngloader.leben.minecraft.cityworld.creator;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.imageio.ImageIO;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.bukkit.map.MapView.Scale;
import org.bukkit.map.MinecraftFont;
import org.bukkit.plugin.PluginManager;

import de.ngloader.leben.core.minecraft.player.LebenPlayer;
import de.ngloader.leben.core.minecraft.util.ItemFactory;
import de.ngloader.leben.core.synced.player.Character;
import de.ngloader.leben.minecraft.cityworld.CityWorld;
import de.ngloader.npcsystem.NPCPlugin;
import de.ngloader.npcsystem.NPCRegistry;
import de.ngloader.npcsystem.runner.NPCRunnerType;

public class CharacterCreator implements Listener {

	private static final List<Class<? extends Step>> STEPS = new LinkedList<>();

	private static final Set<Class<? extends Event>> EVENT_CANCELLED = Collections
			.newSetFromMap(new ConcurrentHashMap<>());
	private static final Map<Class<? extends Event>, Function<Event, Entity>> EVENT_GET_ENTITY = new ConcurrentHashMap<>();

	private static final ItemFactory ITEM_CLOSE = new ItemFactory(Material.BARRIER).addAllFlag();
	private static final ItemFactory ITEM_BACK = new ItemFactory(Material.OAK_DOOR).addAllFlag();
	private static final ItemFactory ITEM_MAP = new ItemFactory(Material.FILLED_MAP).addAllFlag();

	static final NPCRegistry NPC_REGISTRY = NPCPlugin.getInstance().createRegestry();

	static {
		STEPS.add(TypeStep.class);

		addCancelledEvent(BlockBreakEvent.class, "getPlayer");
		addCancelledEvent(BlockPlaceEvent.class, "getPlayer");
		addCancelledEvent(PlayerDropItemEvent.class, PlayerEvent.class, "getPlayer");
		addCancelledEvent(InventoryClickEvent.class, InventoryInteractEvent.class, "getWhoClicked");
		addCancelledEvent(FoodLevelChangeEvent.class, EntityEvent.class, "getEntity");

		NPC_REGISTRY.getRunnerManager().addRunner(NPCRunnerType.DISTANCE_CHECK);
		NPC_REGISTRY.getRunnerManager().addRunner(NPCRunnerType.LOOK);
		NPC_REGISTRY.getRunnerManager().startRunner();
	}

	private static void addCancelledEvent(Class<? extends Event> eventClass, String getEntityMethod) {
		addCancelledEvent(eventClass, eventClass, getEntityMethod);
	}

	private static void addCancelledEvent(Class<? extends Event> eventClass, Class<? extends Event> getEntityClass,
			String getEntityMethod) {
		if (!Cancellable.class.isAssignableFrom(eventClass)) {
			return;
		}

		try {
			Method method = getEntityClass.getDeclaredMethod(getEntityMethod);
			EVENT_CANCELLED.add(eventClass);
			EVENT_GET_ENTITY.put(eventClass, (event) -> {
				try {
					return (Entity) method.invoke(event);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
				return null;
			});
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	final CityWorld plugin;
	final LebenPlayer player;

	private final Consumer<Character> finish;
	private boolean finished = false;

	private Step step;
	private int currentStep = -1;

	Character character = new Character();

	public CharacterCreator(CityWorld plugin, LebenPlayer player, Consumer<Character> finish) {
		this.plugin = plugin;
		this.player = player;
		this.finish = finish;

		PluginManager pluginManager = Bukkit.getPluginManager();
		pluginManager.registerEvents(this, this.plugin);

		for (Class<? extends Event> event : EVENT_CANCELLED) {
			pluginManager.registerEvent(event, this, EventPriority.NORMAL, this::onDisableEvent, this.plugin, true);
		}

		this.nextStep();

		PlayerInventory inventory = this.player.getInventory();
		inventory.clear();

		MapView view = Bukkit.createMap(player.getWorld());
		view.setScale(Scale.FARTHEST);
		view.setLocked(true);
		view.setUnlimitedTracking(false);
		view.setTrackingPosition(false);
		view.getRenderers().forEach(view.getRenderers()::remove);
		try {
			view.addRenderer(new MapRenderer() {

				BufferedImage image = MapPalette.resizeImage(ImageIO.read(CharacterCreator.this.plugin.getResource("image/test.png")));

				@Override
				public void render(MapView var1, MapCanvas var2, Player var3) {
					var2.drawImage(0, 0, image);
					var2.drawText(0, 20, MinecraftFont.Font, "§4;Moin xD");
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}

		inventory.setItem(4, ITEM_MAP.clone().setDisplayName("§aTranslate this NILS! (Info)").setMap(view).build());
		inventory.setItem(7, ITEM_BACK.clone().setDisplayName("§aTranslate this NILS! (Back)").build());
		inventory.setItem(8, ITEM_CLOSE.clone().setDisplayName("§aTranslate this NILS! (Close)").build());
	}

	public void onDisableEvent(Listener listener, Event event) {
		Function<Event, Entity> getEntity = EVENT_GET_ENTITY.get(event.getClass());
		if (getEntity != null) {
			Entity entity = getEntity.apply(event);
			if (entity != null && entity.getUniqueId().equals(this.player.getUniqueId())) {
				((Cancellable) event).setCancelled(true);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (!event.getEntity().getUniqueId().equals(this.player.getUniqueId())) {
			return;
		}

		if (event.getCause() == DamageCause.VOID) {
			this.step.teleportToStartPoint();
		}
		event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (!event.getPlayer().getUniqueId().equals(this.player.getUniqueId())) {
			return;
		}

		event.setCancelled(true);

		if (event.getItem() != null
				&& (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
			switch (event.getItem().getType()) {

			case OAK_DOOR:
				this.previousStep();
				return;

			case BARRIER:
				this.finish(null);
				return;

			default:
				return;
			}
		}
	}

	void previousStep() {
		if (this.currentStep > 0) {
			this.currentStep--;
		} else {
			return;
		}

		this.updateStep();
	}

	void nextStep() {
		this.currentStep++;

		if (this.currentStep > STEPS.size()) {
			this.finish(this.character);
			return;
		}

		this.updateStep();
	}

	private void updateStep() {
		try {
			if (this.step != null) {
				this.step.destroy();
			}

			this.step = STEPS.get(this.currentStep).getConstructor(CharacterCreator.class).newInstance(this);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
			this.finish(null);
		}
	}

	private void finish(Character character) {
		if (this.finished) {
			return;
		}
		this.finished = true;

		try {
			this.finish.accept(character);
		} finally {
			this.destroy();
		}
	}

	public void destroy() {
		HandlerList.unregisterAll(this);

		if (this.step != null) {
			this.step.destroy();
		}

		this.step = null;
		this.character = null;
	}
}
