package com.example.graphicsmaker.db

//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [TemplateInfo::class, TextInfo::class, ComponentInfo::class], version = 1)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun templateDao(): TemplateDao
//    abstract fun textInfoDao(): TextInfoDao
//    abstract fun componentInfoDao(): ComponentInfoDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: AppDatabase? = null
//
//        fun getDatabase(context: Context): AppDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = INSTANCE ?: Room.databaseBuilder(
//                    context.applicationContext,
//                    AppDatabase::class.java,
//                    "logomaker_db"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}

