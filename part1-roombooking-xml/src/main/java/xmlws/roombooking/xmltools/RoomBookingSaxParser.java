package xmlws.roombooking.xmltools;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RoomBookingSaxParser implements RoomBookingParser {

    private RoomBooking rb = new RoomBooking();

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public RoomBooking parse(InputStream inputStream) {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(inputStream, new RoomBookingSaxParser.RoomBookingHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rb;
    }

    private class RoomBookingHandler extends DefaultHandler {

        boolean label;
        boolean username;
        boolean startDate;
        boolean endDate;

        public void startElement(String namespaceURI,
                                 String localName,
                                 String qName,
                                 Attributes atts)
                throws SAXException {

            switch (localName) {
                case "label":
                    label = true;
                    break;
                case "username":
                    username = true;
                    break;
                case "startDate":
                    startDate = true;
                    break;
                case "endDate":
                    endDate = true;
                    break;
            }
        }

        public void characters(char ch[], int start, int length)
                throws SAXException {

            if (label) {
                rb.setRoomLabel(new String(ch, start, length));
                label = false;
            } else if (username) {
                rb.setUsername(new String(ch, start, length));
                username = false;
            } else if (startDate) {
                try {
                    rb.setStartDate(sdf.parse(new String(ch, start, length)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                startDate = false;
            } else if (endDate) {
                try {
                    rb.setEndDate(sdf.parse(new String(ch, start, length)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                endDate = false;
            }
        }

    }

}