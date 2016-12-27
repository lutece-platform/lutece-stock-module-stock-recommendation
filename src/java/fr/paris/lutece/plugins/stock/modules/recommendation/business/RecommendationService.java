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
import fr.paris.lutece.portal.service.plugin.PluginService;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import org.apache.mahout.cf.taste.impl.model.file.FileIDMigrator;

/**
 * RecommendationService
 */
public class RecommendationService
{
    private static final String PLUGIN_NAME = "stock-recommendation";
    private static final String PROPERTY_ID_MIGRATOR_FILE_PATH = "stock-recommendation.idMigratorFilePath";
    private static final String PROPERTY_DATA_FILE_PATH = "stock-recommendation.dataFilePath";
    private static Plugin _plugin = PluginService.getPlugin( PLUGIN_NAME );
    private static StockPurchaseDAO _dao = new StockPurchaseDAO( );
    private static FileIDMigrator _migrator;
    private static RecommendationService _singleton;
    private static PurchaseDataWriter _writer;

    /** private constructor */
    private RecommendationService( )
    {
    }

    public static RecommendationService instance( )
    {
        if ( _singleton == null )
        {
            _singleton = new RecommendationService( );
            String strIdMigratorFilePath = AppPropertiesService.getProperty( PROPERTY_ID_MIGRATOR_FILE_PATH );
            File idMigratorFile = new File( AppPathService.getAbsolutePathFromRelativePath( strIdMigratorFilePath ) );
            String strDataFilePath = AppPropertiesService.getProperty( PROPERTY_DATA_FILE_PATH );
            File dataFile = new File( AppPathService.getAbsolutePathFromRelativePath( strDataFilePath ) );
            try
            {
                _migrator = new FileIDMigrator( idMigratorFile );
                _writer = new FilePurchaseDataWriter( dataFile );
            }
            catch( FileNotFoundException ex )
            {
                AppLogService.error( "stock-recommendation : Error creating file " + strIdMigratorFilePath + " " + ex.getMessage( ), ex );
            }

        }
        return _singleton;
    }

    public void extractPurchases( )
    {
        _writer.reset( );
        List<UserItem> list = _dao.selectUserItemsList( _plugin );
        for ( UserItem ui : list )
        {
            long lUserID = _migrator.toLongID( ui.getUserName( ) );
            _writer.write( lUserID, ui.getItemId( ) );

        }
        _writer.close( );
    }

}
