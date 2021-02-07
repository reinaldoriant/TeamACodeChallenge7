package com.teamacodechallenge7.ui.profileplayer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.teamacodechallenge7.R
import com.teamacodechallenge7.data.local.SharedPref
import com.teamacodechallenge7.ui.about.AboutActivity
import com.teamacodechallenge7.ui.loginPage.LoginAct
import com.teamacodechallenge7.ui.mainMenu.MainMenuAct

class ProfilePlayer : AppCompatActivity() {
    private val tag: String = "ProfilePlayer"
    private lateinit var profilePlayerViewModel: ProfilePlayerViewModel
    private lateinit var lParent: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_player)

        val pref = SharedPref
        val factory = ProfilePlayerViewModel.Factory(pref)
        profilePlayerViewModel =
            ViewModelProvider(this, factory)[ProfilePlayerViewModel::class.java]

        lParent = findViewById(R.id.lParent)
        val btEdit = findViewById<Button>(R.id.btEdit)
        val ivBack = findViewById<ImageView>(R.id.ivBack)
        val ivProfile = findViewById<ImageView>(R.id.ivProfile)
        val tvName = findViewById<TextView>(R.id.tvName)
        val tvEmail = findViewById<TextView>(R.id.tvEmail)
        val signOut=findViewById<Button>(R.id.btSignOut)
        val about =findViewById<TextView>(R.id.tvAbout)

        fetchData()

        ivBack.setOnClickListener {
            startActivity(Intent(this, MainMenuAct::class.java))
            finish()
        }
        btEdit.setOnClickListener {
            val intent = Intent(this, EditProfilePlayer::class.java)
            startActivity(intent)
            finish()
        }
        profilePlayerViewModel.resultUser.observe(this) {
            tvName.text = it.data.username
            tvEmail.text = it.data.email
            btEdit.text = ("Edit Profile")
            Glide
                .with(this)
                .load(it.data.photo)
                .centerCrop()
                .circleCrop()
                .placeholder(R.drawable.ic_people)
                .into(ivProfile)
        }
        profilePlayerViewModel.resultMessage.observe(this) {
            Log.e(tag, it.toString())
            if (it == "Token is expired" || it == "Invalid Token") {
                val snackbar = Snackbar.make(
                    lParent,
                    "Waktu bermain sudah selesai, main lagi? silahkan Login",
                    Snackbar.LENGTH_INDEFINITE
                )
                snackbar.setAction("Login") {
                    snackbar.dismiss()
                    startActivity(Intent(this, LoginAct::class.java))
                    finish()
                }.show()
            }
        }
        signOut.setOnClickListener {
            SharedPref.isLogin=false
            startActivity(Intent(this, LoginAct::class.java))
            finish()
        }
        about.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData() {
        profilePlayerViewModel.playerData()
    }

    override fun onBackPressed() {
        startActivity(Intent(this, MainMenuAct::class.java))
        finish()
    }
}