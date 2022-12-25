package com.example.sumit.smart_ring;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Display_Contact extends AppCompatActivity {
    TextView tv;
    public static String user="";
    Button ba, br,bs;
    String phone="",name="";

    double lat=0.0,lon=0.0;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String LOCATION_PREF = "locationPref";
    Context context;Activity activity;





    final public static int SEND_SMS = 101;
    String msg="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display__contact);


        tv = (TextView) findViewById(R.id.tv);

         ba=(Button)findViewById(R.id.btnAdd);
        br=(Button)findViewById(R.id.btnRem);
        bs=(Button)findViewById(R.id.btnSMS);
        Bundle ii=getIntent().getExtras();
        String item = ii.getString("item");

        String a[]=item.split("\n");

        phone=a[1];//ii.getString("phone");
//        phonenumber=ii.getString("number");
        name=a[0];//ii.getString("name");

        Toast.makeText(getApplicationContext(),item,Toast.LENGTH_SHORT).show();

        tv.setText(item);



        context = Display_Contact.this;
        activity = Display_Contact.this;
        checkLocationPermission(activity,context,LOCATION_PERMISSION,LOCATION_PREF);








        ba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     //   new add().execute();




                File FileDir =   new File(Environment.getExternalStorageDirectory(), "A");  // getDir();
                if (!FileDir.exists() && !FileDir.mkdirs()) {
                    return;
                }
                FileDir.mkdirs();

                String data=user+"="+name+"="+phone;

                String data1=name+"="+phone;



                File f=new File(FileDir+"/smart_ring_contact.txt");
                if(!f.exists())
                {
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }




                try
                {

                    FileInputStream fis1 = new FileInputStream(FileDir+"/smart_ring_contact.txt");
                    byte b1[]=new byte[fis1.available()];
                    fis1.read(b1);
                    String s1=new String(b1);

                    // Toast.makeText(getApplicationContext(), numbers, Toast.LENGTH_SHORT).show();
                    fis1.close();




                    data1=s1+"\n"+data1;


                    File fsrc =new File(FileDir+"/smart_ring_contact.txt");
                    FileOutputStream fosrc =new FileOutputStream(fsrc);
                    fosrc.write(data1.getBytes());
                    fosrc.close();

                    //   Toast.makeText(getApplicationContext(), "Saved Successfully ", Toast.LENGTH_SHORT).show();


                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }



            }
        });
        br.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
       //  new remove().execute();



                File FileDir =   new File(Environment.getExternalStorageDirectory(), "A");  // getDir();
                if (!FileDir.exists() && !FileDir.mkdirs()) {
                    return;
                }
                FileDir.mkdirs();

                String data=name+"="+phone;











                try
                {

                    FileInputStream fis1 = new FileInputStream(FileDir+"/smart_ring_contact.txt");
                    byte b1[]=new byte[fis1.available()];
                    fis1.read(b1);
                    String s1=new String(b1);

                    // Toast.makeText(getApplicationContext(), numbers, Toast.LENGTH_SHORT).show();
                    fis1.close();


                    String newdata="";

                    String arr[]=s1.split("\n");

                    for(int i=0;i<arr.length;i++)
                    {

                        if(data.equals(arr[i]))
                        {



                        }
                        else{

                            newdata+=arr[i]+"\n";

                        }

                    }





                    //  data=s1+"\n"+data;


                    File fsrc =new File(FileDir+"/smart_ring_contact.txt");
                    FileOutputStream fosrc =new FileOutputStream(fsrc);
                    fosrc.write(newdata.getBytes());
                    fosrc.close();

                    //   Toast.makeText(getApplicationContext(), "Saved Successfully ", Toast.LENGTH_SHORT).show();


                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             lat=GPSTracker.latitude;
                lon=GPSTracker.longitude;
                Toast.makeText(getApplicationContext(),lat+""+lon,Toast.LENGTH_LONG).show();
                 msg="Hello "+name+" need helphttps://www.google.co.in/maps/@"+GPSTracker.latitude+","+GPSTracker.longitude+",15z?hl";
              //   msg="Hello "+name+" need help https://www.google.co.in/?gfe_rd=cr&ei=SE4EWentI-_I8AexhZ_QCg&gws_rd=ssl#q="+GPSTracker.latitude+""+GPSTracker.longitude+"";

              //  msg="Hello "+name+"\nlat"+lat+",lon"+lon;
                checkAndroidVersion(phone);
            }
        });
    }



    public class add extends AsyncTask<String , Void, String>
    {


        @Override
        protected String doInBackground(String... params) {
            String res="";
            try {
                URL u=new URL(info.url+"add_contact");
                JSONObject j=new JSONObject();
                j.put("user",user);
                j.put("n",name);
                j.put("m",phone);

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

            String data=user+"="+name+"="+phone;

            String data1=name+"="+phone;



File f=new File(FileDir+"/smart_ring_contact.txt");
if(!f.exists())
{
    try {
        f.createNewFile();
    } catch (IOException e) {
        e.printStackTrace();
    }

}




            try
            {

                FileInputStream fis1 = new FileInputStream(FileDir+"/smart_ring_contact.txt");
                byte b1[]=new byte[fis1.available()];
                fis1.read(b1);
                String s1=new String(b1);

                // Toast.makeText(getApplicationContext(), numbers, Toast.LENGTH_SHORT).show();
                fis1.close();




               data1=s1+"\n"+data1;


                File fsrc =new File(FileDir+"/smart_ring_contact.txt");
                FileOutputStream fosrc =new FileOutputStream(fsrc);
                fosrc.write(data1.getBytes());
                fosrc.close();

                //   Toast.makeText(getApplicationContext(), "Saved Successfully ", Toast.LENGTH_SHORT).show();


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }
    public class remove extends AsyncTask<String , Void, String>
    {


        @Override
        protected String doInBackground(String... params) {
            String res="";
            try {
                URL u=new URL(info.url+"remove_contact");
                JSONObject j=new JSONObject();
                j.put("user",user);
                j.put("n",name);
                j.put("m",phone);

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

            String data=name+"="+phone;











            try
            {

                FileInputStream fis1 = new FileInputStream(FileDir+"/smart_ring_contact.txt");
                byte b1[]=new byte[fis1.available()];
                fis1.read(b1);
                String s1=new String(b1);

                // Toast.makeText(getApplicationContext(), numbers, Toast.LENGTH_SHORT).show();
                fis1.close();


                String newdata="";

                String arr[]=s1.split("\n");

                for(int i=0;i<arr.length;i++)
                {

                    if(data.equals(arr[i]))
                    {



                    }
                    else{

                        newdata+=arr[i]+"\n";

                    }

                }





              //  data=s1+"\n"+data;


                File fsrc =new File(FileDir+"/smart_ring_contact.txt");
                FileOutputStream fosrc =new FileOutputStream(fsrc);
                fosrc.write(newdata.getBytes());
                fosrc.close();

                //   Toast.makeText(getApplicationContext(), "Saved Successfully ", Toast.LENGTH_SHORT).show();


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }






    private void checkLocationPermission(Activity activity, final Context context, final String Permission,final String prefName) {

        PermissionUtil.checkPermission(activity,context,Permission,prefName,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onPermissionAsk() {


                        ActivityCompat.requestPermissions(Display_Contact.this,
                                new String[]{Permission},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    }
                    @Override
                    public void onPermissionPreviouslyDenied() {
                        //show a dialog explaining permission and then request permission

                        showToast("Permission previously Denied.");

                        ActivityCompat.requestPermissions(Display_Contact.this,
                                new String[]{Permission},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    }
                    @Override
                    public void onPermissionDisabled() {

                        askUserToAllowPermissionFromSetting();

                    }
                    @Override
                    public void onPermissionGranted() {

                        showToast("Permission Granted.");
                        getGpsLocation();
                    }
                });
    }

    private void askUserToAllowPermissionFromSetting() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("Permission Required:");

        // set dialog message
        alertDialogBuilder
                .setMessage("Kindly allow Permission from App Setting, without this permission app could not show maps.")
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
                        showToast("Permission forever Disabled.");
                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void getGpsLocation() {
        // check if GPS enabled
        GPSTracker gpsTracker = new GPSTracker(context);
        if (gpsTracker.getIsGPSTrackingEnabled())
        {
            showToast("Gps Values are:"+GPSTracker.latitude+" , "+GPSTracker.longitude);
        }
        else
        {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gpsTracker.showSettingsAlert();
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the task you need to do.
                    getGpsLocation();


                } else {


                    showToast("Permission denied,without permission can't access maps.");
                    // permission denied, boo! Disable the functionality that depends on this permission.
                }
                return;
            }
        }
    }


    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }








    public void checkAndroidVersion(String mobile){

        if (Build.VERSION.SDK_INT >= 23) {
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(Display_Contact.this, Manifest.permission.SEND_SMS);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(Display_Contact.this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS);
                return;
            }else{
                sendSms(mobile);
            }
        } else {
            sendSms(mobile);
        }
    }
    public void sendSms(String m)
    {


        try {

            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(m, null, msg, null, null);



        } catch (Exception ex) {
            Toast.makeText(Display_Contact.this, "Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();

        }


    }



}
