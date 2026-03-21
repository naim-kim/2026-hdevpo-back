package com.csee.swplus.mileage.portfolio.converter;

import com.csee.swplus.mileage.portfolio.dto.TechStackItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA converter: List&lt;TechStackItem&gt; ↔ JSON.
 * Supports backward compatibility: old format ["Java","React"] is migrated to
 * [{name:"Java",domain:null,level:null}, ...].
 */
@Converter
public class TechStackListConverter implements AttributeConverter<List<TechStackItem>, String> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(List<TechStackItem> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }
        try {
            return OBJECT_MAPPER.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to serialize tech stack to JSON", e);
        }
    }

    @Override
    public List<TechStackItem> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.trim().isEmpty()) {
            return new ArrayList<>();
        }
        try {
            JsonNode root = OBJECT_MAPPER.readTree(dbData);
            if (!root.isArray()) {
                return new ArrayList<>();
            }
            List<TechStackItem> result = new ArrayList<>();
            for (JsonNode node : root) {
                if (node.isTextual()) {
                    // Old format: ["Java", "React"] -> {name, domain: null, level: null}
                    result.add(TechStackItem.builder()
                            .name(node.asText())
                            .domain(null)
                            .level(null)
                            .build());
                } else if (node.isObject()) {
                    result.add(OBJECT_MAPPER.treeToValue(node, TechStackItem.class));
                }
            }
            return result;
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to deserialize tech stack JSON", e);
        }
    }
}
