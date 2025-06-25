package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var menuContainer: LinearLayout
    private lateinit var btnOrder: Button
    private lateinit var txtSummary: TextView

    private lateinit var menuItems: List<MenuItem>
    private val checkBoxes = mutableListOf<CheckBox>()

    private lateinit var dbHelper: MenuDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menuContainer = findViewById(R.id.menuContainer)
        btnOrder = findViewById(R.id.btnOrder)
        txtSummary = findViewById(R.id.txtSummary)

        dbHelper = MenuDatabaseHelper(this)
        menuItems = dbHelper.getAllMenuItems()

        if (menuItems.isEmpty()) {
            Toast.makeText(this, "菜單無資料", Toast.LENGTH_SHORT).show()
            return
        }

        // 動態生成 Checkbox
        for (item in menuItems) {
            val checkBox = CheckBox(this)
            checkBox.text = "${item.name}  \$${item.price}"
            menuContainer.addView(checkBox)
            checkBoxes.add(checkBox)
        }

        btnOrder.setOnClickListener {
            val result = StringBuilder("您點了：\n")
            var total = 0
            var hasOrder = false

            for ((index, cb) in checkBoxes.withIndex()) {
                if (cb.isChecked) {
                    val item = menuItems[index]
                    result.append("- ${item.name}  \$${item.price}\n")
                    total += item.price
                    hasOrder = true
                }
            }

            if (!hasOrder) {
                Toast.makeText(this, "請至少選擇一項餐點", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            result.append("總價：\$${total}")
            txtSummary.text = result.toString()
        }
    }
}
