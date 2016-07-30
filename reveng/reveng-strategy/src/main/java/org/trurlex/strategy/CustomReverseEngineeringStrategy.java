package org.trurlex.strategy;

import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.DelegatingReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.TableIdentifier;
import org.hibernate.mapping.Column;

import java.util.List;

public class CustomReverseEngineeringStrategy extends DelegatingReverseEngineeringStrategy {
    private final static DefaultReverseEngineeringStrategy defaultStrategy = new DefaultReverseEngineeringStrategy();

    public CustomReverseEngineeringStrategy(ReverseEngineeringStrategy delegate) {
        super(delegate);
    }

    @Override
    public String tableToIdentifierPropertyName(TableIdentifier tableIdentifier) {
        return "id";
    }

    @Override
    public String getTableIdentifierStrategyName(TableIdentifier tableIdentifier) {
        String configuredStrategy = super.getTableIdentifierStrategyName(tableIdentifier);
        return configuredStrategy == null ? "native" : configuredStrategy;
    }

    @Override
    public String foreignKeyToEntityName(String keyname,
                                         TableIdentifier fromTable,
                                         @SuppressWarnings("rawtypes") List fromColumnNames,
                                         TableIdentifier referencedTable,
                                         @SuppressWarnings("rawtypes") List referencedColumnNames,
                                         boolean uniqueReference) {
        String existingValue = super.foreignKeyToEntityName(keyname, fromTable, fromColumnNames, referencedTable, referencedColumnNames, uniqueReference);
        String defaultValue = defaultStrategy.foreignKeyToEntityName(keyname, fromTable, fromColumnNames, referencedTable, referencedColumnNames, uniqueReference);
        if (defaultValue.equals(existingValue)) {
            if (fromColumnNames.size() != 1) {
                return defaultValue;
            } else {
                String propertyName = Column.class.cast(fromColumnNames.get(0)).getName().toLowerCase();

                if (propertyName.endsWith("_id")) {
                    propertyName = propertyName.substring(0, propertyName.length() - 3);
                }

                if (propertyName.endsWith("id")) {
                    propertyName = propertyName.substring(0, propertyName.length() - 2);
                }
                while (propertyName.contains("_")) {
                    String target = propertyName.substring(propertyName.indexOf("_"), propertyName.indexOf("_") + 2);
                    String replacement = (propertyName.substring(propertyName.indexOf("_") + 1, propertyName.indexOf("_") + 2)).toUpperCase();
                    propertyName = propertyName.replaceAll(target, replacement);
                }

                StringBuilder buf = new StringBuilder(propertyName);
                buf.replace(0, 1, new String(new char[]{Character.toLowerCase(propertyName.charAt(0))}));
                return buf.toString();
            }
        } else {
            return existingValue;
        }
    }
}
