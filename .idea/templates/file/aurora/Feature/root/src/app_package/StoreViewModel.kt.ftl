package ${escapeKotlinIdentifiers(packageName)}.ui

import androidx.lifecycle.ViewModel
import com.toggl.architecture.core.Store
import ${packageName}.domain.${actionName}
import ${packageName}.domain.${stateName}
import javax.inject.Inject

class ${storeViewModelName} @Inject constructor(
    store: Store<${stateName}, ${actionName}>
) : ViewModel(), Store<${stateName}, ${actionName}> by store