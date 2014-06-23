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
package com.fpt.robot.example.apis.infrared;

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
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.infrared.RobotInfrared;
/**
 * RemoteControl which is use IR of robot to control device
 * For example TV Sharp
 * @author Robot Team (FTI)
 */
public class RemoteControl extends RobotApiDemoActivity implements OnClickListener {
	private static final String TAG = "RemoteControl";
	private static final String INSTRUCTIONS = "Remote control class allow you to use IR of Robot " +
			"to control some devices. This example, we use IR to control TVSharp (code of TVSharp have " +
			"saved on robot";
	
	private static String selectedRemoteName = "SharpTv";

	private Spinner spRemoteControlList;
	/*List button of remote control*/
	private Button btIRPower;
	
	private Button btIRVolUp;	
	private Button btIRVolDown;	
	private Button btIRMute;
	
	private Button btIRChannelUp;
	private Button btIRChannelDown;
	
	private Button btIRKey0;
	private Button btIRKey1;
	private Button btIRKey2;
	private Button btIRKey3;
	private Button btIRKey4;
	private Button btIRKey5;
	private Button btIRKey6;
	private Button btIRKey7;
	private Button btIRKey8;
	private Button btIRKey9;
	
	private Button btIRKeyAVPlus;
	private Button btIRKeyAVMinus;
	
	private Button btIRTestKey;
	
	// Key name is defined on robot
	private static String KEY_POWER="POWER";
	private static String KEY_VOLUME_UP="VOLUME+";
	private static String KEY_VOLUME_DOWN="VOLUME-";
	private static String KEY_MUTE="MUTE";
	private static String KEY_CHANNEL_UP="CHANNEL+";
	private static String KEY_CHANNEL_DOWN="CHANNEL-";
	private static String KEY_0="0";
	private static String KEY_1="1";
	private static String KEY_2="2";
	private static String KEY_3="3";
	private static String KEY_4="4";
	private static String KEY_5="5";
	private static String KEY_6="6";
	private static String KEY_7="7";
	private static String KEY_8="8";
	private static String KEY_9="9";
	
	private static String KEY_AVPLUS = "AV+";
	private static String KEY_AVMINUS = "AV-";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		spRemoteControlList = (Spinner)findViewById(R.id.spRemoteControlList);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
        		this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter.add("SharpTv");
        spRemoteControlList.setAdapter(adapter);
        spRemoteControlList.setSelection(0);
		
        btIRPower = (Button)findViewById(R.id.btIRPower);
        btIRPower.setOnClickListener(this);
        
        btIRVolUp = (Button)findViewById(R.id.btIRVolUp);
        btIRVolUp.setOnClickListener(this);
        btIRVolDown = (Button)findViewById(R.id.btIRVolDown);
        btIRVolDown.setOnClickListener(this);
        btIRMute = (Button)findViewById(R.id.btIRMute);
        btIRMute.setOnClickListener(this);
        
        btIRChannelUp = (Button)findViewById(R.id.btIRChannelUp);
        btIRChannelUp.setOnClickListener(this);
        btIRChannelDown = (Button)findViewById(R.id.btIRChannelDown);
        btIRChannelDown.setOnClickListener(this);
        
        btIRKey0 = (Button)findViewById(R.id.btIRKey0);
        btIRKey0.setOnClickListener(this);
        btIRKey1 = (Button)findViewById(R.id.btIRKey1);
        btIRKey1.setOnClickListener(this);
        btIRKey2 = (Button)findViewById(R.id.btIRKey2);
        btIRKey2.setOnClickListener(this);
        btIRKey3 = (Button)findViewById(R.id.btIRKey3);
        btIRKey3.setOnClickListener(this);
        btIRKey4 = (Button)findViewById(R.id.btIRKey4);
        btIRKey4.setOnClickListener(this);
        btIRKey5 = (Button)findViewById(R.id.btIRKey5);
        btIRKey5.setOnClickListener(this);
        btIRKey6 = (Button)findViewById(R.id.btIRKey6);
        btIRKey6.setOnClickListener(this);
        btIRKey7 = (Button)findViewById(R.id.btIRKey7);
        btIRKey7.setOnClickListener(this);
        btIRKey8 = (Button)findViewById(R.id.btIRKey8);
        btIRKey8.setOnClickListener(this);
        btIRKey9 = (Button)findViewById(R.id.btIRKey9);
        btIRKey9.setOnClickListener(this);
        
