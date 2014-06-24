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
package com.fpt.robot.example.apis.motion;

import android.os.Bundle;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.motion.RobotMotionLocomotionController;
import com.fpt.robot.types.RobotMoveTargetPosition;
import com.fpt.robot.types.RobotVelocity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
/**
 * This class is used to use some locomotion of robot. 
 * @author Robot Team (FTI)
 *
 */
public class MotionLocomotionController extends RobotApiDemoActivity
		implements OnClickListener {
	private final String TAG = "MotionLocomotionController";
	private final String INSTRUCTIONS = "This class is used to use some locomotion of robot. " +
			"Move init fuction will turn robot to initial state to move. " +
			"If you use \"Move To\" you need set value for x,y,z for robot (it's target for robot move), while robot move to, you can't stop. " +
			"If you use \"Set walk\" you need set value for x,y,z,speed for robot (it is target and speed for robot walk, while robot walk, you can call \"Stop move\" function to stop walk. " +
			"Check enable left arm and right arm to run arm behavior while robot moving. " +
			"Click \"Get Postion\", \"Get next postion\", \"Get Robot Velocity\" to get robot position " +
			"Set Foot Steps and Foot step with speed to set a step for robot's foot, you also need to enter value for robot target (x,y,z). If set foot steps with speed, you need set value for speed";
	// x value for robot target position
	private EditText edX;
	// y value for robot target position
	private EditText edY;
	// theta value for robot target position
	private EditText edR;
	// speed for set walk
	private EditText edMoveSpeed;
	// speed for set foot step
	private EditText edFootSpeed;
	// enable left arm and right arm while robot moving
	private CheckBox cbEnableLeftArm;
	private CheckBox cbEnableRightArm;

	// button initialize move state
	private Button btMoveInit;
	// button stop moving
	private Button btStopMove;
	// button move to
	private Button btMoveTo;
	// button set walk with speed
	private Button btSetWalk;
	// button get current position
	private Button btGetCurrentPosition;
	// button get next position
	private Button btGetNextPosition;
	// button get robot velocity
	private Button btGetVelocity;
	// button set foot step
	private Button btSetFoot;
	// button set foot step with speed
	private Button btSetFootWithSpeed;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// set event click for buttons
		btMoveInit.setOnClickListener(this);
		btStopMove.setOnClickListener(this);
		btMoveTo.setOnClickListener(this);
		btSetWalk.setOnClickListener(this);
		btGetCurrentPosition.setOnClickListener(this);
		btGetNextPosition.setOnClickListener(this);
		btGetVelocity.setOnClickListener(this);
		btSetFoot.setOnClickListener(this);
		btSetFootWithSpeed.setOnClickListener(this);
	}

	@Override
	protected void settingView() {
		edX = (EditText) findViewById(R.id.edXTarget);
		edY = (EditText) findViewById(R.id.edYTarget);
		edR = (EditText) findViewById(R.id.edAngleTarget);
		edMoveSpeed = (EditText) findViewById(R.id.edSpeed);
		edFootSpeed = (EditText) findViewById(R.id.edFootSpeed);

		cbEnableLeftArm = (CheckBox) findViewById(R.id.cbEnableLeftArm);
		cbEnableRightArm = (CheckBox) findViewById(R.id.cbEnableRightArm);

		btMoveInit = (Button) findViewById(R.id.btMoveInit);
		btStopMove = (Button) findViewById(R.id.btStopMove);
		btMoveTo = (Button) findViewById(R.id.btMoveTo);
		btSetWalk = (Button) findViewById(R.id.btSetWalk);
		btGetCurrentPosition = (Button) findViewById(R.id.btGetPosition);
		btGetNextPosition = (Button) findViewById(R.id.btGetNextPosition);
		btGetVelocity = (Button) findViewById(R.id.btGetVelocity);
		btSetFoot = (Button) findViewById(R.id.btSetFootSteps);
		btSetFootWithSpeed = (Button) findViewById(R.id.btSetFootStepWithSpeed);
		super.settingView();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.copy_activity_locomotion_controller;
	}

	/**
	 * Initialize state for moving action
	 */
	private void moveInit() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				try {
					// check if robot is moving
					if (RobotMotionLocomotionController
							.moveIsActive(getRobot())) {
						showInfoDialogFromThread(TAG,
								"Robot moving action is running");
						return;
					} else {
						showProgress("Initializing moving state...");
						// run move initializing
						result = RobotMotionLocomotionController
								.moveInit(getRobot());
					}
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG,
							"Initialize failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (result) {
					showInfoDialogFromThread(TAG, "Initialze successfully");
				} else {
					showInfoDialogFromThread(TAG, "Initialze failed!");
				}
			}
		}).start();
	}

	/**
	 * Stop all moving action
	 */
	private void moveStop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				try {
					// check if robot is running
					if (!RobotMotionLocomotionController
							.moveIsActive(getRobot())) {
						showInfoDialogFromThread(TAG,
								"Robot moving action is not running");
						return;
					} else {
						showProgress("Stop moving...");
						// stop moving action
						result = RobotMotionLocomotionController
								.moveStop(getRobot());
					}
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG,
							"Stop failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (result) {
					showInfoDialogFromThread(TAG, "Stop successfully");
				} else {
					showInfoDialogFromThread(TAG, "Stop failed!");
				}
			}
		}).start();
	}

	// Robot move target position
	private RobotMoveTargetPosition target = null;

	/**
	 * start moving
	 */
	private void moveTo() {
		try {
			// get target from edit text
			float x = Float.parseFloat(edX.getText().toString());
			float y = Float.parseFloat(edY.getText().toString());
			float r = Float.parseFloat(edR.getText().toString());
			target = new RobotMoveTargetPosition(x, y, r);
		} catch (Exception e) {
			showInfoDialog(TAG, "Enter correct value for x,y and r");
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				try {
					// check if robot is running
					if (RobotMotionLocomotionController
							.moveIsActive(getRobot())) {
						showInfoDialogFromThread(TAG,
								"Robot moving action is running");
						return;
					} else {
						// set action for arm
						RobotMotionLocomotionController.setWalkArmsEnabled(
								getRobot(), cbEnableLeftArm.isChecked(),
								cbEnableRightArm.isChecked());
						showProgress("Start moving...");
						// start moving action
						result = RobotMotionLocomotionController.moveTo(
								getRobot(), target);
					}
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG,
							"move failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (result) {
					showInfoDialogFromThread(TAG, "move successfully");
				} else {
					showInfoDialogFromThread(TAG, "move failed!");
				}
			}
		}).start();
	}

	float speed = 0.0f;

	/**
	 * set walk with speed
	 */
	private void setWalk() {
		try {
			// get target and speed from edit text
			float x = Float.parseFloat(edX.getText().toString());
			float y = Float.parseFloat(edY.getText().toString());
			float r = Float.parseFloat(edR.getText().toString());
			speed = Float.parseFloat(edMoveSpeed.getText().toString());
			target = new RobotMoveTargetPosition(x, y, r);
		} catch (Exception e) {
			showInfoDialog(TAG, "Enter correct value for x,y,r and speed");
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				try {
					// check if robot is running
					if (RobotMotionLocomotionController
							.moveIsActive(getRobot())) {
						showInfoDialogFromThread(TAG,
								"Robot moving action is running");
						return;
					} else {
						// set action for arm
						RobotMotionLocomotionController.setWalkArmsEnabled(
								getRobot(), cbEnableLeftArm.isChecked(),
								cbEnableRightArm.isChecked());
						makeToast("Start moving...");
						// start walking
						result = RobotMotionLocomotionController
								.setWalkTargetVelocity(getRobot(), target,
										speed);
					}
				} catch (RobotException e) {
					showInfoDialogFromThread(TAG,
							"walk failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread(TAG, "walk failed!");
				}
			}
		}).start();
	}

	/**
	 * get current position of robot
	 */
	private void getCurrentPosition() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RobotMoveTargetPosition position = null;
				try {

					showProgress("Getting postion...");
					// getting position
					position = RobotMotionLocomotionController
							.getRobotPosition(getRobot(), false);

				} catch (RobotException e) {
					showInfoDialogFromThread(TAG,
							"Get position: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (position == null) {
					showInfoDialogFromThread(TAG, "get position failed!");
				} else {
					showInfoDialogFromThread(TAG, "Position:\n (" + position.x
							+ "," + position.y + "," + position.theta + ")");
				}
			}
		}).start();
	}

	/**
	 * get next position of robot
	 */
	private void getNextPosition() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RobotMoveTargetPosition position = null;
				try {

					showProgress("Getting next postion...");
					// getting position
					position = RobotMotionLocomotionController
							.getNextRobotPosition(getRobot());

				} catch (RobotException e) {
					showInfoDialogFromThread(TAG,
							"Get next position: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (position == null) {
					showInfoDialogFromThread(TAG, "Get next position failed!");
				} else {
					showInfoDialogFromThread(TAG, "Next position:\n ("
							+ position.x + "," + position.y + ","
							+ position.theta + ")");
				}
			}
		}).start();
	}

	/**
	 * get robot velocity
	 */
	private void getVelocity() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				RobotVelocity velocity = null;
				try {

					showProgress("Getting next postion...");
					// getting position
					velocity = RobotMotionLocomotionController
							.getRobotVelocity(getRobot());

				} catch (RobotException e) {
					showInfoDialogFromThread(TAG,
							"Get velocity: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (velocity == null) {
					showInfoDialogFromThread(TAG, "Get robot velocity failed!");
				} else {
					showInfoDialogFromThread(TAG, "Robot velocity:\n ("
							+ velocity.x + "," + velocity.y + "," + velocity.wz
							+ ")");
				}
			}
		}).start();
	}

	/**
	 * set foot steps
	 */
	private void setFootSteps() {
		try {
			// get target from edit text
			float x = Float.parseFloat(edX.getText().toString());
			float y = Float.parseFloat(edY.getText().toString());
			float r = Float.parseFloat(edR.getText().toString());			
			target = new RobotMoveTargetPosition(x, y, r);
		} catch (Exception e) {
			showInfoDialog(TAG, "Enter correct value for x,y,r");
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {				
				try {
					showProgress("Set Foot Steps...");
					// set foot steps
					RobotMotionLocomotionController.setFootSteps(getRobot(),
							new String[] { "RLeg","LLeg" },
							new RobotMoveTargetPosition[] { target,target },
							new float[] { 1.0f,1.0f }, false);					

				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG,
							"Set Foot Steps: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				
			}
		}).start();
	}
	
		
	/**
	 * set foot steps
	 */
	private void setFootStepsWithSpeed() {
		speed = 0.0f;
		try {
			// get target from edit text
			float x = Float.parseFloat(edX.getText().toString());
			float y = Float.parseFloat(edY.getText().toString());
			float r = Float.parseFloat(edR.getText().toString());
			speed = Float.parseFloat(edFootSpeed.getText().toString());
			target = new RobotMoveTargetPosition(x, y, r);
		} catch (Exception e) {
			showInfoDialog(TAG, "Enter correct value for x,y,r");
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {				
				try {
					showProgress("Set Foot Steps With Speed...");
					// set foot steps
					RobotMotionLocomotionController.setFootStepsWithSpeed(getRobot(),
							new String[] { "RLeg","LLeg" },
							new RobotMoveTargetPosition[] { target,target },
							new float[] { speed,speed }, false);					

				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(TAG,
							"Set Foot Steps With Speed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btMoveInit:
			moveInit();
			break;
		case R.id.btStopMove:
			moveStop();
			break;
		case R.id.btMoveTo:
			moveTo();
			break;
		case R.id.btSetWalk:
			setWalk();
			break;
		case R.id.btGetPosition:
			getCurrentPosition();
			break;
		case R.id.btGetNextPosition:
			getNextPosition();
			break;
		case R.id.btGetVelocity:
			getVelocity();
			break;
		case R.id.btSetFootSteps:
			setFootSteps();
			break;
		case R.id.btSetFootStepWithSpeed:
			setFootStepsWithSpeed();
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
