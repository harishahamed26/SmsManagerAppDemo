package com.harish.smsmanagerappdemo;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    String SENT="mgs_sent";
    String DELIVERY="msg_devivered";
    PendingIntent sendIntent,deliveryInttent;
    BroadcastReceiver sendBR,deliveyBR;

    @Override
    protected void onResume() {
        super.onResume();
        sendBR=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "mgs sent", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "null pdu", Toast.LENGTH_SHORT).show();
                        break;

                }

            }

        };
        deliveyBR=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "delivered", Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "sms not delivered", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        };
        registerReceiver(sendBR,new IntentFilter(SENT));
        registerReceiver(deliveyBR,new IntentFilter(DELIVERY));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(deliveyBR);
        unregisterReceiver(sendBR);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText sms,phno;
        sms=(EditText)findViewById(R.id.sms_message);
        phno=(EditText)findViewById(R.id.tellno);
        Button sms_butt=(Button)findViewById(R.id.sms_button);
        sendIntent=PendingIntent.getBroadcast(MainActivity.this,0,new Intent(SENT),0);
        deliveryInttent=PendingIntent.getBroadcast(MainActivity.this,0,new Intent(DELIVERY),0);



        sms_butt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=sms.getText().toString();
                String phoneNumber=phno.getText().toString();

                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED)
                {

                    int MY_PERMISSION=1;
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSION);
                }
                else
                {
                    SmsManager smsManager=SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNumber,null,message,sendIntent,deliveryInttent);

                }

            }
        });
    }

}
