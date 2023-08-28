package com.krokosha.marvelhero.model.db

import kotlinx.coroutines.flow.Flow
import java.util.concurrent.locks.LockSupport

interface ICollectionDbRepo {
    suspend fun getCharacters(): Flow<List<DbCharacter>>
    suspend fun getCharacterBy(id: Int): Flow<DbCharacter>
    suspend fun add(character: DbCharacter)
    suspend fun update(character: DbCharacter)
    suspend fun delete(character: DbCharacter)
    suspend fun deleteAllNotesBy(character: DbCharacter)

    suspend fun getAllNotes(): Flow<List<DbNote>>
    suspend fun getNotesBy(characterId: Int): Flow<List<DbNote>>
    suspend fun add(note: DbNote)
    suspend fun update(note: DbNote)
    suspend fun delete(note: DbNote)
}