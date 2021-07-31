package de.ngloader.leben.core.synced.core.player;

import de.ngloader.leben.core.synced.core.kingdom.KingdomClassType;
import de.ngloader.leben.core.synced.core.kingdom.KingdomType;
import de.ngloader.leben.network.packet.Packet;
import de.ngloader.leben.network.packet.PacketHandler;
import de.ngloader.leben.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class Character implements Packet<PacketHandler> {

	public static Character readCharacter(ByteBuf buffer) {
		Character character = new Character();
		character.read(buffer);
		return character;
	}

	public static void writeCharacter(ByteBuf buffer, Character character) {
		character.write(buffer);
	}

	private String profileName;
	private String username;

	private KingdomType kingdom;
	private KingdomClassType kingdomClass;

	public Character() { }

	@Override
	public void read(ByteBuf buffer) {
		this.profileName = ByteBufUtil.readString(buffer);
		this.username = ByteBufUtil.readString(buffer);
		this.kingdom = ByteBufUtil.readEnum(buffer, KingdomType.class);
		this.kingdomClass = ByteBufUtil.readEnum(buffer, KingdomClassType.class);
	}

	@Override
	public void write(ByteBuf buffer) {
		ByteBufUtil.writeString(buffer, this.profileName);
		ByteBufUtil.writeString(buffer, this.username);
		ByteBufUtil.writeEnum(buffer, this.kingdom);
		ByteBufUtil.writeEnum(buffer, this.kingdomClass);
	}

	public KingdomType getKingdom() {
		return this.kingdom;
	}

	public KingdomClassType getKingdomClass() {
		return this.kingdomClass;
	}

	public void setUsername(String name) {
		this.username = name;
	}

	public String getUsername() {
		return this.username;
	}

	public String getProfileName() {
		return this.profileName;
	}
}