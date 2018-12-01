package com.example.christian.control_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class control extends AppCompatActivity {

     BluetoothAdapter mBluetoothAdapter;
    private String TAG ="rugal";
    boolean switchState ;
    SwitchCompat switchOnOff;

    String direccion_dispositivo;
    String nombre_dispositivo;
    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBrodcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_LOCAL_NAME, mBluetoothAdapter.ERROR);
                switch (state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    @Override
    protected void onDestroy() {

        super.onDestroy();
        unregisterReceiver(mBrodcastReceiver1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);
         switchOnOff =(SwitchCompat) findViewById(R.id.switch_OnOff);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        switchOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchState = switchOnOff.isChecked();
                Log.i("Rugal",""+switchState);
                if(switchState)
                    enableDisableBT();

            }
        });
    }

    private void enableDisableBT() {

        if(mBluetoothAdapter ==null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities");
        }
            if(!mBluetoothAdapter.isEnabled()  )
            {

              Intent enableBTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
              startActivity(enableBTintent);

                IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mBrodcastReceiver1,BTIntent);
            }



    }
}
