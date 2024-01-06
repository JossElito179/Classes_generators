package com.generate.main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.generate.config.Config;
import com.generate.template.Template;

import connection.Connect;

public class Main {

    public static void main(String[] args) throws StreamReadException, DatabindException, IOException, ParserConfigurationException, SAXException, ClassNotFoundException, SQLException {
        ObjectMapper objectMapper = new ObjectMapper();
        File configJson = new File("./config/config.json");
        Config[] configuration = objectMapper.readValue(configJson, Config[].class);

        Connection c = null;

        File xmlFile = new File("./config/config.xml");
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        NodeList config = doc.getElementsByTagName("config");
        Element conf = (Element) config.item(0);
        String language = conf.getElementsByTagName("language").item(0).getTextContent();
        Element base = (Element) conf.getElementsByTagName("base").item(0);
        String serveur = base.getElementsByTagName("serveur").item(0).getTextContent();
        String database = base.getElementsByTagName("database").item(0).getTextContent();
        String table = base.getElementsByTagName("table").item(0).getTextContent();
        String user = base.getElementsByTagName("user").item(0).getTextContent();
        String password = base.getElementsByTagName("password").item(0).getTextContent();
        Element paths = (Element) conf.getElementsByTagName("paths").item(0);
        String packages = paths.getElementsByTagName("package").item(0).getTextContent();

        if (serveur.equals("mysql")) {
            c = Connect.mysql("jdbc:mysql://localhost:3306/" + database, user, password);
        } else if (serveur.equals("postgres")) {
            c = Connect.postgres("jdbc:postgresql://localhost:5432/" + database, user, password);
        }

        for (Config cfg : configuration) {
            if (cfg.getNom().equals(language)) {
                System.out.println();
                String content = Template.changeVariable(cfg, "./template/template.templ", packages, table, c);
                Template.saveToFile(content, table + "." +cfg.getExtension(), packages);
                break;
            }
        }
    }
}
