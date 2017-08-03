package com.vaporware.reverendcode.jsondisplay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick



class MainActivity : AppCompatActivity() {
    val apiBaseUrl = "https://newt.nersc.gov/newt"
    val apiMotd = "/status/motd"
    val apiLogin = "/login"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * This is where we need to check UserPrefs for the existence of user/pass
         * login automagically if we already have it, and prompt for login info if not.
         * */

        val prefs = this.getSharedPreferences("com.vaporware.reverendcode.jsondisplay.prefs",0)
            alert("Please enter your username and password") {
                customView {
                    verticalLayout {
                        val user = editText {
                            hint = "Username"
                        }
                        val pass = editText {
                            hint = "Password"
                        }
                        positiveButton("Submit") {
                            prefs.edit().putString("USER", user.text.toString()).apply()
                            prefs.edit().putString("PASS", pass.text.toString()).apply()
                            prefs.edit().putBoolean("HAS_USER",true).apply()
                            ApiManager(apiBaseUrl).let {
                                val attempt = it.post(apiLogin, hashMapOf(
                                        "username" to prefs.getString("USER","no"),
                                        "password" to prefs.getString("PASS", "stop")
                                ))
                                async(kotlinx.coroutines.experimental.android.UI) {
                                    toast(attempt.await())
                                }
                            }
                        }
                    }
                }
            }.show()

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

