package ru.otus.otuskotlin.marketplace.lib.koform

import jakarta.validation.*
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import kotlinx.datetime.*
import org.junit.Test
import ru.otuskotlin.m4l5.hibernate.CheckMinAge
import kotlin.reflect.KClass
import kotlin.time.Duration.Companion.days


class HibernateTest {
    @Test
    fun hibernate() {
        val factory: ValidatorFactory = Validation.buildDefaultValidatorFactory()
        val validator = factory.validator

        val errors1 = validator.validate(SomeObject2())
        println("=== result1\n" + errors1.joinToString("\n"))

        val errors2 = validator.validate(SomeObject2(
            userId = "123",
            dob = Clock.System.now().minus((-5 * 365).days).toLocalDateTime(TimeZone.currentSystemDefault()).date))
        println("=== result2\n" + errors2.joinToString("\n"))

        val errors3 = validator.validate(SomeObject2(
            userId = "123",
            dob = Clock.System.now().minus((-25 * 365).days).toLocalDateTime(TimeZone.currentSystemDefault()).date))
        println("=== result3\n" + errors3.joinToString("\n"))
    }
}

data class SomeObject2(
    @field:Pattern(regexp = "^[0-9a-zA-Z_-]{1,64}$")
    val userId: String = "",
    @field:NotNull
    @field:CheckMinAge(minAge = 15)
    val dob: LocalDate? = null,
)

