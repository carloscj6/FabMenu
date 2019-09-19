package com.revosleap.fabmenu

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        initFab()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun initFab() {
        fab.setMiniFabsColors(R.color.colorPrimary, R.color.green_fab)
        fab.setMainFabOnClickListener(View.OnClickListener {
            Toast.makeText(
                this@MainActivity,
                "Main Clicked",
                Toast.LENGTH_SHORT
            ).show()
        })
        fab.setMiniFabSelectedListener(object : OptionsFabLayout.OnMiniFabSelectedListener {
            override fun onMiniFabSelected(fabItem: MenuItem) {
                when (fabItem.itemId) {
                    R.id.fab_add -> {
                        Toast.makeText(this@MainActivity, "Add", Toast.LENGTH_SHORT).show()
                    }
                    R.id.fab_link -> {
                        Toast.makeText(this@MainActivity, "Link", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }
}
