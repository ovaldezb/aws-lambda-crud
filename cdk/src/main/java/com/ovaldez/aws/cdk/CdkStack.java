package com.ovaldez.aws.cdk;

import java.util.HashMap;
import java.util.Map;

import com.ovaldez.aws.cdk.construct.Database;
import com.ovaldez.aws.cdk.construct.Gateway;
import com.ovaldez.aws.cdk.construct.Lambdas;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

public class CdkStack extends Stack {
    

    public CdkStack(final Construct scope, final String id) {
        super(scope, id);
        Map<String, Function> props = new HashMap<>();
        Database itemTable = new Database(this, "ItemTable");
        Lambdas lambdas = new Lambdas(this, "ItemLambda", itemTable.getItemTable());
        props.put("getOneItem", lambdas.getGetOneItemFunction());
        props.put("createItem", lambdas.getCreateItemFunction());
        props.put("getAllItems", lambdas.getGetAllItemFunction());
        props.put("updateItem", lambdas.getUpdateItemFunction());
        props.put("deleteItem", lambdas.getDeleteItemFunction());
        new Gateway(this, "ItemGateway", props);
    }
}
