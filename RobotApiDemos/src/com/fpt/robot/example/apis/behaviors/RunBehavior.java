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
package com.fpt.robot.example.apis.behaviors;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.fpt.robot.RobotException;
import com.fpt.robot.behavior.RobotBehavior;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;

/**
 * This class is used to run predefined behaviors on robot
 * 
 * @author Robot Team (FTI)
 */

public class RunBehavior extends RobotApiDemoActivity implements
		OnItemClickListener {
	private static final String TAG = "RunBehavior";
	private static final String INSTRUCTIONS = "This class is used to run predefined behaviors on robot. " +
			"Click \"Refresh\" to get list predefined behaviors on robot. Choose one behavior on the list " +
			"and lick \"Run behavior\" to run it";
	// Button to run behavior
	private Button btRunSelectedBehavior;
	// Button to get list behavior
	private Button btRefreshBehaviorList;
	// Listview contain list behaviors
	private ListView lvInstalledBehaviorList;
	// index selected of listview
	private int selectedBehaviorIndex = ListView.INVALID_POSITION;
	// array contain list behaviors
	private ArrayList<String> installedBehaviorList;
	// array adapter contain behaviors list
	private ArrayAdapter<String> installedBehaviorListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// set event click for button run behavior
		btRunSelectedBehavior.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAlertDialog(TAG, "Are you sure run this behavior?", null,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								runSelectedBehavior();
							}
						}, null);

			}
		});
		// set event click for button get list behaviors
		btRefreshBehaviorList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshInstalledBehaviorList();
			}
		});
		
		// set adapter for list behaviors
		installedBehaviorList = new ArrayList<String>();
		installedBehaviorListAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_single_choice,
				installedBehaviorList);
		installedBehaviorListAdapter.setNotifyOnChange(true);

		lvInstalledBehaviorList.setItemsCanFocus(true);
		lvInstalledBehaviorList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvInstalledBehaviorList.setAdapter(installedBehaviorListAdapter);
		// set event click for list view
		lvInstalledBehaviorList.setOnItemClickListener(this);
		// get list behavior
		refreshInstalledBehaviorList();
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
			// show help
			showInfoDialog(TAG, INSTRUCTIONS);
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_run_behavior;
	}

	/**
	 * Get list behavior which is installed on robot
	 */
	private void refreshInstalledBehaviorList() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				String[] behaviorList;
				showProgress("getting installed behavior list...");
				try {
					// Get list behaviors
					behaviorList = RobotBehavior
							.getInstalledBehaviors(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					// makeToast("get installed behaviors failed! " +
					// e.getMessage());
					showInfoDialogFromThread("Run behavior",
							"get installed behaviors failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (behaviorList == null || behaviorList.length == 0) {
					// makeToast("none behavior available!");
					showInfoDialogFromThread("Run behavior",
							"None of behavior is available!");
					return;
				}
				updateInstalledBehaviorList(behaviorList);
			}
		}).start();
	}

	/*
	 * Update installed list behaviors to adapter
	 */
	protected void updateInstalledBehaviorList(final String[] behaviorList) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				selectedBehaviorIndex = ListView.INVALID_POSITION;
				installedBehaviorListAdapter.clear();
				for (int i = 0; i < behaviorList.length; i++) {
					installedBehaviorListAdapter.add(behaviorList[i]);
				}
			}
		});
	}

	protected void runSelectedBehavior() {
		Log.d(TAG, "runSelectedBehavior()");
		int position = selectedBehaviorIndex;
		Log.d(TAG, "selected index: " + position);
		if (position == ListView.INVALID_POSITION) {
			Log.e(TAG, "nothing selected!");
			return;
		}
		if (position >= installedBehaviorListAdapter.getCount()) {
			Log.e(TAG, "out of range!");
			return;
		}
		final String behaviorName = installedBehaviorListAdapter
				.getItem(position);
		Log.d(TAG, "behavior: " + behaviorName);
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("running behavior [" + behaviorName + "]...");
				try {
					// Run behavior with name is behaviorName on robot
					RobotBehavior.runBehavior(getRobot(), behaviorName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					showInfoDialogFromThread("Run behavior", "run behavior ["
							+ behaviorName + "] failed! " + e.getMessage());
					return;
				}
				cancelProgress();
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Log.d(TAG, "onItemClick(position=" + position + ")");
		selectedBehaviorIndex = position;
	}

	@Override
	protected void settingView() {
		btRunSelectedBehavior = (Button) findViewById(R.id.btRunSelectedBehavior);
		btRefreshBehaviorList = (Button) findViewById(R.id.btRefreshInstalledBehaviorList);
		lvInstalledBehaviorList = (ListView) findViewById(R.id.lvInstalledBehaviorList);
	}
}
