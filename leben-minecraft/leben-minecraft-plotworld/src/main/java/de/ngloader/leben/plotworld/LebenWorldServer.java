package de.ngloader.leben.plotworld;

import java.util.List;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import org.bukkit.World.Environment;
import org.bukkit.entity.HumanEntity;

import net.minecraft.server.v1_16_R3.Block;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Chunk;
import net.minecraft.server.v1_16_R3.ChunkGenerator;
import net.minecraft.server.v1_16_R3.ChunkStatus;
import net.minecraft.server.v1_16_R3.Convertable.ConversionSession;
import net.minecraft.server.v1_16_R3.DimensionManager;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.IBlockData;
import net.minecraft.server.v1_16_R3.IChunkAccess;
import net.minecraft.server.v1_16_R3.IProgressUpdate;
import net.minecraft.server.v1_16_R3.IWorldDataServer;
import net.minecraft.server.v1_16_R3.MinecraftServer;
import net.minecraft.server.v1_16_R3.MobSpawner;
import net.minecraft.server.v1_16_R3.ResourceKey;
import net.minecraft.server.v1_16_R3.World;
import net.minecraft.server.v1_16_R3.WorldLoadListener;
import net.minecraft.server.v1_16_R3.WorldServer;

public class LebenWorldServer extends WorldServer {

	public LebenWorldServer(MinecraftServer minecraftserver, Executor executor,
			ConversionSession convertable_conversionsession, IWorldDataServer iworlddataserver,
			ResourceKey<World> resourcekey, DimensionManager dimensionmanager, WorldLoadListener worldloadlistener,
			ChunkGenerator chunkgenerator, boolean flag, long i, List<MobSpawner> list, boolean flag1, Environment env,
			org.bukkit.generator.ChunkGenerator gen) {
		super(minecraftserver, executor, convertable_conversionsession, iworlddataserver, resourcekey, dimensionmanager,
				worldloadlistener, chunkgenerator, flag, i, list, flag1, env, gen);
	}

	/*
	 * Need to look addEntity0 addPlayer0 addEntityChunk
	 */

//	@Override
//	public void entityJoinedWorld(Entity entity) {
//		//CraftServer
//		if (entity instanceof HumanEntity) {
//			entity.tickTimer.startTiming();
//
//			entity.g(entity.locX(), entity.locY(), entity.locZ());
//			entity.lastYaw = entity.yaw;
//			entity.lastPitch = entity.pitch;
//
//			entity.tick();
//			entity.postTick();
//
//			// TODO CHECK chunks!
//
//			entity.tickTimer.stopTiming();
//		} else {
//			// TODO handle normal entity
//		}
//	}
//
//	@Override
//	public void chunkCheck(Entity entity) {
//	}
//
//	@Override
//	public void playBlockAction(BlockPosition blockposition, Block block, int i, int j) {
//		super.playBlockAction(blockposition, block, i, j);
//	}
//
//	@Override
//	public void notifyAndUpdatePhysics(BlockPosition blockposition, Chunk chunk, IBlockData oldBlock,
//			IBlockData newBlock, IBlockData actualBlock, int i, int j) {
//	}
//
//	/**
//	 * Disable world save
//	 */
//	@Override
//	public void save(@Nullable IProgressUpdate iprogressupdate, boolean flag, boolean flag1) {
//	}
//
//	/**
//	 * Disable chunk update by WorldServer.addPlayer0
//	 */
//	@Override
//	public IChunkAccess getChunkAt(int i, int j, ChunkStatus chunkstatus, boolean flag) {
//		return null;
//	}
}