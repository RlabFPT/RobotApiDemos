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
package com.fpt.robot.example.apis.sensors;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.fpt.robot.RobotException;
import com.fpt.robot.RobotHardware;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.sensors.RobotSensor;
import com.fpt.robot.sensors.RobotSensor.RobotSensorListener;
import com.fpt.robot.sensors.RobotSensor.RobotSensorMonitor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
/**
 * This class is used to get joint position sensor value of robot
 * @author Robot Team (FTI)
 *
 */
public class SensorJointPosition extends RobotApiDemoActivity implements
		RobotSensorListener, OnClickListener {
	private final String TAG = "SensorJointPosition";
	private final String INSTRUCTIONS = "This class is used to get joint position sensor value of robot";
	// all joint sensor name
	private final String[] JOINT_SENSOR = new String[] {
			RobotHardware.Sensor.JointPosition.HEAD_PITCH,
			RobotHardware.Sensor.JointPosition.HEAD_YAW,
			RobotHardware.Sensor.JointPosition.LEFT_ANKLE_PITCH,
			RobotHardware.Sensor.JointPosition.LEFT_ANKLE_ROLL,
			RobotHardware.Sensor.JointPosition.LEFT_ELBOW_ROLL,
			RobotHardware.Sensor.JointPosition.LEFT_ELBOW_YAW,
			RobotHardware.Sensor.JointPosition.LEFT_HAND,
			RobotHardware.Sensor.JointPosition.LEFT_HIP_PITCH,
			RobotHardware.Sensor.JointPosition.LEFT_HIP_ROLL,
			RobotHardware.Sensor.JointPosition.LEFT_HIP_YAW_PITCH,
			RobotHardware.Sensor.JointPosition.LEFT_KNEE_PITCH,
			RobotHardware.Sensor.JointPosition.LEFT_SHOULDER_PITCH,
			RobotHardware.Sensor.JointPosition.LEFT_SHOULDER_ROLL,
			RobotHardware.Sensor.JointPosition.LEFT_WRIST_YAW,
			RobotHardware.Sensor.JointPosition.RIGHT_ANKLE_PITCH,
			RobotHardware.Sensor.JointPosition.RIGHT_ANKLE_ROLL,
			RobotHardware.Sensor.JointPosition.RIGHT_ELBOW_ROLL,
			RobotHardware.Sensor.JointPosition.RIGHT_ELBOW_YAW,
			RobotHardware.Sensor.JointPosition.RIGHT_HAND,
			RobotHardware.Sensor.JointPosition.RIGHT_HIP_PITCH,
			RobotHardware.Sensor.JointPosition.RIGHT_HIP_ROLL,
			RobotHardware.Sensor.JointPosition.RIGHT_HIP_YAW_PITCH,
			RobotHardware.Sensor.JointPosition.RIGHT_KNEE_PITCH,
			RobotHardware.Sensor.JointPosition.RIGHT_SHOULDER_PITCH,
			RobotHardware.Sensor.JointPosition.RIGHT_SHOULDER_ROLL,
			RobotHardware.Sensor.JointPosition.RIGHT_WRIST_YAW, };
	// joint sensor name to display on spinner
	private final String[] adapter = new String[] { "JointPosition",
			"HeadPitch", "HeadYaw", "LAnklePitch", "LAnkleRoll", "LElbowRoll",
			"LElbowYaw", "LHand", "LHipPitch", "LHipRoll", "LHipYawPitch",
			"LKneePitch", "LShoulderPitch", "LShoulderRoll", "LWristYaw",
			"RAnklePitch", "RAnkleRoll", "RElbowRoll", "RElbowYaw", "RHand",
			"RHipPitch", "RHipRoll", "RHipYawPitch", "RKneePitch",
			"RShoulderPitch", "RShoulderRoll", "RWristYaw"

	};
	// RobotSensorMonitor to monitoring sensor
	private RobotSensorMonitor monitor;
	// spinner display name of sensors
	private Spinner spSensor;
	// button get sensor value
	private Button btGetValue;
	// text view display sensor value
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initialize monitor
		monitor = new RobotSensorMonitor(getRobot());
		// set listener for monitor
		monitor.setSensorListener(this);
		try {
			// start monitor
			monitor.start();
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// set name of sensors to spinner
		spSensor.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, adapter));
		spSensor.setSelection(0);
		// set event click for button get value
		btGetValue.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		try {
			// stop monitor when activity is destroyed
			monitor.stop();
		} catch (RobotException e) {
			// TODO Auto-generated catch block
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
		return R.layout.activity_sensor_joint_position;
	}

	@Override
	protected void settingView() {
		spSensor = (Spinner) findViewById(R.id.spSensorJointName);
		btGetValue = (Button) findViewById(R.id.btGetValue);
		tvResult = (TextView) findViewById(R.id.tvSensonJointResult);
	}

	/**
	 * Get sensor value with sensorName
	 * @param sensorName
	 * @return sensor value if success or -1 otherwise
	 */
	private float getSensorJointPosition(String sensorName) {
		showProgress("Getting joint position sensor...");
		float result = -1;
		try {
			// start get joint position sensor value 
			result = RobotSensor.getJointPosition(getRobot(), sensorName);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			cancelProgress();
			e.printStackTrace();
		}
		cancelProgress();
		return result;
	}

	/**
	 * Get values of list sensors joint position
	 * @param listSensorName
	 * @return float[] values if success or null otherwise
	 */
	private float[] getListSensorJointPosition(String[] listSensorName) {
		showProgress("Get list sensor joint position...");
		float[] result = null;
		try {
			// start get list value sensors joint position
			result = RobotSensor.getJointListPosition(getRobot(),
					listSensorName);
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			cancelProgress();
			e.printStackTrace();
		}
		cancelProgress();
		return result;
	}

	@SuppressLint("DefaultLocale")
	@Override
	public void onClick(View v) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				float value = -1;
				float[] values = null;
				String sensorValue = "";
				// get index selected on spinner
				int index = spSensor.getSelectedItemPosition();
				switch (index) {
				case 0:
					// get values of all joints position sensor
					values = getListSensorJointPosition(JOINT_SENSOR);
					if (values == null) {
						sensorValue = "Error when get sensors value";
					} else {
						for (int i = 0; i < values.length; i++) {
							sensorValue += adapter[i + 1] + " : " + values[i] + "\n";
						}
					}
					break;

				default:
					// get sensor of joint position
					value = getSensorJointPosition(JOINT_SENSOR[index - 1]);
					if (value == -1) {
						sensorValue = "Error when get sonson value";
					} else {
						sensorValue = String.format("%.3g%n", value);
					}
					break;
				}
				final String text = sensorValue;
				// display sensor value
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						tvResult.setText(text);
					}
				});				
			}
		}).start();
		

	}

	@Override
	public void onFootContactChanged(boolean arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHotJointDetected(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRobotHasFallen() {
		// TODO Auto-generated method stub

	}

}
