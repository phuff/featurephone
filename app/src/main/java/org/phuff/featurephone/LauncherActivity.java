package org.phuff.featurephone;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class LauncherActivity extends Activity {
    // Todo: read from storage the list of things
    // Display them
    private GridView grid;

    private void loadGridView(){
        PackageManager manager = getPackageManager();
        grid = (GridView)findViewById(R.id.gridview);
        final LauncherState state = StateHelper.getState(getApplication());
        final ArrayList<AppDetails> appDetails = new ArrayList<AppDetails>();
        for(AppTile appTile : state.appTiles) {
            try {
                ApplicationInfo info = manager.getApplicationInfo(appTile.name, 0);
                AppDetails details = new AppDetails();
                details.label = info.name;
                details.icon = info.loadIcon(manager);
                details.name = info.packageName;
                appDetails.add(details);
            } catch (PackageManager.NameNotFoundException e) {
                continue;
            }

        }
        ArrayAdapter<AppDetails> adapter = new ArrayAdapter<AppDetails>(this,
                R.layout.app_list_item,
                appDetails) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.app_list_item, null);
                }

                ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(appDetails.get(position).icon);

                TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label);
                appLabel.setText(appDetails.get(position).label);

                return convertView;
            }
        };

        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                // do nothing for now
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        final Button button = (Button) findViewById(R.id.apps_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showApps();
            }
        });
        loadGridView();
    }

    public void showApps(){
        Intent i = new Intent(this, AppSelectionActivity.class);
        startActivity(i);
    }
}
