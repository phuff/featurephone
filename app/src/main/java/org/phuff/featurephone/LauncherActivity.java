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
import android.widget.ListPopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class LauncherActivity extends Activity {
    // Todo: read from storage the list of things
    // Display them
    private GridView grid;
    public static final String ACTION_RELOAD_LAUNCHER_LIST = "org.phuff.featurephone.RELOAD_LAUNCHER_LIST";
    private ListPopupWindow mListPopupWindow;

    private void loadListView(ListPopupWindow listPopupWindow){
        PackageManager manager = getPackageManager();
        final LauncherState state = StateHelper.getState(getApplication());
        final ArrayList<AppDetails> appDetailsList = new ArrayList<AppDetails>();
        final ArrayList<AppTile> sortedAppTiles = new ArrayList<AppTile>(state.appTilesMap.values());
        Collections.sort(sortedAppTiles, new Comparator<AppTile>() {
            @Override
            public int compare(AppTile lhs, AppTile rhs) {
                return lhs.order < rhs.order ? 1 : -1;
            }
        });
        for(AppTile appTile : sortedAppTiles) {
            try {
                ApplicationInfo info = manager.getApplicationInfo(appTile.name, 0);
                AppDetails details = new AppDetails();
                details.label = manager.getApplicationLabel(info);
                details.icon = info.loadIcon(manager);
                details.name = info.packageName;
                appDetailsList.add(details);
            } catch (PackageManager.NameNotFoundException e) {
                continue;
            }

        }
        ArrayAdapter<AppDetails> adapter = new ArrayAdapter<AppDetails>(this,
                R.layout.app_list_item,
                appDetailsList) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null){
                    convertView = getLayoutInflater().inflate(R.layout.app_list_item, null);
                }

                ImageView appIcon = (ImageView)convertView.findViewById(R.id.item_app_icon);
                appIcon.setImageDrawable(appDetailsList.get(position).icon);

                TextView appLabel = (TextView)convertView.findViewById(R.id.item_app_label);
                appLabel.setText(appDetailsList.get(position).label);
                return convertView;
            }
        };

        listPopupWindow.setAdapter(adapter);
        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
                AppDetails ad = appDetailsList.get(pos);
                Intent i = getPackageManager().getLaunchIntentForPackage(ad.name.toString());
                startActivity(i);
            }
        });
        /*listPopupWindow.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> av, View v, int pos, long id) {
                final AppDetails ad = appDetails.get(pos);
                new AlertDialog.Builder(LauncherActivity.this)
                        .setTitle("Remove " + ad.label.toString() + "?")
                        .setMessage("Do you really want to remove " + ad.label.toString() + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                LauncherState state = StateHelper.getState(getApplicationContext());
                                if (state.appTilesMap.containsKey(ad.name)) {
                                    state.appTilesMap.remove(ad.name);
                                    StateHelper.setState(state, getApplicationContext());
                                }
                                loadListView((GridView) findViewById(R.id.gridview));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });*/
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
        mListPopupWindow = new ListPopupWindow(LauncherActivity.this);
        mListPopupWindow.setAnchorView(button);
        mListPopupWindow.setContentWidth(300);
        mListPopupWindow.getBackground().setAlpha(179); // Goes from 0-255 255 * .7 = 178.5
        loadListView(mListPopupWindow);

    }

    public void showApps(){
        mListPopupWindow.show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(ACTION_RELOAD_LAUNCHER_LIST.equals(intent.getAction())) {
        }
    }
}
