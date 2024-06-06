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
import org.json.JSONArray
import org.json.JSONObject

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //link to register while in Login.
        val linktoregister = findViewById<MaterialTextView>(R.id.linktoregister)
        linktoregister.setOnClickListener {
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
        }


        val surname = findViewById<TextInputEditText>(R.id.surname)
        val password = findViewById<TextInputEditText>(R.id.password)

        val login = findViewById<MaterialButton>(R.id.login)
        login.setOnClickListener {
            val api = Constants.BASE_URL+"/member_signin"
            val helper = ApiHelper(applicationContext)
            val body = JSONObject()
            //Use Email Edit Text
            body.put("email", surname.text.toString())
            body.put("password", password.text.toString())
            helper.post(api, body, object : ApiHelper.CallBack {
                override fun onSuccess(result: JSONArray?) {
                }
                //user: bob101
                //pass: Qwerty1234
                override fun onSuccess(result: JSONObject?) {
                    //Consume the JSON - access keys
                    if (result!!.has("access_token")){

                        val access_token = result.getString("access_token")
                        val member = result.getString("member")// {} Object user details

                        Toast.makeText(applicationContext, "Success",
                            Toast.LENGTH_SHORT).show()

                        PrefsHelper.savePrefs(applicationContext,
                            "access_token", access_token)

                        //convert member to an Object
                        val memberObject = JSONObject(member)
                        val member_id = memberObject.getString("member_id")
                        val email = memberObject.getString("email")
                        val surname = memberObject.getString("surname")

                        //save member_id, email, surname to Prefs

                        PrefsHelper.savePrefs(applicationContext,
                            "userObject", member)

                        PrefsHelper.savePrefs(applicationContext,
                            "member_id", member_id)

                        PrefsHelper.savePrefs(applicationContext,
                            "email", email)

                        PrefsHelper.savePrefs(applicationContext,
                            "surname", surname)

                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finishAffinity()
                        //Proceed to HomePage. We need to Create It.

                    }
                    else {
                        Toast.makeText(applicationContext, result.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(result: String?) {
                    Toast.makeText(applicationContext, result.toString(),
                        Toast.LENGTH_SHORT).show()
                }

            });
            //

        }//end listener
    }//end oncreate
}//end class



