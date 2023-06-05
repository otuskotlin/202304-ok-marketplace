package ru.otuskotlin.m4l5

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import kotlinx.datetime.LocalDate
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.otuskotlin.m4l5.hibernate.CheckMinAge

data class Model(
    @field:Pattern(regexp = "^[0-9a-zA-Z_-]{1,64}$")
    val userId: String = "",
    @field:NotNull
    @field:CheckMinAge(minAge = 15)
    // "2015-12-30"
    val dob: LocalDate? = null,
)

@RestController
class SomeController {
    @PostMapping("/some")
    fun post(@Valid @RequestBody model: Model): String {
        return "${model.userId}: ${model.dob}"
    }
}

@SpringBootApplication
class Application

fun main() {
    runApplication<Application>()
}