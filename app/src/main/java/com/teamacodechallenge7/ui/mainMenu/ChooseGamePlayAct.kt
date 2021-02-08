package com.teamacodechallenge7.ui.mainMenu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.teamacodechallenge7.R
import com.teamacodechallenge7.databinding.ActivityChooseGamePlayBinding
import com.teamacodechallenge7.databinding.ActivityLoginBinding
import com.teamacodechallenge7.ui.pilihlawan.PilihLawan
import com.teamacodechallenge7.ui.playgamevscomputer.PlayGameVsComputer
import com.teamacodechallenge7.ui.playgamevsplayer.PlayGameVsPlayer
import com.teamacodechallenge7.ui.signUp.SignUpActivity

class ChooseGamePlayAct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityChooseGamePlayBinding>(
                this,
                R.layout.activity_choose_game_play
            )
        binding.btPlayOther.setOnClickListener {
            startActivity(Intent(this, PilihLawan::class.java))
            finish()
        }
        binding.btPlayComputer.setOnClickListener {
            startActivity(Intent(this, PlayGameVsComputer::class.java))
            finish()
        }
        binding.btBackMenu.setOnClickListener {
            startActivity(Intent(this, MainMenuAct::class.java))
            finish()
        }
    }
}