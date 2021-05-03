package com.example.receiptas.ui.scan_receipt;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;

public class ImagesGallery {

    public static ArrayList<String> listOfImages(Context context) {
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage;
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};

        String orderBy = MediaStore.Video.Media.DATE_MODIFIED;
        cursor = context.getContentResolver().query(uri, projection, null,
                null, orderBy + " DESC");

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            File imageFile = new File(absolutePathOfImage);
            if (imageFile.exists()) {
                listOfAllImages.add(absolutePathOfImage);
            }
        }

        cursor.close();

        return listOfAllImages;
    }
}
