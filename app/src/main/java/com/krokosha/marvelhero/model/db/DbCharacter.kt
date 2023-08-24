package com.krokosha.marvelhero.model.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.krokosha.marvelhero.comicsToString
import com.krokosha.marvelhero.model.CharacterResult
import com.krokosha.marvelhero.model.CharacterThumbnail

@Entity(tableName = Constants.CHARACTER_TABLE)
class DbCharacter(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val apiId: Int?,
    val name: String?,
    val thumbnail: String?,
    val comics: String?,
    val description: String?
) {
    companion object {
        fun from(character: CharacterResult) =
            DbCharacter(
                id = 0,
                apiId = character.id,
                name = character.name,
                thumbnail = "${character.thumbnail?.path}.${character.thumbnail?.extension}",
                comics = character.comics?.items?.mapNotNull { it.name }?.comicsToString() ?: "no comics",
                description = character.description
            )
    }
}