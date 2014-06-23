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
package com.fpt.robot.example.apis;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fpt.robot.ui.RobotActivity;
/**
 * 
 * @author Robot Team (FTI)
 * 
 */
public abstract class RobotApiDemoActivity extends RobotActivity {

    private ProgressDialog progressDialog = null;
    
    protected void showProgress(final String message) {
        //Log.d(TAG, "showProgress('" +message+ "')");
        runOnUiThread(new Runnable() {
            public void run() {
                if (progressDialog == null) {
                    progressDialog = new ProgressDialog(RobotApiDemoActivity.this);
                }
                // no title
                if (message != null) {
                    progressDialog.setMessage(message);
                }
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        });
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    protected void cancelProgress() {
        //Log.d(TAG, "cancelProgress()");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDialog != null) {
                    //progressDialog.cancel();
                    progressDialog.dismiss();
                }
            }
        });
    }

	protected void showInputDialog(String title, String message,
			DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		if (message != null && !message.isEmpty()) {
			builder.setMessage(message);
		}
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		builder.setView(input);
		builder.setPositiveButton("OK", positiveListener);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	
	protected void showSingleChoiceDialog(String title, CharSequence[] items, int checkedItem,
			DialogInterface.OnClickListener choiceListener,
			DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		builder.setSingleChoiceItems(items, checkedItem, choiceListener);
		builder.setPositiveButton("OK", positiveListener);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	protected void showMultiChoiceDialog(String title, CharSequence[] items, boolean[] checkedItems,
			DialogInterface.OnMultiChoiceClickListener choiceListener,
			DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		builder.setMultiChoiceItems(items, checkedItems, choiceListener);
		builder.setPositiveButton("OK", positiveListener);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}
	
	protected void showInfoListDialog(String title, CharSequence[] items,
			DialogInterface.OnClickListener choiceListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		builder.setItems(items, choiceListener);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	protected void showAlertDialog(String title, String message, 
			View customView, DialogInterface.OnClickListener positiveListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		if (message != null && !message.isEmpty()) {
			builder.setMessage(message);
		}
		if (customView != null) {
			builder.setView(customView);
		}
		builder.setPositiveButton("OK", positiveListener);
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.show();
	}

	protected void showAlertDialog(String title, String message, 
			View customView, DialogInterface.OnClickListener positiveListener,
			DialogInterface.OnClickListener negativeListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		if (message != null && !message.isEmpty()) {
			builder.setMessage(message);
		}
		if (customView != null) {
			builder.setView(customView);
		}
		builder.setPositiveButton("Yes", positiveListener);
		builder.setNegativeButton("No", negativeListener);
		builder.show();
	}
	
	protected void showInfoDialog(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		if (title != null && !title.isEmpty()) {
			builder.setTitle(title);
		}
		if (message != null && !message.isEmpty()) {
			builder.setMessage(message);
		}
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}
	
	protected void showInfoDialogFromThread(final String title, final String message) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showInfoDialog(title, message);
			}
		});
	}
	
	protected void makeToast(final String toast) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(RobotApiDemoActivity.this, toast, 
						Toast.LENGTH_SHORT).show();
			}
		});
	}


	/* (non-Javadoc)
	 * @see com.fpt.robot.ui.RobotActivity#settingView()
	 */
	protected void settingView() {
		
	}
}
