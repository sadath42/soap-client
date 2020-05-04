package com.iwslim5ap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Iterator;
import java.util.Set;

import javax.activation.DataSource;
import javax.xml.soap.AttachmentPart;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.apache.cxf.attachment.ByteDataSource;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import com.sun.xml.internal.messaging.saaj.soap.ver1_1.Message1_1Impl;

public class SOAPMessageWriterHandler implements SOAPHandler<SOAPMessageContext> {

    public boolean handleMessage(SOAPMessageContext smc) {

	Boolean outboundProperty = (Boolean) smc.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

	SOAPMessage message = smc.getMessage();
	
	message.getClass().getSimpleName();
	com.sun.xml.internal.messaging.saaj.soap.ver1_1.Message1_1Impl msg =  (Message1_1Impl) smc.getMessage();
	
	try {
	    if (!outboundProperty.booleanValue()) {
		System.out.println("SOAP Response : ");
		printMessage(message, "Response");
	    } else {/*
		System.out.println("SOAP Request : ");
		SOAPEnvelope envelope = message.getSOAPPart().getEnvelope();
		SOAPBody soapBody = envelope.getBody();
		SOAPFactory factory = SOAPFactory.newInstance();
		com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl ex = (com.sun.xml.internal.messaging.saaj.soap.SOAPPartImpl)message.getSOAPPart();
		String header1 = "multipart/related; boundary=\"--MIME_boundaryB0R9532143182121\"; type=\"text/xml\"";

	  ex.removeMimeHeader("Content-Type");
	  ex.addMimeHeader("Content-Type", header1);
		
		String[] header = message.getMimeHeaders().getHeader("Content-Type");
		message.getMimeHeaders().removeHeader("Content-Type");
		message.getSOAPHeader();
		File file = new File("Capture.JPG");
		// AttachmentPart attachmentPart = new AttachmentPartImpl();

		byte[] readAllBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
		byte[] xattach = Base64.getEncoder().encode(readAllBytes);
		DataSource ds = new ByteDataSource(xattach);
		// attachmentPart.setDataHandler(new DataHandler(ds));

		Iterator attachments = message.getAttachments();
		String content = null;
		while (attachments.hasNext()) {
		    AttachmentPart attachmentPart = (AttachmentPart) attachments.next();
		    attachmentPart.setContentType("application/binary");
		    attachmentPart.addMimeHeader("SOAPAction", "urn:WSLim5APIntf-IWSLim5AP#IMGsd");
		    attachmentPart.addMimeHeader("Content-Type", "application/binary");
		    attachmentPart.addMimeHeader("Content-transfer-encoding", "binary");
		    content = attachmentPart.getContentId().replaceAll("xattach=", "").replace("@jaxws.sun.com", "");
		    attachmentPart.setContentId(content);
		    break;

		}
		// message.addAttachmentPart(attachmentPart);
		SOAPElement img = (SOAPElement) soapBody.getChildElements().next();
		// QName createQName = soapBody.createQName("xattach ", "");
		SOAPElement createElement = factory.createElement("xattach");
		Name name = factory.createName("href");
		content = content.replaceAll("[<>]", "");
		createElement.addAttribute(name, "cid:" + content);
		// createElement.addAttribute(name, content);
		img.addChildElement(createElement);
		
		message.getMimeHeaders().addHeader("Content-Type", header1);
		message.getMimeHeaders().addHeader("SOAPAction", "urn:WSLim5APIntf-IWSLim5AP#IMGsd");
		message.getMimeHeaders().addHeader("Content-ID", "<http://www.borland.com/rootpart.xml>");
		message.getMimeHeaders().addHeader("Content-Location", "http://www.borland.com/rootpart.xml");

		message.saveChanges();*/
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