package lyra.alpha.struct;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class DynamicConcurrentArrayList<T> {
	/**
	 * 未处理
	 */
	private CopyOnWriteArrayList<T> unprocessed = new CopyOnWriteArrayList<>();
	/**
	 * 已处理列表
	 */
	private CopyOnWriteArrayList<T> processed = new CopyOnWriteArrayList<>();

	public void forEach(Consumer<T> op, BiConsumer<T, RuntimeException> ex_op) {
		while (!unprocessed.isEmpty()) {
			for (T e : unprocessed) {
				try {
					op.accept(e);
					processed.add(e);
				} catch (RuntimeException ex) {
					ex_op.accept(e, ex);// 执行抛出错误就放弃当前操作继续执行下一个元素的操作
				}
			}
			unprocessed.removeAll(processed);
		}
		CopyOnWriteArrayList<T> tmp = processed;
		processed = unprocessed;
		unprocessed = tmp;
	}

	public static final BiConsumer<Object, RuntimeException> IGNORE_RUNTIME_EXCEPTION = (Object e, RuntimeException ex) -> {
	};

	public static final BiConsumer<Object, RuntimeException> PRINT_RUNTIME_EXCEPTION = (Object e, RuntimeException ex) -> {
		System.err.println("Iterating element " + e + " throws RuntimeException");
		ex.printStackTrace();
	};

	@SuppressWarnings("unchecked")
	public void forEach(Consumer<T> op) {
		forEach(op, (BiConsumer<T, RuntimeException>) IGNORE_RUNTIME_EXCEPTION);
	}

	public void add(T e) {
		unprocessed.add(e);
	}
}
