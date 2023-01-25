package com.example.todolist

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText

class CustomDialog (var activity: MainActivity):Dialog(activity),View.OnClickListener{

    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var inputFieldTitle:EditText
    private lateinit var inputFieldDescription:EditText
    private lateinit var inputFieldNumber:EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_template)

        inputFieldTitle = findViewById(R.id.dialog_input_title)
        inputFieldDescription = findViewById(R.id.dialog_input_description)
        inputFieldNumber = findViewById(R.id.dialog_input_number)
        dialogSizeControl()
        initViews()

    }


    private fun dialogSizeControl() {
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(this.window?.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        lp.gravity = Gravity.CENTER
        this.window?.attributes = lp
    }

    private fun initViews() {
        okButton = findViewById<Button>(R.id.dialog_ok_button)
        cancelButton = findViewById<Button>(R.id.dialog_cancel_button)
        okButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)
    }
 //Имплементируемая функция через View.OnClickListener, в функции отобюражает всек кликабельные элементы разом
    override fun onClick(view: View) {
        when(view.id) {
                R.id.dialog_ok_button ->{
                    val inputTitleResult = inputFieldTitle.text.toString()
                    val inputDescriptionResult = inputFieldDescription.text.toString()
                    val inputNumberResult = inputFieldNumber.text.toString().toInt()
                    activity.addItem(ToDoItem(inputTitleResult,inputDescriptionResult,inputNumberResult))
                    dismiss()
                }
                R.id.dialog_cancel_button -> dismiss() //dismiss закрытие диалоговго окна
                else -> {
                }
            }
        }

    }

