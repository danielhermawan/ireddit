package co.folto.core

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.core.content.withStyledAttributes
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import java.util.*

private val controllerClasses = HashMap<String, Class<out ViewController>>()

@Navigator.Name("ControllerNavigator")
class ControllerNavigator(val context: Context, val container: ViewGroup)
    : Navigator<ControllerNavigator.Destination>() {

    private val stack: Deque<Pair<Int, ViewController>> = LinkedList()

    override fun createDestination() = Destination(context, this)

    override fun popBackStack(): Boolean {
        return if (stack.size > 1) {
            stack.pop()
            replaceView(stack.peek().second)
            dispatchOnNavigatorNavigated(stack.peek().first, BACK_STACK_DESTINATION_POPPED)
            true
        } else {
            false
        }
    }

    // todo: handle navOptions such as transisiton
    // todo: add comment
    override fun navigate(destination: Destination, args: Bundle?, navOptions: NavOptions?) {
        val viewController = destination.createController(args)
        stack.push(Pair(destination.id, viewController))
        replaceView(viewController)
        dispatchOnNavigatorNavigated(destination.id, BACK_STACK_DESTINATION_ADDED)
    }

    private fun replaceView(viewController: ViewController) {
        container.removeAllViews();
        container.addView(viewController)
    }

    class Destination(val context: Context, navigator: Navigator<out Destination>) : NavDestination(navigator) {

        private var viewControllerClass: Class<out ViewController>? = null

        override fun onInflate(context: Context, attrs: AttributeSet) {
            super.onInflate(context, attrs)

            context.withStyledAttributes(attrs, R.styleable.ControllerNavigator, 0, 0) {
                getString(R.styleable.ControllerNavigator_name).also {
                    if (it != null)
                        setControllerClass(getControllerClassByName(context, it))
                    else
                        throw IllegalStateException("Name attribute is null")
                }
            }
        }

        fun setControllerClass(clazz: Class<out ViewController>): Destination {
            viewControllerClass = clazz
            return this
        }

        fun createController(args: Bundle?): ViewController {
            val clazz = viewControllerClass
                    ?: throw IllegalStateException("ViewController class not set")

            val c: ViewController
            try {
                c = clazz.getDeclaredConstructor(Context::class.java, Bundle::class.java)
                        .newInstance(context, args);
            } catch (e: Exception) {
                throw RuntimeException(e)
            }

            return c
        }

        @Suppress("UNCHECKED_CAST")
        private fun getControllerClassByName(context: Context, name: String): Class<out ViewController> {
            var className = name
            if (className[0] == '.') {
                className = context.packageName + className
            }
            var clazz = controllerClasses.get(className)
            if (clazz == null) {
                try {
                    clazz = Class.forName(className, true,
                            context.classLoader) as Class<out ViewController>
                    controllerClasses.put(className, clazz)
                } catch (e: ClassNotFoundException) {
                    throw RuntimeException(e)
                }

            }
            return clazz
        }
    }
}