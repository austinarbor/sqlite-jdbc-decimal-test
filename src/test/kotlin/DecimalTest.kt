package dev.aga.sqlite

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement

class DecimalTest {
    @Test
    fun testDecimalAdd() {
        val (conn, stmt) = getConnectionAndStatement()
        conn.use { _ ->
            stmt.use { stmt ->
                val insert = """
                    INSERT INTO test(a,b) VALUES("1.234", "5.678");
                    """
                stmt.execute(insert);
                val add = """
                    SELECT decimal_add(a,b) as "add" FROM test;
                    """
                stmt.executeQuery(add).use { rs ->
                    verifyResultSet(rs, "add", "6.912")
                }
            }
        }
    }

    @Test
    fun testDecimalSub() {
        val (conn, stmt) = getConnectionAndStatement()
        conn.use { _ ->
            stmt.use { stmt ->
                val insert = """
                    INSERT INTO test(a,b) VALUES("1.234", "5.678");
                    """
                stmt.execute(insert);

                val sub = """
                    SELECT decimal_sub(a,b) as "sub" FROM test;
                """
                stmt.executeQuery(sub).use { rs ->
                    verifyResultSet(rs, "sub", "-4.444")
                }
            }
        }
    }

    @Test
    fun testDecimalMul() {
        val (conn, stmt) = getConnectionAndStatement()
        conn.use { _ ->
            stmt.use { stmt ->
                val insert = """
                    INSERT INTO test(a,b) VALUES("1.234", "5.678");
                    """
                stmt.execute(insert);

                val sub = """
                    SELECT decimal_mul(a,b) as "mul" FROM test;
                """
                stmt.executeQuery(sub).use { rs ->
                    verifyResultSet(rs, "mul", "7.006652")
                }
            }
        }
    }

    @Test
    fun testDecimalSum() {
        val (conn, stmt) = getConnectionAndStatement()
        conn.use { _ ->
            stmt.use { stmt ->
                val insert = """
                    INSERT INTO test(a,b) VALUES 
                    ("1.234", "5.678"), 
                    ("9.1011", "12.1314");
                    """
                stmt.execute(insert);

                val sub = """
                    SELECT decimal_sum(b) as "sum" FROM test;
                """
                stmt.executeQuery(sub).use { rs ->
                    verifyResultSet(rs, "sum", "17.8094")
                }
            }
        }
    }

    @Test
    fun testDecimalCmp() {
        val (conn, stmt) = getConnectionAndStatement()
        conn.use { _ ->
            stmt.use { stmt ->
                val insert = """
                    INSERT INTO test(a,b) VALUES ("1.234", "5.678");
                    """
                stmt.execute(insert);

                val sub = """
                    SELECT decimal_cmp(a,b) as "cmp" FROM test;
                """
                stmt.executeQuery(sub).use { rs ->
                    verifyResultSet(rs, "cmp", "-1")
                }
            }
        }
    }

    private fun verifyResultSet(rs: ResultSet, colName: String, expected: String) {
        assertThat(rs.next()).isTrue()
        assertThat(rs.getBigDecimal(colName))
            .isEqualByComparingTo(expected)
    }

    @Throws(SQLException::class)
    private fun getConnectionAndStatement(): Pair<Connection, Statement> {
        val conn = DriverManager.getConnection("jdbc:sqlite::memory:")
        val stmt = conn.createStatement()
        createTable(stmt)
        return conn to stmt
    }

    @Throws(SQLException::class)
    private fun createTable(stmt: Statement) {
        val create = """
            CREATE TABLE test(a TEXT, b TEXT);
            """
        stmt.execute(create)
    }
}
