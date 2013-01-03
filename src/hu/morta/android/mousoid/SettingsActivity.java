package hu.morta.android.mousoid;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import android.widget.ToggleButton;

public class SettingsActivity extends Activity implements OnCheckedChangeListener, OnSeekBarChangeListener, OnClickListener, OnLongClickListener{

	private SharedPreferences preferences = MainActivity.preferences;
	private Editor editor;

	///////////////////////////////////////////////////////////////////
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
    	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    	editor = preferences.edit();
    	((ToggleButton)findViewById(R.id.toggleButtonShowButtons)).setChecked(preferences.getBoolean("BUTTONS", true));
    	((ToggleButton)findViewById(R.id.toggleButtonMultitouch)).setChecked(preferences.getBoolean("MULTITOUCH", false));
    	((ToggleButton)findViewById(R.id.toggleButtonArrows)).setChecked(preferences.getBoolean("ARROWS_LAYOUT", true));
    	((ToggleButton)findViewById(R.id.toggleButtonMedia)).setChecked(preferences.getBoolean("MEDIA_LAYOUT", true));
    	((ToggleButton)findViewById(R.id.toggleButtonBrowser)).setChecked(preferences.getBoolean("BROWSER_LAYOUT", true));
    	((ToggleButton)findViewById(R.id.toggleButtonPresenter)).setChecked(preferences.getBoolean("PRESENTER_LAYOUT", true));
    	((ToggleButton)findViewById(R.id.toggleButtonWindows)).setChecked(preferences.getBoolean("WIN_LAYOUT", false));
    	((ToggleButton)findViewById(R.id.toggleButtonLinux)).setChecked(preferences.getBoolean("LINUX_LAYOUT", false));
    	((ToggleButton)findViewById(R.id.toggleButtonLaptop)).setChecked(preferences.getBoolean("LAPTOP_LAYOUT", false));
    	((ToggleButton)findViewById(R.id.toggleButtonEditLine)).setChecked(preferences.getBoolean("LINE_EDIT", true));
    	((ToggleButton)findViewById(R.id.toggleButtonCommandLine)).setChecked(preferences.getBoolean("RUN_COMMAND", false));
    	((ToggleButton)findViewById(R.id.toggleButtonGesture)).setChecked(preferences.getBoolean("GESTURE_PRESENTER", false));
    	((SeekBar)findViewById(R.id.resolutionSeekBar)).setProgress(preferences.getInt("RESOLUTION", 3));
    	((RadioButton)findViewById(R.id.radioDown)).setChecked(preferences.getInt("GP_KEY", Constant.Key_Down) == Constant.Key_Down);
    	((RadioButton)findViewById(R.id.radioPgDn)).setChecked(preferences.getInt("GP_KEY", Constant.Key_PageDown) == Constant.Key_PageDown);
    	
    	((ToggleButton)findViewById(R.id.toggleButtonShowButtons)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonMultitouch)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonArrows)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonMedia)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonBrowser)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonPresenter)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonWindows)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonLinux)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonLaptop)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonEditLine)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonCommandLine)).setOnCheckedChangeListener(this);
    	((ToggleButton)findViewById(R.id.toggleButtonGesture)).setOnCheckedChangeListener(this);
    	((SeekBar)findViewById(R.id.resolutionSeekBar)).setOnSeekBarChangeListener(this);
    	((Button)findViewById(R.id.buttonRestore)).setOnClickListener(this);
    	((Button)findViewById(R.id.buttonRestore)).setOnLongClickListener(this);
    	((RadioButton)findViewById(R.id.radioDown)).setOnCheckedChangeListener(this);
    	((RadioButton)findViewById(R.id.radioPgDn)).setOnCheckedChangeListener(this);
	}
	
	public void onBackPressed() {
		editor.commit();
		startActivity(new Intent(this, MainActivity.class));
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}

	///////////////////////////////////////////////////////////////////
	
	// ToggleButtons
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.toggleButtonShowButtons:
			editor.putBoolean("BUTTONS", isChecked);
			break;
		case R.id.toggleButtonMultitouch:
			editor.putBoolean("MULTITOUCH", isChecked);
			break;
		case R.id.toggleButtonArrows:
			editor.putBoolean("ARROWS_LAYOUT", isChecked);
			break;
		case R.id.toggleButtonMedia:
			editor.putBoolean("MEDIA_LAYOUT", isChecked);
			break;
		case R.id.toggleButtonBrowser:
			editor.putBoolean("BROWSER_LAYOUT", isChecked);
			break;
		case R.id.toggleButtonPresenter:
			editor.putBoolean("PRESENTER_LAYOUT", isChecked);
			break;
		case R.id.toggleButtonWindows:
			editor.putBoolean("WIN_LAYOUT", isChecked);
			break;
		case R.id.toggleButtonLinux:
			editor.putBoolean("LINUX_LAYOUT", isChecked);
			break;
		case R.id.toggleButtonLaptop:
			editor.putBoolean("LAPTOP_LAYOUT", isChecked);
			break;
		case R.id.toggleButtonEditLine:
			editor.putBoolean("LINE_EDIT", isChecked);
			break;
		case R.id.toggleButtonCommandLine:
			editor.putBoolean("RUN_COMMAND", isChecked);
			break;
		case R.id.toggleButtonGesture:
			editor.putBoolean("GESTURE_PRESENTER", isChecked);
			break;
		case R.id.radioDown:
			editor.putInt("GP_KEY", Constant.Key_Down);
			break;
		case R.id.radioPgDn:
			editor.putInt("GP_KEY", Constant.Key_PageDown);
			break;
		}
	}

	// SeekBar
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		editor.putInt("RESOLUTION", progress);
	}
	// SeekBar
	public void onStartTrackingTouch(SeekBar seekBar) {}
	// SeekBar
	public void onStopTrackingTouch(SeekBar seekBar) {}
	// Buttons
	public void onClick(View v) {
		Toast.makeText(this, R.string.longclick, Toast.LENGTH_SHORT).show();
	}
	// Delete buttons
	public boolean onLongClick(View v) {
		editor.clear();
		editor.commit();
		finish();
		startActivity(getIntent());
		return true;
	}


}
