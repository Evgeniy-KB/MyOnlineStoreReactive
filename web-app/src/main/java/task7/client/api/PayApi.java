package task7.client.api;

import task7.client.ApiClient;

import task7.client.model.PayResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", date = "2025-08-11T20:14:25.934505900+05:00[Asia/Yekaterinburg]", comments = "Generator version: 7.12.0")
public class PayApi {
    private ApiClient apiClient;

    public PayApi() {
        this(new ApiClient());
    }

    @Autowired
    public PayApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    
    /**
     * Оплата заказа
     * 
     * <p><b>200</b> - Заказ оформлен
     * <p><b>400</b> - Недостаточно средств для покупки
     * <p><b>500</b> - Внутренняя ошибка сервера
     * @param price цена покупки
     * @return PayResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec payOrderRequestCreation(Long price) throws WebClientResponseException {
        Object postBody = null;
        // verify the required parameter 'price' is set
        if (price == null) {
            throw new WebClientResponseException("Missing the required parameter 'price' when calling payOrder", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<String, Object>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<String, String>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "price", price));
        
        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] {  };

        ParameterizedTypeReference<PayResponse> localVarReturnType = new ParameterizedTypeReference<PayResponse>() {};
        return apiClient.invokeAPI("/api/pay", HttpMethod.PATCH, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Оплата заказа
     * 
     * <p><b>200</b> - Заказ оформлен
     * <p><b>400</b> - Недостаточно средств для покупки
     * <p><b>500</b> - Внутренняя ошибка сервера
     * @param price цена покупки
     * @return PayResponse
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<PayResponse> payOrder(Long price) throws WebClientResponseException {
        ParameterizedTypeReference<PayResponse> localVarReturnType = new ParameterizedTypeReference<PayResponse>() {};
        return payOrderRequestCreation(price).bodyToMono(localVarReturnType);
    }

    /**
     * Оплата заказа
     * 
     * <p><b>200</b> - Заказ оформлен
     * <p><b>400</b> - Недостаточно средств для покупки
     * <p><b>500</b> - Внутренняя ошибка сервера
     * @param price цена покупки
     * @return ResponseEntity&lt;PayResponse&gt;
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public Mono<ResponseEntity<PayResponse>> payOrderWithHttpInfo(Long price) throws WebClientResponseException {
        ParameterizedTypeReference<PayResponse> localVarReturnType = new ParameterizedTypeReference<PayResponse>() {};
        return payOrderRequestCreation(price).toEntity(localVarReturnType);
    }

    /**
     * Оплата заказа
     * 
     * <p><b>200</b> - Заказ оформлен
     * <p><b>400</b> - Недостаточно средств для покупки
     * <p><b>500</b> - Внутренняя ошибка сервера
     * @param price цена покупки
     * @return ResponseSpec
     * @throws WebClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec payOrderWithResponseSpec(Long price) throws WebClientResponseException {
        return payOrderRequestCreation(price);
    }
}
