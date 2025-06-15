package it.polimi.ingsw.controller;

import it.polimi.ingsw.message.Message;

import java.util.concurrent.*;

/**
 * Wrapper around {@link ArrayBlockingQueue} with a {@link ExecutorService} tied to it, allowing for asyncronous inserts and takes. 
 */
public class ThreadSafeMessageQueue<T extends Message> {

	private final ExecutorService threadpool;
	private final ArrayBlockingQueue<T> queue;

	/**
	 * Constructs a new Queue object and initializes the {@link ExecutorService} tied to it.
	 * 
	 * @param size Size of the underlying {@link ArrayBlockingQueue}.
	 */
	public ThreadSafeMessageQueue(int size) {
		threadpool = new ThreadPoolExecutor(1, 120, Long.MAX_VALUE, TimeUnit.MILLISECONDS, new SynchronousQueue<>());
		queue = new ArrayBlockingQueue<>(size, true);
	}

	/**
	 * Retrieves the head of the queue, waiting until it's available.
	 * 
	 * @return Head of the queue.
	 * @throws InterruptedException if interrupted while waiting.
	 */
	public T take() throws InterruptedException {
		return queue.take();
	}

	/**
	 * Inserts an item asynchronously into the back of the queue.
	 * 
	 * @param item Item to be inserted.
	 */
	public void insert(T item) {
		if(item == null) return;
		threadpool.submit(() -> {
			queue.add(item);
		});
	}

	/**
	 * Clears the entire contents of the queue.
	 */
	public void dump(){
		queue.clear();
	}
}
