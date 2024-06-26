package br.com.deyvidfernandes.verbDataFetcher.database.queries;

import br.com.deyvidfernandes.verbDataFetcher.utils.Numbers;

import java.lang.reflect.Field;

import java.util.ArrayList;

public class InsertQuery<Model> implements IQuery {

    private final String table;
    private final String columnList;
    private final Field[] modelFields;
    private final ArrayList<Model> valueList = new ArrayList<>();

    public InsertQuery(String columnList, String table, Model smapleModel) {
        this.columnList = columnList;
        this.table = table;
        this.modelFields = smapleModel.getClass().getFields();
    }

    public void addValue(Model model) {
        this.valueList.add(model);
    }

    private String generateValueRow(Model model, String stringSymbol) {
        StringBuilder valueRow = new StringBuilder("(");
        for(Field field : modelFields) {
            try {
                Object value = field.get(model);
                if (value instanceof Number numberValue) {
                    valueRow.append(Numbers.correctlyDisplayDouble(numberValue));
                    valueRow.append(", ");
                } else {
                    if (value != null) {
                        valueRow.append(stringSymbol);
                        valueRow.append(value.toString());
                        valueRow.append(stringSymbol).append(", ");

                    } else {
                        valueRow.append("NULL, ");
                    }
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        return valueRow.substring(0, valueRow.length() - 2).concat(")");
    }

    private String generate(String stringSymbol) {
        StringBuilder query = new StringBuilder("INSERT INTO " + table + "\n");
        query.append(columnList + "\n");
        query.append("VALUES \n");
        for (Model model : valueList) {
            query.append(generateValueRow(model, stringSymbol)).append(", \n");
        }
        return query.substring(0, query.length() - 3);
    }

    public String MYSQL() {
        return generate("\"");
    }
    public String MARIADB() {
        return generate("\"");
    }
    public String POSTGRESQL() {
        return generate("'");
    }

}
