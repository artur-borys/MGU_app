package com.example.mgumobilenet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.logging.Logger;

public class PickImageFromGallery extends ActivityResultContract<String, Uri> {
    private final Logger logger = Logger.getLogger(String.valueOf(PickImageFromGallery.class));
    @NonNull
    @Override
    public Intent createIntent(@NonNull Context context, String input) {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

        return chooserIntent;
    }

    @Override
    public Uri parseResult(int resultCode, @Nullable Intent intent) {
        return intent.getData();
    }
}
