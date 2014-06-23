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
package com.fpt.robot.example.apis.vision;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.vision.RobotCamera;
import com.fpt.robot.vision.RobotCameraPreview;
import com.fpt.robot.vision.RobotCameraPreviewView;
/**
 * CamearaPreview class allows you to preview real time image from robot's camera
 * @author Robot Team (FTI)
 *
 */
public class CameraPreview extends RobotApiDemoActivity implements OnCheckedChangeListener {	
	private static final String TAG = "CameraPreview";
	private static final String INSTRUCTIONS = "CamearaPreview class allows you to preview real time image from robot's camera. " +
			"Select which camera do you want to preview (Bottom or Top). Click Start and Stop to preview and stop preview";
	// button start preview
	private Button btStartCameraPreview;
	// button stop preview
	private Button btStopCameraPreview;
	// RobotCameraPreview object to get image from robot's camera
	private RobotCameraPreview mRobotCameraPreview;
	//private CameraPreviewView mCameraPreview;
	
	// predefined view (RobotCameraPreview is extended from SurfaceView)
	private RobotCameraPreviewView mCameraPreview;

	// camera selected index
    private int selectedCameraIndex = 0;
    
    private RadioGroup rgCameraPreviewCameraSelection;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		btStartCameraPreview = (Button)findViewById(R.id.btStartCameraPreview);
		// set event click for button start preview
        btStartCameraPreview.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startCameraPreview();
			}
		});
        
        btStopCameraPreview = (Button)findViewById(R.id.btStopCameraPreview);
        // set event click for button stop preview
        btStopCameraPreview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopCameraPreview();
			}
		});        

        rgCameraPreviewCameraSelection = (RadioGroup) 
				findViewById(R.id.rgCameraPreviewCameraSelection);
        rgCameraPreviewCameraSelection.setOnCheckedChangeListener(this);
        
        //mCameraPreview = (CameraPreviewView)findViewById(R.id.cameraPreview);
        mCameraPreview = (RobotCameraPreviewView)findViewById(R.id.cameraPreview);
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
		return R.layout.activity_camera_preview;
	}

	/**
	 * Start view, the camera preview will run until stop view called
	 */
	protected void startCameraPreview() {
		// get robot camera preview
		mRobotCameraPreview = RobotCamera.getCameraPreview(getRobot(), 
				selectedCameraIndex);
        //mRobotCameraPreview.setPreviewDisplay(mCameraPreview.getHolder());
		// set view to display on the screen
		mRobotCameraPreview.setPreviewDisplay(mCameraPreview);
		// set quality for image get from camera
		mRobotCameraPreview.setQuality(mRobotCameraPreview.QUALITY_MEDIUM);
		// get quality of robot camera preview
        int cameraQuality = mRobotCameraPreview.getQuality();        
        makeToast("Camera quality: " + cameraQuality);
        // set speed
        mRobotCameraPreview.setSpeed(mRobotCameraPreview.SPEED_MEDIUM);
        int cameraSpeed = mRobotCameraPreview.getSpeed();
        makeToast("Camera speed: " + cameraSpeed);
        
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				try {
					// start preview and display image to view on the screen
					result = mRobotCameraPreview.startPreview();
				} catch (final RobotException e) {
					e.printStackTrace();
					makeToast("start camera preview failed! " + e.getMessage());
					return;
				}
				if (result) {
					makeToast("camera preview started!");
				} else {
					makeToast("start camera preview failed!");
				}
			}
		}).start();
	}

	/**
	 * Stop robot camera preview
	 */
	protected void stopCameraPreview() {
		if (mRobotCameraPreview == null) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				try {
					// stop view
					result = mRobotCameraPreview.stopPreview();
				} catch (final RobotException e) {
					e.printStackTrace();
					makeToast("stop camera preview failed! " + e.getMessage());
					return;
				}
				if (result) {
					makeToast("camera preview stopped!");
				} else {
					makeToast("stop camera preview failed!");
				}
			}
		}).start();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// get which camera is choosen
		if (checkedId == R.id.rdCameraPreviewCameraTop) {
			selectedCameraIndex = RobotCamera.CAMERA_TOP;
		} else if (checkedId == R.id.rdCameraPreviewCameraBottom) {
			selectedCameraIndex = RobotCamera.CAMERA_BOTTOM;
		}
	}
}
