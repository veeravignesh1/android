import com.toggl.architecture.DispatcherProvider
import com.toggl.environment.services.time.TimeService
import com.toggl.repository.interfaces.TimeEntryRepository
import com.toggl.timer.common.testReduce
import com.toggl.timer.common.testReduceState
import com.toggl.timer.startedit.domain.DateTimePickMode
import com.toggl.timer.startedit.domain.StartEditAction
import com.toggl.timer.startedit.domain.StartEditReducer
import com.toggl.timer.startedit.domain.TemporalInconsistency
import com.toggl.timer.startedit.domain.createInitialState
import io.kotlintest.matchers.collections.shouldBeEmpty
import io.kotlintest.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@kotlinx.coroutines.ExperimentalCoroutinesApi
@DisplayName("The PickerTapped action")
internal class PickerTappedActionTests {
    val testDispatcher = TestCoroutineDispatcher()
    val timeService = mockk<TimeService>()
    val dispatcherProvider = DispatcherProvider(testDispatcher, testDispatcher, Dispatchers.Main)
    val repository = mockk<TimeEntryRepository>()
    val initialState = createInitialState()
    val reducer = StartEditReducer(repository, timeService, dispatcherProvider)

    @ParameterizedTest
    @EnumSource(DateTimePickMode::class)
    fun `sets the DateTimePickMode in state and returns no effect`(dateTimePickMode: DateTimePickMode) = runBlockingTest {
        reducer.testReduce(
            initialState = initialState,
            action = StartEditAction.PickerTapped(dateTimePickMode)
        ) { state, effects ->
            state.dateTimePickMode shouldBe dateTimePickMode
            effects.shouldBeEmpty()
        }
    }

    @ParameterizedTest
    @EnumSource(DateTimePickMode::class)
    fun `clears a set TemporalInconsistency`(dateTimePickMode: DateTimePickMode) = runBlockingTest {
        reducer.testReduceState(
            initialState = initialState.copy(temporalInconsistency = TemporalInconsistency.DurationTooLong),
            action = StartEditAction.PickerTapped(dateTimePickMode)
        ) {
            it.temporalInconsistency shouldBe TemporalInconsistency.None
        }
    }
}