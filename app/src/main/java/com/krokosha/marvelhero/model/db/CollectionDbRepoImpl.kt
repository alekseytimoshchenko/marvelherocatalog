package com.krokosha.marvelhero.model.db

import kotlinx.coroutines.flow.Flow

class CollectionDbRepoImpl(
    private val characterDao: CharacterDao,
    private val noteDao: NoteDao
): ICollectionDbRepo {
    override suspend fun getCharacters(): Flow<List<DbCharacter>> = characterDao.getCharacters()
    override suspend fun getCharacterBy(id: Int): Flow<DbCharacter> = characterDao.getCharacterBy(id = id)
    override suspend fun add(character: DbCharacter) = characterDao.add(character = character)
    override suspend fun update(character: DbCharacter) = characterDao.update(character = character)
    override suspend fun delete(character: DbCharacter) = characterDao.delete(character = character)

    override suspend fun add(note: DbNote) = noteDao.add(note = note)
    override suspend fun update(note: DbNote) = noteDao.update(note = note)
    override suspend fun delete(note: DbNote) = noteDao.delete(note = note)
    override suspend fun getAllNotes(): Flow<List<DbNote>> = noteDao.getAllNotes()
    override suspend fun getNotesBy(characterId: Int): Flow<List<DbNote>> = noteDao.getNotesBy(characterId = characterId)
    override suspend fun deleteAllNotesBy(character: DbCharacter) = noteDao.deleteAllNotesBy(characterId = character.id)
}