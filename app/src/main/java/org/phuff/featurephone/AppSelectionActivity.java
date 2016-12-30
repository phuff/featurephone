package org.phuff.featurephone;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AppSelectionActivity extends Activity {

    private PackageManager manager;
    private ArrayList<AppDetails> apps;
    private ListView list;

    private void loadApps(){
        manager = getPackageManager();
        apps = new ArrayList<AppDetails>();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> availableActivities = manager.queryIntentActivities(i, 0);
        for(ResolveInfo ri:availableActivities){
            AppDetails app = new AppDetails();
            app.label = ri.loadLabel(manager);
            app.name = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(manager);
            apps.add(app);
        }
        Collections.sort(apps, new Comparator<AppDetails>() {

            @Override
            public int compare(AppDetails lhs, AppDetails rhs) {
                return lhs.label.toString().compareTo(rhs.label.toString());
            }
        });
    }

    private void loadListView(){
        list = (ListView)findViewById(R.id.apps_list);

        ArrayAdapter<AppDetails> adapter = new ArrayAdapter<AppDetails>(this,
                R.layout.app_list_item,
                apps) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.app_list_item, null);
                }

                ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(apps.get(position).icon);

                TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label);
                appLabel.setText(apps.get(position).label);

                return convertView;
            }
        };

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                LauncherState state = StateHelper.getState(getApplicationContext());
                String name = apps.get(pos).name.toString();
                if(!state.appTilesMap.containsKey(name)) {

                    AppTile newAppTile = new AppTile();
                    newAppTile.name = name;
                    newAppTile.order = state.appTilesMap.size();
                    state.appTilesMap.put(name, newAppTile);
                    StateHelper.setState(state, getApplicationContext());
                }
                Intent reloadIntent = new Intent();
                reloadIntent.setAction(LauncherActivity.ACTION_RELOAD_LAUNCHER_LIST);
                reloadIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(reloadIntent);
                finish();
            }
        });
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_selection);
        loadApps();
        loadListView();
    }
}
