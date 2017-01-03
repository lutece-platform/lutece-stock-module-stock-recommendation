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
package fr.paris.lutece.plugins.stock.modules.recommendation.web;

import fr.paris.lutece.plugins.stock.modules.recommendation.business.RecommendedProduct;
import fr.paris.lutece.plugins.stock.modules.recommendation.service.StockRecommendationService;
import fr.paris.lutece.portal.service.security.LuteceUser;
import fr.paris.lutece.portal.service.security.SecurityService;
import fr.paris.lutece.portal.service.security.UserNotSignedException;
import fr.paris.lutece.portal.service.util.AppLogService;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.xpages.XPage;
import fr.paris.lutece.portal.util.mvc.xpage.MVCApplication;
import fr.paris.lutece.portal.util.mvc.commons.annotations.View;
import fr.paris.lutece.portal.util.mvc.xpage.annotations.Controller;
import fr.paris.lutece.portal.web.l10n.LocaleService;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.apache.mahout.cf.taste.common.TasteException;

/**
 * This class provides a simple implementation of an XPage
 */
@Controller( xpageName = "recommendation", pageTitleI18nKey = "module.stock.recommendation.xpage.recommendation.pageTitle", pagePathI18nKey = "module.stock.recommendation.xpage.recommendation.pagePathLabel" )
public class RecommendationApp extends MVCApplication
{
    private static final String TEMPLATE_XPAGE = "/skin/plugins/stock/modules/recommendation/recommendation.html";
    private static final String MARK_PRODUCTS_LIST = "products_list";
    private static final String MARK_PRODUCT_LINK_URL = "product_link_url";
    private static final String PROPERTY_LINK_URL = "stock-recommendation.product.linkUrl";
    private static final String PRODUCT_LINK_URL = AppPropertiesService.getProperty( PROPERTY_LINK_URL );
    private static final String PROPERTY_TEST_USER = "stock-recommendation.testuser";
    private static final String VIEW_HOME = "home";
    private static final String PARAMETER_USERNAME = "username";

    /**
     * Returns the content of the page recommendation.
     * 
     * @param request
     *            The HTTP request
     * @return The view
     * @throws UserNotSignedException
     *             if no user connected
     */
    @View( value = VIEW_HOME, defaultView = true )
    public XPage viewHome( HttpServletRequest request ) throws UserNotSignedException
    {

        // ////////// Test features - begin
        String strUserName = AppPropertiesService.getProperty( PROPERTY_TEST_USER );

        String strRequestUser = request.getParameter( PARAMETER_USERNAME );
        if ( strRequestUser != null )
        {
            strUserName = strRequestUser;
        }
        // ////////// Test features - end

        if ( strUserName == null )
        {
            LuteceUser user = SecurityService.getInstance( ).getRegisteredUser( request );
            if ( user == null )
            {
                throw new UserNotSignedException( );
            }
            strUserName = user.getName( );
        }

        List<RecommendedProduct> listProducts = null;
        try
        {
            listProducts = StockRecommendationService.instance( ).getRecommendedProducts( strUserName );
        }
        catch( TasteException ex )
        {
            // User not found
            addError( "User not found" );
            AppLogService.info( "Recommendation error : " + ex.getMessage( ) );
        }
        Map<String, Object> model = getModel( );
        model.put( MARK_PRODUCTS_LIST, listProducts );
        model.put( MARK_PRODUCT_LINK_URL, PRODUCT_LINK_URL );
        return getXPage( TEMPLATE_XPAGE, LocaleService.getDefault( ), model );
    }
}
