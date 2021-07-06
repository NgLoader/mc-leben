package de.ngloader.leben.core.minecraft.item;

public class CustomItem {

	private final ItemManager itemManager;

	public CustomItem(ItemManager itemManager) {
		this.itemManager = itemManager;
	}

	public ItemManager getItemManager() {
		return this.itemManager;
	}
}