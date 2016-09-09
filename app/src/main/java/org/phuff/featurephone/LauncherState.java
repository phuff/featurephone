package org.phuff.featurephone;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by phuff on 6/1/16.
 */
public class LauncherState {
    @JsonProperty
    List<AppTile> appTiles;
    @JsonProperty
    Map<String, Boolean> appTilesMap;

    LauncherState() {
        appTiles = new ArrayList<AppTile>();
        appTilesMap = new HashMap<String, Boolean>();
    }
}
