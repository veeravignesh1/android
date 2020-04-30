package com.toggl.common

import android.content.Context
import android.os.Build
import android.os.Vibrator
import androidx.core.content.getSystemService
import androidx.test.core.app.ApplicationProvider
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatCode
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * At least locally, these tests take some time to run since they download the dependencies for each sdk level
 * We might want to disable skip these tests and run them only locally
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class VibratorExtensionsKtTest {

    @Test
    @Config(minSdk = Build.VERSION_CODES.LOLLIPOP, maxSdk = Build.VERSION_CODES.O_MR1)
    fun performClickEffect() {
        val vibrator = ApplicationProvider.getApplicationContext<Context>().getSystemService<Vibrator>()

        assertThat(vibrator).isNotNull
        assertThatCode { vibrator!!.performClickEffect() }.doesNotThrowAnyException()
    }

    @Test
    @Config(minSdk = Build.VERSION_CODES.LOLLIPOP, maxSdk = Build.VERSION_CODES.O_MR1)
    fun performTickEffect() {
        val vibrator = ApplicationProvider.getApplicationContext<Context>().getSystemService<Vibrator>()

        assertThat(vibrator).isNotNull
        assertThatCode { vibrator!!.performTickEffect() }.doesNotThrowAnyException()
    }
}