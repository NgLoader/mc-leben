package de.ngloader.leben.core.minecraft.util;

public class DoubleConsumer<T, S> {

	private T first;
	private S second;

	private Consumer<T, S> handle;

	public DoubleConsumer(T first, S second, Consumer<T, S> handle) {
		this.first = first;
		this.second = second;
		this.handle = handle;
	}

	public void acceptFirst(T value) {
		this.first = value;
		if (this.second != null) {
			this.handle.accept(this.first, this.second);
		}
	}

	public void acceptSecond(S value) {
		this.second = value;
		if (this.first != null) {
			this.handle.accept(this.first, this.second);
		}
	}

	public interface Consumer<T, S> {

		void accept(T first, S second);
	}
}