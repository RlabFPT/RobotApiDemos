/*
 * Copyright (C) 2013 FPT Corporation
 * @author: Robot Team (FTI)
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fpt.robot.example.apis.leds;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fpt.robot.RobotException;
import com.fpt.robot.RobotHardware;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.leds.RobotLeds;
/**
 * Leds class is used to turn on, turn off leds and run some led animations
 * @author Robot Team (FTI)
 */
public class Leds extends RobotApiDemoActivity {
	private static final String TAG = "Leds";
	private static final String INSTRUCTIONS = "Leds class allows you to turn on and turn off single led. " +
			"Select led name which you want to turn on or turn off led and then click Led On or Led Off. You can also " +
			"run some predefined led animations";
	// button turn on single led
	private Button btLedOn;
	// button turn off single led
	private Button btLedOff;
	// button run ear led animation
	private Button btEarLeds;
	// button run fade led animation
	private Button btFade;
	// button run face led animation with RGB
	private Button btFadeRGB;
	// button run face led animation with list RGB
	private Button btFadeListRGB;
	// spinner list single led names
	private Spinner spLeds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		spLeds = (Spinner) findViewById(R.id.spLeds);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		/* Brain */		
		adapter.add(RobotHardware.Led.Brain.LED_0);
		adapter.add(RobotHardware.Led.Brain.LED_1);
		adapter.add(RobotHardware.Led.Brain.LED_2);
		adapter.add(RobotHardware.Led.Brain.LED_3);
		adapter.add(RobotHardware.Led.Brain.LED_4);
		adapter.add(RobotHardware.Led.Brain.LED_5);
		adapter.add(RobotHardware.Led.Brain.LED_6);
		adapter.add(RobotHardware.Led.Brain.LED_7);
		adapter.add(RobotHardware.Led.Brain.LED_8);
		adapter.add(RobotHardware.Led.Brain.LED_9);
		adapter.add(RobotHardware.Led.Brain.LED_10);
		adapter.add(RobotHardware.Led.Brain.LED_11);
		/* LeftEye */
		adapter.add(RobotHardware.Led.LeftEye.LED_0);
		adapter.add(RobotHardware.Led.LeftEye.LED_1);
		adapter.add(RobotHardware.Led.LeftEye.LED_2);
		adapter.add(RobotHardware.Led.LeftEye.LED_3);
		adapter.add(RobotHardware.Led.LeftEye.LED_4);
		adapter.add(RobotHardware.Led.LeftEye.LED_5);
		adapter.add(RobotHardware.Led.LeftEye.LED_6);
		adapter.add(RobotHardware.Led.LeftEye.LED_7);
		/* RightEye */
		adapter.add(RobotHardware.Led.RightEye.LED_0);
		adapter.add(RobotHardware.Led.RightEye.LED_1);
		adapter.add(RobotHardware.Led.RightEye.LED_2);
		adapter.add(RobotHardware.Led.RightEye.LED_3);
		adapter.add(RobotHardware.Led.RightEye.LED_4);
		adapter.add(RobotHardware.Led.RightEye.LED_5);
		adapter.add(RobotHardware.Led.RightEye.LED_6);
		adapter.add(RobotHardware.Led.RightEye.LED_7);
		/* LeftEar */
		adapter.add(RobotHardware.Led.LeftEar.LED_1);
		adapter.add(RobotHardware.Led.LeftEar.LED_2);
		adapter.add(RobotHardware.Led.LeftEar.LED_3);
		adapter.add(RobotHardware.Led.LeftEar.LED_4);
		adapter.add(RobotHardware.Led.LeftEar.LED_5);
		adapter.add(RobotHardware.Led.LeftEar.LED_6);
		adapter.add(RobotHardware.Led.LeftEar.LED_7);
		adapter.add(RobotHardware.Led.LeftEar.LED_8);
		adapter.add(RobotHardware.Led.LeftEar.LED_9);
		adapter.add(RobotHardware.Led.LeftEar.LED_10);
		/* RightEar */
		adapter.add(RobotHardware.Led.RightEar.LED_1);
		adapter.add(RobotHardware.Led.RightEar.LED_2);
		adapter.add(RobotHardware.Led.RightEar.LED_3);
		adapter.add(RobotHardware.Led.RightEar.LED_4);
		adapter.add(RobotHardware.Led.RightEar.LED_5);
		adapter.add(RobotHardware.Led.RightEar.LED_6);
		adapter.add(RobotHardware.Led.RightEar.LED_7);
		adapter.add(RobotHardware.Led.RightEar.LED_8);
		adapter.add(RobotHardware.Led.RightEar.LED_9);
		adapter.add(RobotHardware.Led.RightEar.LED_10);
		/* Chest */
		adapter.add(RobotHardware.Led.Chest.BLUE);
		adapter.add(RobotHardware.Led.Chest.GREEN);
		adapter.add(RobotHardware.Led.Chest.RED);
		/* LeftFoot */
		adapter.add(RobotHardware.Led.LeftFoot.BLUE);
		adapter.add(RobotHardware.Led.LeftFoot.GREEN);
		adapter.add(RobotHardware.Led.LeftFoot.RED);
		/* RightFoot */
		adapter.add(RobotHardware.Led.RightFoot.BLUE);
		adapter.add(RobotHardware.Led.RightFoot.GREEN);
		adapter.add(RobotHardware.Led.RightFoot.RED);
		
