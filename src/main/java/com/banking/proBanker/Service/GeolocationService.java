package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.GeolocationResponse;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.CompletableFuture;

public interface GeolocationService {

    @Async
    public CompletableFuture<GeolocationResponse> getGeolocation(String ip);

}
