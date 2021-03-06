package com.example.mike.btrgbcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class DeviceList extends AppCompatActivity {

    private ListView deviceList;
    private BluetoothAdapter my_bluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADRESS = "device_adress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        deviceList = (ListView)findViewById(R.id.listView);
        my_bluetooth = BluetoothAdapter.getDefaultAdapter();

        if(my_bluetooth==null){

            Toast.makeText(getApplicationContext(),"Bluetooth adapter is not exist",Toast.LENGTH_LONG).show();


        }

        if(!my_bluetooth.isEnabled()){

            Intent turnBtOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

        }

        pairedDevicesList();








    }

    private void pairedDevicesList(){

        pairedDevices = my_bluetooth.getBondedDevices();
        ArrayList list = new ArrayList();

        if (pairedDevices.size()>0){

            for(BluetoothDevice bt : pairedDevices){

                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else{

            Toast.makeText(getApplicationContext(), "No Paired Bluetooth Devices Found.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        deviceList.setAdapter(adapter);
        deviceList.setOnItemClickListener(item_click);

    }

    private AdapterView.OnItemClickListener item_click = new AdapterView.OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            String device_name = ((TextView) view).getText().toString();
            String mac_adress = device_name.substring(device_name.length()-17);
            Intent mainAvitvity = new Intent(DeviceList.this,MainActivity.class);
            mainAvitvity.putExtra(EXTRA_ADRESS,mac_adress);
            startActivity(mainAvitvity);


        }
    };



}
