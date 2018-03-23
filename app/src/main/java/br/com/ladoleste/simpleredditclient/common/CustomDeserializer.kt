package br.com.ladoleste.simpleredditclient.common

import br.com.ladoleste.simpleredditclient.dto.Thing
import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

/**
 *Created by Anderson on 17/02/2018.
 */
class CustomDeserializer : JsonDeserializer<Thing> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Thing {
        val jsonString = json.toString().replace("replies\":\"\"", "replies\":null")
        return Gson().fromJson(jsonString, Thing::class.java)
    }
}