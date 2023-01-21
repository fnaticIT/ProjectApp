package com.example.projectapp;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import org.tensorflow.lite.Interpreter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class TFLiteClassifier {

    private static final String MODEL_PATH = "model.tflite";

    private Interpreter interpreter;
    private int inputSize;
    private int outputSize;

    public TFLiteClassifier(AssetManager assets) throws IOException {
        interpreter = createInterpreter(assets);
        inputSize = getInputSize();
        outputSize = getOutputSize();
    }

    public float[][] classify(float[][] input) {
        float[][] output = new float[1][outputSize];
        interpreter.run(input, output);
        return output;
    }

    private Interpreter createInterpreter(AssetManager assets) throws IOException {
        MappedByteBuffer byteBuffer = loadModelFile(assets);
        Interpreter.Options options = new Interpreter.Options();
        Interpreter interpreter = new Interpreter(byteBuffer, options);
        return interpreter;
    }

    private MappedByteBuffer loadModelFile(AssetManager assets) throws IOException {
        AssetFileDescriptor fileDescriptor = assets.openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    private int getInputSize() {
        // TODO: Implement this method to return the input size of the model
        return 4;
    }

    private int getOutputSize() {
        // TODO: Implement this method to return the output size of the model
        return 5;
    }

}
