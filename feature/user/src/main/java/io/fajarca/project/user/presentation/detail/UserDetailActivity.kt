package io.fajarca.project.user.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import io.fajarca.project.apiclient.di.DaggerApiClientComponent
import io.fajarca.project.base.ViewState
import io.fajarca.project.base.abstraction.BaseActivity
import io.fajarca.project.base.abstraction.BaseApplication
import io.fajarca.project.base.extension.gone
import io.fajarca.project.base.extension.visible
import io.fajarca.project.base.network.exception.ClientErrorException
import io.fajarca.project.base.network.exception.NoInternetConnection
import io.fajarca.project.base.network.exception.ServerErrorException
import io.fajarca.project.user.databinding.ActivityUserDetailBinding
import io.fajarca.project.user.di.component.DaggerUserComponent
import io.fajarca.project.user.domain.entity.User
import javax.inject.Inject

class UserDetailActivity : BaseActivity<ActivityUserDetailBinding>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<UserDetailViewModel> { viewModelFactory }

    private var userId: Int = 0

    override val getViewBinding: (LayoutInflater) -> ActivityUserDetailBinding
        get() = ActivityUserDetailBinding::inflate

    companion object {
        private const val INTENT_KEY_USER_ID = "userId"

        @JvmStatic
        fun start(context: Context, userId: Int) {
            val starter = Intent(context, UserDetailActivity::class.java).apply {
                putExtra(INTENT_KEY_USER_ID, userId)
            }
            context.startActivity(starter)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val apiClientComponent = DaggerApiClientComponent.factory().create()

        val userComponent = DaggerUserComponent
            .builder()
            .baseComponent((application as BaseApplication).getBaseComponent())
            .apiClientComponent(apiClientComponent)
            .build()

        userComponent.userDetailComponent().create().inject(this)

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        handleIntentArguments()
        observeUserDetail()
        viewModel.getUserDetail(userId)
    }

    private fun handleIntentArguments() {
        val extras = intent.extras
        userId = extras?.getInt(INTENT_KEY_USER_ID, 0) ?: 0
    }

    private fun observeUserDetail() {
        viewModel.user.observe(this) {
            when (it) {
                ViewState.Loading -> {
                    binding.progressBar.visible()
                }
                is ViewState.Success -> {
                    binding.progressBar.gone()
                    displayUserDetail(it.data)
                }
                is ViewState.Error -> {
                    binding.progressBar.gone()
                    when (val cause = it.cause) {
                        is ClientErrorException -> {
                            val code = cause.code
                        }
                        is ServerErrorException -> {
                            val code = cause.code
                        }
                        is NoInternetConnection -> {
                            Toast.makeText(this, "No connection", Toast.LENGTH_LONG).show()
                        }
                    }

                }
            }
        }
    }

    private fun displayUserDetail(user: User) {
        with(binding) {
            tvEmail.text = user.email
            tvName.text = user.name
            supportActionBar?.title = user.name
        }
    }
}