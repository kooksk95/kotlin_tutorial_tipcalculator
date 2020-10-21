package com.example.kotlin_tutorial_tipcalculator

import android.animation.ArgbEvaluator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainActivity"
private const val INIT_VAL = 15

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarTip.progress = INIT_VAL;
        tvTipPercentLabel.text = "$INIT_VAL%";
        setTipStatus(INIT_VAL)
        seekBarTip.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvTipPercentLabel.text = "$progress%";
                calculateTipAndTotal()
                setTipStatus(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) { }

            override fun onStopTrackingTouch(seekBar: SeekBar?) { }
        })

        etBase.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                calculateTipAndTotal()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
        })
    }

    private fun setTipStatus(tip_percent : Int) {
        val tipStatus : String
        when(tip_percent){
            in 0..9 -> tipStatus = "Poor"
            in 10..14 -> tipStatus = "Acceptable"
            in 15..20 -> tipStatus = "Nice"
            in 21..25 -> tipStatus = "Great"
            else -> tipStatus = "Amazing!"
        }
        tv_tip_status.text = tipStatus
        val status_color = ArgbEvaluator().evaluate(
            tip_percent.toFloat()/seekBarTip.max,
            ContextCompat.getColor(this, R.color.colorWorstTip),
            ContextCompat.getColor(this, R.color.colorBestTip)
        ) as Int
        tv_tip_status.setTextColor(status_color)
    }

    private fun calculateTipAndTotal() {
        if(etBase.text.isEmpty()) {
            tv_tip_amount.text = "0"
            tv_total_amount.text = "0"
            return
        }
        val base = etBase.text.toString().toDouble()
        val tip = base * seekBarTip.progress / 100
        val total = base + tip
        tv_tip_amount.text = "%.2f".format(tip)
        tv_total_amount.text = "%.2f".format(total)
    }
}