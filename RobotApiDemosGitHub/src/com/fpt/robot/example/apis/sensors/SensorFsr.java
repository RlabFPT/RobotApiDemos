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
 * Sensor FSR
 * @author Robot Team (FTI)
 */
public class SensorFsr extends RobotApiDemoActivity implements RobotSensorListener,OnClickListener{
	private static final String TAG = "SensorFsr";
	private static final String INSTRUCTIONS = "Sensor FSR";
	// list FSR Sensor name
	private final String[] FSR_SENSOR = new String[] {
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
			RobotHardware.Sensor.FSR.RightFoot.TOTAL_WEIGHT
			};
	// FSR sensor name display
	private final String[] adapter = new String[]{
			"LFoot.CenterOfPressure.X",
			"LFoot.CenterOfPressure.Y",
			"LFoot.FrontLeft",
			"LFoot.FrontRight",
			"LFoot.RearLeft",
			"LFoot.RearRight",
			"LFoot.TotalWeight",
			"RFoot.CenterOfPressure.X",
			"RFoot.CenterOfPressure.Y",
			"RFoot.FrontLeft",
			"RFoot.FrontRight",
			"RFoot.RearLeft",
			"RFoot.RearRight",
			"RFoot.TotalWeight",
			
	};
	// Monitor to monitoring robot sensor
	private RobotSensorMonitor monitor;
	// spinner name of sensor FSR
	private Spinner spSensor;
	// button get value of selected FSR value
	private Button btGetValue;
	// text view display value of sensor
	private TextView tvResult;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initialize sensor monitor 
		monitor = new RobotSensorMonitor(getRobot());
		// set listener
		monitor.setSensorListener(this);
		try {
			monitor.start();
		} catch (RobotException e) {

			e.printStackTrace();
		}
		// set FSR names to spinner
		spSensor.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, adapter));
		spSensor.setSelection(0);
		// set event click for button get sensor value
		btGetValue.setOnClickListener(this);
		
	}

	/**
	 * Get sensor FSR with sensorName
	 * @param sensorName
	 * @return float is value of sensorName value if success, -1 otherwise
	 */
	private float getSensorFSR(String sensorName) {
		showProgress("Get FSR sensor value...");
		float result = -1;
		try {
			result = RobotSensor.getSensorValue(getRobot(), sensorName);
		} catch (RobotException e) {

			e.printStackTrace();
		}
		cancelProgress();
		return result;
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
		return R.layout.activity_sensor_fsr;
	}

	@Override
	protected void settingView() {
		
		spSensor = (Spinner) findViewById(R.id.spSensorFsrName);
		tvResult = (TextView) findViewById(R.id.tvSensonFsrResult);
		btGetValue = (Button) findViewById(R.id.btGetValue);
	}

	@SuppressLint("DefaultLocale") @Override
	public void onClick(View v) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// get index of selected item in spinner to get name of sensor
				int index = spSensor.getSelectedItemPosition();
				// get sensor value with name
				float value = getSensorFSR(FSR_SENSOR[index]);
				String sensorValue = "";
				if (value==-1){
					sensorValue = "Error when get sonson value";
				} else{
					sensorValue = String.format("%.3g%n", value);
				}
				final String valueSensors = sensorValue;
				// display sensor value to text view
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						tvResult.setText(valueSensors);
					}
				});				
			}
		}).start();		
	}

	@Override
	public void onFootContactChanged(boolean arg0) {
		// TODO
		
	}

	@Override
	public void onHotJointDetected(String arg0) {
		// TODO
		
	}

	@Override
	public void onRobotHasFallen() {
		//TODO
		
	}

}
