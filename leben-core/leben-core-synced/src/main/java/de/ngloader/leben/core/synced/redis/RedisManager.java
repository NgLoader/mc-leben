package de.ngloader.leben.core.synced.redis;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.ngloader.leben.core.synced.LebenCoreConfig;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisManager {

	public static byte[] key(UUID uuid) {
		ByteBuffer buffer = ByteBuffer.allocate(16);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		return buffer.array();
	}

	private final JedisPool jedisPool;
	private final ExecutorService executorService = Executors.newWorkStealingPool();

	public RedisManager(LebenCoreConfig config) throws Exception {
		LebenCoreConfig.RedisConfig redisConfig = config.redis;

		this.jedisPool = new JedisPool(
				new JedisPoolConfig(),
				new HostAndPort(redisConfig.host, redisConfig.port),
				DefaultJedisClientConfig.builder()
					.ssl(true)
					.sslSocketFactory(RedisSSLSocketFactory.createSocketFactory(redisConfig))
					.password(redisConfig.password)
					.build());
	}

	public CompletableFuture<String> get(RedisDB db, String key) {
		return this.handle(db, jedis -> jedis.get(key));
	}

	public CompletableFuture<byte[]> get(RedisDB db, byte[] key) {
		return this.handle(db, jedis -> jedis.get(key));
	}

	public CompletableFuture<List<String>> get(RedisDB db, String key, String... fields) {
		return this.handle(db, jedis -> jedis.hmget(key, fields));
	}

	public CompletableFuture<List<byte[]>> get(RedisDB db, byte[] key, byte[]... fields) {
		return this.handle(db, jedis -> jedis.hmget(key, fields));
	}

	public CompletableFuture<String> set(RedisDB db, String key, String value) {
		return this.handle(db, jedis -> jedis.set(key, value));
	}

	public CompletableFuture<String> set(RedisDB db, byte[] key, byte[] value) {
		return this.handle(db, jedis -> jedis.set(key, value));
	}

	public CompletableFuture<String> set(RedisDB db, String key, Map<String, String> value) {
		return this.handle(db, jedis -> jedis.hmset(key, value));
	}

	public CompletableFuture<String> set(RedisDB db, byte[] key, Map<byte[], byte[]> value) {
		return this.handle(db, jedis -> jedis.hmset(key, value));
	}

	public CompletableFuture<Double> increment(RedisDB db, String key, String value, double increment) {
		return this.handle(db, jedis -> jedis.zincrby(key, increment, value));
	}

	public CompletableFuture<Double> increment(RedisDB db, byte[] key, byte[] value, double increment) {
		return this.handle(db, jedis -> jedis.zincrby(key, increment, value));
	}

	public CompletableFuture<Long> publish(RedisDB db, String channel, String message) {
		return this.handle(db, jedis -> jedis.publish(channel, message));
	}

	public CompletableFuture<Long> publish(RedisDB db, byte[] channel, byte[] message) {
		return this.handle(db, jedis -> jedis.publish(channel, message));
	}

	public <T> CompletableFuture<T> handle(RedisDB db, Transformer<Jedis, T> function) {
        CompletableFuture<T> future = new CompletableFuture<>();
        this.executorService.execute(() -> {
            try (Jedis jedis = this.jedisPool.getResource()) {
                jedis.select(db.getId());
                future.complete(function.apply(jedis));
            } catch (Exception e) {
            	Logger.getLogger(RedisManager.class.getSimpleName()).log(Level.SEVERE, e.getMessage(), e);
            	future.completeExceptionally(e);
			}
        });
        return future;
    }

	private interface Transformer<T, V> {
	    V apply(T t) throws Exception;
	}
}