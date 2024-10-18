package com.falkordb;

import java.io.Closeable;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.Pool;

/**
 * An interface which aligned to FalkorDB Driver interface
 */
public interface Driver extends Closeable {

    /**
     * Returns a selected Graph
     * 
     * @param graphId Graph name
     * @return a selected Graph
     */
    GraphContextGenerator graph(String graphId);

    /**
     * Returns a underline connection to the database
     * 
     * @return a underline connection to the database
     */
    Jedis getConnection();

    Pool<Jedis> getPool();
}
