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
package com.fpt.robot.example.apis.audio;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.tts.RobotTextToSpeech;

/**
 * This class is used to enter text and robot will speech that text.
 * @author Robot Team (FTI)
 */

public class TextToSpeech extends RobotApiDemoActivity implements
		OnClickListener, OnCheckedChangeListener {
	private static final String TAG = "TextToSpeech";
	private static final String INSTRUCTIONS = "This class is used to enter text and robot will speech that text. " +
			"You need choose language for robot speak. With each language, enter correctly text." +
			"If you check \"Say stoppable\" you can stop robot speaking while robot is speaking, otherwise " +
			"you cannot stop. After done setting, click \"Say\" button";
	// Button to say
	private Button btSay;
	// Button to sotp say
	private Button btShutup;
	// Editext to enter text to say
	private EditText etSay;
	// Checkbox to enable say stoppable
	private CheckBox cbSayStoppable;
	// Group radio to choose language say
	private RadioGroup rgLanguageSelection;
	// Radio language English
	private RadioButton rdEnglish;
	// Radio language Vietnam
	private RadioButton rdVietnamese;
	// Radio language France
	private RadioButton rdFrench;
	// Radio language Japanese
	private RadioButton rdJapanese;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		btSay = (Button) findViewById(R.id.btSay);
		// set event click
		btSay.setOnClickListener(this);
		btShutup = (Button) findViewById(R.id.btShutup);
		// set event click
		btShutup.setOnClickListener(this);

		etSay = (EditText) findViewById(R.id.etSay);

		cbSayStoppable = (CheckBox) findViewById(R.id.cbSayStoppable);
		// set event click
		cbSayStoppable.setOnClickListener(this);

		rgLanguageSelection = (RadioGroup) findViewById(R.id.rgLanguageSelection);
		rdEnglish = (RadioButton) findViewById(R.id.rdEnglish);
		rdVietnamese = (RadioButton) findViewById(R.id.rdVietnamese);
		rdFrench = (RadioButton) findViewById(R.id.rdFrench);
		rdJapanese = (RadioButton) findViewById(R.id.rdJapanese);
		// set event check change
		rgLanguageSelection.setOnCheckedChangeListener(this);

		
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
		return R.layout.activity_text_to_speech;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btSay) {
			say();
		} else if (v.getId() == R.id.btShutup) {
			shutup();
		} else if (v.getId() == R.id.cbSayStoppable) {
			// check if say stoppable
			if (cbSayStoppable.isChecked()) {
				btShutup.setEnabled(true);
			} else {
				btShutup.setEnabled(false);
			}
		}
	}

	/**
	 * Speak from text on robot
	 */
	private void say() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// get text from edittext
				String textToSay = etSay.getText().toString();
				if (textToSay.isEmpty()) {
					makeToast("nothing to say!");
					return;
				}
				// Language to say
				String language = RobotTextToSpeech.ROBOT_TTS_LANG_EN;				
				boolean result = false;
				if (rdVietnamese.isChecked()) {
					language = RobotTextToSpeech.ROBOT_TTS_LANG_VI;
				} else if (rdFrench.isChecked()) {
					language = RobotTextToSpeech.ROBOT_TTS_LANG_FR;
				} else if (rdJapanese.isChecked()) {
					language = "Japanese";
				} else if (rdEnglish.isChecked()) {
					language = RobotTextToSpeech.ROBOT_TTS_LANG_EN;
				}

				

				showProgress("saying...");
				try {				
					// start speak from text, check if say stoppable
					if (!cbSayStoppable.isChecked()) {
						// say and cannot stop
						result = RobotTextToSpeech.say(getRobot(), textToSay,
								language);
					} else {
						// say and can stop
						result = RobotTextToSpeech.sayStoppable(getRobot(),
								textToSay, language);
					}
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("say text failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("say text failed!");
				}
				Log.d(TAG, "say('" + textToSay + "'): result=" + result);
			}
		}).start();
	}

	/**
	 * Stop speaking if can
	 */
	private void shutup() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("stopping...");
				boolean result = false;
				try {
					// stop speaking
					result = RobotTextToSpeech.shutUp(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("stop failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("stop failed!");
				}
			}
		}).start();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.rdVietnamese) {
			etSay.setText("xin chào!");
		} else if (checkedId == R.id.rdFrench) {
			etSay.setText("bonjour!");
		} else if (checkedId == R.id.rdEnglish) {
			etSay.setText("hello!");
		} else if (checkedId == R.id.rdJapanese) {
			etSay.setText("こんにちは。");
		}
	}
}
