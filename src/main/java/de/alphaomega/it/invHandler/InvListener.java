package de.alphaomega.it.invHandler;

import java.util.function.Consumer;

public record InvListener<T>(Class<T> type, Consumer<T> consumer) {

	public void accept(final T t) {
		consumer.accept(t);
	}
}
