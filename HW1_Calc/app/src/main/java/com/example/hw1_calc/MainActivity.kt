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
const val LAST_OP = "LAST_OP"
const val HELLO = "HELLO, WORLD!"

class MainActivity : AppCompatActivity() {

    private fun report(s: String) = Log.d("MAIN_ACTIVITY", s)
    private var currentExpression = ""
    private var savedResult = 0.0
    private var lastOperator = ""
    private var FLAG = 0
    private var SIGN = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        report("onCreate")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val RESULT = findViewById<TextView>(R.id.RESULT)
        outState.putString(EXPR, currentExpression)
        outState.putString(RES, RESULT.text.toString())
        outState.putDouble(SAVED_RESULT, savedResult)
        outState.putString(LAST_OP, lastOperator)
        report("onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val EXPRESSION = findViewById<TextView>(R.id.EXPRESSION)
        val RESULT = findViewById<TextView>(R.id.RESULT)
        currentExpression = savedInstanceState.getString(EXPR)!!
        EXPRESSION.text = currentExpression
        RESULT.text = savedInstanceState.getString(RES)
        savedResult = savedInstanceState.getDouble(SAVED_RESULT)
        lastOperator = savedInstanceState.getString(LAST_OP)!!
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

    fun Operation(v: View) {
        val TEXTVIEW: TextView = v as TextView
        val EXPRESSION = findViewById<TextView>(R.id.EXPRESSION)
        val RESULT = findViewById<TextView>(R.id.RESULT)


        when (TEXTVIEW.id) {

            findViewById<Button>(R.id.CLEAR).id -> {
                if (currentExpression != "" || RESULT.text != "") {
                    RESULT.text = ""
                    currentExpression = ""
                    EXPRESSION.text = ""
                    FLAG = 0
                    lastOperator = ""
                    SIGN = ""
                }
            }

            findViewById<Button>(R.id.COPY).id -> {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("label", RESULT.text)
                clipboard.setPrimaryClip(clip)
            }

            findViewById<Button>(R.id.HELLO).id->{
                RESULT.text = HELLO
            }

            findViewById<Button>(R.id.DIV).id -> {
                if (lastOperator.last().isDigit() && (FLAG == 0 || FLAG == 2)) {
                    currentExpression += TEXTVIEW.text
                    EXPRESSION.text = currentExpression
                    FLAG = 1
                    lastOperator = TEXTVIEW.text.toString()
                    SIGN = TEXTVIEW.text.toString()
                }
            }

            findViewById<Button>(R.id.MUL).id -> {
                if (lastOperator.last().isDigit() && (FLAG == 0 || FLAG == 2)) {
                    currentExpression += TEXTVIEW.text
                    EXPRESSION.text = currentExpression
                    FLAG = 1
                    lastOperator = TEXTVIEW.text.toString()
                    SIGN = TEXTVIEW.text.toString()
                }
            }

            findViewById<Button>(R.id.SUB).id -> {
                if (lastOperator.last().isDigit() && (FLAG == 0 || FLAG == 2)) {
                    currentExpression += TEXTVIEW.text
                    EXPRESSION.text = currentExpression
                    FLAG = 1
                    lastOperator = TEXTVIEW.text.toString()
                    SIGN = TEXTVIEW.text.toString()
                }
            }

            findViewById<Button>(R.id.ADD).id -> {
                if (lastOperator.last().isDigit() && (FLAG == 0 || FLAG == 2)) {
                    currentExpression += TEXTVIEW.text
                    EXPRESSION.text = currentExpression
                    FLAG = 1
                    lastOperator = TEXTVIEW.text.toString()
                    SIGN = TEXTVIEW.text.toString()
                }
            }

            findViewById<Button>(R.id.DOT).id -> {
                if (lastOperator.last().isDigit() && (FLAG == 0 || FLAG == 4)) {
                    currentExpression += TEXTVIEW.text
                    EXPRESSION.text = currentExpression
                    FLAG = 3
                    lastOperator = "."
                }
            }

            findViewById<Button>(R.id.DEL).id -> {
                if (currentExpression != "") {
                    if (lastOperator == "+" || lastOperator == "-" ||
                        lastOperator == "*" || lastOperator == "/" ||
                        lastOperator == "."
                    ) {
                        FLAG = 0
                        lastOperator = "0"
                        SIGN = ""
                    }
                    currentExpression = currentExpression.dropLast(1)
                    EXPRESSION.text = currentExpression
                }
            }

            findViewById<Button>(R.id.EQUAL).id -> {
                report("EQUAL")
                if (SIGN != "" && lastOperator.last().isDigit()) {
                    var firstString = ""
                    var secondString = ""
                    var costyl = 0

                    for (i in currentExpression.indices) {
                        if (currentExpression[i].toString() != SIGN) {
                            firstString += currentExpression[i]
                        } else {
                            break
                        }
                    }

                    for (i in currentExpression.indices) {
                        if(costyl == 1){
                            secondString += currentExpression[i]
                        }
                        if (currentExpression[i].toString() == SIGN) {
                            costyl = 1
                        }
                    }

                    val first = firstString.toDouble()
                    val second = secondString.toDouble()

                    if (SIGN == "+") {
                        RESULT.text = (first + second).toString()
                        currentExpression = RESULT.text.toString()
                        EXPRESSION.text = currentExpression
                        lastOperator = "0"
                        SIGN = ""
                        FLAG = 2
                    }

                    if (SIGN == "-") {
                        RESULT.text = (first - second).toString()
                        currentExpression = RESULT.text.toString()
                        EXPRESSION.text = currentExpression
                        lastOperator = "0"
                        SIGN = ""
                        FLAG = 2
                    }

                    if (SIGN == "*") {
                        RESULT.text = (first * second).toString()
                        currentExpression = RESULT.text.toString()
                        EXPRESSION.text = currentExpression
                        lastOperator = "0"
                        SIGN = ""
                        FLAG = 2
                    }

                    if (SIGN == "/") {
                        RESULT.text = (first / second).toString()
                        currentExpression = RESULT.text.toString()
                        EXPRESSION.text = currentExpression
                        lastOperator = "0"
                        SIGN = ""
                        FLAG = 2
                    }
                }
            }
        }
    }

    fun Number(v: View) {
        val EXPRESSION = findViewById<TextView>(R.id.EXPRESSION)
        val button: Button = v as Button

        if (FLAG == 3) {
            FLAG = 2
        }
        if (FLAG == 1) {
            FLAG = 4
        }

        currentExpression += button.text
        EXPRESSION.text = currentExpression
        lastOperator = button.text.toString()
    }
}