package com.example.storeapp.di

import android.content.Context
import androidx.room.Room
import com.example.storeapp.core.util.AppConstants
import com.example.storeapp.data.datasource.LocalStoreDataSource
import com.example.storeapp.data.datasource.RemoteStoreDataSource
import com.example.storeapp.data.local.StoreDatabase
import com.example.storeapp.data.local.StoreDatabase.Companion.DATABASE_NAME
import com.example.storeapp.data.local.dao.ProductCartDao
import com.example.storeapp.data.remote.api.ApiService
import com.example.storeapp.data.repository.StoreRepositoryImpl
import com.example.storeapp.domain.repository.StoreRepository
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Qualifier
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class IoDispatcher

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    // region coroutines
    @IoDispatcher
    @Provides
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    // endregion

    // region data
    @Singleton
    @Provides
    fun provideStoreRepository(
        remoteStoreDataSource: RemoteStoreDataSource,
        localStoreDataSource: LocalStoreDataSource
    ): StoreRepository {

        return StoreRepositoryImpl(
            remoteDataSource = remoteStoreDataSource,
            localDataSource = localStoreDataSource
        )
    }

    @Singleton
    @Provides
    fun provideRemoteStoreDataSource(apiService: ApiService): RemoteStoreDataSource {

        return RemoteStoreDataSource(apiService)
    }

    @Singleton
    @Provides
    fun provideLocalStoreDataSource(productCartDao: ProductCartDao): LocalStoreDataSource {

        return LocalStoreDataSource(productCartDao)
    }

    // endregion

    // region Network
    @Singleton
    @Provides
    fun provideStoreService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(AppConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {

        val logger: HttpLoggingInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder().addInterceptor(logger).build()
    }

    // region local

    @Singleton
    @Provides
    fun provideRoomInstance(@ApplicationContext context: Context): StoreDatabase {

        return Room.databaseBuilder(
            context = context,
            klass = StoreDatabase::class.java,
            name = DATABASE_NAME
        ).build()
    }

    @Singleton
    @Provides
    fun provideProductCartDao(db: StoreDatabase): ProductCartDao = db.productCartDao()

    // endregion
}