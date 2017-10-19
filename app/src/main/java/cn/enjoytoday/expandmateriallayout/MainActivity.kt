package cn.enjoytoday.expandmateriallayout

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        hello_world.text = "完美的写法"

        val view= View.inflate(this,android.R.layout.activity_list_item,null)
        layout.addView(view)

        val text1=view.findViewById(android.R.id.text1) as TextView
        text1.text ="Test"

    }
}
