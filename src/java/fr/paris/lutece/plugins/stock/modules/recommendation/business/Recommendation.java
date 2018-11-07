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

/**
 * This is the business class for the object Recommendation
 */
public class Recommendation
{
    // Variables declarations
    private String _strUsername;
    private int _nIdProduct;
    private double _dScore;

    /**
     * Returns the Username
     * 
     * @return The Username
     */
    public String getUsername( )
    {
        return _strUsername;
    }

    /**
     * Sets the Username
     * 
     * @param strUsername
     *            The Username
     */
    public void setUsername( String strUsername )
    {
        _strUsername = strUsername;
    }

    /**
     * Returns the IdProduct
     * 
     * @return The IdProduct
     */
    public int getIdProduct( )
    {
        return _nIdProduct;
    }

    /**
     * Sets the IdProduct
     * 
     * @param nIdProduct
     *            The IdProduct
     */
    public void setIdProduct( int nIdProduct )
    {
        _nIdProduct = nIdProduct;
    }

    /**
     * Returns the Score
     * 
     * @return The Score
     */
    public double getScore( )
    {
        return _dScore;
    }

    /**
     * Sets the Score
     * 
     * @param fScore
     *            The Score
     */
    public void setScore( double fScore )
    {
        _dScore = fScore;
    }

    @Override
    public String toString( )
    {
        return "USER:" + _strUsername + "  ITEM:" + _nIdProduct + "  SCORE:" + _dScore;
    }

}
