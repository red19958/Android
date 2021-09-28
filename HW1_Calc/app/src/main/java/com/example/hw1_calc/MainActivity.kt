package com.example.hw1_calc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.view.View
import android.widget.TextView
import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context


const val EXPR = "EXPR"
const val RES = "RES"
const val SAVED_RESULT = "SAVED_RESULT"
const val HELLO = "HELLO, WORLD!"
const val FLAG = "FLAG"
const val SIGN = "SIGN"

class MainActivity : AppCompatActivity() {

    private var currentExpression = ""
    private var savedResult = 0.0
    private var flag = 0

    //flag = 0 поставить можно любой знак; flag = 1 есть операция нет точки у 1;
    //flag = 2 у 1ого стоит точка нет операции; flag = 3 была точка у 1ого сейчас операция;
    //flag = 4 у 1ого была точка, была операция и сейчас точка; flag = 5 запрет у 1ого нет точки;
    //flag = 6 запрет у 1ого есть точка; flag = 7 у 1ого была точка и была операция
    private var sign = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        report("onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val res = findViewById<TextView>(R.id.result)
        outState.putString(EXPR, currentExpression)
        outState.putString(RES, res.text.toString())
        outState.putDouble(SAVED_RESULT, savedResult)
        outState.putInt(FLAG, flag)
        outState.putString(SIGN, sign)
        report("onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val expr = findViewById<TextView>(R.id.expression)
        val res = findViewById<TextView>(R.id.result)
        currentExpression = savedInstanceState.getString(EXPR)!!
        expr.text = currentExpression
        res.text = savedInstanceState.getString(RES)
        savedResult = savedInstanceState.getDouble(SAVED_RESULT)
        sign = savedInstanceState.getString(SIGN)!!
        flag = savedInstanceState.getInt(FLAG)
        report("onRestoreInstanceState")
    }

    override fun onStart() {
        super.onStart()
        report("onStart")
    }

    override fun onResume() {
        super.onResume()
        report("onResume")
    }

    override fun onPause() {
        super.onPause()
        report("onPause")
    }

    override fun onStop() {
        super.onStop()
        report("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        report("onDestroy")
    }

    fun operation(v: View) {
        val textView: TextView = v as TextView
        val expr = findViewById<TextView>(R.id.expression)
        val res = findViewById<TextView>(R.id.result)


        when (textView.id) {
            R.id.clear -> {
                if (currentExpression != "" || res.text != "") {
                    res.text = ""
                    currentExpression = ""
                    expr.text = ""
                    flag = 0
                    sign = ""
                }
            }

            R.id.copy -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", res.text)
                clipboard.setPrimaryClip(clip)
            }

            R.id.hello -> {
                res.text = HELLO
            }

            R.id.div, R.id.sub, R.id.mul, R.id.add -> {
                if ((currentExpression != "" &&
                            currentExpression.last().isDigit() && (flag == 2 || flag == 0)) ||
                    currentExpression == "Infinity" || currentExpression == "NaN"
                ) {
                    currentExpression += textView.text
                    expr.text = currentExpression
                    if(flag == 0){
                        flag = 1
                    }
                    if(flag == 2){
                        flag = 3
                    }
                    sign = textView.text.toString()
                }
            }

            R.id.dot -> {
                if (currentExpression != "" && currentExpression.last()
                        .isDigit() && (flag == 0 || flag == 1 || flag == 7)
                ) {
                    currentExpression += textView.text
                    expr.text = currentExpression

                    when (flag) {
                        0 -> {
                            flag = 2
                        }
                        1 -> {
                            flag = 5
                        }
                        7 -> {
                            flag = 6
                        }
                    }
                }
            }

            R.id.del -> {

                if (currentExpression == "Infinity" || currentExpression == "NaN" || currentExpression.length == 1) {
                    currentExpression = ""
                    expr.text = ""
                    flag = 0
                    sign = ""
                }

                if (currentExpression != "") {
                    if (currentExpression.last() == '+' || currentExpression.last() == '—' ||
                        currentExpression.last() == '*' || currentExpression.last() == '/'
                    ) {
                        if(flag == 3){
                            flag = 2
                        }

                        if(flag == 1){
                            flag = 0
                        }

                        sign = ""
                        currentExpression = currentExpression.dropLast(1)
                    }

                    else if (currentExpression.last() == '.') {
                        if(flag == 2){
                            flag = 0
                            sign = ""
                        }

                        if(flag == 4){
                            flag = 7
                        }

                        if(flag == 6){
                            flag = 7
                        }
                        currentExpression = currentExpression.dropLast(1)
                    }

                    else if (currentExpression.last().isDigit()) {
                        currentExpression = currentExpression.dropLast(1)
                        if(currentExpression.last() == '.'){
                            if(flag == 5){
                                flag = 1
                            }
                            if(flag == 6){
                                flag = 4
                            }
                        }

                        if (currentExpression.last() == '+' || currentExpression.last() == '—' ||
                            currentExpression.last() == '*' || currentExpression.last() == '/'
                        ){
                            if(flag == 7){
                                flag = 3
                            }
                        }
                    }
                    expr.text = currentExpression
                }
            }

            R.id.equal -> {
                report("EQUAL")
                if (sign != "" && currentExpression.last().isDigit()) {
                    var firstString = ""
                    var secondString = ""
                    var costyl = 0

                    for (i in currentExpression.indices) {
                        if (currentExpression[i].toString() != sign) {
                            firstString += currentExpression[i]
                        } else {
                            break
                        }
                    }

                    for (i in currentExpression.indices) {
                        if (costyl == 1) {
                            secondString += currentExpression[i]
                        }
                        if (currentExpression[i].toString() == sign) {
                            costyl = 1
                        }
                    }

                    val first = firstString.toDouble()
                    val second = secondString.toDouble()

                    if (sign == "+") {
                        res.text = (first + second).toString()
                    }

                    if (sign == "—") {
                        res.text = (first - second).toString()
                    }

                    if (sign == "*") {
                        res.text = (first * second).toString()
                    }

                    if (sign == "/") {
                        res.text = (first / second).toString()
                    }

                    currentExpression = res.text.toString()
                    expr.text = currentExpression
                    sign = ""
                    flag = 2
                }
            }
        }
    }

    fun number(v: View) {
        if (currentExpression != "NaN" && currentExpression != "Infinity") {
            val button: Button = v as Button

            if(flag == 3){
                flag = 7
            }
            currentExpression += button.text
            findViewById<TextView>(R.id.expression).text = currentExpression
        }
    }

    private fun report(s: String) = Log.d("MAIN_ACTIVITY", s)
}
