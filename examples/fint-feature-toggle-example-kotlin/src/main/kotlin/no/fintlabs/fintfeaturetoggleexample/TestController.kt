package no.fintlabs.fintfeaturetoggleexample

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/app")
class TestController(
    @Value("\${spring.application.name}") private val applicationName: String
){
    @GetMapping
    fun getApplicationName() : String {
        return applicationName;
    }
}