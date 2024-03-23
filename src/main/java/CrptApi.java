import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CrptApi {
    private static final String SERVICE_URL = "https://ismp.crpt.ru/api/v3/lk/documents/create";
    private final ObjectMapper mapper = new ObjectMapper();
    private final TimeUnit timeUnit;
    private final int requestLimit;

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.timeUnit = timeUnit;
        this.requestLimit = requestLimit;
    }

    public void createDocument(Document document, String signature) throws IOException {
        long limit = timeUnit.toMillis(requestLimit);

        CloseableHttpClient client = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout((int) limit)
                        .setSocketTimeout(30000)
                        .build())
                .build();

        String json = mapper.writeValueAsString(document);

        HttpPost request = new HttpPost(SERVICE_URL);
        request.setHeader("Content-type", "application/json");
        request.setHeader("Signature", signature);
        request.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
        CloseableHttpResponse response = client.execute(request);

        response.close();
        client.close();
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Document {
    @JsonProperty("description")
    private Description description;
    @JsonProperty("doc_id")
    private String docId;
    @JsonProperty("doc_status")
    private String docStatus;
    @JsonProperty("doc_type")
    private DocType docType;
    @JsonProperty("importRequest")
    private boolean importRequest;
    @JsonProperty("owner_inn")
    private String ownerInn;
    @JsonProperty("participant_inn")
    private String participantInn;
    @JsonProperty("producer_inn")
    private String producerInn;
    @JsonProperty("production_date")
    private Date productionDate;
    @JsonProperty("production_type")
    private String productionType;
    @JsonProperty("products")
    private List<Product> products;
    @JsonProperty("reg_date")
    private Date regDate;
    @JsonProperty("reg_number")
    private String regNumber;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Description {
    @JsonProperty("participant_inn")
    private String participantInn;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Product {
    @JsonProperty("certificate_document")
    private String certificateDocument;
    @JsonProperty("certificate_document_date")
    private Date certificateDocumentDate;
    @JsonProperty("certificate_document_number")
    private String certificateDocumentNumber;
    @JsonProperty("owner_inn")
    private String ownerInn;
    @JsonProperty("producer_inn")
    private String producerInn;
    @JsonProperty("production_date")
    private Date productionDate;
    @JsonProperty("tnved_code")
    private String tnvedCode;
    @JsonProperty("uit_code")
    private String uitCode;
    @JsonProperty("uitu_code")
    private String uituCode;
}

enum DocType {
    LP_INTRODUCE_GOODS
}