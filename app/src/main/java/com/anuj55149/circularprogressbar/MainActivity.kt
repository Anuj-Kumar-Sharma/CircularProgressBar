package com.anuj55149.circularprogressbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun animate(view: View) {
        val progress = editText.text.toString().toFloat()
        if(progress > 0 && progress <= 100) {
            progress_circular.setProgressWithAnimation(progress)
        } else {
            Toast.makeText(this, "Invalid Progress", Toast.LENGTH_LONG).show()
        }
    }
}
