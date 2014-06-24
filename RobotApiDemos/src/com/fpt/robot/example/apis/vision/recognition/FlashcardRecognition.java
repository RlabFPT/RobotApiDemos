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
package com.fpt.robot.example.apis.vision.recognition;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.fpt.robot.RobotException;
import com.fpt.robot.audio.RobotAudioPlayer;
import com.fpt.robot.example.apis.R;
import com.fpt.robot.example.apis.RobotApiDemoActivity;
import com.fpt.robot.leds.RobotLedAnimation;
import com.fpt.robot.tts.RobotTextToSpeech;
import com.fpt.robot.vision.RobotFlashcardRecognition;
/**
 * 
 * @author Robot Team (FTI)
 *
 */
@SuppressLint("HandlerLeak") public class FlashcardRecognition extends RobotApiDemoActivity implements 
	RobotFlashcardRecognition.Listener, OnItemClickListener {
	private static final String TAG = "FlashcardRecognitionTestActivity";
    
    @SuppressWarnings("unused")
	private static final String DATABASE_PATH = "data/flashcards";

    private Timer mTimer = null;    
    private static final long DEFAULT_TIMEOUT_VALUE = 10000; // milliseconds
    
    private String[] mActionList = {
    		"1. Load Database",
    		"2. Flashcard/Tag List",
    		"3. Set Database Directory",
    		"4. Get Current Database Directory",
    		"5. Start Recognition",
    		"6. Stop Recognition",
    		"7. Learn A Flashcard",
    		"8. Forget Flashcard(s)",
    		"9. Clear Database"
    };
    
    private ListView lvActionList = null;    
	private RobotFlashcardRecognition.Monitor mFlashcardMonitor;
	
	private class FlashcardInfo {
		String englishDescription;
		String vietnameseDescription;
		public FlashcardInfo(String engDes, String vieDes) {
			englishDescription = engDes;
			vietnameseDescription = vieDes;
		}
	}
	
	private HashMap<String, FlashcardInfo> mFlashcardInfos;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lvActionList = (ListView) findViewById(R.id.lvActionList);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, 
        		android.R.layout.simple_list_item_1, 
        		android.R.id.text1, mActionList);
        adapter.setNotifyOnChange(true);
        lvActionList.setAdapter(adapter);
        lvActionList.setOnItemClickListener(this);
        
        initFlashcardsInfo();        
    }

	private void initFlashcardsInfo() {

        mFlashcardMonitor = new RobotFlashcardRecognition.Monitor(getRobot(), this);
        mFlashcardInfos = new HashMap<String /*tag*/, FlashcardInfo>();
        
        mFlashcardInfos.put("chu_a", new FlashcardInfo("Ant", "con kiến"));
        mFlashcardInfos.put("chu_b", new FlashcardInfo("Bear", "con gấu"));
        mFlashcardInfos.put("chu_c", new FlashcardInfo("Cat", "con mèo"));
        mFlashcardInfos.put("chu_d", new FlashcardInfo("Dog", "con chó"));
        mFlashcardInfos.put("chu_e", new FlashcardInfo("Elephant", "con voi"));
        mFlashcardInfos.put("chu_f", new FlashcardInfo("Fish", "con cá"));
        mFlashcardInfos.put("chu_g", new FlashcardInfo("Goat", "con dê"));
        mFlashcardInfos.put("chu_h", new FlashcardInfo("Horse", "con ngựa"));
        mFlashcardInfos.put("chu_i", new FlashcardInfo("Insect", "côn trùng"));
        mFlashcardInfos.put("chu_j", new FlashcardInfo("Jerboa", "con chuột nhảy"));
        mFlashcardInfos.put("chu_k", new FlashcardInfo("Kangaroo", "con chuột túi"));
        mFlashcardInfos.put("chu_l", new FlashcardInfo("Lion", "con sư tử"));
        mFlashcardInfos.put("chu_m", new FlashcardInfo("Mouse", "con chuột"));
        mFlashcardInfos.put("chu_n", new FlashcardInfo("Nurse shark", "con cá mập y tá"));
        mFlashcardInfos.put("chu_o", new FlashcardInfo("Ostrich", "con đà điểu"));
        mFlashcardInfos.put("chu_p", new FlashcardInfo("Pig", "con lợn"));
        mFlashcardInfos.put("chu_q", new FlashcardInfo("Quetzal", "con chim quýt"));
        mFlashcardInfos.put("chu_r", new FlashcardInfo("Ant", "con kiến"));
        mFlashcardInfos.put("chu_s", new FlashcardInfo("Snake", "con rắn"));
        mFlashcardInfos.put("chu_t", new FlashcardInfo("Turtle", "con rùa"));
        mFlashcardInfos.put("chu_u", new FlashcardInfo("Unicorn", "con ngựa một sừng"));
        mFlashcardInfos.put("chu_v", new FlashcardInfo("Vulture", "con kền kền"));
        mFlashcardInfos.put("chu_w", new FlashcardInfo("Wolf", "con chó sói"));
        mFlashcardInfos.put("chu_x", new FlashcardInfo("Xu", "con không biết tên"));
        mFlashcardInfos.put("chu_y", new FlashcardInfo("Yak", "con bò tây tạng"));
        mFlashcardInfos.put("chu_z", new FlashcardInfo("Zebra", "con ngựa vằn"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.flashcard_recognition, menu);
		return true;
	}

	@Override
	protected int getLayoutID() {
		return R.layout.activity_flashcard_recognition;
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Log.d(TAG, "onItemClick(position=" + position + ")");
		if (position > mActionList.length) {
			Log.e(TAG, "out of range!");
			return;
		}
		String action_name = mActionList[position];
		Log.d(TAG, "action: " + action_name);
		callAction(action_name);
	}

    private void callAction(String action_name) {
    	if (action_name.equals("Load Database")) {
            loadDatabase();
        } else if (action_name.equals("Start Recognition")) {
            startRecognition();
        } else if (action_name.equals("Stop Recognition")) {
            stopRecognition();
        } else if (action_name.equals("Learn A Flashcard")) {
            learnFlashcard();
        } else if (action_name.equals("Forget Flashcard(s)")) {
        	forgetFlashcard();
        } else if (action_name.equals("Flashcard/Tag List")) {
        	chooseTypeToDisplay();
        } else if (action_name.equals("Clear Database")) {
            clearDatabase();
        } else if (action_name.equals("Get Current Database Directory")) {
            getCurrentDatabaseDirectory();
        } else if (action_name.equals("Set Database Directory")) {
            setDatabaseDirectory();
        }
	}

    private int mSelectedDisplayType;
	private void chooseTypeToDisplay() {
		CharSequence[] items = {
				"Flashcard List By Tag...", 
				"All Tags List..."
		};
		mSelectedDisplayType = 0; // by select tag
		showSingleChoiceDialog("Please select", items, mSelectedDisplayType,
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				mSelectedDisplayType = item;
			}
		}, 
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mSelectedDisplayType == 0) {
					getFlashcardsByTag();
				} else if (mSelectedDisplayType == 1) {
					getAllTags();
				}
				dialog.dismiss();
			}
		});
	}

	private void loadDatabase() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final String directory;
				showProgress("getting current database directory...");
				try {
					directory = RobotFlashcardRecognition.getCurrentDatabaseDirectory(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						doLoadDatabase_prepare(directory);
					}
				});
			}
		}).start();
	}

	private void doLoadDatabase_prepare(String directory) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Load database from...");
		builder.setMessage("Input path to databse directory (on robot):");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		if (directory != null) {
			input.setText(directory);
		}
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String databaseDir = input.getText().toString();
				doLoadDatabase(databaseDir);
			}

		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	
	protected void doLoadDatabase(final String databaseDir) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean result;
				showProgress("loading database...");
				try {
					result = RobotFlashcardRecognition.loadDatabase(getRobot(), databaseDir);
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Load database", 
							"Load database from directory [" + databaseDir + "] failed!");
				} else {
					showInfoDialogFromThread("Set database directory", 
							"Load database from directory [" + databaseDir + "]!");
				}
			}
		}).start();
	}

	private void getCurrentDatabaseDirectory() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String directory;
				showProgress("getting current database directory...");
				try {
					directory = RobotFlashcardRecognition.getCurrentDatabaseDirectory(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (directory != null) {
					showInfoDialogFromThread("Current database directory", 
							"[" + directory + "]");
				} else {
					showInfoDialogFromThread("Current database directory", 
							"not available!");
				}
			}
		}).start();
	}

	@SuppressWarnings("unused")
	private void getCurrentConfidenceThreshold() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				float confidence;
				showProgress("getting current confidence threshold..");
				try {
					confidence = RobotFlashcardRecognition.getCurrentConfidenceThreshold(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				showInfoDialogFromThread("Current confidence threshold", 
						"[" + confidence + "]");
			}
		}).start();
	}

	private void setDatabaseDirectory() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final String directory;
				showProgress("getting current database directory...");
				try {
					directory = RobotFlashcardRecognition.getCurrentDatabaseDirectory(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						doSetDatabaseDirectory_prepare(directory);
					}
				});
			}
		}).start();
	}

	private void doSetDatabaseDirectory_prepare(String directory) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Set database directory");
		builder.setMessage("Input path to databse directory (on robot):");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		if (directory != null) {
			input.setText(directory);
		}
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String databaseDir = input.getText().toString();
				doSetDatabaseDirectory(databaseDir);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	
	private void doSetDatabaseDirectory(
			final String databaseDir) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean result;
				showProgress("setting database directory...");
				try {
					result = RobotFlashcardRecognition.setDatabaseDirectory(getRobot(), databaseDir);
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Set database directory", 
							"Set database directory to [" + databaseDir + "] failed!");
				} else {
					showInfoDialogFromThread("Set database directory", 
							"Database directory has been set to [" + databaseDir + "]!");
				}
			}
		}).start();
	}
	
	@SuppressWarnings("unused")
	private void setConfidenceThreshold() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final float confidenceThreshold;
				showProgress("get current confidence threshold...");
				try {
					confidenceThreshold = RobotFlashcardRecognition.getCurrentConfidenceThreshold(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						doSetConfidenceThreshold_prepare(confidenceThreshold);
					}
				});
			}
		}).start();
	}

	private void doSetConfidenceThreshold_prepare(float confidenceThreshold) {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Set confidence threshold");
		builder.setMessage("Input confidence threshold value:");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setText(String.valueOf(confidenceThreshold));
		input.setInputType(InputType.TYPE_CLASS_NUMBER);
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				float threshold = Float.valueOf(input.getText().toString());
				doSetConfidenceThreshold(threshold);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	private void doSetConfidenceThreshold(
			final float threshold) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean result;
				showProgress("setting confidence threshold...");
				try {
					result = RobotFlashcardRecognition.setConfidenceThreshold(getRobot(), threshold);
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Set confidence threshold", 
							"Set confidence threshold to [" + threshold + "] failed!");
				} else {
					showInfoDialogFromThread("Set database directory", 
							"Confidence threshold has been set to [" + threshold + "]!");
				}
			}
		}).start();
	}

	private int mSelectedTrainingMethod;
	
    private void learnFlashcard() {
    	CharSequence[] items = {
				"From camera...", 
				"From an existing picture...",
				"From robot camera..."
		};
    	mSelectedTrainingMethod = 1; // from existing picture
		showSingleChoiceDialog("Select Training Method", items, mSelectedTrainingMethod,
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				mSelectedTrainingMethod = item;
			}
		}, 
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (mSelectedTrainingMethod == 0) {
					doCameraTraining();
				} else if (mSelectedTrainingMethod == 1) {
					doPictureTraining();
				} else if (mSelectedTrainingMethod == 2) {
					doRobotCameraTraining();
				}
				dialog.dismiss();
			}
		});
	}

	private String flashcardFolder;
    
    private boolean createFlashcardTraningFolder() {
    	flashcardFolder = Environment.getExternalStoragePublicDirectory(
    			Environment.DIRECTORY_PICTURES) + "/flashcards/"; 
        File newdir = new File(flashcardFolder); 
        return newdir.mkdirs();
    }
    
    private File createImageFile() throws IOException {
        // Create an image file name
    	String imageFileName = String.valueOf(System.currentTimeMillis());
        File image = File.createTempFile(
            imageFileName, 
            ".jpg", 
            new File(flashcardFolder)
        );
        return image;
    }
    
	protected void doCameraTraining() {
		createFlashcardTraningFolder();
		File f;
		try {
			f = createImageFile();
		} catch (IOException e) {
			e.printStackTrace();
			makeToast("create file for saving picture failed!");
			return;
		}
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
	    startActivityForResult(takePictureIntent, TAKE_PICTURE_REQUEST_CODE);
	}
	
	protected void doPictureTraining() {
        Intent pickPictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
        pickPictureIntent.setType("image/*");
		startActivityForResult(pickPictureIntent, PICK_PICTURE_REQUEST_CODE);
	}

	protected void doRobotCameraTraining() {
		// TODO Auto-generated method stub
		
	}

    private static final int TAKE_PICTURE_REQUEST_CODE = 1;
    private static final int PICK_PICTURE_REQUEST_CODE = 2;
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	super.onActivityResult(requestCode, resultCode, data);
    	if (requestCode == TAKE_PICTURE_REQUEST_CODE) {
    		if (resultCode == RESULT_OK) {
    			makeToast("picture saved!");
    			Uri picUri = data.getData();
    			makeToast("URI: " + picUri.getPath());
    		} else {
    			makeToast("training by camera has been cancelled!");
    		}
    	} else if (requestCode == PICK_PICTURE_REQUEST_CODE) {
    		if (resultCode == RESULT_OK) {
    			Uri selectedImage = data.getData();
    			
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(
                                   selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                
                makeToast("selected: " + filePath);
                
                doRequestTagForPicture(filePath);
    		} else {
    			makeToast("training by existing picture has been cancelled!");
    		}
    	}
	}
    
    private void doRequestTagForPicture(final String pictureFilePath) {
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Set tag");
		builder.setMessage("Input tag for selected picture:");
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		input.setText("anh");
		builder.setView(input);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String tag = input.getText().toString();
				doLearnFlashcard(pictureFilePath, tag);
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	protected void doLearnFlashcard(final String pictureFilePath, final String tag) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				String id = null;
				showProgress("learning flashcard...");
				try {
					id = RobotFlashcardRecognition.learnFlashcard(getRobot(), 
							pictureFilePath, tag);
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (id == null) {
					showInfoDialogFromThread("Error", "training flashcard from path '" 
									+ pictureFilePath + "' failed!");
				} else {
					showInfoDialogFromThread("Learn Flashcard Success!", "trained id: [" + id + "]");
				}
			}
		}).start();
	}

	private String[] tagsList;
	private int selectedTagIndex = 0;
	private String[] flashcardIdsList;
	private int selectedFlashcardIdIndex = 0;
	
	private void forgetFlashcard() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("getting all tags...");
				try {
					tagsList = RobotFlashcardRecognition.getAllTags(getRobot());
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (tagsList == null || tagsList.length==0) {
					makeToast("none tag available!");
					showInfoDialogFromThread("Tag List", "None tag available!\n");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						selectedTagIndex = 0;
						showSingleChoiceDialog("Select tag", tagsList, 0, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								selectedTagIndex = which;
							}
						},
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								doForgetFlashcard_prepare();
							}
						});
					}
				});
			}
		}).start();
	}

	private void doForgetFlashcard_prepare() {
		if (selectedTagIndex > tagsList.length) {
			return;
		}
		final String tagName = tagsList[selectedTagIndex];
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("get flashcards by tag [" + tagName + "]");
				try {
					flashcardIdsList = RobotFlashcardRecognition.getFlashcardsByTag(getRobot(), 
							tagName);
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (flashcardIdsList == null && flashcardIdsList.length==0) {
					makeToast("none flashcard available!");
					showInfoDialogFromThread("Flashcard ID List", 
							"None flashcard available!\n");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						selectedFlashcardIdIndex = 0;
						showSingleChoiceDialog("Flashcard ID List", flashcardIdsList, 0, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								selectedFlashcardIdIndex = which;
							}
						},
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								doForgetFlashcard();
							}
						});
					}
				});
			}
		}).start();
	}

	private void doForgetFlashcard() {
		if (selectedFlashcardIdIndex > flashcardIdsList.length) {
			return;
		}
		final String flashcardId = flashcardIdsList[selectedFlashcardIdIndex];
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result;
				showProgress("forgetting flashcard [id=" + flashcardId + "]...");
				try {
					result = RobotFlashcardRecognition.forgetFlashcard(getRobot(), flashcardId);
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Forget Flashcard", "Forget flashcard ID='" 
								+ flashcardId + "' failed!");
				} else {
					showInfoDialogFromThread("Forget Flashcard", "Forget flashcard ID='" 
							+ flashcardId + "' successful!");
				}
			}
		}).start();
	}
	
	@SuppressWarnings("unused")
	private void forgetFlashcardsByTag() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("getting all tags");
				try {
					tagsList = RobotFlashcardRecognition.getAllTags(getRobot());
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (tagsList == null || tagsList.length==0) {
					makeToast("none tag available!");
					showInfoDialogFromThread("Tag list", "None tag available!\n");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						selectedTagIndex = 0;
						showSingleChoiceDialog("Select tag", tagsList, 0, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								selectedTagIndex = which;
							}
						},
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								doForgetFlashcardsByTag();
							}
						});
					}
				});
			}
		}).start();
	}
	
	protected void doForgetFlashcardsByTag() {
		if (selectedTagIndex > tagsList.length) {
			return;
		}
		final String tagName = tagsList[selectedTagIndex];
		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result;
				showProgress("forgetting flashcards by tag [" + tagName + "]");
				try {
					result = RobotFlashcardRecognition.forgetFlashcardsByTag(getRobot(), tagName);
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Forget Flashcard By Tag", "Forget flashcards for tag='" 
								+ tagName + "' failed!");
				} else {
					showInfoDialogFromThread("Forget Flashcard By Tag", "Forget flashcards for tag='" 
							+ tagName + "' successful!");
				}
			}
		}).start();
	}

	private void getAllTags() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("getting all tags...");
				try {
					tagsList = RobotFlashcardRecognition.getAllTags(getRobot());
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (tagsList == null || tagsList.length==0) {
					makeToast("none tag available!");
					showInfoDialogFromThread("Tag list", "None tag available!\n");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showInfoListDialog("All tags", tagsList, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
					}
				});
			}
		}).start();
	}

	private void getFlashcardsByTag() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("getting all tags...");
				try {
					tagsList = RobotFlashcardRecognition.getAllTags(getRobot());
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (tagsList == null || tagsList.length==0) {
					makeToast("none tag available!");
					showInfoDialogFromThread("Tag list", "None tag available!\n");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						selectedTagIndex = 0;
						showSingleChoiceDialog("Select tag", tagsList, 0, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								selectedTagIndex = which;
							}
						},
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								doGetFlashcardsByTag();
							}
						});
					}
				});
			}
		}).start();
	}

	private void doGetFlashcardsByTag() {
		if (selectedTagIndex > tagsList.length) {
			return;
		}
		final String tagName = tagsList[selectedTagIndex];
		new Thread(new Runnable() {
			@Override
			public void run() {
				showProgress("getting flashcards by tag [" + tagName + "]");
				try {
					flashcardIdsList = RobotFlashcardRecognition.getFlashcardsByTag(getRobot(), 
							tagName);
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (flashcardIdsList == null || flashcardIdsList.length==0) {
					makeToast("none flashcard id available!");
					showInfoDialog("Flashcard ID List", "None flashcard id available!\n");
					return;
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showInfoListDialog("Flashcard ID List", flashcardIdsList, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
					}
				});
			}
		}).start();
	}

	private void clearDatabase() {
		showAlertDialog("Warning", 
		"Do you want to clear flashcard database ?", 
		null, 
		new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				doClearDatabase();
			}
		});
	}

	protected void doClearDatabase() {

		new Thread(new Runnable() {
			@Override
			public void run() {
				boolean result;
				showProgress("clearing database...");
				try {
					result = RobotFlashcardRecognition.clearDatabase(getRobot());
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Clear Database", 
							"Clear flashcard database failed!");
				} else {
					showInfoDialogFromThread("Clear Database", 
							"Flashcard database cleared!");
				}
			}
		}).start();
	}

	private void startRecognition() {
		showAlertDialog("Warning", 
		"Do you want to start flashcard recognition?", 
		null, 
		new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				doStartRecognition();
			}
		});
    }

    protected void doStartRecognition() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                boolean result = false;
                try {
					RobotTextToSpeech.say(getRobot(), "Please show me a picture after a beep!", 
							RobotTextToSpeech.ROBOT_TTS_LANG_EN);
					RobotTextToSpeech.say(getRobot(), "Hãy cho tôi xem một tấm ảnh sau tiếng bíp!",
	                		RobotTextToSpeech.ROBOT_TTS_LANG_VI);
					RobotAudioPlayer.beep(getRobot());
                } catch (final RobotException e) {
					e.printStackTrace();
					makeToast("say text failed! " + e.getMessage());
					return;
				}
                showProgress("start flashcard recognition...");
                try {
                	result = mFlashcardMonitor.start();
                	if (!result) {
                		makeToast("start flashcard recognition monitor failed!");
                	}
					result = RobotFlashcardRecognition.startRecognition(getRobot());
                } catch (final RobotException e) {
                	cancelProgress();
					e.printStackTrace();
					makeToast("start flashcard recognition failed! " + e.getMessage());
					return;
				}
                cancelProgress();
                if (result) {
                	startTimer();
                	makeToast("start flashcard recognition successful!");
                } else {
                	makeToast("start flashcard recognition failed!");
                }
            }
        }).start();
	}

	private void stopRecognition() {
		new Thread(new Runnable() {
					
			@Override
			public void run() {
				boolean result;
				showProgress("checking recognition status...");
				try {
					result = RobotFlashcardRecognition.isRecognizing(getRobot());
				} catch (RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast(e.getMessage());
					return;
				}
				cancelProgress();
				if (!result) {
					showInfoDialogFromThread("Is Recognizing?", 
							"Flashcard Recognition is not active!");
				} else {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							showAlertDialog("Warning", 
							"Do you want to stop flashcard recognition?", 
							null, 
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
									doStopRecognition();
								}
							});
						}
					});
				}
			}
		}).start();
    }

	private void doStopRecognition() {
        new Thread(new Runnable() {
            
            @Override
            public void run() {
                boolean result;
                showProgress("stopping flashcard recognition...");
				try {
					RobotLedAnimation.twinkleEyesStop(getRobot());
					RobotAudioPlayer.beep(getRobot());
					result = mFlashcardMonitor.stop();
                	if (!result) {
                		makeToast("stop flashcard recognition monitor failed!");
                	}
					result = RobotFlashcardRecognition.stopRecognition(getRobot());
				} catch (final RobotException e) {
					cancelProgress();
					e.printStackTrace();
					makeToast("stop flashcard recognition failed! " + e.getMessage());
					return;
				}
				cancelProgress();
                if (result) {
                	makeToast("stop flashcard recognition successful!");
                } else {
                	makeToast("stop flashcard recognition failed!");
                }
            }
        }).start();
	}
	
	protected void speakAloudFlashcardInfo(String detectedTag) {
		String textToSayEnglish = new String("");
		String textToSayVietnamese = new String("");
		
		for (Map.Entry<String, FlashcardInfo> entry : mFlashcardInfos.entrySet()) {
			String tag = entry.getKey();
			if (detectedTag.equals(tag)) {
				FlashcardInfo info = entry.getValue();
				textToSayEnglish = info.englishDescription;
				textToSayVietnamese = info.vietnameseDescription;
			}
		}
    	if(!textToSayEnglish.isEmpty()) {
    		try {
				RobotTextToSpeech.say(getRobot(), textToSayEnglish, 
						RobotTextToSpeech.ROBOT_TTS_LANG_EN);
    		} catch (final RobotException e) {
    			e.printStackTrace();
    			makeToast("say text failed! " + e.getMessage());
    			return;
    		}
    	}
    	if(!textToSayVietnamese.isEmpty()) {
    		try {
				RobotTextToSpeech.say(getRobot(), textToSayVietnamese, 
						RobotTextToSpeech.ROBOT_TTS_LANG_VI);
    		} catch (final RobotException e) {
    			e.printStackTrace();
    			makeToast("say text failed! " + e.getMessage());
    			return;
    		}
    	}
	}
    
	private static final int MSG_FLASHCARD_RECOGNIZED = 0;
	private static final int MSG_TIMEOUT = 1;
	
	final Messenger mMessenger = new Messenger(new FlashcardRecognitionHandler());
		
    class FlashcardRecognitionHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_FLASHCARD_RECOGNIZED:
            {
                final String tag = (String)msg.obj;
                new Thread(new Runnable() {					
					@Override
					public void run() {
		                stopTimer();
						doStopRecognition();
		            	speakAloudFlashcardInfo(tag);
		            	try {
							Thread.sleep(2);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}).start();
                break;
            }
            case MSG_TIMEOUT:
            {
                new Thread(new Runnable() {					
					@Override
					public void run() {
						stopTimer();
						doStopRecognition();
		            	try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
		            	try {
			            	RobotTextToSpeech.say(getRobot(), "You did not show me an invalid picture. Thank you!", 
			                		RobotTextToSpeech.ROBOT_TTS_LANG_EN);
			                RobotTextToSpeech.say(getRobot(), "Bạn chưa đưa ra được bức ảnh hợp lệ. Hãy thử lần sau.",
			                		RobotTextToSpeech.ROBOT_TTS_LANG_VI);
						} catch (RobotException e) {
							e.printStackTrace();
						}
					}
				}).start();
                break;
            }
            default:
                super.handleMessage(msg);
            }
        }
    }

	@Override
	public void onFlashcardRecognized(String tag) {
        try {
        	Message msg = Message.obtain(null, MSG_FLASHCARD_RECOGNIZED, tag);
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
	}

    protected void startTimer() {
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				makeToast("time out and none flash card recognition!");
				Message msg = Message.obtain(null, MSG_TIMEOUT, null);
		        try {
		            mMessenger.send(msg);
		        } catch (RemoteException e) {
		            e.printStackTrace();
		        }
			}
		}, DEFAULT_TIMEOUT_VALUE, DEFAULT_TIMEOUT_VALUE);
	}
    
    protected void stopTimer() {
    	if (mTimer != null) {
    		mTimer.cancel();
    	}
    }
}
