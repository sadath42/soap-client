package com.iwslim5ap;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Holder;
import javax.xml.ws.soap.SOAPBinding;

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
	byte[] xattach = Base64.getEncoder().encode(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
	 iwsLim5APPort.imGsd("testimg", Files.readAllBytes(Paths.get(file.getAbsolutePath())));
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
	//iwsLim5APPort.loadIMG1(xctxt, xretxt, xrpic1, xrpic1, xrpic1);

    }

}
