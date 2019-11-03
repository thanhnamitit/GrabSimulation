package com.example.epoxytut.di.module

import dagger.Module
import dagger.Provides
import hvktqx.team.grabsimulation.data.repository.GoogleRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class NetworkModule {

  @Provides
  fun provideOkhttpClient(): OkHttpClient {
    val logging = HttpLoggingInterceptor()
    logging.level = Level.BODY
    return OkHttpClient.Builder()
      .addInterceptor(logging)
      .build()
  }

  @Provides
  fun provideRetrofit(okhttp: OkHttpClient): Retrofit {
    return Retrofit.Builder()
      .baseUrl("https://maps.googleapis.com/")
      .addConverterFactory(GsonConverterFactory.create())
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .client(okhttp)
      .build()
  }

  @Provides
  fun provideGoogleRepository(retrofit: Retrofit): GoogleRepository {
    return retrofit.create(GoogleRepository::class.java)
  }
}