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

        //Find Views
        val surname = findViewById<TextInputEditText>(R.id.surname)
        val password = findViewById<TextInputEditText>(R.id.password)

        val login = findViewById<MaterialButton>(R.id.login)
        login.setOnClickListener {
            //Specify the /member_signin" Endpoint
            val api = Constants.BASE_URL+"/member_signin"
            val helper = ApiHelper(applicationContext)
            //Create a JSON Object of email and Password
            val body = JSONObject()
            //Use Email Edit Text
            body.put("email", surname.text.toString())
            body.put("password", password.text.toString())
            helper.post(api, body, object : ApiHelper.CallBack {
                override fun onSuccess(result: JSONArray?) {
                }

                override fun onSuccess(result: JSONObject?) {
                    //Consume the JSON - access keys
                    //Check if access_token exist in response
                    if (result!!.has("access_token")){
                        //Access token Found, Login Success
                        //access the access token and member from the JSOn returned
                        val access_token = result.getString("access_token")
                        val member = result.getString("member")// {} Object user details

                        //Toast a success message
                        Toast.makeText(applicationContext, "Success",
                            Toast.LENGTH_SHORT).show()

                        //Save access Token to Shared Prefs
                        PrefsHelper.savePrefs(applicationContext,
                            "access_token", access_token)

                        //convert member to an Object
                        val memberObject = JSONObject(member)
                        val member_id = memberObject.getString("member_id")
                        val email = memberObject.getString("email")
                        val surname = memberObject.getString("surname")

                        //save member, member_id, email, surname to Shared Prefs
                        PrefsHelper.savePrefs(applicationContext,
                            "userObject", member)

                        PrefsHelper.savePrefs(applicationContext,
                            "member_id", member_id)

                        PrefsHelper.savePrefs(applicationContext,
                            "email", email)

                        PrefsHelper.savePrefs(applicationContext,
                            "surname", surname)

                        //redirect to MainActivity upon successful Login
                        startActivity(Intent(applicationContext, MainActivity::class.java))
                        finishAffinity()

                    }
                    else {
                        //No access token Found , Login Failed
                        Toast.makeText(applicationContext, result.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(result: String?) {
                    //Fails to Connect
                    Toast.makeText(applicationContext, result.toString(),
                        Toast.LENGTH_SHORT).show()
                }

            });

        }//end listener
    }//end oncreate
}//end class



