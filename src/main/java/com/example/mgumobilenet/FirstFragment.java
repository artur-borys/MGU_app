package com.example.mgumobilenet;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mgumobilenet.databinding.FragmentFirstBinding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class FirstFragment extends Fragment {

    private final Logger logger = Logger.getLogger(String.valueOf(FirstFragment.class));

    private FragmentFirstBinding binding;
    private GTSRBClassifier gtsrbClassifier;

    private ActivityResultLauncher<String> mGetImage = registerForActivityResult(
            new PickImageFromGallery(),
            result -> {
                try {
                    InputStream inputStream = this.getContext().getContentResolver().openInputStream(result);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    showImage(bitmap);
                    logger.info("User selected image " + result.toString());
                    logger.info("Selected images byte size: " + bitmap.getByteCount());
                    String className = gtsrbClassifier.classify(bitmap);
                    showClassName(className);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
    );

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        try {
            gtsrbClassifier = new GTSRBClassifier(getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGetImage.launch("image/*");
            }
        });
    }

    private void showImage(Bitmap imageBitmap) {
        binding.imageView.setImageBitmap(imageBitmap);
        binding.imageView.setVisibility(View.VISIBLE);
    }

    private void showClassName(String className) {
        binding.textView.setText("Klasa: " + className);
        binding.textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}