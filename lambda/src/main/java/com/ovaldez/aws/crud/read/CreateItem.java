package com.ovaldez.aws.crud.read;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ovaldez.aws.crud.GatewayResponse;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemResponse;

public class CreateItem implements RequestHandler<Map<String,Object>, GatewayResponse> {

	private static Logger logger = LoggerFactory.getLogger(CreateItem.class);
	@Override
	public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.info("Inside software.amazon.awscdk.examples.lambda: getOneItem "+input.getClass()+ " data:"+input);
		String body = (String)input.get("body");
        logger.info("Body is:"+body);


        String output = createItem(body);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        return new GatewayResponse(output, headers, 200);
	}
	private String createItem(String body) {
		DynamoDbClient ddbClient = DynamoDbClient.create();
		String tableName= System.getenv("TABLE_NAME");
        String primaryKey = System.getenv("PRIMARY_KEY");
        Map<String, AttributeValue> item = new HashMap<>();
        String id = UUID.randomUUID().toString();
        item.put(primaryKey, AttributeValue.builder().s(id).build());
        
        JsonElement element = JsonParser.parseString(body);
        JsonObject jsonObject = element.getAsJsonObject();
        Set<String> keys = jsonObject.keySet();
        for(String key: keys) {
        	item.put(key, AttributeValue.builder().s(jsonObject.get(key).getAsString()).build());
        }
        
        PutItemRequest itemRequest = PutItemRequest.builder()
        							.tableName(tableName)
        							.item(item)
        							.build();
        PutItemResponse itemResponse = ddbClient.putItem(itemRequest);
        
		return itemResponse.toString();
	}

	
}
