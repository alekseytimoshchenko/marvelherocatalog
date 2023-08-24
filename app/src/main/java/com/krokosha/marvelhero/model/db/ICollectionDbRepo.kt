package com.krokosha.marvelhero.model.db

import kotlinx.coroutines.flow.Flow

interface ICollectionDbRepo {
    suspend fun getCharacters(): Flow<List<DbCharacter>>
    suspend fun getCharacterBy(id: Int): Flow<DbCharacter>
    suspend fun add(character: DbCharacter)
    suspend fun update(character: DbCharacter)
    suspend fun delete(character: DbCharacter)
}