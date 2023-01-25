package com.example.projectapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.projectapp.ml.Model;

import org.json.JSONException;
import org.json.JSONObject;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Context context;
    private TFLiteClassifier classifier;

    EditText bt,bo,sh,hr;
    Button predict;
    TextView result;
    String url = "https://gg-kfxv.onrender.com/predict";
    Interpreter interpreter;

    public static void print2D(float mat[][])
    {
        // Loop through all rows
        for (int i = 0; i < mat.length; i++)

            // Loop through all elements of current row
            for (int j = 0; j < mat[i].length; j++)
                System.out.print(mat[i][j] + " ");
    }

    public static int findIndexOfMax(float[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt = findViewById(R.id.bt);
        bo = findViewById(R.id.bo);
        sh = findViewById(R.id.sh);
        hr = findViewById(R.id.hr);
        predict = findViewById(R.id.predict);
        result = findViewById(R.id.result);

//        try {
//            interpreter = new Interpreter(loadModelFile(),null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        try {
            classifier = new TFLiteClassifier(getAssets());
        } catch (IOException e) {
            e.printStackTrace();
        }



        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float[][] input = new float[1][4];
                input[0][0]=Float.parseFloat(bt.getText().toString());
                input[0][1]=Float.parseFloat(bo.getText().toString());
                input[0][2]=Float.parseFloat(sh.getText().toString());
                input[0][3]=Float.parseFloat(hr.getText().toString());

                float[][] output = classifier.classify(input);

                print2D(output);
                int f=findIndexOfMax(output[0]);

                float u = (float) f;
                result.setText("Result - "+u);

                Qlearning reco = new Qlearning(5,4);
                Log.d("2D Array", Arrays.deepToString(reco.qTable));
                int state= (int) u;

                int action = reco.getAction(state);


                reco.takeAction(state,action,context);

                bt.setText("");
                bo.setText("");
                sh.setText("");
                hr.setText("");
                result.setText("");
                Log.d("buffer","yo");
                Log.d("2D Array", Arrays.deepToString(reco.qTable));
//
//                float f =  doInference(bt.getText().toString(),bo.getText().toString(),sh.getText().toString(),hr.getText().toString());
//                result.setText("Result - "+f);

//                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
//                        new Response.Listener<String>() {
//                            @Override
//                            public void onResponse(String response) {
//
//                                try {
//                                    JSONObject jsonObject = new JSONObject(response);
//                                    String data = jsonObject.getString("r");
//                                    if(data.equals("1")){
//                                        result.setText("1");
//                                    }else if(data.equals("2")){
//                                        result.setText("2");
//                                    }else if(data.equals("3")){
//                                        result.setText("3");
//                                    }else if(data.equals("4")){
//                                        result.setText(("4"));
//                                    }else{
//                                        result.setText("0");
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }){
//
//                    @Override
//                    protected Map<String,String> getParams(){
//                        Map<String,String> params = new HashMap<String,String>();
//                        params.put("bt",bt.getText().toString());
//                        params.put("bo",bo.getText().toString());
//                        params.put("sh",sh.getText().toString());
//                        params.put("hr",hr.getText().toString());
//                        return params;
//                    }
//
//                };
//                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
//                queue.add(stringRequest);

            }
        });


    }

//    private MappedByteBuffer loadModelFile() throws IOException {
//        AssetFileDescriptor fileDescriptor=this.getAssets().openFd("model.tflite");
//        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
//        FileChannel fileChannel=inputStream.getChannel();
//        long startOffset=fileDescriptor.getStartOffset();
//        long declareLength=fileDescriptor.getDeclaredLength();
//        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
//    }
//
//    public float doInference(String bt,String bo,String sh,String hr)
//    {
//
//        float[] input = new float[6];
//        float[][] output = new float[1][4];
//
//        // Run decoding signature.
//        try (Interpreter interpreter = new Interpreter(loadModelFile())) {
//            Map<String, Object> inputs = new HashMap<>();
//            inputs.put("dense_6_input", input);
//
//            Map<String, Object> outputs = new HashMap<>();
//            outputs.put("dense_8", output);
//
//            interpreter.runSignature(inputs, outputs, "serving_default");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}