package ru.zhem.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import ru.zhem.entity.Client;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientClientRestClient implements ClientRestClient {

    private static final ParameterizedTypeReference<List<Client>> CLIENT_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    private final RestClient restClient;

    @Override
    public List<Client> findAllClients() {
        return null;
    }

    @Override
    public Optional<Client> findClient(BigDecimal phone) {
        try {
            return Optional.ofNullable(
                    this.restClient.get()
                            .uri("/service-api/v1/clients/{phone}",phone)
                            .retrieve()
                            .body(Client.class)
            );
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public Client createClient(BigDecimal phone, String name, String surname, String password) {
        return null;
    }

    @Override
    public void updateClient(BigDecimal phone, BigDecimal newPhone, String name, String surname, String password) {

    }

    @Override
    public void deleteClient(BigDecimal phone) {

    }
}
