package com.enoch02.alttube

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

@Module
@InstallIn(SingletonComponent::class)
class AltTubeModule {
    @Provides
    fun providesApplicationContext(@ApplicationContext context: Context) = context

    @Provides
    fun provideSupabaseClient(): SupabaseClient {
        val supabase: SupabaseClient = createSupabaseClient(
            supabaseUrl = BuildConfig.SUPABASE_URL,
            supabaseKey = BuildConfig.SUPABASE_KEY
        ) {
            install(Storage)
            install(Auth)
            install(Postgrest)
        }

        return supabase
    }
}