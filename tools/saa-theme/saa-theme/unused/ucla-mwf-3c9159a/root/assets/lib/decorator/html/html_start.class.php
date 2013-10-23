<?php

/**
 *
 *
 * @package decorator
 * @subpackage html_decorator
 *
 * @author ebollens
 * @copyright Copyright (c) 2010-11 UC Regents
 * @license http://mwf.ucla.edu/license
 * @version 20110518
 *
 * @uses Tag_Open_HTML_Decorator
 */

require_once(dirname(__FILE__).'/tag_open.class.php');

class HTML_Start_HTML_Decorator extends Tag_Open_HTML_Decorator
{
    public function __construct()
    {
        parent::__construct('html');
    }

    public function render()
    {
        return '<!DOCTYPE html>
'.parent::render();
    }
}
