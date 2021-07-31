package de.ngloader.leben.core.synced.redis;

public enum RedisDB {

	PLAYER_DATA(0),
	PLAYER_INFO(1),
	PLAYER_STATS(2),

	CITY_PLAYER(5),
	CITY_INFO(6),
	CITY_STATS(7);

	private final int dbId;

	private RedisDB(int dbId) {
		this.dbId = dbId;
	}

	public int getId() {
		return dbId;
	}
}