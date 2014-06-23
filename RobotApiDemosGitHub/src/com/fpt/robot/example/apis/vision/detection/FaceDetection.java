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

import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.vision.RobotFaceDetection;
import com.fpt.robot.vision.RobotFaceDetection.FaceDetectedInfo;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * FaceDetection class allows you to detecting the face.
 * @author Robot Team (FTI)
 *
 */
public class FaceDetection extends RobotApiDemoActivity implements RobotFaceDetection.Listener{
	private static final String TAG = "FaceDetection";
	private static final String INSTRUCTIONS = "FaceDetection class allows you to detecting the face. When robot detect a face, " +
			"it will return an event and face detecting will be auto stop";
	// Face detection monitor to monitoring 
	private RobotFaceDetection.Monitor faceMonitor;
	// button start face detection
	private Button btStart;
	// button stop face detection
	private Button btStop;
	// text view display result on face detect
	private TextView tvFaceDetect;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initialize face detection monitor
		faceMonitor = new RobotFaceDetection.Monitor(getRobot());
		// set listener for monitor
		faceMonitor.setListener(this);
		// set event click for button start face detection
		btStart.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startFaceDetection();
			}
		});
		// set event click for button stop face detection
		btStop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopFaceDetection();
			}
		});
	}

	/**
	 * Start face detection
	 */
	private void startFaceDetection() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// check if face detection is running or not
					if (RobotFaceDetection.isDetecting(getRobot())){
						makeToast("Face Detection is running...");
						return;
					}
					showProgress("Start Face Detection...");
					// start face detection monitor
					faceMonitor.start();
					// start face detection
					RobotFaceDetection.startDetection(getRobot());
					// play beep default audio file when start detection
					RobotAudioPlayer.beep(getRobot());
					cancelProgress();
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Start Face detection failed: "
							+ e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * Stop face detection
	 */
	private void stopFaceDetection() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// check if face detection is running or not
					if (!RobotFaceDetection.isDetecting(getRobot())){
						makeToast("Face Detection is not running");
						return;
					}
					showProgress("Stop Face Detection...");
					// stop face monitor 
					faceMonitor.stop();
					// stop face detection
					RobotFaceDetection.stopDetection(getRobot());
					cancelProgress();
				} catch (RobotException e) {
					cancelProgress();
					makeToast("Stop Face failed: " + e.getMessage());
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		// stop face detection when activity is destroyed
		try {			
			faceMonitor.stop();
			RobotFaceDetection.stopDetection(getRobot());
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
		return R.layout.activity_face_detection;
	}

	@Override
	protected void settingView() {
		btStart = (Button) findViewById(R.id.btStartFaceDetection);
		btStop = (Button) findViewById(R.id.btStopFaceDetection);
		tvFaceDetect = (TextView) findViewById(R.id.tvFaceDetect);
	}


	@Override
	public void onFaceDetected(FaceDetectedInfo info) {		
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				tvFaceDetect.setText("On Face Detected");
			}
		});
	}

	@Override
	public void onFaceCropDetected() {
		runOnUiThread(new Runnable() {		
			@Override
			public void run() {
				tvFaceDetect.setText("On Face Crop Detected");
			}
		});
	}
}
