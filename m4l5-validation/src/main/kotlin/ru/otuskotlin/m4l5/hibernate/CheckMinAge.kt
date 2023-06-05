package ru.otuskotlin.m4l5.hibernate

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlinx.datetime.*
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [CheckMinAgeValidator::class])
@MustBeDocumented
annotation class CheckMinAge(
    val message: String = "Age less then '{minAge}'",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = [],
    val minAge: Int
)

class CheckMinAgeValidator : ConstraintValidator<CheckMinAge, LocalDate> {
    private var minAge: Int = 0
    override fun initialize(constraintAnnotation: CheckMinAge) {
        minAge = constraintAnnotation.minAge
    }

    override fun isValid(value: LocalDate?, constraintContext: ConstraintValidatorContext): Boolean {
        if (value == null) return true

        val currentDate = Clock.System
            .now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
        val realAge = (currentDate - value).years

        return realAge >= minAge
    }
}