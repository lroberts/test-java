<?php
// $Id$
?>

<!-- start block.tpl.php -->
<div class="block-wrapper <?php print $block_zebra; ?>">
  <div id="block-<?php print $block->module .'-'. $block->delta; ?>" class="block block-<?php print $block->module ?>">
    <?php if ($block->subject): ?>
    <h2 class="block-title"><?php print $block->subject ?></h2>
    <?php endif; ?>
    <div class="content">
      <?php print $block->content ?>
    </div>
  </div>
</div>
<!-- /end block.tpl.php -->
