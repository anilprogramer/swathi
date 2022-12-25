package com.example.sumit.smart_ring;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class User_Home extends AppCompatActivity {
    ListView listView;
    Button btn;
    ArrayList<String> StoreContacts ;
    ArrayAdapter<String> arrayAdapter ;
    Intent intent;
    Cursor cursor ;
    public  static final int RequestPermissionCode  = 1 ;
    String name;
    String phonenumber ;
    public static String user="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__home);


        listView= (ListView) findViewById(R.id.listview);
        btn= (Button) findViewById(R.id.btnAC);

        StoreContacts = new ArrayList<String>();

        EnableRuntimePermission();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetContactsIntoArrayList();

                arrayAdapter = new ArrayAdapter<String>(
                        User_Home.this,
                        R.layout.simlpe_list_item,
                        R.id.textView, StoreContacts
                );

                listView.setAdapter(arrayAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String item=parent.getItemAtPosition(position).toString();
                        intent=new Intent(User_Home.this,Display_Contact.class);
                        Display_Contact.user=user;
                        intent.putExtra("item",item);
//                        intent.putExtra("name",name);
                        //intent.putExtra("phone",listView.getItemAtPosition(position).toString());
                        //   intent.putExtra("number",listView.getItemAtPosition(position).toString());
                        startActivity(intent);


                    }
                });


            }
        });




    }
    public void GetContactsIntoArrayList(){

        cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (cursor.moveToNext()) {

            name= cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            phonenumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


            StoreContacts.add(name + " \n" + phonenumber);


        }



        cursor.close();

    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(
                User_Home.this,
                Manifest.permission.READ_CONTACTS))
        {

            Toast.makeText(User_Home.this,"CONTACTS permission allows us to Access CONTACTS app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(User_Home.this,new String[]{
                    Manifest.permission.READ_CONTACTS}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(User_Home.this,"Permission Granted, Now your application can access CONTACTS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(User_Home.this,"Permission Canceled, Now your application cannot access CONTACTS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }

}
