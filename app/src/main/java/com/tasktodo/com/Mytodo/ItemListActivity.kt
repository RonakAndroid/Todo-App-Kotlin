package com.tasktodo.com.Mytodo

import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast

import com.tasktodo.com.Mytodo.Adapter.AssignmentListAdapter
import com.tasktodo.com.Mytodo.DatabaseConnector.DatabaseHelper

import java.util.ArrayList

class ItemListActivity : AppCompatActivity() {

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var assignmentListView: ListView? = null
    private var databaseHelper: DatabaseHelper? = null
    private var assignmentListAdapter: AssignmentListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        //This method will be used to initialize all the attributes with XML
        init()
        //calling the  method populateListview() in order to populate the listview with user data
        populateListView()
        //setting whenever a user swips what actions will happen
        swipeRefreshLayout!!.setOnRefreshListener {
            Handler().postDelayed({
                swipeRefreshLayout!!.isRefreshing = false
                populateListView()
                assignmentListAdapter!!.notifyDataSetChanged()
                assignmentListView!!.smoothScrollToPosition(0)
                Toast.makeText(this@ItemListActivity, "New record loaded.", Toast.LENGTH_SHORT).show()
            }, 2000)
        }
    }


    //This method will be used to initialize all the attributes with XML
    fun init() {
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        assignmentListView = findViewById(R.id.assignmentListViewXML)
        databaseHelper = DatabaseHelper(this)
    }

    //This method will be used to populate the listview
    fun populateListView() {
        val assignments = ArrayList(databaseHelper!!.allAssignment)
        assignmentListAdapter = AssignmentListAdapter(this@ItemListActivity, assignments)
        assignmentListView!!.adapter = assignmentListAdapter
    }

}
