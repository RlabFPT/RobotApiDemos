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
 * Get inner sensor value
 * @author Robot Team (FTI)
 *
 */
public class SensorInertial extends RobotApiDemoActivity implements
		RobotSensorListener,OnClickListener {
	private final String TAG = "SensorInnertial";
	private final String INSTRUCTIONS = "SensorInnertial class allows you get value of inner sensors";
	// robot sensor monitor to monitoring sensor
	private RobotSensorMonitor monitor;
	// names of acceleremoteter sensor
	private final String[] ACCELEREMOETER_SENSOR = new String[] {
			RobotHardware.Sensor.Accelerometer.X,
			RobotHardware.Sensor.Accelerometer.Y,
			RobotHardware.Sensor.Accelerometer.Z };
	// names of gyrometer sensor
	private final String[] GYROMETER_SENSOR = new String[] {
			RobotHardware.Sensor.Gyrometer.X, RobotHardware.Sensor.Gyrometer.Y,
			};
	// names of angle sensor
	private final String[] ANGLE_SENSOR = new String[] {
			RobotHardware.Sensor.Angle.X,
			RobotHardware.Sensor.Angle.Y
	};
	// names of all inner sensor
	private final String[] INERTIAL_SENSOR = new String[] {
			RobotHardware.Sensor.Accelerometer.X,
			RobotHardware.Sensor.Accelerometer.Y,
			RobotHardware.Sensor.Accelerometer.Z,
			RobotHardware.Sensor.Gyrometer.X, 
			RobotHardware.Sensor.Gyrometer.Y,
			RobotHardware.Sensor.Angle.X,
			RobotHardware.Sensor.Angle.Y
			};
	// names of inner sensor display in adapter
	private final String[] adapter = new String[]{
			"All",
			"Accelerometer (X,Y,Z)",
			"Gyrometer (X,Y)",
			"Angle (X,Y)",
			"Accelerometer.X",
			"Accelerometer.Y",
			"Accelerometer.Z",
			"Gyrometer.X",
			"Gyrometer.Y",
			"Angle.X",
			"Angle.Y"
	};
	// spinner names of inner sensor
	private Spinner spSensor;
	// button get value of selected inner sensor
	private Button btGetValue;
	// text view display sensor value
	private TextView tvResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initial monitor
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
		// set inner name sensors to spinner
		spSensor.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, adapter));
		// set event click for button get value
		btGetValue.setOnClickListener(this);
	}

	/**
	 * Get sensor value of sensorName
	 * @param sensorName
	 * @return value of sensor if success or -1 otherwise
	 */
	private float getSensorInertial(String sensorName) {
		showProgress("Get sensor inertial...");
		float result = -1;
		try {
			// get sensor value
			result = RobotSensor.getSensorValue(getRobot(), sensorName);
		} catch (RobotException e) {
			cancelProgress();
			e.printStackTrace();
		}
		cancelProgress();
		return result;
	}

	/**
	 * Get sensor value of list name of sensors
	 * @param listSensorName
	 * @return array float value of sensors if success or null otherwise
	 */
	private float[] getListSensorInertial(String[] listSensorName) {
		showProgress("Get List Sensor Inertial...");
		float[] result = null;
		try {
			// get list sensor value
			result = RobotSensor.getSensorListValue(getRobot(), listSensorName);
		} catch (RobotException e) {
			cancelProgress();
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
		return R.layout.activity_sensor_inertial;
	}

	@Override
	protected void settingView() {
		spSensor = (Spinner) findViewById(R.id.spSensorName);
		btGetValue = (Button) findViewById(R.id.btGetValue);
		tvResult = (TextView) findViewById(R.id.tvSensonInerResult);
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

	@SuppressLint("DefaultLocale") @Override
	public void onClick(View v) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// get index of item selected on spinner
				int indexSelected = spSensor.getSelectedItemPosition();
				float value = -1;
				float[] values = null;
				String sensorValue = "";
				switch (indexSelected) {
				case 0:
					// get all inner sensors value
					values = getListSensorInertial(INERTIAL_SENSOR);
					if (values==null){
						sensorValue = "Error when get sensors value";
					} else{
						for (int i=0;i<values.length;i++){
							sensorValue += adapter[i+4] + " : " + values[i] +"\n";
						}
					}
					break;
				case 1:
					// get all acceleremoeter sensors value
					values = getListSensorInertial(ACCELEREMOETER_SENSOR);
					if (values==null){
						sensorValue = "Error when get sensors value";
					} else{
						for (int i=0;i<values.length;i++){
							sensorValue += adapter[i+4] +" : " + values[i] +"\n";
						}
					}
					break;
				case 2:
					// get all gyrometer sensors value
					values = getListSensorInertial(GYROMETER_SENSOR);
					if (values==null){
						sensorValue = "Error when get sensors value";
					} else{
						for (int i=0;i<values.length;i++){
							sensorValue += adapter[i+7]+ " : " + values[i] +"\n";
						}
					}
					break;
				case 3:
					// get all angle sensors value
					values = getListSensorInertial(ANGLE_SENSOR);
					if (values==null){
						sensorValue = "Error when get sensors value";
					} else{
						for (int i=0;i<values.length;i++){
							sensorValue += adapter[i+9]+ " : " + values[i] +"\n";
						}
					}
					break;
				case 4:
					value = getSensorInertial(ACCELEREMOETER_SENSOR[0]);
					if (value==-1){
						sensorValue = "Error when get sonson value";
					} else{
						sensorValue = String.format("%.3g%n", value);
					}
					break;
				case 5:
					value = getSensorInertial(ACCELEREMOETER_SENSOR[1]);
					if (value==-1){
						sensorValue = "Error when get sonson value";
					} else{
						sensorValue = String.format("%.3g%n", value);
					}
					break;
				case 6:
					value = getSensorInertial(ACCELEREMOETER_SENSOR[2]);
					if (value==-1){
						sensorValue = "Error when get sonson value";
					} else{
						sensorValue = String.format("%.3g%n", value);
					}
					break;
				case 7:
					value = getSensorInertial(GYROMETER_SENSOR[0]);
					if (value==-1){
						sensorValue = "Error when get sonson value";
					} else{
						sensorValue = String.format("%.3g%n", value);
					}
					break;
				case 8:
					value = getSensorInertial(GYROMETER_SENSOR[1]);
					if (value==-1){
						sensorValue = "Error when get sonson value";
					} else{
						sensorValue = String.format("%.3g%n", value);
					}
					break;
				case 9:
					value = getSensorInertial(ANGLE_SENSOR[0]);
					if (value==-1){
						sensorValue = "Error when get sonson value";
					} else{
						sensorValue = String.format("%.3g%n", value);
					}
					break;
				case 10:
					value = getSensorInertial(ANGLE_SENSOR[1]);
					if (value==-1){
						sensorValue = "Error when get sonson value";
					} else{
						sensorValue = String.format("%.3g%n", value);
					}
					break;		
				default:
					sensorValue = "Invalib sensor name";
					break;
				}
				final String text = sensorValue;
				// display sensor value to text view
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						tvResult.setText(text);
					}
				});
				
			}
		}).start();
		
	}

}
