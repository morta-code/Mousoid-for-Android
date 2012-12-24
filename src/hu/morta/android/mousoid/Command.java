package hu.morta.android.mousoid;

public class Command {
	
	public final byte[] command;

	/**
	 * Use:
	 * @param args type, action, values...
	 */
	public Command(byte... args) {
		command = args;
	}

}
