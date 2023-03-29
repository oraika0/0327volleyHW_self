package com.example.a0327volleyhw_self;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class MainActivity extends AppCompatActivity {
    TableLayout tableLayout;
    EditText et;
    String filename = "out.csv";
    String url="https://web.cs.wpi.edu/~cs1004/a16/Resources/SacramentoRealEstateTransactions.csv\n";

    private void sendAndRequestResponse(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  et.setText(response.toString());
                try {
                    FileOutputStream outfile = openFileOutput(filename, Context.MODE_APPEND);
                    outfile.write(response.getBytes(StandardCharsets.UTF_8));
                    outfile.close();
                    parsing_csvFile();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error){
            }
        });
        mRequestQueue.add(mStringRequest);

    }

    private void parsing_csvFile() {
        try {
            File file = getBaseContext().getFileStreamPath("out.csv");
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = "";
            String spiltBy = ",";

            while ((line = br.readLine()) != null) {
                String[] apartment = line.split(spiltBy);
                TableRow row = new TableRow(this);

                for (int ct = 0; ct < apartment.length; ct++) {
                    TextView tv = new TextView(this);
                    tv.setText(apartment[ct]);
                    row.addView(tv);
                }

                tableLayout.addView(row);
                System.out.println("street" + apartment[0] + ", city = " + apartment[1] + ", zip = " + apartment[2] + ", state = " + apartment[3] + "beds = " + apartment[4]);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (OutOfMemoryError e) {
            throw new RuntimeException("CSV file too large to parse.", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tableLayout = (TableLayout)findViewById(R.id.tl);
        et = (EditText)findViewById(R.id.et);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendAndRequestResponse();
            }
        });
    }
}
