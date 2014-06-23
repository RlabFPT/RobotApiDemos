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
import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.stt.RobotSpeechRecognition;
import com.fpt.robot.stt.RobotSpeechRecognition.RecognizedPhrase;
import com.fpt.robot.stt.RobotSpeechRecognition.WordRecognizedInfo;

import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class SpeechRecognition extends RobotApiDemoActivity implements
		RobotSpeechRecognition.Listener, OnClickListener {

	private final String TAG = "SPeechRecognition";
	private final String INSTRUCTIONS = "This class allow you to use speech recognition engine of robot. "
			+ "First, you need choose language and set vocabularies for recognition (Each vocabulary is separated by '\\n') "
			+ "If you choose enable sound, robot will play \"Beep\" when start recognition. "
			+ "If you choose enable led, robot will run led while recognition. "
			+ "After done setting, click \"Start Recognition\" to start and click \"Stop Recognitino\" to stop";
	// SpeechMonitor to monitoring speech recognition
	private RobotSpeechRecognition.Monitor speechMonitor;

	// Button start recognition
	private Button btStartRecognition;
	// Button stop recognition
	private Button btStopRecognition;
	// Button set vocabulary for recognition
	private Button btSetVocab;

	// Spinner available languages
	private Spinner spLanguages;

	// Checkbox to enable playing audio when start recognition
	private CheckBox cbEnableAudio;
	// Checkbox to enable led while recognition
	private CheckBox cbEnableLeds;

	// Editext to enter vocabulary for recognition
	private EditText edVocab;

	// Text view to display result after recognition
	private TextView tvResult;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// create new speech monitor
		speechMonitor = new RobotSpeechRecognition.Monitor(getRobot());
		// set listener for speech monitor
		speechMonitor.setListener(this);
		// set event click for buttons
		btSetVocab.setOnClickListener(this);
		btStartRecognition.setOnClickListener(this);
		btStopRecognition.setOnClickListener(this);
		// set languages
		setLanguagesAdapter();
	}

	/**
	 * set available languages to adapter
	 */
	private void setLanguagesAdapter() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				showProgress("initial...");
				try {
					// get languages which is available in robot speech
					// recognition
					final String[] avaiLangs = RobotSpeechRecognition
							.getAvailableLanguages(getRobot());
					// set vocabulary for robot speech recognition
					RobotSpeechRecognition.setWordListAsVocabulary(getRobot(),
							new String[] { "Hi", "Hello", "How are you" });
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							// set default vocabularies in edit text
							edVocab.setText("Hi \nHello \nHow are you");
							// set array languages to adapter
							spLanguages
									.setAdapter(new ArrayAdapter<String>(
											SpeechRecognition.this,
											android.R.layout.simple_spinner_dropdown_item,
											avaiLangs));
							// set default selection
							spLanguages.setSelection(0);
						}
					});
				} catch (final RobotException e) {
					cancelProgress();
					showInfoDialogFromThread("Speech Recognition",
							"Initial failed: " + e.getMessage());
					e.printStackTrace();
					return;
				}
				cancelProgress();
			}
		}).start();

	}

	/**
	 * Set vocabularies for robot speech recognition
	 */
	private void setVocabulary() {
		// get vocabularies from edittext
		final String vocabs = edVocab.getText().toString();
		if (vocabs == null || TextUtils.isEmpty(vocabs)) {
			showInfoDialog("Speech Recognition", "Vocabs is empty");
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				showProgress("setting vocabulary...");
				try {
					// split list vocabularies by "\n"
					String[] wordList = vocabs.split("\\n");
					Log.d(TAG, "Vocabs: " + vocabs);
					Log.d(TAG, "Vocab length: " + wordList.length);
					RobotSpeechRecognition.setVocabulary(getRobot(), wordList,
							true);
				} catch (Exception e) {
					// set default vocabularies if get exception
					String[] wordList = { "Hi", "Hello", "How are you" };
					try {
						// set vocabularies with word spotting is true
						RobotSpeechRecognition.setVocabulary(getRobot(),
								wordList, true);
						makeToast("Set defaul vocabulary");
					} catch (RobotException e1) {
						e1.printStackTrace();
						cancelProgress();
						showInfoDialogFromThread("Speech Recognition",
								"set vocabulary failed! " + e.getMessage());
						return;
					}
				}
				cancelProgress();
			}
		}).start();
	}

	/**
	 * Start recognition
	 */
	private void startSpeechRecoginition() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean result;
				showProgress("starting speech recognition...");
				try {
					// get value in CheckBox and set enable audio, led
					RobotSpeechRecognition.setVisualExpression(getRobot(),
							cbEnableLeds.isChecked());
					RobotSpeechRecognition.setAudioExpression(getRobot(),
							cbEnableAudio.isChecked());
					// get Value from spinner and set language
					RobotSpeechRecognition.setCurrentLanguage(getRobot(),
							(String) spLanguages.getSelectedItem());
					// start monitor first
					result = speechMonitor.start();
					if (!result) {
						cancelProgress();
						showInfoDialogFromThread("Speech Recognition",
								"start speech recognition monitor failed!");
						return;
					}
					// start recognition
					result = RobotSpeechRecognition
							.startRecognition(getRobot());
				} catch (RobotException e) {
					e.printStackTrace();
					cancelProgress();
					showInfoDialogFromThread("Speech Recognition",
							e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Speech Recognition",
							"start speech recognition failed!");
				} else {
					makeToast("speech recognition is started!");
				}
			}
		}).start();
	}

	/**
	 * Stop speech recognition
	 */
	private void stopSpeechRecognition() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean result = false;
				showProgress("stopping speech recognition...");
				try {
					// stop monitor
					result = speechMonitor.stop();
					if (!result) {
						cancelProgress();
						makeToast("stop speech recognition monitor failed!");
						return;
					}
					// stop speech recognition
					result = RobotSpeechRecognition.stopRecognition(getRobot());
				} catch (RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Speech Recognition",
							"stop speech recognition failed!");
				} else {
					showInfoDialogFromThread("Speech Recognition",
							"speech recognition is stopped!");
				}
			}
		}).start();
	}

	@Override
	protected void onDestroy() {
		try {
			speechMonitor.stop();
			RobotSpeechRecognition.stopRecognition(getRobot());
		} catch (RobotException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_speech_recognition;
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
	protected void settingView() {
		btSetVocab = (Button) findViewById(R.id.btSetVocab);
		btStartRecognition = (Button) findViewById(R.id.btStartRecognition);
		btStopRecognition = (Button) findViewById(R.id.btStopRecognition);
		spLanguages = (Spinner) findViewById(R.id.spLanguageRecognition);
		cbEnableAudio = (CheckBox) findViewById(R.id.cbEnableSound);
		cbEnableLeds = (CheckBox) findViewById(R.id.cbEnableLed);
		edVocab = (EditText) findViewById(R.id.edVocab);
		tvResult = (TextView) findViewById(R.id.tvSpeechRecognitionResult);
		super.settingView();
	}

	@Override
	public void onSpeechDetected() {
		System.out.println("On Speech Detected");
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvResult.setText("On Speech Detected");
			}
		});
	}

	@Override
	public void onSpeechWordRecognized(WordRecognizedInfo arg0) {
		// System.out.println("On Speech Word Recognized");

		if (arg0 == null) {
			return;
		}
		RobotSpeechRecognition.RecognizedPhrase[] recognizedPhrases = arg0.recognizedPhrases;
		if (recognizedPhrases == null) {
			return;
		}
		String s = "";
		for (RecognizedPhrase rp : recognizedPhrases) {
			s += rp.phrase + ", " + rp.confidence + "\n";
		}
		final String print = s;
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				tvResult.setText("On Speech Word Recognized\n" + print);
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSetVocab:
			setVocabulary();
			break;
		case R.id.btStartRecognition:
			startSpeechRecoginition();
			break;
		case R.id.btStopRecognition:
			stopSpeechRecognition();
			break;
		default:
			break;
		}
	}
}
