package model.database.converters;

import model.actions.AAction;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ActionToStringConverter implements AttributeConverter<AAction, String> {
    @Override
    public String convertToDatabaseColumn(AAction attribute) {
        if(attribute == null){
            return null;
        }
        return attribute.getName();
    }

    @Override
    public AAction convertToEntityAttribute(String dbData) {
        if(dbData == null){
            return null;
        }
        return AAction.createByName(dbData);
    }
}
