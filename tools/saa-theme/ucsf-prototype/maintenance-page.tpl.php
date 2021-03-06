<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="<?php print $language->language ?>" lang="<?php print $language->language ?>" dir="<?php print $language->dir ?>">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=7" />
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
  <meta http-equiv="Content-Style-Type" content="text/css" />
  <meta http-equiv="Content-Script-Type" content="text/javascript" />
  <title><?php print $head_title; ?></title>
  <?php print $head; ?>
  <?php print $styles; ?>
  <?php print $scripts; ?>
  <script type="text/javascript">$(function(){$("a:not([href*=<?php print $_SERVER['HTTP_HOST']; ?>])[href^='http'], a[href$='.pdf']").addClass("ExternalLink").click(function(){window.open($(this).attr('href'));return false;});});</script>
  <script type="text/javascript"><?php    /* Needed to avoid Flash of Unstyled Content in IE */ ?> </script>
</head>
<body class="<?php print $body_classes; ?>">


<div id="ucsf" class="clear-block">
  <div id="ucsf-links" class="site-width">
    <img src="/sites/all/themes/ucsf/ucsf_base/ucsf.gif" width="553" height="30" alt="University of California, San Francisco" usemap="#banner_map" />
    <map name="banner_map" id="banner_map">
      <area shape="rect" coords="0,0,46,30" href="http://www.ucsf.edu/" alt="UCSF home page" >
      <area shape="rect" coords="55,9,249,22" href="http://www.ucsf.edu/" alt="UCSF home" >
      <area shape="rect" coords="266,9,330,22" href="http://www.ucsf.edu/about-ucsf/" alt="About UCSF" >
      <area shape="rect" coords="347,9,416,22" href="http://www.googlesyndicatedsearch.com/u/ucsf" alt="Search UCSF" >
      <area shape="rect" coords="434,9,546,22" href="http://www.ucsfhealth.org/" alt="UCSF Medical Center" >
    </map>
  </div><!-- /id="ucsf-links" -->
</div><!-- /id="ucsf" -->


<?php if(!empty($sys_msg)): ?>
<div id="site-msg" class="clear-block site-width">
  <div id="site-msg-content">
    <?php print $sys_msg; ?>
  </div>
</div>
<?php endif; ?>




<div id="site" class="site-width">
<div id="ui">
  <div id="ui-header">
    <div class="ui-content clear-block">
      <?php    if (!empty($logo)): ?>
        <a href="<?php    print $front_page; ?>" rel="home" id="logo"><img src="<?php    print $logo; ?>" alt="<?php    print $site_name; ?>" /></a>
      <?php    endif; ?>

      <?php    if (!empty($search_box)): ?>
        <div id="search-box"><?php    print $search_box; ?></div>
      <?php    endif; ?>

      <?php if (!empty($header)): ?>
        <div id="ui-header-content"><?php print $header; ?></div>
      <?php endif; ?>

      <?php    if (!empty($secondary_links)): ?>
      <div id="header-menu"><?php    print theme('links', $secondary_links, array('class' => 'header-menu')); ?></div>
      <?php    endif; ?>
  </div><!-- /class="ui-content"-->
  </div><!-- /id="ui-header" -->


  <?php    if ($primary_links): ?>
  <div id="ui-menu" class="clear-block">
  <?php    //print theme('links', $primary_links, array('class' =>'sf-menu', 'id' => 'ui-menu-primary')); ?>
  <?php    print theme('links', phptemplate_primary_links(), array('class' =>'sf-menu', 'id' => 'ui-navigation-menu')); ?>
  </div><!-- / id="ui-menu" -->
  <?php    endif; ?>


  <div id="ui-menubottom"><div></div></div>


  <?php    if ($help): ?>
    <div id="ui-help" class="clear-block">
    <?php    print $help; ?>
    </div>
  <?php    endif; ?>
  <?php    if ($messages): ?>
    <div id="ui-messages" class="clear-block">
    <?php    print $messages; ?>
    </div>
  <?php    endif; ?>
  <?php    if ($user->uid): /* Show TABS to logged in users only */ ?>
    <?php    if ($tabs): ?>
      <div id="ui-tabs" class="clear-block">
      <?php    print $tabs; ?>
      </div>
    <?php    endif; ?>
  <?php    endif; ?>






  <div id="ui-columns" class="clear-block">
    <div id="ui-wrapper">
      <div id="ui-content-main">
        <div class="ui-content">
          <?php    if ($title): ?>
            <h1 class="title" title="<?php    print $title; ?>"><?php    print $title; ?></h1>
          <?php    endif; ?>

          <?php    print $content; ?>
        </div><!-- /class="ui-content" -->
      </div><!-- /id="ui-content-main" -->
    </div><!-- /id="ui-wrapper" -->


  </div><!-- /id="ui-columns" -->
  <div id="ui-columns-clear"></div>

</div><!-- /id="ui" -->
</div><!-- /id="site" -->
<div id="ui-footer" class="site-width">
  <div class="ui-content clear-block">

    <div id="ui-footer-msg" class="clear-block">
      <p id="c">&copy; <?php print date("Y") ?> The Regents of the University of California</p>
      <?php    //begin: Footer content ?>
      <?php    print $footer; ?>

      <?php    if ($footer_message): ?>
      <?php    print $footer_message; ?>
      <?php    endif; ?>
      <?php    //end: Footer content ?>
    </div>
  </div>
</div><!-- /id="ui-footer" -->

<script type="text/javascript"> Cufon.now(); </script>
<?php    print $closure; ?>
</body>
</html>
