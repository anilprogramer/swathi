package com.example.sumit.smart_ring;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {
    EditText etm,etp;
    Button btn;
    TextView tv,tvbt;
    Intent next;
    String mob="",pass="";

    Intent nextuh;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        verifyStoragePermissions(this);
        etm=(EditText)findViewById(R.id.etm);
        etp=(EditText)findViewById(R.id.etp);
        btn=(Button)findViewById(R.id.btn);
        tv=(TextView)findViewById(R.id.textView2);
        next=new Intent(this,signup.class);
        nextuh=new Intent(this,User_Home.class);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(next);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mob=etm.getText().toString();
                pass=etp.getText().toString();
                new login().execute();
            }
        });
    }
    public class login extends AsyncTask<String , Void, String>
    {


        @Override
        protected String doInBackground(String... params) {
            String res="";
            try {
                URL u=new URL(info.url+"login_check");
                JSONObject j=new JSONObject();

                j.put("m",mob);

                j.put("p",pass);

                res=HttpClientConnection.HttpExecute(u,j);





            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return res;
        }


        @Override

        protected  void onPostExecute(String status)

        {
            if(status.equals("ok"))
            {
                Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_LONG).show();
                User_Home.user=mob;
                startActivity(nextuh);

            }
            else{

                Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_LONG).show();

            }

        }
    }

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

}
