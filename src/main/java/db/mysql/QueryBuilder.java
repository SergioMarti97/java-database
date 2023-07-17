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

    public String buildQueryInsert(boolean idAuto) {
        StringBuilder out = new StringBuilder();
        out.append("insert into `").append(entityName).append("`(");
        if (!idAuto) {
            out.append('`').append(idField).append('`');
            if (!fields.isEmpty()) {
                out.append(", ");
            }
        }
        if (!fields.isEmpty()) {
            for (int i = 0; i < fields.size() - 1; i++) {
                var field = fields.get(i);
                out.append('`').append(field).append("`, ");
            }
            out.append('`').append(fields.get(fields.size() - 1)).append('`');
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
        out.append('`').append(entityName).append("` ");
        out.append("set ");
        if (!fields.isEmpty()) {
            out.append('`').append(fields.get(0)).append("` = ?");
            for (int i = 1; i < fields.size(); i++) {
                String field = fields.get(i);
                out.append(", `").append(field).append("` = ?");
            }
        }
        out.append(" where ");
        out.append('`').append(idField).append("` ");
        out.append("= ?;");
        return out.toString();
    }

    public String buildQueryUpdateByField(int idxField) {
        StringBuilder out = new StringBuilder();
        out.append("update ");
        out.append('`').append(entityName).append("` ");
        out.append("set ");
        if (!fields.isEmpty()) {
            out.append('`').append(fields.get(0)).append("` = ?");
            for (int i = 1; i < fields.size(); i++) {
                String field = fields.get(i);
                out.append(", `").append(field).append("` = ?");
            }
        }
        out.append(" where ");
        out.append('`').append(fields.get(idxField)).append("` ");
        out.append("= ?;");
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

    // Main

    public static void main(String[] args) {
        String entityName = "my_table";
        String idField = "id_of_my_table";
        String[] fields = new String[] {
                "field_one",
                "field_two",
                "field_three"
        };
        QueryBuilder queryBuilder = new QueryBuilder(entityName, idField, fields);
        System.out.println(queryBuilder.buildQueryInsert(false));
    }

}
