/**
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

import java.util.ArrayList;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.fpt.robot.RobotException;
import com.fpt.robot.RobotHardware;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.motion.RobotMotionStiffnessController;

public class MotionStiffness extends RobotApiDemoActivity implements
		OnClickListener {
	private final String TAG = "MotionStiffness";
	private final String INSTRUCTIONS = "MotionStiffness class allows you to set stiffness for some joints and chains of robot. " +
			"You also can get stiffness or set stiffness interpolation for joints or chains";
	// button set stiffness
	private Button btSetStiffness;
	// button get stiffness
	private Button btGetStiffness;
	// button set stiffness interpolation
	private Button btStiffnessInterpolation;
	// button wake up (set motor on)
	private Button btWakeUp;
	// button rest (set motor off)
	private Button btRest;
	
	// spinner name of joints, chains
	private Spinner spStiffnessNames;
	// spinner value to set stiffness for joints, chains
	private Spinner spStiffnessValues;

	// list joint names and chain names in robot
	private ArrayList<String> jointsAndChains = new ArrayList<String>();
	private ArrayList<Float> stiffnessValues = new ArrayList<Float>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// add value to list. This is an example for list of joint and chain.
		// More name, see RobotHardWare class
		jointsAndChains.add(RobotHardware.Joint.GroupName.ACTUATORS);
		jointsAndChains.add(RobotHardware.Joint.GroupName.BODY);
		jointsAndChains.add(RobotHardware.Joint.GroupName.JOINTACTUATORS);
		jointsAndChains.add(RobotHardware.Joint.GroupName.JOINTS);
		jointsAndChains.add(RobotHardware.Joint.Head.HEAD_PITCH);
		jointsAndChains.add(RobotHardware.Joint.Head.HEAD_YAW);
		jointsAndChains.add(RobotHardware.Joint.LeftArm.LEFT_ELBOW_ROLL);
		jointsAndChains.add(RobotHardware.Joint.LeftArm.LEFT_ELBOW_YAW);
		jointsAndChains.add(RobotHardware.Joint.LeftArm.LEFT_HAND);
		jointsAndChains.add(RobotHardware.Joint.LeftArm.LEFT_SHOULDER_PITCH);
		jointsAndChains.add(RobotHardware.Joint.LeftArm.LEFT_SHOULDER_ROLL);
		jointsAndChains.add(RobotHardware.Joint.LeftArm.LEFT_WRIST_YAW);
		jointsAndChains.add(RobotHardware.Joint.RightArm.RIGHT_ELBOW_ROLL);
		jointsAndChains.add(RobotHardware.Joint.RightArm.RIGHT_ELBOW_YAW);
		jointsAndChains.add(RobotHardware.Joint.RightArm.RIGHT_HAND);
		jointsAndChains.add(RobotHardware.Joint.RightArm.RIGHT_SHOULDER_PITCH);
		jointsAndChains.add(RobotHardware.Joint.RightArm.RIGHT_SHOULDER_ROLL);
		jointsAndChains.add(RobotHardware.Chain.HEAD);
		jointsAndChains.add(RobotHardware.Chain.LEFT_ARM);
		jointsAndChains.add(RobotHardware.Chain.LEFT_LEG);
		jointsAndChains.add(RobotHardware.Chain.RIGHT_ARM);
		jointsAndChains.add(RobotHardware.Chain.RIGHT_LEG);
		jointsAndChains.add(RobotHardware.Chain.TORSO);
		jointsAndChains.add(RobotHardware.Joint.RightArm.RIGHT_WRIST_YAW);
		// add value for stiffnessValues
		stiffnessValues.add(0.0f);
		stiffnessValues.add(0.1f);
		stiffnessValues.add(0.2f);
		stiffnessValues.add(0.3f);
		stiffnessValues.add(0.4f);
		stiffnessValues.add(0.5f);
		stiffnessValues.add(0.6f);
		stiffnessValues.add(0.7f);
		stiffnessValues.add(0.8f);
		stiffnessValues.add(0.9f);
		stiffnessValues.add(1.0f);
		// set adapter for name of joints or chains spinner
		spStiffnessNames
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_dropdown_item,
						jointsAndChains));
		spStiffnessNames.setSelection(0);
		// set adapter for value spinner
		spStiffnessValues
				.setAdapter(new ArrayAdapter<Float>(this,
						android.R.layout.simple_spinner_dropdown_item,
						stiffnessValues));
		spStiffnessValues.setSelection(1);
		
		// set event click for buttons
		btSetStiffness.setOnClickListener(this);
		btGetStiffness.setOnClickListener(this);
		btStiffnessInterpolation.setOnClickListener(this);
		btWakeUp.setOnClickListener(this);
		btRest.setOnClickListener(this);
	}

	@Override
	protected void settingView() {
		btSetStiffness = (Button) findViewById(R.id.btSetStiffness);
		btGetStiffness = (Button) findViewById(R.id.btGetStiffness);
		btStiffnessInterpolation = (Button) findViewById(R.id.btStiffnessInterpolation);
		btRest = (Button) findViewById(R.id.btRest);
		btWakeUp = (Button) findViewById(R.id.btWakeUp);
		spStiffnessNames = (Spinner) findViewById(R.id.spStiffnessesNames);
		spStiffnessValues = (Spinner) findViewById(R.id.spStiffnessesValues);
		super.settingView();
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
		return R.layout.activity_motion_stiffness;
	}

	/**
	 * Set stiffness for robot
	 * 
	 * @param names
	 *            String[] list of joints or chains need to set stiffness
	 * @param stiffnesses
	 *            float[] list of values to set stiffness
	 */
	private void setStiffnesses(final String[] names, final float[] stiffnesses) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean result = false;
				try {
					/*
					 * Set stiffness for joint names or chain names .Stiffness
					 * must be between 0 to 1. Value of stiffnesses[i] will used
					 * to set stiffness for names[i]
					 */
					showProgress("Setting stiffnesses...");
					result = RobotMotionStiffnessController.setStiffnesses(
							getRobot(), names, stiffnesses);
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread("Motion stiffness",
							"Set stiffnesses failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (result) {
					showInfoDialogFromThread("Motion stiffness",
							"Set stiffness successfully");
				} else {
					showInfoDialogFromThread("Motion stiffness",
							"Set stiffness failed");
				}
			}
		}).start();

	}

	/**
	 * Get stiffness value of a joint or chain
	 * 
	 * @param name
	 *            joint or chain
	 */
	private void getStiffness(final String name) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				float result[];
				try {
					showProgress("Getting stiffness value...");
					/**
					 * Get stiffness of joint or chain
					 */
					result = RobotMotionStiffnessController.getStiffnesses(
							getRobot(), name);
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread("Motion stiffness",
							"Get stiffness failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (result == null) {
					showInfoDialogFromThread("Motion stiffness",
							"Get stiffness failed: ");
				} else {
					String values = "";
					for (float f : result) {
						values += f + " ; ";
					}
					showInfoDialogFromThread("Motion stiffness",
							"Get stiffness successfully \n Values: " + values);
				}
			}
		}).start();
	}

	/**
	 * Set stiffness interpolation
	 * 
	 * @param names
	 *            String[] list of joints or chains need to set stiffness
	 * @param stiffnesses
	 *            float[] list of values to set stiffness
	 * @param times
	 *            float[] times
	 */
	private void stiffnessInterpolation(final String[] names,
			final float[] stiffnesses, final float[] times) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean result = false;
				try {
					showProgress("Setting stiffness interpolation...");
					/*
					 * Set stiffness interpolation for joint names or chain
					 * names .Stiffness must be between 0 to 1. Value of
					 * stiffnesses[i] will used to set stiffness for names[i].
					 * times[i] is time need to done setting stiffness for
					 * names[i]
					 */
					result = RobotMotionStiffnessController
							.stiffnessInterpolation(getRobot(), names,
									stiffnesses, times);
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(
							"Motion stiffness",
							"Set stiffness interpolation failed: "
									+ e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
				if (result) {
					showInfoDialogFromThread("Motion stiffness",
							"Set stiffness interpolation successfully");
				} else {
					showInfoDialogFromThread("Motion stiffness",
							"Set stiffness interpolation failed! ");
				}

			}
		}).start();
	}

	/**
	 * Robot wake up, set motor on
	 */
	private void wakeUp() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showProgress("Waking up...");
				boolean result = false;
				try {
					// start wake up
					result = RobotMotionStiffnessController.wakeUp(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread("Motion stiffness",
							"Wake up failed: " + e.getMessage());
					e.printStackTrace();
				}
				cancelProgress();
				if (result) {
					showInfoDialogFromThread("Motion stiffness",
							"Wake up successfully");
				} else {
					showInfoDialogFromThread("Motion stiffness",
							"Wake up failed!");
				}
			}
		}).start();
	}

	/**
	 * Robot go to crouch and motor off
	 */
	private void rest() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showProgress("Running rest...");
				boolean result = false;
				try {
					// Robot go to crouch
					// RobotPosture.goToPosture(getRobot(), "Crouch", 1.0f);
					// start rest, set motor off
					result = RobotMotionStiffnessController.rest(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread("Motion stiffness",
							"Rest failed: " + e.getMessage());
					e.printStackTrace();
				}
				cancelProgress();
				if (result) {
					showInfoDialogFromThread("Motion stiffness",
							"Rest successfully");
				} else {
					showInfoDialogFromThread("Motion stiffness", "Rest failed!");
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSetStiffness:
			// get name of joints or chain on spinner
			String[] names = new String[] { (String) spStiffnessNames
					.getSelectedItem() };
			// get value to set stiffness from spinner
			float[] values = new float[] { (Float) spStiffnessValues
					.getSelectedItem() };
			setStiffnesses(names, values);
			break;
		case R.id.btGetStiffness:
			// get name of joints or chain on spinner
			String name = (String) spStiffnessNames.getSelectedItem();
			getStiffness(name);
			break;
		case R.id.btStiffnessInterpolation:
			// get name of joints or chain on spinner
			names = new String[] { (String) spStiffnessNames.getSelectedItem() };
			// get value to set stiffness from spinner
			values = new float[] { (Float) spStiffnessValues.getSelectedItem() };
			float[] times = new float[] { 1.0f };
			stiffnessInterpolation(names, values, times);
			break;
		case R.id.btWakeUp:
			wakeUp();
			break;
		case R.id.btRest:
			rest();
			break;
		default:
			break;
		}

	}

}
