package com.iwslim5ap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.FileDataSource;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPBinding;

import org.apache.cxf.attachment.AttachmentUtil;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Message;
import org.tempuri.IWSLim5AP;
import org.tempuri.IWSLim5APservice;

public class IWSLim5ApClient {

    public static void main(String[] args) throws IOException {
	IWSLim5APservice iwsLim5APservice = new IWSLim5APservice();
	ClientHandlerResolver clientHandlerResolver = new ClientHandlerResolver();
	iwsLim5APservice.setHandlerResolver(clientHandlerResolver);
	IWSLim5AP iwsLim5APPort = iwsLim5APservice.getIWSLim5APPort();
	// String txtcompname = iwsLim5APPort.txtcompname();
	// System.out.println(txtcompname);
	Binding binding = ((BindingProvider) iwsLim5APPort).getBinding();
	((SOAPBinding) binding).setMTOMEnabled(true);

	File file = new File("Capture.JPG");
	byte[] readAllBytes = Files.readAllBytes(Paths.get(file.getAbsolutePath()));
	byte[] xattach = Base64.getEncoder().encode(readAllBytes);
	iwsLim5APPort.textrec("test");
	//iwsLim5APPort.imGsd("testimg", xattach); 

	FileDataSource ds = new FileDataSource(file);

	List<Attachment> attachments = new ArrayList<Attachment>();
	Map<String, List<String>> headers = new HashMap<String, List<String>>();
	List<String> params = new ArrayList<>();
	params.add("multipart/Related");
	params.add("charset=utf-16");
	headers.put("Content-Type", params);
	params = new ArrayList<>();
	params.add("<http://www.borland.com/rootpart.xml>");
	headers.put("Content-ID", params);
	params = new ArrayList<>();
	params.add("base64");
	headers.put("Content-Transfer-Encoding", params);
	params = new ArrayList<>();
	params.add("http://www.borland.com/rootpart.xml");
	headers.put("Content-Location", params);
	params = new ArrayList<>();
	params.add("filename=Capture.JPG");
	headers.put("Content-Disposition", params);
	// AttachmentUtil.createMtomAttachment(isXop, mimeType, elementNS, data,
	// offset, length, threshold)
	Attachment attach = AttachmentUtil.createAttachment(ds.getInputStream(), headers);
	attachments.add(attach);

	BindingProvider bp = (BindingProvider) iwsLim5APPort;
	java.util.Map<String, Object> reqContext = bp.getRequestContext();
	reqContext.put(Message.ATTACHMENTS, attachments);
	reqContext.put(Message.CONTENT_TYPE, "multipart/Related");
	byte[] xattach1 = new byte[xattach.length];
	String xctxt = "test";

	System.out.println("\nStarting MTOM Test using basic byte array:");
	Holder<String> xretxt = new Holder("Sam");
	Holder<byte[]> xrpic1 = new Holder();
	int fileSize = (int) file.length();
	xrpic1.value = new byte[(int) file.length()];
	InputStream in = file.toURI().toURL().openStream();
	int len = in.read(xrpic1.value);
	while (len < fileSize) {
	    len += in.read(xrpic1.value, len, (int) (fileSize - len));
	}
	System.out.println("--Sending the me.bmp image to server");
	System.out.println("--Sending a name value of " + xretxt.value);
	Holder<byte[]> xrpic2 = null;
	Holder<byte[]> xrpic3 = null;
	// iwsLim5APPort.loadIMG1(xctxt, xretxt, xrpic1, xrpic1, xrpic1);
	
	
	

    }

}
