<?php
// $Id$
?>
<!-- start block.tpl.php -->
<?php if ($block->content): ?>
<div id="how_do_i">
  <ul id="how_do_i_title">
    <li class="clear-block"><a id="how_do_i_title_btn" href="javascript:void(0);"><?php print $block->subject ?></a>
      <div id="how_do_i_content" class="clear-block">

      <?php print $block->content ?>

      </div>
    </li>
  </ul>
</div>

<script type="text/javascript">
var timeout=500,closetimer=0,ddmenuitem=0;function howdoi_open(){howdoi_canceltimer();howdoi_close();ddmenuitem=jQuery('#how_do_i_content').css('visibility','visible');jQuery('#how_do_i').addClass('over')}function howdoi_close(){if(ddmenuitem){jQuery('#how_do_i').removeClass('over');ddmenuitem.css('visibility','hidden')}}function howdoi_timer(){closetimer=window.setTimeout(howdoi_close,timeout)}function howdoi_canceltimer(){if(closetimer){window.clearTimeout(closetimer);closetimer=null}}jQuery(document).ready(function(){jQuery('#how_do_i_title>li').bind('mouseover',howdoi_open);jQuery('#how_do_i_title>li').bind('mouseout',howdoi_timer)});document.onclick=howdoi_close;
</script>
<?php endif; ?>
<!-- /end block.tpl.php -->
