package com.teamacodechallenge7.ui.profileteman

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamacodechallenge7.R

class ProfileTeman : AppCompatActivity() {
    private lateinit var profileTemanViewModel: ProfileTemanViewModel
    private var usernamePlayer = mutableListOf<String>()
    private var usernameEmail = mutableListOf<String>()
    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_teman)
        val factory = ProfileTemanViewModel.Factory()
        profileTemanViewModel = ViewModelProvider(this, factory)[ProfileTemanViewModel::class.java]

        recyclerView = findViewById(R.id.recyclerView)
        val ib_home = findViewById<ImageView>(R.id.ib_home)
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
            val etNama = view.findViewById<EditText>(R.id.etNama)
            val etEmail = view.findViewById<EditText>(R.id.etEmail)
            val btClose = view.findViewById<EditText>(R.id.btClose)
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
        ib_home.setOnClickListener {
//            startActivity(Intent(this, MainMenuActivity::class.java))
        }


        profileTemanViewModel.resultName.observe(this) {
            Toast.makeText(this, it.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    fun fetchData() {
        profileTemanViewModel.listTeman(recyclerView, this@ProfileTeman)
    }

    override fun onDestroy() {
        super.onDestroy()
        profileTemanViewModel.destroyDB()
    }
}