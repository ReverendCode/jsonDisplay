package com.vaporware.reverendcode.jsondisplay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.button
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textView
import org.jetbrains.anko.verticalLayout
import kotlin.properties.Delegates

class StatusActivity: AppCompatActivity() {
    private var ApiBaseUrl by Delegates.notNull<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiBaseUrl = intent.extras.getString("base_url")
        statusUI()
    }
    private fun statusUI() {
        verticalLayout {
            val motdButton = button {
                text = "Click for MOTD"
            }
            val textField = textView {
                hint = "information goes here"
            }
        motdButton.onClick {
            ApiManager(ApiBaseUrl).let {
                val attempt = it.get("/status/motd/")
                textField.text = attempt.await()
            }
        }
        }
    }
}
