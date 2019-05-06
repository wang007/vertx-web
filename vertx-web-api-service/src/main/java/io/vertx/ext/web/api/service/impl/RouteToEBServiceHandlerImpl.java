package io.vertx.ext.web.api.service.impl;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.service.RouteToEBServiceHandler;
import io.vertx.ext.web.api.service.ServiceRequest;
import io.vertx.ext.web.api.service.ServiceResponse;
import io.vertx.ext.web.validation.RequestParameters;

import java.util.function.Function;

public class RouteToEBServiceHandlerImpl implements RouteToEBServiceHandler {

  private final EventBus eventBus;
  private final String address;
  private final DeliveryOptions deliveryOptions;
  private Function<RoutingContext, JsonObject> extraPayloadMapper;

  public RouteToEBServiceHandlerImpl(EventBus eventBus, String address, DeliveryOptions deliveryOptions) {
    this.eventBus = eventBus;
    this.address = address;
    this.deliveryOptions = deliveryOptions;
  }

  @Override
  public void handle(RoutingContext routingContext) {
    eventBus.send(address, buildPayload(routingContext), deliveryOptions, (AsyncResult<Message<JsonObject>> res) -> {
      if (res.succeeded()) {
        ServiceResponse op = new ServiceResponse(res.result().body());
        HttpServerResponse response = routingContext.response().setStatusCode(op.getStatusCode());
        if (op.getStatusMessage() != null)
          response.setStatusMessage(op.getStatusMessage());
        if (op.getHeaders() != null)
          op.getHeaders().forEach(h -> response.putHeader(h.getKey(), h.getValue()));
        if (op.getPayload() != null)
          response.end(op.getPayload().toString());
        else
          response.end();
      } else {
        routingContext.fail(res.cause());
      }
    });
  }

  @Override
  @Fluent
  public RouteToEBServiceHandlerImpl extraPayloadMapper(Function<RoutingContext, JsonObject> extraPayloadMapper) {
    this.extraPayloadMapper = extraPayloadMapper;
    return this;
  }

  private JsonObject buildPayload(RoutingContext context) {
    return new JsonObject().put("context", new ServiceRequest(
      ((RequestParameters)context.get("parsedParameters")).toJson(),
      context.request().headers(),
      (context.user() != null) ? context.user().principal() : null,
      (this.extraPayloadMapper != null) ? this.extraPayloadMapper.apply(context) : null
    ).toJson());
  }

}