        btIRKeyAVPlus = (Button)findViewById(R.id.btIRKeyAVPlus);
        btIRKeyAVPlus.setOnClickListener(this);
        btIRKeyAVMinus = (Button)findViewById(R.id.btIRKeyAVMinus);
        btIRKeyAVMinus.setOnClickListener(this);
        
        btIRTestKey = (Button)findViewById(R.id.btIRTestKey);
        btIRTestKey.setOnClickListener(this);
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
		return R.layout.activity_remote_control;
	}

	@Override
	public void onClick(View v) {
		selectedRemoteName = spRemoteControlList.getSelectedItem().toString();
		Log.d(TAG, "selected remote name: '" + selectedRemoteName + "'");
		switch(v.getId()) {
		case R.id.btIRPower:
			sendRemoteKey(selectedRemoteName, KEY_POWER);
			break;
		case R.id.btIRVolUp:
			sendRemoteKey(selectedRemoteName, KEY_VOLUME_UP);
			break;
		case R.id.btIRVolDown:
			sendRemoteKey(selectedRemoteName, KEY_VOLUME_DOWN);
			break;
		case R.id.btIRMute:
			sendRemoteKey(selectedRemoteName, KEY_MUTE);
			break;
		case R.id.btIRChannelUp:
			sendRemoteKey(selectedRemoteName, KEY_CHANNEL_UP);
			break;
		case R.id.btIRChannelDown:
			sendRemoteKey(selectedRemoteName, KEY_CHANNEL_DOWN);
			break;
		case R.id.btIRKey0:
			sendRemoteKey(selectedRemoteName, KEY_0);
			break;
		case R.id.btIRKey1:
			sendRemoteKey(selectedRemoteName, KEY_1);
			break;
		case R.id.btIRKey2:
			sendRemoteKey(selectedRemoteName, KEY_2);
			break;
		case R.id.btIRKey3:
			sendRemoteKey(selectedRemoteName, KEY_3);
			break;
		case R.id.btIRKey4:
			sendRemoteKey(selectedRemoteName, KEY_4);
			break;
		case R.id.btIRKey5:
			sendRemoteKey(selectedRemoteName, KEY_5);
			break;
		case R.id.btIRKey6:
			sendRemoteKey(selectedRemoteName, KEY_6);
			break;
		case R.id.btIRKey7:
			sendRemoteKey(selectedRemoteName, KEY_7);
			break;
		case R.id.btIRKey8:
			sendRemoteKey(selectedRemoteName, KEY_8);
			break;
		case R.id.btIRKey9:
			sendRemoteKey(selectedRemoteName, KEY_9);
			break;
		case R.id.btIRKeyAVPlus:
			sendRemoteKey(selectedRemoteName, KEY_AVPLUS);
			break;
		case R.id.btIRKeyAVMinus:
			sendRemoteKey(selectedRemoteName, KEY_AVMINUS);
			break;
		case R.id.btIRTestKey:
			testKey();
			break;
		}
	}

	private void testKey() {
		selectedRemoteName = spRemoteControlList.getSelectedItem().toString();
		Log.d(TAG, "selected remote name: '" + selectedRemoteName + "'");
		sendRemoteKey(selectedRemoteName, KEY_AVPLUS);
	}


	/**
	 * Send key to robot through IR
	 * @param remoteName
	 * @param keyName
	 */
	private void sendRemoteKey(final String remoteName, final String keyName) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				showProgress("sending remote key...");
				try {
					result = RobotInfrared.sendRemoteKeyWithTime(getRobot(), 
							remoteName, keyName, 200);
					//result = RobotInfrared.sendRemoteKey(getRobot(), remoteName, keyName);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					showInfoDialogFromThread("Remote control", 
							"send remote key failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("send remote key failed!");
				}
			}
		}).start();
	}
}
