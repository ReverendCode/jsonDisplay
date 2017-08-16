package com.vaporware.reverendcode.jsondisplay

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.boolean
import com.beust.klaxon.string
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick



class MainActivity : AppCompatActivity() {
    val apiBaseUrl = "https://newt.nersc.gov/newt"
    val apiMotd = "/status/motd"
    val apiLogin = "/login/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginUI()

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

    private fun loginUI() {
        val prefs = getSharedPreferences("com.vaporware.reverendcode.jsondisplay.prefs",0)
        verticalLayout {
            val name = editText {
                hint = "Username"
            }
            val pass = editText {
                hint = "Password"
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            val keepPass = checkBox("Save Password") {
                isChecked = (prefs.getString("PASS","") != "")
            }
            button {
                text = "Login"
                onClick {
                    prefs.edit().putString("USER", name.text.toString())
                                .putString("PASS",
                                    if (keepPass.isChecked) pass.text.toString() else "").apply()

                    ApiManager("https://newt.nersc.gov/newt").let {
                        val attempt = it.post("/login/", hashMapOf(
                                "username" to name.text.toString(),
                                "password" to pass.text.toString()
                        ))
                        toast("Logging in, please wait..")
                        Log.d("Main","attempting login")
                        val sBuilder = StringBuilder("{\"username\": null, \"session_lifetime\": 0, \"auth\": true, \"newt_sessionid\": null}")
                        val resultJson: JsonObject = Parser().parse(sBuilder) as JsonObject
                        if (resultJson.boolean("auth") ?: false) {
                            toast("Welcome, ${resultJson.string("username")}, Login successful!")
                            startActivity<StatusActivity>("base_url" to "https://newt.nersc.gov/newt")
                        } else {
                            toast("Login failed, please try again.")
                            pass.setText("")
                        }
                    }
                }
            }
        }
    }

    private fun displayUI() {
        verticalLayout {
            val loginButton = button {
                text = "Login[DEBUG]"
            }
            val motdButton = button {
                text = "Click me for MOTD"
            }
            val mTextView = textView {
                hint = "Json will appear here"
            }

            motdButton.onClick {
                longToast("Fetching MOTD")
                ApiManager(apiBaseUrl).let {
                    mTextView.text = it.get(apiMotd).await()
                }
            }
            loginButton.onClick {
                longToast("Attempting Post")
                mTextView.isVerticalScrollBarEnabled = true
                ApiManager("http://jsonplaceholder.typicode.com").let {
                    mTextView.text = it.post("/posts", hashMapOf(
                            "title" to "jim",
                            "body" to "a@b.com",
                            "userId" to "1"
                            )).await()
                }
            }
        }
    }
}

