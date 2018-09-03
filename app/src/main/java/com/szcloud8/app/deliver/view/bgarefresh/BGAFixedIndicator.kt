package com.szcloud8.app.deliver.view.bgarefresh

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.support.v4.view.ViewPager
import android.support.v4.view.ViewPager.OnPageChangeListener
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.szcloud8.app.deliver.R

class BGAFixedIndicator @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : LinearLayout(context, attrs), View.OnClickListener, View.OnFocusChangeListener, OnPageChangeListener {
    private val BSSEEID = 0xffff00
    private var mTextColor: ColorStateList? = null
    private var mTextSizeNormal = 12
    private var mTextSizeSelected = 15

    private var mTriangleColor = resources.getColor(R.color.white)
    private var mTriangleHeight = 5
    private var mTriangleHorizontalMargin = 20

    private var mHasDivider = true
    private var mDividerColor = resources.getColor(R.color.black)
    private var mDividerWidth = 3
    private var mDividerVerticalMargin = 10

    private var mPaintFooterTriangle: Paint? = null
    private var mInflater: LayoutInflater? = null

    private var mViewPager: ViewPager? = null

    private var mTabCount = 0
    private var mCurrentTabIndex = 0

    private var mTriangleLeftX = 0

    private val mPath = Path()
    private var mItemWidth: Int = 0

    private var mOnPageChangeListener: OnPageChangeListener? = null

