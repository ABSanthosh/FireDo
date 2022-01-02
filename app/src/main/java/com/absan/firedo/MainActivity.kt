package com.absan.firedo

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.gson.Gson


var itemList = arrayListOf<ListItem>();


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mainPrefInstance by lazy {
            this.getSharedPreferences(
                "Storage",
                Context.MODE_PRIVATE
            )
        }

        val storedData = getStoreData(mainPrefInstance)


        storedData.forEach {
            itemList.add(ListItem(it.id, it.content, it.isComplete))
        }


        val itemViewer = findViewById<TableLayout>(R.id.itemViewer)

        inflateTable(itemViewer, mainPrefInstance)

        val todoInput = findViewById<EditText>(R.id.todoInput)
        val addButton = findViewById<Button>(R.id.addItem)


        addButton.setOnClickListener {
            itemList.add(ListItem(itemList.size, todoInput.text.toString()))
            todoInput.text.clear()

            inflateTable(itemViewer, mainPrefInstance)
        }
    }

    fun inflateTable(tableLayout: TableLayout, mainPrefInstance: SharedPreferences) {
        tableLayout.removeAllViewsInLayout()
        itemList.forEach { listItem ->
            var row = layoutInflater.inflate(R.layout.recycleritem, tableLayout, false)
            var textContent = row.findViewById<TextView>(R.id.todoContent)
            val clearTodo = row.findViewById<ImageView>(R.id.clearTodo)

            textContent.text = listItem.content

            row.findViewById<CheckBox>(R.id.completeButton).isChecked = listItem.isComplete

            if (row.findViewById<CheckBox>(R.id.completeButton).isChecked) {
                textContent.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
            } else {
                textContent.paintFlags = 0
            }


            clearTodo.setOnClickListener {
                System.out.println("Delete " + listItem.id)
                itemList = itemList.filter { Item -> Item.id != listItem.id } as ArrayList<ListItem>
                inflateTable(tableLayout, mainPrefInstance)
                setStoreData(mainPrefInstance)
            }

            row.findViewById<CheckBox>(R.id.completeButton)
                .setOnCheckedChangeListener { compoundButton, b ->
                    itemList.forEachIndexed { index, item ->
                        run {
                            if (item.id == listItem.id) {
                                item.isComplete = b

                                if (row.findViewById<CheckBox>(R.id.completeButton).isChecked) {
                                    textContent.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG;
                                } else {
                                    textContent.paintFlags = 0
                                }
                            }
                        }
                    }
                    setStoreData(mainPrefInstance)
                }
            tableLayout.addView(row)

        }
    }

    fun setStoreData(mainPrefInstance: SharedPreferences) {
        mainPrefInstance.edit().apply { putString("json", Gson().toJson(itemList)) }.apply()
    }

    fun getStoreData(mainPrefInstance: SharedPreferences): List<ListItem> {
        return Gson().fromJson(mainPrefInstance.getString("json", ""), Array<ListItem>::class.java)
            .asList()
    }


}