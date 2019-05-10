package com.tasktodo.com.Mytodo


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.tasktodo.com.Mytodo.DatabaseConnector.DatabaseHelper
import com.tasktodo.com.Mytodo.Models.UserModel

class RegisterActivity : AppCompatActivity() {

    private var username: EditText? = null
    private var password: EditText? = null
    private var email: EditText? = null
    private var confirmPassword: EditText? = null
    private var signUp: Button? = null
    private var databaseHelper: DatabaseHelper? = null
    private var user: UserModel? = null
    private var editor: SharedPreferences.Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //This method will be used to initialize all the attributes
        init()

        //Setting on click listener on signup button
        signUp!!.setOnClickListener { validate() }
    }

    //This method will be used to initialize all the attributes
    fun init() {
        username = findViewById(R.id.usernameXML)
        password = findViewById(R.id.passwordXML)
        confirmPassword = findViewById(R.id.conpasswordXML)
        email = findViewById(R.id.emailXML)
        signUp = findViewById(R.id.signupButtonXML)
        databaseHelper = DatabaseHelper(this)
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        user = UserModel()
    }

    //This method will be used for validating inputs
    fun validate() {
        if (username!!.text.toString().isEmpty()) {
            username!!.error = "Username can not be empty."
        }
        if (password!!.text.toString().isEmpty()) {
            password!!.error = "Password can not be empty."
        }
        if (confirmPassword!!.text.toString().isEmpty()) {
            confirmPassword!!.error = "Password can not be empty."
        }
        if (email!!.text.toString().isEmpty()) {
            email!!.error = "Email can not be empty."
        }
        if (password!!.text.toString() != confirmPassword!!.text.toString()) {
            confirmPassword!!.error = "Password do not match."
            Log.d("Pass : ", "Confirm : " + confirmPassword!!.text.toString() + "\nPassword : " + password!!.text.toString())
        }

        if (username!!.text.toString() != "" && password!!.text.toString() != "" && confirmPassword!!.text.toString() != "" && email!!.text.toString() != "") {
            registerForNew()
        }

    }

    //This method will create a new user into sqlite database
    fun registerForNew() {
        if (!databaseHelper!!.checkUser(email!!.text.toString().trim { it <= ' ' })) {

            user!!.name = username!!.text.toString().trim { it <= ' ' }
            user!!.email = email!!.text.toString().trim { it <= ' ' }
            user!!.password = password!!.text.toString().trim { it <= ' ' }

            databaseHelper!!.addUser(user!!)
            //passing username or taking usernme in order to make session
            editor!!.putString("USERNAME", username!!.text.toString())
            editor!!.commit()
            // Snack Bar to show success message that record saved successfully
            Toast.makeText(applicationContext, "User registered successfully.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this@RegisterActivity, HomeActivity::class.java))
        } else {
            Toast.makeText(applicationContext, "Email already exists.", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private var sharedPreferences: SharedPreferences? = null
        val MyPREFERENCES = "TASKPREFERENCE"
    }
}
