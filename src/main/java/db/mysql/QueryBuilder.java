package db.mysql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Esta clase contiene los métodos necesarios para generar las queries a lanzar
 * Construye las queries en base al nombre de la entidad y sus campos
 */
public class QueryBuilder {

    private String entityName;

    private String idField;

    private List<String> fields;

    // Constructors

    public QueryBuilder(String entityName, String idField, String... fields) {
        this.entityName = entityName;
        this.idField = idField;
        this.fields = new ArrayList<>(Arrays.asList(fields));
    }

    public QueryBuilder(String entityName, String idField) {
        this.entityName = entityName;
        this.idField = idField;
        this.fields = new ArrayList<>();
    }

    public QueryBuilder(String entityName, String idField, List<String> fields) {
        this.entityName = entityName;
        this.idField = idField;
        this.fields = new ArrayList<>(fields);
    }

    // Private methods (useful utilities)

    /**
     * Concatena el nombre del campo que se indica por parámetro en el constructor de cadenas, añadiendo acentos
     * @param out el constructor de cadenas donde se va a concatenar el campo con acentos
     * @param field el campo a concatenar
     * @return la misma instancia del constructor de cadenas pasado por parámetro
     */
    private StringBuilder addFieldWithAccents(StringBuilder out, String field) {
        out.append('`').append(field).append('`');
        return out;
    }

    /**
     * Genera una nueva lista filtrando la lista de nombres de campos pasado como primer parámetro, en base al array
     * de booleanos pasado como segundo parámetro
     * @param fieldsNames lista con los nombres de los campos
     * @param fieldsBool array de booleanos para filtrar los nombres de los campos
     * @return lista filtrada
     */
    private List<String> filterFields(List<String> fieldsNames, boolean... fieldsBool) {
        // Calcular la nueva lista campos
        List<String> l = new ArrayList<>();
        for (int i = 0; i < fieldsNames.size(); i++) {
            if (fieldsBool[i]) {
                l.add(fieldsNames.get(i));
            }
        }
        return l;
    }

    // Methods

    public String buildQueryInsert(boolean idAuto) {
        StringBuilder out = new StringBuilder();

        out.append("insert into ");
        addFieldWithAccents(out, entityName).append('(');

        if (!idAuto) {
            addFieldWithAccents(out, idField);
            if (!fields.isEmpty()) {
                out.append(", ");
            }
        }
        if (!fields.isEmpty()) {
            for (int i = 0; i < fields.size() - 1; i++) {
                addFieldWithAccents(out, fields.get(i)).append(", ");
            }
            int lastIndex = fields.size() - 1;
            addFieldWithAccents(out, fields.get(lastIndex));
        }
        out.append(") values(");
        if (!fields.isEmpty() || !idAuto) {
            out.append("?");
        }
        out.append(", ?".repeat(idAuto ? Math.max(0, fields.size() - 1) : fields.size()));
        out.append(");");
        return out.toString();
    }

    public String buildQueryInsert() {
        return buildQueryInsert(true);
    }

    public String buildQueryInsert(boolean idAuto, boolean... fields) {
        if (this.fields.size() != fields.length) {
            return null;
        }

        StringBuilder out = new StringBuilder();

        out.append("insert into ");
        addFieldWithAccents(out, entityName).append('(');

        List<String> filteredFields = filterFields(this.fields, fields);

        if (!idAuto) {
            addFieldWithAccents(out, idField);
            if (!filteredFields.isEmpty()) {
                out.append(", ");
            }
        }

        if (!filteredFields.isEmpty()) {
            for (int i = 0; i < filteredFields.size() - 1; i++) {
                addFieldWithAccents(out, filteredFields.get(i)).append(", ");
            }
            int lastIndex = filteredFields.size() - 1;
            addFieldWithAccents(out, filteredFields.get(lastIndex));
        }

        out.append(") values(");
        if (!filteredFields.isEmpty() || !idAuto) {
            out.append("?");
        }
        out.append(", ?".repeat(idAuto ? Math.max(0, filteredFields.size() - 1) : filteredFields.size()));
        out.append(");");

        return out.toString();
    }

