package hu.morta.android.mousoid;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnMenuItemClickListener {
	
	class MousoidGestureListener extends GestureDetector.SimpleOnGestureListener{
		@Override
		public boolean onDoubleTap(MotionEvent e) {
//			Log.d("onDoubleTap",e.toString());
			queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.DOUBLE_CLICK, Constant.MOUSE_LEFT}));
			return true;
		}
		
//		@Override
//		public boolean onDoubleTapEvent(MotionEvent e) {
//			Log.d("onDoubleTapEvent",e.toString());
//			return true;
//		}
		
//	    @Override
//	    public boolean onSingleTapUp(MotionEvent ev) {
//	    	Log.d("onSingleTapUp",ev.toString());
//	    	return true;
//	    }
	    
//	    @Override
//	    public void onShowPress(MotionEvent ev) {
//	    	Log.d("onShowPress",ev.toString());
//	    }
	    
	    @Override
	    public void onLongPress(MotionEvent ev) {
//	    	Log.d("onLongPress",ev.toString());
			queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.PRESS, Constant.MOUSE_LEFT}));
	    }
	    
	    // for move
	    @Override
	    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//	    	Log.d("onScroll",e1.toString() + Float.toString(distanceX)+" "+Float.toString(distanceY));
	    	queue.add(new Command(new byte[]{Constant.MOUSEMOVE, (byte)(distanceX*mouseResolution), (byte)(distanceY*mouseResolution)}));
	    	return true;
	    }
	    
//	    @Override
//	    public boolean onDown(MotionEvent ev) {
//	    	Log.d("onDownd",ev.toString());
//	    	return true;
//	    }
	    
