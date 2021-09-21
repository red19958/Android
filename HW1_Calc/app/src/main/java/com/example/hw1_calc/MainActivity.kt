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
const val FLAG = "FLAG"
const val SIGN = "SIGN"

class MainActivity : AppCompatActivity() {

    private var currentExpression = ""
    private var savedResult = 0.0
    private var lastOperator = "a"
    private var flag = 0

    //flag = 0 поставить можно любой знак; flag = 1 последний символ это знак операции;
    // flag = 2 нельзя ставить точку; flag = 3 последний символ это точка ;
    // flag = 4 - не допускаются знаки операций; flag = 5 можно ставить только цифры
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
        outState.putString(LAST_OP, lastOperator)
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
        lastOperator = savedInstanceState.getString(LAST_OP)!!
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
                    lastOperator = "a"
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

            R.id.div -> {
                if (lastOperator.last().isDigit() && (flag == 0 || flag == 2)) {
                    currentExpression += textView.text
                    expr.text = currentExpression
                    flag = 1
                    lastOperator = textView.text.toString()
                    sign = textView.text.toString()
                }
            }

            R.id.mul -> {
                if (lastOperator.last().isDigit() && (flag == 0 || flag == 2)) {
                    currentExpression += textView.text
                    expr.text = currentExpression
                    flag = 1
                    lastOperator = textView.text.toString()
                    sign = textView.text.toString()
                }
            }

            R.id.sub -> {
                if (lastOperator.last().isDigit() && (flag == 0 || flag == 2)) {
                    currentExpression += textView.text
                    expr.text = currentExpression
                    flag = 1
                    lastOperator = textView.text.toString()
                    sign = textView.text.toString()
                }
            }

            R.id.add -> {
                if (lastOperator.last().isDigit() && (flag == 0 || flag == 2)) {
                    currentExpression += textView.text
                    expr.text = currentExpression
                    flag = 1
                    lastOperator = textView.text.toString()
                    sign = textView.text.toString()
                }
            }

            R.id.dot -> {
                if (lastOperator.last().isDigit() && (flag == 0 || flag == 4)) {
                    currentExpression += textView.text
                    expr.text = currentExpression

                    if (flag == 0) {
                        flag = 3
                    }

                    if (flag == 4) {
                        flag = 5
                    }

                    lastOperator = "."
                }
            }

            R.id.del -> {
                if (currentExpression == "Infinity" || currentExpression == "NaN") {
                    currentExpression = ""
                    expr.text = ""
                    flag = 0
                    lastOperator = "a"
                    sign = ""
                }
                if (currentExpression.length == 1) {
                    currentExpression = ""
                    expr.text = ""
                    flag = 0
                    lastOperator = "a"
                    sign = ""
                }
                if (currentExpression != "") {
                    if (lastOperator == "+" || lastOperator == "-" ||
                        lastOperator == "*" || lastOperator == "/" ||
                        lastOperator == "."
                    ) {
                        flag = 0
                        lastOperator = "0"
                        sign = ""
                    }
                    currentExpression = currentExpression.dropLast(1)
                    expr.text = currentExpression
                }
            }

            R.id.equal -> {
                report("EQUAL")
                if (sign != "" && lastOperator.last().isDigit()) {
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
                        currentExpression = res.text.toString()
                        expr.text = currentExpression
                        lastOperator = "0"
                        sign = ""
                        flag = 2
                    }

                    if (sign == "—") {
                        res.text = (first - second).toString()
                        currentExpression = res.text.toString()
                        expr.text = currentExpression
                        lastOperator = "0"
                        sign = ""
                        flag = 2
                    }

                    if (sign == "*") {
                        res.text = (first * second).toString()
                        currentExpression = res.text.toString()
                        expr.text = currentExpression
                        lastOperator = "0"
                        sign = ""
                        flag = 2
                    }

                    if (sign == "/") {
                        res.text = (first / second).toString()
                        currentExpression = res.text.toString()
                        expr.text = currentExpression
                        lastOperator = "0"
                        sign = ""
                        flag = 2
                    }
                }
            }
        }
    }

    fun number(v: View) {
        val button: Button = v as Button

        if (flag == 3) {
            flag = 2
        }

        if (flag == 1) {
            flag = 4
        }

        currentExpression += button.text
        findViewById<TextView>(R.id.expression).text = currentExpression
        lastOperator = button.text.toString()
    }

    private fun report(s: String) = Log.d("MAIN_ACTIVITY", s)
}
