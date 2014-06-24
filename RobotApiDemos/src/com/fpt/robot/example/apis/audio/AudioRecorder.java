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

import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.audio.RobotAudioRecorder;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.filetransfer.RobotFileTransfer;

/**
 * This class is used to record an audio file in robot, and this file will pulled to mobile device
 * 
 * @author Robot Team (FTI)
 */
public class AudioRecorder extends RobotApiDemoActivity {
	private static final String TAG = "AudioRecorder";
	private static final String INSTRUCTIONS = "This class is used to record an audio file in robot, and this file will pulled to mobile device" +
			"After choose file type, sample rate, channels, the robot audio recorder will be started. Click button \"Stop Record\" to stop record";
	// Textview display how long have recorded
	private TextView tvAudioRecordingTimer;
	// Edittext display recorded audio file name
	private EditText etRecordedAudioFileName;
	// Button start record
	private Button btStartRecording;
	// Button stop record
	private Button btStopRecording;
	// Button play audio file which was just record
	private Button btPlayAudioRecorded;

	private Boolean mStartRequested = false;
	private Boolean mStopRequested = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		showInfoDialog(TAG, INSTRUCTIONS);
		etRecordedAudioFileName = (EditText) findViewById(R.id.etRecordedAudioFileName);
		// set default file name of audio record file
		etRecordedAudioFileName.setText("record_"
				+ String.valueOf(System.currentTimeMillis()) + ".wav");
		tvAudioRecordingTimer = (TextView) findViewById(R.id.tvAudioRecordingTimer);

