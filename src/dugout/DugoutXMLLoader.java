/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dugout;

import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mpill
 */
public class DugoutXMLLoader {
    
    public static Team load(File xmlRosterFile) throws Exception {
        Team team = new Team();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() {
                Player player = null;
                boolean firstNameFlag = false;
                boolean lastNameFlag = false;
                boolean positionFlag = false;
                boolean battingAverageFlag = false;

                @Override
                public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                    if (qName.equalsIgnoreCase("player")) {
                        player = new Player();
                        String idString = attributes.getValue("id");
                        if (idString != null) {
                            int id = 0;
                            try {
                                id = Integer.parseInt(idString);
                            } catch (NumberFormatException e) {
                                throw new SAXException("Player id in xml could not be converted to an int");
                            }
                            player.setId(id);
                        }
                    }
                    if (qName.equalsIgnoreCase("firstname")) {
                        firstNameFlag = true;
                    }
                    if (qName.equalsIgnoreCase("lastname")) {
                        lastNameFlag = true;
                    }
                    if (qName.equalsIgnoreCase("position")) {
                        positionFlag = true;
                    }
                    if (qName.equalsIgnoreCase("battingaverage")) {
                        battingAverageFlag = true;
                    }
                }

                @Override
                public void endElement(String uri, String localName, String qName) throws SAXException {
                    if (qName.equalsIgnoreCase("player")) {
                        team.addPlayer(player);
                        player = null;
                    }
                    if (qName.equalsIgnoreCase("firstname")) {
                        firstNameFlag = false;
                    }
                    if (qName.equalsIgnoreCase("lastname")) {
                        lastNameFlag = false;
                    }
                    if (qName.equalsIgnoreCase("position")) {
                        positionFlag = false;
                    }
                    if (qName.equalsIgnoreCase("battingaverage")) {
                        battingAverageFlag = false;
                    }
                }
                
                @Override
                public void characters(char ch[], int start, int length) throws SAXException {
                    if (firstNameFlag) {
                        if (player != null) {
                            player.setFirstname(new String(ch, start, length));
                        }
                    }
                    if (lastNameFlag) {
                        if (player != null) {
                            player.setLastname(new String(ch, start, length));
                        }
                    }
                    if (positionFlag) {
                        if (player != null) {
                            player.setPosition(new String(ch, start, length));
                        }
                    }
                    if (battingAverageFlag) {
                        if (player != null) {
                            String battingAverageString = new String(ch, start, length);
                            double battingAverage = 0.0;
                            try {
                                battingAverage = Double.parseDouble(battingAverageString);
                            } catch (NumberFormatException e) {
                                throw new SAXException("Batting Average could not be converted to Double!");
                            }
                            
                            player.setBattingAverage(battingAverage);
                        }
                    }
                }
            };
            saxParser.parse(xmlRosterFile.getAbsoluteFile(), handler);
        } catch (Exception ex) {
            throw ex;
        }

    return team;
    }
}
