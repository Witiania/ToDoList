package com.example.todolist

import android.os.Bundle
import android.view.*
import android.view.WindowManager.LayoutParams
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer


class CustomDialog(
    var activity: MainActivity,
    private val isNewItem: Boolean,
    private val item: ToDoItem?
) : DialogFragment(), View.OnClickListener {

    private val mCustomDialogViewModel:CustomDialogViewModel by activityViewModels()

    private lateinit var okButton: Button
    private lateinit var cancelButton: Button
    private lateinit var inputFieldTitle: EditText
    private lateinit var inputFieldDescription: EditText
    private lateinit var dialogLabel: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view:View = inflater.inflate(R.layout.dialog_template,container,false)
        initViews(view)

        if (isNewItem) {
            //Если создаетсяч новаяя ячейка
            createNewItem()
        } else {
            //Если создается не новая ячейка
            updateExistingItem()
        }
        dialogSizeControl()
        return view
    }

    override fun onResume() {
        super.onResume()
        dialogSizeControl()
        mCustomDialogViewModel.todoItemResult.observe(this, Observer {
            if(isNewItem) {
                inputFieldTitle.setText(it.title)
                inputFieldDescription.setText(it.description)
            }
        })
    }

    private fun updateExistingItem() {

        dialogLabel.text = "Update Item"
        inputFieldTitle.setText(item?.title)
        inputFieldDescription.setText(item?.description)
    }

    private fun createNewItem() {

        mCustomDialogViewModel.getToDoItemFromPrefs()

    }

    private fun initViews(view: View) {
        inputFieldTitle = view.findViewById(R.id.dialog_input_title)
        inputFieldDescription = view.findViewById(R.id.dialog_input_description)
        dialogLabel =  view.findViewById(R.id.dialog_label)

        okButton =  view.findViewById<Button>(R.id.dialog_ok_button)
        cancelButton =  view.findViewById<Button>(R.id.dialog_cancel_button)
        okButton.setOnClickListener(this)
        cancelButton.setOnClickListener(this)}

    private fun dialogSizeControl() {
        val params:ViewGroup.LayoutParams = dialog!!.window!!.attributes
        params.width = ActionBar.LayoutParams.MATCH_PARENT
        params.height = ActionBar.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as LayoutParams
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

                val inputTitleResult = inputFieldTitle.text.toString()
                val inputDescriptionResult = inputFieldDescription.text.toString()
                mCustomDialogViewModel.saveDataInPrefs("titleKey", inputTitleResult)
                mCustomDialogViewModel.saveDataInPrefs("descriptionKey", inputTitleResult)
        }
    }
}

