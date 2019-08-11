package com.skosc.arko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein = Kodein.lazy { import(MainModule) }
    private val vm: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        vm.counter.observe(this, Observer {
            textView.text = it.toString()
        })
        button.setOnClickListener {
            vm.add(1)
        }
    }
}
