package com.enoch02.alttube

import android.content.Context
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AltTubeModule {
    @Provides
    fun providesApplicationContext(@ApplicationContext context: Context) = context

    @Provides
    fun provideFirebaseAuthInstance() = FirebaseAuth.getInstance()
}