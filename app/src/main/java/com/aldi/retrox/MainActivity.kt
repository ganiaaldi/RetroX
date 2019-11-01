package com.aldi.retrox

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.aldi.retrox.Interface.Service
import com.aldi.retrox.Model.Github
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers



class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //initialized gson
        val gson = GsonBuilder().create()

        //initialized retrofit
        val retrofit: Retrofit = Retrofit.Builder()
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://api.github.com/")
            .build()

        val service: Service = retrofit.create(
            Service::class.java)

        //get data from github by username
        service.getUser("ganiaaldi")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { user ->
                    getData(user)
                },
                { error ->
                    Log.e("Error", error.message)
                }
            )

        btn.setOnClickListener {
            val intent = Intent(this@MainActivity, Rx::class.java)
            startActivity(intent)
        }

    }

    private fun getData(user: Github?) {
        val image = findViewById<ImageView>(R.id.image)
        val username = findViewById<TextView>(R.id.username)
        Glide.with(this).load(user?.avatarUrl).into(image)
        username.text = user?.name
    }
}