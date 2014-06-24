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
package com.fpt.robot.example.apis.vision.detection;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.vision.RobotLandmarkDetection;
import com.fpt.robot.vision.RobotLandmarkDetection.LandmarkDetectedInfo;
/**
 * This class is used to detect robot position with landmark
 * @author Robot Team (FTI)
 *
 */
public class LandMarkDetection extends RobotApiDemoActivity implements
		RobotLandmarkDetection.Listener {
	private static final String TAG = "LandMarkDetection";
	private static final String INSTRUCTIONS = "This class is used to detect robot position with landmark";
	// Land mark monitor
	private RobotLandmarkDetection.Monitor landMarkMonitor;
	// button start landmark detection
	private Button btStart;
	// button stop landmark detection
	private Button btStop;
	// text view display result when return event onLandDetection
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initialize land mark monitor
		landMarkMonitor = new RobotLandmarkDetection.Monitor(getRobot());
		// set event for monitor
		landMarkMonitor.setListener(this);
		// set event click for button start land mark detection
		btStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startLandMarkDetection();
			}
		});
		// set event click for button stop land mark detection
		btStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopLandMarkDetection();
			}
		});
	}

	/**
	 * Start land mark detection
	 */
	private void startLandMarkDetection() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// check if land mark detection is running or not
					if (RobotLandmarkDetection.isActive(getRobot())) {
						makeToast("LandMark Detection is running...");
						return;
					}
					showProgress("Start LandMark Detection...");
					// start monitor first
					landMarkMonitor.start();
					// start land mark detection
					RobotLandmarkDetection.startDetection(getRobot());
					// play beep audio when start land mark detection
					RobotAudioPlayer.beep(getRobot());
					cancelProgress();
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Start landmark detection failed: "
							+ e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Stop land mark detection
	 */
	private void stopLandMarkDetection() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// check if land mark detection is running or not
					if (!RobotLandmarkDetection.isActive(getRobot())) {
						makeToast("LandMark Detection is not running");
						return;
					}
					showProgress("Stop LandMark Detection...");
					// stop land mark monitor
					landMarkMonitor.stop();
					// stop land mark detection
					RobotLandmarkDetection.stopDetection(getRobot());
					cancelProgress();
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Stop landmark failed: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		// stop land mark detection when activity is destroyed
		try {			
			landMarkMonitor.stop();
			RobotLandmarkDetection.stopDetection(getRobot());
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
		return R.layout.activity_land_mark_detection;
	}

	@Override
	protected void settingView() {
		btStart = (Button) findViewById(R.id.btStartLandMark);
		btStop = (Button) findViewById(R.id.btStopLandMark);
		tvResult = (TextView) findViewById(R.id.tvOnLandMarkDetect);
	}

	@Override
	public void onLanmarkDetected(LandmarkDetectedInfo info) {
		// TODO
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				tvResult.setText("On land mark detected");
			}
		});		
	}

	
}
