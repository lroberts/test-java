
/**
 * Move a block in the blocks table from one region to another via select list.
 *
 * This behavior is dependent on the tableDrag behavior, since it uses the
 * objects initialized in that behavior to update the row.
 */
Drupal.behaviors.blockDrag = function(context) {
  var table = jQuery('table#blocks');
  var tableDrag = Drupal.tableDrag.blocks; // Get the blocks tableDrag object.
/*
if (typeof tableDrag == 'undefined') {
      // This is not the blocks admin screen.
      return;
    }
*/
  // Add a handler for when a row is swapped, update empty regions.
  tableDrag.row.prototype.onSwap = function(swappedRow) {
    checkEmptyRegions(table, this);
  };

  // A custom message for the blocks page specifically.
  Drupal.theme.tableDragChangedWarning = function () {
    return '<div class="warning">' + Drupal.theme('tableDragChangedMarker') + ' ' + Drupal.t("The changes to these blocks will not be saved until the <em>Save blocks</em> button is clicked.") + '</div>';
  };

  // Add a handler so when a row is dropped, update fields dropped into new regions.
  tableDrag.onDrop = function() {
    dragObject = this;
    if (jQuery(dragObject.rowObject.element).prev('tr').is('.region-message')) {
      var regionRow = jQuery(dragObject.rowObject.element).prev('tr').get(0);
      var regionName = regionRow.className.replace(/([^ ]+[ ]+)*region-([^ ]+)-message([ ]+[^ ]+)*/, '$2');
      var regionField = jQuery('select.block-region-select', dragObject.rowObject.element);
      var weightField = jQuery('select.block-weight', dragObject.rowObject.element);
      var oldRegionName = weightField[0].className.replace(/([^ ]+[ ]+)*block-weight-([^ ]+)([ ]+[^ ]+)*/, '$2');

      if (!regionField.is('.block-region-'+ regionName)) {
        regionField.removeClass('block-region-' + oldRegionName).addClass('block-region-' + regionName);
        weightField.removeClass('block-weight-' + oldRegionName).addClass('block-weight-' + regionName);
        regionField.val(regionName);
      }
    }
  };

  // Add the behavior to each region select list.
  jQuery('select.block-region-select:not(.blockregionselect-processed)', context).each(function() {
    jQuery(this).change(function(event) {
      // Make our new row and select field.
      var row = jQuery(this).parents('tr:first');
      var select = jQuery(this);
      tableDrag.rowObject = new tableDrag.row(row);

      // Find the correct region and insert the row as the first in the region.
      jQuery('tr.region-message', table).each(function() {
        if (jQuery(this).is('.region-' + select[0].value + '-message')) {
          // Add the new row and remove the old one.
          jQuery(this).after(row);
          // Manually update weights and restripe.
          tableDrag.updateFields(row.get(0));
          tableDrag.rowObject.changed = true;
          if (tableDrag.oldRowElement) {
            jQuery(tableDrag.oldRowElement).removeClass('drag-previous');
          }
          tableDrag.oldRowElement = row.get(0);
          tableDrag.restripeTable();
          tableDrag.rowObject.markChanged();
          tableDrag.oldRowElement = row;
          jQuery(row).addClass('drag-previous');
        }
      });

      // Modify empty regions with added or removed fields.
      checkEmptyRegions(table, row);
      // Remove focus from selectbox.
      select.get(0).blur();
    });
    jQuery(this).addClass('blockregionselect-processed');
  });

  var checkEmptyRegions = function(table, rowObject) {
    jQuery('tr.region-message', table).each(function() {
      // If the dragged row is in this region, but above the message row, swap it down one space.
      if (jQuery(this).prev('tr').get(0) == rowObject.element) {
        // Prevent a recursion problem when using the keyboard to move rows up.
        if ((rowObject.method != 'keyboard' || rowObject.direction == 'down')) {
          rowObject.swap('after', this);
        }
      }
      // This region has become empty
      if (jQuery(this).next('tr').is(':not(.draggable)') || jQuery(this).next('tr').size() == 0) {
        jQuery(this).removeClass('region-populated').addClass('region-empty');
      }
      // This region has become populated.
      else if (jQuery(this).is('.region-empty')) {
        jQuery(this).removeClass('region-empty').addClass('region-populated');
      }
    });
  };
};
