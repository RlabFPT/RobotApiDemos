/**
 * Copyright (C) 2013 FPT Corporation
 * @author: Robot Team (FTI)
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fpt.robot.example.apis.tracking;

import android.os.Bundle;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.tracking.RobotSoundTracker;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

/**
 * SoundTracking class allows you to use sound (clap sound) tracking of robot
 * 
 * @author Robot Team (FTI)
 * 
 */
public class SoundTracking extends RobotApiDemoActivity {

	private final static String TAG = "SoundTracking";
	private final static String INSTRUCTIONS = "SoundTracking class allows you to use sound (clap sound) tracking of robot "
			+ "When start sound tracking, robot will tracking sound. Check Whole Body to enable tracking whole body gesture. "
			+ "Click Start and Stop button to start and stop sound tracking";
	// check box enable tracking who body
	private CheckBox cbWholeBody;
	// button start sound tracking
	private Button btStart;
	// button stop sound tracking
	private Button btStop;

	// private Button btGetTarget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// set event click for button start sound tracking
		btStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startSoundTracking();
			}
		});
		// set event click for button stop sound tracking
		btStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopSoundTracking();
			}
		});
		/*
		 * btGetTarget.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { getTargetPosition(); } });
		 */
	}

	/**
	 * Start sound tracking, sound tracking will run until stop is called
	 */
	private void startSoundTracking() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// check if sound tracking is running or not
					if (RobotSoundTracker.isActive(getRobot())) {
						makeToast("Sound tracking is running");
						return;
					} else {
						showProgress("Start Sound tracking...");
						// get value from check box and set tracking whole body
						RobotSoundTracker.setTrackingWholeBody(getRobot(),
								cbWholeBody.isChecked());
						// start sound tracking
						RobotSoundTracker.startTracker(getRobot());
						cancelProgress();
					}
				} catch (RobotException e) {
					cancelProgress();
					makeToast("start Sound tracking failed " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Stop sound tracking
	 */
	private void stopSoundTracking() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// check if sound tracking is running or not
					if (!RobotSoundTracker.isActive(getRobot())) {
						makeToast("Sound tracking is not running!");
						return;
					} else {
						showProgress("Stop Sound tracking...");
						// stop sound tracking
						RobotSoundTracker.stopTracker(getRobot());
						cancelProgress();
					}
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Stop Sound tracking failed: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Get Target position while tracking
	 */
	/*
	private void getTargetPosition() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean newData = false;
				try {
					if (RobotSoundTracker.isNewData(getRobot())) {
						newData = true;
					}
					showProgress("Getting target position...");
					RobotPosition3D target = RobotSoundTracker
							.getTargetPosition(getRobot());
					cancelProgress();
					makeToast("NewData: " + newData + " Target: x,y,z: "
							+ target.x + ", " + target.y + ", " + target.z);
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Get target postion failed: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}
*/
	@Override
	protected void onDestroy() {
		try {
			// stop sound tracking when activity is destroyed
			RobotSoundTracker.stopTracker(getRobot());
		} catch (RobotException e) {
			e.printStackTrace();
		}
		super.onDestroy();
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
		return R.layout.activity_sound_tracking;
	}

	@Override
	protected void settingView() {
		cbWholeBody = (CheckBox) findViewById(R.id.cbSoundTrackingWholeBody);
		btStart = (Button) findViewById(R.id.btStartSoundTracking);
		btStop = (Button) findViewById(R.id.btStopSoundTracking);
		//btGetTarget = (Button) findViewById(R.id.btSoundTrackingGetTarget);
	}	
}
