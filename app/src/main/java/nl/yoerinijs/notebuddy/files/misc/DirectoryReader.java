package nl.yoerinijs.notebuddy.files.misc;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.PatternSyntaxException;

/**
 * This class displays files in the NoteBuddy dir
 */
public class DirectoryReader {

    private static final String LOG_TAG = "Directory Reader";

    /**
     * This method returns a list of all files
     * @param folder
     * @param sort
     * @return
     * @throws PatternSyntaxException
     */
    @Nullable
    public static ArrayList<String> getFileNames(final String folder, final int sort) throws PatternSyntaxException
    {
        ArrayList<String> storedData = new ArrayList<String>();
        File fileDir = new File(folder);
        if(!fileDir.exists() || !fileDir.isDirectory()){
            return null;
        }

        String[] files = fileDir.list();

        if(files.length == 0){
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            // Some users report that NoteBuddy in some cases displays full activity paths (e.g. nl.yoerinijs.notebuddy.activities.MainActivity).
            // Therefore, it will be checked here whether the file contains certain words in order to filter them out.
            if(!files[i].contains("notebuddy") && !files[i].contains("yoerinijs")) {
                storedData.add(files[i]);
            }
        }

        if (storedData != null) {
            if (storedData.size() == 0) {
                return null;
            }
        }

        if (sort != 0)
        {
            Collections.sort(storedData, String.CASE_INSENSITIVE_ORDER);
            if (sort < 0)
                Collections.reverse(storedData);
        }

        // Log stored data
        Log.d(LOG_TAG, storedData.toString());

        // Return data
        return storedData;
    }

}
