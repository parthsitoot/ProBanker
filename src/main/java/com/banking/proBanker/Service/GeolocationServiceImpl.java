package com.banking.proBanker.Service;

import com.banking.proBanker.DTO.GeolocationResponse;
import com.banking.proBanker.Exceptions.GeolocationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class GeolocationServiceImpl implements GeolocationService{

    @Value("${geo.api.url}")
    public String apiUrl;

    @Value("${geo.api.key}")
    public String apiKey;

    @Override
    public CompletableFuture<GeolocationResponse> getGeolocation(String ip) {
        val future = new CompletableFuture<GeolocationResponse>();

        try {
            InetAddress.getByName(ip);

            log.info("Getting Geolocation of ip Address : {}", ip);

            //calling geoLocationApi

            val url = String.format("%s/%s/?token=%s", apiUrl, ip, apiKey);
            val response = new RestTemplate().getForObject(url, GeolocationResponse.class);

            if (response == null) {
                log.error("Failed to get Geolocation of ip : {}", ip);
                future.completeExceptionally(new GeolocationException(
                        "Failed to get Geolocation for ip : " + ip));
            } else {
                future.complete(response);
            }


        } catch (UnknownHostException e) {
            log.error("Invalid ip address : {}", ip, e);
            future.completeExceptionally(e);
        } catch (RestClientException e) {
            log.error("Failed to get Geolocation of IP address : {}", ip, e);
            future.completeExceptionally(e);
        }

        return future;
    }
}
