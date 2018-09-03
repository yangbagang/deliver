package com.ybg.app.base.utils

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*

/**
 * Created by yangbagang on 16/8/9.
 */
object GsonUtil {

    fun createGson(): Gson {
        return GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").create()
    }

    fun createGson(dateFormate: String): Gson {
        return GsonBuilder().serializeNulls().setDateFormat(dateFormate).create()
    }

    fun createWithoutNulls(): Gson {
        return GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create()
    }

    fun createWithoutNullsDisableHtmlEscaping(): Gson {
        return GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd").excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().disableHtmlEscaping().create()
    }

    fun toJsonProperties(o: Any, properties: Array<String>): String {
        val gsonBuilder = GsonBuilder().serializeNulls().setExclusionStrategies(PropertiesInclude(properties))
        gsonBuilder.setDateFormat("yyyy-MM-dd")
        return gsonBuilder.create().toJson(o)
    }

    fun toJsonPropertiesDes(o: Any, properties: Array<String>): String {
        val gsonBuilder = GsonBuilder().serializeNulls().setExclusionStrategies(PropertiesInclude(properties))
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss")
        return gsonBuilder.create().toJson(o)
    }

    internal class PropertiesInclude(pros: Array<String>) : ExclusionStrategy {
        var includeProSet: HashSet<String>? = null

        init {
            includeProSet = HashSet<String>(pros.size)
            for (s in pros) {
                includeProSet!!.add(s)
            }
        }

        override fun shouldSkipField(f: FieldAttributes): Boolean {
            val name = f.name
            return !includeProSet!!.contains(name)
        }

        override fun shouldSkipClass(clazz: Class<*>): Boolean {
            return false
        }
    }

    fun <T> getPerson(jsonString: String, cls: Class<T>): T? {
        var t: T? = null
        try {
            t = createGson().fromJson(jsonString, cls)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return t
    }

    fun <T> getPersons(jsonString: String, cls: Class<T>): List<T> {
        var list: List<T> = ArrayList()
        try {
            list = createGson().fromJson<List<T>>(jsonString, object : TypeToken<List<T>>() {

            }.type)

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }

    fun getList(jsonString: String): List<String> {
        var list: List<String> = ArrayList()
        try {
            list = createGson().fromJson<List<String>>(jsonString, object : TypeToken<List<String>>() {

            }.type)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list

    }

    fun listKeyMap(jsonString: String): List<Map<String, Any>> {
        var list: List<Map<String, Any>> = ArrayList()
        try {
            list = createGson().fromJson<List<Map<String, Any>>>(jsonString, object : TypeToken<List<Map<String, Any>>>() {

            }.type)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list
    }

}
