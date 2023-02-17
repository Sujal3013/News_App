package com.example.newsapp.Database

import android.content.Context
import androidx.room.*
import com.example.newsapp.modals.Article

@Database
    (entities = [Article::class],
     version=1)
@TypeConverters(Converters::class)
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