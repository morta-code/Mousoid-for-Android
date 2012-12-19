package hu.morta.android.mousoid;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class RFCOMMConnection implements MousoidConnection {
	
	private BluetoothSocket socket;
	private OutputStream output;

	public RFCOMMConnection(BluetoothDevice device) throws IOException {
		socket = device.createRfcommSocketToServiceRecord(UUID.fromString("b87a56f4-31dd-4edf-af6d-904516d18478"));
		socket.connect();
		output = socket.getOutputStream();
		Log.i("Bluetooth", "Connection established");
	}

	public void sendBytes(byte[] bytes) {
		// TODO Auto-generated method stub
	}
	
	public void close() {
		try {
			socket.close();
		} catch (IOException e) {
			// TODO test
			socket = null;
		}
	};

}
