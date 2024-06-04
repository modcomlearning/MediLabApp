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
        val textpay = findViewById<MaterialTextView>(R.id.textpay)
        val helper = SQLiteCartHelper(applicationContext)
        textpay.text = "Please Pay KES "+helper.totalCost()
        val btnpay = findViewById<MaterialButton>(R.id.btnpay)
        btnpay.setOnClickListener {
            val phone = findViewById<TextInputEditText>(R.id.phone)
            val api = Constants.BASE_URL+"/make_payment"
            val body = JSONObject()
            body.put("phone", phone.text.toString())//254
            //body.put("amount", helper.totalCost().toString())
            body.put("amount", "1")
            //Get from invoice_no from prefs
            val invoice_no = PrefsHelper.getPrefs(this, "invoice_no")
            //provide it as a body to payment API
            body.put("invoice_no", invoice_no)// get from prefs
            //Where did the Money go? to Till Number
            //Did we update our database? NO
            val apihelper = ApiHelper(applicationContext)
            apihelper.post2(api, body, object : ApiHelper.CallBack{
                override fun onSuccess(result: JSONArray?) {

                }

                override fun onSuccess(result: JSONObject?) {
                    Toast.makeText(applicationContext, result.toString(),
                        Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(result: String?) {
                    Toast.makeText(applicationContext,
                        result.toString(), Toast.LENGTH_SHORT).show()
                    val json = JSONObject(result.toString())
                    val msg = json.opt("msg")
                    //TODO
                    if (msg == "Token has Expired"){
                        PrefsHelper.clearPrefs(applicationContext)
                        startActivity(Intent(applicationContext, SignInActivity::class.java))
                        finishAffinity()
                    }
                }
               //Videos, Publishing on Play Store.
               //Github.com.
               //254729
            })
        }//end listener
    }//end oncreate
}//end class