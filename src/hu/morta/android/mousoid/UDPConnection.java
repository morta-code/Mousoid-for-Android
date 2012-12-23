package hu.morta.android.mousoid;

import java.io.IOException;
import java.net.*;

import android.util.Log;

public class UDPConnection implements MousoidConnection {
	
	private DatagramSocket socket;
	private InetAddress address;

	public UDPConnection(InetAddress address) throws SocketException {
		this.address = address;
		this.socket = new DatagramSocket(10066);
	}

	public void sendBytes(byte[] bytes) {
		if(address == null){
			Log.e("UDP", "NuLL Address!");
			return;
		}
		if(socket == null){
			Log.e("UDP", "NuLL Socket!");
			return;
		}
		try {
			socket.send(new DatagramPacket(bytes, bytes.length, address, 10066));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		socket.close();
	}

}
