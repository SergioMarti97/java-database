package org.simple.db.mysql.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.simple.db.mysql.query.QueryBuilder;

import static org.junit.jupiter.api.Assertions.*;

class QueryBuilderTest {

    @Test
    @DisplayName("Construir la query \"insert\" con \"id\" automático y sin campos")
    void queryBuilderInsertAutoIdNoFields() {
        String expected = "insert into `my_table`() values();";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id");
        assertEquals(expected, queryBuilder.buildQueryInsert());
    }

    @Test
    @DisplayName("Construir la query \"insert\" sin \"id\" automático y sin campos")
    void queryBuilderInsertNoAutoIdNoFields() {
        String expected = "insert into `my_table`(`id`) values(?);";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id");
        assertEquals(expected, queryBuilder.buildQueryInsert(false));
    }

    @Test
    @DisplayName("Construir la query \"insert\" con \"id\" automático y con un solo campo")
    void queryBuilderInsertAutoIdOneField() {
        String expected = "insert into `my_table`(`field_one`) values(?);";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id", "field_one");
        assertEquals(expected, queryBuilder.buildQueryInsert());
    }

    @Test
    @DisplayName("Construir la query \"insert\" sin \"id\" automático y con un solo campo")
    void queryBuilderInsertNoAutoIdOneField() {
        String expected = "insert into `my_table`(`id`, `field_one`) values(?, ?);";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id", "field_one");
        assertEquals(expected, queryBuilder.buildQueryInsert(false));
    }

    @Test
    @DisplayName("Construir la query \"insert\" con \"id\" automático y dos campos")
    void queryBuilderInsertAutoIdTwoFields() {
        String expected = "insert into `my_table`(`field_one`, `field_two`) values(?, ?);";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id", "field_one", "field_two");
        assertEquals(expected, queryBuilder.buildQueryInsert());
    }

    @Test
    @DisplayName("Construir la query \"insert\" sin \"id\" automático y dos campos")
    void queryBuilderInsertNoAutoIdTwoFields() {
        String expected = "insert into `my_table`(`id`, `field_one`, `field_two`) values(?, ?, ?);";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id", "field_one", "field_two");
        assertEquals(expected, queryBuilder.buildQueryInsert(false));
    }

    @Test
    @DisplayName("Construir la query \"select\" en base al \"id\"")
    void queryBuilderSelectById() {
        String expected = "select * from `my_table` where `id` = %s;";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id");
        assertEquals(expected, queryBuilder.buildQuerySelectById());
    }

    @Test
    @DisplayName("Construir la query \"select\" en base a un campo")
    void queryBuilderSelectByField() {
        String expected = "select * from `my_table` where `field_one` = %s;";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id", "field_one", "field_two");
        assertEquals(expected, queryBuilder.buildQuerySelectByField(0));
    }

    @Test
    @DisplayName("Construir la query \"select\" para todos los registros")
    void queryBuilderSelectAll() {
        String expected = "select * from `my_table`;";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id");
        assertEquals(expected, queryBuilder.buildQuerySelectAll());
    }

    @Test
    @DisplayName("Construir la query \"delete\" para eliminar un registro en base al id")
    void queryBuilderDeleteById() {
        String expected = "delete from `my_table` where `id` = ?;";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id");
        assertEquals(expected, queryBuilder.buildQueryDeleteById());
    }

    @Test
    @DisplayName("Construir la query \"delete\" para eliminar un registro en base a un campo")
    void queryBuilderDeleteByField() {
        String expected = "delete from `my_table` where `field_one` = ?;";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id", "field_one", "field_two");
        assertEquals(expected, queryBuilder.buildQueryDeleteByField(0));
    }

    @Test
    @DisplayName("Construir la query \"update\" para actualizar un registro en base al id con un campo")
    void queryBuilderUpdateByIdOneField() {
        String expected = "update `my_table` set `field_one` = ? where `id` = ?;";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id", "field_one");
        assertEquals(expected, queryBuilder.buildQueryUpdateById());
    }

    @Test
    @DisplayName("Construir la query \"update\" para actualizar un registro en base al id con dos campos")
    void queryBuilderUpdateByIdTwoField() {
        String expected = "update `my_table` set `field_one` = ?, `field_two` = ? where `id` = ?;";
        QueryBuilder queryBuilder = new QueryBuilder("my_table", "id", "field_one", "field_two");
        assertEquals(expected, queryBuilder.buildQueryUpdateById());
    }

    @Test
    @DisplayName("Construir la query \"insert\" filtrando los campos")
    void queryBuilderInsertFiltered() throws Exception {
        String expected = "insert into `my_table`(`table_id`, `field_two`, `field_three`) values(?, ?, ?);";
        String entityName = "my_table";
        String entityId = "table_id";
        String[] fields = {
                "field_one",
                "field_two",
                "field_three",
                "field_four",
                "field_five"
        };
        boolean[] include = {
                false,
                true,
                true,
                false,
                false
        };
        QueryBuilder queryBuilder = new QueryBuilder(entityName, entityId, fields);
        assertEquals(expected, queryBuilder.buildQueryInsert(false, include));
    }

    @Test
    @DisplayName("Construir la query \"update\" en base al \"id\" filtrando los campos")
    void queryBuilderUpdateByIdFiltered() throws Exception {
        String expected = "update `my_table` set `field_two` = ?, `field_three` = ? where `table_id` = ?;";
        String entityName = "my_table";
        String entityId = "table_id";
        String[] fields = {
                "field_one",
                "field_two",
                "field_three",
                "field_four",
                "field_five"
        };
        boolean[] include = {
                false,
                true,
                true,
                false,
                false
        };
        QueryBuilder queryBuilder = new QueryBuilder(entityName, entityId, fields);
        assertEquals(expected, queryBuilder.buildQueryUpdateById(include));
    }


    @Test
    @DisplayName("Construir la query \"update\" en base al a un campo, filtrando algunos campos")
    void queryBuilderUpdateByFieldFiltered() throws Exception {
        String expected = "update `my_table` set `field_two` = ?, `field_three` = ? where `field_five` = ?;";
        String entityName = "my_table";
        String entityId = "table_id";
        String[] fields = {
                "field_one",
                "field_two",
                "field_three",
                "field_four",
                "field_five"
        };
        boolean[] include = {
                false,
                true,
                true,
                false,
                false
        };
        QueryBuilder queryBuilder = new QueryBuilder(entityName, entityId, fields);
        assertEquals(expected, queryBuilder.buildQueryUpdateByField(4, include));
    }

}