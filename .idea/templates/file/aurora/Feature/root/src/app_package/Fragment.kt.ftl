package ${escapeKotlinIdentifiers(packageName)}.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import ${packageName}.domain.${actionName}
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class ${fragmentName} : Fragment(R.layout.${layoutName}) {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val store: ${storeViewModelName} by viewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        TODO("Inject your component: " +
            "        (requireActivity().applicationContext as ExampleComponentProvider)\n" +
            "            .provideExampleComponent().inject(this)")
        super.onAttach(context)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

	store.dispatch(${actionName}.ExampleAction)

        store.state
            .onEach { state ->

            }
            .launchIn(lifecycleScope)
    }
}
