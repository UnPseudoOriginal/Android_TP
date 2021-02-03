package fr.esme.esme_map

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.AbsListView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity


class DevicesActivity : AppCompatActivity() {

    //Find BLE Devices variables
    private val bluetoothLeScanner = BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
    private var mScanning = false
    private val handler = Handler()
    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000

    private val devicesAround = java.util.ArrayList<BluetoothDevice>()
    // Implementing LeScanCallBack
    //private val leDeviceListAdapter: LeDeviceListAdapter? = null

    private lateinit var mListView : AbsListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_devices)

        //Lancement de recherche de devices
        scanLeDevice()

        mListView = findViewById<ListView>(R.id.DevicesList)
    }

    // Device scan callback.
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            if (result.device != null) {
                if (!devicesAround.contains(result.device)){
                    Log.d("TAG", "Ajout de device")
                    devicesAround.add(result.device)
                    Log.d("TAG", "J'ai découvert "+result.device.name)
                } else {
                    Log.d("TAG", "Device déjà inséré")
                }
            } else {
                Log.d("TAG", "Pas de device à ajouter")
            }

            //leDeviceListAdapter!!.addDevice(result.device)
            //leDeviceListAdapter.notifyDataSetChanged()
        }
    }

    private fun scanLeDevice() {
        if (!mScanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                mScanning = false
                Log.d("TAG", "Je m'arrête de scanner les gars")
                bluetoothLeScanner.stopScan(leScanCallback)
                showDevices()
            }, SCAN_PERIOD)
            mScanning = true

            Log.d("TAG", "Je me mets à scanner les gars")
            bluetoothLeScanner.startScan(leScanCallback)
        } else {
            mScanning = false
            Log.d("TAG", "Je n'ai pas scanner les gars")
            bluetoothLeScanner.stopScan(leScanCallback)
        }
    }

    private fun showDevices(){
        var devicesName = java.util.ArrayList<String>()
        devicesAround.forEach(){
            if(it.name != null){
                devicesName.add(it.name)
            }

        }
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, devicesName)
        mListView.adapter = adapter

    }
}