package com.szcloud8.app.deliver.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class HomePageAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var mTitleList = ArrayList<String>()

    private var mFragmentList = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment = mFragmentList[position]

    override fun getCount(): Int = mFragmentList.size

    override fun getPageTitle(position: Int): CharSequence? = mTitleList[position]

    fun setTitles(titleList: List<String>) {
        mTitleList.clear()
        mTitleList.addAll(titleList)
    }

    fun setFragmentList(fragmentList: List<Fragment>) {
        mFragmentList.clear()
        mFragmentList.addAll(fragmentList)
    }
}