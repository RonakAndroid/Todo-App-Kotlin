package com.tasktodo.com.Mytodo

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.tasktodo.com.Mytodo.DatabaseConnector.DatabaseHelper
import com.tasktodo.com.Mytodo.Models.UserModel

class LoginActivity : AppCompatActivity() {
    private var username: EditText? = null
    private var password: EditText? = null
    private var rememberMe: CheckBox? = null
    private var forgetPassword: Button? = null
    private var signin: Button? = null
    private var createNewAccount: Button? = null
    private var progressDialog: ProgressDialog? = null
    private var databaseHelper: DatabaseHelper? = null
    private var user: UserModel? = null
    private var editor: SharedPreferences.Editor? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //this method initialize all the attributes with XML
        init()

        //This will manage the session that whenever a user is logged in into the system the value will change based on the scenario
        if (IS_LOGIN) {
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
        }

        signin!!.setOnClickListener {
            //This method will be used to check the inputs
            inputValidation()
        }
        createNewAccount!!.setOnClickListener { startActivity(Intent(this@LoginActivity, RegisterActivity::class.java)) }
    }
    /*public boolean isLoggedIn(){
        return true;
    }*/

    //This method initialize all the attributes with XML
    fun init() {
        username = findViewById(R.id.usernameXML)
        password = findViewById(R.id.passwordXML)
        rememberMe = findViewById(R.id.checkboxXML)
        forgetPassword = findViewById(R.id.forgetPasswordXML)
        signin = findViewById(R.id.signinButtonXML)
        createNewAccount = findViewById(R.id.createNewAccountXML)
        databaseHelper = DatabaseHelper(this)
        user = UserModel()
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        progressDialog = ProgressDialog(this, R.style.AppTheme_Dark_Dialog)
    }

    //This method will be used to check the inputs
    fun inputValidation() {
        if (username!!.text.toString().isEmpty()) {
            username!!.error = "Username Can Not Be Empty."
        }
        if (password!!.text.toString().isEmpty()) {
            password!!.error = "Password Can Not Be Emprty."
        }
        if (rememberMe!!.isChecked) {
            //Make session
        }
        if (!username!!.text.toString().isEmpty() && !password!!.text.toString().isEmpty()) {
            progressDialog!!.isIndeterminate = true
            progressDialog!!.setMessage("Please wait , Logging in..")
            progressDialog!!.show()
            //This method will be used to login into the system
            signInIntoTheSystem()
        }
    }

    //This method will be used to login into the system
    fun signInIntoTheSystem() {
        if (databaseHelper!!.checkUser(username!!.text.toString(), password!!.text.toString())) {
            IS_LOGIN = true
            progressDialog!!.dismiss()
            //passing username or taking usernme in order to make session
            editor!!.putString("USERNAME", username!!.text.toString())
            editor!!.commit()
            //isLoggedIn();
            val accountsIntent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(accountsIntent)

        } else {
            // toast to show success message that record is wrong
            Toast.makeText(applicationContext, "Username and password do not match.", Toast.LENGTH_LONG).show()
            progressDialog!!.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        finishActivity(0)
    }


    override fun onPause() {
        super.onPause()
    }

    companion object {
        private var sharedPreferences: SharedPreferences? = null
        val MyPREFERENCES = "TASKPREFERENCE"
        var IS_LOGIN = true
    }
}
