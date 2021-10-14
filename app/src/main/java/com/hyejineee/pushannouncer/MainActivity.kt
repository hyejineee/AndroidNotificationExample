package com.hyejineee.pushannouncer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import com.hyejineee.pushannouncer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFirebase()
        update()

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        update(true)
    }

    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("token", task.result.toString())
                binding.tokenTextView.text = task.result
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun update(isNewIntent: Boolean = false) {

        Toast.makeText(this, intent.getStringExtra("type" )?:"값 없음.",Toast.LENGTH_SHORT).show()
        binding.resultTextView.text = (intent.getStringExtra("type") ?: "앱 런처") + if (isNewIntent) {
            "(으)로 갱신했습니다."
        } else {
            "(으)로 시작되었습니다."
        }
    }
}