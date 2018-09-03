package com.ybg.app.base.utils

import android.text.TextUtils
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import android.provider.MediaStore.Images.ImageColumns
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.ybg.app.base.constants.AppConstant
import java.text.DecimalFormat

object FileUtils {

    private val TAG = "FileUtils"
    val FILE_EXTENSION_SEPARATOR = "."
    val KB = 1024.0
    val MB = KB * KB
    val GB = KB * KB * KB

    /**
     * 创建文件夹
     */
    fun createDirs(dirPath: String): Boolean {
        val file = File(dirPath)
        if (!file.exists() || !file.isDirectory) {
            return file.mkdirs()
        }
        return false
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    fun copyFile(srcPath: String, destPath: String, deleteSrc: Boolean): Boolean {
        val srcFile = File(srcPath)
        val destFile = File(destPath)
        return copyFile(srcFile, destFile, deleteSrc)
    }

    /**
     * 复制文件，可以选择是否删除源文件
     */
    fun copyFile(srcFile: File, destFile: File, deleteSrc: Boolean): Boolean {
        if (!srcFile.exists() || !srcFile.isFile) {
            return false
        }
        var inputStream: InputStream? = null
        var out: OutputStream? = null
        try {
            inputStream = FileInputStream(srcFile)
            out = FileOutputStream(destFile)
            val buffer = ByteArray(1024)
            var i = inputStream.read(buffer)
            while (i > 0) {
                out.write(buffer, 0, i)
                out.flush()
                i = inputStream.read(buffer)
            }
            if (deleteSrc) {
                srcFile.delete()
            }
        } catch (e: Exception) {
            LogUtil.e(e.toString())
            return false
        } finally {
            close(out)
            close(inputStream)
        }
        return true
    }

    /**
     * 判断文件是否可写
     */
    fun isWriteable(path: String): Boolean {
        try {
            if (TextUtils.isEmpty(path)) {
                return false
            }
            val f = File(path)
            return f.exists() && f.canWrite()
        } catch (e: Exception) {
            LogUtil.e(e.toString())
            return false
        }

    }

    /**
     * 修改文件的权限,例如"777"等
     */
    fun chmod(path: String, mode: String) {
        try {
            val command = "chmod $mode $path"
            val runtime = Runtime.getRuntime()
            runtime.exec(command)
        } catch (e: Exception) {
            LogUtil.e(e.toString())
        }

    }

    /**
     * 把数据写入文件
     *
     * @param is       数据流
     * @param path     文件路径
     * @param recreate 如果文件存在，是否需要删除重建
     *
     * @return 是否写入成功
     */
    fun writeFile(inputStream: InputStream?, path: String, recreate: Boolean): Boolean {
        var res = false
        val f = File(path)
        var fos: FileOutputStream? = null
        try {
            if (recreate && f.exists()) {
                f.delete()
            }
            if (!f.exists() && null != inputStream) {
                val parentFile = File(f.parent)
                parentFile.mkdirs()
                val buffer = ByteArray(1024)
                fos = FileOutputStream(f)
                var count = inputStream.read(buffer)
                while (count != -1) {
                    fos.write(buffer, 0, count)
                    count = inputStream.read(buffer)
                }
                res = true
            }
        } catch (e: Exception) {
            LogUtil.e(e.toString())
        } finally {
            close(fos)
            close(inputStream)
        }
        return res
    }

    /**
     * 把字符串数据写入文件
     *
     * @param content 需要写入的字符串
     * @param path    文件路径名称
     * @param append  是否以添加的模式写入
     *
     * @return 是否写入成功
     */
    fun writeFile(content: ByteArray, path: String, append: Boolean): Boolean {
        var res = false
        val f = File(path)
        var raf: RandomAccessFile? = null
        try {
            if (f.exists()) {
                if (!append) {
                    f.delete()
                    f.createNewFile()
                }
            } else {
                f.createNewFile()
            }
            if (f.canWrite()) {
                raf = RandomAccessFile(f, "rw")
                raf.seek(raf.length())
                raf.write(content)
                res = true
            }
        } catch (e: Exception) {
            LogUtil.e(e.toString())
        } finally {
            close(raf)
        }
        return res
    }

    /**
     * 把字符串数据写入文件
     *
     * @param content 需要写入的字符串
     * @param path    文件路径名称
     * @param append  是否以添加的模式写入
     *
     * @return 是否写入成功
     */
    fun writeFile(content: String, path: String, append: Boolean): Boolean {
        return writeFile(content.toByteArray(), path, append)
    }

    /**
     * 把键值对写入文件
     *
     * @param filePath 文件路径
     * @param key      键
     * @param value    值
     *
     * @param comment  该键值对的注释
     */
    fun writeProperties(filePath: String, key: String, value: String, comment: String) {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(filePath)) {
            return
        }
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            fis = FileInputStream(f)
            val p = Properties()
            p.load(fis)// 先读取文件，再把键值对追加到后面
            p.setProperty(key, value)
            fos = FileOutputStream(f)
            p.store(fos, comment)
        } catch (e: Exception) {
            LogUtil.e(e.toString())
        } finally {
            close(fis)
            close(fos)
        }
    }

    /**
     * 根据值读取
     */
    fun readProperties(filePath: String, key: String, defaultValue: String): String? {
        if (TextUtils.isEmpty(key) || TextUtils.isEmpty(filePath)) {
            return null
        }
        var value: String? = null
        var fis: FileInputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            fis = FileInputStream(f)
            val p = Properties()
            p.load(fis)
            value = p.getProperty(key, defaultValue)
        } catch (e: IOException) {
            LogUtil.e(e.toString())
        } finally {
            close(fis)
        }
        return value
    }

    /**
     * 把字符串键值对的map写入文件
     */
    fun writeMap(filePath: String, map: Map<String, String>?, append: Boolean, comment: String) {
        if (map == null || map.size == 0 || TextUtils.isEmpty(filePath)) {
            return
        }
        var fis: FileInputStream? = null
        var fos: FileOutputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            val p = Properties()
            if (append) {
                fis = FileInputStream(f)
                p.load(fis)// 先读取文件，再把键值对追加到后面
            }
            p.putAll(map)
            fos = FileOutputStream(f)
            p.store(fos, comment)
        } catch (e: Exception) {
            LogUtil.e(e.toString())
        } finally {
            close(fis)
            close(fos)
        }
    }

    /**
     * 把字符串键值对的文件读入map
     */
    @SuppressWarnings("rawtypes", "unchecked")
    fun readMap(filePath: String, defaultValue: String): Map<String, String>? {
        if (TextUtils.isEmpty(filePath)) {
            return null
        }
        var map: Map<String, String>? = null
        var fis: FileInputStream? = null
        val f = File(filePath)
        try {
            if (!f.exists() || !f.isFile) {
                f.createNewFile()
            }
            fis = FileInputStream(f)
            val p = Properties()
            p.load(fis)
            map = p as Map<String, String>// 因为properties继承了map，所以直接通过p来构造一个map
        } catch (e: Exception) {
            LogUtil.e(e.toString())
        } finally {
            close(fis)
        }
        return map
    }

    /**
     * 改名
     */
    fun copy(src: String, des: String, delete: Boolean): Boolean {
        val file = File(src)
        if (!file.exists()) {
            return false
        }
        val desFile = File(des)
        var inputStream: FileInputStream? = null
        var out: FileOutputStream? = null
        try {
            inputStream = FileInputStream(file)
            out = FileOutputStream(desFile)
            val buffer = ByteArray(1024)
            var count = inputStream.read(buffer)
            while (count != -1) {
                out.write(buffer, 0, count)
                out.flush()
                count = inputStream.read(buffer)
            }
        } catch (e: Exception) {
            LogUtil.e(e.toString())
            return false
        } finally {
            close(inputStream)
            close(out)
        }
        if (delete) {
            file.delete()
        }
        return true
    }

    fun getDirSizeInByte(file: File?): Long {
        try {
            //判断文件是否存在
            if (file != null && file.exists()) {
                //如果是目录则递归计算其内容的总大小
                if (file.isDirectory) {
                    val children = file.listFiles()
                    var size: Long = 0
                    for (f in children) {
                        size += getDirSizeInByte(f)
                    }
                    return size
                } else {
                    return file.length()
                }//如果是文件则直接返回其大小
            }
        } catch (e: Exception) {
            e.message?.let { LogUtil.e(it) }
        }

        return 0
    }

    private fun close(io: Closeable?) {
        if (io != null) {
            try {
                io.close()
            } catch (e: IOException) {
                LogUtil.e(e.toString())
            }

        }
    }

    fun deleteFile(path: String): Boolean {
        if (TextUtils.isEmpty(path)) {
            return true
        }

        val file = File(path)
        if (!file.exists()) {
            return true
        }
        if (file.isFile) {
            return file.delete()
        }
        if (!file.isDirectory) {
            return false
        }
        for (f in file.listFiles()) {
            if (f.isFile) {
                f.delete()
            } else if (f.isDirectory) {
                deleteFile(f.absolutePath)
            }
        }
        return file.delete()
    }

    fun deleteFile(path: File?): Boolean {
        if (path == null) {
            return false
        }
        return deleteFile(path.absolutePath)
    }

    fun getFileExtension(filePath: String): String {
        if (TextUtils.isEmpty(filePath)) {
            return filePath
        }

        val extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR)
        val filePosi = filePath.lastIndexOf(File.separator)
        if (extenPosi == -1) {
            return ""
        }
        return if (filePosi >= extenPosi) "" else filePath.substring(extenPosi + 1)
    }


    fun makeFolders(filePath: String): Boolean {
        return makeDirs(filePath)
    }

    fun makeFolders(filePath: File): Boolean {
        return makeDirs(filePath.absolutePath)
    }

    fun makeDirs(filePath: String): Boolean {
        val folderName = getFolderName(filePath)
        if (TextUtils.isEmpty(folderName)) {
            return false
        }

        val folder = File(folderName)
        return if (folder.exists() && folder.isDirectory) true else folder.mkdirs()
    }

    fun getFolderName(filePath: String): String {

        if (TextUtils.isEmpty(filePath)) {
            return filePath
        }

        val filePosi = filePath.lastIndexOf(File.separator)
        return if (filePosi == -1) "" else filePath.substring(0, filePosi)
    }

    /**
     * 给指定的文件名按照时间命名
     */
    private val OUTGOING_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS")

    /**
     * 得到输出的Video保存路径

     * @return
     */
    fun newOutgoingFilePath(): String {
        val str = OUTGOING_DATE_FORMAT.format(Date())
        val fileName = AppConstant.VIDEO_SAVE_PATH + str + ".mp4"
        return fileName
    }

    fun closeStream(stream: Closeable?) {
        try {
            stream?.close()
        } catch (e: IOException) {

        }

    }

    fun getFileName(filePath: String): String {
        if (TextUtils.isEmpty(filePath)) {
            return filePath
        }
//        var extName = getFileExtension(filePath).toLowerCase()
//        val validExtName = arrayOf("jpg", "gif", "png", "jpeg", "bmp")
//        if (extName !in validExtName) {
//            extName = "png"
//        }
        return "${System.currentTimeMillis()}.png"
    }

    fun getResourceName(resource: String): String? {
        if (TextUtils.isEmpty(resource)) {
            return null
        }
        val index = resource.lastIndexOf("/")
        return resource.substring(index + 1)
    }

    fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) return null
        val scheme = uri.scheme
        var data: String? = null
        if (scheme == null)
            data = uri.path
        else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor = context.contentResolver.query(uri, arrayOf(ImageColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index = cursor.getColumnIndex(ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    fun formatFileSize(fileSize: Long): String {
        val df = DecimalFormat("#.00")
        var fileSizeString = ""
        val wrongSize = "0B"
        if (fileSize == 0L) {
            return wrongSize
        }
        if (fileSize < 1024) {
            fileSizeString = df.format(fileSize.toDouble()) + "B"
        } else if (fileSize < 1048576) {
            fileSizeString = df.format(fileSize.toDouble() / 1024) + "KB"
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format(fileSize.toDouble() / 1048576) + "MB"
        } else {
            fileSizeString = df.format(fileSize.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }
}
