package ${escapeKotlinIdentifiers(packageName)}.domain

import com.toggl.architecture.core.Effect
import com.toggl.architecture.core.MutableValue
import com.toggl.architecture.core.Reducer
import com.toggl.common.feature.extensions.mutateWithoutEffects
import javax.inject.Inject

class ${reducerName} @Inject constructor(
) : Reducer<${stateName}, ${actionName}> {

    override fun reduce(
        state: MutableValue<${stateName}>,
        action: ${actionName}
    ): List<Effect<${actionName}>> =
        when (action) {
            is ${actionName}.ExampleAction -> state.mutateWithoutEffects {
                copy(exampleText = "test")
            }
        }
}
