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
import android.view.View.OnClickListener;
import android.widget.Button;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.motion.RobotMotionAction;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.motion.RobotPosture;
/**
 * This class is used to run some motions of robot
 * @author Robot Team (FTI)
 */
public class MotionActions extends RobotApiDemoActivity implements
		OnClickListener {
	private static final String TAG = "MotionActions";
	private static final String INSTRUCTIONS = "This class is used to run some actions of robot";
	// Button to stand up
	private Button btStandUp;
	// Button to sit down
	private Button btSitDown;
	// Button to go to crouch
	private Button btCrouch;
	// Button to step forward
	private Button btStepForWard;
	// Button to step backward
	private Button btStepBackWard;
	// Button to turn left
	private Button btTurnLeft;
	// Button to turn right
	private Button btTurnRight;
	// Button to stop walk
	private Button btStopWalk;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// set event click for buttons
		btStandUp.setOnClickListener(this);
		btSitDown.setOnClickListener(this);
		btCrouch.setOnClickListener(this);
		btStepForWard.setOnClickListener(this);
		btStepBackWard.setOnClickListener(this);
		btTurnLeft.setOnClickListener(this);
		btTurnRight.setOnClickListener(this);
		btStopWalk.setOnClickListener(this);
	}

	@Override
	protected void settingView() {
		btStandUp = (Button) findViewById(R.id.btMotionStandUp);
		btSitDown = (Button) findViewById(R.id.btMotionSitDown);
		btCrouch = (Button) findViewById(R.id.btMotionCrouch);
		btStepForWard = (Button) findViewById(R.id.btMotionStepForWard);
		btStepBackWard = (Button) findViewById(R.id.btMotionStepBackWard);
		btTurnLeft = (Button) findViewById(R.id.btMotionTurnLeft);
		btTurnRight = (Button) findViewById(R.id.btMotionTurnRight);
		btStopWalk = (Button) findViewById(R.id.btMotionStopWalk);
		
		super.settingView();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_motion_actions;
	}

	/**
	 * Robot stand up
	 */
	private void standUp() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean result = false;
				try {
					showProgress("Standing up...");
					// run stand up with speed is 0.5 (max speed is 1.0)
					result = RobotMotionAction.standUp(getRobot(), 0.5f);
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG, "Stand up failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (result){
					showInfoDialogFromThread(TAG,"Stand up successfully");
				} else{
					showInfoDialogFromThread(TAG,"Stand up failed");
				}
			}
		}).start();
	}
	/**
	 * Robot sit down
	 */
	private void sitDown() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean result = false;
				try {
					showProgress("Sitting down...");
					// run sit down with speed is 0.5 (max speed is 1.0)
					result = RobotMotionAction.sitDown(getRobot(), 0.5f);
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG, "Sit down failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (result){
					showInfoDialogFromThread(TAG,"Sit Down successfully");
				} else{
					showInfoDialogFromThread(TAG,"Sit Down failed");
				}
			}
		}).start();
	}

	/**
	 * Robot go to crouch state
	 */
	private void crouch() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean result = false;
				try {
					showProgress("Crouch...");
					// go to crouch state with speed is 0.5 (max speed is 1.0)
					result = RobotPosture.goToPosture(getRobot(), "Crouch", 0.5f);
					// set motor off
					RobotMotionStiffnessController.rest(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG, "Crouch failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (result){
					showInfoDialogFromThread(TAG,"Crouch successfully");
				} else{
					showInfoDialogFromThread(TAG,"Crouch failed");
				}
			}
		}).start();
	}

	/**
	 * Robot go ahead
	 */
	private void stepForWard() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// Robot go ahead and only stop if stopWalk is called
					RobotMotionAction.stepForward(getRobot(), 0.5f);
				} catch (RobotException e) {
					showInfoDialogFromThread(TAG, "Step ForWard failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
			}
		}).start();
	}

	/**
	 * Robot go backward
	 */
	private void stepBackWard() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// Robot go backward and only stop if stopWalk is called
					RobotMotionAction.stepBackward(getRobot(), 0.5f);
				} catch (RobotException e) {
					showInfoDialogFromThread(TAG, "Step ForWard failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
			}
		}).start();
	}

	/**
	 * Robot turn left
	 */
	private void turnLeft() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// Robot turn left and only stop if stopWalk is called
					RobotMotionAction.turnLeft(getRobot(), 0.5f);
				} catch (RobotException e) {
					showInfoDialogFromThread(TAG, "Turn Left failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
			}
		}).start();
	}

	/**
	 * Robot turn right
	 */
	private void turnRight() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// Robot turn right and only stop if stopWalk is called
					RobotMotionAction.turnRight(getRobot(), 0.5f);
				} catch (RobotException e) {
					showInfoDialogFromThread(TAG, "Turn Right failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
			}
		}).start();
	}

	/**
	 * Robot stop walk
	 */
	private void stopWalk() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// Stop walk, also stop turn left and turn right
					RobotMotionAction.stopWalk(getRobot());
				} catch (RobotException e) {
					showInfoDialogFromThread(TAG, "Stop Walk failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btMotionStandUp:
			showAlertDialog(TAG, "Are you sure run stand up?", null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							standUp();
						}
					}, null);			
			break;
		case R.id.btMotionSitDown:
			showAlertDialog(TAG, "Are you sure run sit down?", null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							sitDown();
						}
					}, null);			
			break;
		case R.id.btMotionCrouch:
			showAlertDialog(TAG, "Are you sure run crouch?", null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							crouch();
						}
					}, null);				
			break;
		case R.id.btMotionStepForWard:
			showAlertDialog(TAG, "Are you sure run step forward?", null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							stepForWard();
						}
					}, null);				
			break;
		case R.id.btMotionStepBackWard:
			showAlertDialog(TAG, "Are you sure run step backward?", null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							stepBackWard();
						}
					}, null);				
			break;
		case R.id.btMotionTurnLeft:
			showAlertDialog(TAG, "Are you sure run turn left?", null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							turnLeft();
						}
					}, null);				
			break;
		case R.id.btMotionTurnRight:
			showAlertDialog(TAG, "Are you sure run turn right?", null,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							turnRight();
						}
					}, null);	
			break;
		case R.id.btMotionStopWalk:
			stopWalk();
			break;
		default:
			break;
		}

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

}
