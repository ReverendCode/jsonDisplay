package com.vaporware.reverendcode.jsondisplay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.coroutines.experimental.bg
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.net.URL


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        displayUI()
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

    fun displayUI() {
        verticalLayout {
            val mButton = button {
                text = "Click me for JSON"
            }
            val mTextView = textView {
                hint = "Json will appear here"
            }

            mButton.onClick {

//                toast("Attempting to fetch")
                async(UI) {
                    val data: Deferred<String> = bg {
//                        for some reason, this call is not returning anything..
                        URL("https://newt.nersc.gov/newt/status/motd").openConnection().getInputStream().bufferedReader().readText()

                    }
                mTextView.text = data.await()
                }
            }
        }
    }
}
