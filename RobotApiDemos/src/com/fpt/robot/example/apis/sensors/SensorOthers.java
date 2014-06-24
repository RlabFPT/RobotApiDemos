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
package com.fpt.robot.example.apis.sensors;

import android.os.Bundle;
import android.util.Log;
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
import com.fpt.robot.sensors.RobotSensor;
import com.fpt.robot.sensors.RobotSensor.RobotSensorListener;
import com.fpt.robot.sensors.RobotSensor.RobotSensorMonitor;
/**
 * This class is used to get sensor value of a sensor name after get all value sensor from bundle
 * @author QUYET
 *
 */
public class SensorOthers extends RobotApiDemoActivity implements
		RobotSensorListener, OnClickListener {	
	private final String TAG = "SensorOther";
	private final String INSTRUCTIONS = "This class is used to get sensor value of a sensor name after " +
			"get all value sensor from bundle. Select name of sensor value which you want to get value in spinner and " +
			"click Get Value.";
	/**
	 * Sensor name of sensor name display on spinners
	 */
	private final String[] NORMAL_SENSOR = new String[] {
			RobotHardware.Sensor.FSR.LeftFoot.CenterOfPressure.X,
			RobotHardware.Sensor.FSR.LeftFoot.CenterOfPressure.Y,
			RobotHardware.Sensor.FSR.LeftFoot.FRONT_LEFT,
			RobotHardware.Sensor.FSR.LeftFoot.FRONT_RIGHT,
			RobotHardware.Sensor.FSR.LeftFoot.REAR_LEFT,
			RobotHardware.Sensor.FSR.LeftFoot.REAR_RIGHT,
			RobotHardware.Sensor.FSR.LeftFoot.TOTAL_WEIGHT,
			RobotHardware.Sensor.FSR.RightFoot.CenterOfPressure.X,
			RobotHardware.Sensor.FSR.RightFoot.CenterOfPressure.Y,
			RobotHardware.Sensor.FSR.RightFoot.FRONT_LEFT,
			RobotHardware.Sensor.FSR.RightFoot.FRONT_RIGHT,
			RobotHardware.Sensor.FSR.RightFoot.REAR_LEFT,
			RobotHardware.Sensor.FSR.RightFoot.REAR_RIGHT,
			RobotHardware.Sensor.FSR.RightFoot.TOTAL_WEIGHT };
	private final String[] adapter_sensor_normal = new String[] {
			"LeftFoot-CenterPress.X", "LeftFoot-CenterPress.Y",
			"LeftFoot-Front-Left", "LeftFoot-Front-Right",
			"LeftFoot-Rear-Left", "LeftFoot-Rear-Right",
			"LeftFoot-Total-Weight", "RightFoot-CenterPress.X",
			"RightFoot-CenterPress.Y", "RightFoot-Front-Left",
			"RightFoot-Front-Right", "RightFoot-Rear-Left",
			"RightFoot-Rear-Right", "RightFoot-Total-Weight", };
	private final String[] TEMPERATURE_SENSOR = new String[] {
			RobotHardware.Sensor.Temperature.BATTERY,
			RobotHardware.Sensor.Temperature.HEAD_PITCH,
			RobotHardware.Sensor.Temperature.HEAD_YAW,
			RobotHardware.Sensor.Temperature.LEFT_ANKLE_PITCH,
			RobotHardware.Sensor.Temperature.LEFT_ANKLE_ROLL,
			RobotHardware.Sensor.Temperature.LEFT_ELBOW_ROLL,
			RobotHardware.Sensor.Temperature.LEFT_ELBOW_YAW,
			RobotHardware.Sensor.Temperature.LEFT_HAND,
			RobotHardware.Sensor.Temperature.LEFT_HIP_PITCH,
			RobotHardware.Sensor.Temperature.LEFT_HIP_ROLL,
			RobotHardware.Sensor.Temperature.LEFT_HIP_YAW_PITCH,
			RobotHardware.Sensor.Temperature.LEFT_KNEE_PITCH,
			RobotHardware.Sensor.Temperature.LEFT_SHOULDER_PITCH,
			RobotHardware.Sensor.Temperature.LEFT_SHOULDER_ROLL,
			RobotHardware.Sensor.Temperature.LEFT_WRIST_YAW,
			RobotHardware.Sensor.Temperature.RIGHT_ANKLE_PITCH,
			RobotHardware.Sensor.Temperature.RIGHT_ANKLE_ROLL,
			RobotHardware.Sensor.Temperature.RIGHT_ELBOW_ROLL,
			RobotHardware.Sensor.Temperature.RIGHT_ELBOW_YAW,
			RobotHardware.Sensor.Temperature.RIGHT_HAND,
			RobotHardware.Sensor.Temperature.RIGHT_HIP_PITCH,
			RobotHardware.Sensor.Temperature.RIGHT_HIP_ROLL,
			RobotHardware.Sensor.Temperature.RIGHT_KNEE_PITCH,
			RobotHardware.Sensor.Temperature.RIGHT_SHOULDER_PITCH,
			RobotHardware.Sensor.Temperature.RIGHT_SHOULDER_ROLL,
			RobotHardware.Sensor.Temperature.RIGHT_WRIST_YAW, };
	private final String[] adapter_sensor_temperature = new String[] {
			"Temperature", "Battery", "Head-pitch", "Head-yaw",
			"Left-ankle-pitch", "Left-ankle-roll", "Left-elbow-roll",
			"Left-elbow-yaw", "Left-hand", "Left-hip-pitch", "Left-hip-roll",
			"Left-hip-yaw-pitch", "Left-knee-pitch", "Left-shoulder-pitch",
			"Left-shoulder-roll", "Left-wrist-yaw", "Right-ankle-pitch",
			"Right-ankle-roll", "Right-elbow-roll", "Right-elbow-yaw",
			"Right-hand", "Right-hip-pitch", "Right-hip-roll",
			"Right-knee-pitch", "Right-shoulder-pitch", "Right-shoulder-roll",
			"Right-wrist-yaw" };
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
	private final String[] adapter_electric = new String[] { "ElectricCurrent",
			"Battery", "Head-pitch", "Head-yaw", "Left-ankle-pitch",
			"Left-ankle-roll", "Left-elbow-roll", "Left-elbow-yaw",
			"Left-hand", "Left-hip-pitch", "Left-hip-roll",
			"Left-hip-yaw-pitch", "Left-knee-pitch", "Left-shoulder-pitch",
			"Left-shoulder-roll", "Left-wrist-yaw", "Right-ankle-pitch",
			"Right-ankle-roll", "Right-elbow-roll", "Right-elbow-yaw",
			"Right-hand", "Right-hip-pitch", "Right-hip-roll",
			"Right-hip-yaw-pitch", "Right-knee-pitch", "Right-shoulder-pitch",
			"Right-shoulder-roll", "Right-wrist-yaw" };
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
	private final String[] adapter_sensor_joint = new String[] {
			"JointPosition", "Head-pitch", "Head-yaw", "Left-ankle-pitch",
			"Left-ankle-roll", "Left-elbow-roll", "Left-elbow-yaw",
			"Left-hand", "Left-hip-pitch", "Left-hip-roll",
			"Left-hip-yaw-pitch", "Left-knee-pitch", "Left-shoulder-pitch",
			"Left-shoulder-roll", "Left-wrist-yaw", "Right-ankle-pitch",
			"Right-ankle-roll", "Right-elbow-roll", "Right-elbow-yaw",
			"Right-hand", "Right-hip-pitch", "Right-hip-roll",
			"Right-hip-yaw-pitch", "Right-knee-pitch", "Right-shoulder-pitch",
			"Right-shoulder-roll", "Right-wrist-yaw"

	};

	// Robot sensor monitor to monitoring sensor
	private RobotSensorMonitor sensorMonitor;
	/*Spinner name of sensor to get value*/
	private Spinner spSensorNormal;
	private Spinner spSensorTemperature;
	private Spinner spSensorElectric;
	private Spinner spSensorJointPosition;
	// button get sensors value
	private Button btGetValue;
	/*bundle which is save sensors value*/
	private Bundle bdSensorNormal;
	private Bundle bdSensorTemperature;
	private Bundle bdSensorElectric;
	private Bundle bdSensorJoint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initialize monitor
		sensorMonitor = new RobotSensorMonitor(getRobot());
		// set listener for monitor
		sensorMonitor.setSensorListener(this);
		// set event click for button get value
		btGetValue.setOnClickListener(this);
		// start monitor first
		try {
			sensorMonitor.start();
		} catch (RobotException e) {
			e.printStackTrace();
		}
		// set adapter
		spSensorNormal.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item,
				adapter_sensor_normal));
		spSensorNormal.setSelection(0);
		spSensorElectric
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_spinner_dropdown_item,
						adapter_electric));
		spSensorElectric.setSelection(0);
		spSensorTemperature.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item,
				adapter_sensor_temperature));
		spSensorTemperature.setSelection(0);
		spSensorJointPosition.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item,
				adapter_sensor_joint));
		spSensorJointPosition.setSelection(0);

	}

	@Override
	protected void settingView() {
		spSensorNormal = (Spinner) findViewById(R.id.spSensorNormal);
		spSensorElectric = (Spinner) findViewById(R.id.spSensorJointElectric);
		spSensorTemperature = (Spinner) findViewById(R.id.spSensorJointTemperature);
		spSensorJointPosition = (Spinner) findViewById(R.id.spSensorJointPosition);
		btGetValue = (Button) findViewById(R.id.btGetValue);
		super.settingView();
	}

	@Override
	protected void onDestroy() {
		try {
			// stop monitor when activity is destroyed
			sensorMonitor.stop();
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
		return R.layout.activity_sensor_others;
	}

	/**
	 * get sensors value and save it in bundle
	 */
	private void getSensorToBundle() {

		try {
			// get sensors value
			bdSensorNormal = RobotSensor.getSensorsValue(getRobot(),
					NORMAL_SENSOR);
			// get sensors electric
			bdSensorElectric = RobotSensor.getJointsElectricCurrent(getRobot(),
					ELECTRIC_SENSOR);
			// get sensors joint position
			bdSensorJoint = RobotSensor.getJointsPosition(getRobot(),
					JOINT_SENSOR);
			// get sensors temperature
			bdSensorTemperature = RobotSensor.getJointsTemperature(getRobot(),
					TEMPERATURE_SENSOR);
		} catch (RobotException e) {
			showInfoDialogFromThread(TAG, "Initial failed: " + e.getMessage());
			e.printStackTrace();
			return;
		}

	}

	/**
	 * Get value of sensor name which is selected from spinner
	 */
	private void getValue() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					// get all sensors value 
					getSensorToBundle();
					showProgress("Getting value...");
					/*Get index selected from spinner*/
					int normalIndex = spSensorNormal.getSelectedItemPosition();
					int electricIndex = spSensorElectric
							.getSelectedItemPosition();
					int temperatureIndex = spSensorTemperature
							.getSelectedItemPosition();
					int jointIndex = spSensorJointPosition
							.getSelectedItemPosition();
					/*Get name of sensor which is selected*/
					String normalName = NORMAL_SENSOR[normalIndex];
					String electricName = ELECTRIC_SENSOR[electricIndex];
					String temperatureName = TEMPERATURE_SENSOR[temperatureIndex];
					String jointName = JOINT_SENSOR[jointIndex];
					Log.d(TAG, normalIndex + "," + electricIndex + ","
							+ temperatureIndex + "," + jointIndex);
					Log.d(TAG, normalName + electricName + temperatureName
							+ jointName);
					/*Get value from bundle*/
					float normalValue = bdSensorNormal.getFloat(normalName,
							0.0f);
					float electricValue = bdSensorElectric.getFloat(
							electricName, 0.0f);
					float temperatureValue = bdSensorTemperature.getFloat(
							temperatureName, 0.0f);
					float jointValue = bdSensorJoint.getFloat(jointName, 0.0f);
					// append value to result
					String resultShow = adapter_sensor_normal[normalIndex]
							+ ": " + normalValue + "\n";
					resultShow += adapter_electric[electricIndex] + ": "
							+ electricValue + "\n";
					resultShow += adapter_sensor_joint[jointIndex] + ": "
							+ jointValue + "\n";
					resultShow += adapter_sensor_temperature[temperatureIndex]
							+ ": " + temperatureValue + "\n";
					// show result
					showInfoDialogFromThread(TAG, resultShow);
					cancelProgress();
				} catch (Exception e) {
					cancelProgress();
					showInfoDialogFromThread(TAG,
							"Getting value failed: " + e.getMessage());
				}
			}
		}).start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btGetValue:
			getValue();
			break;

		default:
			break;
		}

	}

	@Override
	public void onRobotHasFallen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFootContactChanged(boolean touchToGround) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHotJointDetected(String motorBoard) {
		// TODO Auto-generated method stub

	}

}
