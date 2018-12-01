package com.example.christian.control_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.Set;

import java.util.ArrayList;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.bluetooth.BluetoothDevice;
import android.widget.AdapterView;
import android.widget.TextView;


public class DeviceList extends AppCompatActivity {
    Button btnPaired;
    ListView devicelist;
    private BluetoothAdapter myBluetooth = null;
    private String TAG ="rugal";
    boolean switchState ;
    SwitchCompat switchOnOff;
    private Set pairedDevices;

    String direccion_dispositivo;
    String nombre_dispositivo;
    public static String EXTRA_ADDRESS = "device_address";//va la direccion del dispositivo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_device_list);
        btnPaired = (Button)findViewById(R.id.button);
        devicelist = (ListView)findViewById(R.id.listView);
        switchOnOff = (SwitchCompat) findViewById(R.id.switch_OnOff);


        switchOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchState = switchOnOff.isChecked();
                Log.i("Rugal", "" + switchState);
                if (switchState)
                    enableDisableBT();

            }
        });

        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        btnPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                pairedDevicesList(); //method that will be called
            }
        });


    }



    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mBrodcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(myBluetooth.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_LOCAL_NAME, myBluetooth.ERROR);
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

    private void enableDisableBT() {

        if(myBluetooth ==null) {
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities");
        }
        if(!myBluetooth.isEnabled()  )
        {

            Intent enableBTintent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTintent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBrodcastReceiver1,BTIntent);
        }
    }

    private void pairedDevicesList()
    {
        Set <BluetoothDevice> pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
            // Make an intent to start next activity.
            Intent i = new Intent(DeviceList.this, control.class);
            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };
}

