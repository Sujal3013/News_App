package com.example.newsapp.Database

import androidx.room.TypeConverter
import com.example.newsapp.modals.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source):String{
        return source.name
    }

    @TypeConverter
    fun toSource(name:String): Source
    {
        return Source(name,name)
    }
}