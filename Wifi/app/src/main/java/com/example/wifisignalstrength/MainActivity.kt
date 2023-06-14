@file:Suppress("DEPRECATION")

package com.example.wifisignalstrength

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.wifisignalstrength.data.AccessPointDatabase
import com.example.wifisignalstrength.model.AccesPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.log10
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    private lateinit var wifiManager: WifiManager
    private lateinit var textViewSsid: TextView
    private lateinit var textViewFrequency: TextView
    private lateinit var textViewSpeedLink: TextView
    private lateinit var textViewRssi: TextView
    private lateinit var textViewDistance: TextView
    private lateinit var saveButton: Button
    private lateinit var historyButton: Button
    private lateinit var scanButton: Button
    private lateinit var database: AccessPointDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            } else {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1
                )
            }
        }

        setContentView(R.layout.activity_main)

        database = Room.databaseBuilder(
            applicationContext,
            AccessPointDatabase::class.java,
            "accessPoint_database"
        ).build()

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        textViewSsid = findViewById(R.id.ssid_value)
        textViewFrequency = findViewById(R.id.textView_frequency)
        textViewSpeedLink = findViewById(R.id.textView_speedlink)
        textViewRssi = findViewById(R.id.textView_rssi)
        textViewDistance = findViewById(R.id.textView_distance)
        saveButton = findViewById(R.id.save_button)
        historyButton = findViewById(R.id.history_button)
        scanButton = findViewById(R.id.scan_button)


        checkWifiStatus(this)


        scanButton.setOnClickListener {
            wifiParametes()
        }
        saveButton.setOnClickListener {
            lifecycleScope.launch {
                saveInformation()
            }
        }
        historyButton.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun checkWifiStatus(context: Context) {
        if (!wifiManager.isWifiEnabled) {
            textViewSsid.text = "Brak połączenia z siecią"
            textViewFrequency.visibility = View.GONE
            textViewRssi.visibility = View.GONE
            textViewSpeedLink.visibility = View.GONE
            textViewDistance.visibility = View.GONE
            hideButtons()
            Toast.makeText(context, "Problem z połączeniem.\nWłącz wifi!", Toast.LENGTH_LONG).show()
        }
        else{
            wifiParametes()
        }
    }

    private fun hideButtons() {
        saveButton.isEnabled = false
        scanButton.isEnabled = false
    }

    private fun wifiParametes() {
//        Użycie przestarzałej metody "deprecated", do uzyskania informacji na temat wifi
        val wifiInfo = wifiManager.connectionInfo
        textViewSsid.text = wifiInfo.ssid
        textViewFrequency.text = getString(R.string.frequency, wifiInfo.frequency.toString())
        textViewSpeedLink.text = getString(R.string.speedlink, wifiInfo.linkSpeed.toString())
        textViewRssi.text = getString(R.string.RSSI, wifiInfo.rssi.toString())
        textViewDistance.text = getString(R.string.distance, estimateDistance(wifiInfo.rssi,wifiInfo.frequency.toDouble()))
    }

    private fun estimateDistance(rssi: Int, freq: Double): String{
        return  10.0.pow((27.55 - (20 * log10(freq)) + abs(rssi)) / 20.0)
            .toBigDecimal()
            .setScale(0, RoundingMode.HALF_UP)
            .toString()
    }
    private suspend fun saveInformation() {
        val dao = database.accespointDao()

        val accesPoint = AccesPoint(
            ssid = textViewSsid.text.toString(),
            frequency = textViewFrequency.text.toString(),
            rssi = textViewRssi.text.toString(),
            speedLink = textViewSpeedLink.text.toString(),
            distance = textViewDistance.text.toString()
        )
        withContext(Dispatchers.IO) {
            dao.insert(accesPoint)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            1 -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    if ((ActivityCompat.checkSelfPermission(
                            this@MainActivity,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) ==
                                PackageManager.PERMISSION_GRANTED)
                    ) {
                        checkWifiStatus(this)
                        Toast.makeText(this, "Przydzielono uprawnienia!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    textViewSsid.text = "Nieznany SSID\n Przydziel uprawnienia!"
                    hideButtons()
                    Toast.makeText(this, "Nie przydzielono uprawnień!", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }

}
