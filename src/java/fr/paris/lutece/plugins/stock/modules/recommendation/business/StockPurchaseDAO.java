/*
 * Copyright (c) 2002-2018, Mairie de Paris
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
 * StockPurchaseDAO
 */
public class StockPurchaseDAO
{
    private static final String SQL_QUERY_SELECTALL = "SELECT a.username , b.product_id FROM stock_purchase a, stock_offer b WHERE a.offer_id = b.id_offer";
    private static final String SQL_QUERY_SELECT_USERS = "SELECT DISTINCT username FROM stock_purchase";

    /**
     * Read all purchase data
     * 
     * @param plugin
     *            The plugin
     * @return The list of purchase data
     */
    public List<UserItem> selectUserItemsList( Plugin plugin )
    {
        List<UserItem> list = new ArrayList<UserItem>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            UserItem userItem = new UserItem( );

            userItem.setUserName( daoUtil.getString( 1 ) );
            userItem.setItemId( daoUtil.getInt( 2 ) );

            list.add( userItem );
        }

        daoUtil.free( );

        return list;
    }

    /**
     * Select all users that purchase an item
     * 
     * @param plugin
     *            The plugin
     * @return The list of users
     */
    public List<String> selectUsersList( Plugin plugin )
    {
        List<String> list = new ArrayList<String>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_USERS, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            list.add( daoUtil.getString( 1 ) );
        }

        daoUtil.free( );

        return list;
    }
}
