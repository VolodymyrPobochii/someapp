package com.pobochii.someapp.data

import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.util.*

object RetrofitProvider {
    fun create(): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.stackexchange.com/2.3/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().apply {
            registerTypeAdapter(Date::class.java, DateDeserializer())
        }.create()))
        .client(OkHttpClient.Builder().build())
        .build()
}

class DateDeserializer : JsonDeserializer<Date> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Date? {
        return json.asLong?.let {
            Date(it)
        }
    }
}