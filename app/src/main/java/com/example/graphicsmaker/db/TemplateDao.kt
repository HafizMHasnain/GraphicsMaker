package com.example.graphicsmaker.db

//import androidx.room.*
//
//@Dao
//interface TemplateDao {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insert(template: TemplateInfo): Long
//
//    @Query("SELECT * FROM templates WHERE templateId = :id")
//    suspend fun getTemplateById(id: Int): TemplateInfo?
//
//    @Query("SELECT * FROM templates WHERE type = :type ORDER BY templateId DESC")
//    suspend fun getTemplatesByType(type: String): List<TemplateInfo>
//
//    @Delete
//    suspend fun delete(template: TemplateInfo)
//}
