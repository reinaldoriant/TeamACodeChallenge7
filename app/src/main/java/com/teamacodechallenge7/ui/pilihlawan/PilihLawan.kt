package com.teamacodechallenge7.ui.pilihlawan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamacodechallenge7.R
import com.teamacodechallenge7.data.database.TemanDatabase
import com.teamacodechallenge7.data.local.SharedPref
import com.teamacodechallenge7.ui.mainMenu.ChooseGamePlayAct
import com.teamacodechallenge7.ui.mainMenu.MainMenuAct
import com.teamacodechallenge7.utils.GamePlayMusic

class PilihLawan : AppCompatActivity() {
    private val tag : String = "PilihLawan"
    private lateinit var pilihLawanViewModel: PilihLawanViewModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_lawan)
        startService(Intent(this, GamePlayMusic::class.java))
        val pref = SharedPref
        val mDB: TemanDatabase = TemanDatabase.getInstance(this)!!

        val factory = PilihLawanViewModel.Factory(mDB, pref)
        pilihLawanViewModel = ViewModelProvider(this, factory)[PilihLawanViewModel::class.java]

        recyclerView = findViewById(R.id.recyclerView)
        val ivBack : ImageView = findViewById(R.id.ivBack)
        recyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        ivBack.setOnClickListener {
            startActivity(Intent(this, MainMenuAct::class.java))
            stopMusic()
            finish()
        }

        fetchData()
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() {
        Log.e(tag,"fetchData")
        pilihLawanViewModel.showList(recyclerView, this@PilihLawan)
    }

    override fun onDestroy() {
        super.onDestroy()
        pilihLawanViewModel.destroyDB()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, ChooseGamePlayAct::class.java))
        stopMusic()
        finish()
    }
    private fun stopMusic() {
        stopService(Intent(this, GamePlayMusic::class.java))
    }
}