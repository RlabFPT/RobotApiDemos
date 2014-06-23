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
package com.fpt.robot.example.apis.motion;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;

import com.fpt.robot.Robot;
import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.motion.RobotMotionJointController;
import com.fpt.robot.motion.RobotMotionSelfCollisionProtection;
import com.fpt.robot.motion.RobotMotionStiffnessController;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * MotionJointAngleController class allows you to change angle of a joint.
 * @author Robot Team (FTI)
 *
 */
public class MotionJointsAngleController extends RobotApiDemoActivity {
	private static final String TAG = "MotionJointAngleController";
	private static final String INSTRUCTIONS = "MotionJointAngleController class allows you to change angle of a joint.";
	private Button angleInterP;
	private Button angleInterPWSP;
	private Button setStiffnessBt;
	private Button openHandBt;
	private Button closeHandBt;
	private Button changeAngleBt;
	private Button setAngleBt;
	

	private EditText jointName;
	private EditText angle;
	private EditText time;
	private EditText isAbsolute;
	private EditText fSpeed;
	
	private String jN;
	@SuppressWarnings("unused")
	private float[] currentAngle;
	private float anglevalue;
	private float timevalue;
	private float speedValue;
	boolean isAbsolutevalue;
	@SuppressWarnings("unused")
	private TextView angleValue;
	
	private Robot mRobot;
	
	private Timer timerHandlingUpdate;

