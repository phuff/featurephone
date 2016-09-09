package org.phuff.featurephone;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

/**
 * Created by phuff on 6/1/16.
 */
public class StateHelper {
    private static ObjectMapper mapper = new ObjectMapper();
    public static LauncherState getState(Context context) {
        // Read file with LauncherState in it
        File f = new File(context.getFilesDir(), "launcherstate.json");
        if (f.exists() && !f.isDirectory()) {
            try {
                LauncherState state = mapper.readValue(f, LauncherState.class);
                return state;
            } catch (IOException e) {
                return new LauncherState();
            }
        }
        // If file is non-existent, make a blank LauncherState and return that
        return new LauncherState();
    }

    public static boolean setState(LauncherState state, Context context) {
        // Use objectmapper to write state to state file
        try {
            mapper.writeValue(new File(context.getFilesDir(), "launcherstate.json"), state);
        } catch (IOException e) {
            return false;
        }
        return true;
    }
}
