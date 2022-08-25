package com.ovaldez.aws.crud.read;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.ovaldez.aws.crud.GatewayResponse;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

public class GetAllItems implements RequestHandler<Map<String,Object>, GatewayResponse> {

	private static Logger logger = LoggerFactory.getLogger(GetAllItems.class);
	@Override
	public GatewayResponse handleRequest(Map<String, Object> input, Context context) {
		logger.info("Inside software.amazon.awscdk.examples.lambda: getAllItems ");
		String output = getData();

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        return new GatewayResponse(output, headers, 200);
		
	}
	private String getData() {
		DynamoDbClient ddbClient = DynamoDbClient.create();
		String tableName= System.getenv("TABLE_NAME");
		ScanRequest scanRequest = ScanRequest.builder()
									.tableName(tableName)
									.build();
		ScanResponse response = ddbClient.scan(scanRequest);
		return response.toString();
	}

}
