package com.example.letspoll

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ColorTemplate.*
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import java.lang.String


class Result : AppCompatActivity() {

    lateinit var barchart:BarChart
    lateinit var barData:BarData
    lateinit var barDataSet:BarDataSet
    lateinit var al:ArrayList<BarEntry>



    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        barchart=findViewById(R.id.barChart)

        getEntries()

        barDataSet= BarDataSet(al,"Data Set")
        barData= BarData(barDataSet)

        barchart.setData(barData)

        barDataSet.setColor(MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);







    }
  private fun getEntries()
    {
        al = ArrayList<BarEntry>()
        al.add(BarEntry(1f,2f))
        al.add(BarEntry(2f,4f))
        al.add(BarEntry(3f,6f))
        al.add(BarEntry(4f,8f))




    }


    }

private fun BarDataSet.setColor(materialColors: IntArray?) {

}
