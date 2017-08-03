package com.vaporware.reverendcode.jsondisplay

import kotlinx.coroutines.experimental.Deferred
import org.jetbrains.anko.coroutines.experimental.bg
import java.net.URL

/**
 * Created by ReverendCode on 8/2/17.
 */


class ApiManager (val base_url: String) {

    fun get(location: String): Deferred<String> {
            return bg { URL("$base_url/$location").openConnection().getInputStream().bufferedReader().readText() }
    }

    fun post(location: String, data: HashMap<String,String>): Deferred<String> {
//        first pass. this syntax is probably wrong.

        var words = ""
        for ( (key, value) in data) {
            words += "$key : $value&"
        }
        return bg {
            val connection = URL("$base_url/$location").openConnection()
            connection.doOutput = true
            connection.getOutputStream().bufferedWriter().write(words)
            connection.getInputStream().bufferedReader().readText()
        }
    }
}