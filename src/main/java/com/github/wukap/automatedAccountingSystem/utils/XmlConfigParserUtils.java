package com.github.wukap.automatedAccountingSystem.utils;

import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class XmlConfigParserUtils {
    private XmlConfigParserUtils() {
    }

    public static InputConfig parse(String path) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        InputConfig config = new InputConfig();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            File file = new File(path);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            Element settingsElement = (Element) doc.getElementsByTagName("settings").item(0);
            int measureEventsPeriod = Integer.parseInt(settingsElement.getElementsByTagName("measure_events_period").item(0).getTextContent());
            int historyDays = Integer.parseInt(settingsElement.getElementsByTagName("history_days").item(0).getTextContent());
            int ffc_id = Integer.parseInt(settingsElement.getElementsByTagName("ffc_id").item(0).getTextContent());

            config.setSettings(new InputConfig.Settings(measureEventsPeriod, historyDays, ffc_id));

            List<InputConfig.Sensor> sensors = new ArrayList<>();
            NodeList sensorNodes = doc.getElementsByTagName("sensor");
            for (int j = 0; j < sensorNodes.getLength(); j++) {
                Element sensorElement = (Element) sensorNodes.item(j);
                String id = sensorElement.getAttribute("id");
                String item_id = sensorElement.getAttribute("item_id");
                sensors.add(new InputConfig.Sensor(id, item_id));
            }
            List<InputConfig.EventTransaction> transactions = new ArrayList<>();
            List<InputConfig.EventStatus> statuses = new ArrayList<>();
            NodeList eventNodes = doc.getElementsByTagName("event");
            for (int k = 0; k < eventNodes.getLength(); k++) {
                Element eventElement = (Element) eventNodes.item(k);
                String type = eventElement.getAttribute("type");
                switch (type) {
                    case "transaction": {
                        String tagStart = eventElement.getAttribute("tag_Start");
                        String tag1 = eventElement.getAttribute("tag1");
                        String tag2 = eventElement.getAttribute("tag2");
                        String tag3 = eventElement.getAttribute("tag3");
                        String tag4 = eventElement.getAttribute("tag4");
                        transactions.add(new InputConfig.EventTransaction(type, tagStart, tag1, tag2, tag3, tag4));
                        break;
                    }

                    case "status": {
                        statuses.add(new InputConfig.EventStatus(type, eventElement.getAttribute("tag"), eventElement.getAttribute("UU_id")));
                        break;
                    }
                    default:
                        log.error("Unknown event type: " + type);
                        break;
                }
            }
            config.setSensors(sensors);
            config.setEventTransactions(transactions);
            config.setEventStatuses(statuses);

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
            return null;
        }
        return config;
    }
}
