package com.mcfly.poll.domain.converter;

import com.mcfly.poll.domain.user_role.RoleName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<RoleName, String> {


    @Override
    public String convertToDatabaseColumn(RoleName attribute) {
        return attribute.getName();
    }

    @Override
    public RoleName convertToEntityAttribute(String dbData) {
        return RoleName.valueOfName(dbData);
    }
}
