import com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public  class XMLStaxCursorParser {
    private static boolean titleFlag;
    private static boolean artistFlag;
    private static boolean countryFlag;
    private static boolean companyFlag;
    private static boolean priceFlag;
    private static boolean yearFlag;

        public static void validateXSD(String fileName) throws IOException, SAXException, XMLStreamException {
        XMLStreamReader reader = XMLInputFactory.newInstance().createXMLStreamReader(new FileInputStream  (fileName));
        reader = new StreamReaderDelegate(reader) {
            public int next() throws XMLStreamException {
                int n = super.next();
                return n;
            }};
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(new File("src/main/resources/schema.xsd"));
        Validator validator = schema.newValidator();
        validator.validate(new StAXSource(reader));
    }
        public static void validateDTD(String fileName){
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
            DocumentBuilder builder = null;
            try {
                builder = factory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                Document doc = builder.parse(fileName);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    public static List<CdAlbum> parseXMLfile(String fileName) {
        List<CdAlbum> albumList = new ArrayList<>();
        CdAlbum album = null;
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(fileName));
            int event = reader.getEventType();
            while (true) {
                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        if (reader.getLocalName().equals("CD")) {
                            album = new CdAlbum();
                            album.setId(Integer.parseInt(reader.getAttributeValue(0)));
                        } else if (reader.getLocalName().equals("TITLE")) {
                            titleFlag = true;
                        } else if (reader.getLocalName().equals("ARTIST")) {
                            artistFlag = true;
                        } else if (reader.getLocalName().equals("COUNTRY")) {
                            countryFlag = true;
                        }else if (reader.getLocalName().equals("COMPANY")) {
                            companyFlag = true;
                        }else if (reader.getLocalName().equals("PRICE")) {
                            priceFlag = true;
                        }else if (reader.getLocalName().equals("YEAR")) {
                            yearFlag = true;
                        }
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if (titleFlag) {
                            album.setTitle(reader.getText());
                            titleFlag = false;
                        } else if (artistFlag) {
                            album.setArtist(reader.getText());
                            artistFlag = false;
                        } else if (countryFlag) {
                            album.setCountry(reader.getText());
                            countryFlag = false;
                        }else if (companyFlag) {
                            album.setCompany(reader.getText());
                            companyFlag = false;
                        }else if (priceFlag) {
                            album.setPrice(Double.parseDouble(reader.getText()));
                            priceFlag = false;
                        }else if (yearFlag) {
                            album.setYear(Integer.parseInt(reader.getText()));
                            yearFlag = false;
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (reader.getLocalName().equals("CD")) {
                            albumList.add(album);
                        }
                        break;
                }

                if (!reader.hasNext())
                    break;

                event = reader.next();
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            e.printStackTrace();
        }
        return albumList;
    }
}