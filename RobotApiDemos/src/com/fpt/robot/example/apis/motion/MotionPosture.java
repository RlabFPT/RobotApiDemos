/*
 * Copyright (C) 2014 FPT Corporation
 * @author: Robot Team (FTI)
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fpt.robot.example.apis.motion;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.motion.RobotPosture;

/**
 * This class is used to run some posture of robot.
 * @author Robot Team (FTI)
 * 
 */
public class MotionPosture extends RobotApiDemoActivity {

	private final String TAG = "MotionPosture";
	private final String INSTRUCTIONS = "This class is used to run some posture of robot. Select posutre name " +
			"on spinner and then click \"Go to posture\" to run this posture";
	// button go to posture
	private Button btGoToPosture;
	// spinner posture names
	private Spinner spPostureName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// get list robot posture name
		getPostureName();
		// set event click for button go to posture
		btGoToPosture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showAlertDialog(TAG, "Are you sure run this posture?", null,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								gotoPosture();
							}
						}, null);				
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
		return R.layout.activity_motion_posture;
	}

	/**
	 * Robot go to posture
	 */
	private void gotoPosture() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showProgress("Going to posture...");
				try {
					// get name from spinner
					String posture = (String) spPostureName.getSelectedItem();
					// robot go to posture
					RobotPosture.goToPosture(getRobot(), posture, 0.5f);
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG,
							"Go to posture failed: " + e.getMessage());
					e.printStackTrace();
				}
				cancelProgress();
			}
		}).start();
	}

	/**
	 * Get list postures name on robot
	 */
	private void getPostureName() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showProgress("Initializing...");
				try {
					// get list posture
					final String[] postureNames = RobotPosture
							.getPostureList(getRobot());
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// set list posture name to spinner
							spPostureName
									.setAdapter(new ArrayAdapter<String>(
											MotionPosture.this,
											android.R.layout.simple_spinner_dropdown_item,
											postureNames));
							spPostureName.setSelection(0);
						}
					});
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG, "Fail to get list posture: "
							+ e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
			}
		}).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.fpt.robot.ui.RobotActivity#settingView()
	 */

	@Override
	protected void settingView() {
		btGoToPosture = (Button) findViewById(R.id.btGoToPosture);
		spPostureName = (Spinner) findViewById(R.id.spPostureName);
		super.settingView();
	}

}
