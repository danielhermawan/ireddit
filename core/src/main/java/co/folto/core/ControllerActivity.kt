package co.folto.core

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController

abstract class ControllerActivity : AppCompatActivity() {

    abstract fun getNavController(): NavController

    override fun onSupportNavigateUp(): Boolean =
            getNavController().navigateUp()

    override fun onBackPressed() {
        if (!getNavController().popBackStack()) {
            super.onBackPressed()
        }
    }
}