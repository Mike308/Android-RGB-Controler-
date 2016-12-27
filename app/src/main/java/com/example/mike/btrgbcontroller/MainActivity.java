package com.example.mike.btrgbcontroller;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

   private float[] hsv = new float[3];
   private int[] rgb = new int[3];
    private ProgressDialog progress;
   private String adress = " ";
   private BluetoothAdapter my_bt = null;
   private BluetoothSocket sock_bt = null;
   private boolean is_connected = false;
   static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent deviceList = getIntent();
        adress = deviceList.getStringExtra(DeviceList.EXTRA_ADRESS);


        setContentView(R.layout.activity_main);

        Button b = (Button)findViewById(R.id.button);
        Button blueBtn = (Button)findViewById(R.id.button4);
        final TextView text = (TextView)findViewById(R.id.textView3);
        final TextView text2 = (TextView)findViewById(R.id.textView2);
        text.setText("Adress: "+adress);

        if(adress != null){

            new ConnectBT().execute();

        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                HSVColorPickerDialog cp = new HSVColorPickerDialog(MainActivity.this, 0xFF4488CC, new HSVColorPickerDialog.OnColorSelectedListener() {
                    @Override
                    public void colorSelected(Integer color) {

                        Color.colorToHSV(color,hsv);
                        int h = (int)hsv[0];
                        int s = (int)(hsv[1]*100);
                        int v = (int)(hsv[2]*100);
                        text2.setText("H: "+Integer.toString(h)+" S: "+Integer.toString(s)+" V: "+Integer.toString(v));
                        sendCommand("1^"+Integer.toString(h)+"^"+Integer.toString(s)+"^"+Integer.toString(v)+"\r\n");





                    }
                });


                cp.setTitle("Paleta HSV");

                cp.show();


            }
        });

        blueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent deviceListx = new Intent(getApplicationContext(),DeviceList.class);
                startActivity(deviceListx);

            }
        });

    }

    private void sendCommand(String cmd){

        if(sock_bt!=null){

            try{

                sock_bt.getOutputStream().write(cmd.getBytes());

            }catch (IOException e){

                Toast.makeText(getApplicationContext(),"Err: "+e.toString(),Toast.LENGTH_LONG);

            }

        }

    }

    private class ConnectBT extends AsyncTask<Void,Void,Void>{

        private boolean connectSucces = true;


        @Override
        protected void onPreExecute() {

            progress = ProgressDialog.show(MainActivity.this,"Connecting...","Please Wait...");

        }

        @Override
        protected Void doInBackground(Void... devices) {

            try{

                if(sock_bt==null || !is_connected){

                    my_bt = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice my_device = my_bt.getRemoteDevice(adress);
                    sock_bt = my_device.createInsecureRfcommSocketToServiceRecord(myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    sock_bt.connect();

                }

            }catch (IOException e){

                Toast.makeText(getApplicationContext(),"Err "+e.toString(),Toast.LENGTH_LONG).show();



            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if(!connectSucces){

                Toast.makeText(getApplicationContext(),"Connection Failed",Toast.LENGTH_LONG);
            }else{

                Toast.makeText(getApplicationContext(),"Connected",Toast.LENGTH_LONG);
                is_connected = true;
            }

            progress.dismiss();


        }
    }
}


