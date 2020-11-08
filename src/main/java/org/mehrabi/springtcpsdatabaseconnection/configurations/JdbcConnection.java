/*
 * Copyright (c) 2020.
 * by Mohammad Mehrabi
 */

package org.mehrabi.springtcpsdatabaseconnection.configurations;

import oracle.jdbc.pool.OracleDataSource;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import static oracle.jdbc.OracleConnection.CONNECTION_PROPERTY_PASSWORD;
import static oracle.jdbc.OracleConnection.CONNECTION_PROPERTY_USER_NAME;

@Service
public class JdbcConnection {

    public static final String EXCEPTION_ON_DB = "Exception on db";
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JdbcConnection.class.getName());

    @PostConstruct
    private void init() {

        System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
        System.setProperty("javax.net.ssl.trustStorePassword", "trustStorePassword");
        System.setProperty("javax.net.ssl.trustStore", "trustStore");
        System.setProperty("javax.net.ssl.keyStore", "keyStore");
        System.setProperty("javax.net.ssl.keyStoreType", "JKS");
        System.setProperty("javax.net.ssl.keyStorePassword", "keyStorePassword");
    }

    private Connection openConnection() {
        Connection connection = null;
        try {
            Properties info = new Properties();
            info.put(CONNECTION_PROPERTY_USER_NAME, "user");
            info.put(CONNECTION_PROPERTY_PASSWORD, "password");
            OracleDataSource ods = new OracleDataSource();
            ods.setURL("urltcps");
            ods.setConnectionProperties(info);

            connection = ods.getConnection();
        } catch (SQLException e) {
            LOGGER.error(EXCEPTION_ON_DB + " ", e);
        }
        return connection;
    }


}