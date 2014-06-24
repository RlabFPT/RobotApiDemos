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
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioDevice;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;

/**
 * This class allow you to get current robot's volume and it's also allow you to set volume for robot.
 * 
 * @author Robot Team (FTI)
 */
public class AudioDeviceGetSetVolume extends RobotApiDemoActivity {
	private static final String TAG = "AudioDeviceSetVolume";
	private static final String INSTRUCTION = "This class allow you to get current robot's volume and it's also allow you to set volume for robot."
			+ " Touch on the volume seekbar and choose volume percentage, finally click button \"Set Volume\" to set current volume for robot ";
	// SeekBar show current audio volume of robot
	private SeekBar sbAudioVolume;
	// Button to set volume which is chosen in SeekBar to robot's volume
	private Button btSetVolume;
	// Text view display percent (like 50%) of robot's volume
	private TextView tvVolumePercent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// Display instruction of class
		showInfoDialog(TAG, INSTRUCTION);
		// set event click for button SetVolume
		btSetVolume.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setVolume();				
			}
		});
		// set event for seekbar when change value of its
		sbAudioVolume.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, final int progress,
					boolean fromUser) {
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// update volume percentage to TextView
						tvVolumePercent.setText(progress + "%");
					}
				});
			}
		});

		updateVolume();
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
		return R.layout.activity_audio_device_set_volume;
	}
	
	

	/**
	 * Get current volume of robot and update information to seekbar, textview
	 */
	private void updateVolume() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				int volume = 0;
				showProgress("getting output volume...");
				try {
					// Get current volume of robot
					volume = RobotAudioDevice.getOutputVolume(getRobot());
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("get robot's volume failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				Log.d(TAG, "getVolume(): volume=" + volume);
				final int vol = volume;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// set value to SeekBar
						sbAudioVolume.setProgress(vol);
						// set text to textview
						tvVolumePercent.setText(vol + "%");
					}
				});
			}
		}).start();
	}

	/**
	 * Set volume for robot speaker
	 */
	private void setVolume() {
		// get volume percentage on seekbar
		final int volume = sbAudioVolume.getProgress();
		new Thread(new Runnable() {

			@Override
			public void run() {
				boolean result = false;
				showProgress("setting output volume...");
				try {
					// set volume for robot
					result = RobotAudioDevice.setOutputVolume(getRobot(),
							volume);
				} catch (final RobotException e) {
					e.printStackTrace();
					cancelProgress();
					makeToast("set robot's volume failed! " + e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					makeToast("set robot's volume failed!");
				}
			}
		}).start();
	}

	@Override
	protected void settingView() {
		sbAudioVolume = (SeekBar) findViewById(R.id.sbAudioVolume);
		btSetVolume = (Button) findViewById(R.id.btSetVolume);
		tvVolumePercent = (TextView) findViewById(R.id.tvVolumePercent);
		super.settingView();
	}
}
