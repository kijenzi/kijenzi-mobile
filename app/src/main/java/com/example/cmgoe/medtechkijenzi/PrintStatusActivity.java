package com.example.cmgoe.medtechkijenzi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class PrintStatusActivity extends AppCompatActivity {
    BluetoothThread connection;
    StringBuilder messages;
    TextView statusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_status);
//
//        Bundle extras = getIntent().getExtras();
//        connection = (BluetoothThread) extras.getSerializable("btConnect");
//        System.out.println(connection.getState());
//        System.out.println(connection.isAlive());

        statusView = (TextView) findViewById(R.id.status_text);
        statusView.setMovementMethod(new ScrollingMovementMethod());
        messages = new StringBuilder();

        LocalBroadcastManager.getInstance(this).registerReceiver(mReciever, new IntentFilter("StatusIntent"));
    }

    BroadcastReceiver mReciever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("theCurrChar");
            messages.append(text);
            System.out.println(text + " here is the text!");
            statusView.setText(messages);
        }
    };
}