    public String buildQueryInsert(boolean... fields) {
        return buildQueryInsert(true, fields);
    }

    public String buildQuerySelectAll() {
        return "select * from " +
                "`" + entityName + "`;";
    }

    public String buildQuerySelectById() {
        return "select * from " +
                '`' + entityName + "` " +
                "where `" + idField + "` = %s;";
    }

    public String buildQuerySelectByField(int idxField) {
        return "select * from " +
                '`' + entityName + "` " +
                "where `" + fields.get(idxField) + "` = %s;";
    }

    public String buildQueryDeleteById() {
        return "delete from " +
                '`' + entityName + "` " +
                "where " +
                '`' + idField + "` = ?;";
    }

    public String buildQueryDeleteByField(int idxField) {
        return "delete from " +
                '`' + entityName + "` " +
                "where " +
                '`' + fields.get(idxField) + "` = ?;";
    }

    public String buildQueryUpdateById() {
        StringBuilder out = new StringBuilder();

        out.append("update ");
        addFieldWithAccents(out, entityName).append(' ');
        out.append("set ");

        if (!fields.isEmpty()) {
            out.append('`').append(fields.get(0)).append("` = ?");
            for (int i = 1; i < fields.size(); i++) {
                String field = fields.get(i);
                out.append(", ");
                addFieldWithAccents(out, field);
                out.append(" = ?");
            }
        }

        out.append(" where ");
        addFieldWithAccents(out, idField);
        out.append(" = ?;");
        return out.toString();
    }

    public String buildQueryUpdateByField(int fieldIndex) {
        StringBuilder out = new StringBuilder();

        out.append("update ");
        addFieldWithAccents(out, entityName).append(' ');
        out.append("set ");

        if (!fields.isEmpty()) {
            out.append('`').append(fields.get(0)).append("` = ?");
            for (int i = 1; i < fields.size(); i++) {
                String field = fields.get(i);
                out.append(", ");
                addFieldWithAccents(out, field);
                out.append(" = ?");
            }
        }
        out.append(" where ");
        addFieldWithAccents(out, fields.get(fieldIndex));
        out.append(" = ?;");
        return out.toString();
    }

    public String buildQueryUpdateById(boolean... fields) {
        if (this.fields.size() != fields.length) {
            return null;
        }

        StringBuilder out = new StringBuilder();

        out.append("update ");
        addFieldWithAccents(out, entityName).append(' ');
        out.append("set ");

        List<String> filteredFields = filterFields(this.fields, fields);

        if (!filteredFields.isEmpty()) {
            out.append('`').append(filteredFields.get(0)).append("` = ?");
            for (int i = 1; i < filteredFields.size(); i++) {
                String field = filteredFields.get(i);
                out.append(", ");
                addFieldWithAccents(out, field);
                out.append(" = ?");
            }
        }
        out.append(" where ");
        addFieldWithAccents(out, idField);
        out.append(" = ?;");
        return out.toString();
    }

    public String buildQueryUpdateByField(int fieldIndex, boolean... fields) {
        if (this.fields.size() != fields.length) {
            return null;
        }

        StringBuilder out = new StringBuilder();

        out.append("update ");
        addFieldWithAccents(out, entityName).append(' ');
        out.append("set ");

        List<String> filteredFields = filterFields(this.fields, fields);

        if (!filteredFields.isEmpty()) {
            out.append('`').append(filteredFields.get(0)).append("` = ?");
            for (int i = 1; i < filteredFields.size(); i++) {
                String field = filteredFields.get(i);
                out.append(", ");
                addFieldWithAccents(out, field);
                out.append(" = ?");
            }
        }
        out.append(" where ");
        addFieldWithAccents(out, this.fields.get(fieldIndex));
        out.append(" = ?;");
        return out.toString();
    }

    // Getters & Setters

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getIdField() {
        return idField;
    }

    public void setIdField(String idField) {
        this.idField = idField;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

}
