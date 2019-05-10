package com.tasktodo.com.Mytodo.DatabaseConnector

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.tasktodo.com.Mytodo.Models.AssignmentModel
import com.tasktodo.com.Mytodo.Models.UserModel
import java.util.*

class DatabaseHelper
/**
 * Constructor
 *
 * @param context
 */
(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    //Creatint table sql query for user table
    private val CREATE_USER_TABLE = ("CREATE TABLE " + TABLE_USER + "("
            + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_USER_NAME + " TEXT,"
            + COLUMN_USER_EMAIL + " TEXT," + COLUMN_USER_PASSWORD + " TEXT" + ")")

    //Creating query for assignment table query
    private val CREATE_ASSIGNMENT_TABLE = ("CREATE TABLE " + TABLE_ASSIGNMENT + "("
            + COLUMN_ASSIGNMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_ASSIGNMENT_NAME + " TEXT," + COLUMN_COURSE_NAME + " TEXT,"
            + COLUMN_ASSIGNMENT_NUMBER + " INTEGER," + COLUMN_ASSIGNMENT_DATE + " TEXT" + ")")

    // drop table sql query
    private val DROP_USER_TABLE = "DROP TABLE IF EXISTS $TABLE_USER"
    private val DROP_ASSIGNMENT_TABLE = "DROP TABLE IF EXISTS $TABLE_ASSIGNMENT"

    /**
     * This method is to fetch all user and return the list of assignment records
     *
     * @return assignmentList
     */
    // array of columns to fetch
    // sorting orders
    // query the user table
    /**
     * Here query function is used to fetch records from user table this function works like we use sql query.
     * SQL query equivalent to this query function is
     * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
     *///Table to query
    //columns to return
    //columns for the WHERE clause
    //The values for the WHERE clause
    //group the rows
    //filter by row groups
    //The sort order
    // Traversing through all rows and adding to list
    // Adding user record to list
    // return user list
    val allAssignment: List<AssignmentModel>
        get() {
            val columns = arrayOf(COLUMN_ASSIGNMENT_ID, COLUMN_ASSIGNMENT_NAME, COLUMN_ASSIGNMENT_NUMBER, COLUMN_COURSE_NAME, COLUMN_ASSIGNMENT_DATE)
            val sortOrder = "$COLUMN_ASSIGNMENT_ID ASC"
            val assignmentList = ArrayList<AssignmentModel>()

            val db = this.readableDatabase
            val cursor = db.query(TABLE_ASSIGNMENT,
                    columns, //filter by row groups
                    null, null, null, null,
                    sortOrder)
            if (cursor.moveToFirst()) {
                do {
                    val assignment = AssignmentModel()
                    assignment.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_ID)))
                    assignment.assignmentName = cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_NAME))
                    assignment.courseName = cursor.getString(cursor.getColumnIndex(COLUMN_COURSE_NAME))
                    assignment.assignmentNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_NUMBER)))
                    assignment.assignmentDueDate = cursor.getString(cursor.getColumnIndex(COLUMN_ASSIGNMENT_DATE))
                    assignmentList.add(assignment)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return assignmentList
        }


    /**
     * This method is to fetch all user and return the list of user records
     *
     * @return list
     */
    // array of columns to fetch
    //COLUMN_USER_PASSWORD
    // sorting orders
    // query the user table
    /**
     * Here query function is used to fetch records from user table this function works like we use sql query.
     * SQL query equivalent to this query function is
     * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
     *///Table to query
    //columns to return
    //columns for the WHERE clause
    //The values for the WHERE clause
    //group the rows
    //filter by row groups
    //The sort order
    // Traversing through all rows and adding to list
    //user.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_USER_PASSWORD)));
    // Adding user record to list
    // return user list
    val allUser: List<UserModel>
        get() {
            val columns = arrayOf(COLUMN_USER_ID, COLUMN_USER_EMAIL, COLUMN_USER_NAME)
            val sortOrder = "$COLUMN_USER_NAME ASC"
            val userList = ArrayList<UserModel>()

            val db = this.readableDatabase
            val cursor = db.query(TABLE_USER,
                    columns, null, null, null, null,
                    sortOrder)
            if (cursor.moveToFirst()) {
                do {
                    val user = UserModel()
                    user.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_USER_ID)))
                    user.name = cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME))
                    user.email = cursor.getString(cursor.getColumnIndex(COLUMN_USER_EMAIL))
                    userList.add(user)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return userList
        }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_USER_TABLE)
        db.execSQL(CREATE_ASSIGNMENT_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        //Drop User Table if exist
        db.execSQL(DROP_USER_TABLE)
        db.execSQL(DROP_ASSIGNMENT_TABLE)
        // Create tables again
        onCreate(db)

    }

    /**
     * This method is to create assignment record
     *
     * @param assignment
     */
    fun addAssignment(assignment: AssignmentModel) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_ASSIGNMENT_NAME, assignment.assignmentName)
        values.put(COLUMN_ASSIGNMENT_NUMBER, assignment.assignmentNumber)
        values.put(COLUMN_COURSE_NAME, assignment.courseName)
        values.put(COLUMN_ASSIGNMENT_DATE, assignment.assignmentDueDate)

        // Inserting Row to the assignment table
        db.insert(TABLE_ASSIGNMENT, null, values)
        db.close()
    }

    fun addUser(user: UserModel) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)

        // Inserting Row to the user table
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    /**
     * This method to update user record
     *
     * @param user
     */
    fun updateUser(user: UserModel) {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(COLUMN_USER_NAME, user.name)
        values.put(COLUMN_USER_EMAIL, user.email)
        values.put(COLUMN_USER_PASSWORD, user.password)

        // updating row
        db.update(TABLE_USER, values, "$COLUMN_USER_ID = ?",
                arrayOf(user.id.toString()))
        db.close()
    }

    /**
     * This method is to delete user record
     *
     * @param user
     */
    fun deleteUser(user: UserModel) {
        val db = this.writableDatabase
        // delete user record by id
        db.delete(TABLE_USER, "$COLUMN_USER_ID = ?",
                arrayOf(user.id.toString()))
        db.close()
    }

    /**
     * This method to check user exist or not
     *
     * @param email
     * @return true/false
     */
    fun checkUser(email: String): Boolean {

        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase

        // selection criteria
        val selection = "$COLUMN_USER_EMAIL = ?"

        // selection argument
        val selectionArgs = arrayOf(email)

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        val cursor = db.query(TABLE_USER, //Table to query
                columns, //columns to return
                selection, //columns for the WHERE clause
                selectionArgs, null, null, null)//The values for the WHERE clause
        //group the rows
        //filter by row groups
        //The sort order
        val cursorCount = cursor.count
        cursor.close()
        db.close()

        return if (cursorCount > 0) {
            true
        } else false

    }


    /**
     * This method to check user exist or not
     *
     * @param username
     * @param password
     * @return true/false
     */
    fun checkUser(username: String, password: String): Boolean {

        // array of columns to fetch
        val columns = arrayOf(COLUMN_USER_ID)
        val db = this.readableDatabase
        // selection criteria
        val selection = "$COLUMN_USER_NAME = ? AND $COLUMN_USER_PASSWORD = ?"

        // selection arguments
        val selectionArgs = arrayOf(username, password)

        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        val cursor = db.query(TABLE_USER, //Table to query
                columns, //columns to return
                selection, //columns for the WHERE clause
                selectionArgs, null, null, null)//The values for the WHERE clause
        //group the rows
        //The sort order

        val cursorCount = cursor.count

        cursor.close()
        db.close()
        return if (cursorCount > 0) {
            true
        } else false

    }

    companion object {
        // Database Version
        private val DATABASE_VERSION = 1

        // Database Name
        private val DATABASE_NAME = "TheHumbleAssignment.db"

        // User table name
        private val TABLE_USER = "user"

        //Assignment table name
        private val TABLE_ASSIGNMENT = "assignment"

        // User Table Columns names
        private val COLUMN_USER_ID = "user_id"
        private val COLUMN_USER_NAME = "user_name"
        private val COLUMN_USER_EMAIL = "user_email"
        private val COLUMN_USER_PASSWORD = "user_password"

        //Assignment lists table columns
        private val COLUMN_ASSIGNMENT_ID = "assignment_id"
        private val COLUMN_ASSIGNMENT_NAME = "assignment_name"
        private val COLUMN_COURSE_NAME = "course_name"
        private val COLUMN_ASSIGNMENT_NUMBER = "assignment_number"
        private val COLUMN_ASSIGNMENT_DATE = "assignment_date"
    }

}