		/* All Leds */
        adapter.add(RobotHardware.Led.GroupName.ALL_LEDS);
        adapter.add(RobotHardware.Led.GroupName.ALL_LEDS_BLUE);
        adapter.add(RobotHardware.Led.GroupName.ALL_LEDS_GREEN);
        adapter.add(RobotHardware.Led.GroupName.ALL_LEDS_RED);
        /* Brain Leds */
        adapter.add(RobotHardware.Led.GroupName.BRAIN_LEDS);
        adapter.add(RobotHardware.Led.GroupName.BRAIN_LEDS_BACK);
        adapter.add(RobotHardware.Led.GroupName.BRAIN_LEDS_FRONT);
        adapter.add(RobotHardware.Led.GroupName.BRAIN_LEDS_LEFT);
        adapter.add(RobotHardware.Led.GroupName.BRAIN_LEDS_MIDDLE);
        adapter.add(RobotHardware.Led.GroupName.BRAIN_LEDS_RIGHT);
        /* Chest Leds */
        adapter.add(RobotHardware.Led.GroupName.CHEST_LEDS);
        /* Ear Leds */
        adapter.add(RobotHardware.Led.GroupName.EAR_LEDS);
        adapter.add(RobotHardware.Led.GroupName.LEFT_EAR_LEDS);
        adapter.add(RobotHardware.Led.GroupName.LEFT_EAR_LEDS_BACK);
        adapter.add(RobotHardware.Led.GroupName.LEFT_EAR_LEDS_EVEN);
        adapter.add(RobotHardware.Led.GroupName.LEFT_EAR_LEDS_FRONT);
        adapter.add(RobotHardware.Led.GroupName.LEFT_EAR_LEDS_ODD);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_EAR_LEDS);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_EAR_LEDS_BACK);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_EAR_LEDS_EVEN);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_EAR_LEDS_FRONT);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_EAR_LEDS_ODD);
        /* Face Leds */
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_0);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_1);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_2);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_3);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_4);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_5);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_6);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_7);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_LEFT_0);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_LEFT_1);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_LEFT_2);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_LEFT_3);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_LEFT_4);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_LEFT_5);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_LEFT_6);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_LEFT_7);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_RIGHT_0);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_RIGHT_1);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_RIGHT_2);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_RIGHT_3);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_RIGHT_4);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_RIGHT_5);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_RIGHT_6);
        adapter.add(RobotHardware.Led.GroupName.FACE_LED_RIGHT_7);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_BOTTOM);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_EXTERNAL);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_INTERNAL);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_LEFT_BOTTOM);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_LEFT_EXTERNAL);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_LEFT_INTERNAL);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_LEFT_TOP);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_RIGHT_BOTTOM);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_RIGHT_EXTERNAL);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_RIGHT_INTERNAL);
        adapter.add(RobotHardware.Led.GroupName.FACE_LEDS_RIGHT_TOP);
        adapter.add(RobotHardware.Led.GroupName.LEFT_FACE_LEDS);
        adapter.add(RobotHardware.Led.GroupName.LEFT_FACE_LEDS_BLUE);
        adapter.add(RobotHardware.Led.GroupName.LEFT_FACE_LEDS_GREEN);
        adapter.add(RobotHardware.Led.GroupName.LEFT_FACE_LEDS_RED);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_FACE_LEDS);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_FACE_LEDS_BLUE);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_FACE_LEDS_GREEN);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_FACE_LEDS_RED);
        /* Feet Leds */
        adapter.add(RobotHardware.Led.GroupName.FEET_LEDS);
        adapter.add(RobotHardware.Led.GroupName.LEFT_FOOT_LEDS);
        adapter.add(RobotHardware.Led.GroupName.RIGHT_FOOT_LEDS);
        // set list led names for spinner
		spLeds.setAdapter(adapter);

		btLedOn = (Button) findViewById(R.id.btLedOn);
		// set event click for button turn on led
		btLedOn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ledOn();
			}
		});

		btLedOff = (Button) findViewById(R.id.btLedOff);
		// set event click for button turn off led
		btLedOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ledOff();
			}
		});
		
		btEarLeds = (Button) findViewById(R.id.btEarLedsSetAngle);
		// set event click for button ear led
		btEarLeds.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				earLedsSetAngle();
			}
		});
		
		btFade = (Button) findViewById(R.id.btLedFade);
		// set event click for button fade led
		btFade.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fade();
			}
		});
		
		btFadeRGB = (Button) findViewById(R.id.btFadeRGB);
		// set event click for button fade led with RGB
		btFadeRGB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fadeRGB();
			}
		});
		btFadeListRGB = (Button) findViewById(R.id.btFadeListRGB);
		// set event click for button fade led with list RGB
		btFadeListRGB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				fadeListRGB();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.robot_api_demo_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_help:
			showInfoDialog(TAG, INSTRUCTIONS);
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_leds;
	}

	/**
	 * Turn on led
	 */
	protected void ledOn() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					return;
				}
				// get led name
				String ledName = spLeds.getSelectedItem().toString();
				boolean result = false;
				showProgress("turning LED on...");
				try {
					// turn on led
					result = RobotLeds.ledOn(getRobot(), ledName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("turn on led failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("turn on led successfully!");
				} else {
					makeToast("turn on led failed!");
				}
			}
		}).start();
	}

	/**
	 * Turn off led
	 */
	protected void ledOff() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					return;
				}
				// get led name
				String ledName = spLeds.getSelectedItem().toString();
				boolean result = false;
				showProgress("turning LED off...");
				try {
					// turn of led
					result = RobotLeds.ledOff(getRobot(), ledName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("turn off led failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("turn off led successfully!");
				} else {
					makeToast("turn off led failed!");
				}
			}
		}).start();
	}

	/**
	 * Robot start ear leds with angle
	 */
	private void earLedsSetAngle() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showProgress("ear leds set angle...");
				int degree = 90;
				float duration = 2.0f;
				boolean leaveOnAtEnd = true;
				boolean result = false;
				try {
					// run ear leds with degree, duration, leaveOnAtEnd
					result = RobotLeds.earLedsSetAngle(getRobot(), degree,
							duration, leaveOnAtEnd);
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread("Leds", "earLedsSetAngle failed: "
							+ e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Leds", "earLedsSetAngle failed");
				} else {
					makeToast("earLedsSetAngle successfully");
				}				
			}
		}).start();
	}
	
	/**
	 * Robot start fade
	 */
	private void fade() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showProgress("fade ...");
				String ledOrGroupName = (String) spLeds.getSelectedItem();
				int intensity = 1;
				float duration = 2.0f;
				boolean async = false;
				boolean result = false;
				try {
					// turn on led or group first
					RobotLeds.ledOn(getRobot(), ledOrGroupName);
					// fade, after fade, led will turn to state is off
					result = RobotLeds.fade(getRobot(), ledOrGroupName, intensity, duration, async);					
					
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread("Leds", "Fade failed: "
							+ e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Leds", "Fade failed");
				} else {
					makeToast("Fade successfully");
				}				
			}
		}).start();
	}
	
	/**
	 * fade led with list RGB
	 */
	private void fadeListRGB(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				showProgress("fade list RGB...");
				String ledOrGroupName = (String) spLeds.getSelectedItem();
				int[] rgbList = new int[] {0xFFFFFF,0xFF0000,0x0F0FFF};
				float[] timeList = new float[] {1.0f,1.0f,1.0f};
				boolean async = false;
				boolean result = false;
				try {
					// turn on led first
					RobotLeds.ledOn(getRobot(), ledOrGroupName);
					// fade list RGB, after fade list RGB, led will turn the last color in rgbList
					result = RobotLeds.fadeListRGB(getRobot(), ledOrGroupName, rgbList, timeList, async);					
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread("Leds", "Fade list RGB failed: "
							+ e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Leds", "Fade list RGB failed");
				} else {
					makeToast("Fade list RGB successfully");
				}					
			}
		}).start();
	}
	
	/**
	 * Fade led with RGB color
	 */
	private void fadeRGB(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				showProgress("fade RGB...");
				String ledOrGroupName = (String) spLeds.getSelectedItem();
				int rgb = 0xFF0000;
				float duration = 1.0f;
				boolean async = false;
				boolean result = false;
				try {
					// turn on led first
					RobotLeds.ledOn(getRobot(), ledOrGroupName);
					// fade RGB, after fade RGB led will turn color @param (rgb)
					result = RobotLeds.fadeRGB(getRobot(), ledOrGroupName, rgb, duration, async);					
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread("Leds", "Fade RGB failed: "
							+ e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Leds", "Fade RGB failed");
				} else {
					makeToast("Fade RGB successfully");
				}					
			}
		}).start();
	}
}
