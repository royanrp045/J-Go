package com.example.j_go.database

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun loadJsonFromRaw(context: Context, resourceId: Int): List<Place> {
    val inputStream = context.resources.openRawResource(resourceId)
    val jsonString = inputStream.bufferedReader().use { it.readText() }
    val listType = object : TypeToken<List<Place>>() {}.type
    return Gson().fromJson(jsonString, listType)
}