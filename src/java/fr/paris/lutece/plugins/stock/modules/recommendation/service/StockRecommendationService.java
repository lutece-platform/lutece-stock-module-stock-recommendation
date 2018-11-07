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

import fr.paris.lutece.plugins.stock.modules.recommendation.business.AvailableProductsDAO;
import fr.paris.lutece.plugins.stock.modules.recommendation.business.Recommendation;
import fr.paris.lutece.plugins.stock.modules.recommendation.business.RecommendationDAO;
import fr.paris.lutece.plugins.stock.modules.recommendation.business.RecommendedProduct;
import fr.paris.lutece.plugins.stock.modules.recommendation.business.StockPurchaseDAO;
import fr.paris.lutece.plugins.stock.modules.recommendation.business.UserItem;
import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.mahout.cf.taste.common.NoSuchUserException;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.model.file.FileIDMigrator;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericBooleanPrefUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

/**
 * StockRecommendationService
 */
public final class StockRecommendationService
{
    private static final String PLUGIN_NAME = "stock-recommendation";
    private static final String PROPERTY_ID_MIGRATOR_FILE_PATH = "stock-recommendation.idMigratorFilePath";
    private static final String PROPERTY_DATA_FILE_PATH = "stock-recommendation.dataFilePath";
    private static final String PROPERTY_THRESHOLD = "stock-recommendation.recommender.threshold";
    private static final String PROPERTY_COUNT = "stock-recommendation.recommender.count";
    private static final String DEFAULT_THRESHOLD = "0.1";
    private static final int DEFAULT_COUNT = 6;

    private static Plugin _plugin = PluginService.getPlugin( PLUGIN_NAME );
    private static StockPurchaseDAO _daoPurchase = new StockPurchaseDAO( );
    private static RecommendationDAO _daoRecommendation = new RecommendationDAO( );
    private static AvailableProductsDAO _daoProducts = new AvailableProductsDAO( );
    private static FileIDMigrator _migrator;
    private static StockRecommendationService _singleton;
    private static PurchaseDataWriter _writer;
    private static UserBasedRecommender _recommender;
    private static int _nCount = AppPropertiesService.getPropertyInt( PROPERTY_COUNT, DEFAULT_COUNT );
    private static List<Integer> _listAvailableProducts;

    /** private constructor */
    private StockRecommendationService( )
    {
    }

    /**
     * Return the unique instance
     * 
     * @return The unique instance
     */
    public static StockRecommendationService instance( )
    {
        if ( _singleton == null )
        {
            synchronized( StockRecommendationService.class )
            {
                _singleton = new StockRecommendationService( );
                String strIdMigratorFilePath = AppPropertiesService.getProperty( PROPERTY_ID_MIGRATOR_FILE_PATH );
                File idMigratorFile = new File( AppPathService.getAbsolutePathFromRelativePath( strIdMigratorFilePath ) );
                String strDataFilePath = AppPropertiesService.getProperty( PROPERTY_DATA_FILE_PATH );
                File dataFile = new File( AppPathService.getAbsolutePathFromRelativePath( strDataFilePath ) );
                try
                {
                    _migrator = new FileIDMigrator( idMigratorFile );

                    _listAvailableProducts = buildAvailableProductsList( );
                    AppLogService.info( "stock-recommendation : " + _listAvailableProducts.size( ) + " products found that can be ordered." );
                    AppLogService.info( "stock-recommendation : creating data file with current purchases." );
                    _writer = new FilePurchaseDataWriter( dataFile );
                    extractPurchases( );
                    AppLogService.info( "stock-recommendation : initialize the recommender with data." );
                    _recommender = createRecommender( dataFile );
                }
                catch( FileNotFoundException ex )
                {
                    AppLogService.error( "stock-recommendation : Error creating file " + strIdMigratorFilePath + " " + ex.getMessage( ), ex );
                }
                catch( IOException ex )
                {
                    AppLogService.error( "stock-recommendation : Error creating file " + strIdMigratorFilePath + " " + ex.getMessage( ), ex );
                }
            }
        }
        return _singleton;
    }

    /**
     * Extract purchase from the database and write data using the selected DataWriter implementation
     */
    public static void extractPurchases( )
    {
        _writer.reset( );
        List<UserItem> list = _daoPurchase.selectUserItemsList( _plugin );
        for ( UserItem ui : list )
        {
            long lUserID = _migrator.toLongID( ui.getUserName( ) );
            _writer.write( lUserID, ui.getItemId( ) );

        }
        AppLogService.info( "stock-recommendation : retieved purchases count : " + list.size( ) );
        _writer.close( );
    }

    /**
     * Get the list of recommended items for a given user
     * 
     * @param strUserName
     *            The User ID
     * @return The list
     * @throws NoSuchUserException
     *             if the user is not found
     * @throws TasteException
     *             Other problem
     */
    public List<RecommendedItem> getRecommendedItems( String strUserName ) throws NoSuchUserException, TasteException
    {
        long lUserId = _migrator.toLongID( strUserName );
        return _recommender.recommend( lUserId, _nCount );
    }

