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
 * 
 * @author QUYET
 *
 */
public class SensorTemperature extends RobotApiDemoActivity implements
		RobotSensorListener, OnClickListener {
	private final String TAG = "SensorTemperature";
	private final String INSTRUCTIONS = "SensorTemperature class allows you to get sensor temperature value of robot";
	// name of temperature sensor
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
			RobotHardware.Sensor.Temperature.RIGHT_WRIST_YAW,
			 };
	// name display on spinner
	private final String[] adapter = new String[]{
			"Temperature",
			"Battery",
			"HeadPitch",
			"HeadYaw",
			"LAnklePitch",
			"LAnkleRoll",
			"LElbowRoll",
			"LElbowYaw",
			"LHand",
			"LHipPitch",
			"LHipRoll",
			"LHipYawPitch",
			"LKneePitch",
			"LShoulderPitch",
			"LShoulderRoll",
			"LWristYaw",
			"RAnklePitch",
			"RAnkleRoll",
			"RElbowRoll",
			"RElbowYaw",
			"RHand",
			"RHipPitch",
			"RHipRoll",
			"RKneePitch",
			"RShoulderPitch",
			"RShoulderRoll",
			"RWristYaw"
			
			
	};
	// Robot sensor monitor to monitoring sensor
	RobotSensorMonitor monitor;
	// spinner display name of sensor
	private Spinner spSensor;
	// button get value
	private Button btGetValue;
	// display sensor value
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
			// start monitor first
			monitor.start();
		} catch (RobotException e) {
			e.printStackTrace();
		}
		// set adapter for spinner to display sensor name
		spSensor.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, adapter));
		spSensor.setSelection(0);
		// set event click for button get value
		btGetValue.setOnClickListener(this);
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
	protected void onDestroy() {
		try {
			// stop monitor when activity is destroyed
			monitor.stop();
		} catch (RobotException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_sensor_temperature;
	}

	@Override
	protected void settingView() {
		spSensor = (Spinner) findViewById(R.id.spSensorTemperatureName);
		btGetValue = (Button) findViewById(R.id.btGetValue);
		tvResult = (TextView) findViewById(R.id.tvSensonTemperatureResult);
	}

	/**
	 * Get sensor temperature of sensor name
	 * @param sensorName
	 * @return sensor value if success, -1 otherwise
	 */
	private float getSensorTemperature(String sensorName) {
		showProgress("Getting joint temperature...");
		float result = -1;
		try {
			// start get joint temperature value
			result = RobotSensor.getJointTemperature(getRobot(), sensorName);
		} catch (RobotException e) {
			cancelProgress();
			showInfoDialogFromThread("Sensor Temperature","Error: " + e.getMessage());
			e.printStackTrace();
		}
		cancelProgress();
		return result;
	}

	/**
	 * Get list sensors temperature values
	 * @param listSensorName
	 * @return float[] values if success, null otherwise
	 */
	private float[] getListSensorTemperature(String[] listSensorName) {
		showProgress("Getting list joint temperature...");
		float[] result = null;
		try {
			// start get list temperature values
			result = RobotSensor.getJointListTemperature(getRobot(), listSensorName);
		} catch (RobotException e) {
			cancelProgress();
			showInfoDialogFromThread("Sensor Temperature","Error: " + e.getMessage());
			e.printStackTrace();
		}
		cancelProgress();
		return result;
	}
	
	
	@SuppressLint("DefaultLocale") @Override
	public void onClick(View v) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				float value = -1;
				float[] values = null;
				String sensorValue = "";
				// get selected index on spinner
				int index = spSensor.getSelectedItemPosition();
				switch (index) {
				case 0:
					// get all temperature sensor values
					values = getListSensorTemperature(TEMPERATURE_SENSOR);
					if (values==null){
						sensorValue = "Error when get sensors value";
					} else{
						for (int i=0;i<values.length;i++){
							sensorValue += adapter[i+1] + " : " + values[i] +"\n";
						}
					}
					break;

				default:
					// get temperature sensor value
					value = getSensorTemperature(TEMPERATURE_SENSOR[index-1]);
					if (value==-1){
						sensorValue = "Error when get sonson value";
					} else{
						sensorValue = String.format("%.3g%n", value);
					}
					break;
				}
				final String text = sensorValue;
				// display value
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
