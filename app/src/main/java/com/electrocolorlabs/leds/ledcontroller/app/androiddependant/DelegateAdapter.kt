package com.electrocolorlabs.leds.ledcontroller.app.androiddependant

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by Gabriel on 12/23/2017.
 */
class DelegateAdapter(vararg delegates : AdapterDelegate) : RecyclerView.Adapter<DelegateAdapter.AdapterHolder>() {

    private val models: ArrayList<Any> = ArrayList()
    private val delegates: ArrayList<AdapterDelegate> = ArrayList()
    private val noDelegateError: Throwable = Throwable("Missing Adapter Delegate")

    init {
        this.delegates.addAll(delegates)
        noDelegateError.fillInStackTrace()
    }

    override fun getItemViewType(position: Int): Int {
        for (delegate: AdapterDelegate in delegates) {
            if (delegate.canHandelData(models[position])) {
                return delegates.indexOf(delegate)
            }
        }
        val className = models[position].javaClass.simpleName
        val error = Throwable("No AdapterDelegate found for model class $className, did " +
                "you remember to add a proper delegate to the constructor?", noDelegateError)
        throw throw error
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): AdapterHolder {
        return delegates[viewType].onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AdapterHolder?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    class AdapterHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    }

    interface AdapterDelegate {
        fun canHandelData(data : Any): Boolean
        fun onCreateViewHolder(parent: ViewGroup?): AdapterHolder
        fun onBindViewHolder(holder: AdapterHolder?, model: Any)
    }
}