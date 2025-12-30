package com.rprojects.gudangku.credential.controller

import com.rprojects.gudangku.credential.dto.CredentialDto
import com.rprojects.gudangku.credential.service.CredentialService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/credential")
class CredentialController(
    private val credentialService: CredentialService,
) {

    @PostMapping("/register")
    fun registerNewCredential(@RequestBody @Valid dto: CredentialDto): ResponseEntity<Void> {
        val newCred = credentialService.save(dto)

        val uri = ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/{username}")
            .buildAndExpand(newCred.username)
            .toUri()
        return ResponseEntity.created(uri).build()
    }
}