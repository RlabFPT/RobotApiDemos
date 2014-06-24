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
package com.fpt.robot.example.apis.behaviors;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.fpt.robot.RobotException;
import com.fpt.robot.behavior.RobotBehavior;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;

/**
 * This class is used to check a behavior was installed or wasn't, also
 * check a behavior is running or isn't
 * @author Robot Team (FTI)
 */

public class CheckBehavior extends RobotApiDemoActivity implements
		OnClickListener {	
	private static final String TAG = "CheckBehavior";
	private static final String INSTRUCTIONS = "You can check: " +
			"Behavior is installed or not, is running or not. " +
			"The first, enter behavior name which you want to check" +
			"on the edittext, and then click button to check";

	// Button to check if behavior is installed
	private Button btCheckInstalled;
	// Button to check if behavior is running
	private Button btCheckRunning;
	// Edittext to enter behavior name
	private EditText edBehaviorName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// set event click for buttons
		btCheckInstalled.setOnClickListener(this);
		btCheckRunning.setOnClickListener(this);
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
			// show help
			showInfoDialog(TAG, INSTRUCTIONS);
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_check_behavior;
	}

	@Override
	protected void settingView() {
		btCheckInstalled = (Button) findViewById(R.id.btCheckBehaviorInstalled);
		btCheckRunning = (Button) findViewById(R.id.btCheckBehaviorRunning);
		edBehaviorName = (EditText) findViewById(R.id.edBehaviorNameNeedCheck);
	}

	@Override
	public void onClick(View v) {
		// get behavior name from editext
		final String behaviorName = edBehaviorName.getText().toString();
		// check if empty
		if (behaviorName == null || TextUtils.isEmpty(behaviorName)) {
			showInfoDialog("Check Behavior", "Please enter bahavior name");
		} else {
			switch (v.getId()) {
			case R.id.btCheckBehaviorInstalled:
				new Thread(new Runnable() {

					@Override
					public void run() {
						showProgress("Checking behavior installed...");
						boolean result = false;
						try {
							// check if behavior is installed
							result = RobotBehavior.isBehaviorInstalled(
									getRobot(), behaviorName);
						} catch (RobotException e) {
							cancelProgress();
							showInfoDialogFromThread(
									"Check Installed Behavior",
									"Check Installed Behavior failed: "
											+ e.getMessage());
							e.printStackTrace();
							return;
						}
						cancelProgress();
						if (result) {
							showInfoDialogFromThread("Check Installed Behavior",
									"Behavior " + behaviorName
											+ " has already installed");
						} else {
							showInfoDialogFromThread("Check Installed Behavior",
									"Behavior " + behaviorName
											+ " has not installed yet");
						}
					}
				}).start();
				break;

			case R.id.btCheckBehaviorRunning:
				new Thread(new Runnable() {

					@Override
					public void run() {
						showProgress("Check behavior running...");
						boolean result = false;
						try {
							// check if behavior is running
							result = RobotBehavior.isBehaviorRunning(
									getRobot(), behaviorName);
						} catch (RobotException e) {
							cancelProgress();
							showInfoDialogFromThread(
									"Check Running Behavior",
									"Check Running Behavior failed: "
											+ e.getMessage());
							e.printStackTrace();
							return;
						}
						cancelProgress();
						if (result) {
							showInfoDialogFromThread("Check Running Behavior",
									"Behavior " + behaviorName + " is running");
						} else {
							showInfoDialogFromThread("Check Running Behavior",
									"Behavior " + behaviorName
											+ " is not running");
						}
					}
				}).start();
				break;
			default:

				break;
			}

		}
	}
}
