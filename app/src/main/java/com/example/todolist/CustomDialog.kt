package com.example.todolist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class CustomDialog(
    var activity: MainActivity,
    private val isNewItem: Boolean,
    private val item: ToDoItem?
) : Dialog(activity), View.OnClickListener {

    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var inputFieldTitle: EditText
    private lateinit var inputFieldDescription: EditText
    private lateinit var dialogLabel: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_template)

        inputFieldTitle = findViewById(R.id.dialog_input_title)
        inputFieldDescription = findViewById(R.id.dialog_input_description)
        dialogLabel = findViewById(R.id.dialog_label)

        if (isNewItem) {
            //Если создаетсяч новаяя ячейка
            createNewItem()
        } else {
            //Если создается не новая ячейка
            updateExistingItem()
        }

        dialogSizeControl()
        initViews()

    }

    private fun updateExistingItem() {
        Log.d("dialogTest", "update")

        dialogLabel.text = "Update Item"
        inputFieldTitle.setText(item?.title)
        inputFieldDescription.setText(item?.description)


    }

    private fun createNewItem() {
        Log.d("dialogTest", "new")
        dialogLabel.text = "New Item"
        inputFieldTitle.setText(item?.title)
        inputFieldDescription.setText(item?.description)

        val sharedPref =activity.getPreferences(Context.MODE_PRIVATE)
        val titleFromPrefs = sharedPref.getString("titleKey", " ")
        val descriptionFromPrefs = sharedPref.getString("descriptionKey", " ")
        inputFieldTitle.setText(titleFromPrefs)
        inputFieldDescription.setText(descriptionFromPrefs)
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

    override fun onClick(view: View) {
        when (view.id) {
            R.id.dialog_ok_button -> {
                okButtonClicker()
            }
            R.id.dialog_cancel_button -> {
                cancelButtonClicked()
            }
            else -> {
                elseBeenClicked()
            }
        }
    }

    private fun elseBeenClicked() {

    }

    private fun cancelButtonClicked() {
        dismiss()
    }

    private fun okButtonClicker() {
        //№2 отправляем данные в БД
        //2,1 вытаскиваем данные из полей
        //Если создается новая ячейка то этот сценарий
        if (isNewItem) {
            okNewItemBeenClicked()
        } else {
            //Если не новая то этот
            okUpdateItemBeenClicked()
        }
        dismiss()
    }

    private fun okUpdateItemBeenClicked() {
        val inputTitleResult = inputFieldTitle.text.toString()
        val inputDescriptionResult = inputFieldDescription.text.toString()
        item?.let { ToDoItem(it.id,inputTitleResult,inputDescriptionResult) }
            ?.let { activity.updateItem(it) }
    }

    private fun okNewItemBeenClicked() {
        val inputTitleResult = inputFieldTitle.text.toString()
        val inputDescriptionResult = inputFieldDescription.text.toString()
        activity.addItem(ToDoItem(0, inputTitleResult, inputDescriptionResult))
        inputFieldTitle.text.clear()
        inputFieldDescription.text.clear()
    }

    override fun onStop() {
        super.onStop()
        if (isNewItem) {
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
            Log.d("share", "save data start")
            with(sharedPref.edit()) {
                val inputTitleResult = inputFieldTitle.text.toString()
                val inputDescriptionResult = inputFieldDescription.text.toString()
                putString("titleKey", inputTitleResult)
                putString("descriptionKey", inputDescriptionResult)
                apply()
                Log.d("share", "save data finish")
            }
        }
    }
}

