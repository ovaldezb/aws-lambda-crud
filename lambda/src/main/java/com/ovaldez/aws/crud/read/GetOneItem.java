package com.ovaldez.aws.crud.read;

import java.util.HashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ovaldez.aws.crud.GatewayResponse;
import com.ovaldez.aws.crud.dto.ItemProd;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;



public class GetOneItem implements RequestHandler<Map<String,Object>, GatewayResponse>{

	public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
		System.out.println("Inside: getOneItem "+input.getClass()+ " data:"+input);
		@SuppressWarnings("unchecked")
		Map<String, Object> pathParameters = (Map<String, Object>)input.get("pathParameters");
		String id = (String) pathParameters.get("id");
		System.out.println("Getting data for input parameter:"+id);
		
		String output = getData(id);
		System.out.println("Output: "+output);
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
		
		return new GatewayResponse(output, headers, 200);
	}

	private String getData(String id) {
		System.out.println("Dentro de getData");
		DynamoDbClient ddb = DynamoDbClient.create();
		String tableName = System.getenv("TABLE_NAME");
		String primaryKey = System.getenv("PRIMARY_KEY");
		Map<String, AttributeValue> tableKey = new HashMap<>();
		tableKey.put(primaryKey, AttributeValue.builder().s(id).build());
		GetItemRequest getItemRequest = GetItemRequest.builder()
				.key(tableKey)
				.tableName(tableName)
				.build();
		System.out.println("After Call");
		System.out.println(getItemRequest.toString());
		GetItemResponse response = ddb.getItem(getItemRequest);
		
		ItemProd itemProd = new ItemProd(response.item().get("itemId").s(),
										response.item().get("name").s(),
										response.item().get("qty").s() );
        return itemProd.toString();
	}
	
	

}
