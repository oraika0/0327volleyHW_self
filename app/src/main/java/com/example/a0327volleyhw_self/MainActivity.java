package com.example.a0327volleyhw_self;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
    TableLayout tl;
    EditText et;
    String filename = "out.csv";
    //String url="https://web.cs.wpi.edu/~cs1004/a16/Resources/SacramentoRealEstateTransactions.csv";

    private void sendAndRequestResponse(){
        RequestQueue mRequestQueue = Volley.newRequestQueue(this);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, et.getText().toString(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("CSV",response);
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
            @Override
            public void onErrorResponse(VolleyError error){
                Log.d("onErrorResponse",error.toString());
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
            int no = 0;
            boolean firstrow = true;
           while(no != 986) {
               line = br.readLine();
               //System.out.println(line);
                TableRow row = new TableRow(this);
                String[] apartment = line.split(spiltBy);
                    TextView tv1 = new TextView(this);
                    TextView tv2 = new TextView(this);
                    TextView tv3 = new TextView(this);
                    TextView tv4 = new TextView(this);
                    if (firstrow) {
                        tv1.setText("No. ");
                        row.addView(tv1);
                        tv2.setText("Address ");
                        row.addView(tv2);
                        tv3.setText("Type ");
                        row.addView(tv3);
                        tv4.setText("price ");
                        row.addView(tv4);
                    } else {
                        tv1.setText(String.valueOf(no));
                        row.addView(tv1);
                        tv2.setText(" " +apartment[0] + "," + apartment[1] + "," + apartment[2] + "," + apartment[3]);
                        row.addView(tv2);
                        tv3.setText(" " +apartment[4] + "bed," + apartment[5] + " bath," + apartment[6] + "sqft");
                        row.addView(tv3);
                        tv4.setText(" " +"$" + apartment[9]);
                        row.addView(tv4);
                    }
                firstrow = false;
                no++;
                //System.out.println(String.valueOf(no));
                tl.addView(row);
            }
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tl = findViewById(R.id.tl);
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
