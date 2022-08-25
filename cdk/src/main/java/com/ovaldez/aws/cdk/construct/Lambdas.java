package com.ovaldez.aws.cdk.construct;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.constructs.Construct;

public class Lambdas extends Construct {

	private final Function getOneItemFunction;
	private final Function createItemFunction;
	private final Function getAllItemFunction;
	private final Function updateItemFunction;
	private final Function deleteItemFunction;

	public Lambdas(@NotNull Construct scope, @NotNull String id, Table itemTable) {
		super(scope, id);
		
		
		Map<String, String> lambdaEnvMap = new HashMap<>();
        lambdaEnvMap.put("TABLE_NAME", itemTable.getTableName());
        lambdaEnvMap.put("PRIMARY_KEY","itemId");
        
        this.getOneItemFunction = getFunction(lambdaEnvMap,"com.ovaldez.aws.crud.read.GetOneItem::handleRequest", "getOneItem");
        this.createItemFunction = getFunction(lambdaEnvMap,"com.ovaldez.aws.crud.read.CreateItem::handleRequest", "createItem");
        this.getAllItemFunction = getFunction(lambdaEnvMap,"com.ovaldez.aws.crud.read.GetAllItems::handleRequest", "getAllItem");
        this.updateItemFunction = getFunction(lambdaEnvMap,"com.ovaldez.aws.crud.read.UpdateItem::handleRequest", "updateItem");
        this.deleteItemFunction = getFunction(lambdaEnvMap,"com.ovaldez.aws.crud.read.DeleteItem::handleRequest", "deleteItem");
        itemTable.grantReadWriteData(getOneItemFunction);
        itemTable.grantReadWriteData(createItemFunction);
        itemTable.grantReadWriteData(getAllItemFunction);
        itemTable.grantReadWriteData(updateItemFunction);
        itemTable.grantReadWriteData(deleteItemFunction);
        
	}
	
	private Function getFunction(Map<String, String> lambdaEnvMap, String handler, String functionName) {
		return Function.Builder
					.create(this, functionName)
					.timeout(Duration.seconds(20))
					.memorySize(512)
					.runtime(Runtime.JAVA_8)
					.code(Code.fromAsset("/Users/macbookpro/Documents/workspace-aws/aws-lambda-crud/lambda/target/aws-lambda-1.0.0-LAMBDA-jar-with-dependencies.jar"))
					.environment(lambdaEnvMap)
					.handler(handler)
					.functionName(functionName)
					.build();
	}

	public Function getGetOneItemFunction() {
		return getOneItemFunction;
	}

	public Function getCreateItemFunction() {
		return createItemFunction;
	}

	public Function getGetAllItemFunction() {
		return getAllItemFunction;
	}

	public Function getUpdateItemFunction() {
		return updateItemFunction;
	}

	public Function getDeleteItemFunction() {
		return deleteItemFunction;
	}
	

}
