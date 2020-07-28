
import com.google.common.truth.Truth.assertThat
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.testReduce
import com.toggl.timer.common.testReduceState
import com.toggl.timer.startedit.domain.DateTimePickMode
import com.toggl.timer.startedit.domain.StartEditAction
import com.toggl.timer.startedit.domain.TemporalInconsistency
import com.toggl.timer.startedit.domain.createInitialState
import com.toggl.timer.startedit.domain.createReducer

import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@kotlinx.coroutines.ExperimentalCoroutinesApi
@DisplayName("The PickerTapped action")
internal class PickerTappedActionTests {
    val repository = mockk<TimeEntryRepository>()
    val initialState = createInitialState()
    val reducer = createReducer()

    @ParameterizedTest
    @EnumSource(DateTimePickMode::class)
    fun `sets the DateTimePickMode in state and returns no effect`(dateTimePickMode: DateTimePickMode) = runBlockingTest {
        reducer.testReduce(
            initialState = initialState,
            action = StartEditAction.PickerTapped(dateTimePickMode)
        ) { state, effects ->
            assertThat(state.dateTimePickMode).isEqualTo(dateTimePickMode)
            assertThat(effects).isEmpty()
        }
    }

    @ParameterizedTest
    @EnumSource(DateTimePickMode::class)
    fun `clears a set TemporalInconsistency`(dateTimePickMode: DateTimePickMode) = runBlockingTest {
        reducer.testReduceState(
            initialState = initialState.copy(temporalInconsistency = TemporalInconsistency.DurationTooLong),
            action = StartEditAction.PickerTapped(dateTimePickMode)
        ) {
            assertThat(it.temporalInconsistency).isEqualTo(TemporalInconsistency.None)
        }
    }
}