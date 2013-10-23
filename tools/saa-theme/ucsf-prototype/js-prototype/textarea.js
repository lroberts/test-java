
Drupal.behaviors.textarea = function(context) {
  jQuery('textarea.resizable:not(.textarea-processed)', context).each(function() {
    // Avoid non-processed teasers.
    if (jQuery(this).is(('textarea.teaser:not(.teaser-processed)'))) {
      return false;  
    }
    var textarea = jQuery(this).addClass('textarea-processed'), staticOffset = null;

    // When wrapping the text area, work around an IE margin bug.  See:
    // http://jaspan.com/ie-inherited-margin-bug-form-elements-and-haslayout
    jQuery(this).wrap('<div class="resizable-textarea"><span></span></div>')
      .parent().append(jQuery('<div class="grippie"></div>').mousedown(startDrag));

    var grippie = jQuery('div.grippie', jQuery(this).parent())[0];
    grippie.style.marginRight = (grippie.offsetWidth - jQuery(this)[0].offsetWidth) +'px';

    function startDrag(e) {
      staticOffset = textarea.height() - e.pageY;
      textarea.css('opacity', 0.25);
      jQuery(document).mousemove(performDrag).mouseup(endDrag);
      return false;
    }

    function performDrag(e) {
      textarea.height(Math.max(32, staticOffset + e.pageY) + 'px');
      return false;
    }

    function endDrag(e) {
      jQuery(document).unbind("mousemove", performDrag).unbind("mouseup", endDrag);
      textarea.css('opacity', 1);
    }
  });
};
