package com.example.newsapp.Database

import android.content.Context
import android.provider.CalendarContract.Instances
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.newsapp.Article

@Database(entities = [Article::class],
version=1)
abstract class Article_Database:RoomDatabase() {

    abstract fun getArticleDao():ArticleDao

    companion object{
        @Volatile
        private var instance:Article_Database?=null
        private val LOCK=Any()

        operator fun invoke(context: Context)=instance ?: synchronized(LOCK){
            instance?:createDatabase(context).also{ instance=it}
        }

        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
            context.applicationContext,
            Article_Database::class.java,
            "article_db.db"
            ).build()

    }
}