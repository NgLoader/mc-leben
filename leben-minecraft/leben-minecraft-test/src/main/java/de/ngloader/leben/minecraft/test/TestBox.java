package de.ngloader.leben.minecraft.test;

public class TestBox {

	public int startX;
	public int startY;
	public int startZ;

	public int endX;
	public int endY;
	public int endZ;

	public TestBox(int startX, int startY, int startZ, int endX, int endY, int endZ) {
		this.startX = startX - 1;
		this.startY = startY - 1;
		this.startZ = startZ - 1;
		this.endX = endX;
		this.endY = endY;
		this.endZ = endZ;
	}

	public boolean contains(int x, int y, int z) {
		return this.startX < x && this.endX > x && this.startY < y && this.endY > y && this.startZ < z && this.endZ > z;
	}
}