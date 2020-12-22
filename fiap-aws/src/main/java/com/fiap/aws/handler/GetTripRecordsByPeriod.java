package com.fiap.aws.handler;

import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fiap.aws.dao.TripRepository;
import com.fiap.aws.model.HandlerRequest;
import com.fiap.aws.model.HandlerResponse;
import com.fiap.aws.model.Trip;



public class GetTripRecordsByPeriod implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final TripRepository repository = new TripRepository();
	
	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		// final String trip = request.getPathParameters().get("trip");
		final String starts = request.getQueryStringParameters().get("start");
		final String ends = request.getQueryStringParameters().get("end");

		context.getLogger().log("Procurando por trip  entre " + starts + " e " + ends);

		final List<Trip> trips = this.repository.findByPeriod(starts, ends);
		
		if(trips == null || trips.isEmpty()) {
			return HandlerResponse.builder().setStatusCode(404).build();
		}
		
		return HandlerResponse.builder().setStatusCode(200).setObjectBody(trips).build();
	}
}