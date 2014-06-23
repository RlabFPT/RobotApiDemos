/**
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
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.fpt.robot.RobotException;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.vision.RobotCamera;
/**
 * GetCameraImage class allows you to get the last image which is captured 
 * @author Robot Team (FTI)
 *
 */
public class GetCameraImage extends RobotApiDemoActivity implements OnCheckedChangeListener {
	private static final String TAG = "GetCameraImage";
	private static final String INSTRUCTIONS = "GetCameraImage class allows you to get the last image which is captured " +
			"from robot and display to screen. Select resolution and color space from spinner and click Get Image button";
    // image view to display image get from robot
    private ImageView ivCameraImage;    
    // button get image from robot
    private Button btGetImage;
    // radio select index of camera
    private RadioGroup rgGetCameraImageCameraSelection;
    // camera top and bottom
    private RobotCamera[] mCamera = new RobotCamera[2];
    private int selectedCameraIndex = 0;
    // select image resolution    
    private Spinner spGetCameraImageResolution;
    // select image color space
    private Spinner spGetCameraImageColorSpace;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// show instructions
		showInfoDialog(TAG, INSTRUCTIONS);
		ivCameraImage = (ImageView)findViewById(R.id.ivCameraImage);
		
		btGetImage = (Button)findViewById(R.id.btGetImage);
		// set event click for button get image
		btGetImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                getCameraImage();
            }
        });
		
		spGetCameraImageResolution = (Spinner)findViewById(R.id.spGetCameraImageResolution);
        ArrayAdapter<CharSequence> adapter1 = new ArrayAdapter<CharSequence>(
        		this, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // add resolution for adapter
        adapter1.add("QQVGA");
        adapter1.add("QVGA");
        adapter1.add("VGA");
        adapter1.add("4VGA");
        // display resolution to spinner
        spGetCameraImageResolution.setAdapter(adapter1);
        spGetCameraImageResolution.setSelection(2);
        
        spGetCameraImageColorSpace = (Spinner)findViewById(R.id.spGetCameraImageColorSpace);
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter<CharSequence>(
        		this, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set color space for adapter
        adapter2.add("BGR");
        adapter2.add("RGB");
        adapter2.add("GRAY");
        // display color space to spinner
        spGetCameraImageColorSpace.setAdapter(adapter2);
        spGetCameraImageColorSpace.setSelection(0);
        
		rgGetCameraImageCameraSelection = (RadioGroup) 
				findViewById(R.id.rgGetCameraImageCameraSelection);
		rgGetCameraImageCameraSelection.setOnCheckedChangeListener(this);
        // camera top
        mCamera[0] = RobotCamera.getCamera(getRobot(), RobotCamera.CAMERA_TOP);
        // camera bottom
        mCamera[1] = RobotCamera.getCamera(getRobot(), RobotCamera.CAMERA_BOTTOM);
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
		return R.layout.activity_get_camera_image;
	}

	/**
	 * Get image from camera
	 */
	protected void getCameraImage() {
		if (selectedCameraIndex < 0 && selectedCameraIndex > 1) {
    		showInfoDialog("Get camera image", "Invalid camera index. " +
    				"Please select Camera Top or Camera Bottom");
    		return;
    	}
		new Thread(new Runnable() {
            
            @Override
            public void run() {
            	byte[] image = null;
                showProgress("getting image from robot camera...");
                // default resolution and color space
                int resolution = RobotCamera.PICTURE_RESOLUTION_VGA;
            	int colorSpace = RobotCamera.PICTURE_COLORSPACE_GRAY;
                // get selected resolution
            	String resolutionStr = spGetCameraImageResolution.getSelectedItem().toString();
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
            	// get selected color space
            	String colorSpaceStr = spGetCameraImageColorSpace.getSelectedItem().toString();
            	if (colorSpaceStr.equals("BGR")) {
            		colorSpace = RobotCamera.PICTURE_COLORSPACE_BGR;
            	} else if (colorSpaceStr.equals("RGB")) {
            		colorSpace = RobotCamera.PICTURE_COLORSPACE_RGB;
            	} else if (colorSpaceStr.equals("GRAY")) {
            		colorSpace = RobotCamera.PICTURE_COLORSPACE_GRAY;
            	} else {
            		makeToast("Invalid selected color space!");
            		return;
            	}
            	
            	try {
            		// set config and get image
                	mCamera[selectedCameraIndex].setConfig(resolution, colorSpace);
                	image = mCamera[selectedCameraIndex].getImage();
                } catch (RobotException e) {
                    e.printStackTrace();
                    cancelProgress();
                    makeToast(e.getMessage());
                    return;
                }
                cancelProgress();
                if (image == null || image.length == 0) {
                    makeToast("Can not get image from robot camera!");
                    return;
                } else {
                	makeToast("Get image from robot camera successfully (size=" + image.length + ")!");
                	// default resolution
                	int imageWidth = 320;
                	int imageHeight = 240;
                	if (resolution == RobotCamera.PICTURE_RESOLUTION_QQVGA) {
                		// QQVGA resolution
                		imageWidth = 160;
                		imageHeight = 120;
                	} else if (resolution == RobotCamera.PICTURE_RESOLUTION_QVGA) {
                		// QVGA resolution
                		imageWidth = 320;
                		imageHeight = 240;
                	} else if (resolution == RobotCamera.PICTURE_RESOLUTION_VGA) {
                		// VGA resolution
                		imageWidth = 640;
                		imageHeight = 480;
                	} else if (resolution == RobotCamera.PICTURE_RESOLUTION_4VGA
                			|| resolution == RobotCamera.PICTURE_RESOLUTION_960P) {
                		// 960 resolution
                		imageWidth = 1280;
                		imageHeight = 960;
                	}
                	int imageSize = imageWidth*imageHeight;
                	int[] pixelData = new int[imageSize];
                	// set color space
                	if (colorSpace == RobotCamera.PICTURE_COLORSPACE_BGR) {
                    	for (int i = 0; i < imageSize; i++) {
                    		int r = image[3*i+2]&0xff;
                    		int g = image[3*i+1]&0xff;
                    		int b = image[3*i]&0xff;
                    		pixelData[i] = Color.rgb(r, g, b);
                    	}
                	} else if (colorSpace == RobotCamera.PICTURE_COLORSPACE_RGB) {
                    	for (int i = 0; i < imageSize; i++) {
                    		int r = image[3*i]&0xff;
                    		int g = image[3*i+1]&0xff;
                    		int b = image[3*i+2]&0xff;
                    		pixelData[i] = Color.rgb(r, g, b);
                    	}
                	} else if (colorSpace == RobotCamera.PICTURE_COLORSPACE_GRAY) {
                    	for (int i = 0; i < imageSize; i++) {
                    		int r, g, b;
                    		r = g = b = image[i]&0xff;
                    		pixelData[i] = Color.rgb(r, g, b);
                    	}
                	}                	
                	final Bitmap bitmap = Bitmap.createBitmap(pixelData, imageWidth, 
                			imageHeight, Bitmap.Config.ARGB_8888);
    				if (bitmap != null) {
    					runOnUiThread(new Runnable() {
    		                public void run() {
    		                	// display image to image view
    		                	ivCameraImage.setImageBitmap(bitmap);
    		                }
    		            });
    				}
                }
            }
        }).start();
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if (checkedId == R.id.rdCameraPreviewCameraTop) {
			selectedCameraIndex = RobotCamera.CAMERA_TOP;
		} else if (checkedId == R.id.rdCameraPreviewCameraBottom) {
			selectedCameraIndex = RobotCamera.CAMERA_BOTTOM;
		}
	}
}
