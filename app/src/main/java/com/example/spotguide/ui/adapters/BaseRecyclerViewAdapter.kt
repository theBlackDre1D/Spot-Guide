package com.example.spotguide.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.spotguide.ui.ViewHolders
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

open class BaseRecyclerViewAdapter<M: Any, VH: ViewHolders.BaseViewHolder<M>>(
//    private val viewResId: Int,
    private var models: MutableList<M>? = mutableListOf(),
    private val viewHolderClass: KClass<VH>,
    private val bind: (VH, M, Int) -> Unit
): RecyclerView.Adapter<VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(getView(parent.context), parent, false)
        val viewHolderInstance = viewHolderClass.primaryConstructor // kind of finding reference to primary constructor (not no-args constructor)
        return viewHolderInstance!!.call(v) // call primary constructor with parameter/parameters
    }

    private fun getView(context: Context): Int {
        val tmpView = View(context)
        val viewHolderConstructor = viewHolderClass.primaryConstructor
        val instance = viewHolderConstructor!!.call(tmpView)
        return instance.viewResId
    }

    val adapterData: List<M>?
        get() = models?.toList()

    override fun getItemCount() = models?.size ?: run { 0 }
    override fun onBindViewHolder(holder: VH, position: Int) {
        models?.let { bind(holder, it[position], position) }
    }

    fun addData(data: List<M>?, reset: Boolean = false) {
        data?.let {
            if (reset) models = it.toMutableList()
            else models?.plusAssign(it)
            notifyDataSetChanged()
        }
    }

    fun addOnlyNewData(data: List<M>?) {
        val oldCount = models!!.size
        data?.let {
            it.forEach { item ->
                if (!models!!.contains(item)) models?.add(item)
            }
        }
        val currentCount = models!!.size
        notifyItemRangeChanged(currentCount, currentCount - oldCount)
    }

    fun removeData(data: List<M>) {
        models?.let { actualModels ->
            data.forEach {
                if (actualModels.contains(it)) models?.remove(it)
            }
        }
        notifyDataSetChanged()
    }

    fun deleteElement(element: M) {
        models?.remove(element)
        notifyDataSetChanged()
    }

    fun clearData() {
        models?.clear()
        notifyDataSetChanged()
    }
}