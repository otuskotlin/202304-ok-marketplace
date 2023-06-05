package ru.otus.otuskotlin.marketplace.lib.koform

import io.konform.validation.Validation
import io.konform.validation.ValidationBuilder
import io.konform.validation.ValidationResult
import io.konform.validation.jsonschema.pattern
import kotlinx.datetime.*
import org.junit.Test
import kotlin.time.Duration.Companion.days

class KonformTest {
    @Test
    fun konform() {
        val objValidator = Validation<SomeObject> {
            SomeObject::userId {
                pattern("^[0-9a-zA-Z_-]{1,64}\$")
            }
            SomeObject::dob {
                minAge(15)
            }
        }

        val resultUserId: ValidationResult<SomeObject> = objValidator.validate(SomeObject())
        println(resultUserId.errors)

        val resultAge = objValidator.validate(
            SomeObject(
                userId = "987987987",
                dob = Clock.System.now().minus((-5 * 365).days).toLocalDateTime(TimeZone.currentSystemDefault()).date
            )
        )
        println(resultAge.errors)

        val resultOk = objValidator.validate(
            SomeObject(
                userId = "987987987",
                dob = Clock.System.now().minus((-20 * 365).days).toLocalDateTime(TimeZone.currentSystemDefault()).date
            )
        )
        println(resultOk.errors)
    }
}

private fun ValidationBuilder<LocalDate?>.minAge(min: Int) = addConstraint(
    errorMessage = "Age cannot be less then $min or null",
) {
    if (it == null) {
        false
    } else {
        val currentDate = Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val realAge = (it - currentDate).years

        realAge >= min
    }
}

data class SomeObject(
    val userId: String = "",
    val dob: LocalDate? = null,
)
