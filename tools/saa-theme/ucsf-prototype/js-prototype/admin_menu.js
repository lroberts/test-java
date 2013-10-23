/* $Id: admin_menu.js,v 1.7.2.9 2010/02/20 23:53:18 sun Exp $ */

jQuery(document).ready(function() {
  if (!jQuery('#admin-menu').length) {
    return;
  }

  // Apply margin-top if enabled; directly applying marginTop doesn't work in IE.
  if (Drupal && Drupal.settings && Drupal.settings.admin_menu) {
    if (Drupal.settings.admin_menu.margin_top) {
      jQuery('body').addClass('admin-menu');
    }
    if (Drupal.settings.admin_menu.position_fixed) {
      jQuery('#admin-menu').css('position', 'fixed');
    }
    // Move page tabs into administration menu.
    if (Drupal.settings.admin_menu.tweak_tabs) {
      jQuery('ul.tabs.primary li').each(function() {
        jQuery(this).addClass('admin-menu-tab').appendTo('#admin-menu > ul');
      });
      jQuery('ul.tabs.secondary').appendTo('#admin-menu > ul > li.admin-menu-tab.active').removeClass('secondary');
    }
    // Collapse fieldsets on Modules page. For why multiple selectors see #111719.
    if (Drupal.settings.admin_menu.tweak_modules) {
      jQuery('#system-modules fieldset:not(.collapsed), #system-modules-1 fieldset:not(.collapsed)').addClass('collapsed');
    }
  }

  // Hover emulation for IE 6.
  if (jQuery.browser.msie && parseInt(jQuery.browser.version) == 6) {
    jQuery('#admin-menu li').hover(function() {
      jQuery(this).addClass('iehover');
    }, function() {
      jQuery(this).removeClass('iehover');
    });
  }

  // Delayed mouseout.
  jQuery('#admin-menu li').hover(function() {
    // Stop the timer.
    clearTimeout(this.sfTimer);
    // Display child lists.
    jQuery('> ul', this).css({left: 'auto', display: 'block'})
      // Immediately hide nephew lists.
      .parent().siblings('li').children('ul').css({left: '-999em', display: 'none'});
  }, function() {
    // Start the timer.
    var uls = jQuery('> ul', this);
    this.sfTimer = setTimeout(function() {
      uls.css({left: '-999em', display: 'none'});
    }, 400);
  });
});
