package com.fiap.aws.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "trip")
public class Trip {

	@DynamoDBHashKey(attributeName = "trip")
	private String trip;

	@DynamoDBIndexRangeKey(attributeName = "country", localSecondaryIndexName = "countryIndex")
	private String country;

	@DynamoDBRangeKey(attributeName = "date")
	private String date;

	@DynamoDBIndexRangeKey(attributeName = "city", localSecondaryIndexName = "cityIndex")
	private String city;

	@DynamoDBAttribute(attributeName = "reason")
	private String reason;

	@Override
    public String toString(){
        return "Trip: " + this.trip + " | Country: " + this.country + " | City: " + this.city  + " | Date: " + this.date + " | Reason: " + this.reason;
    }

	public Trip(String country, String date, String city, String reason) {
		super();
		this.country = country;
		this.date = date;
		this.city = city;
		this.reason = reason;
	}

	public String getTrip() {
		return trip;
	}

	public void setTrip(String trip) {
		this.trip = trip;
	}

	public Trip() {
		super();
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String Country) {
		this.country = Country;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}