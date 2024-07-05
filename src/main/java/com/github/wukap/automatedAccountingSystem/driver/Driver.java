package com.github.wukap.automatedAccountingSystem.driver;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.opcfoundation.ua.common.ServiceResultException;

import java.sql.SQLException;

@Slf4j
public abstract class Driver<R, V> {

    public static final String DRIVER_TAG = "driver";

    @Getter
    private final String name = getClass().getSimpleName();


    protected abstract R read(String tagname) throws ServiceResultException;

    protected abstract boolean write_(V value) throws SQLException;


    public boolean write(V value) throws SQLException {

        try {
            return write_(value);

        } catch (SQLException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception while writing to driver " + getName(), e);
        }
        return false;
    }


}
