<?php

/**
 * Javascript file that defines the functions of mwf.user_agent. This also
 * writes cookies with the cookie prefix for page width and page height.
 *
 * These cookies are written inline as this is XHTML MP 1.0 supported, as
 * opposed to as an event call since event calls are less supported.
 *
 * These cookies are used by User_Browser class after the first page load.
 *
 * @package core
 * @subpackage js
 *
 * @author ebollens
 * @copyright Copyright (c) 2010-11 UC Regents
 * @license http://mwf.ucla.edu/license
 * @version 20101021
 *
 * @uses Config
 * @link User_Browser
 */

include_once(dirname(dirname(dirname(__FILE__))).'/config.php');

?> mwf.browser = new function() {
    this.pageWidth=function(){return window.innerWidth != null? window.innerWidth : document.documentElement && document.documentElement.clientWidth ?       document.documentElement.clientWidth : document.body != null ? document.body.clientWidth : null;}
    this.pageHeight=function(){return  window.innerHeight != null? window.innerHeight : document.documentElement && document.documentElement.clientHeight ?  document.documentElement.clientHeight : document.body != null? document.body.clientHeight : null;}
    this.posLeft=function(){return typeof window.pageXOffset != 'undefined' ? window.pageXOffset :document.documentElement && document.documentElement.scrollLeft ? document.documentElement.scrollLeft : document.body.scrollLeft ? document.body.scrollLeft : 0;}
    this.posTop=function(){return typeof window.pageYOffset != 'undefined' ?  window.pageYOffset : document.documentElement && document.documentElement.scrollTop ? document.documentElement.scrollTop : document.body.scrollTop ? document.body.scrollTop : 0;}
    this.posRight=function(){return mwf.browser.posLeft()+mwf.browser.pageWidth();}
    this.posBottom=function(){return mwf.browser.posTop()+mwf.browser.pageHeight();}
};

document.cookie='<?php echo Config::get('global', 'cookie_prefix'); ?>bw='+mwf.browser.pageWidth()+';path=/';
document.cookie='<?php echo Config::get('global', 'cookie_prefix'); ?>bh='+mwf.browser.pageHeight()+';path=/';