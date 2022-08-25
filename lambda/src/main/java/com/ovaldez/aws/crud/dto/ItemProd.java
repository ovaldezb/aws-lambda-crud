package com.ovaldez.aws.crud.dto;

import com.google.gson.Gson;

public class ItemProd {
	
	private String itemId;
	private String name;
	private String qty;
	
	public ItemProd(String itemId, String name, String qty) {
		super();
		this.itemId = itemId;
		this.name = name;
		this.qty = qty;
	}
	
	public ItemProd(String jsonItemProd) {
		Gson gson = new Gson();
		ItemProd tempItemProd = gson.fromJson(jsonItemProd, ItemProd.class);
		this.itemId = tempItemProd.getItemId();
		this.name = tempItemProd.getName();
		this.qty = tempItemProd.getQty();
	}
	
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getQty() {
		return qty;
	}
	public void setQty(String qty) {
		this.qty = qty;
	}
	
	@Override
	public String toString() {
		return "\"item\":{\"itemId\":\""+this.itemId+"\",\"name\":\""+this.name+"\",\"qty\":"+this.qty+"}";
	}

}
