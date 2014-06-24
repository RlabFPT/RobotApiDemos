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
package com.fpt.robot.example.apis;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.fpt.robot.ui.RobotActivity;
@SuppressWarnings("unused")
public class RobotApiDemos extends RobotActivity 
			implements OnItemClickListener {
	private static final String TAG="RobotApiDemos";
	
	private ListView lvModuleList;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Intent intent = getIntent();
        String path = intent.getStringExtra("com.fpt.robot.example.apis.Path");
        
        if (path == null) {
            path = "";
        }
        
        lvModuleList = (ListView) findViewById(R.id.lvModuleList);
        
        List<Map<String, Object>> data = getData(path);
        SimpleAdapter adapter = new SimpleAdapter(this, data,
                android.R.layout.simple_list_item_1, new String[] { "title" },
                new int[] { android.R.id.text1 });
        lvModuleList.setAdapter(adapter);
        lvModuleList.setTextFilterEnabled(true);
        lvModuleList.setOnItemClickListener(this);
    }

    protected List<Map<String, Object>> getData(String prefix) {
        List<Map<String, Object>> myData = new ArrayList<Map<String, Object>>();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory("com.fpt.robot.category.DEMO_APP");

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (null == list)
            return myData;

        String[] prefixPath;
        String prefixWithSlash = prefix;
        
        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
            prefixWithSlash = prefix + "/";
        }
        
        int len = list.size();
        
        Map<String, Boolean> entries = new HashMap<String, Boolean>();

        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);
            CharSequence labelSeq = info.loadLabel(pm);
            String label = labelSeq != null
                    ? labelSeq.toString()
                    : info.activityInfo.name;
            
            if (prefixWithSlash.length() == 0 || label.startsWith(prefixWithSlash)) {
                
                String[] labelPath = label.split("/");

                String nextLabel = prefixPath == null ? 
                		labelPath[0] : labelPath[prefixPath.length];

                if ((prefixPath != null ? 
                		prefixPath.length : 0) == labelPath.length - 1) {
                    addItem(myData, nextLabel, activityIntent(
                            info.activityInfo.applicationInfo.packageName,
                            info.activityInfo.name));
                } else {
                    if (entries.get(nextLabel) == null) {
                        addItem(myData, nextLabel, 
                        		browseIntent(prefix.equals("") ? 
                        				nextLabel : prefix + "/" + nextLabel));
                        entries.put(nextLabel, true);
                    }
                }
            }
        }

        Collections.sort(myData, sDisplayNameComparator);
        
        return myData;
    }

    private final static Comparator<Map<String, Object>> sDisplayNameComparator =
        new Comparator<Map<String, Object>>() {
        private final Collator   collator = Collator.getInstance();

        public int compare(Map<String, Object> map1, Map<String, Object> map2) {
            return collator.compare(map1.get("title"), map2.get("title"));
        }
    };

    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }
    
    protected Intent browseIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, RobotApiDemos.class);
        result.putExtra("com.fpt.robot.example.apis.Path", path);
        return result;
    }

    protected void addItem(List<Map<String, Object>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

	@Override
	protected int getLayoutID() {
		return R.layout.activity_robot_api_demos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onItemClick(AdapterView<?> parent, View v, int pos, long id) {
		Map<String, Object> map = 
				(Map<String, Object>)parent.getAdapter().getItem(pos);
		Intent intent = (Intent) map.get("intent");
        //startActivity(intent);
        startRobotActivity(intent);
	}

	protected void settingView() {
		// TODO Auto-generated method stub
		
	}
}
