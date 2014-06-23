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

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
 * Class for play audio file in mobile
 * @author Robot Team (FTI)
 */
public class AudioPlayLocalFile extends RobotApiDemoActivity {
	private static final String TAG="AudioPlayLocalFile";
	private static final String INSTRUCTIONS = "This class allow you to choose a file from moble device and play it on robot" +
			"Click Button browse file and choose one audio file. Finally, click button play to play audio file on robot";
	// Editext to display audio file path on the mobile device
	private EditText etAudioFilePath;
	// Button to browse a file on mobile
	private Button btBrowseFile;
	// Button to play file which is browsed
	private Button btPlayAudioFile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		// set event click for button browse
		btBrowseFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				browseFile();
			}
		});
		// set event click for button play audio file
		btPlayAudioFile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				playAudioFile();
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
		return R.layout.activity_audio_play_local_file;
	}

	/**
	 * Browse audio file in mobile
	 */
	protected void browseFile() {
		Intent pickAudioIntent = new Intent(Intent.ACTION_GET_CONTENT);
		pickAudioIntent.setType("audio/*");
		startActivityForResult(pickAudioIntent, REQUEST_CODE_PICK_AUDIO_FILE);
	}

	private static final int REQUEST_CODE_PICK_AUDIO_FILE = 0;
	
	/**
	 * Play audio file in mobile to robot speaker
	 */
	protected void playAudioFile() {
		new Thread(new Runnable() {			
			@Override
			public void run() {
				// get file path in edittext
				String filePath = etAudioFilePath.getText().toString();
				if (!filePath.isEmpty()) {
					boolean result = false;
					showProgress("playing local file...");
					try {
						// play local audio file to robot speaker
						result = RobotAudioPlayer.playLocalFile(getRobot(), filePath);	
					} catch (final RobotException e) {
						e.printStackTrace();
						cancelProgress();
						makeToast("play local audio file failed! " + e.getMessage());
						return;
					}
					cancelProgress();
					Log.d(TAG, "playLocalFile('" + filePath + "'): result=" + result);
					if (!result) {
						makeToast("play local audio file failed!");
					}
				} else {
					Log.d(TAG, "empty file path!");
					makeToast("file path is empty! ");
				}		
			}
		}).start();
	}

	/**
	 * Receive result is audio file path
	 */
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK 
				&& requestCode == REQUEST_CODE_PICK_AUDIO_FILE) {
			String path = data.getDataString();
			Log.d(TAG, "path=" + path);
			
			Uri selectedAudioFile = data.getData();
			
            String[] filePathColumn = { MediaStore.Audio.Media.DATA };
            Cursor cursor = getContentResolver().query(
            		selectedAudioFile, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            // get audio file path
            String filePath = cursor.getString(columnIndex);
            cursor.close();
            
            makeToast("selected: " + filePath);
            // set file path to edit text
            etAudioFilePath.setText(filePath);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

    @Override
    protected void settingView() {
    	etAudioFilePath = (EditText) findViewById(R.id.etAudioFilePath);
		btBrowseFile = (Button) findViewById(R.id.btBrowseFile);
		btPlayAudioFile = (Button) findViewById(R.id.btPlayAudioFile);
    	super.settingView();
    }
}