		btStartRecording = (Button) findViewById(R.id.btStartAudioRecording);
		// set event click for button start recording
		btStartRecording.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startAudioRecording();
			}
		});

		btStopRecording = (Button) findViewById(R.id.btStopAudioRecording);
		// set event click for button stop recording
		btStopRecording.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				stopAudioRecording();
			}
		});

		btPlayAudioRecorded = (Button) findViewById(R.id.btPlayAudioRecord);
		btPlayAudioRecorded.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				playFileAudioRecorded();
			}
		});
	}

	// Audio recorded file path which will saved in mobile device
	private String mSelectedSavedFilePath;
	// Audio recorded file path which will saved in robot
	private String mRobotFilePath;

	private int mSelectedRecordingFormatTypeIndex = 0;
	private int mSelectedRecordingFormatType = RobotAudioRecorder.FORMAT_TYPE_WAV;
	private int mSelectedRecordingSampleRateIndex = 0;
	private int mSelectedRecordingSampleRate = RobotAudioRecorder.SAMPLE_RATE_16KHZ;
	private int mSelectedRecordingChannels = (RobotAudioRecorder.CHANNEL_FRONT | RobotAudioRecorder.CHANNEL_REAR);

	/**
	 * Start recording
	 */
	protected void startAudioRecording() {
		// Audio recorder format
		CharSequence[] items = { "WAV format", "OGG format" };
		mSelectedRecordingFormatTypeIndex = 0;
		mSelectedRecordingFormatType = RobotAudioRecorder.FORMAT_TYPE_WAV;
		showSingleChoiceDialog("Please select format", items,
				mSelectedRecordingFormatTypeIndex,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						mSelectedRecordingFormatTypeIndex = item;
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (mSelectedRecordingFormatTypeIndex == 0) {
							mSelectedRecordingFormatType = RobotAudioRecorder.FORMAT_TYPE_WAV;
							etRecordedAudioFileName.setText("record_"
									+ String.valueOf(System.currentTimeMillis())
									+ ".wav");
						} else {
							mSelectedRecordingFormatType = RobotAudioRecorder.FORMAT_TYPE_OGG;
							etRecordedAudioFileName.setText("record_"
									+ String.valueOf(System.currentTimeMillis())
									+ ".ogg");
						}
						doSelectSampleRate();
					}
				});
	}

	/**
	 * Select sample rate for recording
	 */
	protected void doSelectSampleRate() {
		// List of sample rate
		CharSequence[] items = { "16KHz Sampling Rate", "48KHz Sampling Rate" };
		mSelectedRecordingSampleRateIndex = 0;
		mSelectedRecordingSampleRate = RobotAudioRecorder.SAMPLE_RATE_16KHZ;
		showSingleChoiceDialog("Please select sample rate", items,
				mSelectedRecordingSampleRateIndex,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int item) {
						mSelectedRecordingSampleRateIndex = item;
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (mSelectedRecordingSampleRateIndex == 0) {
							mSelectedRecordingSampleRate = RobotAudioRecorder.SAMPLE_RATE_16KHZ;
						} else {
							mSelectedRecordingSampleRate = RobotAudioRecorder.SAMPLE_RATE_48KHZ;
						}
						doSelectChannels();
					}
				});
	}

	/**
	 * Select channel for recording
	 */
	protected void doSelectChannels() {
		// List of channels
		CharSequence[] items = { "Left channel", "Right channel",
				"Front channel", "Rear channel" };
		final boolean[] checkedItems = { true, true, false, false };
		mSelectedRecordingSampleRate = RobotAudioRecorder.SAMPLE_RATE_16KHZ;
		showMultiChoiceDialog("Please select channels", items, checkedItems,
				new DialogInterface.OnMultiChoiceClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
						checkedItems[which] = isChecked;
					}
				}, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						mSelectedRecordingChannels = 0;
						if (checkedItems[0]) {
							mSelectedRecordingChannels = mSelectedRecordingChannels
									| RobotAudioRecorder.CHANNEL_LEFT;
						}
						if (checkedItems[1]) {
							mSelectedRecordingChannels = mSelectedRecordingChannels
									| RobotAudioRecorder.CHANNEL_RIGHT;
						}
						if (checkedItems[2]) {
							mSelectedRecordingChannels = mSelectedRecordingChannels
									| RobotAudioRecorder.CHANNEL_FRONT;
						}
						if (checkedItems[3]) {
							mSelectedRecordingChannels = mSelectedRecordingChannels
									| RobotAudioRecorder.CHANNEL_REAR;
						}
						if (mSelectedRecordingChannels == 0) {
							makeToast("Invalid channel selection");
							return;
						}
						doStartRecording();
					}
				});
	}

	/**
	 * Do recording after select sample rate and channel
	 */
	protected void doStartRecording() {
		if (mStartRequested)
			return;
		mStartRequested = true;
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean isRecording = false;
				showProgress("checking recording status...");
				try {
					// check if Robot audio recorder is running
					isRecording = RobotAudioRecorder.isRecording(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("check audio recoder status failed! "
							+ e.getMessage());
					return;
				}
				cancelProgress();

				if (isRecording) {
					Log.e(TAG, "Audio recording is running!");
					showInfoDialog("Warning!", "Audio recording is running!");
					mStartRequested = false;
					return;
				}
				// file name to save audio file after record
				mSelectedSavedFilePath = Environment
						.getExternalStorageDirectory().getPath()
						+ "/"
						+ etRecordedAudioFileName.getText().toString();

				int audioFormat = mSelectedRecordingFormatType;
				int sampleRate = mSelectedRecordingSampleRate;
				int channels = mSelectedRecordingChannels;

				mRobotFilePath = "tmp/record.wav";
				if (audioFormat == RobotAudioRecorder.FORMAT_TYPE_OGG) {
					mRobotFilePath = "tmp/record.ogg";
				} else if (audioFormat == RobotAudioRecorder.FORMAT_TYPE_WAV) {
					mRobotFilePath = "tmp/record.wav";
				}

				showProgress("record starting...");
				try {
					// start recording
					if (!RobotAudioRecorder.startRecording(getRobot(),
							mRobotFilePath, audioFormat, sampleRate, channels)) {
						Log.e(TAG, "can not start audio recording!");
						cancelProgress();
						showInfoDialog("Error!",
								"Can not start audio recording!");
						return;
					}
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("start audio recoder failed! " + e.getMessage());
					return;
				}
				cancelProgress();

				// showInfoDialog("Info",
				// "Audio recording is started!");
				makeToast("Audio recording is started!");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						btPlayAudioRecorded.setVisibility(View.GONE);
					}
				});
				// start recording timer
				mStartTime = System.currentTimeMillis();
				mRecordingTimer = new Timer();
				mRecordingTimer.schedule(new UpdateTimeTask(), 100, 200);
				mStartRequested = false;
			}
		}).start();
	}

	/**
	 * Stop audio recording
	 */
	protected void stopAudioRecording() {
		if (mStopRequested) {
			return;
		}
		mStopRequested = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean isRecording = false;
				showProgress("checking recording status...");
				try {
					// check if robot audio record is running
					isRecording = RobotAudioRecorder.isRecording(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("check audio recoder status failed! "
							+ e.getMessage());
					return;
				}
				cancelProgress();
				if (!isRecording) {
					Log.e(TAG, "Audio recording is not running!");
					showInfoDialog("Error", "Audio recording is not running!");
					mStopRequested = false;
					return;
				}
				// makeToast("Requesting...");
				// stop recording timer
				mRecordingTimer.cancel();
				showProgress("record stopping...");
				try {
					if (!RobotAudioRecorder.stopRecording(getRobot())) {
						Log.e(TAG, "can not stop audio recording!");
						cancelProgress();
						showInfoDialog("Error", "Can not stop recording!");
						mStopRequested = false;
						return;
					}
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("stop audio recoder failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				// displayAlertDialog("Audio record finish!",
				// "Audio recording is stopped!");
				makeToast("Audio recording is stopped!");

				showProgress("finishing...");
				try {
					// pull file audio from robot to mobile
					if (!RobotFileTransfer.pullFile(getRobot(), mRobotFilePath,
							mSelectedSavedFilePath)) {
						Log.e(TAG, "can not pull recorded file!");
						cancelProgress();
						showInfoDialog("Error!", "Can not pull recorded file!");
						return;
					}
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("pull recorded file failed! " + e.getMessage());
					return;
				}
				cancelProgress();

				makeToast("Audio file saved to " + mSelectedSavedFilePath);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						btPlayAudioRecorded.setVisibility(View.VISIBLE);
					}
				});

				mStopRequested = false;
			}
		}).start();
	}

	// play recorded file
	private void playFileAudioRecorded() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				showProgress("playing local file...");
				try {
					// play local audio file to robot speaker
					result = RobotAudioPlayer.playLocalFile(getRobot(),
							mSelectedSavedFilePath);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("play local audio file failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				Log.d(TAG, "playLocalFile('" + mSelectedSavedFilePath
						+ "'): result=" + result);
				if (!result) {
					makeToast("play local audio file failed!");
				}
			}
		}).start();
	}

	private long mStartTime = 0L;
	private Timer mRecordingTimer;

	class UpdateTimeTask extends TimerTask {
		public void run() {
			long millis = System.currentTimeMillis() - mStartTime;
			int sec = (int) (millis / 1000);
			final int minutes = sec / 60;
			final int seconds = sec % 60;

			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					tvAudioRecordingTimer.setText(String.format("%02d:%02d",
							minutes, seconds));
				}
			});
		}
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_audio_recorder;
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
}
