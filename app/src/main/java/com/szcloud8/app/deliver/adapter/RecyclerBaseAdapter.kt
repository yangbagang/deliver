package com.szcloud8.app.deliver.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ybg.app.base.utils.LogUtil

abstract class RecyclerBaseAdapter<T>(protected var mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    protected var TAG = javaClass.simpleName
    var mList: MutableList<T>? = null
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setDataList(list: MutableList<T>) {
        mList = list
    }

    fun addItem(item: T, position: Int) {
        var position = position
        if (mList != null && !mList!!.isEmpty()) {
            position = if (position >= 0) position else mList!!.size
            mList!!.add(position, item)
            notifyItemInserted(position)
        }
    }

    fun removeItem(position: Int) {
        if (mList != null && !mList!!.isEmpty()) {
            if (position < mList!!.size) {
                mList!!.removeAt(position)
                notifyItemRemoved(position)
            }
        }
    }

    fun clearDataList() {
        if (mList != null && !mList!!.isEmpty()) {
            mList!!.clear()
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.mOnItemClickListener = listener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): BaseViewHolder {
        val rootView = LayoutInflater.from(mContext).inflate(rootResource, viewGroup, false)
        return BaseViewHolder(rootView)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        var item: T? = null
        val holder: BaseViewHolder
        if (viewHolder != null) {
            holder = viewHolder as RecyclerBaseAdapter<T>.BaseViewHolder
            if (mList != null && mList!!.size > position) item = mList!![position]
            getView(holder, item, position)

            if (mOnItemClickListener != null) {
                holder.convertView.setOnClickListener { mOnItemClickListener!!.onItemClick(position) }
            }
        } else {
            LogUtil.e(TAG + " RecyclerView ViewHolder can't be null")
        }
    }

    override fun getItemCount(): Int {
        return if (mList != null && mList!!.size > 0) mList!!.size else 0
    }

    abstract val rootResource: Int

    abstract fun getView(viewHolder: BaseViewHolder, item: T?, position: Int)

    inner class BaseViewHolder(val convertView: View) : RecyclerView.ViewHolder(convertView) {
        private val views = SparseArray<View>()

        fun <T : View> getView(resId: Int): T? {
            var v: View? = views.get(resId)
            if (null == v) {
                v = convertView.findViewById(resId)
                views.put(resId, v)
            }
            return v as T?
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }
}