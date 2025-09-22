package com.example.catapult.di


import android.app.Application
import androidx.room.Room
import com.example.catapult.data.local.database.AppDatabase
import com.example.catapult.data.local.database.UserPreferences
import com.example.catapult.data.local.database.dao.BreedDao
import com.example.catapult.data.local.database.dao.PhotoDao
import com.example.catapult.data.local.database.dao.QuizResultDao
import com.example.catapult.data.remote.api.CatsApi
import com.example.catapult.data.remote.api.LeaderboardApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

import kotlinx.serialization.json.Json
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL_CATS = "https://api.thecatapi.com/"
    private const val BASE_URL_LEADERBOARD = "https://rma.finlab.rs/"


    private const val CAT_API_KEY = "live_I3qsYsJW7OLyVeiggsJgvaeg1IxbTIP9ZU2HEZsauxRAGX7KrIIVge0PPfUQZPR2"


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideCatsApi(okHttpClient: OkHttpClient): CatsApi {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        val clientWithApiKey = okHttpClient.newBuilder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("x-api-key", CAT_API_KEY)
                    .build()
                chain.proceed(request)
            }
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL_CATS)
            .client(clientWithApiKey)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(CatsApi::class.java)
    }

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    @Singleton
    fun provideLeaderboardApi(okHttpClient: OkHttpClient): LeaderboardApi {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(BASE_URL_LEADERBOARD)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(LeaderboardApi::class.java)
    }


    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            "catapult_db"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBreedDao(appDatabase: AppDatabase): BreedDao {
        return appDatabase.breedDao()
    }

    @Provides
    @Singleton
    fun providePhotoDao(appDatabase: AppDatabase): PhotoDao {
        return appDatabase.photoDao()
    }

    @Provides
    @Singleton
    fun provideQuizResultDao(appDatabase: AppDatabase): QuizResultDao {
        return appDatabase.quizResultDao()
    }

    @Provides
    @Singleton
    fun provideUserPreferences(app: Application): UserPreferences {
        return UserPreferences(app)
    }

}
