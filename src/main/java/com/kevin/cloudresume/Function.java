package com.kevin.cloudresume;

import com.azure.data.tables.TableClient;
import com.azure.data.tables.TableServiceClient;
import com.azure.data.tables.TableServiceClientBuilder;
import com.azure.data.tables.models.TableEntity;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

    private final static String CONN_STRING = "DefaultEndpointsProtocol=https;AccountName=cloud-resume-table;AccountKey=9bji0AEW4h53JUSGoLl0HJqFUvUth9I0toblXHvyGfJIEAURP45cdVMuCSIOutbEW5baeiLYaDpCACDbZGbixg==;TableEndpoint=https://cloud-resume-table.table.cosmos.azure.com:443/;";
    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        final List<String> tables = new ArrayList<>();

        try {
            final TableServiceClient tableServiceClient = new TableServiceClientBuilder()
            .connectionString(CONN_STRING)
            .buildClient();

            // final TableClient tableClient = tableServiceClient.createTableIfNotExists("Employees");
        
            // tableServiceClient.listTables().forEach(table -> {
            //     tables.add(table.getName().toString());
            // });

            final var table = tableServiceClient.getTableClient("cloud-resume");

            TableEntity tableEntity;
            try {
                tableEntity = table.getEntity("counter", "0001");
                final Object counter = tableEntity.getProperty("counter");
                context.getLogger().info("Retrieved counterEntity from table. Current value: " + counter);
                
            } catch (final Exception e) {
                context.getLogger().info("Unable to get counter entity. Creating it instead..");
                final Map<String, Object> counter = new HashMap<>(Map.of("counter", 1));
                tableEntity = new TableEntity("counter", "0001").setProperties(counter);
            }

            var originalMap = tableEntity.getProperties();
            originalMap.put("counter", (int)originalMap.get("counter")+1);
            table.upsertEntity(new TableEntity("counter", "0001").setProperties(originalMap));

        } catch (Exception e) {
            e.printStackTrace();
        }


        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a name on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name + ". This is the updated version!").build();
        }
    }
}
