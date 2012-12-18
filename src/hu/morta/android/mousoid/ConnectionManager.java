package hu.morta.android.mousoid;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Map;
import java.util.TreeMap;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

public class ConnectionManager {
	
	private static final byte HEADER = 66;
	private static final byte WHO_ARE_YOU = -128;
	private static final byte NAME = 8;
	private static final byte KEY = 1;
	
	////////////////////////////////////////////////////////////////////////
	
	private static MousoidConnection connection = null;

	public static MousoidConnection getConnection() {
		return connection;
	}

	public static synchronized void connectToUDP(final InetAddress address){
		connection = new UDPConnection(address);
	}
	
	public static TreeMap<String, InetAddress> getAvailableUDPServers(int timeoutInMillis) {
		TreeMap<String, InetAddress> map = new TreeMap<String, InetAddress>();
		try {
			DatagramSocket temporarySocket = new DatagramSocket();

			temporarySocket.setBroadcast(true);
			temporarySocket.send(new DatagramPacket(new byte[]{HEADER, WHO_ARE_YOU}, 2, InetAddress.getByName("224.0.0.1"), 10066));
			
			temporarySocket.setSoTimeout(timeoutInMillis);
			Log.i("UDP Server:", "I sent UDP");
			while(true){
				byte[] buffer = new byte[64];
				DatagramPacket received = new DatagramPacket(buffer, buffer.length);
				temporarySocket.receive(received);
				Log.i("UDP Server:", "I received UDP");
				byte[] data = received.getData();
				Log.i("UDP Server:", "Arrived bytes: " + Byte.toString(data[0]) +" "+ Byte.toString(data[1]));
				if(data[0] != HEADER) continue;
				if(data[1] != NAME) continue;
				Log.i("UDP Server:", "Arrived datagram OK");
				InetAddress inetAddress = received.getAddress();
				String name = "";
				for(short s = 0; s < data[2]; s++){
					name += String.valueOf((char)data[3+s]);
				}
				Log.i("UDP Server:", "Arrived name: " + name);
				map.put(name, inetAddress);
				Log.i("UDP Server:", inetAddress.getHostAddress() + " " +inetAddress.getHostName());
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			if(e instanceof InterruptedIOException){
				Log.i("UDP Server:", "Return map size: " + Integer.toString(map.size()));
				Log.i("UDP Server:", "First pair: " + map.keySet().toArray()[0] +" "+ map.get(map.keySet().toArray()[0]).getHostName());
				return map;
			}
			e.printStackTrace();
		}
		return map;
	}
	

	public static TreeMap<String, BluetoothDevice> getAvailableBluetoothServers(int timeout) {
		// TODO Auto-generated method 
		TreeMap<String, BluetoothDevice> map = new TreeMap<String, BluetoothDevice>();
		return map;
	}
	////////////////////////////////////////////////////////////////////////
	
	public static void sendKey(byte key) {
		if(connection == null){
			Log.w("ConnectionManager", "Nincs kapcsolat!");
			return;
		}
		connection.sendBytes(new byte[]{HEADER, KEY, key});
	}
	
	public static void sendMouseWheel() {
		// TODO
	}
	
	public static void sendMouseMotion() {
		// TODO
	}
	
	public static void sendMouseButton() {
		// TODO
	}
	
	public static void sendName(CharSequence name) {
		// TODO
	}

}
