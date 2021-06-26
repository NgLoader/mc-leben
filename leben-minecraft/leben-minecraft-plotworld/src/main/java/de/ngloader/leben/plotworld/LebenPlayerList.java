package de.ngloader.leben.plotworld;

import net.minecraft.server.v1_16_R3.DedicatedPlayerList;
import net.minecraft.server.v1_16_R3.DedicatedServer;
import net.minecraft.server.v1_16_R3.IRegistryCustom.Dimension;
import net.minecraft.server.v1_16_R3.WorldNBTStorage;

public class LebenPlayerList extends DedicatedPlayerList {

	public LebenPlayerList(DedicatedServer var0, Dimension var1, WorldNBTStorage var2) {
		super(var0, var1, var2);
	}
}
