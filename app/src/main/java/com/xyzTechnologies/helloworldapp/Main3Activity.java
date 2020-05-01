package com.xyzTechnologies.helloworldapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Main3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        textView = (TextView) findViewById(R.id.status);
    }


    private TextView textView;
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                String string = bundle.getString(DownloadService.FILEPATH);
                int resultCode = bundle.getInt(DownloadService.RESULT);
                if (resultCode == RESULT_OK) {
                    Toast.makeText(Main3Activity.this,
                            "Download complete. Download URI: " + string,
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download done");
                } else {
                    Toast.makeText(Main3Activity.this, "Download failed",
                            Toast.LENGTH_LONG).show();
                    textView.setText("Download failed");

                    open();
                }
            }
        }
    };



    @Override
    protected void onResume() {
        super.onResume();
        //register dynamically or through manifest.xml create tag <receiver><receiver/>
        registerReceiver(receiver, new IntentFilter(
                DownloadService.NOTIFICATION));
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    public void onClick(View view) {

        Intent intent = new Intent(this, DownloadService.class);
        // add infos for the service which file to download and where to store
        intent.putExtra(DownloadService.FILENAME, "index.html");
        intent.putExtra(DownloadService.URL,
                "https://www.vogella.com/index.html");
        startService(intent);
        textView.setText("Service started");
    }


    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure, You wanted to make decision");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Toast.makeText(Main3Activity.this,
                                        "You clicked yes ,button",Toast.LENGTH_LONG).show();
                                Intent serv_intent = new Intent(Main3Activity.this, DownloadService.class);
                                // add infos for the service which file to download and where to store
                                serv_intent.putExtra(DownloadService.FILENAME, "index.html");
                                serv_intent.putExtra(DownloadService.URL,
                                        "https://www.vogella.com/index.html");
                                startService(serv_intent);
                                textView.setText("Service started");
                            }
                        });

        alertDialogBuilder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
