package com.example.sumit.smart_ring;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
EditText etm,etp;
    Button btn;
    TextView tv,tvbt;
    Intent next;
    String mob="",pass="";
    int MY_PERMISSIONS_REQUEST_CALL_PHONE = 101;
    Intent nextuh;

    double lat=0.0,lon=0.0;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    public static final String LOCATION_PERMISSION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String LOCATION_PREF = "locationPref";
    Context context;Activity activity;





    final public static int SEND_SMS = 101;
    String msg1="";


    Handler bluetoothIn;
    final int handlerState = 0;        				 //used to identify handler message
    private StringBuilder recDataString = new StringBuilder();
    private BluetoothAdapter btAdapter = null;
    // SPP UUID service - this should work for most devices
    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    // String for MAC address
    private static String address;
    private BluetoothSocket btSocket = null;
    private ConnectedThread mConnectedThread;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;


    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        activity = MainActivity.this;
        checkLocationPermission(activity,context,LOCATION_PERMISSION,LOCATION_PREF);

        mediaPlayer = MediaPlayer.create(this, R.raw.song);
        tvbt=(TextView)findViewById(R.id.tvbt);


        bluetoothIn = new Handler() {
            public void handleMessage(android.os.Message msg) {
                if (msg.what == handlerState) {                                        //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    //   Toast.makeText(getApplicationContext(), ">>" + readMessage + "", Toast.LENGTH_LONG).show();

                    recDataString.append(readMessage);
                    tvbt.setText(readMessage);

boolean flag=false;
                    if(readMessage.equals("1"))
                    {

                        tvbt.setText("Help");
                        File FileDir =   new File(Environment.getExternalStorageDirectory(), "A");  // getDir();
                        if (!FileDir.exists() && !FileDir.mkdirs()) {
                            return;
                        }
                        FileDir.mkdirs();


                        try {

                            FileInputStream fis1 = new FileInputStream(FileDir + "/smart_ring_contact.txt");
                            byte b1[] = new byte[fis1.available()];
                            fis1.read(b1);
                            String s1 = new String(b1);

                            // Toast.makeText(getApplicationContext(), numbers, Toast.LENGTH_SHORT).show();
                            fis1.close();
                            Toast.makeText(getApplicationContext(),s1,Toast.LENGTH_LONG).show();

                             String a[]=s1.split("\n");
                             for(int i=1;i<a.length;i++) {

                                 Toast.makeText(getApplicationContext(),a[i],Toast.LENGTH_LONG).show();
                                 if(a[i].contains("="))

                                 {
                                     String aa[] = a[i].split("=");
if(flag==false) {
    call(aa[1]);
}
                                     flag=true;

                                     lat = GPSTracker.latitude;
                                     lon = GPSTracker.longitude;
                                     Toast.makeText(getApplicationContext(), lat + "" + lon, Toast.LENGTH_LONG).show();
                                     msg1 = "Hello " + aa[0] + ", I am in danger. need help,https://www.google.co.in/maps/@" + GPSTracker.latitude + "," + GPSTracker.longitude + ",15z?hl";
                                     //   msg1="smartwatch-"+lat+"-"+lon;
                                     new send_alert().execute();
                                     //   msg="Hello "+name+" need help https://www.google.co.in/?gfe_rd=cr&ei=SE4EWentI-_I8AexhZ_QCg&gws_rd=ssl#q="+GPSTracker.latitude+""+GPSTracker.longitude+"";

                                     //  msg="Hello "+name+"\nlat"+lat+",lon"+lon;
                                     Toast.makeText(getApplicationContext(), msg1, Toast.LENGTH_LONG).show();

                                     checkAndroidVersion(aa[1]);
                                     mediaPlayer.start();
                                 }
                             }

                        }catch(Exception e)
                        {


                        }

                    }





                }
            }
        };

        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get Bluetooth adapter
        checkBTState();


    }
    private void call(String mob) {

        try {

            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:"+mob));
            System.out.println("====before startActivity====");



            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);

                return;
            }

            startActivity(callIntent);
            System.out.println("=====getcallActivity==="+getCallingActivity());


        } catch (ActivityNotFoundException e) {
            Log.e("helloAndroid","Call failed",e);
        }
    }
    @Override
    public void onResume() {
        super.onResume();

        //Get MAC address from DeviceListActivity via intent
        Intent intent = getIntent();

        //Get the MAC address from the DeviceListActivty via EXTRA
        address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

        //create device and set the MAC address
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        try {
            btSocket = createBluetoothSocket(device);
        } catch (IOException e) {
            Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
        }
        // Establish the Bluetooth socket connection.
        try
        {
            btSocket.connect();
        } catch (IOException e) {
            try
            {
                btSocket.close();
            } catch (IOException e2)
            {
                //insert code to deal with this
            }
        }
        mConnectedThread = new ConnectedThread(btSocket);
        mConnectedThread.start();

        //I send a character when resuming.beginning transmission to check device is connected
        //If it is not an exception will be thrown in the write method and finish() will be called
        mConnectedThread.write("x");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        try
        {
            //Don't leave Bluetooth sockets open when leaving activity
            btSocket.close();


        } catch (IOException e2) {
            //insert code to deal with this
        }
    }
    public class send_alert extends AsyncTask<String , Void, String>
    {


        @Override
        protected String doInBackground(String... params) {
            String res="";
            try {
                URL u=new URL(info.url+"send_alert");
                JSONObject j=new JSONObject();

                j.put("lat",lat);

                j.put("lon",lon);

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


        }
    }




    //Checks that the Android device Bluetooth is available and prompts to be turned on if off
    private void checkBTState() {

        if(btAdapter==null) {
            Toast.makeText(getBaseContext(), "Device does not support bluetooth", Toast.LENGTH_LONG).show();
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }


    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {

        return  device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connecetion with BT device using UUID
    }
    //create new class for connect thread
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        //creation of the connect thread
        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                //Create I/O streams for connection
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }


        public void run() {
            byte[] buffer = new byte[256];
            int bytes;

            // Keep looping to listen for received messages
            while (true) {
                try {
                    bytes = mmInStream.read(buffer);        	//read bytes from input buffer
                    String readMessage = new String(buffer, 0, bytes);
                    // Send the obtained bytes to the UI Activity via handler
                    bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }
        }
        //write method
        public void write(String input) {
            byte[] msgBuffer = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(msgBuffer);                //write bytes over BT connection via outstream
            } catch (IOException e) {
                //if you cannot write, close the application
                Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
                finish();

            }
        }
    }



    private void checkLocationPermission(Activity activity, final Context context, final String Permission, final String prefName) {

        PermissionUtil.checkPermission(activity,context,Permission,prefName,
                new PermissionUtil.PermissionAskListener() {
                    @Override
                    public void onPermissionAsk() {


                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Permission},
                                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

                    }
                    @Override
                    public void onPermissionPreviouslyDenied() {
                        //show a dialog explaining permission and then request permission

                        showToast("Permission previously Denied.");

                        ActivityCompat.requestPermissions(MainActivity.this,
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
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);
            if(checkCallPhonePermission != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},SEND_SMS);
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
            sm.sendTextMessage(m, null, msg1, null, null);
Toast.makeText(getApplicationContext(),m+"--------------------"+msg1,Toast.LENGTH_LONG).show();


        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, "Your sms has failed...",
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();

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
