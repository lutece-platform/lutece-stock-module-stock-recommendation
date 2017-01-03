/*
 * Copyright (c) 2002-2015, Mairie de Paris
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * AvailableProductsDAO
 */
public class AvailableProductsDAO
{

    private static final String SQL_QUERY_SELECTALL = "SELECT a.id_product FROM stock_product a, stock_product_attribute_date b, stock_product_attribute_date c "
            + " WHERE a.id_product = b.owner_id AND b.attribute_key = 'start' AND a.id_product = c.owner_id "
            + " AND c.attribute_key = 'end' AND b.attribute_value < ? AND c.attribute_value > ?";
    
    private static final String SQL_QUERY_PRODUCT = "SELECT name FROM stock_product WHERE id_product = ?";

    public List<Integer> selectUserItemsList( Timestamp time, Plugin plugin )
    {
        List<Integer> list = new ArrayList<Integer>();
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.setTimestamp( 1, time );
        daoUtil.setTimestamp( 2, time );
        daoUtil.executeQuery();

        while( daoUtil.next() )
        {
            list.add( daoUtil.getInt( 1 ) );
        }

        daoUtil.free();

        return list;
    }

    public void getProductInfos( RecommendedProduct rp , Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_PRODUCT, plugin );
        daoUtil.setInt( 1, rp.getProductId() );
        daoUtil.executeQuery();

        if( daoUtil.next() )
        {
            rp.setProductName( daoUtil.getString( 1 ) );
        }

        daoUtil.free();
    }
}
