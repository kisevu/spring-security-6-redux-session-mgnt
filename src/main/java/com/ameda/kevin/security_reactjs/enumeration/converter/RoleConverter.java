package com.ameda.kevin.security_reactjs.enumeration.converter;/*
*
@author ameda
@project security-reactjs
*
*/

import com.ameda.kevin.security_reactjs.enumeration.Authority;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Authority,String> {

    /**
     * Converts the value stored in the entity attribute into the
     * data representation to be stored in the database.
     *
     * @param authority the entity attribute value to be converted
     * @return the converted data to be stored in the database
     * column
     */
    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if(authority==null){
            return null;
        }
        return authority.getValue();
    }

    /**
     * Converts the data stored in the database column into the
     * value to be stored in the entity attribute.
     * Note that it is the responsibility of the converter writer to
     * specify the correct <code>dbData</code> type for the corresponding
     * column for use by the JDBC driver: i.e., persistence providers are
     * not expected to do such type conversion.
     *
     * @param code the data from the database column to be
     *               converted
     * @return the converted value to be stored in the entity
     * attribute
     */

    @Override
    public Authority convertToEntityAttribute(String code) {
        if(code == null){
            return null;
        }
        return Stream.of(Authority.values())
                .filter(authority -> authority.getValue().equals(code))
                .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
