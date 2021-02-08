package com.teamacodechallenge7.ui.profileteman

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamacodechallenge7.R
import com.teamacodechallenge7.data.database.TemanDatabase
import com.teamacodechallenge7.data.local.SharedPref
import com.teamacodechallenge7.ui.mainMenu.MainMenuAct

class ProfileTeman : AppCompatActivity() {
    private val tag : String = "ProfileTeman"
    private lateinit var profileTemanViewModel: ProfileTemanViewModel
    private var usernamePlayer = mutableListOf<String>()
    private var usernameEmail = mutableListOf<String>()
    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_teman)

        val pref = SharedPref
        val mDB: TemanDatabase = TemanDatabase.getInstance(this)!!

        val factory = ProfileTemanViewModel.Factory(mDB, pref)
        profileTemanViewModel = ViewModelProvider(this, factory)[ProfileTemanViewModel::class.java]

        recyclerView = findViewById(R.id.recyclerView)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val btadd = findViewById<Button>(R.id.btadd)
        recyclerView.layoutManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false
        )
        profileTemanViewModel.playerName()
        fetchData()
        btadd.setOnClickListener {
            val view = LayoutInflater.from(this).inflate(R.layout.addfriend_dialog, null, false)
            val dialogBuilder = AlertDialog.Builder(this).setView(view)
            val dialogD1 = dialogBuilder.create()
            dialogD1.setCancelable(false)
            val btSaveFriend = view.findViewById<Button>(R.id.btSaveFriend)
            val btDeleteFriend = view.findViewById<Button>(R.id.btDeleteFriend)
            val etNama = view.findViewById<EditText>(R.id.etNama)
            val etEmail = view.findViewById<EditText>(R.id.etEmail)
            val btClose = view.findViewById<ImageView>(R.id.btClose)
            btDeleteFriend.visibility = View.GONE
            btSaveFriend.setOnClickListener {
                val namaTeman = etNama.text.toString()
                val emailTeman = etEmail.text.toString().trim()
                if (namaTeman.isEmpty() && emailTeman.isEmpty()) {
                    etNama.error = "Username harus diisi"
                    etEmail.error = "Email harus diisi"
                } else if (namaTeman.isEmpty()) {
                    etNama.error = "Username harus diisi"
                } else if (emailTeman.isEmpty()) {
                    etEmail.error = "Email harus diisi"
                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailTeman).matches()) {
                    etEmail.error = "Mohon isi email yang benar"
                } else if(usernamePlayer[0]==namaTeman&&usernameEmail[0]==emailTeman){
                    etNama.error = "Nama teman tidak boleh sama dengan player"
                    etEmail.error = "Email teman tidak boleh sama dengan player"
                }else {
                    profileTemanViewModel.addTeman(namaTeman, emailTeman)
                    dialogD1.dismiss()

                }
            }
            btClose.setOnClickListener {
                dialogD1.dismiss()
            }
            dialogD1.show()
        }
        ivBack.setOnClickListener {
            startActivity(Intent(this, MainMenuAct::class.java))
            finish()
        }


        profileTemanViewModel.resultName.observe(this) {
            usernamePlayer.add(it)
        }
        profileTemanViewModel.resultEmail.observe(this) {
            usernamePlayer.add(it)
        }
        profileTemanViewModel.resultAddTeman.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            fetchData()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    fun fetchData() {
        Log.e(tag,"fetchData")
        profileTemanViewModel.listTeman(recyclerView, this@ProfileTeman)
    }

    override fun onDestroy() {
        super.onDestroy()
        profileTemanViewModel.destroyDB()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainMenuAct::class.java))
        finish()
    }
}