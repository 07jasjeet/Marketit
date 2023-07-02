package com.jasjeet.marketit.application

import android.app.Application
import com.jasjeet.marketit.dependencies.appModule
import org.koin.core.context.GlobalContext.startKoin

class App: Application() {
    override fun onCreate() {
        super.onCreate()
    
        startKoin {
            modules(appModule)
        }
    }
}