package com.toggl.timer.common

import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
open class FreeCoroutineSpec
//     (
//     var dispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher(),
//     body: AbstractFreeSpec.() -> Unit = {}
// ) : FreeSpec(body) {
//     var dispatcherProvider: DispatcherProvider = DispatcherProvider(dispatcher, dispatcher, Dispatchers.Main)
//
//     override fun beforeTest(testCase: TestCase) {
//         Dispatchers.setMain(dispatcher)
//         super.beforeTest(testCase)
//     }
//
//     override fun afterTest(testCase: TestCase, result: TestResult) {
//         super.afterTest(testCase, result)
//         Dispatchers.resetMain()
//         dispatcher.cleanupTestCoroutines()
//     }
// }