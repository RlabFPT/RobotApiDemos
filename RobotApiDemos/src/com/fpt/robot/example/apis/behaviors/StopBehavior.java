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
 * This class is used to stop running behavior on robot
 * @author Robot Team (FTI)
 */
public class StopBehavior extends RobotApiDemoActivity implements OnItemClickListener {
	private static final String TAG="StopBehavior";
	private static final String INSTRUCTION = "This class is used to stop running behavior on robot";
	// Button to stop selected behavior
	private Button btStopSelectedBehavior;
	// Button to stop all behaviors
	private Button btStopAllBehaviors;
	// Button to get list behaviors is running
	private Button btRefreshBehaviorList;
	// List view bahaviors name
	private ListView lvRunningBehaviorList;
	// Index behavior selected
	private int selectedBehaviorIndex = ListView.INVALID_POSITION;

	private ArrayList<String> runningBehaviorList;
	private ArrayAdapter<String> runningBehaviorListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		// show instructions
		showInfoDialog(TAG, INSTRUCTION);
		btStopSelectedBehavior.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopSelectedBehavior();
			}
		});
		
		btRefreshBehaviorList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				refreshRunningBehaviorList();
			}
		});
		
		btStopAllBehaviors.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				stopAllBehaviors();
			}
		});
		
		runningBehaviorList = new ArrayList<String>();
		runningBehaviorListAdapter = new ArrayAdapter<String>(this, 
        		android.R.layout.simple_list_item_single_choice, runningBehaviorList);
		runningBehaviorListAdapter.setNotifyOnChange(true);
		
		lvRunningBehaviorList.setItemsCanFocus(true);
		lvRunningBehaviorList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lvRunningBehaviorList.setAdapter(runningBehaviorListAdapter);
		lvRunningBehaviorList.setOnItemClickListener(this);
        
		refreshRunningBehaviorList();
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
			showInfoDialog(TAG, INSTRUCTION);
			break;

		default:
			break;
		}
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_stop_behavior;
	}

	/**
	 * Get list behaviors which are running on robot
	 */
	protected void refreshRunningBehaviorList() {
		new Thread(new Runnable() {					
			@Override
			public void run() {
				String[] behaviorList;
				showProgress("getting running behavior list...");
				try {
					// get running list behaviors
					behaviorList = RobotBehavior.getRunningBehaviors(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					//makeToast("get running behaviors failed! " + e.getMessage());
					showInfoDialogFromThread("Stop behavior", 
							"get running behaviors failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (behaviorList == null || behaviorList.length == 0) {
					//makeToast("none behavior running!");
					showInfoDialogFromThread("Stop behavior", 
							"None of behavior is running!");
					return;
				}
				updateRunningBehaviorList(behaviorList);
			}
		}).start();
	}

	/*
	 * Update list behaviors which are running to adapter
	 */
	protected void updateRunningBehaviorList(final String[] behaviorList) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				selectedBehaviorIndex = ListView.INVALID_POSITION;
				runningBehaviorListAdapter.clear();
				for (int i = 0; i < behaviorList.length; i++) {
					runningBehaviorListAdapter.add(behaviorList[i]);
				}
			}
		});
	}

	/**
	 * Stop exactly behavior which is selected
	 */
	protected void stopSelectedBehavior() {
		Log.d(TAG, "stopSelectedBehavior()");
		int position = selectedBehaviorIndex;
		Log.d(TAG, "selected index: " + position);
		if (position == ListView.INVALID_POSITION) {
			Log.e(TAG, "nothing selected!");
			return;
		}
		if (position >= runningBehaviorListAdapter.getCount()) {
			Log.e(TAG, "out of range!");
			return;
		}
		final String behaviorName = runningBehaviorListAdapter.getItem(position);
		Log.d(TAG, "behavior: " + behaviorName);
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("stopping behavior [" + behaviorName + "]...");
				try {
					// Stop behavior which have name is bahaviorName
					RobotBehavior.stopBehavior(getRobot(), behaviorName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					showInfoDialogFromThread("Stop behavior", 
							"stop behavior [" + behaviorName + "] failed! " + e.getMessage());
					return;
				}
				cancelProgress();
			}
		}).start();
	}

	/**
	 * Stop all behaviors which are running
	 */
	protected void stopAllBehaviors() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("stopping all behaviors...");
				try {
					// Stop all behavior
					RobotBehavior.stopAllBehaviors(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					showInfoDialogFromThread("Stop behavior", 
							"stop all behaviors failed! " + e.getMessage());
					return;
				}
				cancelProgress();
			}
		}).start();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(TAG, "onItemClick(position=" + position + ")");
		selectedBehaviorIndex = position;
	}

	@Override
	protected void settingView() {
		btStopSelectedBehavior = (Button)findViewById(R.id.btStopSelectedBehavior);
		btRefreshBehaviorList = (Button)findViewById(R.id.btRefreshRunningBehaviorList);
		btStopAllBehaviors = (Button)findViewById(R.id.btStopAllBehaviors);		
		lvRunningBehaviorList = (ListView)findViewById(R.id.lvRunningBehaviorList);
	}
}
