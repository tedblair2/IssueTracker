package com.github.tedblair2.issuetracker

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val result = 2+2
        val expected = 4
        assertThat(result).isEqualTo(expected)
    }
}