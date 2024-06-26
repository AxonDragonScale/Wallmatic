package com.axondragonscale.wallmatic.di

import android.content.Context
import com.axondragonscale.wallmatic.database.WallmaticDatabase
import com.axondragonscale.wallmatic.database.dao.AlbumDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Ronak Harkhani on 24/06/24
 */
@InstallIn(SingletonComponent::class)
@Module
object WallmaticModule {

    @Provides
    @Singleton
    fun provideWallmaticDatabase(appContext: Context): WallmaticDatabase =
        WallmaticDatabase.create(appContext)

    @Provides
    @Singleton
    fun provideAlbumDao(db: WallmaticDatabase): AlbumDao =
        db.albumDao()

}
