package com.example.pwta_proj2;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class Utils {
    @SuppressLint("Range")
    public static String getNameFromURI(@NonNull Context context, @NonNull Uri uri) {
        String result = null;
        try (Cursor c = context.getContentResolver().query(uri, null, null, null, null)) {
            c.moveToFirst();
            result = c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
        } catch (Exception e) {
            Toast.makeText(context, "Error occurred while getting file name from selected filePath.", Toast.LENGTH_LONG).show();            // error occurs
        }
        return result;
    }
}
