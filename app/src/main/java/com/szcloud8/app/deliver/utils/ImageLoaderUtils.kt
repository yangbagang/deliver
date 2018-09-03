package com.szcloud8.app.deliver.utils

import android.content.Context
import android.graphics.Bitmap
import android.support.annotation.DrawableRes
import android.widget.ImageView
import com.szcloud8.app.deliver.R
import com.szcloud8.app.deliver.app.ShowApplication
import com.ybg.app.base.constants.AppConstant
import com.ybg.app.base.picasso.Picasso
import com.ybg.app.base.utils.FileUtils
import java.io.File
import java.io.IOException

/**
 * 用来加载图片的工具类对Picasso一些常用的方法进行封装
 */
class ImageLoaderUtils(private val mContext: Context) {

    /**
     * 加载图片 assets, files, content providers

     * @param url  网络图片地址/本地文件路径/本地图片uri
     * *
     * @param view 将加载的图片现实的view
     */
    fun loadBitmap(view: ImageView, url: String) {
        Picasso.with(mContext).load(url).placeholder(RES_ID_PLACEHOLDER).error(RES_ID_ERROR).into(view)
    }

    /**
     * 加载图片 Resources

     * @param resourceId 需要加载的DrawableRes资源
     * *
     * @param view       将加载的图片现实的view
     */
    fun loadBitmap(view: ImageView, @DrawableRes resourceId: Int) {
        Picasso.with(mContext).load(resourceId).placeholder(RES_ID_PLACEHOLDER).error(RES_ID_ERROR).into(view)
    }

    /**
     * 转换图片以适合所显示的ImageView，来减少内存消耗

     * @param url    网络图片地址/本地文件路径/本地图片uri
     * *
     * @param view   将加载的图片现实的view
     * *
     * @param width  所需bitmap的宽
     * *
     * @param height 所需bitmap的高
     */
    fun loadBitmap(view: ImageView, url: String, width: Int, height: Int) {
        Picasso.with(mContext).load(url).resize(width, height).centerCrop().placeholder(RES_ID_PLACEHOLDER).error(RES_ID_ERROR).into(view)
    }

    fun loadFileBitmap(view: ImageView, path: String, width: Int, height: Int) {
        Picasso.with(mContext).load(File(path)).resize(width, height).centerCrop().placeholder(RES_ID_PLACEHOLDER).error(RES_ID_PLACEHOLDER).into(view)
    }

    fun loadFileBitmap(view: ImageView, path: String) {
        Picasso.with(mContext).load(File(path)).placeholder(RES_ID_PLACEHOLDER).error(RES_ID_PLACEHOLDER).into(view)
    }

    fun loadBitmap(url: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = Picasso.with(mContext).load(url).get()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bitmap
    }

    /**
     * 获取缓存图片的文件夹大小
     */
    val imgCacheSize: String
        get() {
            val cacheSize = String.format("%.2f M", FileUtils.getDirSizeInByte(File(AppConstant.IMAGE_CACHE_PATH)).toDouble() / 1024.0 / 1024.0)
            return cacheSize
        }

    companion object {
        private val RES_ID_ERROR = R.mipmap.ic_launcher//加载出错时出现的图片
        private val RES_ID_PLACEHOLDER = R.mipmap.ic_launcher//加载时出现的缓存图片
        private var mImageLoaderUtil: ImageLoaderUtils? = null

        val instance: ImageLoaderUtils
            get() {
                val context = ShowApplication.instance!!
                if (mImageLoaderUtil == null)
                    synchronized(ImageLoaderUtils::class.java) {
                        if (mImageLoaderUtil == null) mImageLoaderUtil = ImageLoaderUtils(context)
                    }
                return mImageLoaderUtil!!
            }
    }

}
