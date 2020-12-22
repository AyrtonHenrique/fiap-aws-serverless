package com.fiap.aws.handler;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fiap.aws.dao.TripRepository;
import com.fiap.aws.model.HandlerRequest;
import com.fiap.aws.model.HandlerResponse;
import com.fiap.aws.model.Trip;

public class GetTripRecordsByCountry implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final TripRepository repository = new TripRepository();

	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		// final String topic = request.getPathParameters().get("topic");
		//context.getLogger().log("Searching for registered studies for " + topic + " and tag equals " + tag);
		
		final String country = request.getQueryStringParameters().get("country");


		final List<Trip> trips = this.repository.findByCountry(country);

		if (trips == null || trips.isEmpty()) {
			return HandlerResponse.builder().setStatusCode(404).build();
		}

		return HandlerResponse.builder().setStatusCode(200).setObjectBody(trips).build();
	}
}