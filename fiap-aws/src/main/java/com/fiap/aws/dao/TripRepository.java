package com.fiap.aws.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.fiap.aws.model.Trip;

public class TripRepository {

	private static final DynamoDBMapper mapper = DynamoDBManager.mapper();

	public Trip save(final Trip trip,final Context context) {
		context.getLogger().log("salvando");
		mapper.save(trip);
		context.getLogger().log("salvo");
		return trip;
	}

	public List<Trip> findByPeriod(final String starts, final String ends, final Context context) {

		final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		// eav.put(":val1", new AttributeValue().withS(trip));
		eav.put(":val1", new AttributeValue().withS(starts));
		eav.put(":val2", new AttributeValue().withS(ends));

		final Map<String, String> expression = new HashMap<>();
		// consumed is a reserver word in DynamoDB
		expression.put("#date", "date");
		
		context.getLogger().log("INICIANDO BUCAs DYNAMO DB");
		
		// final DynamoDBQueryExpression<Trip> queryExpression = new DynamoDBQueryExpression<Trip>().withSelect("")
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("#date between :val1 and :val2")
				.withExpressionAttributeValues(eav)
				.withExpressionAttributeNames(expression);

		final List<Trip> trips = mapper.scan(Trip.class, scanExpression);

		context.getLogger().log("finalizando BUsCA DYNAMO DB");
		return trips;
	}

	public List<Trip> findByCountry(final String country) {

		final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		// eav.put(":val1", new AttributeValue().withS(trip));
		eav.put(":val1", new AttributeValue().withS(country));

		// final DynamoDBQueryExpression<Trip> queryExpression = new DynamoDBQueryExpression<Trip>()
		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("country=:val1")
				.withExpressionAttributeValues(eav);

		final List<Trip> trips = mapper.scan(Trip.class, scanExpression);

		return trips;
	}

	public List<Trip> findByCity(final String country, final String city) {

		final Map<String, AttributeValue> eav = new HashMap<String, AttributeValue>();
		eav.put(":val1", new AttributeValue().withS(country));
		eav.put(":val2", new AttributeValue().withS(city));

		// final Map<String, String> expression = new HashMap<>();

		// final DynamoDBQueryExpression<Trip> queryExpression = new DynamoDBQueryExpression<Trip>()
		// 		.withIndexName("cityIndex").withConsistentRead(false)
		// 		.withKeyConditionExpression("country=:val1 and city=:val2").withExpressionAttributeValues(eav)
		// 		.withExpressionAttributeNames(expression);

		DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
				.withFilterExpression("country=:val1 and city=:val2")
				.withExpressionAttributeValues(eav);


		final List<Trip> trips = mapper.scan(Trip.class, scanExpression);

		return trips;
	}
}
