package com.kasao.qintaiframework.until

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * 作者 :created  by suochunming
 * 日期：2018/8/18 0018:17
 */

class GsonUtil {

    companion object {
        var gson = Gson()
        /**
         * 将对象转成json格式
         *
         * @param object
         * @return String
         */
        fun GsonString(obj: Any): String? {
            var gsonString: String? = null
            if (gson != null) {
                gsonString = gson!!.toJson(obj)
            }
            return gsonString
        }

        /**
         * 将json转成特定的cls的对象
         *
         * @param gsonString
         * @param cls
         * @return
         */
        fun <T> GsonToBean(gsonString: String, cls: Class<T>): T? {
            var t: T? = null
            if (gson != null) {
                //传入json对象和对象类型,将json转成对象
                t = gson!!.fromJson(gsonString, cls)
            }
            return t
        }

        /**
         * json字符串转成list
         *
         * @param gsonString
         * @param cls
         * @return
         */
        fun <T> GsonToList(gsonString: String, cls: Class<T>): List<T>? {
            var list: List<T>? = null
            if (gson != null) {
                //根据泛型返回解析指定的类型,TypeToken<List<T>>{}.getType()获取返回类型
                list = gson!!.fromJson<List<T>>(gsonString, object : TypeToken<List<T>>() {

                }.type)
            }
            return list
        }

        /**
         * json字符串转成list中有map的
         *
         * @param gsonString
         * @return
         */
        fun <T> GsonToListMaps(gsonString: String): List<Map<String, T>>? {
            var list: List<Map<String, T>>? = null
            if (gson != null) {
                list = gson!!.fromJson<List<Map<String, T>>>(gsonString,
                        object : TypeToken<List<Map<String, T>>>() {

                        }.type)
            }
            return list
        }

        /**
         * json字符串转成map的
         *
         * @param gsonString
         * @return
         */
        fun <T> GsonToMaps(gsonString: String): Map<String, T>? {
            var map: Map<String, T>? = null
            if (gson != null) {
                map = gson!!.fromJson<Map<String, T>>(gsonString, object : TypeToken<Map<String, T>>() {

                }.type)
            }
            return map
        }
    }

}