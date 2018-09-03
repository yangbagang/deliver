package com.ybg.app.base.http.parser

import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

import okhttp3.Response

class OkFileParser(private val mDestFileDir: String, private val mDestFileName: String) : OkBaseParser<File>() {

    @Throws(IOException::class)
    override fun parse(response: Response): File {
        return saveFile(response)
    }

    @Throws(IOException::class)
    fun saveFile(response: Response): File {
        var `is`: InputStream? = null
        val buf = ByteArray(2048)
        var len: Int
        var fos: FileOutputStream? = null
        try {
            `is` = response.body().byteStream()
            val dir = File(mDestFileDir)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val file = File(dir, mDestFileName)
            fos = FileOutputStream(file)
            len = `is`!!.read(buf)
            while (len != -1) {
                fos.write(buf, 0, len)
                len = `is`!!.read(buf)
            }
            fos.flush()
            return file
        } finally {
            try {
                if (`is` != null) `is`.close()
            } catch (e: IOException) {
            }

            try {
                if (fos != null) fos.close()
            } catch (e: IOException) {
            }

        }
    }
}
