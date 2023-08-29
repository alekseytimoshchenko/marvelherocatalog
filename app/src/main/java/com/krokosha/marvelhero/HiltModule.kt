package com.krokosha.marvelhero

import android.content.Context
import androidx.room.Room
import com.krokosha.marvelhero.connectivity.ConnectivityMonitor
import com.krokosha.marvelhero.model.api.ApiService
import com.krokosha.marvelhero.model.db.CharacterDao
import com.krokosha.marvelhero.model.db.CollectionDb
import com.krokosha.marvelhero.model.db.CollectionDbRepoImpl
import com.krokosha.marvelhero.model.db.Constants
import com.krokosha.marvelhero.model.db.ICollectionDbRepo
import com.krokosha.marvelhero.model.db.NoteDao
import com.krokosha.marvelhero.repos.MarvelApiRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)

    @Provides
    fun provideCollectionDb(@ApplicationContext ctx: Context): CollectionDb =
        Room.databaseBuilder(ctx, CollectionDb::class.java, Constants.DB).build()

    @Provides
    fun provideCharacterDao(collectionDb: CollectionDb): CharacterDao = collectionDb.characterDao()

    @Provides
    fun provideNotesDao(collectionDb: CollectionDb): NoteDao = collectionDb.noteDao()

    @Provides
    fun provideDbRepoImpl(characterDao: CharacterDao, noteDao: NoteDao): ICollectionDbRepo =
        CollectionDbRepoImpl(characterDao = characterDao, noteDao = noteDao)

    @Provides
    fun provideConnectivityManager(@ApplicationContext ctx: Context): ConnectivityMonitor = ConnectivityMonitor.getInstance(ctx)
}