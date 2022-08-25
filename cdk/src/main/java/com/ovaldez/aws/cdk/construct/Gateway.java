package com.ovaldez.aws.cdk.construct;

import java.util.Map;

import org.jetbrains.annotations.NotNull;

import software.amazon.awscdk.services.apigateway.IResource;
import software.amazon.awscdk.services.apigateway.Integration;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.RestApiProps;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

public class Gateway extends Construct {

	public Gateway(@NotNull Construct scope, @NotNull String id, Map<String, Function> props) {
		super(scope, id);
		RestApi apiGw = new RestApi(this, "Item API Gw", RestApiProps.builder()
														.restApiName("Items Service")
														.build());
		IResource items = apiGw.getRoot().addResource("items");
		IResource singleItem = items.addResource("{id}");
		Integration getOneIntegration = new LambdaIntegration((Function)props.get("getOneItem"));
        singleItem.addMethod("GET",getOneIntegration);
        
        Integration updateIntegration = new LambdaIntegration((Function)props.get("updateItem"));
        singleItem.addMethod("PUT",updateIntegration);
        
        Integration deleteOneIntegration = new LambdaIntegration((Function)props.get("deleteItem"));
        singleItem.addMethod("DELETE",deleteOneIntegration);
        
        Integration getAllIntegration = new LambdaIntegration((Function)props.get("getAllItems"));
        items.addMethod("GET", getAllIntegration);
        
        Integration createOneIntegration = new LambdaIntegration((Function)props.get("createItem"));
        items.addMethod("POST", createOneIntegration);
	}

}
