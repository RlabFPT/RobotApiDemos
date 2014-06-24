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

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.sensors.RobotBattery;
import com.fpt.robot.sensors.RobotBattery.RobotBatteryListener;
import com.fpt.robot.sensors.RobotBattery.RobotBatteryMonitor;

/**
 * This class is used to get value of sensor about battery of robot
 * @author Robot Team (FTI)
 */
public class SensorBattery extends RobotApiDemoActivity implements
		RobotBatteryListener {
	private static final String TAG = "SensorBattery";
	private static final String INSTRUCTIONS = "This class is used to get value of sensor about battery of robot";
	// monitor to monitoring battery sensor
	private RobotBatteryMonitor monitor;
	// button to refresh getting value of battery sensor 
	private Button btRefresh;
	/*textviews display status of battery*/
	private TextView tvPowerPlugged;
	private TextView tvCurrent;
	private TextView tvMinCell;
	private TextView tvCharging;
	private TextView tvChargeLevel;
	private TextView tvDisCharge;
	private TextView tvFullCharge;
	private TextView tvTemparuture;
	private float currentBattery;
	private float temparatureBattery;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initialize monitor battery
		monitor = new RobotBatteryMonitor(getRobot());
		// set listener
		monitor.setBatteryListener(this);
		try {
			// start monitor
			monitor.start();
		} catch (RobotException e) {
			e.printStackTrace();
		}
		// get values sensor about battery
		getInfor();
		// set event click for button refresh
		btRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getInfor();
			}
		});
	}

	/**
	 * Get battery informations
	 */
	private void getInfor() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showProgress("Get battery informations...");
				try {
					// get value of battery current
					currentBattery = RobotBattery.getBatteryCurrent(getRobot());
					// get value of battery temperature
					temparatureBattery = RobotBattery
							.getBatteryTemperature(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					showInfoDialogFromThread(
							"SensorBattery",
							"Get battery informations failed: "
									+ e.getMessage());
					e.printStackTrace();
					return;
				}
				setText(tvCurrent, currentBattery + "");
				setText(tvTemparuture, temparatureBattery + "");
				cancelProgress();
			}
		}).start();
	}

	@Override
	protected void settingView() {
		btRefresh = (Button) findViewById(R.id.btRefreshBattery);
		tvPowerPlugged = (TextView) findViewById(R.id.tvPowerPlugged);
		tvCurrent = (TextView) findViewById(R.id.tvCurrent);
		tvMinCell = (TextView) findViewById(R.id.tvMinCell);
		tvCharging = (TextView) findViewById(R.id.tvCharging);
		tvChargeLevel = (TextView) findViewById(R.id.tvChargeLevel);
		tvDisCharge = (TextView) findViewById(R.id.tvDischarging);
		tvFullCharge = (TextView) findViewById(R.id.tvFullCharged);
		tvTemparuture = (TextView) findViewById(R.id.tvTemperature);

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
		return R.layout.activity_sensor_battery;
	}

	@Override
	protected void onDestroy() {
		try {
			monitor.stop();
		} catch (RobotException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	public void onBatteryChargeCellVoltageMinChanged(int arg0) {
		setText(tvMinCell, arg0 + "");
	}

	@Override
	public void onBatteryCharging() {
		setText(tvCharging, true + "");
	}

	@Override
	public void onBatteryDischarging() {
		setText(tvDisCharge, true + "");
	}

	@Override
	public void onBatteryFullCharged() {
		setText(tvFullCharge, true + "");
	}

	@Override
	public void onBatteryLevelChanged(int arg0) {
		setText(tvChargeLevel, arg0 + "");
	}

	@Override
	public void onBatteryPowerPlugged() {
		setText(tvPowerPlugged, true + "");
	}

	@Override
	public void onBatteryPowerUnplugged() {
		setText(tvDisCharge, true + "");
	}

	/**
	 * Set text to text view v
	 * @param v
	 * @param text
	 */
	private void setText(final TextView v, final String text) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				v.setText(text);
			}
		});
	}
}
