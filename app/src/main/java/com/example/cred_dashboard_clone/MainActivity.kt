package com.example.cred_dashboard_clone

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thelumiereguy.neumorphicview.utils.updateNeumorphicLayoutParams
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_add_new.setOnClickListener {
            neumorphicCardView.enableHighlight = false
            materialButton3.updateNeumorphicLayoutParams {
                highlightColor = Color.RED
                highlightRadius = 107F
                enableHighlight = false
            }
        }
    }
}