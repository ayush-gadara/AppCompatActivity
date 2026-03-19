package com.example.appcompatactivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button btnLoad;
    TextView txtResult;

    String url = "http://10.0.2.2/data.php"; // your PHP API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnLoad = findViewById(R.id.btnLoad);
        txtResult = findViewById(R.id.txtResult);

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url1 = new URL(url);
                            HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
                            conn.connect();

                            BufferedReader br = new BufferedReader(
                                    new InputStreamReader(conn.getInputStream())
                            );

                            String line;
                            StringBuilder result = new StringBuilder();

                            while ((line = br.readLine()) != null) {
                                result.append(line);
                            }

                            JSONArray jsonArray = new JSONArray(result.toString());
                            final StringBuilder data = new StringBuilder();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                String name = obj.getString("name");
                                String email = obj.getString("email");

                                data.append("Name: ").append(name).append("\n");
                                data.append("Email: ").append(email).append("\n\n");
                            }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtResult.setText(data.toString());
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    txtResult.setText("Error loading data");
                                }
                            });
                        }
                    }
                }).start();
            }
        });
    }
}