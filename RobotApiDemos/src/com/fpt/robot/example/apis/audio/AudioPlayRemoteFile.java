/*
 * Copyright (C) 2014 FPT Corporation
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;

/**
 * Play audio file on robot
 * 
 * @author Robot Team (FTI)
 */
public class AudioPlayRemoteFile extends RobotApiDemoActivity implements
		OnClickListener {
	private static final String TAG = "AudioPlayRemoteFile";
	private static final String INSTRUCTIONS = "This class allow you to play an audio file on the robot"
			+ "You can play audio file directly by click \"Play Remote File\", If you want load a file to play it later,"
			+ "you need to click \"Load File\" button and you can play, pause, stop file. Click \"Beep\" to play default file Beep";
	// Edittext display audio file path
	private EditText etAudioFilePath;
	// Button to load and unload file
	private Button btLoadFile;
	// Button to play file which is loaded
	private Button btPlay;
	// Button to pause file which is playing
	private Button btPause;
	// Button to stop file which is playing
	private Button btStop;
	// Button to play default beep file
	private Button btBeep;
	// Button to play remote file which is not need to load
	private Button btPlayRemoteFile;

	private RobotAudioPlayer mRobotAudioPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// create new robot audio player
		mRobotAudioPlayer = RobotAudioPlayer.getAudioPlayer(getRobot());
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		try {
			// set robot audio volume to 0.5
			mRobotAudioPlayer.setVolume(0.5f);
		} catch (RobotException e1) {
			e1.printStackTrace();
		}

		// set event click for buttons
		btLoadFile.setOnClickListener(this);
		btPlay.setOnClickListener(this);
		btPause.setOnClickListener(this);
		btStop.setOnClickListener(this);
		btBeep.setOnClickListener(this);
		btPlayRemoteFile.setOnClickListener(this);

		try {
			// get current audio volume of robot
			makeToast("Volume: " + mRobotAudioPlayer.getVolume());
		} catch (RobotException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_audio_play_remote_file;
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

	/**
	 * Load audio file to play later
	 */
	protected void loadFile() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// get file path on the edittext
				String filePath = etAudioFilePath.getText().toString();
				if (!filePath.isEmpty()) {
					boolean result = false;
					showProgress("loading remote file...");
					try {
						// load audio with path to robot
						result = mRobotAudioPlayer.loadFile(filePath);
						// get duration of audio which is loaded
						float duration = mRobotAudioPlayer.getDuration();
						makeToast("Audio Duration: " + duration);
					} catch (final RobotException e) {
						e.printStackTrace();
						cancelProgress();
						makeToast("load remote audio file failed! "
								+ e.getMessage());
						return;
					}
					cancelProgress();
					if (!result) {
						makeToast("load remote audio file failed!");
					}
				} else {
					makeToast("file path is empty! ");
				}
			}
		}).start();
	}

	/**
	 * Unload audio file which is loaded before
	 */
	protected void unloadFile() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				showProgress("loading remote file...");
				try {
					// Unload file
					result = mRobotAudioPlayer.unloadFile();
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("unload remote audio file failed! "
							+ e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("lunoad remote audio file failed!");
				}
			}
		}).start();
	}

	/**
	 * Play audio file which have already loaded on robot
	 */
	protected void play() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				showProgress("playing remote file...");
				try {
					// Play file
					result = mRobotAudioPlayer.play();
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("play remote audio file failed! "
							+ e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("play remote audio file failed!");
				}
			}
		}).start();
	}

	/**
	 * Pause current audio file which is playing
	 */
	protected void pause() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				showProgress("pausing remote file...");
				try {
					// Pause audio
					result = mRobotAudioPlayer.pause();
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("pausing remote audio file failed! "
							+ e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("pausing remote audio file failed!");
				}
			}
		}).start();
	}

	/**
	 * Stop current audio file which is playing
	 */
	protected void stop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				showProgress("stopping remote file...");
				try {
					// Stop audio
					result = mRobotAudioPlayer.stop();
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("stop remote audio file failed! "
							+ e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("stop remote audio file failed!");
				}
			}
		}).start();
	}

	/**
	 * Play default file "beep" on robot
	 */
	protected void beep() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				showProgress("playing beep sound...");
				try {
					// Play default file
					result = RobotAudioPlayer.beep(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("play beep sound failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("play beep sound failed!");
				}
			}
		}).start();
	}

	/**
	 * Play file on robot and don't need to load
	 */
	protected void playRemoteFileNoLoad() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result = false;
				showProgress("playing remote file...");
				try {
					// play file on robot with path
					result = RobotAudioPlayer.playRemoteFile(getRobot(),
							"tmp/audio.wav");
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("play remote failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("play remote failed!");
				}
			}
		}).start();
	}

	@Override
	protected void settingView() {
		etAudioFilePath = (EditText) findViewById(R.id.edRemotePath);
		btLoadFile = (Button) findViewById(R.id.btLoadRemoteFile);
		btPlay = (Button) findViewById(R.id.btPlayRemoteFile);
		btPause = (Button) findViewById(R.id.btPauseRemoteFile);
		btStop = (Button) findViewById(R.id.btStopRemoteFile);
		btBeep = (Button) findViewById(R.id.btBeep);
		btPlayRemoteFile = (Button) findViewById(R.id.btPlayRemoteNoLoad);
		btPlay.setEnabled(false);
		btPause.setEnabled(false);
		btStop.setEnabled(false);
		super.settingView();
	}

	boolean isLoaded = false;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btBeep:
			beep();
			break;
		case R.id.btLoadRemoteFile:
			if (isLoaded) {
				unloadFile();
				btPlay.setEnabled(false);
				isLoaded = false;
				btLoadFile.setText("Load File");
			} else {
				loadFile();
				btPlay.setEnabled(true);
				isLoaded = true;
				btLoadFile.setText("Unload File");
			}
			break;
		case R.id.btPlayRemoteFile:
			btPlay.setEnabled(false);
			btPause.setEnabled(true);
			btStop.setEnabled(true);
			play();
			break;
		case R.id.btPauseRemoteFile:
			btPause.setEnabled(false);
			btPlay.setEnabled(true);
			pause();
			break;
		case R.id.btStopRemoteFile:
			btStop.setEnabled(false);
			btPause.setEnabled(false);
			btPlay.setEnabled(true);
			stop();
			break;
		case R.id.btPlayRemoteNoLoad:
			playRemoteFileNoLoad();
			break;
		default:
			break;
		}
	}
}
