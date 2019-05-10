package com.tasktodo.com.Mytodo

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.tasktodo.com.Mytodo.DatabaseConnector.DatabaseHelper
import com.tasktodo.com.Mytodo.Models.AssignmentModel
import java.text.SimpleDateFormat
import java.util.*

class HomeActivity : AppCompatActivity() {

    private var mainLayout: RelativeLayout? = null
    private var sharedPreferences: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null
    private var selectdateButton: Button? = null
    private var submitButton: Button? = null
    private var dateTextView: TextView? = null
    private var username: TextView? = null
    private var assignmentName: EditText? = null
    private var courseName: EditText? = null
    private var assignmentNumber: EditText? = null
    private var mYear: Int = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var dueYear: Int = 0
    private var dueMonth: Int = 0
    private var dueDayName: String? = null
    private var assignmentNameStr: String? = null
    private var assignmentNumberStr: String? = null
    private var databaseHelper: DatabaseHelper? = null
    private var assignmentModel: AssignmentModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //This method will be used to initialize the attributes of xml
        init()
        username!!.text = "User : " + usernameSession!!

        selectdateButton!!.setOnClickListener {
            //This method will be used to select date and set the date into a textview
            selectDateMethod()
        }

        submitButton!!.setOnClickListener {
            //This method will be used to check the inputs
            checkInputs()
        }
    }

    //This method will be used to select date and set the date into a textview
    fun selectDateMethod() {
        // Getting the current Current Date
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this@HomeActivity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    //Storing the month and year value into this two variables
                    dueYear = year
                    dueMonth = monthOfYear
                    //This will be used to get the day name of the select date , like whether it is sundya or monday etc.
                    val simpledateformat = SimpleDateFormat("EEEE")
                    val date = Date(year, monthOfYear, dayOfMonth - 1)
                    dueDayName = simpledateformat.format(date)
                    dateTextView!!.text = "Date : " + dueDayName + "-" + (monthOfYear + 1) + "-" + year
                }, mYear, mMonth, mDay)
        try {
            datePickerDialog.show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "" + e.message, Toast.LENGTH_LONG).show()
        }

    }

    override fun onBackPressed() {
        moveTaskToBack(false)
    }

    override fun onPause() {
        super.onPause()
        GET_ACTIVITY_CODE = "HOME"
    }

    //This method will be used to sinitialize the attributes of xml
    fun init() {
        sharedPreferences = getSharedPreferences(LoginActivity.MyPREFERENCES, Context.MODE_PRIVATE)
        editor = sharedPreferences!!.edit()
        usernameSession = sharedPreferences!!.getString("USERNAME", "")
        //Toast.makeText(getApplicationContext(),"Username : "+usernameSession,Toast.LENGTH_SHORT).show();
        selectdateButton = findViewById(R.id.dateButtonXML)
        dateTextView = findViewById(R.id.dateTextViewXML)
        assignmentName = findViewById(R.id.assignmentNameXML)
        courseName = findViewById(R.id.courseNameOrCodeXML)
        assignmentNumber = findViewById(R.id.assignmentNumberXML)
        submitButton = findViewById(R.id.submitButtonXML)
        username = findViewById(R.id.usernameXMLHOME)
        mainLayout = findViewById(R.id.relativeLayoutForHomeContent)
        databaseHelper = DatabaseHelper(this)
        assignmentModel = AssignmentModel()
    }

    //This method will be used to check the inputs
    fun checkInputs() {
        assignmentNameStr = assignmentName!!.text.toString()
        assignmentNumberStr = assignmentNumber!!.text.toString()

        if (assignmentName!!.text.toString().isEmpty()) {
            assignmentName!!.error = "Assignment name can not be empty."
        }
        if (courseName!!.text.toString().isEmpty()) {
            courseName!!.error = "Course nam can not be empty."
        }
        if (assignmentNumber!!.text.toString().isEmpty()) {
            assignmentNumber!!.error = "Assignment number can not be empty"
        }

        if (dateTextView!!.text.toString() == "Date:") {
            Toast.makeText(applicationContext, "Please select due date.", Toast.LENGTH_LONG).show()
        }
        if (!assignmentName!!.text.toString().isEmpty() && !courseName!!.text.toString().isEmpty() &&
                !assignmentNumber!!.text.toString().isEmpty() && dateTextView!!.text.toString() != "Date:") {
            assignmentModel!!.assignmentName = assignmentNameStr
            assignmentModel!!.assignmentNumber = Integer.parseInt(assignmentNumberStr)
            assignmentModel!!.courseName = courseName!!.text.toString()
            assignmentModel!!.assignmentDueDate = dateTextView!!.text.toString()
            databaseHelper!!.addAssignment(assignmentModel!!)
            Toast.makeText(applicationContext, "Name :" + assignmentNameStr + "\nNumber : " + assignmentNumberStr + "\nCourse name : " + courseName!!.text.toString() +
                    "\nDate : " + dateTextView!!.text.toString(), Toast.LENGTH_LONG).show()
            Snackbar.make(mainLayout!!, "Assignment added to the list.", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.itemLogout -> {
                editor!!.clear()
                editor!!.commit()
                usernameSession = ""
                LoginActivity.IS_LOGIN = false
                Toast.makeText(applicationContext, "Logged out", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
            }
            R.id.itemShowList -> startActivity(Intent(this@HomeActivity, ItemListActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        var usernameSession: String? = ""
        lateinit var GET_ACTIVITY_CODE: String
        var CODE: Int = 0
        var courseInt: Int = 0
    }
}
