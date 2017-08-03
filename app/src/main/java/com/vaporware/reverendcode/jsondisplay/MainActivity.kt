package com.vaporware.reverendcode.jsondisplay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import org.jetbrains.anko.*
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

        val base_url_test_val = "http://androidexample.com"
        val posts_get_test = "/media/webservice/httppost.php"
        val apiBaseUrl = "https://newt.nersc.gov/newt"
        val apiMotd = "/status/motd"

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
                ApiManager(apiBaseUrl).let {
                    mTextView.text = it.get(apiMotd).await()
                }
            }
            loginButton.onClick {
                longToast("Attempting Post")
                ApiManager(base_url_test_val).let {
                    mTextView.text = it.post(posts_get_test, hashMapOf(
                            "name" to "jim",
                            "email" to "a@b.com",
                            "user" to "jeb",
                            "pass" to "complexPassword")).await()
                }
            }
        }
    }
}

