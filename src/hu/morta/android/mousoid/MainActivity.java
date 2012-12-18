package hu.morta.android.mousoid;

import java.net.InetAddress;
import java.net.UnknownHostException;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	preferences = getSharedPreferences("Mousoid", 0);
        
        setContentView(R.layout.activity_main);
        
        if(ConnectionManager.getConnection() == null){
        	startActivity(new Intent(this, ConnectionActivity.class));
        }
        
        new Thread(new Runnable() {
			public void run() {
				try {
					ConnectionManager.connectToUDP(InetAddress
							.getByName("192.168.1.70"));
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
        
		Button button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				new Thread(new Runnable() {
					public void run() {
						ConnectionManager.sendKey(Key.SPACE);
					}
				}).start();
			}
		});
    }
    
    @Override
    protected void onResume() {
    	if(ConnectionManager.getConnection() == null){
        	startActivity(new Intent(this, ConnectionActivity.class));
        }
    	super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        // TODO
        return true;
    }
}
