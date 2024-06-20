package com.modcom.medilabsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.modcom.medilabsapp.adapters.LabTestsCartAdapter
import com.modcom.medilabsapp.helpers.PrefsHelper
import com.modcom.medilabsapp.helpers.SQLiteCartHelper

class MyCart : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_cart)
        //put total cost in a textview
        val helper = SQLiteCartHelper(applicationContext)
        val checkout = findViewById<MaterialButton>(R.id.checkout)
        if (helper.totalCost() == 0.0){
            checkout.visibility = View.GONE
        }//end
        checkout.setOnClickListener {
             val token = PrefsHelper.getPrefs(applicationContext, "access_token")
             if (token.isEmpty()){
                 Toast.makeText(applicationContext, "Not Logged In",
                     Toast.LENGTH_SHORT).show()
                 startActivity(Intent(applicationContext, SignInActivity::class.java))
                 finish()
             }
            else {
                 //TODO
                 startActivity(Intent(applicationContext, CheckoutStep1::class.java))
                 Toast.makeText(applicationContext, "Logged In", Toast.LENGTH_SHORT).show()
             }
        }//end


        val total = findViewById<MaterialTextView>(R.id.total)

        total.text = "Total: "+helper.totalCost()

        //Find recycler
        val recycler = findViewById<RecyclerView>(R.id.recycler)
        recycler.layoutManager = LinearLayoutManager(applicationContext)
        recycler.setHasFixedSize(true)
        //Access adapter and provide it with from getAllItems
        if(helper.getNumItems() == 0){
            Toast.makeText(applicationContext, "Your Cart is Empty",
                Toast.LENGTH_SHORT).show()
        }
        else {
            val adapter = LabTestsCartAdapter(applicationContext)
            adapter.setListItems(helper.getAllItems())//pass data
            recycler.adapter = adapter //link adapter to recycler
        }
    }

    //Activate Options menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Below code Loads the cart XML
        menuInflater.inflate(R.menu.cart, menu)
        return super.onCreateOptionsMenu(menu) //Creates an Options Menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Below code makes id clear cart clickablea and clears the cart using helper.clearCart()
        if (item.itemId == R.id.clearcart){
            val helper = SQLiteCartHelper(applicationContext)
            helper.clearCart()
        }
        //backtoLabs takes us back to MainActivity
        if (item.itemId == R.id.backtoLabs){
            startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val i = Intent(applicationContext, MainActivity::class.java)
        startActivity(i)
        finishAffinity()
    }

}//last