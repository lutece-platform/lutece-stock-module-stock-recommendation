/*
 * Copyright (c) 2002-2017, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */

package fr.paris.lutece.plugins.stock.modules.recommendation.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * This class provides Data Access methods for Recommendation objects
 */

public final class RecommendationDAO
{

    // Constants
    private static final String SQL_QUERY_INSERT = "INSERT INTO stock_recommendation ( username, id_product, score ) VALUES ( ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM stock_recommendation WHERE username = ? ";
    private static final String SQL_QUERY_SELECTALL = "SELECT username, id_product, score FROM stock_recommendation WHERE username = ?";

    /**
     * Insert a new record in the table.
     * 
     * @param recommendation
     *            instance of the Recommendation object to insert
     * @param plugin
     *            The plugin
     */

    public void insert( Recommendation recommendation, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        daoUtil.setString( 1, recommendation.getUsername( ) );
        daoUtil.setInt( 2, recommendation.getIdProduct( ) );
        daoUtil.setFloat( 3, recommendation.getScore( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Delete a record from the table
     * 
     * @param strUsername
     *            The username
     * @param plugin
     *            The plugin
     */

    public void deleteByUser( String strUsername, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setString( 1, strUsername );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * Load the data of all the recommendations and returns them as a List
     * 
     * @param strUsername The username
     * @param plugin
     *            The plugin
     * @return The List which contains the data of all the recommendations
     */

    public List<Recommendation> selectRecommendationsByUser( String strUsername, Plugin plugin )
    {
        List<Recommendation> recommendationList = new ArrayList<Recommendation>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.setString( 1, strUsername );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            Recommendation recommendation = new Recommendation( );

            recommendation.setUsername( daoUtil.getString( 1 ) );
            recommendation.setIdProduct( daoUtil.getInt( 2 ) );
            recommendation.setScore( daoUtil.getFloat( 3 ) );

            recommendationList.add( recommendation );
        }

        daoUtil.free( );
        return recommendationList;
    }

}
