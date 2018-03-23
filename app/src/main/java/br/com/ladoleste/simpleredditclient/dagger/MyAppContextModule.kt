package br.com.ladoleste.simpleredditclient.dagger

import br.com.ladoleste.simpleredditclient.common.CustomApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


/**
 * Created by Anderson on 23/03/2018
 */
@Module
class MyAppContextModule(private var appContext: CustomApplication) {

    @Provides
    @Singleton
    fun provideApplication() = appContext
}