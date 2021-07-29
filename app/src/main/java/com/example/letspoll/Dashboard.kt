package com.example.letspoll

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Dashboard : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        lateinit var home:Button
        lateinit var host:Button
        lateinit var join:Button


        home=findViewById(R.id.home)
        host=findViewById(R.id.host)
        join=findViewById(R.id.join)

        home.setOnClickListener {

            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        host.setOnClickListener {

            val intent=Intent(this,Question::class.java)
            startActivity(intent)
        }
        join.setOnClickListener {

        val intent=Intent(this,wait_host::class.java)
        startActivity(intent)
        }

    }

}