package com.aldi.retrox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.aldi.retrox.ViewModel.*
import kotlinx.android.synthetic.main.activity_rx.*

class Rx : AppCompatActivity() {

    companion object{
        private const val EMPTY_STRING =""
    }

    private val viewModel by lazy{
        viewModel{ MainViewModel()}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx)

        observeViewModel()
    }

    fun randomJoke(view: View) = viewModel.onJokeRequest()
    fun randomJokes(view : View) = viewModel.onJokesRequest()
    fun Install(view: View) = viewModel.onInstall()

    private fun observeViewModel() {
        btn_single_joke.setOnClickListener {
            viewModel.onJokeRequest()
        }

        btn_jokes.setOnClickListener {
            viewModel.onJokesRequest()
        }

        btn_install.setOnClickListener{
            viewModel.onInstall()
        }
        //Single
        viewModel.jokeLoadingStatus.observe(this, Observer{
            when (it){
                MainViewModel.LoadingStatus.LOADING ->{
                    loader_single_joke.show()
                    btn_single_joke.disable()
                }
                MainViewModel.LoadingStatus.NOT_LOADING ->{
                    loader_single_joke.hide()
                    btn_single_joke.enable()
                }
            }
        })
        viewModel.jokes.observe(this, Observer {
            tv_jokes.text = it?.toJokeString() ?: EMPTY_STRING
        })

        //Completable

        viewModel.installLoadingStatus.observe(this, Observer {
            when (it) {
                MainViewModel.LoadingStatus.LOADING -> {
                    loader_install.show()
                    btn_install.disable()
                }
                MainViewModel.LoadingStatus.NOT_LOADING -> {
                    loader_install.hide()
                    btn_install.enable()
                }
            }
        })

        viewModel.installation.observe(this, Observer{
            when(it){
                MainViewModel.InstallationStatus.SUCCESS -> toast("Install Sucess")
                MainViewModel.InstallationStatus.ERROR -> toast("Failed")
            }
        })
    }

}
