/*
  Copyright (C) 2008 by Phase2 Technology.
  Author: Irakli Nadareishvili

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License.
  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY. See the LICENSE.txt file for more details.

 */

jQuery(document).ready(function(){

    var regexp = new RegExp(/block-(.+?)-(.+?)/mi);
    var checkp = new RegExp(/block-.*?-.+/mi);

  jQuery("div.block").each(function (i) {
    var block_id = jQuery(this).attr('id');
  if (block_id.match(checkp)) {
      var block_path = block_id.replace(regexp, '$1/$2');
      block_id = block_id.replace(regexp, '$1_$2');
    var block_link = '<div id="bel_id_' + block_id + '" class="block_edit_link"><a href="/admin/build/block/configure/' + block_path + '">Edit</a></div>';
      jQuery(this).prepend(block_link);
    }
  });

  jQuery("div.block").mouseover(function(){
    var block_id = jQuery(this).attr('id');
    if (block_id.match(checkp)) {
      block_id = block_id.replace(regexp, '$1_$2');
      jQuery('div#bel_id_'+block_id).css('display', 'block');
    }
  });

  jQuery("div.block").mouseout(function(){
    var block_id = jQuery(this).attr('id');
    if (block_id.match(checkp)) {
      block_id = block_id.replace(regexp, '$1_$2');
      jQuery('div#bel_id_'+block_id).css('display', 'none');
    }
  });

});
