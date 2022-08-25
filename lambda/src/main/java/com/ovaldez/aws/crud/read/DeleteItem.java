package com.ovaldez.aws.crud.read;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ovaldez.aws.crud.GatewayResponse;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;

public class DeleteItem implements RequestHandler<Map<String,Object>, GatewayResponse> {

	private final Logger logger = LoggerFactory.getLogger(DeleteItem.class);
	
	@Override
	public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.info("Inside software.amazon.awscdk.examples.lambda: getOneItem "+input.getClass()+ " data:"+input);
		
		@SuppressWarnings("unchecked")
		Map<String, Object> pathParameters = (Map<String, Object>)input.get("pathParameters");

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        String id=(String)pathParameters.get("id");
        logger.info("delete data for input parameter:"+id);
        String output = deleteItem(id);
        return new GatewayResponse(output, headers, 200);
	}

	private String deleteItem(String id) {
		DynamoDbClient ddb = DynamoDbClient.create();
        String tableName= System.getenv("TABLE_NAME");
        String primaryKey = System.getenv("PRIMARY_KEY");
        Map<String, AttributeValue> tableKey = new HashMap<>();
        tableKey.put(primaryKey, AttributeValue.builder().s(id).build());
		
        DeleteItemRequest getItemRequest= DeleteItemRequest.builder()
                .key(tableKey)
                .tableName(tableName)
                .build();
        DeleteItemResponse response = ddb.deleteItem(getItemRequest);
        return response.toString();
	}

}
