package com.falkordb.exceptions;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.falkordb.FalkorDB;
import com.falkordb.GraphContext;
import com.falkordb.GraphContextGenerator;

public class GraphErrorTest {

    private GraphContextGenerator api;

    @Before
    public void createApi() {
        api = FalkorDB.driver().graph("social");
        Assert.assertNotNull(api.query("CREATE (:person{mixed_prop: 'strval'}), (:person{mixed_prop: 50})"));
    }

    @After
    public void deleteGraph() throws Exception{

        api.deleteGraph();
        api.close();
    }

    @Test
    public void testSyntaxErrorReporting() {
        GraphException exception = assertThrows(GraphException.class,
                () -> api.query("RETURN toUpper(5)"));
        assertTrue(exception.getMessage().contains("Type mismatch: expected String or Null but was Integer"));
    }

    @Test
    public void testRuntimeErrorReporting() {
        GraphException exception = assertThrows(GraphException.class,
                () -> api.query("MATCH (p:person) RETURN toUpper(p.mixed_prop)"));
        assertTrue(exception.getMessage().contains("Type mismatch: expected String or Null but was Integer"));
    }

    @Test
    public void testExceptionFlow() {

        try {
            // Issue a query that causes a compile-time error
            api.query("RETURN toUpper(5)");
        } catch (Exception e) {
            Assert.assertEquals(GraphException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("Type mismatch: expected String or Null but was Integer"));
        }

        // On general api usage, user should get a new connection

        try {
            // Issue a query that causes a compile-time error
            api.query("MATCH (p:person) RETURN toUpper(p.mixed_prop)");
        } catch (Exception e) {
            Assert.assertEquals(GraphException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("Type mismatch: expected String or Null but was Integer"));
        }
    }

    @Test
    public void testContextSyntaxErrorReporting() {
        GraphContext c = api.getContext();

        GraphException exception = assertThrows(GraphException.class,
                () -> c.query("RETURN toUpper(5)"));
        assertTrue(exception.getMessage().contains("Type mismatch: expected String or Null but was Integer"));
    }

    @Test
    public void testMissingParametersSyntaxErrorReporting() {
        GraphException exception = assertThrows(GraphException.class,
                () -> api.query("RETURN $param"));
        assertTrue(exception.getMessage().contains("Missing parameters"));
    }

    @Test
    public void testMissingParametersSyntaxErrorReporting2() {
        GraphException exception = assertThrows(GraphException.class,
                () -> api.query("RETURN $param", new HashMap<>()));
        assertTrue(exception.getMessage().contains("Missing parameters"));
    }

    @Test
    public void testContextRuntimeErrorReporting() {
        GraphContext c = api.getContext();

        GraphException exception = assertThrows(GraphException.class,
                () -> c.query("MATCH (p:person) RETURN toUpper(p.mixed_prop)"));
        assertTrue(exception.getMessage().contains("Type mismatch: expected String or Null but was Integer"));
    }

    @Test
    public void testContextExceptionFlow() {

        GraphContext c = api.getContext();
        try {
            // Issue a query that causes a compile-time error
            c.query("RETURN toUpper(5)");
        } catch (Exception e) {
            Assert.assertEquals(GraphException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("Type mismatch: expected String or Null but was Integer"));
        }

        // On contexted api usage, connection should stay open
        try {
            // Issue a query that causes a compile-time error
            c.query("MATCH (p:person) RETURN toUpper(p.mixed_prop)");
        } catch (Exception e) {
            Assert.assertEquals(GraphException.class, e.getClass());
            Assert.assertTrue(e.getMessage().contains("Type mismatch: expected String or Null but was Integer"));
        }
    }

    @Test
    public void timeoutException() {
        GraphException exception = assertThrows(GraphException.class,
                () -> api.query("UNWIND range(0,100000) AS x WITH x AS x WHERE x = 10000 RETURN x", 1L));
        assertTrue(exception.getMessage().contains("Query timed out"));
    }
}
