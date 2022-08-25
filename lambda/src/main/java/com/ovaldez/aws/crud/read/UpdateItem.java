package com.ovaldez.aws.crud.read;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ovaldez.aws.crud.GatewayResponse;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeAction;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.AttributeValueUpdate;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;

public class UpdateItem implements RequestHandler<Map<String,Object>, GatewayResponse> {
	private final Logger logger = LoggerFactory.getLogger(UpdateItem.class);
	@Override
	public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.info("Inside software.amazon.awscdk.examples.lambda: getOneItem "+input.getClass()+ " data:"+input);
		@SuppressWarnings("unchecked")
		Map<String, Object> pathParameters = (Map<String, Object>)input.get("pathParameters");
        String id=(String)pathParameters.get("id");

        String body = (String)input.get("body");
        logger.info("Body is:"+body);

        logger.info("updating data for input parameter:"+id);
        String output = updateData(id, body);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return new GatewayResponse(output, headers, 200);
	}
	private String updateData(String id, String body) {
		DynamoDbClient ddb = DynamoDbClient.create();
        String tableName= System.getenv("TABLE_NAME");
        String primaryKey = System.getenv("PRIMARY_KEY");
        Map<String, AttributeValue> tableKey = new HashMap<>();
        tableKey.put(primaryKey, AttributeValue.builder().s(id).build());

        Map<String, AttributeValueUpdate> item = new HashMap<>();
        JsonElement element = JsonParser.parseString(body);
        JsonObject jsonObject = element.getAsJsonObject();
        Set<String> keys = jsonObject.keySet();
        for (String key: keys) {
            item.put(key, AttributeValueUpdate.builder()
                            .value(AttributeValue.builder().s(jsonObject.get(key).getAsString()).build())
                            .action(AttributeAction.PUT)
                            .build()
                    );
        }
        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder()
                .key(tableKey)
                .tableName(tableName)
                .attributeUpdates(item)
                .returnValues(ReturnValue.ALL_NEW)
                .build();
        UpdateItemResponse response = ddb.updateItem(updateItemRequest);
        return response.toString();
	}

}
