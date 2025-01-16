package com.example.graphicsmaker

import vocsy.ads.AdsApplication

class App : AdsApplication() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        var appInstance: App? = null

        @JvmStatic
        @get:Synchronized
        val app: App?
            get() {
                var appInstance: App?
                synchronized(App::class.java) {
                    appInstance = Companion.appInstance
                }
                return appInstance
            }
    }
}
