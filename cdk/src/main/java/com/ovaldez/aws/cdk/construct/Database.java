package com.ovaldez.aws.cdk.construct;

import org.jetbrains.annotations.NotNull;

import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.TableProps;
import software.constructs.Construct;

public class Database extends Construct {
	private final Table itemTable;
	public Database(@NotNull Construct scope, @NotNull String id) {
		super(scope, id);
		this.itemTable = createItemTable();
	}
	
	private Table createItemTable() {
		Table tableItem = new Table(this,"items",
								TableProps.builder()
								.tableName("items")
								.partitionKey(Attribute.builder()
												.name("itemId")
												.type(AttributeType.STRING)
												.build()
										)
								.removalPolicy(RemovalPolicy.DESTROY)
								.billingMode(BillingMode.PAY_PER_REQUEST)
								.build()
				);
		
		return tableItem;
	}

	public Table getItemTable() {
		return itemTable;
	}

}
