package hu.morta.android.mousoid;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

public class ConnectionActivity extends Activity implements OnItemClickListener{
	
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
			adapter.addAll(result);
		}
		
	}
	
	class SearchButtonListener implements OnClickListener{
		
		public void onClick(View v) {
			if(radioWifi.isChecked()){
				new ServerFinderTask().execute(SEARCH_WIFI);
			}else{
				new ServerFinderTask().execute(SEARCH_BLUETOOTH);
			}
		}
		
	}
	
	////////////////////////////////////////////////////////////////////////
	
	private static final byte SEARCH_WIFI = 0;
	private static final byte SEARCH_BLUETOOTH = 1;
	
	private static ServerListItemAdapter adapter = null;
	
	private ListView serversList;
	private RadioButton radioWifi;
	private RadioButton radioBluetoot;
	private Button startSearch;
	private Handler handler = new Handler();

	////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
		
		if(adapter == null)
			adapter = new ServerListItemAdapter(this, R.layout.server_list_item);
		
		serversList = (ListView) findViewById(R.id.serversList);
		serversList.setAdapter(adapter);
		serversList.setOnItemClickListener(this);
		radioWifi = (RadioButton) findViewById(R.id.radioWifi);
		radioBluetoot = (RadioButton) findViewById(R.id.radioBluetooth);
		startSearch = (Button) findViewById(R.id.buttonStart);
		
		// TODO fekvő helyzetben a lista animálódjon át
		startSearch.setOnClickListener(new SearchButtonListener());
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_connection, menu);
		return true;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO
	}

}
