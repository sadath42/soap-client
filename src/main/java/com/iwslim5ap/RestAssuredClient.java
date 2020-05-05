package com.iwslim5ap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;

public class RestAssuredClient {

    public static void main(String[] args) throws IOException {

	String soapRequest = " <?xml version=\"1.0\" encoding=\"utf-8\"?>"
		+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\">"
		+ "<SOAP-ENV:Body xmlns:NS1=\"urn:WSLim5APIntf-IWSLim5AP\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
		+ "<NS1:RecIMG>" + "<xctxt xsi:type=\"xsd:string\">test</xctxt>"
		+ "<xattach href=\"cid:665CFF81-7880-4BD9-BDE0-867FB78B59CB\"/>" + "</NS1:RecIMG>" + "</SOAP-ENV:Body>"
		+ "</SOAP-ENV:Envelope>".trim();

	FileInputStream fileInputStream = new FileInputStream(new File("Request2.xml"));
	RestAssured.baseURI = "http://s11gs3s.doomdns.com:9091/WSDL/soap/IWSLim5AP";
	File file = new File("Capture.JPG");
	Response response = RestAssured.given().header("Content-Type", "multipart/related;boundary=123")
		.header("Content-ID", "<http://www.borland.com/rootpart.xml>")
		.header("Content-Location", "http://www.borland.com/rootpart.xml")
		.header("SOAPAction", "\"urn:WSLim5APIntf-IWSLim5AP#IMGsd\"").and().body(soapRequest)
		.multiPart(getMultiPart()).when().log().all().post().then().and().log().all().extract().response();

	XmlPath jsXpath = new XmlPath(response.asString());// Converting string
							   // into xml path to
							   // assert

    }

    private static MultiPartSpecification getMultiPart2() throws IOException {
	String soapRequest = " <?xml version=\"1.0\" encoding=\"utf-8\"?>"
		+ "<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:SOAP-ENC=\"http://schemas.xmlsoap.org/soap/encoding/\">"
		+ "<SOAP-ENV:Body xmlns:NS1=\"urn:WSLim5APIntf-IWSLim5AP\" SOAP-ENV:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\">"
		+ "<NS1:RecIMG>" + "<xctxt xsi:type=\"xsd:string\">test</xctxt>"
		+ "<xattach href=\"cid:665CFF81-7880-4BD9-BDE0-867FB78B59CB\"/>" + "</NS1:RecIMG>" + "</SOAP-ENV:Body>"
		+ "</SOAP-ENV:Envelope>".trim();
	return new MultiPartSpecBuilder(soapRequest).header("Content-Type", "text/xml; charset=utf-8").header("Content-Length", soapRequest.length()+"").build();
    }

    private static MultiPartSpecification getMultiPart() throws IOException {
	File file = new File("Capture.JPG");
	byte[] readAllBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
	byte[] xattach = Base64.getEncoder().encode(readAllBytes);
	return new MultiPartSpecBuilder(xattach).fileName("Capture.JPG").controlName("file")
		.mimeType("application/binary").header("Content-Length", xattach.length+"")
		.header("Content-ID", "<665CFF81-7880-4BD9-BDE0-867FB78B59CB>")
		.header("Content-transfer-encoding", "binary").build();
    }
}