//	    @Override
//	    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
//	    	Log.d("onFling d",e1.toString());
//	    	Log.d("onFling e2",e2.toString());
//	    	return true;
//	    }
	    
	    // for click
	    @Override
	    public boolean onSingleTapConfirmed(MotionEvent e) {
//	    	Log.d("onSingleTapConfirmed",e.toString());
	    	queue.add(new Command(new byte[]{Constant.MOUSEBUTTON, Constant.CLICK, Constant.MOUSE_LEFT}));
	    	return true;
	    }
	    
	}
	
	class MouseButtonsListener implements OnClickListener{
		public void onClick(View v) {
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
			byte[] head = new byte[]{Constant.KEYCOMMAND, 0, 0};
			byte[] bs = new byte[]{};
			
			switch (v.getId()) {
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
			case R.id.buttonSleep:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_Suspend).array();
				head[1] = 1;				
				break;
			case R.id.buttonPower:
				bs = ByteBuffer.allocate(4).putInt(Constant.Key_PowerDown).array();
				head[1] = 1;				
				break;
			case R.id.buttonLineOk:
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
	private boolean mouseMode;
	private float mouseResolution;
	private boolean multitouch;
	private boolean showButtons;
	
	///////////////////////////////////////////////////////////////////
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
    	preferences = getSharedPreferences("Mousoid", 0);
        Log.i("MainActivity", "onCreate");        
        if(ConnectionManager.getConnection() == null){
        	startActivity(new Intent(this, ConnectionActivity.class));
        }
        // TODO
    	mouseMode = preferences.getBoolean("MOUSE", false);
        
    }
    
    ///////////////////////////////////////////////////////////////////
    @Override
    protected void onResume() {
        Log.i("MainActivity", "onResume");
    	if(ConnectionManager.getConnection() == null){
        	startActivity(new Intent(this, ConnectionActivity.class));
        }if(mouseMode){
    		mouseResolution = preferences.getFloat("RESOLUTION", 1.7F);
    		multitouch = preferences.getBoolean("MULTITOUCH", false);
    		showButtons = preferences.getBoolean("BUTTONS", true);
    		loadMouseInterface();
    	}else{
    		loadPresenterInterface();
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
        if(mouseMode){
        	menu.findItem(R.id.switchModeMenuItem).setTitle(R.string.enablePresenter);
        	menu.findItem(R.id.multitouchMenuItem).setVisible(true);
        	menu.findItem(R.id.showButtons).setVisible(true);
        	menu.findItem(R.id.multitouchMenuItem).setChecked(multitouch);
        	menu.findItem(R.id.showButtons).setChecked(showButtons);
        }else{
        	menu.findItem(R.id.switchModeMenuItem).setTitle(R.string.enableMouse);
        	menu.findItem(R.id.multitouchMenuItem).setVisible(false);
        	menu.findItem(R.id.showButtons).setVisible(false);
        }
        menu.findItem(R.id.settingsMenuItem).setOnMenuItemClickListener(this);
        menu.findItem(R.id.switchModeMenuItem).setOnMenuItemClickListener(this);
        menu.findItem(R.id.multitouchMenuItem).setOnMenuItemClickListener(this);
        menu.findItem(R.id.showButtons).setOnMenuItemClickListener(this);
        menu.findItem(R.id.connectionsMenuItem).setOnMenuItemClickListener(this);
        menu.findItem(R.id.aboutMenuItem).setOnMenuItemClickListener(this);
        return true;
    }
    
    @Override
    public void onBackPressed() {
    	preferences.edit().putBoolean("MOUSE", !mouseMode).commit();
		finish();
		startActivity(getIntent());
    }
    
    ///////////////////////////////////////////////////////////////////

    void loadMouseInterface(){
    	setContentView(R.layout.main_mouse);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
    	detector = new GestureDetector(this, new MousoidGestureListener());
    	if(preferences.getBoolean("BUTTONS", true)){
    		MouseButtonsListener listener = new MouseButtonsListener();
    		findViewById(R.id.buttonLeft).setOnClickListener(listener);
    		findViewById(R.id.buttonMiddle).setOnClickListener(listener);
    		findViewById(R.id.buttonRight).setOnClickListener(listener);
    		findViewById(R.id.buttonScrollUp).setOnClickListener(listener);
    		findViewById(R.id.buttonScrollDown).setOnClickListener(listener);
    	}
    }
    
    void loadPresenterInterface(){
    	setContentView(R.layout.main_presenter);
    	listener = new PresenterButtonsListener();
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	findViewById(R.id.lineLayout).setVisibility(preferences.getBoolean("LINE_EDIT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.runCommandLayout).setVisibility(preferences.getBoolean("RUN_COMMAND", false) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.arrowsLayout).setVisibility(preferences.getBoolean("ARROWS_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.mediaLayout).setVisibility(preferences.getBoolean("MEDIA_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.volumeLayout).setVisibility(preferences.getBoolean("MEDIA_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.browserLayout).setVisibility(preferences.getBoolean("BROWSER_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.presenterLayout).setVisibility(preferences.getBoolean("PRESENTER_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.jumpLayout).setVisibility(preferences.getBoolean("PRESENTER_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.controlLayout).setVisibility(View.VISIBLE);
    	findViewById(R.id.winLayout).setVisibility(preferences.getBoolean("WIN_LAYOUT", false) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.linuxLayout).setVisibility(preferences.getBoolean("LINUX_LAYOUT", false) == true ? View.VISIBLE : View.GONE);
    	findViewById(R.id.laptopLayout).setVisibility(preferences.getBoolean("LAPTOP_LAYOUT", true) == true ? View.VISIBLE : View.GONE);
    	
    	findViewById(R.id.buttonLineOk).setOnClickListener(listener);
    	//findViewById(R.id.buttonCommandOk).setOnClickListener(listener); TODO
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
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if(mouseMode)
    		return detector.onTouchEvent(event);
    	else
    		return super.onTouchEvent(event);
    	
    }

	
    public boolean onMenuItemClick(MenuItem item) {
		// TODO Auto-generated method stub
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
		case R.id.multitouchMenuItem:
			preferences.edit().putBoolean("MULTITOUCH", !multitouch).commit();
			finish();
			startActivity(getIntent());
			break;
		case R.id.showButtons:
			preferences.edit().putBoolean("BUTTONS", !showButtons).commit();
			finish();
			startActivity(getIntent());
			break;

		default:
			Log.i("Menu", item.toString());
			break;
		}
		return false;
	}

}
