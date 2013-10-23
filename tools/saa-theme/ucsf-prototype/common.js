// February 27, 2009 11:02:46 AM
jQuery(document).ready(function(){
  // Append a new class when search box gets focus
  jQuery("#search-theme-form :text").toggleVal();
  jQuery("#search-theme-form").submit(function() {
    jQuery(this).find(".toggleval").each(function() {
      if(jQuery(this).val() == jQuery(this).data("defText")) {
        jQuery(this).val("");
      }
    });
  });

  // How Do I?
  jQuery('#how-do-i-title a').toggle(
    function(){
      jQuery('#how-do-i').addClass('expanded');
      jQuery('#how-do-i-content').show();
      },
    function(){
      jQuery('#how-do-i').removeClass('expanded');
      jQuery('#how-do-i-content').hide();
      }
  );

  jQuery("ul.sf-menu").supersubs({ minWidth: 12, maxWidth: 27, extraWidth: 1 }).superfish({ pathClass: 'active', autoArrows: false});

  // Add a PDF icon to PDF files
  if (jQuery.browser.msie){
    jQuery("a[href$='.pdf']").before('<img src="/sites/all/themes/ucsf/ucsf_base/icon_pdf_ie.png" alt="PDF" align="absmiddle" style="padding-right:4px" />');
  }else{
    jQuery("a[href$='.pdf']").before('<img src="/sites/all/themes/ucsf/ucsf_base/icon_pdf.png" alt="PDF" align="absmiddle" style="padding-right:4px" />');
  }
  // Add an arrow to class="a" links
  jQuery('a.a').after('<img src="/sites/all/themes/ucsf/ucsf_base/a.a.png" alt="" align="absmiddle" style="padding-left:4px" />');
  // &raquo; » More links
  jQuery('a.more').before('<span class="more">&raquo; </span>');
  //For Registrar site, h2 headlines with links
  jQuery('#ucsf_registrar-not-front #ui-content-main .ui-content h2 a').after('<img src="/sites/all/themes/ucsf/ucsf_base/a.h2.png" alt="" align="absmiddle" style="padding-left:4px" />');

  jQuery('#ui-content-left a', '#ui-content-right a').each(function(){ jQuery(this).after(' &raquo;'); });

  jQuery('a#p6_AddThis')
    .click(function(){ return addthis_open(this, '', '[URL]', '[TITLE]'); })
    .mouseover(function(){ return addthis_open(this, '', '[URL]', '[TITLE]'); })
    .mouseout(function(){ addthis_close(); });

  jQuery('.not-front #ui-content-left .block-wrapper:first, .not-front #ui-content-right .block-wrapper:first').addClass('firstBlock');

  // Close message boxes
  jQuery('a.closeThis').click(function(){ jQuery(this).parent().fadeOut('slow'); });

  // Cufon replacements
  Cufon.set('fontFamily', 'Helvetica Neue');
  Cufon.replace(
								[ 
								 'h1.title', 
								 'h2.title', 
								 'h3.title', 
								 'h4.title', 
								 '.front h2.block-title', 
								 '.not-front #ui-content-main .ui-content h2',
								 '#ucsf_registrar-front div#block-sidenav ul.menu li a'
								 ]
								);
								
  Cufon.set('fontFamily', 'Helvetica Neue Light');
  Cufon.replace(
								[ 
								 'div#home-intro', 
								 'div#ui-slides-content h2.block-title' 
								 ]
								);

  jQuery('#home-intro, h1.title, h2.title, h3.title, h4.title, .front h2.block-title, .not-front #ui-content-main .ui-content h2').css('zoom', '1');

  // Tabs
  jQuery('.ucsftabs + ul').addClass('clear-block');
  jQuery('.ucsftabs').tabs();

  // Zebra tables
  jQuery("tbody tr:odd").addClass("odd");

  // Font resizing
  fontResizer('12px','14px','18px');


});


/* Font Sizer Developed by fluidByte (http://www.fluidbyte.net)
 *
 * This script is modified.
 * Visit developer's website for the original script.
**/
function fontResizer(smallFont,medFont,largeFont) {

  function saveState(curSize) {
    var date = new Date();
    date.setTime(date.getTime()+(7*24*60*60*1000));
    var expires = "; expires="+date.toGMTString();
    document.cookie = "fontSizer"+"="+curSize+expires+"; path=/";
  }

  jQuery(".toolFontSize").toggle(
    function () {
      jQuery('#ui-columns, #ui-footer').css('font-size', medFont);
      saveState(medFont);
      Cufon.refresh();
    },
    function () {
      jQuery('#ui-columns, #ui-footer').css('font-size', largeFont);
      saveState(largeFont);
      Cufon.refresh();
    },
    function () {
      jQuery('#ui-columns, #ui-footer').css('font-size', smallFont);
      saveState(smallFont);
      Cufon.refresh();
    }
  );


  function getCookie(c_name) { if (document.cookie.length>0) { c_start=document.cookie.indexOf(c_name + "="); if (c_start!=-1) { c_start=c_start + c_name.length+1; c_end=document.cookie.indexOf(";",c_start); if (c_end==-1) c_end=document.cookie.length; return unescape(document.cookie.substring(c_start,c_end)); } } return ""; }

  var savedSize = getCookie('fontSizer');

  if (savedSize!="") {
    jQuery('#ui-columns, #ui-footer').css('font-size', savedSize);
  } else {
    jQuery('#ui-columns, #ui-footer').css('font-size', smallFont);
  }

}