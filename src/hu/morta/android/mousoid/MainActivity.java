package hu.morta.android.mousoid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	
	class MousoidGestureListener extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			Log.d("onDoubleTap",e.toString());
			return true;
		}
		
		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			Log.d("onDoubleTapEvent",e.toString());
			return true;
		}
		
	    @Override
	    public boolean onSingleTapUp(MotionEvent ev) {
	    	Log.d("onSingleTapUp",ev.toString());
	    	return true;
	    }
	    
	    @Override
	    public void onShowPress(MotionEvent ev) {
	    	Log.d("onShowPress",ev.toString());
	    }
	    
	    @Override
	    public void onLongPress(MotionEvent ev) {
	    	Log.d("onLongPress",ev.toString());
	    }
	    
	    // for move
	    @Override
	    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	    	Log.d("onScroll",e1.toString() + Float.toString(distanceX)+" "+Float.toString(distanceY));
	    	queue.add(new Command(Constant.MOUSEMOVE, (byte)(distanceX*mouseResolution), (byte)(distanceY*mouseResolution)));
	    	return true;
	    }
	    
	    @Override
	    public boolean onDown(MotionEvent ev) {
	    	Log.d("onDownd",ev.toString());
	    	return true;
	    }
	    
	    @Override
	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
	    	Log.d("d",e1.toString());
	    	Log.d("e2",e2.toString());
	    	return true;
	    }
	    
	    // for click
	    @Override
	    public boolean onSingleTapConfirmed(MotionEvent e) {
	    	Log.d("onSingleTapConfirmed",e.toString());
	    	queue.add(new Command(Constant.MOUSEBUTTON, Constant.CLICK, Constant.MOUSE_LEFT));
	    	return true;
	    }
	    
	}
	///////////////////////////////////////////////////////////////////
	
	public static SharedPreferences preferences;
	private GestureDetector detector;
	private CommandQueue queue;
	private boolean mouseMode;
	private float mouseResolution;
	private boolean multitouch;
	private boolean showButtons;
	
	///////////////////////////////////////////////////////////////////
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	preferences = getSharedPreferences("Mousoid", 0);
        Log.i("MainActivity", "onCreate");        
        if(ConnectionManager.getConnection() == null){
        	startActivity(new Intent(this, ConnectionActivity.class));
        }

    	mouseMode = preferences.getBoolean("MOUSE", true);
        if(mouseMode){
    		mouseResolution = preferences.getFloat("RESOLUTION", 2F);
    		multitouch = preferences.getBoolean("MULTITOUCH", false);
    		showButtons = preferences.getBoolean("BUTTONS", true);
    		loadMouseInterface();
    	}else{
    		loadPresenterInterface();
    	}
        
    }
    
    ///////////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {
        Log.i("MainActivity", "onResume");
    	if(ConnectionManager.getConnection() == null){
        	startActivity(new Intent(this, ConnectionActivity.class));
        }
    	queue = new CommandQueue();
    	super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("MainActivity", "onPause");
    	queue.interrupt();
    	super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        // TODO
        return true;
    }

    ///////////////////////////////////////////////////////////////////

    void loadMouseInterface(){
    	setContentView(R.layout.main_mouse);
    	detector = new GestureDetector(this, new MousoidGestureListener());
    	if(preferences.getBoolean("BUTTONS", true)){
    		Button button = (Button) findViewById(R.id.buttonLeft);
            button.setOnClickListener(new OnClickListener() {
    			
    			public void onClick(View v) {
    				new Thread(new Runnable() {
    					public void run() {
    						ConnectionManager.sendMouseButton(Constant.CLICK, Constant.MOUSE_LEFT);
    					}
    				}).start();
    			}
    		});
    	}
    }
    
    void loadPresenterInterface(){
    	// TODO
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(mouseMode)
    		return detector.onTouchEvent(event);
    	else
    		return super.onTouchEvent(event);
    	
    }

}