    init {
        initAttrs(context, attrs)
        initDraw(context)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGAFixedIndicator)
        val N = typedArray.indexCount
        for (i in 0..N - 1) {
            initAttr(typedArray.getIndex(i), typedArray)
        }
        typedArray.recycle()
    }

    fun initAttr(attr: Int, typedArray: TypedArray) {
        if (attr == R.styleable.BGAFixedIndicator_indicator_triangleColor) {
            mTriangleColor = typedArray.getColor(attr, mTriangleColor)
        } else if (attr == R.styleable.BGAFixedIndicator_indicator_triangleHorizontalMargin) {
            /**
             * getDimension和getDimensionPixelOffset的功能差不多,都是获取某个dimen的值,如果是dp或sp的单位,将其乘以density,如果是px,则不乘;两个函数的区别是一个返回float,一个返回int. getDimensionPixelSize则不管写的是dp还是sp还是px,都会乘以denstiy.
             */
            mTriangleHorizontalMargin = typedArray.getDimensionPixelSize(attr, mTriangleHorizontalMargin)
        } else if (attr == R.styleable.BGAFixedIndicator_indicator_triangleHeight) {
            mTriangleHeight = typedArray.getDimensionPixelSize(attr, mTriangleHeight)
        } else if (attr == R.styleable.BGAFixedIndicator_indicator_textColor) {
            mTextColor = typedArray.getColorStateList(attr)
        } else if (attr == R.styleable.BGAFixedIndicator_indicator_textSizeNormal) {
            mTextSizeNormal = typedArray.getDimensionPixelSize(attr, mTextSizeNormal)
        } else if (attr == R.styleable.BGAFixedIndicator_indicator_textSizeSelected) {
            mTextSizeSelected = typedArray.getDimensionPixelSize(attr, mTextSizeSelected)
        } else if (attr == R.styleable.BGAFixedIndicator_indicator_hasDivider) {
            mHasDivider = typedArray.getBoolean(attr, mHasDivider)
        } else if (attr == R.styleable.BGAFixedIndicator_indicator_dividerColor) {
            mDividerColor = typedArray.getColor(attr, mDividerColor)
        } else if (attr == R.styleable.BGAFixedIndicator_indicator_dividerWidth) {
            mDividerWidth = typedArray.getDimensionPixelSize(attr, mDividerWidth)
        } else if (attr == R.styleable.BGAFixedIndicator_indicator_dividerVerticalMargin) {
            mDividerVerticalMargin = typedArray.getDimensionPixelSize(attr, mDividerVerticalMargin)
        }
    }

    private fun initDraw(context: Context) {
        mPaintFooterTriangle = Paint()
        mPaintFooterTriangle!!.style = Paint.Style.FILL_AND_STROKE
        mPaintFooterTriangle!!.color = mTriangleColor
        mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    // 初始化选项卡
    fun initData(currentTab: Int, viewPager: ViewPager) {
        this.removeAllViews()
        mViewPager = viewPager
        mTabCount = mViewPager!!.adapter?.count ?: 0

        mViewPager!!.setOnPageChangeListener(this)

        initTab(currentTab)
        postInvalidate()
    }

    private fun initTab(currentTab: Int) {
        for (index in 0..mTabCount - 1) {
            val tabIndicator = mInflater!!.inflate(R.layout.view_indicator, this, false)
            tabIndicator.id = BSSEEID + index
            tabIndicator.setOnClickListener(this)

            val titleTv = tabIndicator.findViewById<TextView>(R.id.tv_indicator_title)
            if (mTextColor != null) {
                titleTv.setTextColor(mTextColor)
            }
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeNormal.toFloat())
            titleTv.text = mViewPager!!.adapter?.getPageTitle(index)

            val tabLp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            tabLp.gravity = Gravity.CENTER
            tabIndicator.layoutParams = tabLp
            // 防止currentTab为0时，第一个tab文字颜色没变化
            if (index == 0) {
                resetTab(tabIndicator, true)
            }
            this.addView(tabIndicator)

            if (index != mTabCount - 1 && mHasDivider) {
                val dividerLp = LinearLayout.LayoutParams(mDividerWidth, LinearLayout.LayoutParams.MATCH_PARENT)
                dividerLp.setMargins(0, mDividerVerticalMargin, 0, mDividerVerticalMargin)
                val vLine = View(context)
                vLine.setBackgroundColor(mDividerColor)
                vLine.layoutParams = dividerLp
                this.addView(vLine)
            }
        }
        setCurrentTab(currentTab)
    }

    private fun setCurrentTab(index: Int) {
        if (mCurrentTabIndex != index && index > -1 && index < mTabCount) {
            val oldTab = findViewById<View>(BSSEEID + mCurrentTabIndex)
            resetTab(oldTab, false)

            mCurrentTabIndex = index
            val newTab = findViewById<View>(BSSEEID + mCurrentTabIndex)
            resetTab(newTab, true)

            if (mViewPager!!.currentItem != mCurrentTabIndex) {
                mViewPager!!.currentItem = mCurrentTabIndex
            }
            postInvalidate()
        }
    }

    private fun resetTab(tab: View, isSelected: Boolean) {
        val tv = tab.findViewById<TextView>(R.id.tv_indicator_title)
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, (if (isSelected) mTextSizeSelected else mTextSizeNormal).toFloat())
        tab.isSelected = isSelected
        tab.isPressed = isSelected
    }

    fun setOnPageChangeListener(onPageChangeListener: OnPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener
    }

    override fun onClick(v: View) {
        val currentTabIndex = v.id - BSSEEID
        setCurrentTab(currentTabIndex)
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        if (v === this && hasFocus) {
            findViewById<View>(BSSEEID + mCurrentTabIndex).requestFocus()
            return
        } else if (hasFocus) {
            for (i in 0..mTabCount - 1) {
                if (getChildAt(i) === v) {
                    setCurrentTab(i)
                    return
                }
            }
        }
    }

    // 注意：必须要设置背景后，该方法才会被调用
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mPath.rewind()
        val left_x = (mTriangleHorizontalMargin + mTriangleLeftX).toFloat()
        val right_x = (mTriangleLeftX + mItemWidth - mTriangleHorizontalMargin).toFloat()
        val top_y = (height - mTriangleHeight).toFloat()
        val bottom_y = height.toFloat()

        mPath.moveTo(left_x, top_y)
        mPath.lineTo(right_x, top_y)
        mPath.lineTo(right_x, bottom_y)
        mPath.lineTo(left_x, bottom_y)
        mPath.close()

        canvas.drawPath(mPath, mPaintFooterTriangle!!)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mItemWidth = width
        if (mTabCount != 0) {
            mItemWidth = width / mTabCount
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        mTriangleLeftX = (mItemWidth * (position + positionOffset)).toInt()

        postInvalidate()

        if (mOnPageChangeListener != null) {
            mOnPageChangeListener!!.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    override fun onPageSelected(position: Int) {
        setCurrentTab(position)

        if (mOnPageChangeListener != null) {
            mOnPageChangeListener!!.onPageSelected(position)
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener!!.onPageScrollStateChanged(state)
        }
    }
}
