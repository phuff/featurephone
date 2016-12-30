package org.phuff.featurephone;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by phuff on 6/1/16.
 */
public class LauncherState {
    @JsonProperty
    Map<String, AppTile> appTilesMap;

    LauncherState() {
        appTilesMap = new HashMap<String, AppTile>();
    }
}
