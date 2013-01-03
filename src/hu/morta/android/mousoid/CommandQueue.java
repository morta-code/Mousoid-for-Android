package hu.morta.android.mousoid;

import java.util.LinkedList;


public class CommandQueue extends Thread {

	private final LinkedList<Command> queue;

    ///////////////////////////////////////////////////////////////////
	
	public CommandQueue() {
		queue = new LinkedList<Command>();
		start();
	}
	
	public synchronized void add(Command c) {
		queue.add(c);
		notify();
	}
	
	public synchronized Command poll() throws InterruptedException{
		while(queue.isEmpty()){
			wait();
		}
		return queue.poll();
	}

    ///////////////////////////////////////////////////////////////////
	
	@Override
	public void run() {
		while(!isInterrupted()){
			try {
				ConnectionManager.sendCommand(poll());
			} catch (InterruptedException e) {
				return;
			}
		}
	}

}