    /**
     * Get the list of recommended products for a given user
     * 
     * @param strUserName
     *            The User ID
     * @return The list
     * @throws NoSuchUserException
     *             if the user is not found
     * @throws TasteException
     *             Other problem
     */
    public List<RecommendedProduct> getRecommendedProducts( String strUserName ) throws NoSuchUserException, TasteException
    {
        // Search in database
        List<RecommendedProduct> list = loadFromDatabase( strUserName );

        if ( list != null )
        {
            return list;
        }

        // build the list
        list = getRecommendedProductsList( strUserName );

        // save data in database
        writeUserRecommendations( strUserName, list );

        return list;

    }

    /**
     * Get the recommended products list
     * 
     * @param strUserName
     *            The username
     * @return The list
     * @throws NoSuchUserException
     *             if the user is not found
     * @throws TasteException
     *             if an error occurs
     */
    private List<RecommendedProduct> getRecommendedProductsList( String strUserName ) throws NoSuchUserException, TasteException
    {
        List<RecommendedProduct> list = new ArrayList<>( );

        for ( RecommendedItem item : getRecommendedItems( strUserName ) )
        {
            int nItemId = (int) item.getItemID( );
            if ( _listAvailableProducts.contains( nItemId ) )
            {
                RecommendedProduct product = new RecommendedProduct( );
                product.setProductId( nItemId );
                product.setScore( item.getValue( ) );
                _daoProducts.getProductInfos( product, _plugin );
                list.add( product );
                AppLogService.info( "Product recommended for user " + strUserName + " : " + product );
            }
        }
        return list;
    }

    /**
     * Load recommended products from the database for a given user
     * 
     * @param strUsername
     *            The user
     * @return The list
     */
    private List<RecommendedProduct> loadFromDatabase( String strUsername )
    {
        List<Recommendation> listRecommandations = _daoRecommendation.selectRecommendationsByUser( strUsername, _plugin );

        if ( listRecommandations == null || listRecommandations.isEmpty( ) )
        {
            return null;
        }

        List<RecommendedProduct> list = new ArrayList<>( );
        for ( Recommendation recommendation : listRecommandations )
        {
            RecommendedProduct product = new RecommendedProduct( );
            product.setProductId( recommendation.getIdProduct( ) );
            product.setScore( recommendation.getScore( ) );
            _daoProducts.getProductInfos( product, _plugin );
            list.add( product );
        }
        return list;

    }

    /**
     * Create a recommender
     * 
     * @param fileData
     *            The data file
     * @return The recommender
     * @throws IOException
     *             if a problem occurs with the data file
     */
    private static UserBasedRecommender createRecommender( File fileData ) throws IOException
    {
        DataModel model = new FileDataModel( fileData );
        UserSimilarity similarity = new LogLikelihoodSimilarity( model );

        String strThreshold = AppPropertiesService.getProperty( PROPERTY_THRESHOLD, DEFAULT_THRESHOLD );
        double threshold = Double.valueOf( strThreshold );
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood( threshold, similarity, model );
        return new GenericBooleanPrefUserBasedRecommender( model, neighborhood, similarity );

    }

    /**
     * Build the available productlist
     * 
     * @return The list of IDs
     */
    private static List<Integer> buildAvailableProductsList( )
    {
        Timestamp time = new Timestamp( ( new Date( ) ).getTime( ) );
        List<Integer> listAvailableProducts = _daoProducts.selectAvailableProductsIdList( time, _plugin );
        return listAvailableProducts;
    }

    /**
     * build recommendations (used by the daemon)
     * 
     * @param sbLogs
     *            The daemons logs
     */
    public void buildRecommendations( StringBuilder sbLogs )
    {
        List<String> listUsers = _daoPurchase.selectUsersList( _plugin );
        int nUsersCount = listUsers.size( );
        int nRecommendationCount = 0;
        AppLogService.info( "Starting building recommendations for " + nUsersCount + " users ..." );
        for ( String strUsername : listUsers )
        {
            List<RecommendedProduct> listProduct;
            try
            {
                listProduct = getRecommendedProductsList( strUsername );
                nRecommendationCount += writeUserRecommendations( strUsername, listProduct );
            }
            catch( TasteException ex )
            {
                AppLogService.error( "stock-recommendation : Error building recommendations : " + ex.getMessage( ), ex );
            }
        }

        double ratio = (double) nRecommendationCount / (double) nUsersCount;
        sbLogs.append( "Recommendation builder\n " ).append( "number of users : " ).append( nUsersCount ).append( '\n' )
                .append( "number of recommendations : " ).append( nRecommendationCount ).append( '\n' ).append( "ratio per user : " ).append( ratio )
                .append( '\n' );
    }

    /**
     * Write recommendations into the database for a given user
     * 
     * @param strUsername
     *            The Username
     * @param listProduct
     *            The product list
     * @return The number of recommendations found
     */
    private int writeUserRecommendations( String strUsername, List<RecommendedProduct> listProduct )
    {
        int nCount = 0;
        _daoRecommendation.deleteByUser( strUsername, _plugin );
        for ( RecommendedProduct product : listProduct )
        {
            Recommendation recommendation = new Recommendation( );
            recommendation.setUsername( strUsername );
            recommendation.setIdProduct( product.getProductId( ) );
            recommendation.setScore( product.getScore( ) );
            _daoRecommendation.insert( recommendation, _plugin );
            nCount++;
        }

        return nCount;

    }
}
