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
 * LedGroups class is used to start some group led of robot
 * @author Robot Team (FTI)
 */
public class LedGroups extends RobotApiDemoActivity {
	private static final String TAG = "LedGroups";
	private static final String INSTRUCTIONS = "LedGroups class allows you to turn on and turn off all single leds " +
			"in a group led. Select group name which you want to turn on or turn off led and then click Led On or Led Off";	
	// button turn on group led
	private Button btLedGroupOn;
	// button turn off group led
	private Button btLedGroupOff;
	// spinner list group led name
	private Spinner spLedGroups;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
        spLedGroups = (Spinner)findViewById(R.id.spLedGroups);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
        		this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        // set list group name to spinner
        spLedGroups.setAdapter(adapter);
        
        btLedGroupOn = (Button)findViewById(R.id.btLedGroupOn);
        // set event click for button turn on group led
        btLedGroupOn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ledGroupOn();
			}
		});
        
        btLedGroupOff = (Button)findViewById(R.id.btLedGroupOff);
        // set event click for button turn off group led
        btLedGroupOff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ledGroupOff();
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
		return R.layout.activity_led_groups;
	}
	
	// Turn on group led
	protected void ledGroupOn() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					return;
				}
				// get name of group led
				String ledGroupName = spLedGroups.getSelectedItem().toString();
				boolean result = false;
				showProgress("turning LED group on...");
				try {
					// turn on group led
					result = RobotLeds.ledOn(getRobot(), ledGroupName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("turn on led group failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("turn on led group successfully!");
				} else {
					makeToast("turn on led group failed!");
				}
			}
		}).start();
	}

	// Turn of group led
	protected void ledGroupOff() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (getRobot() == null) {
					return;
				}
				// get name of group name
				String ledGroupName = spLedGroups.getSelectedItem().toString();
				boolean result = false;
				showProgress("turning LED group off...");
				try {
					// turn off led group
					result = RobotLeds.ledOff(getRobot(), ledGroupName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("turn off led group failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("turn off led group successfully!");
				} else {
					makeToast("turn off led group failed!");
				}
			}
		}).start();
	}
}
