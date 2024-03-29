package com.example.carplatekeyboard

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.exmample.carplatekeyboard.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        binding.textView.setOnClickListener {
            CarNumberDialog(binding.tvNumber.text.toString()) {
                binding.tvNumber.text = it
            }.show(supportFragmentManager, null)
        }
    }
}