package com.sap;

import com.jayway.restassured.RestAssured;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.net.URL;
import java.util.ArrayList;

import com.sap.HelloWorldServlet;
import com.sap.cloud.sdk.testutil.MockUtil;

import static com.jayway.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith( Arquillian.class )
public class HelloWorldServiceTest
{
    private static final MockUtil mockUtil = new MockUtil();

    @ArquillianResource
    private URL baseUrl;

    @Deployment
    public static WebArchive createDeployment()
    {
        return TestUtil.createDeployment(HelloWorldServlet.class);
    }

    @BeforeClass
    public static void beforeClass()
    {
        mockUtil.mockDefaults();
        mockUtil.mockErpDestination("ErpQueryEndpoint", "erp");
        mockUtil.mockDestination("SuccessFactorsODataEndpoint", "sfsf");
    }

    @Before
    public void before()
    {
        RestAssured.baseURI = baseUrl.toExternalForm();
    }

    @Test
    public void testService()
    {
        final ArrayList<String> companyCode = given().get("/costcenter-employees")
                .then()
                .log().all()
                .extract().path("companyCode");

        assertThat(!companyCode.isEmpty());
        assertThat(companyCode.get(0).equals("1010"));

        //final String body = given().get("/hello").body().asString();
        //assertThat(body).isEqualToIgnoringCase("Hello World!");
    }
}
