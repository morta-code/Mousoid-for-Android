package hu.morta.android.mousoid;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class ConnectionActivity extends Activity implements OnItemLongClickListener, OnClickListener{
	
	class ServerListItemData{
		public String name;
		public String address;
	}
	
	class ServerListItemAdapter extends ArrayAdapter<ServerListItemData>{

		public ServerListItemAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		public ServerListItemAdapter(Context context, int resource,
				int textViewResourceId) {
			super(context, resource, textViewResourceId);
		}

		public ServerListItemAdapter(Context context, int resource,
				int textViewResourceId, List<ServerListItemData> objects) {
			super(context, resource, textViewResourceId, objects);
		}

		public ServerListItemAdapter(Context context, int resource,
				int textViewResourceId, ServerListItemData[] objects) {
			super(context, resource, textViewResourceId, objects);
		}

		public ServerListItemAdapter(Context context, int textViewResourceId,
				List<ServerListItemData> objects) {
			super(context, textViewResourceId, objects);
		}

		public ServerListItemAdapter(Context context, int textViewResourceId,
				ServerListItemData[] objects) {
			super(context, textViewResourceId, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.server_list_item, null);
			}
			TextView name = (TextView) v.findViewById(R.id.serverNameInList);
			name.setText(getItem(position).name);
			return v;
		}
		
	}

	class ServerFinderTask extends AsyncTask<Byte, Void, ArrayList<ServerListItemData>>{

		@Override
		protected void onPreExecute() {
			startSearch.setEnabled(false);
			connectToAddress.setEnabled(false);
			progressBar.setVisibility(View.VISIBLE);
			adapter.clear();
		}
		
		@Override
		protected ArrayList<ServerListItemData> doInBackground(Byte... params) {
			ArrayList<ServerListItemData> datas = new ArrayList<ServerListItemData>();
			if(params[0] == SEARCH_WIFI){
				TreeMap<String, InetAddress> availableUDPServers = ConnectionManager.getAvailableUDPServers(4000);
				Set<String> keySet = availableUDPServers.keySet();
				Log.i("Connection Activity", "Servers found! " + Integer.toString(keySet.size()));
				for (String string : keySet) {
					Log.i("Connection Activity", string);
					ServerListItemData data = new ServerListItemData();
					data.name = string;
					data.address = availableUDPServers.get(string).getHostAddress();
					datas.add(data);
				}
			}else{
				TreeMap<String, BluetoothDevice> availableBluetoothServers = ConnectionManager.getAvailableBluetoothServers(5000);
				Set<String> keySet = availableBluetoothServers.keySet();
				for (String string : keySet) {
					ServerListItemData data = new ServerListItemData();
					data.name = string;
					data.address = availableBluetoothServers.get(string).getAddress();
					datas.add(data);
				}
			}
			return datas;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ServerListItemData> result) {
			startSearch.setEnabled(true);
			connectToAddress.setEnabled(true);
			progressBar.setVisibility(View.GONE);
			if(result.size() == 0)
				Toast.makeText(ConnectionActivity.this, R.string.noHosts, Toast.LENGTH_SHORT).show();
			else
				adapter.addAll(result);
		}
		
	}
	
	class SearchButtonListener implements OnClickListener{
		
		public void onClick(View v) {
			if(((RadioButton) findViewById(R.id.radioWifi)).isChecked()){
				new ServerFinderTask().execute(SEARCH_WIFI);
			}else{
				new ServerFinderTask().execute(SEARCH_BLUETOOTH);
			}
		}
		
	}
	
	class UDPConnectionTask extends AsyncTask<String, Void, Boolean>{
		
		@Override
		protected void onPreExecute() {
			startSearch.setEnabled(false);
			connectToAddress.setEnabled(false);
			progressBar.setVisibility(View.VISIBLE);
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean b;
			try {
				b = ConnectionManager.connectToUDP(InetAddress.getByName(params[0]));
			} catch (UnknownHostException e) {
				return false;
			}
			return b;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			startSearch.setEnabled(true);
			connectToAddress.setEnabled(true);
			progressBar.setVisibility(View.GONE);
			if(result){
				Toast.makeText(ConnectionActivity.this, R.string.connectionEstablished, Toast.LENGTH_SHORT).show();
				startActivity(new Intent(ConnectionActivity.this, MainActivity.class));
			}else{
				Toast.makeText(ConnectionActivity.this, R.string.connectionFailed, Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	////////////////////////////////////////////////////////////////////////
	
	private static final byte SEARCH_WIFI = 0;
	private static final byte SEARCH_BLUETOOTH = 1;
	
	private static ServerListItemAdapter adapter = null;
	
	private ListView serversList;
	private Button startSearch;
	private Button connectToAddress;
	private ProgressBar progressBar;

	////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		
		if(adapter == null)
			adapter = new ServerListItemAdapter(this, R.layout.server_list_item);
		
		serversList = (ListView) findViewById(R.id.serversList);
		serversList.setAdapter(adapter);
		serversList.setOnItemLongClickListener(this);
		startSearch = (Button) findViewById(R.id.buttonStart);
		startSearch.setOnClickListener(new SearchButtonListener());
		connectToAddress = ((Button) findViewById(R.id.buttonConnect));
		connectToAddress.setOnClickListener(this);
		progressBar = (ProgressBar) findViewById(R.id.connectionProgressBar);
		
		// TODO fekvő helyzetben a lista animálódjon át
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// TODO
		getMenuInflater().inflate(R.menu.activity_connection, menu);
		return true;
	}


	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		new UDPConnectionTask().execute(((ServerListItemData)serversList.getItemAtPosition(position)).address);
		return true;
	}

	public void onClick(View v) {
		String address = ((EditText) findViewById(R.id.addressField)).getText().toString();
		if(address == null || address.equals("")){
			Toast.makeText(ConnectionActivity.this, R.string.noAddress, Toast.LENGTH_SHORT).show();
			return;
		}
		new UDPConnectionTask().execute(address);
	}

}
