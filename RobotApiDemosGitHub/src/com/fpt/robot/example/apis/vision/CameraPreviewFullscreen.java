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

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.vision.RobotCamera;
import com.fpt.robot.vision.RobotCameraPreview;
import com.fpt.robot.vision.RobotCameraPreviewView;
/**
 *  CamearaPreviewFullscreen class allows you to preview real time image from robot's camera on full screen
 * @author Robot Team (FTI)
 *
 */
public class CameraPreviewFullscreen extends RobotApiDemoActivity {
	
	private RobotCameraPreviewView mPreviewView;
	private RobotCameraPreview mCameraPreview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mPreviewView = new RobotCameraPreviewView(this, null);
		setContentView(mPreviewView);
		
		mCameraPreview = RobotCamera.getCameraPreview(getRobot(), RobotCamera.CAMERA_TOP);
		mCameraPreview.setPreviewDisplay(mPreviewView);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					mCameraPreview.startPreview();
				} catch (RobotException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		if (mCameraPreview != null) {
			try {
				mCameraPreview.stopPreview();
			} catch (RobotException e) {
				e.printStackTrace();
			}
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
	protected int getLayoutID() {
		return R.layout.activity_camera_preview_fullscreen;
	}
}
