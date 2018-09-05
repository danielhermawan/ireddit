package co.folto.ireddit

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import co.folto.core.ControllerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : ControllerActivity() {

    private lateinit var navCon: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        navCon = findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navCon)
    }

    override fun getNavController() = navCon
}
