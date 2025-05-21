package com.example.sidebar;

import android.content.Context;
import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TFLitePriorityPredictor {
    private Interpreter interpreter;

    public TFLitePriorityPredictor(Context context) throws IOException {
        interpreter = new Interpreter(loadModelFile(context));
    }

    private MappedByteBuffer loadModelFile(Context context) throws IOException {
        FileInputStream input = new FileInputStream(context.getAssets().openFd("priority_model.tflite").getFileDescriptor());
        FileChannel fileChannel = input.getChannel();
        long startOffset = context.getAssets().openFd("priority_model.tflite").getStartOffset();
        long declaredLength = context.getAssets().openFd("priority_model.tflite").getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public int predict(float[] features) {
        float[][] output = new float[1][3]; // 3 classes: priorité 1, 2, 3
        ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * features.length).order(ByteOrder.nativeOrder());
        for (float f : features) inputBuffer.putFloat(f);
        inputBuffer.rewind();

        interpreter.run(inputBuffer, output);

        // Retourne l’index avec la probabilité la plus élevée
        int predicted = 0;
        float max = output[0][0];
        for (int i = 1; i < 3; i++) {
            if (output[0][i] > max) {
                max = output[0][i];
                predicted = i;
            }
        }
        return predicted + 1; // retourne priorité (1, 2 ou 3)
    }
}
