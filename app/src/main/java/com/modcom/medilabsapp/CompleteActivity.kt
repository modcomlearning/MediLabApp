package com.modcom.medilabsapp
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.modcom.medilabsapp.constants.Constants
import com.modcom.medilabsapp.helpers.ApiHelper
import com.modcom.medilabsapp.helpers.PrefsHelper
import com.modcom.medilabsapp.helpers.SQLiteCartHelper
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


class CompleteActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete)

            //Access SQLIte Helper
            val sqllitehelper = SQLiteCartHelper(applicationContext)
            val items = sqllitehelper.getAllItems() //Get All items
            //Generate Invoice No. Check this Function at the bottom of this File
            val invoice_no = generateInvoiceNumber()

            //Save Invoice No  to Prefs
            PrefsHelper.savePrefs(applicationContext, "invoice_no", invoice_no)
            val numItems = items.size // Get all items size,
            //Loop each item
            items.forEachIndexed { index, labTests ->
            //How many Items do you have?
            // Capture test ID/Lab ID at a given Loop
            //Each item has a test_id and lab_id
            val test_id = labTests.test_id
            val lab_id = labTests.lab_id

            //Can Toast at this Point
            //Toast.makeText(applicationContext, "Test ID ${test_id} and Lab ID ${lab_id}", Toast.LENGTH_SHORT).show()

            //Capture other details from Preferences, These were saved in different part in this projects
            //NB: You might need to confirm that below were saved in preferences.
            //Retrieve all details from prefs
            val member_id = PrefsHelper.getPrefs(this, "member_id")
            val date = PrefsHelper.getPrefs(this, "date")
            val time = PrefsHelper.getPrefs(this, "time")
            val booked_for = PrefsHelper.getPrefs(this, "booked_for")
            val where_taken = PrefsHelper.getPrefs(this, "where_taken")
            val latitude = PrefsHelper.getPrefs(this, "latitude")
            val longitude = PrefsHelper.getPrefs(this, "longitude")
            val dependant_id = PrefsHelper.getPrefs(this, "dependant_id")

            //Access API helper and Post your variables to API
            val helper = ApiHelper(this)
            val api = Constants.BASE_URL + "/make_booking"
            val body = JSONObject()
            body.put("member_id", member_id)
            body.put("appointment_date", date)
            body.put("appointment_time", time)
            body.put("booked_for", booked_for)
            body.put("where_taken", where_taken)
            body.put("latitude", latitude)
            body.put("longitude", longitude)
            body.put("dependant_id", dependant_id)
            body.put("test_id", test_id)
            body.put("lab_id", lab_id)
            body.put("invoice_no", invoice_no)

            helper.post(api, body, object : ApiHelper.CallBack {
                override fun onSuccess(result: JSONObject?) {
                    //Success Posted
                    Toast.makeText(applicationContext, result.toString(), Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(result: String?) {
                     //Failed to POST
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

                override fun onSuccess(result: JSONArray?) {

                }
             })//end post
                //Index counts from zero, Means all lab tests have been posted to API we can now Proceed to Payment
             if (index == (numItems-1)){
                 Toast.makeText(applicationContext, "Processing Done", Toast.LENGTH_SHORT).show()
                 startActivity(Intent(applicationContext, Payment::class.java))
                 finish()
             }//end


            }//end for each
    }//end oncreate

    //Generate Invoice Number Function
    fun generateInvoiceNumber(): String {
        val dateFormat = SimpleDateFormat("yyyyMMddHHmmss")
        val currentTime = Date()
        val timestamp = dateFormat.format(currentTime)

        // You can use a random number or a sequential number to add uniqueness to the invoice number
        // For example, using a random number:
        val random = Random()
        val randomNumber = random.nextInt(1000) // Change the upper bound as needed

        // Combine the timestamp and random number to create the invoice number
        return "INV-$timestamp-$randomNumber" //Unique
    }
    //github.com/modcomlearning/MediLabApp
}//end class