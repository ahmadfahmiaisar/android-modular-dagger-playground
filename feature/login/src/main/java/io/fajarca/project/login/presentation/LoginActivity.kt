package io.fajarca.project.login.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import io.fajarca.project.base.abstraction.BaseApplication
import io.fajarca.project.login.R
import io.fajarca.project.login.di.component.DaggerLoginComponent
import javax.inject.Inject

class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, LoginActivity::class.java)
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        DaggerLoginComponent
            .builder()
            .baseComponent((application as BaseApplication).getBaseComponent())
            .build()
            .inject(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        viewModel.setPin("123456")
        val pin = viewModel.getPin()

        viewModel.getUsers()
        viewModel.users.observe(this){

        }
    }


}