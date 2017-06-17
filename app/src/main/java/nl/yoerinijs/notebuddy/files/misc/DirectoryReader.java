package nl.yoerinijs.notebuddy.files.misc;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.PatternSyntaxException;

/**
 * This class displays files in the NoteBuddy dir
 */
public class DirectoryReader {

    /**
     * This method returns a list of all files
     * @param folder
     * @param sort
     * @return
     * @throws PatternSyntaxException
     */
    @Nullable
    public static List<String> getFileNames(final String folder, final int sort) throws PatternSyntaxException
    {
        File fileDir = new File(folder);
        if(!fileDir.exists() || !fileDir.isDirectory()){
            return null;
        }

        String[] files = fileDir.list();
        if(files.length == 0){
            return null;
        }

        List<String> storedData = new ArrayList<String>();
        for(String file : files) {
            if(!file.contains("notebuddy") && !file.contains("yoerinijs")) {
                storedData.add(file);
            }
        }
        if(storedData.size() == 0) {
            return null;
        }
        if (sort != 0)
        {
            Collections.sort(storedData, String.CASE_INSENSITIVE_ORDER);
            if (sort < 0)
                Collections.reverse(storedData);
        }
        return storedData;
    }

}
