package com.szcloud8.app.deliver.activity.home

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.szcloud8.app.deliver.R
import com.szcloud8.app.deliver.activity.user.LoginActivity
import com.szcloud8.app.deliver.activity.user.UserActivity
import com.szcloud8.app.deliver.adapter.HomePageAdapter
import com.ybg.app.base.constants.MessageEvent
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class MainActivity : AppCompatActivity() {

    private val mEventBus = EventBus.getDefault()

    private var fragmentList = listOf<Fragment>(Deliver0Fragment(),
            Deliver1Fragment(), Deliver2Fragment())
    private var titleList = listOf("待配送", "配送中", "己完成")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pageAdapter = HomePageAdapter(supportFragmentManager)
        pageAdapter.setFragmentList(fragmentList)
        pageAdapter.setTitles(titleList)
        view_pager.adapter = pageAdapter

        tab_layout.setupWithViewPager(view_pager)

        mEventBus.register(this)
    }

    override fun onDestroy() {
        mEventBus.unregister(this)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            startActivity(Intent(this, UserActivity::class.java))
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    @Subscribe
    fun onEvent(event: MessageEvent) {
        if (event.what == MessageEvent.MESSAGE_USER_LOGOUT) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }

}
