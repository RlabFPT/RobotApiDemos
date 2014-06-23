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
package com.fpt.robot.example.apis.vision.detection;

import android.os.Bundle;

import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.vision.RobotRedBallDetection;
import com.fpt.robot.vision.RobotRedBallDetection.RedBallDetectedInfo;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * RedBallDetection class allows you to detecting the red ball
 * @author Robot Team (FTI)
 *
 */
public class RedBallDetection extends RobotApiDemoActivity implements
		RobotRedBallDetection.Listener {
	private static final String TAG = "RedBallDetection";
	private static final String INSTRUCTIONS = "RedBallDetection class allows you to detecting the red ball. When robot detect the red ball, " +
			"it will return an event, redball detection only stop when stopDetection is called";
	// Robot red ball monitor to monitor red ball detection
	private RobotRedBallDetection.Monitor redBallMonitor;
	// button start red ball detection
	private Button btStart;
	// button stop monitor detection
	private Button btStop;
	// text view display result on red ball detection
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initialize red ball detection monitor
		redBallMonitor = new RobotRedBallDetection.Monitor(getRobot());
		// set event for monitor
		redBallMonitor.setListener(this);
		// set event click for button start red ball detection
		btStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startRedBallDetection();
			}
		});
		// set event click for button stop red ball detection
		btStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopRedBallDetection();
			}
		});
	}

	/**
	 * Start red ball detection
	 */
	private void startRedBallDetection() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// check if red ball detection is running or not
					if (RobotRedBallDetection.isActive(getRobot())) {
						makeToast("RedBall Detection is running...");
						return;
					}
					showProgress("Start RedBall Detection...");
					// start red ball detection monitor first
					redBallMonitor.start();
					// start red ball detection
					RobotRedBallDetection.startDetection(getRobot());
					// play beep audio when start red ball detection
					RobotAudioPlayer.beep(getRobot());
					cancelProgress();
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Start RedBall detection failed: "
							+ e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Stop red ball detection
	 */
	private void stopRedBallDetection() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// check if red ball detection is running or not
					if (!RobotRedBallDetection.isActive(getRobot())) {
						makeToast("RedBall Detection is not running");
						return;
					}
					showProgress("Stop RedBall Detection...");
					// stop red ball detection monitor first
					redBallMonitor.stop();
					// stop red ball detection
					RobotRedBallDetection.stopDetection(getRobot());
					cancelProgress();
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Stop RedBall failed: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		// stop red ball detection when activity is destroyed
		try {			
			// stop red ball detection monitor first
			redBallMonitor.stop();
			// stop red ball detection
			RobotRedBallDetection.stopDetection(getRobot());
			cancelProgress();
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
		return R.layout.activity_red_ball_detection;
	}

	@Override
	protected void settingView() {
		btStart = (Button) findViewById(R.id.btStartRedBallDetection);
		btStop = (Button) findViewById(R.id.btStopRedBallDetection);
		tvResult = (TextView) findViewById(R.id.tvOnRedBallDetected);
	}

	
	@Override
	public void onRedBallDetected(RedBallDetectedInfo info) {
		runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				tvResult.setText("On red ball detection");
			}
		});
	}

}
