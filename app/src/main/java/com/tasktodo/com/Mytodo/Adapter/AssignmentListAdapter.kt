package com.tasktodo.com.Mytodo.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import com.tasktodo.com.Mytodo.Models.AssignmentModel
import com.tasktodo.com.Mytodo.R

import java.util.ArrayList

class AssignmentListAdapter : ArrayAdapter<AssignmentModel> {
    //for getting the context
    internal lateinit var context: Context
    //Arraylist of assignment information like assignment number,name,course name and deadline
    internal lateinit var assignmentData: ArrayList<AssignmentModel>

    constructor(context: Context, resource: Int) : super(context, resource) {}

    //This is the constructor for passing the current context and the list of assignment data
    constructor(context: Context, assignmentData: ArrayList<AssignmentModel>) : super(context, R.layout.list_row_item, assignmentData) {
        this.context = context
        this.assignmentData = assignmentData
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // Get the data item for this position

        val data = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view

        val viewHolder: Holder // view lookup cache stored in tag

        if (convertView == null) {

            viewHolder = Holder()
            val inflater = LayoutInflater.from(getContext())
            convertView = inflater.inflate(R.layout.list_row_item, parent, false)
            //initializing the textviews that we declared into the holder class
            viewHolder.asgnNumber = convertView!!.findViewById(R.id.assignmentIDXML)
            viewHolder.asgnName = convertView.findViewById(R.id.assignmentNameXML)
            viewHolder.courseName = convertView.findViewById(R.id.courseNameXML)
            viewHolder.deadline = convertView.findViewById(R.id.deadlineXML)

            convertView.tag = viewHolder
        } else {
            viewHolder = convertView.tag as Holder
        }
        //setting the text into those textviews
        viewHolder.asgnNumber!!.text = "Assignment number : " + data!!.assignmentNumber
        viewHolder.asgnName!!.text = "Assignment name : " + data.assignmentName!!
        viewHolder.courseName!!.text = "Course name : " + data.courseName!!
        // Toast.makeText(context,""+data.getCourseName(),Toast.LENGTH_LONG).show();
        viewHolder.deadline!!.text = "Deadline :" + data.assignmentDueDate!!

        // Return the completed view to render on screen
        return convertView
    }

    //Creating the class holder to hold the view
    inner class Holder {
        //taking three textviews  in order to initialize with xml textviews
        //using asgn as the short form of assignment
        internal var asgnNumber: TextView? = null
        internal var asgnName: TextView? = null
        internal var courseName: TextView? = null
        internal var deadline: TextView? = null
    }
}
