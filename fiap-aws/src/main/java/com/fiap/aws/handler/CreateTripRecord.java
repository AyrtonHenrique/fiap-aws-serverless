package com.fiap.aws.handler;

import java.io.IOException;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.aws.dao.TripRepository;
import com.fiap.aws.model.HandlerRequest;
import com.fiap.aws.model.HandlerResponse;
import com.fiap.aws.model.Trip;

public class CreateTripRecord implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final TripRepository repository = new TripRepository();

	@Override
	public HandlerResponse handleRequest(final HandlerRequest request, final Context context) {

		Trip trip = null;

		try {
			trip = new ObjectMapper().readValue(request.getBody(), Trip.class);
		} catch (IOException e) {
			return HandlerResponse.builder().setStatusCode(400).setRawBody("deu ruim na trip!").build();
		}
		context.getLogger().log("Criando a sua trip " + trip.toString());
		context.getLogger().log("variavel banco de dados:" + System.getenv("ENDPOINT_OVERRIDE"));
		Trip tripRecorded;
		try {
			tripRecorded = repository.save(trip, context);
		} catch (Exception e) {
			//TODO: handle exception
			return HandlerResponse.builder().setStatusCode(400).setRawBody("deu ruiiiiiiim na trip!").build();
		}

		return HandlerResponse.builder().setStatusCode(201).setObjectBody(tripRecorded).build();
	}
}