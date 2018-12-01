package com.example.christian.control_bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.bluetooth.BluetoothSocket;
import android.widget.SeekBar;
import android.widget.Toast;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import java.io.IOException;
import java.util.UUID;

public class control extends AppCompatActivity {
    Button btnIzquierda, btnDerecha, btnArriva,btnAbajo, btnAcelerar, btnDetener,btnBarredora;
    String address = null;
    private ProgressDialog progress;
    BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        //receive the address of the bluetooth device
        Intent newint = getIntent();
        address = newint.getStringExtra(DeviceList.EXTRA_ADDRESS);

        btnAbajo = (Button)findViewById(R.id.btn_abajo);
        btnArriva = (Button)findViewById(R.id.btn_arriva);
        btnDerecha = (Button)findViewById(R.id.btn_derecha);
        btnIzquierda = (Button)findViewById(R.id.btn_izquierda);

        btnDetener = (Button)findViewById(R.id.btn_detener);
        btnAcelerar = (Button)findViewById(R.id.btn_acelerar);
        btnBarredora = (Button)findViewById(R.id.btn_barredora);

        btnArriva.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                arriva();      //method to turn on
            }
        });

        btnAbajo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                abajo();   //method to turn off
            }
        });

        btnDerecha.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                derecha(); //close connection
            }
        });

        btnIzquierda.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                izquierda(); //close connection
            }
        });

        btnBarredora.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                barredora(); //close connection
            }
        });

        btnAcelerar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                acelerar(); //close connection
            }
        });

        btnDetener.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                detener(); //close connection
            }
        });
    }

    private void arriva()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("arriva".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void abajo()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("abajo".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void derecha()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("derecha".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void izquierda()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("izquierda".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void barredora()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("barredora".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void detener()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("detener".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void acelerar()
    {
        if (btSocket!=null)
        {
            try
            {
                btSocket.getOutputStream().write("acelerar".toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
    }

    private void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(control.this, "Connecting...", "Please wait!!!");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    btSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                finish();
            }
            else
            {
                msg("Connected.");
                isBtConnected = true;
            }
            progress.dismiss();
        }

    }
}

