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
import com.fpt.robot.tracking.RobotFaceTracker;

/**
 * This class is used to use face tracking of robot. When start face tracking,
 * robot will follow your face.
 * 
 * @author Robot Team (FTI)
 */
public class FaceTracking extends RobotApiDemoActivity {
	private final static String TAG = "FaceTracking";
	private final static String INSTRUCTIONS = "This class is used to use face tracking of robot "
			+ "When start face tracking, robot will follow your face. Check Whole Body to enable tracking whole body gesture. "
			+ "Click Start and Stop button to start and stop face tracking";
	// check box enable tracking who body
	private CheckBox cbWholeBody;
	// button start face tracking
	private Button btStart;
	// button stop face tracking
	private Button btStop;

	// private Button btGetTarget;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// set event click for button start face tracking
		btStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startFaceTracking();
			}
		});
		// set event stop for button stop face tracking
		btStop.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				stopFaceTracking();
			}
		});
		/*
		 * btGetTarget.setOnClickListener(new View.OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { getTargetPosition(); } });
		 */
	}

	/**
	 * Start face tracking, face tracking will run until stop is called
	 */
	private void startFaceTracking() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// check if face tracking is running or not
					if (RobotFaceTracker.isActive(getRobot())) {
						makeToast("Face tracking is running");
						return;
					} else {
						showProgress("Start face tracking...");
						// get value from check box and set tracking whole body
						RobotFaceTracker.setTrackingWholeBody(getRobot(),
								cbWholeBody.isChecked());
						// start face tracking
						RobotFaceTracker.startTracker(getRobot());
						cancelProgress();
					}
				} catch (RobotException e) {
					cancelProgress();
					makeToast("start face tracking failed " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Stop face tracking
	 */
	private void stopFaceTracking() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// check if face tracking is run or not
					if (!RobotFaceTracker.isActive(getRobot())) {
						makeToast("Face tracking is not running!");
						return;
					} else {
						showProgress("Stop face tracking...");
						// stop face tracking
						RobotFaceTracker.stopTracker(getRobot());
						cancelProgress();
					}
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Stop face tracking failed: " + e.getMessage());
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
	 * (RobotFaceTracker.isNewData(getRobot())) { newData = true; }
	 * showProgress("Getting target position..."); RobotPosition3D target =
	 * RobotFaceTracker .getTargetPosition(getRobot()); cancelProgress();
	 * makeToast("NewData: " + newData + " Target: x,y,z: " + target.x + ", " +
	 * target.y + ", " + target.z); } catch (RobotException e) {
	 * cancelProgress(); makeToast("Get target postion failed: " +
	 * e.getMessage()); e.printStackTrace(); } } }).start(); }
	 */
	@Override
	protected void onDestroy() {
		try {
			// stop face traking when activity is destroyed
			RobotFaceTracker.stopTracker(getRobot());
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
		return R.layout.activity_face_tracking;
	}

	@Override
	protected void settingView() {
		cbWholeBody = (CheckBox) findViewById(R.id.cbFaceTrackingWholeBody);
		btStart = (Button) findViewById(R.id.btStartFaceTracking);
		btStop = (Button) findViewById(R.id.btStopFaceTracking);
		// btGetTarget = (Button) findViewById(R.id.btFaceTrackingGetTarget);
	}

}
