package com.kasao.qintai.adapter

import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.kasao.qintai.R
import com.kasao.qintai.base.BaseKSadapter
import com.kasao.qintai.base.BaseViewHolder
import com.kasao.qintai.model.CarDetailEntity
import com.kasao.qintai.widget.ItemCarDisplayA
import com.kasao.qintai.widget.ItemCarDisplayB
import com.kasao.qintai.widget.ItemCarDisplayDel
import java.util.*

/**
 * 作者 :created  by suochunming
 * 日期：2018/8/24 0024:14
 */

class CarSearchAdapter : BaseKSadapter<CarDetailEntity>() {
    var allData: MutableList<CarDetailEntity>? = null
    private var mHandler: Handler? = Handler()
    val DISPLAYA = 100
    val DISPLAYB = 101
    val DISPLAYDEL = 102
    private var isDisplay = false// a,b 两种样式转换
    var isDelete = false
        set(value) {
            field = value
        }
    private var index = -1

    override fun getHeaderItemCount(): Int {
        if (null == allData || allData!!.isEmpty()) {
            return 1
        } else {
            return 0
        }
    }

    override fun onCreateHeaderItemViewHolder(parent: ViewGroup?, headerViewType: Int): BaseViewHolder<CarDetailEntity>? {
        return HeadViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.search_nodata_display, parent, false))

    }

    override fun getContentItemCount(): Int {
        if (null == allData || allData!!.isEmpty()) {
            return 0
        } else {
            return allData!!.size
        }
    }

    override fun getContentItemViewType(position: Int): Int {
        return if (isDelete) {
            DISPLAYDEL
        } else {
            if (isDisplay) {
                DISPLAYB
            } else {
                DISPLAYA
            }
        }
    }

    override fun onCreateContentItemViewHolder(parent: ViewGroup?, contentViewType: Int): BaseViewHolder<CarDetailEntity>? {
        when (contentViewType) {
            DISPLAYA -> return ContentViewHolderA(ItemCarDisplayA(parent!!.context))
            DISPLAYB -> return ContentViewHolderB(ItemCarDisplayB(parent!!.context))
            DISPLAYDEL -> return ContentViewHolderDel(ItemCarDisplayDel(parent!!.context))
        }
        return null
    }

    override fun onBindContentItemViewHolder(contentViewHolder: BaseViewHolder<CarDetailEntity>?, position: Int) {
        if (null != allData) {
            contentViewHolder?.rendView(allData!!.get(position), position)
        }
    }

    inner class ContentViewHolderA(item: View) : BaseViewHolder<CarDetailEntity>(item) {
        var displayA: ItemCarDisplayA

        init {
            displayA = item as ItemCarDisplayA
        }

        override fun rendView(t: CarDetailEntity, position: Int) {
            displayA.rendView(t, false, false)
            displayA.setOnClickListener { mCarSellAction?.onCarDetaile(t) }
        }
    }

    inner class ContentViewHolderDel(item: View) : BaseViewHolder<CarDetailEntity>(item) {
        var displayDel: ItemCarDisplayDel

        init {
            displayDel = item as ItemCarDisplayDel
        }

        override fun rendView(t: CarDetailEntity, position: Int) {
            displayDel.rendView(t, false, false)
            displayDel.setmIcarDel(object : ItemCarDisplayDel.ICarDel {
                override fun onDelStoreCar(entity: CarDetailEntity?, index: Int) {
                    mCarSellAction?.onCarDel(t,index)
                }
            })
            displayDel.setOnClickListener { mCarSellAction?.onCarDetaile(t) }
            displayDel.setState(position == index)
            displayDel.setOnLongClickListener(object : View.OnLongClickListener {
                override fun onLongClick(p0: View?): Boolean {
                    if (position == index) {
                        displayDel.setState(false)
                        index = -1
                    } else {
                        index = position
                    }
                    notifyDataSetChanged()
                    return true
                }
            })
        }

    }

    inner class ContentViewHolderB(item: View) : BaseViewHolder<CarDetailEntity>(item) {
        var displayB: ItemCarDisplayB

        init {
            displayB = item as ItemCarDisplayB

        }

        override fun rendView(t: CarDetailEntity, position: Int) {
            displayB.rendView(t)
            displayB.setOnClickListener {
                index = -1
                mCarSellAction?.onCarDetaile(t)
            }
        }
    }


    class HeadViewHolder(item: View) : BaseViewHolder<CarDetailEntity>(item) {
        var tvMsg: TextView

        init {
            tvMsg = item.findViewById(R.id.tv_msg)
        }
    }

    // 收藏中移除
    fun removeItem(index: Int) {
        var index = index
        if (null != allData) {
            allData?.removeAt(index)
            notifyContentItemRemoved(index)
            index = -1
        }
    }
    fun setDatas(data: MutableList<CarDetailEntity>) {
        isOnlyLoadingOne = false
        if (null != data) {
            if (null == allData) {
                allData = ArrayList()
            }
            val start = allData!!.size
            val len = data.size
            allData!!.addAll(data)
            notifyContentItemRangeInserted(start, len)
        }

    }

    private var mCarSellAction: ICarSellAction? = null

    fun setCarSellAction(mCarSellAction: ICarSellAction) {
        this.mCarSellAction = mCarSellAction
    }

    fun setItemStyle(isItemStyle: Boolean) {
        isDisplay = isItemStyle
        mHandler?.postDelayed(Runnable { notifyDataSetChanged() }, 200)

    }

    fun setFresh(data: MutableList<CarDetailEntity>?) {
        if (null != data && !data.isEmpty()) {
            mHandler?.postDelayed(Runnable {
                if (null == allData) {
                    allData = ArrayList()
                }
                allData?.clear()
                allData = data
                notifyDataSetChanged()
            }, 300)

        }
    }

    fun setNodata() {
        mHandler?.postDelayed(Runnable {
            if (null != allData && !allData!!.isEmpty()) {
                allData?.clear()
                notifyDataSetChanged()
            }
        }, 300)
    }

    fun onDestroy() {
        if (null != mHandler) {
            mHandler?.removeCallbacks(null)
            mHandler = null
        }
    }

    interface ICarSellAction {
        fun onCarDetaile(entity: CarDetailEntity)
        fun onCarDel(entity: CarDetailEntity,index:Int)
    }
}