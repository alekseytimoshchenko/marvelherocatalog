package com.krokosha.marvelhero.model.db

import kotlinx.coroutines.flow.Flow

class CollectionDbRepoImpl(private val dao: CharacterDao): ICollectionDbRepo {
    override suspend fun getCharacters(): Flow<List<DbCharacter>> = dao.getCharacters()
    override suspend fun getCharacterBy(id: Int): Flow<DbCharacter> = dao.getCharacterBy(id = id)
    override suspend fun add(character: DbCharacter) = dao.add(character = character)
    override suspend fun update(character: DbCharacter) = dao.update(character = character)
    override suspend fun delete(character: DbCharacter) = dao.delete(character = character)
}