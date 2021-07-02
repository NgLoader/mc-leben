package de.ngloader.leben.core.synced.redis;

public enum RedisDB {

	PLAYER_INFO(5),
	PLAYER_STATS(6),
	PLAYER_DATA(7),

	CITY_INFO(10),
	CITY_STATS(11),
	CITY_PLAYER(12);

	private final int dbId;

	private RedisDB(int dbId) {
		this.dbId = dbId;
	}

	public int getId() {
		return dbId;
	}
}