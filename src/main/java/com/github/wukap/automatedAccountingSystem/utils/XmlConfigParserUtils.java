package com.github.wukap.automatedAccountingSystem.utils;

import com.github.wukap.automatedAccountingSystem.model.config.ESConfig;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class XmlConfigParserUtils {
    private XmlConfigParserUtils() {
    }

    public static ESConfig parse(String path) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        List<ESConfig.Ffc> ffcList = new ArrayList<>();
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            File file = new File(path);

            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList ffcNodes = doc.getElementsByTagName("ffc");

            for (int i = 0; i < ffcNodes.getLength(); i++) {
                Element ffcElement = (Element) ffcNodes.item(i);

                List<ESConfig.Sensor> sensors = new ArrayList<>();
                NodeList sensorNodes = ffcElement.getElementsByTagName("sensor");
                for (int j = 0; j < sensorNodes.getLength(); j++) {
                    Element sensorElement = (Element) sensorNodes.item(j);
                    String id = sensorElement.getAttribute("id");
                    String item_id = sensorElement.getAttribute("item_id");
                    sensors.add(new ESConfig.Sensor(id, item_id));
                }
                List<ESConfig.EventTransaction> transactions = new ArrayList<>();
                List<ESConfig.EventStatus> statuses = new ArrayList<>();
                NodeList eventNodes = ffcElement.getElementsByTagName("event");
                for (int k = 0; k < eventNodes.getLength(); k++) {
                    Element eventElement = (Element) eventNodes.item(k);
                    String type = eventElement.getAttribute("type");
                    switch (type) {
                        case "transaction": {
                            Map<String, String> tags = new HashMap<>();
                            NamedNodeMap attributes = eventElement.getAttributes();
                            for (int l = 0; l < attributes.getLength(); l++) {
                                Node attr = attributes.item(l);
                                if (!attr.getNodeName().equals("type") && !attr.getNodeName().equals("UU_id")) {
                                    tags.put(attr.getNodeName(), attr.getNodeValue());
                                }
                            }
                            transactions.add(new ESConfig.EventTransaction(type, tags));
                            break;
                        }

                        case "status": {
                            statuses.add(new ESConfig.EventStatus(type, eventElement.getAttribute("tag"), eventElement.getAttribute("UU_id")));
                            break;
                        }
                        default:
                            log.error("Unknown event type: " + type);
                            break;
                    }

                }
                Element settingsElement = (Element) ffcElement.getElementsByTagName("settings").item(0);
                int ffc_id = Integer.parseInt(settingsElement.getElementsByTagName("ffc_id").item(0).getTextContent());
                int measure_events_period = Integer.parseInt(settingsElement.getElementsByTagName("measure_events_period").item(0).getTextContent());
                int history_days = Integer.parseInt(settingsElement.getElementsByTagName("history_days").item(0).getTextContent());

                ESConfig.Settings settings = new ESConfig.Settings(ffc_id, measure_events_period, history_days);

                ffcList.add(new ESConfig.Ffc(sensors, transactions, statuses, settings));
            }

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return new ESConfig(ffcList);
    }
}
