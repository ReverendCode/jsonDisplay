package com.vaporware.reverendcode.jsondisplay

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
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
    val ApiBaseUrl = "https://newt.nersc.gov/newt"
    var mAdapter by Delegates.notNull<NewtAdapter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAdapter = NewtAdapter(this)
        async(UI) {
            mAdapter.update(NewtManager().getStatus())
        }
        statusUI()
    }

    private fun statusUI() {

        verticalLayout {
            button {
                text = "Click for MOTD"
                onClick {
                    alert(NewtManager().getMotd()) {
                        okButton {  }
                    }.show()
                }
            }
            button {
                text = "Refresh status"
                onClick {
                    toast("Refreshing status...")
                    mAdapter.update(NewtManager().getStatus())
                }
            }
            listView {
                adapter = mAdapter
            }
        }
    }
}


class NewtAdapter(val activity: StatusActivity) : BaseAdapter() {
    private var SystemsList = mutableListOf<Newts>()

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
        Log.d("Update", SystemsList.toString())
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
                textView(item.name + "     ")
                textView("Status: " + item.status)
            }
        }
    }
}

data class Newts (val name: String,
                  val status: String,
                  val id: Int)