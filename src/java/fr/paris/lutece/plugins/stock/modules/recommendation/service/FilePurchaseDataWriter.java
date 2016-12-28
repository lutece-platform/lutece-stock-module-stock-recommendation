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

package fr.paris.lutece.plugins.stock.modules.recommendation.service;

import fr.paris.lutece.portal.service.util.AppLogService;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * FilePurchaseDataWriter
 */
public class FilePurchaseDataWriter implements PurchaseDataWriter
{
    private Writer _output;
    private File _file;

    public FilePurchaseDataWriter( File file )
    {
        _file = file;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void reset( )
    {
        try
        {
            _output = new BufferedWriter( new FileWriter( _file ) ); // clears file every time
        }
        catch( IOException ex )
        {
            AppLogService.error( "stock-recommendation - Error openening data output : " + ex.getMessage( ), ex );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void write( long lUserID, int nItemID )
    {
        try
        {
            _output.append( String.valueOf( lUserID ) ).append( ',' ).append( String.valueOf( nItemID ) ).append( ",1\n" );
        }
        catch( IOException ex )
        {
            AppLogService.error( "stock-recommendation - Error writing data output : " + ex.getMessage( ), ex );
        }
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void close( )
    {
        try
        {
            _output.close( );
        }
        catch( IOException ex )
        {
            AppLogService.error( "stock-recommendation - Error closing data output : " + ex.getMessage( ), ex );
        }
    }

}
