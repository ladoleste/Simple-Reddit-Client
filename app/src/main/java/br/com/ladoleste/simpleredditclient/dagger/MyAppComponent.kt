package br.com.ladoleste.simpleredditclient.dagger

import br.com.ladoleste.simpleredditclient.viewmodel.CommentsViewModel
import br.com.ladoleste.simpleredditclient.viewmodel.MainViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Anderson on 21/03/2018
 */

@Singleton
@Component(modules = [(MyAppModule::class), (MyAppContextModule::class)])
interface MyAppComponent {
    fun inject(target: MainViewModel)
    fun inject(target: CommentsViewModel)
}