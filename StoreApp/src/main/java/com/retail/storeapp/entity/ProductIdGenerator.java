package com.retail.storeapp.entity;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class ProductIdGenerator implements IdentifierGenerator {

    /*@Value("${product.id.generator.sequence.sql}")
    private String productSequenceGeneratorSql;*/

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        try {
            Connection connection = sharedSessionContractImplementor.connection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select MAX(product_id) from PRODUCT");
            //ResultSet resultSet = statement.executeQuery("select MAX(product_id) from product");
            BigInteger sequenceNumber = null;
            if(resultSet.next() && resultSet.getBigDecimal(1) != null)
                sequenceNumber = ((BigDecimal)resultSet.getBigDecimal(1)).toBigInteger();
            if(sequenceNumber == null || sequenceNumber.intValue() == 0) {
                ResultSet resultSetForVerification = statement.executeQuery("select count(*) from request");
                resultSetForVerification.next();
                if(resultSetForVerification.getInt(1) > 0)
                    throw new HibernateException("Unable to fetch last request number from Request table");
                sequenceNumber = new BigInteger("166781");
            }

            String computedRequestNumber = String.valueOf(sequenceNumber.add(new BigInteger(String.valueOf(System.currentTimeMillis()))));
            return computedRequestNumber;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(new BigInteger("156915337495583917").add(new BigInteger("166781")));
    }
}
