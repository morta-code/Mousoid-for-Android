package hu.morta.android.mousoid;

import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;

public class RFCOMMConnection implements MousoidConnection {
	
	private BluetoothSocket socket;
	private OutputStream output;

	public RFCOMMConnection() {
		// TODO Auto-generated constructor stub
	}

	public void sendBytes(byte[] bytes) {
		// TODO Auto-generated method stub
	}

}
