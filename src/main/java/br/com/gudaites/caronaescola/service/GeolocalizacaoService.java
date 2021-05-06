package br.com.gudaites.caronaescola.service;

import br.com.gudaites.caronaescola.models.Contato;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Class Service da Pesquisa por Geolocalização
 *
 * @author Jair Gudaites Junior
 */


@Service
public class GeolocalizacaoService {

    public List<Double> obterLateLongPor(Contato contato) throws InterruptedException, ApiException, IOException {
        GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyD4bLmS8GiE2kWzazp3UUxRXEc8EZKbMYs");
        GeocodingApiRequest request = GeocodingApi.newRequest(context).address(contato.getEndereco());

        GeocodingResult[] results = request.await();
        GeocodingResult resultado = results[0];

        Geometry geometry = resultado.geometry;
        LatLng location = geometry.location;

        return Arrays.asList(location.lat, location.lng);
    }
}
