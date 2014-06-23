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
 * SensorElectricCurrent class is used to get current value electric of robot
 * @author Robot Team (FTI)
 */
@SuppressLint("DefaultLocale")
public class SensorElectricCurrent extends RobotApiDemoActivity implements
		RobotSensorListener, OnClickListener {
	private static final String TAG = "SensorElectricCurrent";
	private static final String INSTRUCTIONS = "SensorElectricCurrent class allows you to get current value electric of robot";
	// Names of electric current sensor
	private final String[] ELECTRIC_SENSOR = new String[] {
			RobotHardware.Sensor.ElectricCurrent.BATTERY,
			RobotHardware.Sensor.ElectricCurrent.HEAD_PITCH,
			RobotHardware.Sensor.ElectricCurrent.HEAD_YAW,
			RobotHardware.Sensor.ElectricCurrent.LEFT_ANKLE_PITCH,
			RobotHardware.Sensor.ElectricCurrent.LEFT_ANKLE_ROLL,
			RobotHardware.Sensor.ElectricCurrent.LEFT_ELBOW_ROLL,
			RobotHardware.Sensor.ElectricCurrent.LEFT_ELBOW_YAW,
			RobotHardware.Sensor.ElectricCurrent.LEFT_HAND,
			RobotHardware.Sensor.ElectricCurrent.LEFT_HIP_PITCH,
			RobotHardware.Sensor.ElectricCurrent.LEFT_HIP_ROLL,
			RobotHardware.Sensor.ElectricCurrent.LEFT_HIP_YAW_PITCH,
			RobotHardware.Sensor.ElectricCurrent.LEFT_KNEE_PITCH,
			RobotHardware.Sensor.ElectricCurrent.LEFT_SHOULDER_PITCH,
			RobotHardware.Sensor.ElectricCurrent.LEFT_SHOULDER_ROLL,
			RobotHardware.Sensor.ElectricCurrent.LEFT_WRIST_YAW,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_ANKLE_PITCH,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_ANKLE_ROLL,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_ELBOW_ROLL,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_ELBOW_YAW,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_HAND,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_HIP_PITCH,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_HIP_ROLL,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_HIP_YAW_PITCH,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_KNEE_PITCH,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_SHOULDER_PITCH,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_SHOULDER_ROLL,
			RobotHardware.Sensor.ElectricCurrent.RIGHT_WRIST_YAW, };
	// Names of electric current sensor display on adapter
	private final String[] adapter = new String[] { "ElectricCurrent",
			"Battery", "HeadPitch", "HeadYaw", "LAnklePitch",
			"LAnkleRoll", "LElbowRoll", "LElbowYaw",
			"LHand", "LhipPitch", "LHipRoll",
			"LHipYawPitch", "LKneePitch", "LShoulderPitch",
			"LShoulderRoll", "LWristYaw", "RAnklePitch",
			"RAnkle-roll", "RElbow-roll", "RElbow-yaw",
			"RHand", "RHipPitch", "RHipRoll",
			"RHipYawPitch", "RKneePitch", "RShoulderPitch",
			"RShoulderRoll", "RWristYaw"

	};
	// RobotSensorMonitor to monitoring sensor
	private RobotSensorMonitor monitor;
	// spinner electric sensor name
	private Spinner spSensor;
	// button get sensor value
	private Button btGetValue;
	// text view to display result of sensor value after getting
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initialize monitor
		monitor = new RobotSensorMonitor(getRobot());
		// set listener
		monitor.setSensorListener(this);
		try {
			// start monitor
			monitor.start();
		} catch (RobotException e) {
			showInfoDialog("SensorElectricCurrent",
					"Start sensor monitor failed: " + e.getMessage());
			e.printStackTrace();
		}
		// set list electric sensor names to spinner
		spSensor.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, adapter));
		spSensor.setSelection(0);
		btGetValue.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		try {
			// stop monitor when activity destroy
			monitor.stop();
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
		return R.layout.activity_sensor_electric_current;
	}

	@Override
	protected void settingView() {
		spSensor = (Spinner) findViewById(R.id.spSensorElectricName);
		btGetValue = (Button) findViewById(R.id.btGetValue);
		tvResult = (TextView) findViewById(R.id.tvSensonElectricResult);
	}

	/**
	 * Get sensor electric current
	 * @param sensorName
	 * @return float value electric current of sensorName
	 */
	private float getSensorElectricCurrent(String sensorName) {
		showProgress("Get joint electric current...");
		float result = -1;
		try {
			// get electric current of a joint
			result = RobotSensor
					.getJointElectricCurrent(getRobot(), sensorName);
		} catch (RobotException e) {
			e.printStackTrace();
		}
		cancelProgress();
		return result;
	}
	/**
	 * Get list sensor values of a list sensor names
	 * @param listSensorName
	 * @return float[] values of list sensor
	 */
	private float[] getListSensorElectricCurrent(String[] listSensorName) {
		showProgress("Get joint list position...");
		float[] result = null;
		try {
			// get values of list joints
			result = RobotSensor.getJointListPosition(getRobot(),
					listSensorName);
		} catch (RobotException e) {
			e.printStackTrace();
		}
		cancelProgress();
		return result;
	}

	public void onClick(final View v) {
		new Thread(new Runnable() {

			public void run() {
				float value = -1;
				float[] values = null;
				String sensorValue = "";
				// get position of selected sensor name
				int index = spSensor.getSelectedItemPosition();
				switch (index) {
				case 0:
					// all electric sensor name
					values = getListSensorElectricCurrent(ELECTRIC_SENSOR);
					if (values == null) {
						sensorValue = "Error when get sensors value";
					} else {
						for (int i = 0; i < values.length; i++) {
							sensorValue += adapter[i + 1] + " : " + values[i]
									+ "\n";
						}
					}
					break;

				default:
					value = getSensorElectricCurrent(ELECTRIC_SENSOR[index - 1]);
					if (value == -1) {
						sensorValue = "Error when get sonson value";
					} else {
						sensorValue = String.format("%.3g%n", value);
					}
					break;
				}
				final String ssValue = sensorValue;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						tvResult.setText(ssValue);
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
