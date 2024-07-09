package com.modcom.medilabsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textview.MaterialTextView
import com.modcom.medilabsapp.helpers.NetworkHelper
import com.modcom.medilabsapp.helpers.SQLiteCartHelper

class Screen1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screen1)

        //Check For Internet Connection
        if(!NetworkHelper.checkForInternet(applicationContext)){
            Toast.makeText(applicationContext, "Not Internet", Toast.LENGTH_SHORT).show()
            startActivity(Intent(applicationContext, NoNetActivity::class.java))
            finish()
        }
        else {
            //There is Internet Connection
            //Find Views by ID
            val skip1 = findViewById<MaterialTextView>(R.id.skip1)
            //Button to Go Next to Screen 2
            skip1.setOnClickListener {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }// End

            //Button to Skip to Main Activity
            val fab = findViewById<FloatingActionButton>(R.id.fab)
            fab.setOnClickListener {
                startActivity(Intent(applicationContext, Screen2::class.java))
            }// End
        }
    }
}