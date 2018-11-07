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
package fr.paris.lutece.plugins.stock.modules.recommendation.service;

import fr.paris.lutece.plugins.stock.modules.recommendation.service.StockRecommendationService;
import fr.paris.lutece.test.LuteceTestCase;
import java.util.List;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.junit.Test;

/**
 * RecommendationServiceTest
 */
public class RecommendationServiceTest extends LuteceTestCase
{
    private static final String USER1 = "BB46625ACAFA4E8E8DB4462C521B715800000000";
    private static final String USER2 = "BB46625ACAFA4E8E8DB4462C521B715800000000";

    /**
     * Test of extractPurchases method, of class StockRecommendationService.
     */
    @Test
    public void testExtractPurchases( )
    {
        System.out.println( "extractPurchases" );
        StockRecommendationService instance = StockRecommendationService.instance( );
        instance.extractPurchases( );
    }

    @Test
    public void testGetRecommendedItems( ) throws NoSuchUserException, TasteException
    {
        System.out.println( "getRecommendedItems" );
        StockRecommendationService instance = StockRecommendationService.instance( );
        getRecommendation( instance, USER1 );
        getRecommendation( instance, USER2 );

    }

    private void getRecommendation( StockRecommendationService instance, String strUsername ) throws NoSuchUserException, TasteException
    {
        List<RecommendedItem> list = instance.getRecommendedItems( strUsername );
        System.out.println( "Recommendations for user " + strUsername );
        for ( RecommendedItem item : list )
        {
            System.out.println( "Recommended item " + item.getItemID( ) + " score : " + item.getValue( ) );
        }
    }
}
