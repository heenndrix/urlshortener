package dev.hendrix.urlshortener.controller;

import dev.hendrix.urlshortener.controller.dto.ShortenUrlRequest;
import dev.hendrix.urlshortener.controller.dto.ShortenUrlResponse;
import dev.hendrix.urlshortener.entities.UrlEntity;
import dev.hendrix.urlshortener.repository.UrlRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;


@RestController
public class UrlController {

    private final UrlRepository urlRepository;

    public UrlController(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    @PostMapping(value = "/shorten-url")
    public ResponseEntity<ShortenUrlResponse> shortenUrl(@RequestBody ShortenUrlRequest request,
                                                         HttpServletRequest servletRequest) {

        String id;
        do {
            id = RandomStringUtils.randomAlphanumeric(5,10);
        }while(urlRepository.existsById(id));


        urlRepository.save(new UrlEntity(id, request.url(), LocalDateTime.now().plusMinutes(1)));

        var redirectUrl = servletRequest.getRequestURL().toString().replace("shorten-url", id);





        return ResponseEntity.ok(new ShortenUrlResponse(redirectUrl));
    }
}
