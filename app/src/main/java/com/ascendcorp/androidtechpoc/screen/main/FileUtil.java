package com.ascendcorp.androidtechpoc.screen.main;

import android.content.Context;

import java.io.File;
import java.io.IOException;

/**
 * Created by hungp on 1/12/2017.
 */

public class FileUtil {

    public static File createTempFileInCache(Context context, String prefix, String suffix) {
        try {
            File outputDir = context.getCacheDir();
            return File.createTempFile(prefix, suffix, outputDir);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
