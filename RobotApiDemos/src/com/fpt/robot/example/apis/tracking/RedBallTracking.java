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
package com.fpt.robot.example.apis.tracking;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.tracking.RobotRedBallTracker;

/**
 * This class is used to use red ball tracking of robot. When start red ball
 * tracking, robot will follow the red ball
 * 
 * @author Robot Team (FTI)
 * 
 */
public class RedBallTracking extends RobotApiDemoActivity {
	private final static String TAG = "RedBallTracking";
	private final static String INSTRUCTIONS = "This class is used to use red ball tracking of robot "
			+ "When start red ball tracking, robot will follow the red ball. Check Whole Body to enable tracking whole body gesture. "
			+ "Click Start and Stop button to start and stop red ball tracking";
	// check box enable tracking who body
	private CheckBox cbWholeBody;
	// button start red ball tracking
	private Button btStart;
	// button stop red ball tracking
	private Button btStop;

	// private Button btGetTarget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// set event click for button start red ball tracking
		btStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startRedBallTracking();
			}
		});
		// set event click for button stop red ball tracking
		btStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopRedBallTracking();
			}
		});
		/*
		 * btGetTarget.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { getTargetPosition(); } });
		 */
	}

	/**
	 * Start red ball tracking, red ball tracking will run until stop is called
	 */
	private void startRedBallTracking() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// check if red ball tracking is running or not
					if (RobotRedBallTracker.isActive(getRobot())) {
						makeToast("Red ball tracking is running");
						return;
					} else {
						showProgress("Start red ball tracking...");
						// get value from check box and set tracking whole body
						RobotRedBallTracker.setTrackingWholeBody(getRobot(),
								cbWholeBody.isChecked());
						// start red ball tracking
						RobotRedBallTracker.startTracker(getRobot());
						cancelProgress();
					}
				} catch (RobotException e) {
					cancelProgress();
					makeToast("start red ball tracking failed "
							+ e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Stop red ball tracking
	 */
	private void stopRedBallTracking() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// check if face tracking is run or not
					if (!RobotRedBallTracker.isActive(getRobot())) {
						makeToast("Red ball tracking is not running!");
						return;
					} else {
						showProgress("Stop red ball tracking...");
						// stop red ball tracking
						RobotRedBallTracker.stopTracker(getRobot());
						cancelProgress();
					}
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Stop red ball tracking failed: "
							+ e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Get Target position while tracking
	 */
	/*
	 * private void getTargetPosition() { new Thread(new Runnable() {
	 * 
	 * @Override public void run() { boolean newData = false; try { if
	 * (RobotRedBallTracker.isNewData(getRobot())) { newData = true; }
	 * showProgress("Getting target position..."); RobotPosition3D target =
	 * RobotRedBallTracker .getTargetPosition(getRobot()); cancelProgress();
	 * makeToast("NewData: " + newData + " Target: x,y,z: " + target.x + ", " +
	 * target.y + ", " + target.z); } catch (RobotException e) {
	 * cancelProgress(); makeToast("Get target postion failed: " +
	 * e.getMessage()); e.printStackTrace(); } } }).start(); }
	 */
	@Override
	protected void onDestroy() {
		try {
			// stop red ball tracking when activity is destroyed
			RobotRedBallTracker.stopTracker(getRobot());
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
		return R.layout.activity_red_ball_tracking;
	}

	@Override
	protected void settingView() {
		cbWholeBody = (CheckBox) findViewById(R.id.cbRedBallTrackingWholeBody);
		btStart = (Button) findViewById(R.id.btStartRedBallTracking);
		btStop = (Button) findViewById(R.id.btStopRedBallTracking);
		// btGetTarget = (Button) findViewById(R.id.btRedBallTrackingGetTarget);
	}

}
