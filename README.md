![](http://dev.lutece.paris.fr/jenkins/buildStatus/icon?job=stock-module-stock-recommendation-deploy)
# Stock Recommendation Module

## Introduction

This module offers recommendations of purchase or reservation to users of the Stock plugin. It uses machine leaning technologiesprovided by the [Apache Mahout](http://mahout.apache.org/) library.

The recommendation principle is based on buying similarities with other users. The products offered are those that the user does not yet have whilethey have been retained by users having made orders close.

## The recommendation XPage

This module provides an XPage available at the following URL :

```

jsp/site/Portal.jsp?page=recommendation
                    
```

## The Page Include

To use the Page Include provided, you only have to had the bookmark `${recommendations}` into your HTML template where the list of recommended products should appear for thecurrently connected user.


[Maven documentation and reports](http://dev.lutece.paris.fr/plugins/module-stock-recommendation/)



 *generated by [xdoc2md](https://github.com/lutece-platform/tools-maven-xdoc2md-plugin) - do not edit directly.*