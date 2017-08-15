package com.vaporware.reverendcode.jsondisplay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.beust.klaxon.string
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import kotlin.properties.Delegates

class StatusActivity: AppCompatActivity() {
    var ApiBaseUrl by Delegates.notNull<String>()
    var mAdapter by Delegates.notNull<NewtAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ApiBaseUrl = intent.extras.getString("base_url")
        mAdapter = NewtAdapter(this)
        async(UI) {
            val manager = ApiManager(ApiBaseUrl).get("/status/")
            Log.d("async", "retrieving data")
            val data = StringBuilder(manager.await())
            val parsedList: JsonArray<JsonObject> = Parser().parse(data) as JsonArray<JsonObject>
            Log.d("async", "Parsing complete: $parsedList")
            mAdapter.update(parsedList)

        }
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
            listView {
                adapter = mAdapter
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

class NewtAdapter(val activity: StatusActivity) : BaseAdapter() {
    var SystemsList = mutableListOf<Newts>()

    override fun getItem(position: Int): Newts = SystemsList[position]

    override fun getItemId(p0: Int) = SystemsList[p0].id.toLong()

    override fun getCount(): Int = SystemsList.size

    fun update(jsonArray: JsonArray<JsonObject>) {
        var i = 0
        SystemsList.clear()
        jsonArray.mapTo(SystemsList) { Newts(
                it.string("system").toString(),
                it.string("status").toString(),
                i++) }

        notifyDataSetChanged()
        Log.d("Home", SystemsList.toString())
    }

    override fun getView(i: Int, v: View?, parent: ViewGroup?): View {
        val item = getItem(i)
        return with(parent!!.context) {
            linearLayout {
                onClick {
                    toast("Todo: display queue for ${item.name}")
                }
                lparams(width = matchParent, height = wrapContent)
                padding = dip(10)
                textView(item.name)
                textView(item.status)
            }
        }
    }
}

data class Newts (val name: String,
                  val status: String,
                  val id: Int)