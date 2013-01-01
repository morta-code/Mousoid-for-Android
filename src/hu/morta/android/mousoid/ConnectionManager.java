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

public class ConnectionManager {
	
	private static MousoidConnection connection = null;
	public static String name;

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
			while(true){
				byte[] buffer = new byte[64];
				DatagramPacket received = new DatagramPacket(buffer, buffer.length);
				temporarySocket.receive(received);
				byte[] data = received.getData();
				if(data[0] != Constant.HEADER) continue;
				if(data[1] != Constant.NAME) continue;
				InetAddress inetAddress = received.getAddress();
				String name = "";
				for(short s = 0; s < data[2]; s++){
					name += String.valueOf((char)data[3+s]);
				}
				map.put(name, inetAddress);
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
		
	public static void sendName(CharSequence name) {
		if(connection == null){
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

	public static void sendCommand(Command c){
		if(connection == null){
			return;
		}
		byte[] b = new byte[c.command.length+1];
		b[0] = Constant.HEADER;
		System.arraycopy(c.command, 0, b, 1, c.command.length);
		connection.sendBytes(b);
	}
}
