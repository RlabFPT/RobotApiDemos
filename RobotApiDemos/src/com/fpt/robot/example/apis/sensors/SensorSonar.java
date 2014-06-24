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

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.sensors.RobotSonar;
import com.fpt.robot.sensors.RobotSonar.RobotSonarListener;
import com.fpt.robot.sensors.RobotSonar.RobotSonarMonitor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * This class is used to get sonar value of robot. You can get sonar value on the left or on the right of robot
 * @author Robot Team (FTI)
 *
 */
public class SensorSonar extends RobotApiDemoActivity implements
		RobotSonarListener {
	private final String TAG = "SensorSonar";
	private final String INSTRUCTIONS = "This class is used to get sonar value of robot. You can get sonar value on the left or on the right of robot";
	// Robot sonar monitor to monitoring sonar robot
	private RobotSonarMonitor monitor;
	// button to get value of sonar on the left
	private Button btLeft;
	// button to get value of sonar on the right
	private Button btRight;
	// text view display value of sonar on the left, right of robot
	private TextView tvLeft;
	private TextView tvRight;
	// list view display detail sonars on the left, right of robot
	private ListView lvLeft;
	private ListView lvRight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// initialize monitor
		monitor = new RobotSonarMonitor(getRobot());
		monitor.setSonarListener(this);
		try {
			// start monitor first
			monitor.start();
		} catch (RobotException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// set event click for button get left sonar value
		btLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								getLeftSonarValue();
								getLeftSonarValues();
							}
						});
					}
				}).start();

			}
		});
		// set event for button get right sonar value
		btRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new Thread(new Runnable() {

					@Override
					public void run() {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {
								getRightSonarValue();
								getRightSonarValues();
							}
						});
					}
				}).start();

			}
		});
	}

	/**
	 * Get left sonar value
	 */
	private void getLeftSonarValue() {
		showProgress("Getting left sonnar value...");
		try {
			// get left sonar value
			float value = RobotSonar.getLeftSonarValue(getRobot());
			// display sonar value
			tvLeft.setText(value + "");
		} catch (RobotException e) {
			cancelProgress();
			showInfoDialogFromThread("SensorSonar", "Error: " + e.getMessage());
			e.printStackTrace();
		}
		cancelProgress();
	}

	/**
	 * Get right sonar value
	 */
	private void getRightSonarValue() {
		showProgress("Getting right sonnar value...");
		try {
			// get right sonar value
			float value = RobotSonar.getRightSonarValue(getRobot());
			// display sonar value
			tvRight.setText(value + "");
		} catch (RobotException e) {
			cancelProgress();
			showInfoDialogFromThread("SensorSonar", "Error: " + e.getMessage());
			e.printStackTrace();
		}
		cancelProgress();
	}

	/**
	 * Get left sonars value detail
	 */
	private void getLeftSonarValues() {
		showProgress("Getting left sonnars value...");
		try {
			// get left sonar values
			float[] values = RobotSonar.getLeftSonarValues(getRobot());
			String[] array = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				array[i] = "Value " + i + " : "
						+ String.format("%.2g%n", values[i]);
			}
			// display value to list view
			lvLeft.setAdapter(new ArrayAdapter<String>(SensorSonar.this,
					android.R.layout.simple_list_item_1, array));
		} catch (RobotException e) {
			cancelProgress();
			showInfoDialogFromThread("SensorSonar", "Error: " + e.getMessage());
			e.printStackTrace();
		}
		cancelProgress();
	}

	/**
	 * Get right sonar values detail
	 */
	private void getRightSonarValues() {
		showProgress("Getting right sonnars value...");
		try {
			// get right sonar values
			float[] values = RobotSonar.getRightSonarValues(getRobot());
			String[] array = new String[values.length];
			for (int i = 0; i < values.length; i++) {
				array[i] = "Value " + i + " : "
						+ String.format("%.2g%n", values[i]);
			}
			// display values to list view
			lvRight.setAdapter(new ArrayAdapter<String>(SensorSonar.this,
					android.R.layout.simple_list_item_1, array));
		} catch (RobotException e) {
			cancelProgress();
			showInfoDialogFromThread("SensorSonar", "Error: " + e.getMessage());
			e.printStackTrace();
		}
		cancelProgress();
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
		return R.layout.activity_sensor_sonar;
	}

	@Override
	protected void settingView() {
		btLeft = (Button) findViewById(R.id.btGetLeftValues);
		btRight = (Button) findViewById(R.id.btGetRightValues);
		tvLeft = (TextView) findViewById(R.id.tvLeftValue);
		tvRight = (TextView) findViewById(R.id.tvRightValue);
		lvLeft = (ListView) findViewById(R.id.listViewLeft);
		lvRight = (ListView) findViewById(R.id.listViewRight);
	}

	@Override
	public void onSonarLeftDetected(final float arg0) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvLeft.setText(arg0 + "");
			}
		});
	}

	@Override
	public void onSonarLeftNothingDetected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSonarRightDetected(final float arg0) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvRight.setText(arg0 + "");
			}
		});
	}

	@Override
	public void onSonarRightNothingDetected() {
		// TODO Auto-generated method stub

	}

}
