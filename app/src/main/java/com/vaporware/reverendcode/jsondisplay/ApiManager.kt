package com.vaporware.reverendcode.jsondisplay
import kotlinx.coroutines.experimental.Deferred
import org.jetbrains.anko.coroutines.experimental.bg
import java.net.URL
import java.net.URLEncoder

import java.io.OutputStreamWriter


/**
 * Created by ReverendCode on 8/2/17.
 */


class ApiManager (val base_url: String) {



    fun get(location: String): Deferred<String> {
        return bg { URL("$base_url$location").openConnection().getInputStream().bufferedReader().readText() }
    }

    fun post(location: String, data: HashMap<String,String>): Deferred<String> {
        var words = ""
        for ( (key, value) in data) {
            words += "${URLEncoder.encode(key, "UTF-8")}=${URLEncoder.encode(value, "UTF-8")}&"
        }
        return bg {
            // Send POST data request
            val conn = URL(base_url+location).openConnection()
            conn.doOutput = true
            with(OutputStreamWriter(conn.getOutputStream())) {
                write(words)
                flush()
            }
            conn.getInputStream().bufferedReader().readText()
        }
    }

}
