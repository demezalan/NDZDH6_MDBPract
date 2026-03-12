package ndzdh6;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class NDZDH6DomQueryOwn {

    public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
        File xmlFile = new File("NDZDH6_XDM1.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = factory.newDocumentBuilder();

        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());


        // aktív státuszú beiratkozások
        System.out.println("\nAzok a beiratkozások, ahol a státusz aktív: \n");

        NodeList nList = doc.getElementsByTagName("beiratkozas");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String statusz = elem.getElementsByTagName("statusz").item(0).getTextContent();

                if ("aktív".equals(statusz)) {
                    String sid = elem.getAttribute("sid");
                    String om = elem.getAttribute("om");

                    String kepzes = elem.getElementsByTagName("kepzes_neve").item(0).getTextContent();
                    String tagozat = elem.getElementsByTagName("tagozat").item(0).getTextContent();
                    String kar = elem.getElementsByTagName("szervezeti_egyseg").item(0).getTextContent();

                    System.out.println("Hallgató ID: " + sid);
                    System.out.println("Egyetem OM: " + om);
                    System.out.println("Képzés: " + kepzes);
                    System.out.println("Tagozat: " + tagozat);
                    System.out.println("Kar: " + kar + "\n");
                }
            }
        }


        //magyar nyelvű kurzusok
        System.out.println("---------------------------\n\nMagyar nyelvű kurzusok: \n");
        nList = doc.getElementsByTagName("kurzus");

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String nyelv = elem.getElementsByTagName("nyelv").item(0).getTextContent();

                if ("magyar".equals(nyelv)) {
                    String id = elem.getAttribute("kurzuskod");

                    String tipus = elem.getElementsByTagName("tipus").item(0).getTextContent();
                    String idotartam = elem.getElementsByTagName("idotartam").item(0).getTextContent();
                    String maxFo = elem.getElementsByTagName("max_fo").item(0).getTextContent();

                    System.out.println("Kurzus kód: " + id);
                    System.out.println("Típus: " + tipus);
                    System.out.println("Időtartam: " + idotartam);
                    System.out.println("Max fő: " + maxFo + "\n");
                }
            }
        }


        // pesti egyetemek
        System.out.println("---------------------------\n\nBudapesti egyetemek: \n");
        nList = doc.getElementsByTagName("egyetem");

        for(int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;

                String varos = elem.getElementsByTagName("varos").item(0).getTextContent();

                if ("Budapest".equals(varos)) {
                    String om = elem.getAttribute("om");

                    String nev = elem.getElementsByTagName("nev").item(0).getTextContent();
                    String utca = elem.getElementsByTagName("utca").item(0).getTextContent();
                    String hazszam = elem.getElementsByTagName("hazszam").item(0).getTextContent();
                    String telefon = elem.getElementsByTagName("telefon").item(0).getTextContent();
                    String email = elem.getElementsByTagName("email").item(0).getTextContent();
                    String fax = elem.getElementsByTagName("fax").item(0).getTextContent();

                    String adr = varos + ", " + utca + " " + hazszam + ".";

                    System.out.println("Egyetem OM: " + om);
                    System.out.println("Név: " + nev);
                    System.out.println("Cím: " + adr);
                    System.out.println("Elérhetőség: tel: " + telefon + ", email: " + email + ", fax: " + fax + "\n");
                }
            }
        }
    }
}