package com.example.sumit.smart_ring;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class signup extends AppCompatActivity {
    EditText etn,ete,etm,etcp,etp;
    Button btn;TextView tv;
String name="",email="",pass="",cpass="",mob="";
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    Intent bk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        etn=(EditText)findViewById(R.id.etn);
        ete=(EditText)findViewById(R.id.ete);
        etm=(EditText)findViewById(R.id.etm);
        etp=(EditText)findViewById(R.id.etp);
        etcp=(EditText)findViewById(R.id.etcp);
        btn=(Button)findViewById(R.id.btn);

        verifyStoragePermissions(this);

        bk=new Intent(this,Login.class);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        name=etn.getText().toString();
                email=ete.getText().toString();
                mob=etm.getText().toString();
                pass=etp.getText().toString();
                cpass=etcp.getText().toString();

                if(name.equals("") || email.equals("") || mob.equals("") || cpass.equals("") || pass.equals(""))
                {

                    Toast.makeText(getApplicationContext(),"Please Enter all the values",Toast.LENGTH_LONG).show();

                }
                else if(!pass.equals(cpass))
                {
                    Toast.makeText(getApplicationContext(),"Passwords are not matching",Toast.LENGTH_LONG).show();



                }
                else{


new signUp().execute();


                }
            }
        });

    }




    public class signUp extends AsyncTask<String , Void, String>
    {


        @Override
        protected String doInBackground(String... params) {
String res="";
            try {
                URL u=new URL(info.url+"add_user");
                JSONObject j=new JSONObject();
                j.put("n",name);
                j.put("m",mob);
                j.put("e",email);
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


            Toast.makeText(getApplicationContext(),status,Toast.LENGTH_LONG).show();
            File FileDir =   new File(Environment.getExternalStorageDirectory(), "A");  // getDir();
            if (!FileDir.exists() && !FileDir.mkdirs()) {
                return;
            }
            FileDir.mkdirs();

         String data=name+"="+email+"="+mob+"="+pass;
            try
            {
                File fsrc =new File(FileDir+"/smart_ring.txt");
                FileOutputStream fosrc =new FileOutputStream(fsrc);
                fosrc.write(data.getBytes());
                fosrc.close();

                //   Toast.makeText(getApplicationContext(), "Saved Successfully ", Toast.LENGTH_SHORT).show();


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            startActivity(bk);
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
