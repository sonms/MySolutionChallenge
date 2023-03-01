package com.example.mysolutionchallenge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.mysolutionchallenge.databinding.ActivitySearchitemviewBinding

class SearchItemViewActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivitySearchitemviewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySearchitemviewBinding.inflate(layoutInflater)

        setContentView(mBinding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val type = intent.getStringExtra("type")

        if (type.equals("view")) {

        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.home -> {
                val intent = Intent().apply {

                }
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}