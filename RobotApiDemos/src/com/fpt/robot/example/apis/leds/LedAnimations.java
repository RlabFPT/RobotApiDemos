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
import com.fpt.robot.binder.RobotValue;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.leds.RobotLedAnimation;

/**
 * LedAnimation class is used to play led animation which is defined on robot
 * @author Robot Team (FTI)
 */
public class LedAnimations extends RobotApiDemoActivity {
	private static final String TAG = "LedAnimations";
	private static final String INSTRUCTIONS = "LedAnimations class allows you to run some predefined led animations on robot";
	// button start led animation
	private Button btLedAnimationStart;
	// button stop led animation
	private Button btLedAnimationStop;
	// button stop all led animations
	private Button btLedAnimationStopAll;
	// button run predefined animation twinkle eyes
	private Button btStartTwinkleEyes;
	// button stop predefined animation twinkle eyes
	private Button btStopTwinkleEyes;
	// button run predefined animation rotate eyes
	private Button btRotateEyes;
	// button run predefined animation blink eyes
	private Button btBlinkEyes;
	// spinner list animations name
	private Spinner spLedAnimationList;
	private ArrayAdapter<String> ledAnimationListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		spLedAnimationList = (Spinner)findViewById(R.id.spLedAnimationList);
		ledAnimationListAdapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_item);
		ledAnimationListAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		ledAnimationListAdapter.setNotifyOnChange(true);
		// set list animations name to spinner
		spLedAnimationList.setAdapter(ledAnimationListAdapter);
        
		btLedAnimationStart = (Button)findViewById(R.id.btLedAnimationStart);
		// set event click for button start led animation
		btLedAnimationStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startAnimation();
			}
		});
        
		btLedAnimationStop = (Button)findViewById(R.id.btLedAnimationStop);
		// set event click for button stop led animation
		btLedAnimationStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopAnimation();
			}
		});
		
		btLedAnimationStopAll = (Button)findViewById(R.id.btLedAnimationStopAll);
		// set event click for button stop all led animations
		btLedAnimationStopAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopAllAnimations();
			}
		});
		
		btStartTwinkleEyes = (Button) findViewById(R.id.btStartTwinkEyes);
		// set event click for button start twinkle eyes animation
		btStartTwinkleEyes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				twinkleEyesStart();
			}
		});
		
		btStopTwinkleEyes = (Button) findViewById(R.id.btStopTwinkleEyes);
		// set event click for button stop twinkle eyes animation
		btStopTwinkleEyes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				twinkleEyesStop();
			}
		});
		btRotateEyes = (Button) findViewById(R.id.btRotateEyes);
		// set event click for button run rotate eyes animation
		btRotateEyes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				rotateEyes();
			}
		});
		
		btBlinkEyes = (Button) findViewById(R.id.btBlinkEyes);
		// set event click for button run blink eyes animation
		btBlinkEyes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				blinkEyes();
			}
		});
		// get list animations from robot
		refreshLedAnimationList();
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
		return R.layout.activity_led_animations;
	}

	/**
	 * Get list led animation on robot
	 */
	private void refreshLedAnimationList() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				showProgress("getting animation list...");
				String[] animationList = null;
				try {
					// start get list animation
					animationList = RobotLedAnimation.getAnimationList(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					makeToast("get animation list failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (animationList == null || animationList.length == 0) {
				    showInfoDialogFromThread("Get led animation list", 
				    		"None animation available!");
				    return;
				}
				// update list animations to spinner
				updateLedAnimationList(animationList);
				
			}
		}).start();
	}

	/**
	 * Update list animation to adapter
	 * @param animationList
	 */
	protected void updateLedAnimationList(final String[] animationList) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				ledAnimationListAdapter.clear();
				for (int i = 0; i < animationList.length; i++) {
					ledAnimationListAdapter.add(animationList[i]);
				}
			}
		});
	}

	/**
	 * Run led animation, led animation will run until stopAnimation is called
	 */
	protected void startAnimation() {
		// get led animation from spinner
		final String ledAnimation = spLedAnimationList.getSelectedItem().toString();
		if (ledAnimation == null || ledAnimation.isEmpty()) {
			showInfoDialog("Start Animation", "Invalid led animation!");
			return;
		}
		if (ledAnimation.equals("emotion")) {
			showInfoDialog("Start Animation", "Animation [emotion] is used to run Led Emotion!");
			return;
		}
		new Thread(new Runnable() {			
			@Override
			public void run() {
				boolean result = false;
				showProgress("starting animation [" + ledAnimation + "]...");
				try {
					// check if led animation is running
					result = RobotLedAnimation.isAnimationRunning(getRobot(), ledAnimation);
					if (result) {
					    cancelProgress();
					    showInfoDialogFromThread("Start Animation", 
					    		"animation '" + ledAnimation + "' is running!");
						return;
					}
					if (ledAnimation.equals("twinkle_eyes")) {
						/**
						 * start animation with defined RobotValue
						 * @see twinkleEyesStart
						 */
						result = RobotLedAnimation.startAnimation(getRobot(), ledAnimation, 
												1.0f, 1, new RobotValue(0x00FF0000));
					} else if (ledAnimation.equals("blink_eyes")) {
						/**
						 * start animation with defined RobotValue
						 * @see blinkEyes
						 */
						result = RobotLedAnimation.startAnimation(getRobot(), ledAnimation, 
								1.0f, 1, new RobotValue());
					}
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("start animation '" + ledAnimation + "' failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("start animation '" + ledAnimation + "' successful!");
				} else {
					makeToast("start animation '" + ledAnimation + "' failed!");
				}
			}
		}).start();
	}

	/**
	 * Stop animation which is running
	 */
	protected void stopAnimation() {
		// get led animation name from spinner
		final String ledAnimation = spLedAnimationList.getSelectedItem().toString();
		if (ledAnimation == null || ledAnimation.isEmpty()) {
			showInfoDialog("Stop Animation", "Invalid led animation!");
			return;
		}
		if (ledAnimation.equals("emotion")) {
			showInfoDialog("Stop Animation", "Animation [emotion] is used to run Led Emotion!");
			return;
		}
		new Thread(new Runnable() {			
			@Override
			public void run() {
				boolean result = false;
				showProgress("stopping animation [" + ledAnimation + "]...");
				try {
					// check animation running first
					result = RobotLedAnimation.isAnimationRunning(getRobot(), ledAnimation);
					if (!result) {
					    cancelProgress();
					    showInfoDialogFromThread("Stop Animation", 
					    		"animation '" + ledAnimation + "' is not running!");
						return;
					}
					// stop exactly animation
					result = RobotLedAnimation.stopAnimation(getRobot(), ledAnimation);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("stop animation '" + ledAnimation + "' failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("stop animation '" + ledAnimation + "' successful!");
				} else {
					makeToast("stop animation '" + ledAnimation + "' failed!");
				}
			}
		}).start();
	}
	

	/**
	 * Stop all animations which are running
	 */
	protected void stopAllAnimations() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				boolean result = false;
				showProgress("stopping all animations...");
				try {
					// stop all animations
					result = RobotLedAnimation.stopAllAnimations(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("stop all animations failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("stop all animations successful!");
				} else {
					makeToast("stop all animations failed!");
				}
			}
		}).start();
	}
	
	/**
	 * Rotate eyes
	 */
	private void rotateEyes() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				boolean result = false;
				int rgb = 0xFF0000; // red
				float timeForRotation = 1.0f;
				float totalDuration = 1.5f;
				boolean async = false; // non blocking
				showProgress("Rotate eyes animations...");
				try {
					// rotate eyes, after rotate eyes, led will have last state and led is on
					result = RobotLedAnimation.rotateEyes(getRobot(), rgb, timeForRotation, totalDuration, async);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("Rotate eyes animations failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("Rotate eyes animations successful!");
				} else {
					makeToast("Rotate eyes animations failed!");
				}
			}
		}).start();
	}
	
	/**
	 * Blink eyes
	 */
	private void blinkEyes() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				boolean result = false;				
				showProgress("Blink eyes animations...");
				try {
					// blink eyes, after blink eyes, led will have last state and led is on
					result = RobotLedAnimation.blinkEyes(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("Blink eyes animations failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("Blink eyes animations successful!");
				} else {
					makeToast("Blink eyes animations failed!");
				}
			}
		}).start();
	}
	
	/**
	 * Start twinkle eyes, led will on until twinkleEyesStop or stopAnimation is called
	 */
	private void twinkleEyesStart() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				boolean result = false;			
				int rgb = 0xFF0000; // red
				float onTime = 1.0f;
				float offTime = 1.0f;
				showProgress("Twinkle eyes animations starting...");
				try {
					// start twinkle eyes with color is rgb color
					result = RobotLedAnimation.twinkleEyesStart(getRobot(), rgb, onTime, offTime);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("Twinkle eyes animations start failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("Twinkle eyes animations start successful!");
				} else {
					makeToast("Twinkle eyes animations start failed!");
				}
			}
		}).start();
	}
	
	/**
	 * Twinkle Eyes stop
	 */
	private void twinkleEyesStop() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				boolean result = false;							
				showProgress("Twinkle eyes animations stopping...");
				try {
					// stop twinkle eyes
					result = RobotLedAnimation.twinkleEyesStop(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("Twinkle eyes animations stop failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("Twinkle eyes animations stop successful!");
				} else {
					makeToast("Twinkle eyes animations stop failed!");
				}
			}
		}).start();
	}
	
}
