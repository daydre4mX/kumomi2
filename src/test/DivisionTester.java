package test;

import java.sql.Connection;
import org.junit.jupiter.api.Test;

import main.java.databaseinteraction.DatabaseInteractor;
import main.java.types.Division;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Zerubbabel Ashenafi
 */
public class DivisionTester {

    Connection database;
    Division div;

    public DivisionTester() {
        database = DatabaseInteractor.getDatabaseConnection();
        div = new Division();
    }

    @Test
    void testGetDivisionFromID() {
        div.setId(1);
        div.setDivisionFromID(database);

        assertEquals("Engineering", div.getName());

        div.setId(2);
        div.setDivisionFromID(database);

        assertEquals("HR", div.getName());

        div.setId(500);
        div.setDivisionFromID(database);

        assertEquals("", div.getName());
    }
}