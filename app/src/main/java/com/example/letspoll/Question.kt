package com.example.letspoll

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.SparseBooleanArray
import android.widget.*
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.lang.StringBuilder
import java.util.*
import java.util.Date as JavaUtilDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class Question : AppCompatActivity() {

    lateinit var btnUploadData:Button
    lateinit var quesId:EditText
    lateinit var add:Button
    lateinit var remove:Button
    lateinit var clear:Button
    lateinit var listview:ListView
    lateinit var opt:EditText
    lateinit var start:Button

    private val questionCollectionRef = Firebase.firestore.collection("Questions")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        btnUploadData=findViewById(R.id.savebtn)
        quesId=findViewById(R.id.edittxt)
        add=findViewById(R.id.add)
        remove=findViewById(R.id.remove)
        listview=findViewById(R.id.listView)
        clear=findViewById(R.id.clear)
        opt=findViewById(R.id.optiontxt)
        start=findViewById(R.id.start)


        var itemlist = arrayListOf<String>()
        var adapter=ArrayAdapter<String>(this,android.R.layout.simple_list_item_multiple_choice,
            itemlist)

        add.setOnClickListener {

            itemlist.add(opt.text.toString())
            listview.adapter=adapter
            adapter.notifyDataSetChanged()

            opt.text.clear()
        }

        clear.setOnClickListener {

            itemlist.clear()
            adapter.notifyDataSetChanged()
        }

        listview.setOnItemClickListener{
            adapterView,view,i,l->Toast.makeText(this,"You have selected "+itemlist.get(i),Toast.LENGTH_SHORT).show()
        }

        remove.setOnClickListener {

            val position :SparseBooleanArray=listview.checkedItemPositions
            val count=listview.count
            var item = count-1
            while(item>=0)
            {
                if(position.get(item))
                {
                    adapter.remove(itemlist.get(item))
                }
                item--
            }
            position.clear()
            adapter.notifyDataSetChanged()
        }

        btnUploadData.setOnClickListener {

            val qtn = quesId.text.toString()
            val list=itemlist
            val current =  LocalDateTime.now()
            val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val formatted = current.format(formatter)
            val question = Ques(qtn,list,formatted)

            saveQuestion(question)

        }




        start.setOnClickListener {

            val intent= Intent(this,PollHere::class.java)
            startActivity(intent)
        }




    }


    private fun saveQuestion(ques:Ques) = CoroutineScope(Dispatchers.IO).launch {
        try {
            questionCollectionRef.add(ques).await()
            withContext(Dispatchers.Main){
                Toast.makeText(this@Question,"Successfully saved question.",Toast.LENGTH_LONG).show()
            }
        } catch(e:Exception) {
            withContext(Dispatchers.Main){
                Toast.makeText(this@Question,e.message,Toast.LENGTH_LONG).show()
            }
        }
    }

}