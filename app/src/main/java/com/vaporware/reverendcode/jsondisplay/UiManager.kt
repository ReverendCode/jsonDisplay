package com.vaporware.reverendcode.jsondisplay
import android.content.SharedPreferences
import android.text.InputType
import android.view.View
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.boolean
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * Created by ReverendCode on 8/9/17.
 */

class LoginView(val prefs: SharedPreferences): AnkoComponent<MainActivity> {
    var savePass: Boolean = false

    override fun createView(ui: AnkoContext<MainActivity>): View {

        val hasPass = prefs.getString("PASS","")
        savePass = hasPass != ""
        return ui.verticalLayout {
            val user = editText {
                hint = "Username"
                setText(prefs.getString("USER","").toString())
            }
            val pass = editText {
                hint = "Password"
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                setText(hasPass)
            }
            val store = checkBox {
                text = "Save Password"
                isChecked = savePass
                onClick {
                    savePass = isChecked
                }
            }
            button {
                text = "Login"
                onClick { doLogin(user.text.toString(), pass.text.toString()) }
            }
        }
    }

    fun doLogin(user: String, pass: String) {
        if (savePass) prefs.edit().putString("PASS",pass).apply()
        else prefs.edit().putString("PASS","").apply()
        prefs.edit().putString("USER",user).apply()
        ApiManager( "https://newt.nersc.gov/newt").let {
            val attempt = it.post("/login/", hashMapOf(
                    "username" to user,
                    "password" to pass))
            async(UI) {
//                parse the result of the attempt to JSON, do something with the result.
                val mJson = Parser().parse(attempt.await()) as JsonObject
                if (mJson.boolean("auth") ?: false) {

                }

            }
        }
    }
}