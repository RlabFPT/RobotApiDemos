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
package com.fpt.robot.example.apis.leds;

import android.os.Bundle;
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
import com.fpt.robot.leds.RobotLedEmotion;
/**
 * This class is used to play some predefined led emotions.
 * @author Robot Team (FTI)
 */
public class LedEmotions extends RobotApiDemoActivity {
	private final static String TAG="LedEmotions";
	private final static String INSTRUCTIONS = "This class is used to play some predefined led emotions. " +
			"Select emotion name on spinner and click Start or Stop button";
	// button start led emotion
	private Button btLedEmotionStart;
	// button stop led emotion
	private Button btLedEmotionStop;
	// spinner list led emotions name
	private Spinner spLedEmotionList;

	// predefined led emotions 
	private String[] emotionList = {
		RobotLedEmotion.EMOTION_ANGRY,
		RobotLedEmotion.EMOTION_ANNOYED,
		RobotLedEmotion.EMOTION_ANXIOUS,
		RobotLedEmotion.EMOTION_ATTENTION,
		RobotLedEmotion.EMOTION_BORED,
		RobotLedEmotion.EMOTION_CAUTIOUS,
		RobotLedEmotion.EMOTION_CONFIDENT,
		RobotLedEmotion.EMOTION_CONFUSED,
		RobotLedEmotion.EMOTION_DERTEMINED,
		RobotLedEmotion.EMOTION_DISAPPOINT,
		RobotLedEmotion.EMOTION_ENTHUSIASTIC,
		RobotLedEmotion.EMOTION_EXCITED,
		RobotLedEmotion.EMOTION_EXHAUSTED,
		RobotLedEmotion.EMOTION_FEAR,
		RobotLedEmotion.EMOTION_FRUSTRATED,
		RobotLedEmotion.EMOTION_HAPPY,
		RobotLedEmotion.EMOTION_HEY,
		RobotLedEmotion.EMOTION_HUMILLIATED,
		RobotLedEmotion.EMOTION_HURT,
		RobotLedEmotion.EMOTION_INNOCENT,
		RobotLedEmotion.EMOTION_INTERESTED,
		RobotLedEmotion.EMOTION_LATE,
		RobotLedEmotion.EMOTION_LAUGH,
		RobotLedEmotion.EMOTION_LAUGH2,
		RobotLedEmotion.EMOTION_LONELY,
		RobotLedEmotion.EMOTION_OPTIMISTIC,
		RobotLedEmotion.EMOTION_PROUD,
		RobotLedEmotion.EMOTION_RELIEVED,
		RobotLedEmotion.EMOTION_SAD,
		RobotLedEmotion.EMOTION_SHOCKED,
		RobotLedEmotion.EMOTION_SHY,
		RobotLedEmotion.EMOTION_SURE,
		RobotLedEmotion.EMOTION_SURPRISE,
		RobotLedEmotion.EMOTION_SUSPICIOUS,
		RobotLedEmotion.EMOTION_WINNER,
	};

	private ArrayAdapter<String> ledEmotionListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		btLedEmotionStart = (Button) findViewById(R.id.btLedEmotionStart);
		// set event click for button start led emotion
		btLedEmotionStart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startEmotion();
			}
		});
		
		btLedEmotionStop = (Button) findViewById(R.id.btLedEmotionStop);
		// set event click for button stop led emotion
		btLedEmotionStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				stopEmotion();
			}
		});
		
		spLedEmotionList = (Spinner) findViewById(R.id.spLedEmotionList);
		ledEmotionListAdapter = new ArrayAdapter<String>(
        		this, android.R.layout.simple_spinner_item);
		ledEmotionListAdapter.setDropDownViewResource(
				android.R.layout.simple_spinner_dropdown_item);
		ledEmotionListAdapter.setNotifyOnChange(true);
		for (int i = 0; i < emotionList.length; i++) {
			ledEmotionListAdapter.add(emotionList[i]);
		}
		// set list predefined emotions to adapter
		spLedEmotionList.setAdapter(ledEmotionListAdapter);
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
		return R.layout.activity_led_emotions;
	}

	/**
	 * Start led emotion
	 */
	protected void startEmotion() {
		// get name of emotion from spinner
		final String ledEmotion = spLedEmotionList.getSelectedItem().toString();
		// if led name is invalid
		if (ledEmotion == null || ledEmotion.isEmpty()) {
			showInfoDialog("Start Emotion", "Invalid led emotion!");
			return;
		}
		new Thread(new Runnable() {	
			@Override
			public void run() {
				boolean result = false;
				showProgress("starting emotion [" + ledEmotion + "]...");
				try {
					// run emotion with name
					result = RobotLedEmotion.startEmotion(getRobot(), ledEmotion);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("start emotion '" + ledEmotion + "' failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("start emotion '" + ledEmotion + "' successful!");
				} else {
					makeToast("start emotion '" + ledEmotion + "' failed!");
				}
			}
		}).start();
	}

	/**
	 * Stop led emotion
	 */
	protected void stopEmotion() {
		new Thread(new Runnable() {	
			@Override
			public void run() {
				boolean result = false;
				showProgress("stopping current emotion...");
				try {
					// stop emotion
					result = RobotLedEmotion.stopEmotion(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("stop emotion failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (result) {
					makeToast("start emotion successful!");
				} else {
					makeToast("start emotion failed!");
				}
			}
		}).start();
	}
}
