package io.vertx.ext.web.validation;

import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.multipart.MultipartForm;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.regex.Pattern;

import static io.vertx.ext.web.validation.testutils.TestRequest.statusCode;
import static io.vertx.ext.web.validation.testutils.TestRequest.testRequest;
import static io.vertx.ext.web.validation.testutils.ValidationTestUtils.failurePredicateResponse;

/**
 * @author Francesco Guardiani @slinkydeveloper
 */
@SuppressWarnings("unchecked")
@ExtendWith(VertxExtension.class)
public class ValidationHandlerPredicatesIntegrationTest extends BaseValidationHandlerTest{

  @Test
  public void testRequiredBodyPredicate(VertxTestContext testContext) {
    Checkpoint checkpoint = testContext.checkpoint(2);

    ValidationHandler validationHandler = ValidationHandler
      .builder(parser)
      .predicate(RequestPredicate.BODY_REQUIRED)
      .build();

    router.route("/testRequiredBody")
      .handler(BodyHandler.create())
      .handler(validationHandler)
      .handler(routingContext ->
        routingContext
          .response()
          .setStatusCode(200)
          .end()
      );

    testRequest(client, HttpMethod.POST, "/testRequiredBody")
      .asserts(statusCode(200))
      .sendJson(new JsonObject(), testContext, checkpoint);

    testRequest(client, HttpMethod.GET, "/testRequiredBody")
      .asserts(statusCode(400), failurePredicateResponse())
      .send(testContext, checkpoint);

    testRequest(client, HttpMethod.POST, "/testRequiredBody")
      .asserts(statusCode(400), failurePredicateResponse())
      .send(testContext, checkpoint);
  }

  @Test
  public void testFileUploadExists(VertxTestContext testContext) {
    Checkpoint checkpoint = testContext.checkpoint(4);

    ValidationHandler validationHandler = ValidationHandler
      .builder(parser)
      .predicate(RequestPredicate.multipartFileUploadExists(
        "myfile",
        Pattern.compile(Pattern.quote("text/plain"))
      ))
      .build();

    router.post("/testFileUpload")
      .handler(BodyHandler.create())
      .handler(validationHandler)
      .handler(routingContext ->
        routingContext
          .response()
          .setStatusCode(200)
          .end()
      );

    testRequest(client, HttpMethod.POST, "/testFileUpload")
      .asserts(statusCode(200))
      .send(testContext, checkpoint);

    testRequest(client, HttpMethod.POST, "/testFileUpload")
      .asserts(statusCode(400))
      .sendMultipartForm(MultipartForm.create(), testContext, checkpoint);

    testRequest(client, HttpMethod.POST, "/testFileUpload")
      .asserts(statusCode(400))
      .sendMultipartForm(MultipartForm.create().attribute("myfile", "bla"), testContext, checkpoint);

    testRequest(client, HttpMethod.POST, "/testFileUpload")
      .asserts(statusCode(200))
      .sendMultipartForm(MultipartForm.create().textFileUpload("myfile", "myfile.txt", "src/test/resources/myfile.txt", "text/plain"), testContext, checkpoint);
  }

}
