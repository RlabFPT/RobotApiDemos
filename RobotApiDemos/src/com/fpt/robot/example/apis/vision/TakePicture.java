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
package com.fpt.robot.example.apis.vision;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.vision.RobotCamera;
/**
 * This class is used to take one or more pictures from robot's camera. 
 * @author Robot Team (FTI)
 *
 */
public class TakePicture extends RobotApiDemoActivity implements
		OnCheckedChangeListener {
	private static final String TAG = "TakePicture";
	private static final String INSTRUCTIONS = "This class is used to take one or more pictures from robot's camera. " +
			"Select camera top or camera button from radio group. " +
			"Select resolution from spinner " +
			"Select enable action or not while take picture. " +
			"If you want to take more picture, select enable take more picture and choose the number which you want to take. " +
			"Image will be save in sdcard and display into image view";
	// image view to display image which is taken from robot's camera
	private ImageView ivTakenPicture;
	// button take picture
	private Button btTakePicture;
	// spinner picture resolution
	private Spinner spTakePictureResolution;
	// radio select camera top or bottom
	private RadioGroup rgTakePictureCameraSelection;
	// spinner number of pictures
	private Spinner spNumberOfPictures;
	// check box enable action while take picture
	private CheckBox cbEnableAction;
	// check box enable take more pictures
	private CheckBox cbMorePictures;
	// 2 camera top and bottom
	private RobotCamera[] mCamera = new RobotCamera[2];
	// camera position
	private int selectedCameraIndex = 0;
	// enable set name for image which is taken from robot
	private boolean SET_PICTURE_FILE_NAME = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		ivTakenPicture = (ImageView) findViewById(R.id.ivTakenPicture);

		btTakePicture = (Button) findViewById(R.id.btTakePicture);
		// set event click for button take picture
		btTakePicture.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				takePicture();
			}
		});

		cbEnableAction = (CheckBox) findViewById(R.id.cbEnableAction);
		cbMorePictures = (CheckBox) findViewById(R.id.cbMorePic);
		spNumberOfPictures = (Spinner) findViewById(R.id.spNumberOfPictures);
		ArrayAdapter<CharSequence> adapterNumberPics = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_item);
		adapterNumberPics.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterNumberPics.add("1");
		adapterNumberPics.add("2");
		adapterNumberPics.add("3");
		adapterNumberPics.add("4");
		// set number of picture if choose take more pictures
		spNumberOfPictures.setAdapter(adapterNumberPics);
		spNumberOfPictures.setSelection(0);
		spNumberOfPictures.setEnabled(false);
		// enable and disable take more picture
		cbMorePictures.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (cbMorePictures.isChecked()){
					spNumberOfPictures.setEnabled(true);
				} else{
					spNumberOfPictures.setEnabled(false);
				}
			}
		});
		spTakePictureResolution = (Spinner) findViewById(R.id.spTakePictureResolution);
		ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
				this, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.add("QQVGA");
		adapter.add("QVGA");
		adapter.add("VGA");
		adapter.add("4VGA");
		// set resolution image
		spTakePictureResolution.setAdapter(adapter);
		spTakePictureResolution.setSelection(2);

		rgTakePictureCameraSelection = (RadioGroup) findViewById(R.id.rgTakePictureCameraSelection);
		rgTakePictureCameraSelection.setOnCheckedChangeListener(this);
		// get number of camera
		int numberOfCamera = RobotCamera.getNumberOfCameras(getRobot());
		makeToast("Number of camera: " + numberOfCamera);
		mCamera[0] = RobotCamera.getCamera(getRobot(), RobotCamera.CAMERA_TOP);
		mCamera[1] = RobotCamera.getCamera(getRobot(),
				RobotCamera.CAMERA_BOTTOM);
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
		return R.layout.activity_take_picture;
	}

	/**
	 * Take picture with robot's camera
	 */
	protected void takePicture() {
    	if (selectedCameraIndex < 0 && selectedCameraIndex > 1) {
    		showInfoDialog("Take picture", "Invalid camera index. " +
    				"Please select Camera Top or Camera Bottom");
    		return;
    	}
        new Thread(new Runnable() {
            
            @Override
            public void run() {
            	boolean enableAction = cbEnableAction.isChecked();
            	boolean morePictures = cbMorePictures.isChecked();
            	// if take more picture, name of pictures which are saved in sdcard will be set to default
            	if (morePictures){
            		SET_PICTURE_FILE_NAME = false;
            	}
                String picture = null;
                showProgress("taking picture...");
                // default resolution
            	int resolution = RobotCamera.PICTURE_RESOLUTION_VGA;
            	// default picture format (can change to JPG,PNG)
            	String pictureFormat = RobotCamera.PICTURE_FORMAT_BMP;
                // get selected resolution
            	String resolutionStr = spTakePictureResolution.getSelectedItem().toString();
            	if (resolutionStr.equals("QQVGA")) {
            		resolution = RobotCamera.PICTURE_RESOLUTION_QQVGA;
            	} else if (resolutionStr.equals("QVGA")) {
            		resolution = RobotCamera.PICTURE_RESOLUTION_QVGA;
            	} else if (resolutionStr.equals("VGA")) {
            		resolution = RobotCamera.PICTURE_RESOLUTION_VGA;
            	} else if (resolutionStr.equals("4VGA")) {
            		resolution = RobotCamera.PICTURE_RESOLUTION_4VGA;
            	} else {
            		makeToast("Invalid selected resolution!");
            		return;
            	}
            	
                try {
                	if (SET_PICTURE_FILE_NAME) {
                		// default name to save picture
	                	String savedPath = Environment.getExternalStorageDirectory().getPath() + "/picture";
	                	if (pictureFormat == RobotCamera.PICTURE_FORMAT_JPG) {
	                		savedPath = savedPath + ".jpg";
	                	} else if (pictureFormat == RobotCamera.PICTURE_FORMAT_PNG) {
	                		savedPath = savedPath + ".png";
	                	} else if (pictureFormat == RobotCamera.PICTURE_FORMAT_BMP) {
	                		savedPath = savedPath + ".bmp";
	                	}
	                	boolean result = false;
	                	if (enableAction){
	                		// take picture with action
	                		result = mCamera[selectedCameraIndex].takePictureWithAction(resolution, pictureFormat, savedPath);
	                	}else{
	                		// take picture no action
	                		result = mCamera[selectedCameraIndex].takePicture(resolution, pictureFormat, savedPath);
	                	}	                		                		                
	                    if (result) {
	                    	picture = savedPath;
	                    }
                	} else {
                		// take picture with specified resolution and format
                		if (enableAction){
                			if (morePictures){
                				// get number of pictures will be taken
                				int numberOfPictures = spNumberOfPictures.getSelectedItemPosition()+1;
                				// time delay for each picture
                				long delay = 1000;
                				// take more picture with action
                				String[] path = mCamera[selectedCameraIndex].takePicturesWithAction(resolution, pictureFormat, numberOfPictures, delay);
        	                	if (path!=null && path[0]!=null){
        	                		picture = path[0];
        	                	}
                			}else{
                				// take 1 picture with action
                				picture = mCamera[selectedCameraIndex].takePictureWithAction(resolution, pictureFormat);
                			}
                		}else{
                			if (morePictures){
                				// get number of pictures will be taken
                				int numberOfPictures = spNumberOfPictures.getSelectedItemPosition()+1;
                				// time delay for each picture
                				long delay = 1000;
                				// taken more pictures no action
                				String[] path = mCamera[selectedCameraIndex].takePictures(resolution, pictureFormat, numberOfPictures, delay);
        	                	if (path!=null && path[0]!=null){
        	                		picture = path[0];
        	                	}
                			}else{
                				// take one picture no action
                				picture = mCamera[selectedCameraIndex].takePicture(resolution, pictureFormat);
                			}
                		}                		
                	}
                } catch (RobotException e) {
                    e.printStackTrace();
                    cancelProgress();
                    showInfoDialogFromThread("Take picture", 
                    		"Take picture from camera failed! " + e.getMessage());
                    return;
                }
                cancelProgress();
                if (picture == null || picture.isEmpty()) {
                    Log.e(TAG, "can not take picture!");
                    showInfoDialogFromThread("Take picture", 
                    		"Cannot take picture from camera!");
                    return;
                } else {
                    Log.d(TAG, "Picture saved to " + picture + "!");
                    makeToast("Picture saved to " + picture + "!");
                    displayPicture(picture);
                }
            }
        }).start();
    }

	/**
	 * Display picture to screen
	 * @param picture path in sdcard
	 */
	protected void displayPicture(final String picture) {
		final String picturePath = picture;
		// decode to bitmap
		final Bitmap bm = BitmapFactory.decodeFile(picturePath);
		if (bm != null) {
			runOnUiThread(new Runnable() {
				public void run() {
					// display image to image view
					ivTakenPicture.setImageBitmap(bm);
				}
			});
		} else {
			makeToast("Picture saved to " + picturePath + "!");
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.rdTakePictureCameraTop) {
			selectedCameraIndex = 0;
		} else if (checkedId == R.id.rdTakePictureCameraBottom) {
			selectedCameraIndex = 1;
		}
	}
}
