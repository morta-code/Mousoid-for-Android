package hu.morta.android.mousoid;

public interface MousoidConnection {
	public void sendBytes(byte [] bytes);
	public void close();
}
