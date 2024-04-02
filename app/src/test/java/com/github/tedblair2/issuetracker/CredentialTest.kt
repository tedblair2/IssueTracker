package com.github.tedblair2.issuetracker

import com.github.tedblair2.issuetracker.helpers.EmailValidator
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CredentialTest {

    @ParameterizedTest
    @CsvSource(
        "' ',false",
        "'a',false",
        "'abc@',false",
        "'abc@email',false",
        "'abc@email.com',true"
    )
    fun emailValidation(email:String,expected:Boolean){
        val validator = EmailValidator()
        val result = validator.validateEmail(email)
        assertThat(result).isEqualTo(expected)
    }
}