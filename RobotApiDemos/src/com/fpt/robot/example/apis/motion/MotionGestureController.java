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
package com.fpt.robot.example.apis.motion;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.RobotHardware;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.leds.RobotLedEmotion;
import com.fpt.robot.motion.RobotGesture;
import com.fpt.robot.motion.RobotMotionAction;
import com.fpt.robot.motion.RobotMotionStiffnessController;
import com.fpt.robot.motion.RobotPosture;

/**
 * This class is used to run some gesture on robot
 * 
 * @author Robot Team (FTI)
 */
public class MotionGestureController extends RobotApiDemoActivity {
	private static final String TAG = "MotionGestureController";
	private static final String INSTRUCTIONS = "This class is used  to run some gesture of robot. "
			+ "Click \"Get List\" to get list gesture and list led in robot. Check enable led or not to run led while gesutre is running. "
			+ "After select gesture name ( and led name), click \"Run\" to run gesture.";
	// button get gesture list
	private Button btGetListGestureAndLed;
	// button run gesture
	private Button btRun;
	// button stand up
	private Button btStandUp;
	// button sit down
	private Button btCrouch;
	// list view gesture names
	private ListView lvGestures;
	// list view led names
	private ListView lvLedEmotions;

	// gesture name to run
	private EditText gName;
	// led name to run
	private EditText lName;
	// mode enable led or not
	private CheckBox cbEnableLed;
	// Robot object
	private Robot mRobot;

	private String gestureName;
	private String ledName;
	// gesture names
	private String[] gestureList;
	// led animations name
	private String[] getAnimationList = { "Angry", "Annoyed", "Anxious",
			"Attention", "Bored", "Cautious", "Confident", "Confused",
			"Determined", "Disappoint", "Enthusiastic", "Excited", "Exhausted",
			"Fear", "Frustrated", "Happy", "Hey", "Humilliated", "Hurt",
			"Innocent", "Interested", "Late", "Laugh", "Laugh2", "Lonely",
			"Optimistic", "Proud", "Relieved", "Sad", "Shocked", "Shy", "Sure",
			"Surprise", "Suspicious", "Winner" };

	// adapter gesture name and led name
	private ArrayList<String> lvGestureData, lvLedsData;
	private ArrayAdapter<String> adapterGestureList, adapterLedsList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		btGetListGestureAndLed = (Button) findViewById(R.id.btn_getGestureList);
		btRun = (Button) findViewById(R.id.btn_RunGesture);
		btCrouch = (Button) findViewById(R.id.btn_sitDown);
		btStandUp = (Button) findViewById(R.id.btn_standUp);

		lvGestures = (ListView) findViewById(R.id.lvGestures);
		lvLedEmotions = (ListView) findViewById(R.id.lvLeds);
		TextView tv = (TextView) findViewById(android.R.id.empty);
		lvGestures.setEmptyView(tv);

		gName = (EditText) findViewById(R.id.editGestureName);
		lName = (EditText) findViewById(R.id.editLedsName);

		mRobot = getRobot();

		// set led name to edit text when select a name from list view led name
		lvLedEmotions.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				lName.setText(getAnimationList[position]);
			}
		});

		lvLedEmotions.setEnabled(false);
		// set gesture name to edit text when select a name from list view
		// gesture name
		lvGestures.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				gName.setText(gestureList[arg2]);

			}
		});

		cbEnableLed = (CheckBox) findViewById(R.id.cbEnableLeds);
		// select enable led or not
		cbEnableLed.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (cbEnableLed.isChecked()) {
					lvLedEmotions.setEnabled(true);
				} else {
					lvLedEmotions.setEnabled(false);
				}
			}
		});

		// set event click for button run gesture
		btRun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showAlertDialog(TAG, "Are you sure run this gesture?", null,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// get gesture name and led name from edit text
								gestureName = gName.getText().toString();
								ledName = lName.getText().toString();
								new Thread(new Runnable() {
									@Override
									public synchronized void run() {
										try {
											if (cbEnableLed.isChecked()) {
												// start led while gesture is
												// running
												RobotLedEmotion.startEmotion(
														mRobot, ledName);
											}
											// run gesture
											Log.i(getClass().getName(),
													"Start gesture");
											RobotGesture.runGesture(mRobot,
													gestureName);
											if (cbEnableLed.isChecked()) {
												// stop emotion after runGesture
												// finish
												RobotLedEmotion
														.stopEmotion(mRobot);
											}
										} catch (RobotException e) {
											e.printStackTrace();
										}
									}
								}).start();
							}
						}, null);

			}
		});
		// set event click for button get list gesture and led
		btGetListGestureAndLed.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				new Thread(new Runnable() {
					@Override
					public synchronized void run() {
						try {
							Log.i(getClass().getName(), "Start standing up");
							// get list gesture in robot							
							gestureList = RobotGesture.getGestureList(mRobot);

							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (gestureList != null) {
										lvGestureData = new ArrayList<String>();
										// add name of gestures to gesture list data
										for (int count = 0; count < gestureList.length; count++) {
											lvGestureData.add(String
													.valueOf(count
															+ "."
															+ gestureList[count]));
										}
										// set list gesture to spinner
										adapterGestureList = new ArrayAdapter<String>(
												getApplicationContext(),
												android.R.layout.simple_list_item_1,
												lvGestureData);
										lvGestures
												.setAdapter(adapterGestureList);
									}

								}
							});
							runOnUiThread(new Runnable() {

								@Override
								public void run() {
									if (gestureList != null) {
										lvLedsData = new ArrayList<String>();
										// add name of leds to led list data
										for (int count1 = 0; count1 < getAnimationList.length; count1++) {
											lvLedsData.add(String
													.valueOf(count1
															+ "."
															+ getAnimationList[count1]));
										}
										// set list emotion led to spinner
										adapterLedsList = new ArrayAdapter<String>(
												getApplicationContext(),
												android.R.layout.simple_list_item_1,
												lvLedsData);
										lvLedEmotions
												.setAdapter(adapterLedsList);
									}

								}
							});
						} catch (RobotException e) {
							e.printStackTrace();
						}

					}
				}).start();
			}
		});

		// set event click for button stand up
		btStandUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				showAlertDialog(TAG, "Are you sure run stand up?", null,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								new Thread(new Runnable() {
									@Override
									public synchronized void run() {
										try {
											// Robot stand up
											Log.i(getClass().getName(), "Start standing up");
											RobotMotionAction.standUp(mRobot, 0.5f);
										} catch (RobotException e) {
											e.printStackTrace();
										}

									}
								}).start();
							}
						}, null);				
			}
		});

		// set event click for button crouch
		btCrouch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				final String[] jName = { "Body" };// Get joint name from
													// editText box of joint
													// name
				final float[] st = { 0.0f };// Get stiffness from editText box
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Log.i(getClass().getName(), "Start crouch");
							// robot go to crouch
							RobotPosture.goToPosture(getRobot(),
									RobotHardware.Posture.CROUCH, 0.5f);
							// set motor off
							RobotMotionStiffnessController.setStiffnesses(
									mRobot, jName, st);

						} catch (RobotException e) {
							e.printStackTrace();
						}

					}
				}).start();
			}
		});
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
		return R.layout.activity_gesture_motion;
	}

	@Override
	protected void settingView() {

	}

}
