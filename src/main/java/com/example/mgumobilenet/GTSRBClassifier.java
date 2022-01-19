package com.example.mgumobilenet;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.mgumobilenet.ml.Model;
import com.example.mgumobilenet.ml.ModelAug2;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

public class GTSRBClassifier {

    ModelAug2 model;

    private final Logger logger = Logger.getLogger(String.valueOf(GTSRBClassifier.class));

    GTSRBClassifier(Context context) throws IOException {
        model = ModelAug2.newInstance(context);
    }

    public String classify(Bitmap image) {
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeOp(128, 128, ResizeOp.ResizeMethod.BILINEAR))
                        .build();
        TensorImage tensorImage = new TensorImage(DataType.FLOAT32);
        tensorImage.load(image);
        tensorImage = imageProcessor.process(tensorImage);

        ModelAug2.Outputs output = model.process(tensorImage.getTensorBuffer());

        float[] probabilities = output.getOutputFeature0AsTensorBuffer().getFloatArray();

        logger.info("Probabilities:\n" + Arrays.toString(probabilities));

        int mostProbableIndex = getMostProbableClass(output.getOutputFeature0AsTensorBuffer());
        String className = classNames[mostProbableIndex];

        logger.info("Most probable class: " + className);

        return className;
    }

    private int getMostProbableClass(TensorBuffer outputBuffer) {
        float[] list = outputBuffer.getFloatArray();
        float max = Float.NEGATIVE_INFINITY;
        int max_idx = -1;

        for (int i = 0; i < list.length; i++) {
            if (list[i] > max) {
                max_idx = i;
                max = list[i];
            }
        }

        return max_idx;
    }

    private Bitmap scaleBitmap(Bitmap image) {
        return Bitmap.createScaledBitmap(image, 128, 128, true);
    }

    private String[] classNames =
            {
                    "20",
                    "30",
                    "ban_overtake_big_car",
                    "cross",
                    "priority",
                    "give_way",
                    "stop",
                    "ban",
                    "ban_truck",
                    "wrong_way",
                    "alert",
                    "sharp_left",
                    "50",
                    "sharp_right",
                    "curvy",
                    "bumps",
                    "slippery",
                    "narrowing",
                    "road_work",
                    "traffic_signals",
                    "pedestrians",
                    "children_crossing",
                    "bicycles_crossing",
                    "60",
                    "ice/snow",
                    "wild_animals",
                    "end_of_limit",
                    "turn_right",
                    "turn_left",
                    "straight",
                    "straight_or_right",
                    "straight_or_left",
                    "keep_right",
                    "keep_left",
                    "70",
                    "roundabout_mandatory",
                    "end_of_no_overtake",
                    "end_of_no_overtake_big_car",
                    "80",
                    "drop_80",
                    "100",
                    "120",
                    "ban_overtake_car"
            };

}
