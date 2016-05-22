package org.bitbucket.zenith4183.zclans.threads;

import java.sql.Connection;
import java.sql.SQLException;
import org.bitbucket.zenith4183.zclans.zClans;

/**
 *
 * @author NeT32
 */
public class ThreadUpdateSQL extends Thread {

    Connection Connection;
    String Query;
    String TypeSQL;

    public ThreadUpdateSQL(Connection Connection, String Query, String TypeSQL)
    {
        this.Query = Query;
        this.Connection = Connection;
        this.TypeSQL = TypeSQL;
    }

    @Override
    public void run()
    {
        try
        {
            this.Connection.createStatement().executeUpdate(this.Query);
        }
        catch (SQLException ex)
        {
            if (!ex.toString().contains("not return ResultSet"))
            {
                zClans.getLog().severe("[Thread] Error at SQL " + this.TypeSQL + " Query: " + ex);
                zClans.getLog().severe("[Thread] Query: " + this.Query);
            }
        }
    }
}