	private boolean isStiffNessRunning=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joints_angle_controller);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		angleInterP = (Button) findViewById(R.id.btn_AngleInt);
		angleInterPWSP = (Button) findViewById(R.id.btn_AngleIntPWSP);
		setStiffnessBt= (Button) findViewById(R.id.btn_stiffness);
		closeHandBt= (Button) findViewById(R.id.btn_CloseHand);
		openHandBt= (Button) findViewById(R.id.btn_OpenHand);
		changeAngleBt= (Button) findViewById(R.id.btn_changeP);
		setAngleBt= (Button) findViewById(R.id.btn_setP);

		setAngleBt.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {					
					jN = jointName.getText().toString();
					anglevalue = Float.parseFloat(angle.getText().toString());
					speedValue= Float.parseFloat(fSpeed.getText().toString());
					
					final String[] jName = {jN,"HeadPitch"};//Get joint name from editText box of joint name
					final float[] al ={anglevalue,-0.5f};//Set joint angle to original point
									
					new Thread(new Runnable() {
					@Override
					  public void run() {
						try {
							// set angle for joint name		
							RobotMotionJointController.setAngles(mRobot, jName, al, speedValue);
									
							} catch (RobotException e) {
									e.printStackTrace();
								}
	
							}
						}).start();
				
				}
			});
	
		
		changeAngleBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				jN = jointName.getText().toString();
				anglevalue = Float.parseFloat(angle.getText().toString());
				speedValue= Float.parseFloat(fSpeed.getText().toString());
				
				final String[] jName = {jN, "HeadPitch"};//Get joint name from editText box of joint name
				final float[] al ={anglevalue,-0.2f};//Set joint angle to original point
								
				new Thread(new Runnable() {
				@Override
				  public void run() {
					try {
						// change angle of joint name		
						RobotMotionJointController.changeAngles(mRobot, jName, al, speedValue);
								
						} catch (RobotException e) {
								e.printStackTrace();
							}

						}
					}).start();
			
			}
		});

		openHandBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					@Override
					  public void run() {
						try {
							//robot open left hand		
							RobotMotionJointController.openHand(mRobot, "LHand");
						
							} catch (RobotException e) {
									e.printStackTrace();
								}

							}
						}).start();
				
			}
		});
		
		
		closeHandBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				new Thread(new Runnable() {
					@Override
					  public void run() {
						try {
							//Robot close left hand		
							RobotMotionJointController.closeHand(mRobot, "LHand");
						
							} catch (RobotException e) {
									e.printStackTrace();
								}

							}
						}).start();
				
			}
		});
		
		jointName = (EditText) findViewById(R.id.editJointNameAI);
		angle = (EditText) findViewById(R.id.editAngleAI);
		time = (EditText) findViewById(R.id.editTimeAI);
		isAbsolute = (EditText) findViewById(R.id.editIsAbsoluteAI);
		fSpeed	= (EditText) findViewById(R.id.editSpeed);
		
		angleValue = (TextView) findViewById(R.id.tvCurrentAngle);
		
		mRobot = getRobot();
		
		timerHandlingUpdate = new Timer();
		timerHandlingUpdate.schedule(new TimerTask() {
		public void run() {
			UpdateJointAngle();
			}

		}, 0, 1000);
		
		angleInterP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//status.setText("");
				
				jN = jointName.getText().toString();
				anglevalue = Float.parseFloat(angle.getText().toString());
				timevalue = Float.parseFloat(time.getText().toString());
				isAbsolutevalue = Boolean.parseBoolean(isAbsolute.getText().toString());
				
				final String[] jName = {jN, "HeadPitch"};//Get joint name from editText box of joint name 
				final float[][] al ={{anglevalue},{-anglevalue}};//get angle value from editText box of angle
				final float[][] t = {{timevalue},{timevalue+1.0f}};//Get duration form editText box of time
				final boolean iA = isAbsolutevalue;//Get coordinate from editText box of isAbsolute
				new Thread(new Runnable() {
				@Override
				  public void run() {
					try {
						//Set angle interpolation for a joint name
						RobotMotionJointController.angleInterpolation(mRobot, jName, al, t, iA);
					
						} catch (RobotException e) {
								e.printStackTrace();
							}

						}
					}).start();
			}

		});

		// ====================================================================================
		angleInterPWSP.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				//status.setText("");
				jN = jointName.getText().toString();
				anglevalue = Float.parseFloat(angle.getText().toString());
				speedValue= Float.parseFloat(fSpeed.getText().toString());		
				
				final String[] jName = {jN};//Get joint name from editText box of joint name
				final float[][] al ={{anglevalue}};//Set joint angle to original point
				final float sp=speedValue;
				new Thread(new Runnable() {
				@Override
				  public void run() {
					try {
						// set angle interpolation for a joint name with speed		
						RobotMotionJointController.angleInterpolationWithSpeed(mRobot, jName, al, sp);//(mRobot, jName, al, t, iA);
								
						} catch (RobotException e) {
								e.printStackTrace();
							}

						}
					}).start();
			}
		});
		
		setStiffnessBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				//handler.sendEmptyMessage(0);
				if(isStiffNessRunning==true){

					final Robot mRobot = getRobot();// get robot from robot service
					jN = jointName.getText().toString();
					final String[] jName = {jN};// Get joint name from editText box of joint name
					final float[] st ={1.0f};// Get stiffness from editText box
					
					new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									// Set motor on
									Log.i(getPackageName(),"stiffness on" );
									RobotMotionSelfCollisionProtection.setEnable(mRobot, "Arms", true);
									RobotMotionStiffnessController.setStiffnesses(mRobot, jName, st);
									} catch (RobotException e) {
									e.printStackTrace();
								}
							}
						}).start();
				}
				else {
					final Robot mRobot = getRobot();// get robot from robot service
					jN = jointName.getText().toString();
					final String[] jName = {jN};// Get joint name from editText box of joint name
					final float[] st ={0.0f};//
					
					new Thread(new Runnable() {
							@Override
							public void run() {
								try {
									//Set motor off
									Log.i(getPackageName()," stiffness off " );
									RobotMotionSelfCollisionProtection.setEnable(mRobot, "Arms", false);
									RobotMotionStiffnessController.setStiffnesses(mRobot, jName, st);
									} catch (RobotException e) {
									e.printStackTrace();
								}
							}
						}).start();					
				}
				if(isStiffNessRunning==false){
					isStiffNessRunning=true;
					setStiffnessBt.setText("Stiffness Off/On");
					setStiffnessBt.setBackgroundResource(R.drawable.color_green);
				}
				else {
					isStiffNessRunning=false;
					setStiffnessBt.setText("Stiffness On/Off");
					setStiffnessBt.setBackgroundResource(R.drawable.color_red);
				}
			}			
		});
		
	}

	
	private void UpdateJointAngle() {
		// This method is called directly by the timer
		// and runs in the same thread as the timer.

		// We call the method that will work with the UI
		// through the runOnUiThread method.
		this.runOnUiThread(Timer_Tick);
	}

	private Runnable Timer_Tick = new Runnable() {
		
		
		public void run() {

			// This method runs in the same thread as the UI.

			// Do something to the UI thread here
			//float av =currentAngle[0];
//			if (currentAngle != null) {
//				angleValue.setText(String.format("%.2f", currentAngle[0]));
//			}
//			
			new Thread(new Runnable() {
				
				@Override
				public synchronized void run() {
					updateAngle();
				}
			}).start();	
		}
	};
	private void updateAngle() {
			
		jN = jointName.getText().toString();
		
		final String[] jName = {jN};//Get joint name from editText box of joint name 
			try {
				// execute function
				currentAngle = RobotMotionJointController.getAngles(mRobot, jName, false);
	//			Log.i(getClass().getSimpleName(),
	//					"get current robot position " +currentRobotPosition.x+" "+currentRobotPosition.y);
				} catch (RobotException e) {
					e.printStackTrace();
			}
			
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
		return R.layout.activity_joints_angle_controller;
	}


	@Override
	protected void settingView() {
		
	}

}
