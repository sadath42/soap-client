package com.iwslim5ap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class SOAPMessageWriterHandler implements SOAPHandler<SOAPMessageContext> {

    public boolean handleMessage(SOAPMessageContext smc) {

	Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

	SOAPMessage message = smc.getMessage();

	try {
	    if (!outboundProperty.booleanValue()) {
		System.out.println("SOAP Response : ");
		printMessage(message, "Response");
	    } else {
		System.out.println("SOAP Request : ");
		printMessage(message, "Request");
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return outboundProperty;
    }

    public Set getHeaders() {
	return null;
    }

    public boolean handleFault(SOAPMessageContext context) {
	printMessage(context.getMessage(), "fault");
	return true;
    }

    public void close(MessageContext context) {
    }

    private void printMessage(SOAPMessage soapMessage, String type) {

	try {
	    File file = new File("/" + type + ".xml");
	    file.createNewFile();
	    FileOutputStream fileOutputStream = new FileOutputStream(file);
	    soapMessage.writeTo(fileOutputStream);
	    fileOutputStream.flush();
	    fileOutputStream.close();
	    SAXBuilder b = new SAXBuilder();
	    org.jdom2.Document doc = b.build(file);
	    XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
	    xmlOutputter.output(doc, System.out);
	} catch (SOAPException | IOException | JDOMException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }

}