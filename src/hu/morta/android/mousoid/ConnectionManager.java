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
import java.util.Set;
import java.util.TreeMap;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.util.Log;

public class ConnectionManager {
	
	private static MousoidConnection connection = null;
	public static String name = "Mousoid";

	public static MousoidConnection getConnection() {
		return connection;
	}

	public static synchronized boolean connectToUDP(final InetAddress address){
		if(connection != null)
			connection.close();
		connection = null;
		try {
			connection = new UDPConnection(address);
		} catch (SocketException e) {
			Log.w("ConnectionManager", "SocketException!");
			Log.w("ConnectionManager", address.getHostAddress());
			return false;
		}
		sendName(name);
		return true;
	}
	
	public static synchronized boolean connectToRFCOMM(String address){
		if(connection != null)
			connection.close();
		connection = null;
		try {
			connection = new RFCOMMConnection(BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address));
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	public static TreeMap<String, InetAddress> getAvailableUDPServers(int timeoutInMillis) {
		TreeMap<String, InetAddress> map = new TreeMap<String, InetAddress>();
		try {
			DatagramSocket temporarySocket = new DatagramSocket();
			
			temporarySocket.setBroadcast(true);
			temporarySocket.send(new DatagramPacket(new byte[]{Constant.HEADER, Constant.WHO_ARE_YOU}, 2, InetAddress.getByName("224.0.0.1"), 10066));
			
			temporarySocket.setSoTimeout(timeoutInMillis);
			Log.i("UDP Server:", "I sent UDP");
			while(true){
				byte[] buffer = new byte[64];
				DatagramPacket received = new DatagramPacket(buffer, buffer.length);
				temporarySocket.receive(received);
				Log.i("UDP Server:", "I received UDP");
				byte[] data = received.getData();
				Log.i("UDP Server:", "Arrived bytes: " + Byte.toString(data[0]) +" "+ Byte.toString(data[1]));
				if(data[0] != Constant.HEADER) continue;
				if(data[1] != Constant.NAME) continue;
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
				return map;
			}
			e.printStackTrace();
		}
		return map;
	}
	
	public static TreeMap<String, String> getAvailableBluetoothServers() {
		TreeMap<String, String> map = new TreeMap<String, String>();
		BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
		if(adapter == null){
			return null;
		}
		Set<BluetoothDevice> bondedDevices = adapter.getBondedDevices();
		for (BluetoothDevice bluetoothDevice : bondedDevices) {
			map.put(bluetoothDevice.getName(), bluetoothDevice.getAddress());
		}
		return map;
	}
	
	////////////////////////////////////////////////////////////////////////
	
	public static void sendKey(byte key) {
		if(connection == null){
			Log.w("ConnectionManager", "Nincs kapcsolat!");
			return;
		}
		connection.sendBytes(new byte[]{Constant.HEADER, Constant.KEYCOMMAND, key});
	}
	
	public static void sendMouseWheel() {
		// TODO
	}
	
	public static void sendMouseMotion() {
		// TODO
	}
	
	public static void sendMouseButton(byte action, byte button) {
		if(connection == null){
			Log.w("ConnectionManager", "Nincs kapcsolat!");
			return;
		}
		connection.sendBytes(new byte[]{Constant.HEADER, Constant.MOUSEBUTTON, action, button});
	}
	
	public static void sendName(CharSequence name) {
		if(connection == null){
			Log.w("ConnectionManager", "Nincs kapcsolat!");
			return;
		}
		byte b[] = new byte[name.length()+3];
		b[0] = Constant.HEADER;
		b[1] = Constant.NAME;
		b[2] = (byte) name.length();
		for (int i = 0; i < name.length(); i++) {
			b[i+3] = (byte) name.charAt(i);
		}
		connection.sendBytes(b);
	}

}
