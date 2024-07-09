package com.modcom.medilabsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.modcom.medilabsapp.constants.Constants
import com.modcom.medilabsapp.helpers.ApiHelper
import com.modcom.medilabsapp.helpers.PrefsHelper
import com.modcom.medilabsapp.helpers.SQLiteCartHelper
import org.json.JSONArray
import org.json.JSONObject

class Payment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        //Find TextView with id textpay
        val textpay = findViewById<MaterialTextView>(R.id.textpay)
        //Access helper
        val helper = SQLiteCartHelper(applicationContext)
        textpay.text = "Please Pay KES "+helper.totalCost() //Set Total Cost in TextView

        //Find Button and set Listener
        val btnpay = findViewById<MaterialButton>(R.id.btnpay)
        btnpay.setOnClickListener {
            //Find the phone EditText
            val phone = findViewById<TextInputEditText>(R.id.phone)
            //Access base URL
            val api = Constants.BASE_URL+"/make_payment"
            //Create JSON
            val body = JSONObject()
            //Put variables phone and amount
            body.put("phone", phone.text.toString())//254
            body.put("amount", helper.totalCost())
            //body.put("amount", "1")  //For Testing

            //Get from invoice_no from prefs
            val invoice_no = PrefsHelper.getPrefs(this, "invoice_no")
            //put invoice in body
            body.put("invoice_no", invoice_no)// get from prefs

            //Access Helper
            val apihelper = ApiHelper(applicationContext)
            //Post body to API
            apihelper.post2(api, body, object : ApiHelper.CallBack{
                override fun onSuccess(result: JSONArray?) {

                }

                override fun onSuccess(result: JSONObject?) {
                    //SUccess
                    Toast.makeText(applicationContext, result.toString(),
                        Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(result: String?) {
                    //Failure
                    Toast.makeText(applicationContext,
                        result.toString(), Toast.LENGTH_SHORT).show()
                    //                    val json = JSONObject(result.toString())
                    //                    val msg = json.opt("msg")
                    //                    //TODO
                    //                    if (msg == "Token has Expired"){
                    //                        PrefsHelper.clearPrefs(applicationContext)
                    //                        startActivity(Intent(applicationContext, SignInActivity::class.java))
                    //                        finishAffinity()
                    //                    }
                }
            })
        }//end listener
    }//end oncreate
}//end class