package com.vaporware.reverendcode.jsondisplay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.sdk25.coroutines.onClick



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
            val loginButton = button {
                text = "Login[DEBUG]"
            }
            val motdButton = button {
                text = "Click me for MOTD"
            }
            val mTextView = textView {
                text = "Json will appear here"
            }
            motdButton.onClick {
                longToast("Fetching MOTD")

                val base_url_test_val = "https://jsonplaceholder.typicode.com"
                val posts_get_test = "/posts/1"

                val apiBaseUrl = "https://newt.nersc.gov/newt"
                val apiMotd = "/status/motd"

                val api = ApiManager(base_url_test_val)
                async(UI) {
                    mTextView.text = api.get(posts_get_test).await()
                    toast("the await has returned")
                }

                }
            loginButton.onClick {
                longToast("Attempting Post")
            }
        }
    }
}

