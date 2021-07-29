package com.example.letspoll

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.*
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.String


class PollHere : AppCompatActivity() {

    private val questionCollectionRef = Firebase.firestore.collection("Questions")

    lateinit var ques:TextView
    lateinit var start:Button
    lateinit var timer:TextView
    lateinit var btn:Button
    lateinit var sub:Button
    lateinit var rl:RelativeLayout
    lateinit var mgrAllButtons: RadioGroup
    lateinit var rdbtn : RadioButton
    val hm:HashMap<kotlin.String,Int> = HashMap<kotlin.String,Int>()

    var counter=30

    lateinit var str:kotlin.String
    lateinit var opt:kotlin.String



    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poll_here)

        ques=findViewById(R.id.tvques)
        start=findViewById(R.id.start)
        timer=findViewById(R.id.time_left)
        btn=findViewById(R.id.res)
        sub=findViewById(R.id.submit)
        mgrAllButtons=findViewById(R.id.radio_grp)
        rl=findViewById(R.id.rl)


        start.setOnClickListener {
            retrieveQuestion()

            object : CountDownTimer(30000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timer.setText(String.valueOf(counter))
                    counter--
               }

                override fun onFinish() {
                    timer.setText("FINISH")
                    btn.setVisibility(View.VISIBLE)}
            }.start()
        }

        btn.setOnClickListener {
            val intent=Intent(this,Result::class.java)
            startActivity(intent)
        }
        sub.setOnClickListener {

            val builder = AlertDialog.Builder(this)
            //set title for alert dialog
            builder.setTitle(R.string.dialog_title)
            //set message for alert dialog
            builder.setMessage(R.string.dialog_message)

            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes") {
                dialogInterface, which ->

                val sId = mgrAllButtons.getCheckedRadioButtonId()

                rdbtn =  findViewById(sId)


                val curr=hm.getOrDefault(rdbtn.getText() as kotlin.String,0)

                hm.put(rdbtn.getText() as kotlin.String,curr+1)

            }
            //performing cancel action
            builder.setNeutralButton("Cancel") { dialogInterface, which ->
                Toast.makeText(
                    applicationContext,
                    "clicked cancel\n operation cancel",
                    Toast.LENGTH_LONG
                ).show()
            }
            //performing negative action
            builder.setNegativeButton("No") { dialogInterface, which ->
                Toast.makeText(applicationContext, "clicked No", Toast.LENGTH_LONG)
            }

            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()
        }


    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun retrieveQuestion() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val querySnapshot = questionCollectionRef.orderBy("current").limitToLast(1).get().await()

            val sb = StringBuilder()
            for(document in querySnapshot.documents) {
                val question = document.toObject(Ques::class.java)
                sb.append("$question\n")
            }
            withContext(Dispatchers.Main) {
                str=sb.toString()
                var i1=str.indexOf('=')
                var i2=str.indexOf(',')
                ques.setText(str.substring(i1+1,i2))
                i1=str.indexOf('[')
                i2=str.indexOf(']')
                opt=str.substring(i1+1,i2)
                val options=opt.split(",")
                val sz=options.size
                    mgrAllButtons.setOrientation(LinearLayout.VERTICAL);
                    //
                for (i in 0..sz-1) {
                    val rdbtn = RadioButton(this@PollHere)
                    rdbtn.id = View.generateViewId()
                    rdbtn.text = options.get(i)
                    hm.put(options.get(i),0)
                    mgrAllButtons.addView(rdbtn)
                }
            }
        }
         catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@PollHere, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}
