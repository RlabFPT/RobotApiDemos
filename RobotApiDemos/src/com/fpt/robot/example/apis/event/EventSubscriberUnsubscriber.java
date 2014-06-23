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
package com.fpt.robot.example.apis.event;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.fpt.robot.RobotException;
import com.fpt.robot.binder.RobotValue;
import com.fpt.robot.event.RobotEvent;
import com.fpt.robot.event.RobotEventSubscriber;
import com.fpt.robot.event.RobotEventSubscriber.OnRobotEventListener;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
/**
 * Class to subscriber and unsubscriber robot event
 * @author Robot Team (FTI)
 *
 */
public class EventSubscriberUnsubscriber extends RobotApiDemoActivity 
		implements OnRobotEventListener {
	private static final String TAG="EventSubscriberUnsubscriber";
	private static final String INSTRUCTIONS = "You can subscibe an unsubscribe selected event on robot. " +
			"Choose event name in spinner and click Subscribe, Unsubscribe";
	// Button to subscribe event
	private Button btSubscribeEvent;
	// Button to unsubscribe event
	private Button btUnsubscribeEvent;
	// Spinner contain list event
	private Spinner spEvents = null;
	// Text view log
	private TextView tvEventLog;	
    private ScrollView svEventLog;
	// RobotEventSubscriber object 
	private RobotEventSubscriber mEventSubscriber = null;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		btSubscribeEvent = (Button)findViewById(R.id.btSubscribeEvent);
        btSubscribeEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// get event name from spinner
				String eventName = spEvents.getSelectedItem().toString();
				// subscribe selected event
				subscribeEvent(eventName);
			}
		});
        
        btUnsubscribeEvent = (Button)findViewById(R.id.btUnsubscribeEvent);
        btUnsubscribeEvent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// get event name from spinner
				String eventName = spEvents.getSelectedItem().toString();
				// unsubscribe selected event
				unsubscribeEvent(eventName);
			}
		});
        
        tvEventLog = (TextView)findViewById(R.id.tvEventLog);
        svEventLog = (ScrollView)findViewById(R.id.svEventLog);
        
        spEvents = (Spinner)findViewById(R.id.spEvents);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
        		this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        /**
         * Sensors
         * */
        adapter.add(RobotEvent.RIGHT_BUMPER_PRESSED);
        adapter.add(RobotEvent.LEFT_BUMPER_PRESSED);
        adapter.add(RobotEvent.CHEST_BUTTON_PRESSED);
        adapter.add(RobotEvent.FRONT_TACTIL_TOUCHED);
        adapter.add(RobotEvent.MIDDLE_TACTIL_TOUCHED);
        adapter.add(RobotEvent.REAR_TACTIL_TOUCHED);
        adapter.add(RobotEvent.HAND_RIGHT_BACK_TOUCHED);
        adapter.add(RobotEvent.HAND_RIGHT_LEFT_TOUCHED);
        adapter.add(RobotEvent.HAND_RIGHT_RIGHT_TOUCHED);
        adapter.add(RobotEvent.HAND_LEFT_BACK_TOUCHED);
        adapter.add(RobotEvent.HAND_LEFT_LEFT_TOUCHED);
        adapter.add(RobotEvent.HAND_LEFT_RIGHT_TOUCHED);
        adapter.add(RobotEvent.BODY_STIFFNESS_CHANGED);
        adapter.add(RobotEvent.HOT_JOINT_DETECTED);
        adapter.add(RobotEvent.SIMPLE_CLICK_OCCURED);
        adapter.add(RobotEvent.DOUBLE_CLICK_OCCURED);
        adapter.add(RobotEvent.TRIPLE_CLICK_OCCURED);
        /**
         * Robot Pose
         * */
        adapter.add(RobotEvent.ROBOT_POSE_CHANGED);
        /**
         * Fall Manager
         * */
        adapter.add(RobotEvent.ROBOT_HAS_FALLEN);
        /**
         * FSR
         * */
        adapter.add(RobotEvent.FOOT_CONTACT_CHANGED);
        /**
         * Battery
         * */
        adapter.add(RobotEvent.BATTERY_POWER_PLUGGED_CHANGED);
        adapter.add(RobotEvent.BATTERY_CHARGE_CELL_VOLTAGE_MIN_CHANGED);
        adapter.add(RobotEvent.BATTERY_CHARGING_FLAG_CHANGED);
        adapter.add(RobotEvent.BATTERY_FULL_CHARGED_FLAG_CHANGED);
        adapter.add(RobotEvent.BATTERY_DISCHARGING_FLAG_CHANGED);
        adapter.add(RobotEvent.BATTERY_CHARGE_CHANGED);
        /**
         * Visual Compass
         * */
        adapter.add(RobotEvent.VISUAL_COMPASS_DEVIATION);
        adapter.add(RobotEvent.VISUAL_COMPASS_MATCH);
        /**
         * Vision
         * */
        adapter.add(RobotEvent.VISION_PICTURE_DETECTED);
        adapter.add(RobotEvent.VISION_RED_BALL_DETECTED);
        adapter.add(RobotEvent.VISION_MOVEMENT_DETECTED);
        adapter.add(RobotEvent.VISION_LANDMARK_DETECTED);
        adapter.add(RobotEvent.VISION_FACE_DETECTED);
        adapter.add(RobotEvent.VISION_DARKNESS_DETECTED);
        adapter.add(RobotEvent.VISION_BACKLIGHTING_DETECTED);
        /**
         * Text To Speech
         * */
        adapter.add(RobotEvent.TTS_CURRENT_BOOKMARK);
        adapter.add(RobotEvent.TTS_CURRENT_SENTENCE);
        adapter.add(RobotEvent.TTS_CURRENT_WORD);
        adapter.add(RobotEvent.TTS_POSITION_OF_CURRENT_WORD);
        adapter.add(RobotEvent.TTS_TEXT_STARTED);
        adapter.add(RobotEvent.TTS_TEXT_DONE);
        /**
         * Speech Recognition
         * */
        adapter.add(RobotEvent.SPEECH_WORD_RECOGNIZED);
        adapter.add(RobotEvent.SPEECH_LAST_WORD_RECOGNIZED);
        adapter.add(RobotEvent.SPEECH_DETECTED);
        /**
         * Sound Detection & Localization
         * */
        adapter.add(RobotEvent.AUDIO_SOUND_DETECTED);
        adapter.add(RobotEvent.AUDIO_SOUND_LOCATED);
        /**
         * Sonar
         * */
        adapter.add(RobotEvent.SONAR_LEFT_DETECTED);
        adapter.add(RobotEvent.SONAR_RIGHT_DETECTED);
        adapter.add(RobotEvent.SONAR_LEFT_NOTHING_DETECTED);
        adapter.add(RobotEvent.SONAR_RIGHT_NOTHING_DETECTED);
        // set adapter which is event names for spinner
        spEvents.setAdapter(adapter);
        // initialize RobotEventSubscriber
        mEventSubscriber = new RobotEventSubscriber(getRobot());
        mEventSubscriber.setOnRobotEventListener(this);
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
		return R.layout.activity_event_subscriber_unsubscriber;
	}

	/**
	 * Subscribe event with name
	 * @param eventName
	 */
	protected void subscribeEvent(final String eventName) {
		Log.d(TAG, "subscribeEvent('" + eventName + "')");
		new Thread(new Runnable() {			
			@Override
			public void run() {
				if (getRobot() == null) {
					return;
				}
				boolean result;
				try {
					// start subscribe
					result = mEventSubscriber.subscribe(eventName, RobotEvent.TYPE_SYSTEM);
				} catch (final RobotException e) {
					e.printStackTrace();
					makeToast("subscribe event" + eventName + " failed! " + e.getMessage());
					return;
				}
				if (result) {
					makeToast("subscribe event '" + eventName + "' sucessfully!");
				} else {
					makeToast("subscribe event '" + eventName + "' failed!");
				}
			}
		}).start();
	}

	/**
	 * Unsubscribe event with name
	 * @param eventName
	 */
	protected void unsubscribeEvent(final String eventName) {
		Log.d(TAG, "subscribeEvent('" + eventName + "')");
		new Thread(new Runnable() {			
			@Override
			public void run() {
				if (getRobot() == null) {
					return;
				}
				boolean result = false;
				try {
					// start unsubscribe
					result = mEventSubscriber.unsubscribe(eventName, RobotEvent.TYPE_SYSTEM);
				} catch (final RobotException e) {
					e.printStackTrace();
					makeToast("unsubscribe event" + eventName + " failed! " + e.getMessage());
					return;
				}
				if (result) {
					makeToast("unsubscribe event '" + eventName + "' sucessfully!");
				} else {
					makeToast("unsubscribe event '" + eventName + "' failed!");
				}
			}
		}).start();
	}
	
    @SuppressLint("HandlerLeak")
	class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_ADD_LOG:
            {
            	Log.d(TAG, "MSG_ADD_LOG");
                String oldLog = tvEventLog.getText().toString();
                tvEventLog.setText(oldLog + "\n" + (String)msg.obj);
                svEventLog.smoothScrollTo(0, tvEventLog.getBottom()-1);
                break;
            }
            default:
                super.handleMessage(msg);
            }
        }
    }

    final Messenger mMessenger = new Messenger(new IncomingHandler());

	static final int MSG_ADD_LOG = 1;
	
    private void addLog(String log) {
        Log.d(TAG, log);
        try {
            Message msg = Message.obtain(null, MSG_ADD_LOG, log);
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

	@Override
	public void onRobotEvent(String event, RobotValue data) {
		Log.d(TAG, "onRobotEvent('" + event + "')");
		addLog("got event: " + event);
		Log.d(TAG, "data: " + data.toString());
	}

	@Override
	protected void settingView() {
		// TODO Auto-generated method stub
		
	}
}
