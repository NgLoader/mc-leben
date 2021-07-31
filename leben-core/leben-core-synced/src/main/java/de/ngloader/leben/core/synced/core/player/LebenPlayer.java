package de.ngloader.leben.core.synced.core.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import de.ngloader.leben.network.packet.Packet;
import de.ngloader.leben.network.packet.PacketHandler;
import de.ngloader.leben.network.util.ByteBufUtil;
import io.netty.buffer.ByteBuf;

public class LebenPlayer implements Packet<PacketHandler> {

	private UUID uuid;

	private List<Character> profiles = new ArrayList<>();
	private Character activeProfile = null;

	public LebenPlayer() { }

	@Override
	public void read(ByteBuf buffer) {
		this.uuid = ByteBufUtil.readUUID(buffer);
		this.profiles = ByteBufUtil.readList(buffer, Character::readCharacter);

		int activeProfile = buffer.readInt();
		this.activeProfile = activeProfile != this.profiles.size() ? this.profiles.get(activeProfile) : null;
	}

	@Override
	public void write(ByteBuf buffer) {
		ByteBufUtil.writeUUID(buffer, this.uuid);
		ByteBufUtil.writeList(buffer, this.profiles, Character::writeCharacter);
		buffer.writeInt(this.activeProfile != null ? this.profiles.indexOf(activeProfile) : this.profiles.size());
	}

	public void addProfile(Character character, boolean active) {
		this.profiles.add(character);
		if (active) {
			this.setActiveProfile(character);
		}
	}

	public void removeProfile(Character character) {
		this.profiles.remove(character);

		if (this.activeProfile.equals(character)) {
			this.setActiveProfile(null);
		}
	}

	public List<Character> getProfiles() {
		return Collections.unmodifiableList(this.profiles);
	}

	public void setActiveProfile(Character activeProfile) {
		this.activeProfile = activeProfile;
	}

	public Character getActiveProfile() {
		return this.activeProfile;
	}

	public UUID getId() {
		return this.uuid;
	}
}