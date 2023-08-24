package com.krokosha.marvelhero.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Query("SELECT * FROM ${Constants.CHARACTER_TABLE} ORDER BY id ASC")
    fun getCharacters(): Flow<List<DbCharacter>>

    @Query("SELECT * FROM ${Constants.CHARACTER_TABLE} WHERE id = :id")
    fun getCharacterBy(id: Int): Flow<DbCharacter>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun add(character: DbCharacter)

    @Update
    fun update(character: DbCharacter)

    @Delete
    fun delete(character: DbCharacter)
}