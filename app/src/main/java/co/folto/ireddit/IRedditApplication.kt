package co.folto.ireddit

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class IRedditApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
