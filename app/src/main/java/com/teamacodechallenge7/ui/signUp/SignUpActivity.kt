package com.teamacodechallenge7.ui.signUp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.teamacodechallenge7.R
import com.teamacodechallenge7.data.remote.ApiModule
import com.teamacodechallenge7.databinding.ActivitySignUpBinding
import com.teamacodechallenge7.ui.loginPage.LoginAct
import com.teamacodechallenge7.ui.mainMenu.MainMenuAct

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewModel: SignUpViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = SignUpFactory(ApiModule.service)
        viewModel = ViewModelProvider(this, factory)[SignUpViewModel::class.java]
        val binding =
            DataBindingUtil.setContentView<ActivitySignUpBinding>(this, R.layout.activity_sign_up)
        binding.viewModel = viewModel
        binding.icBack.setOnClickListener {
            startActivity(Intent(this, LoginAct::class.java))
            finish()
        }
        binding.btnSignUp.setOnClickListener {
            binding.btnSignUp.text = ("Loading...")
            binding.btnSignUp.isEnabled = false
            viewModel.signUp()
        }
        viewModel.resultLogin().observe(this, { bool ->
            if (!bool) {
                startActivity(Intent(this, NotifSignUpActivity::class.java))
                finish()
                binding.btnSignUp.isEnabled = bool
            } else {
                binding.btnSignUp.isEnabled = bool
                viewModel.buttonResult().observe(this, { but ->
                    binding.btnSignUp.text = but
                    viewModel.emailResult().observe(this, { emailErr ->
                        binding.etEmail.error = emailErr
                    })
                    viewModel.usernameResult().observe(this, { usernameErr ->
                        binding.etUsername.error = usernameErr
                    })
                    viewModel.passwordResult().observe(this, { passwordErr ->
                        binding.etPassword.error = passwordErr
                    })
                    viewModel.rePasswordResult().observe(this, { rePasswordErr ->
                        binding.etRePassword.error = rePasswordErr
                    })
                })
            }
        })

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainMenuAct::class.java))
        finish()
    }
}