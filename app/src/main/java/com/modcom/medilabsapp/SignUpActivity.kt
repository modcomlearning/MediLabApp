package com.modcom.medilabsapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import com.google.gson.GsonBuilder
import com.modcom.medilabsapp.constants.Constants
import com.modcom.medilabsapp.helpers.ApiHelper
import com.modcom.medilabsapp.models.Locations
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class SignUpActivity : AppCompatActivity() {
    private lateinit var buttonDatePicker: Button
    private lateinit var editTextDate: EditText
    private lateinit var spinner: Spinner
    private lateinit var selectedItemText: TextView
    private lateinit var locations: List<Locations>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        //link to Login while in Register.
        val linktologin = findViewById<MaterialTextView>(R.id.linktologin)
        linktologin.setOnClickListener {
            startActivity(Intent(applicationContext, SignInActivity::class.java))
        }

        buttonDatePicker = findViewById(R.id.buttonDatePicker)
        editTextDate = findViewById(R.id.editTextDate)

        buttonDatePicker.setOnClickListener {
            //First create below function
            showDatePickerDialog()
        }//end onclick

        //Spinner
        spinner = findViewById(R.id.spinner)
        selectedItemText = findViewById(R.id.selectedItemText)
        // Sample data for the spinner

        //Fetch Locations and Bring them here
        val helper = ApiHelper(applicationContext)
        val body = JSONObject()
        //Access the /locations End point
        val api = Constants.BASE_URL+"/locations"
        helper.post(api, body,object: ApiHelper.CallBack{
            override fun onSuccess(result: JSONArray?) {
                //JSON Array Is Returned - Locations
                    // Convert JSONArray to ArrayList<Locations>
                    val gson = GsonBuilder().create()
                    locations = gson.fromJson(result.toString(),
                        Array<Locations>::class.java).toList()

                val locationNames = locations.map { it.location }

                val adapter = ArrayAdapter(applicationContext,
                    android.R.layout.simple_spinner_item, locationNames)
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Set the adapter to the spinner
                spinner.adapter = adapter
            }

            override fun onSuccess(result: JSONObject?) {
               //JSON Object for No Locations

            }

            override fun onFailure(result: String?) {

            }
        })//end
        //val data: List<String> = listOf("1", "2", "3", "4", "5")// pending
        // Create an ArrayAdapter using the sample data

        //Check the selected location
        var location_id = ""
        spinner.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val selectedLocation = locations[position]
                location_id = selectedLocation.location_id
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                Toast.makeText(applicationContext,
                    "Please Select a location", Toast.LENGTH_SHORT).show()
            }
        }//end

        val create = findViewById<MaterialButton>(R.id.create)
        create.setOnClickListener {  //where do we close it?
            //Push/Post data to APi.
            val surname = findViewById<TextInputEditText>(R.id.surname)
            val others = findViewById<TextInputEditText>(R.id.others)
            val email = findViewById<TextInputEditText>(R.id.email)
            val phone = findViewById<TextInputEditText>(R.id.phone)
            val password = findViewById<TextInputEditText>(R.id.password)
            val confirm = findViewById<TextInputEditText>(R.id.confirm)
            val female = findViewById<RadioButton>(R.id.radioFemale)
            val male = findViewById<RadioButton>(R.id.radioMale)

            //handle radio buttons
            var gender = "N/A"
            if (female.isChecked) {
                gender = "Female"
            }
            if (male.isChecked) {
                gender = "Male"
            }
            //check if passwords are matching
            if (password.text.toString() != confirm.text.toString()) {
                Toast.makeText(
                    applicationContext, "Password Not Matching",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //Post data to /member_signup API
                val api = Constants.BASE_URL+"/member_signup"
                val helper = ApiHelper(applicationContext)
                val body = JSONObject()
                body.put("surname", surname.text.toString())
                body.put("others", others.text.toString())
                body.put("email", email.text.toString())
                body.put("phone", phone.text.toString())
                body.put("dob", editTextDate.text.toString())
                body.put("password", password.text.toString())
                body.put("gender", gender)
                body.put("location_id", location_id)
                body.put("status", "TRUE")


                helper.post(api, body, object : ApiHelper.CallBack {
                    override fun onSuccess(result: JSONArray?) {
                    }

                    override fun onSuccess(result: JSONObject?) {
                        //Posted successfully
                        Toast.makeText(applicationContext, result.toString(),
                            Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure(result: String?) {
                        //Failed to post
                        Toast.makeText(applicationContext, result.toString(),
                            Toast.LENGTH_SHORT).show()
                    }
                });

            }//end
        }//here inside oncreate, closes on click
    }//end oncreate

    //other functions- Date Picker function
    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        // Create a date picker dialog and set the current date as the default selection
        val datePickerDialog = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, month: Int, day: Int ->
                val selectedDate = formatDate(year, month, day)
                editTextDate.setText(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        // Show the date picker dialog, user cannot pick tomorrow
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() - 568080000000;
        datePickerDialog.show()
    }

    //Date conversion
    private fun formatDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}//end class