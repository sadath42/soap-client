package com.iwslim5ap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.cxf.helpers.IOUtils;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;

public class RestAssuredClient {

    public static void main(String[] args) throws IOException {
	FileInputStream fileInputStream = new FileInputStream(new File("simple.xml"));
	RestAssured.baseURI = "http://s11gs3s.doomdns.com:9091/WSDL/soap/IWSLim5AP";

	Response response = RestAssured.given().header("Content-Type", "text/xml")
		.header("SOAPAction", "urn:WSLim5APIntf-IWSLim5AP#textrec").and()
		.body(IOUtils.toString(fileInputStream, "UTF-8")).when().post().then().and().log().all().extract()
		.response();

	XmlPath jsXpath = new XmlPath(response.asString());// Converting string
							   // into xml path to
							   // assert

    }
}
