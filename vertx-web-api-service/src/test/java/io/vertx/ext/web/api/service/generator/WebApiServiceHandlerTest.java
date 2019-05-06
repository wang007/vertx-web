package io.vertx.ext.web.api.service.generator;

import io.vertx.codegen.GenException;
import io.vertx.ext.web.api.service.WebApiServiceGen;
import io.vertx.ext.web.api.service.generator.model.WebApiProxyModel;
import io.vertx.ext.web.api.service.generator.models.InvalidMissingContext;
import io.vertx.ext.web.api.service.generator.models.InvalidMissingHandler;
import io.vertx.ext.web.api.service.generator.models.InvalidWrongHandler;
import io.vertx.ext.web.api.service.generator.models.ValidWebApiProxy;
import io.vertx.test.codegen.GeneratorHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author <a href="https://github.com/slinkydeveloper">Francesco Guardiani</a>
 */
public class WebApiServiceHandlerTest {

  public WebApiProxyModel generateWebApiProxyModel(Class c, Class... rest) throws Exception {
    return new GeneratorHelper().generateClass(codegen -> (WebApiProxyModel) codegen.getModel(c.getCanonicalName(), "webapi_proxy"), WebApiServiceGen.class, c, rest);
  }

  @Test
  public void testValid() throws Exception {
    WebApiProxyModel model = generateWebApiProxyModel(ValidWebApiProxy.class);
    assertEquals(ValidWebApiProxy.class.getName(), model.getIfaceFQCN());
    assertEquals(ValidWebApiProxy.class.getSimpleName(), model.getIfaceSimpleName());
  }

  @Test
  public void testMissingContext() throws Exception {
    try {
      generateWebApiProxyModel(InvalidMissingContext.class);
      fail("Should throw exception");
    } catch (GenException e) {
      // OK
    }
  }

  @Test
  public void testWrongHandler() throws Exception {
    try {
      generateWebApiProxyModel(InvalidWrongHandler.class);
      fail("Should throw exception");
    } catch (GenException e) {
      // OK
    }
  }

  @Test
  public void testMissingHandler() throws Exception {
    try {
      generateWebApiProxyModel(InvalidMissingHandler.class);
      fail("Should throw exception");
    } catch (GenException e) {
      // OK
    }
  }
}
