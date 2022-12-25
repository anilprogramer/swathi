package com.example.sumit.smart_ring;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Start extends AppCompatActivity {
EditText et;
    Button bt,btbl;
    Intent next,nextbl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        et=(EditText)findViewById(R.id.et);
        bt=(Button)findViewById(R.id.button);
      //   next=new Intent(this,Login.class);
        next=new Intent(this,User_Home.class);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u=et.getText().toString();
                info.url=u;
                User_Home.user="8095286693";
                startActivity(next);
            }
        });
        btbl=(Button)findViewById(R.id.btnbl);
        nextbl=new Intent(this,DeviceListActivity.class);

        btbl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String u=et.getText().toString();
                info.url=u;
                startActivity(nextbl);
            }
        });
    }
}
