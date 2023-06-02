package com.redhaputra.movieapp.core.di.module

import android.content.Context
import android.content.SharedPreferences
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module that used to provides shared preferences.
 *
 * @see Module
 */
@Module
@InstallIn(SingletonComponent::class)
object PrefsModule {

    private const val PREF_NAME = "RedhaPutraCode"
    private const val MASTER_KEY_ALIAS = "RedhaPutraCrypto"
    private const val KEY_SIZE = 256

    /**
     * Create a provider method binding for [SharedPreferences].
     *
     * @param context Application Context
     * @return Single instance of SharedPreferences.
     * @see Provides
     */
    @Provides
    @Singleton
    fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
        val spec = KeyGenParameterSpec.Builder(
            MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .build()
        val masterKeyBuilder = MasterKey.Builder(context, MASTER_KEY_ALIAS)
            .setKeyGenParameterSpec(spec)
        return EncryptedSharedPreferences.create(
            context,
            PREF_NAME,
            masterKeyBuilder.build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}