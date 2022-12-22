package com.example.m08actividad02_2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XmlController {

    public ArrayList<Currency> getCurrencies(String xmlString) {
        NodeList cubeNodes = toCubeNodeList(xmlString);
        ArrayList<Currency> currencies = new ArrayList<>();

        String date = "";

        for (int i = 0; i < cubeNodes.getLength(); i++) {
            Element element = (Element) cubeNodes.item(i);

            if (element.hasAttribute("time")){
                date = element.getAttribute("time");
            }

            if(element.hasAttribute("currency")) {
                String name = element.getAttribute("currency");
                String rate = element.getAttribute("rate");

                Currency currency = new Currency(i, name, Double.parseDouble(rate), date);
                currencies.add(currency);
            }
        }

        return currencies;
    }

    private NodeList toCubeNodeList(String xmlString) {
        Document document = toDocument(xmlString);
        NodeList nodeList = document.getElementsByTagName("Cube");

        return nodeList;
    }

    private Document toDocument(String xmlString)
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try
        {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
