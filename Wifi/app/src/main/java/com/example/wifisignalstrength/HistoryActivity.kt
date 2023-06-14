package com.example.wifisignalstrength

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.wifisignalstrength.data.AccessPointDatabase
import com.example.wifisignalstrength.model.AccesPoint
import kotlinx.coroutines.launch

class HistoryActivity: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var accessPointList: List<AccesPoint>
    private lateinit var database: AccessPointDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        database = Room.databaseBuilder(applicationContext,
            AccessPointDatabase::class.java,
            "accessPoint_database"
        ).build()
        lifecycleScope.launch {
            loadAccesPointHistory()
        }
    }

    private suspend fun loadAccesPointHistory() {
        val dao = database.accespointDao()
        accessPointList = dao.getInfo()
        adapter = ItemAdapter(accessPointList)
        runOnUiThread {
            recyclerView.adapter = adapter
        }
    }
}