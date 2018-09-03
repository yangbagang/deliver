package com.ybg.app.base.utils

import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.os.Build
import android.util.Base64
import com.ybg.app.base.constants.AppConstant
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * 对bitmap处理的工具类
 */
object BitmapUtils {

    /**
     * 将bitmap 保存在文件中
     *
     * @param mBitmap  要保存的Bitmap
     * @param filePath 要保存的文件的路径
     */
    fun saveBitmap(mBitmap: Bitmap, filePath: String) {
        saveBitmap(mBitmap, File(AppConstant.IMAGE_SAVE_PATH, filePath))
    }

    /**
     * 将bitmap 保存在文件中
     *
     * @param mBitmap 要保存的Bitmap
     * @param file    要保存的文件
     */
    fun saveBitmap(mBitmap: Bitmap, file: File?) {
        var baos: ByteArrayOutputStream? = null
        var fos: FileOutputStream? = null
        if (file == null) {
            return
        }
        if (!file.exists()) {
            if (!file.parentFile.exists()) {
                file.parentFile.mkdirs()
            }
            file.createNewFile()
        }
        try {
            baos = ByteArrayOutputStream()
            mBitmap.compress(Bitmap.CompressFormat.PNG, 80, baos)
            val bitmapData = baos.toByteArray()
            fos = FileOutputStream(file)
            fos.write(bitmapData)
            fos.flush()
            fos.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                if (fos != null) {
                    fos.flush()
                    fos.close()
                }
                if (baos != null) {
                    baos.flush()
                    baos.close()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }


    /**
     * 将Bitmap 对象转化为Base64字符串
     *
     * @param bitmap 图片
     */
    fun bitmapToBase64(bitmap: Bitmap): String? {

        var out: ByteArrayOutputStream? = null
        try {
            out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)

            out.flush()
            out.close()
            val imgBytes = out.toByteArray()
            return Base64.encodeToString(imgBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            return null
        } finally {
            try {
                out!!.flush()
                out.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    /**
     * 将Base64字符串转化为图片
     */
    fun base64ToBitmap(string: String): Bitmap? {
        //将字符串转换成Bitmap类型
        var bitmap: Bitmap? = null
        try {
            val bitmapArray: ByteArray
            bitmapArray = Base64.decode(string, Base64.NO_WRAP)
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.size)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return bitmap
    }

    /**
     * 对图片进行压缩
     */
    fun compressImage(image: Bitmap, maxSize: Int): Bitmap {

        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > maxSize && options > 0) {  //循环判断如果压缩后图片是否大于100kb,
            // 大于继续压缩
            baos.reset()//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10//每次都减少10
        }
        val isBm = ByteArrayInputStream(baos.toByteArray())//把压缩后的数据baos存放到ByteArrayInputStream中
        val bitmap = BitmapFactory.decodeStream(isBm, null, null)//把ByteArrayInputStream数据生成图片
        return bitmap
    }

    fun resizeImage(bitmap: Bitmap, w: Int, h: Int): Bitmap {
        val bitmapOrg = bitmap
        val width = bitmapOrg.width
        val height = bitmapOrg.height
        val newWidth = w
        val newHeight = h
        
        if (newHeight > width && newHeight > height) {
            return bitmap
        }

        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        val scale = Math.min(scaleWidth, scaleHeight)

        val matrix = Matrix()
        matrix.postScale(scale, scale)
        return Bitmap.createBitmap(bitmapOrg, 0, 0, width,
                height, matrix, true)
    }

    /**
     * 对图片进行高斯模糊效果
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    fun fastBlur(sentBitmap: Bitmap?, radius: Int): Bitmap? {
        if (sentBitmap == null || sentBitmap.config == null) {
            return null
        }
        val bitmap = sentBitmap.copy(sentBitmap.config, true)

        if (radius < 1) {
            return null
        }

        val w = bitmap.width
        val h = bitmap.height

        val pix = IntArray(w * h)
        LogUtil.e("pix" + w + " " + h + " " + pix.size)
        bitmap.getPixels(pix, 0, w, 0, 0, w, h)

        val wm = w - 1
        val hm = h - 1
        val wh = w * h
        val div = radius + radius + 1

        val r = IntArray(wh)
        val g = IntArray(wh)
        val b = IntArray(wh)
        var rSum: Int
        var gSum: Int
        var bSum: Int
        var x: Int
        var y: Int
        var i: Int
        var p: Int
        var yp: Int
        var yi: Int
        var yw: Int
        val vMin = IntArray(Math.max(w, h))

        var divSum = div + 1 shr 1
        divSum *= divSum
        val dv = IntArray(256 * divSum)
        i = 0
        while (i < 256 * divSum) {
            dv[i] = i / divSum
            i++
        }

        yw = 0
        yi = 0

        val stack = Array(div) { IntArray(3) }
        var stackPointer: Int
        var stackStart: Int
        var sir: IntArray
        var rbs: Int
        val r1 = radius + 1
        var routSum: Int
        var goutSum: Int
        var boutSum: Int
        var rinSum: Int
        var ginSum: Int
        var binSum: Int

        y = 0
        while (y < h) {
            rinSum = 0
            ginSum = 0
            binSum = 0
            routSum = 0
            goutSum = 0
            boutSum = 0
            rSum = 0
            gSum = 0
            bSum = 0
            i = -radius
            while (i <= radius) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))]
                sir = stack[i + radius]
                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff
                rbs = r1 - Math.abs(i)
                rSum += sir[0] * rbs
                gSum += sir[1] * rbs
                bSum += sir[2] * rbs
                if (i > 0) {
                    rinSum += sir[0]
                    ginSum += sir[1]
                    binSum += sir[2]
                } else {
                    routSum += sir[0]
                    goutSum += sir[1]
                    boutSum += sir[2]
                }
                i++
            }
            stackPointer = radius

            x = 0
            while (x < w) {

                r[yi] = dv[rSum]
                g[yi] = dv[gSum]
                b[yi] = dv[bSum]

                rSum -= routSum
                gSum -= goutSum
                bSum -= boutSum

                stackStart = stackPointer - radius + div
                sir = stack[stackStart % div]

                routSum -= sir[0]
                goutSum -= sir[1]
                boutSum -= sir[2]

                if (y == 0) {
                    vMin[x] = Math.min(x + radius + 1, wm)
                }
                p = pix[yw + vMin[x]]

                sir[0] = p and 0xff0000 shr 16
                sir[1] = p and 0x00ff00 shr 8
                sir[2] = p and 0x0000ff

                rinSum += sir[0]
                ginSum += sir[1]
                binSum += sir[2]

                rSum += rinSum
                gSum += ginSum
                bSum += binSum

                stackPointer = (stackPointer + 1) % div
                sir = stack[stackPointer % div]

                routSum += sir[0]
                goutSum += sir[1]
                boutSum += sir[2]

                rinSum -= sir[0]
                ginSum -= sir[1]
                binSum -= sir[2]

                yi++
                x++
            }
            yw += w
            y++
        }
        x = 0
        while (x < w) {
            rinSum = 0
            ginSum = 0
            binSum = 0
            routSum = 0
            goutSum = 0
            boutSum = 0
            rSum = 0
            gSum = 0
            bSum = 0
            yp = -radius * w
            i = -radius
            while (i <= radius) {
                yi = Math.max(0, yp) + x

                sir = stack[i + radius]

                sir[0] = r[yi]
                sir[1] = g[yi]
                sir[2] = b[yi]

                rbs = r1 - Math.abs(i)

                rSum += r[yi] * rbs
                gSum += g[yi] * rbs
                bSum += b[yi] * rbs

                if (i > 0) {
                    rinSum += sir[0]
                    ginSum += sir[1]
                    binSum += sir[2]
                } else {
                    routSum += sir[0]
                    goutSum += sir[1]
                    boutSum += sir[2]
                }

                if (i < hm) {
                    yp += w
                }
                i++
            }
            yi = x
            stackPointer = radius
            y = 0
            while (y < h) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = 0xff000000.toInt() and pix[yi] or (dv[rSum] shl 16) or (dv[gSum] shl 8) or dv[bSum]

                rSum -= routSum
                gSum -= goutSum
                bSum -= boutSum

                stackStart = stackPointer - radius + div
                sir = stack[stackStart % div]

                routSum -= sir[0]
                goutSum -= sir[1]
                boutSum -= sir[2]

                if (x == 0) {
                    vMin[y] = Math.min(y + r1, hm) * w
                }
                p = x + vMin[y]

                sir[0] = r[p]
                sir[1] = g[p]
                sir[2] = b[p]

                rinSum += sir[0]
                ginSum += sir[1]
                binSum += sir[2]

                rSum += rinSum
                gSum += ginSum
                bSum += binSum

                stackPointer = (stackPointer + 1) % div
                sir = stack[stackPointer]

                routSum += sir[0]
                goutSum += sir[1]
                boutSum += sir[2]

                rinSum -= sir[0]
                ginSum -= sir[1]
                binSum -= sir[2]

                yi += w
                y++
            }
            x++
        }

        LogUtil.e("pix" + w + " " + h + " " + pix.size)
        bitmap.setPixels(pix, 0, w, 0, 0, w, h)
        return bitmap
    }

    //保存图片文件
    @Throws(FileNotFoundException::class, IOException::class)
    fun saveToFile(fileFolderStr: String, isDir: Boolean, croppedImage: Bitmap): String {
        val jpgFile: File
        if (isDir) {
            val fileFolder = File(fileFolderStr)
            val date = Date()
            val format = SimpleDateFormat("yyyyMMddHHmmss") // 格式化时间
            val filename = format.format(date) + ".jpg"
            if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                FileUtils.makeDirs(fileFolderStr)
            }
            jpgFile = File(fileFolder, filename)
        } else {
            jpgFile = File(fileFolderStr)
            if (!jpgFile.parentFile.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
                FileUtils.makeDirs(jpgFile.parentFile.path)
            }
        }
        val outputStream = FileOutputStream(jpgFile) // 文件输出流

        croppedImage.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        FileUtils.closeStream(outputStream)
        return jpgFile.path
    }

    fun isSquare(imagePath: String): Boolean {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(imagePath, options)
        return options.outHeight == options.outWidth
    }
}
