package hu.morta.android.mousoid;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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

	class UDPFinderTask extends AsyncTask<Void, Void, ArrayList<ServerListItemData>>{

		@Override
		protected void onPreExecute() {
			startSearch.setEnabled(false);
			connectToAddress.setEnabled(false);
			progressBar.setVisibility(View.VISIBLE);
			adapter.clear();
		}
		
		@Override
		protected ArrayList<ServerListItemData> doInBackground(Void... params) {
			ArrayList<ServerListItemData> datas = new ArrayList<ServerListItemData>();
			
			TreeMap<String, InetAddress> availableUDPServers = ConnectionManager.getAvailableUDPServers(MainActivity.preferences.getInt("UDPTIMEOUT", 1500));
			if(availableUDPServers == null)
				return null;
			Set<String> keySet = availableUDPServers.keySet();
			for (String string : keySet) {
				ServerListItemData data = new ServerListItemData();
				data.name = string;
				data.address = availableUDPServers.get(string).getHostAddress();
				datas.add(data);
			}
			return datas;
		}
		
		@Override
		protected void onPostExecute(ArrayList<ServerListItemData> result) {
			((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(50);
			startSearch.setEnabled(true);
			connectToAddress.setEnabled(true);
			progressBar.setVisibility(View.GONE);
			if(result == null){
				Toast.makeText(ConnectionActivity.this, R.string.noDevice, Toast.LENGTH_LONG).show();
				return;
			}
			if(result.size() == 0)
				Toast.makeText(ConnectionActivity.this, R.string.noHosts, Toast.LENGTH_SHORT).show();
			else
				adapter.addAll(result);
		}
		
	}
	
	class SearchButtonListener implements OnClickListener{
		
		public void onClick(View v) {
			new UDPFinderTask().execute();
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
				((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(70);
				startActivity(new Intent(ConnectionActivity.this, MainActivity.class));
			}else{
				Toast.makeText(ConnectionActivity.this, R.string.connectionFailed, Toast.LENGTH_LONG).show();
			}
		}
		
	}
	
	////////////////////////////////////////////////////////////////////////
	
	private static ServerListItemAdapter adapter = null;
	private ListView serversList;
	private Button startSearch;
	private Button connectToAddress;
	private ProgressBar progressBar;
	private EditText editAddress;

	////////////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connection);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
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
		editAddress = (EditText) findViewById(R.id.addressField);
		editAddress.setText(MainActivity.preferences.getString("LAST_ADDRESS", ""));
		((TextView) findViewById(R.id.editNetworkName)).setText(MainActivity.preferences.getString("NET_NAME", "Mousoid"));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(!((WifiManager)getSystemService(Context.WIFI_SERVICE)).isWifiEnabled()){
			findViewById(R.id.buttonEnableWiFi).setVisibility(View.VISIBLE);
			findViewById(R.id.buttonEnableWiFi).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				}
			});
		}else{
			findViewById(R.id.buttonEnableWiFi).setVisibility(View.GONE);
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	@Override
	public void onBackPressed() {
		if(ConnectionManager.getConnection() == null)
			return;
		super.onBackPressed();
	}

	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		ConnectionManager.name = ((TextView) findViewById(R.id.editNetworkName)).getEditableText().toString();
		MainActivity.preferences.edit().putString("NET_NAME", ((TextView) findViewById(R.id.editNetworkName)).getEditableText().toString()).apply();
		new UDPConnectionTask().execute(((ServerListItemData)serversList.getItemAtPosition(position)).address);
		return true;
	}

	public void onClick(View v) {
		ConnectionManager.name = ((TextView) findViewById(R.id.editNetworkName)).getEditableText().toString();
		MainActivity.preferences.edit().putString("NET_NAME", ((TextView) findViewById(R.id.editNetworkName)).getEditableText().toString()).apply();
		String address = editAddress.getText().toString();
		if(address == null || address.equals("")){
			Toast.makeText(ConnectionActivity.this, R.string.noAddress, Toast.LENGTH_SHORT).show();
			return;
		}
		MainActivity.preferences.edit().putString("LAST_ADDRESS", editAddress.getEditableText().toString()).apply();
		new UDPConnectionTask().execute(address);
	}

}
