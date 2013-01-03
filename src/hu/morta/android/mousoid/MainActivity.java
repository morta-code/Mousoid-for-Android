package hu.morta.android.mousoid;

import java.nio.ByteBuffer;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;

public class MainActivity extends Activity implements OnMenuItemClickListener, SensorEventListener{
	
	class MousoidGestureListener extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onDoubleTap(MotionEvent e) {
			queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.DOUBLE_CLICK, Constant.MOUSE_LEFT}));
			return true;
		}
		
	    @Override
	    public void onLongPress(MotionEvent ev) {
			queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.PRESS, Constant.MOUSE_LEFT}));
	    }
	    
	    // for move
	    @Override
	    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
	    	queue.add(new Command(new byte[]{Constant.MOUSEMOVE, (byte)(distanceX*mouseResolution), (byte)(distanceY*mouseResolution)}));
	    	return true;
	    }

	    
	    // for click
	    @Override
	    public boolean onSingleTapConfirmed(MotionEvent e) {
	    	queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.CLICK, Constant.MOUSE_LEFT}));
	    	return true;
	    }
	    
	}
	
	class MouseButtonsListener implements OnClickListener{
		public void onClick(View v) {
			((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(35);
			switch (v.getId()) {
			case R.id.buttonLeft:
				queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.CLICK, Constant.MOUSE_LEFT}));
				return;
			case R.id.buttonMiddle:
				queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.CLICK, Constant.MOUSE_MIDDLE}));
				return;
			case R.id.buttonRight:
				queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.CLICK, Constant.MOUSE_RIGHT}));
				return;
			case R.id.buttonScrollUp:
				queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.SCROLL_VERTICAL, (byte)-1}));
				return;
			case R.id.buttonScrollDown:
				queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.SCROLL_VERTICAL, (byte)1}));
				return;
			}
		}
	}
	
	class PresenterButtonsListener implements OnClickListener{
		public void onClick(View v) {
			((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(35);
			byte[] head = new byte[]{Constant.KEYCOMMAND, 0, 0};
			byte[] bs = new byte[]{};
			
			switch (v.getId()) {
			case R.id.buttonBackspace:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Backspace).array();
				head[1] = 1;				
				break;
			case R.id.buttonArrowUp:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Up).array();
				head[1] = 1;				
				break;
			case R.id.buttonArrowDown:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Down).array();
				head[1] = 1;				
				break;
			case R.id.buttonArrowLeft:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Left).array();
				head[1] = 1;				
				break;
			case R.id.buttonArrowRight:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Right).array();
				head[1] = 1;				
				break;
			case R.id.buttonMediaPrev:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_MediaPrevious).array();
				head[1] = 1;				
				break;
			case R.id.buttonMediaNext:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_MediaNext).array();
				head[1] = 1;				
				break;
			case R.id.buttonMediaPlayPause:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_MediaPlay).array();
				head[1] = 1;				
				break;
			case R.id.buttonMediaStop:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_MediaStop).array();
				head[1] = 1;				
				break;
			case R.id.buttonVolumeLower:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_VolumeDown).array();
				head[1] = 1;				
				break;
			case R.id.buttonVolumeMute:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_VolumeMute).array();
				head[1] = 1;				
				break;
			case R.id.buttonVolumeRaise:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_VolumeUp).array();
				head[1] = 1;				
				break;
			case R.id.buttonBrowserReload:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_F5).array();
				head[1] = 1;				
				break;
			case R.id.buttonBrowserBack:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Back).array();
				head[1] = 1;				
				break;
			case R.id.buttonBrowserForward:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Forward).array();
				head[1] = 1;				
				break;
			case R.id.buttonBrowserSearch:
				bs = new byte[6];
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Control).array(), 0, bs, 0, 4);
				System.arraycopy(ByteBuffer.allocate(2).putChar('l').array(), 0, bs, 4, 2);
				head[1] = 1;				
				head[2] = 1;				
				break;
			case R.id.buttonF5:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_F5).array();
				head[1] = 1;				
				break;
			case R.id.buttonCTRLF5:
				bs = new byte[8];
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Control).array(), 0, bs, 0, 4);
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_F5).array(), 0, bs, 4, 4);
				head[1] = 2;				
				break;
			case R.id.buttonBLANK:
				bs = ByteBuffer.allocate(2).putChar('b').array();
				head[2] = 1;				
				break;
			case R.id.buttonWHITE:
				bs = ByteBuffer.allocate(2).putChar('w').array();
				head[2] = 1;				
				break;
			case R.id.buttonHome:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Home).array();
				head[1] = 1;				
				break;
			case R.id.buttonPgUp:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_PageUp).array();
				head[1] = 1;				
				break;
			case R.id.buttonPgDn:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_PageDown).array();
				head[1] = 1;				
				break;
			case R.id.buttonEnd:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_End).array();
				head[1] = 1;				
				break;
			case R.id.buttonEsc:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Escape).array();
				head[1] = 1;				
				break;
			case R.id.buttonSpace:
				bs = ByteBuffer.allocate(2).putChar(' ').array();
				head[2] = 1;				
				break;
			case R.id.buttonEnter:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Return).array();
				head[1] = 1;				
				break;
			case R.id.buttonCtrlAltDel:
				bs = new byte[12];
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Control).array(), 0, bs, 0, 4);
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Alt).array(), 0, bs, 4, 4);
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Delete).array(), 0, bs, 8, 4);
				head[1] = 3;				
				break;
			case R.id.buttonDash:
			case R.id.buttonSuper:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Super_L).array();
				head[1] = 1;				
				break;
			case R.id.buttonWinP:
				bs = new byte[6];
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Super_L).array(), 0, bs, 0, 4);
				System.arraycopy(ByteBuffer.allocate(2).putChar('p').array(), 0, bs, 4, 2);
				head[1] = 1;				
				head[2] = 1;				
				break;
			case R.id.buttonTerminal:
				bs = new byte[10];
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Control).array(), 0, bs, 0, 4);
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Alt).array(), 0, bs, 4, 4);
				System.arraycopy(ByteBuffer.allocate(2).putChar('t').array(), 0, bs, 8, 2);
				head[1] = 2;				
				head[2] = 1;				
				break;
			case R.id.buttonSuperW:
				bs = new byte[6];
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Super_L).array(), 0, bs, 0, 4);
				System.arraycopy(ByteBuffer.allocate(2).putChar('w').array(), 0, bs, 4, 2);
				head[1] = 1;				
				head[2] = 1;				
				break;
			case R.id.buttonSuperS:
				bs = new byte[6];
				System.arraycopy(ByteBuffer.allocate(4).putInt(Constant.Key_Super_L).array(), 0, bs, 0, 4);
				System.arraycopy(ByteBuffer.allocate(2).putChar('s').array(), 0, bs, 4, 2);
				head[1] = 1;				
				head[2] = 1;				
				break;
			case R.id.buttonLightDown:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_MonBrightnessDown).array();
				head[1] = 1;				
				break;
			case R.id.buttonLightUp:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_MonBrightnessUp).array();
				head[1] = 1;				
				break;
			case R.id.buttonPower:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_PowerOff).array();
				head[1] = 1;				
				break;
			case R.id.buttonLineOk:{
				String line = ((EditText)findViewById(R.id.editLine)).getEditableText().toString();
				head[2] = line.length() > 127 ? 127 : (byte) line.length();			
				ByteBuffer bb = ByteBuffer.allocate(line.length()*2);
				for (int j = 0; j < line.length(); j++) {
					bb.putChar(line.charAt(j));
				}
				bs = bb.array();
				((EditText)findViewById(R.id.editLine)).getEditableText().clear();
				break;
			}
			case R.id.buttonCommandOk:{
				String line = ((EditText)findViewById(R.id.editCommandLine)).getEditableText().toString();
				head = new byte[]{Constant.SYSTEM, line.length() > 127 ? 127 : (byte) line.length()};
				bs = line.getBytes();
				((EditText)findViewById(R.id.editCommandLine)).getEditableText().clear();
				break;
			}
			}
			
			byte[] result = new byte[head.length + bs.length];
			System.arraycopy(head, 0, result, 0, head.length);
			System.arraycopy(bs, 0, result, head.length, bs.length);
			queue.add(new Command(result));
		}
	}
	
	///////////////////////////////////////////////////////////////////
	
	public static SharedPreferences preferences;
	private GestureDetector detector;
	private PresenterButtonsListener listener;
	private CommandQueue queue;
	private SensorManager sManager;
	private boolean mouseMode;
	private boolean gestureEnabled;
	private float mouseResolution;
	private boolean multitouch;
	private float p0y0 = 0;
	private float p0y1 = 0;
	private boolean lim = true;
	
	///////////////////////////////////////////////////////////////////
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	preferences = getSharedPreferences("Mousoid", 0);     
        if(ConnectionManager.getConnection() == null){
        	startActivity(new Intent(this, ConnectionActivity.class));
        }
    	mouseMode = preferences.getBoolean("MOUSE", false);
    	gestureEnabled = preferences.getBoolean("GESTURE_PRESENTER", false);
    	if(mouseMode){
    		mouseResolution = preferences.getInt("RESOLUTION", 3)*0.2f + 1.1f;
    		multitouch = preferences.getBoolean("MULTITOUCH", false);
    		loadMouseInterface();
    	}else{
    		loadPresenterInterface();
    	}
    }

    @Override
    protected void onResume() {
    	if(gestureEnabled)
    		loadSensor();
    	queue = new CommandQueue();
    	super.onResume();
    }

    @Override
    protected void onPause() {
        if(gestureEnabled){
        	sManager.unregisterListener(this);
        	GesturePattern.clear();
        }
    	queue.interrupt();
    	super.onPause();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        menu.findItem(R.id.settingsMenuItem).setOnMenuItemClickListener(this);
        menu.findItem(R.id.switchModeMenuItem).setOnMenuItemClickListener(this);
        menu.findItem(R.id.gestureMenuItem).setOnMenuItemClickListener(this);
        menu.findItem(R.id.connectionsMenuItem).setOnMenuItemClickListener(this);
        menu.findItem(R.id.aboutMenuItem).setOnMenuItemClickListener(this);
        return true;
    }
    
    @Override
    public void onBackPressed() {
		((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(40);
    	preferences.edit().putBoolean("MOUSE", !mouseMode).commit();
		finish();
		startActivity(getIntent());
    }
    
    ///////////////////////////////////////////////////////////////////

    private void loadMouseInterface(){
    	setContentView(R.layout.main_mouse);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    	detector = new GestureDetector(this, new MousoidGestureListener());
		MouseButtonsListener listener = new MouseButtonsListener();
		findViewById(R.id.buttonLeft).setOnClickListener(listener);
		findViewById(R.id.buttonMiddle).setOnClickListener(listener);
		findViewById(R.id.buttonRight).setOnClickListener(listener);
    	if(preferences.getBoolean("BUTTONS", true)){
    		findViewById(R.id.buttonScrollUp).setOnClickListener(listener);
    		findViewById(R.id.buttonScrollDown).setOnClickListener(listener);
    	}else{
    		findViewById(R.id.buttonScrollUp).setVisibility(View.GONE);
    		findViewById(R.id.buttonScrollDown).setVisibility(View.GONE);
    	}
    }
    
    private void loadPresenterInterface(){
    	setContentView(R.layout.main_presenter);
    	listener = new PresenterButtonsListener();
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	findViewById(R.id.lineLayout).setVisibility(preferences.getBoolean("LINE_EDIT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.runCommandLayout).setVisibility(preferences.getBoolean("RUN_COMMAND", false) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.arrowsLayout).setVisibility(preferences.getBoolean("ARROWS_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.mediaLayout).setVisibility(preferences.getBoolean("MEDIA_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.volumeLayout).setVisibility(preferences.getBoolean("MEDIA_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.browserLayout).setVisibility(preferences.getBoolean("BROWSER_LAYOUT", false) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.presenterLayout).setVisibility(preferences.getBoolean("PRESENTER_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.jumpLayout).setVisibility(preferences.getBoolean("PRESENTER_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.controlLayout).setVisibility(View.VISIBLE);
    	findViewById(R.id.winLayout).setVisibility(preferences.getBoolean("WIN_LAYOUT", false) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.linuxLayout).setVisibility(preferences.getBoolean("LINUX_LAYOUT", false) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.laptopLayout).setVisibility(preferences.getBoolean("LAPTOP_LAYOUT", false) == true ? View.VISIBLE : View.GONE);
    	
    	findViewById(R.id.buttonLineOk).setOnClickListener(listener);
    	findViewById(R.id.buttonCommandOk).setOnClickListener(listener);
    	findViewById(R.id.buttonBackspace).setOnClickListener(listener);
    	for (View view : findViewById(R.id.arrowsLayout).getTouchables())		view.setOnClickListener(listener);
    	for (View view : findViewById(R.id.mediaLayout).getTouchables())		view.setOnClickListener(listener);
    	for (View view : findViewById(R.id.volumeLayout).getTouchables())		view.setOnClickListener(listener);
    	for (View view : findViewById(R.id.browserLayout).getTouchables())		view.setOnClickListener(listener);
    	for (View view : findViewById(R.id.presenterLayout).getTouchables())	view.setOnClickListener(listener);
    	for (View view : findViewById(R.id.jumpLayout).getTouchables())			view.setOnClickListener(listener);
    	for (View view : findViewById(R.id.controlLayout).getTouchables())		view.setOnClickListener(listener);
    	for (View view : findViewById(R.id.winLayout).getTouchables())			view.setOnClickListener(listener);
    	for (View view : findViewById(R.id.linuxLayout).getTouchables())		view.setOnClickListener(listener);
    	for (View view : findViewById(R.id.laptopLayout).getTouchables())		view.setOnClickListener(listener);
    }
    
    private void loadSensor() {
    	sManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sManager.registerListener(this, sManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_UI, new Handler());
        GesturePattern.load(preferences.getInt("GP_KEY", Constant.Key_Down));
	}

    ///////////////////////////////////////////////////////////////////
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(mouseMode){
    		if(multitouch){
    			onMultiTouchEvent(event);
    		}
    		return detector.onTouchEvent(event);
    	}
    	else
    		return super.onTouchEvent(event);
    	
    }
	
    public boolean onMenuItemClick(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.settingsMenuItem:
			startActivity(new Intent(this, SettingsActivity.class));
			break;
		case R.id.switchModeMenuItem:
			preferences.edit().putBoolean("MOUSE", !mouseMode).commit();
			finish();
			startActivity(getIntent());
			break;
		case R.id.connectionsMenuItem:
			startActivity(new Intent(this, ConnectionActivity.class));
			break;
		case R.id.gestureMenuItem:
			gestureEnabled = !gestureEnabled;
			preferences.edit().putBoolean("GESTURE_PRESENTER", gestureEnabled).commit();
			if(!gestureEnabled){
	        	sManager.unregisterListener(this);
	        	GesturePattern.clear();
	        }else{
	        	loadSensor();
	        }			
			break;
		case R.id.aboutMenuItem:
			startActivity(new Intent(this, AboutActivity.class));
			break;
		}
		return false;
	}

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	if(mouseMode){
        	menu.findItem(R.id.switchModeMenuItem).setTitle(R.string.enablePresenter);
        }else{
        	menu.findItem(R.id.switchModeMenuItem).setTitle(R.string.enableMouse);
        }
        menu.findItem(R.id.gestureMenuItem).setChecked(gestureEnabled);
    	return super.onPrepareOptionsMenu(menu);
    }
	
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
	
    public void onSensorChanged(SensorEvent event) {
		int key = GesturePattern.getKey(event.values);
		if(key != 0){
			((Vibrator) getSystemService(VIBRATOR_SERVICE)).vibrate(20);
			byte[] head = new byte[]{Constant.KEYCOMMAND, 1, 0};
			byte[] bs = ByteBuffer.allocate(4).putInt(key).array();
						
			byte[] result = new byte[7];
			System.arraycopy(head, 0, result, 0, 3);
			System.arraycopy(bs, 0, result, 3, 4);
			queue.add(new Command(result));
		}
	}

    public void onMultiTouchEvent(MotionEvent e){
    	if(e.getAction() == MotionEvent.ACTION_DOWN){
    		p0y0 = 0;
    		p0y1 = 0;
    	}
    	
    	if(e.getAction() == MotionEvent.ACTION_MOVE && e.getPointerCount() == 2){
    		lim = !lim;
    		if(lim)
    			return;
    		if(p0y0 == 0){
        		p0y0 = e.getY(0);
        		p0y1 = e.getY(1);
        		return;
    		}
    		byte step = 10;
    		if(p0y0 > e.getY(0)+step && p0y1 > e.getY(1)+step){
    			queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.SCROLL_VERTICAL, (byte)-1}));
    		}
    		if(p0y0+step < e.getY(0) && p0y1+step < e.getY(1)){
    			queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.SCROLL_VERTICAL, (byte)1}));
    		}
    		p0y0 = e.getY(0);
    		p0y1 = e.getY(1);   		
    		
    	}
        
    }
}
