<?php
// June 04, 2009 04:32:51 PM

$content['type']  = array (
  'name' => 'Event',
  'type' => 'ucsf_event',
  'description' => 'An <em>Event</em> is a content-type for adding new calendar-based events to the site. Most current events will be displayed on the homepage.',
  'title_label' => 'Title',
  'body_label' => 'Body',
  'min_word_count' => '0',
  'help' => '',
  'node_options' =>
  array (
    'status' => true,
    'promote' => true,
    'revision' => true,
    'sticky' => false,
  ),
  'nodeblock' => '0',
  'nodewords' => 1,
  'old_type' => 'ucsf_event',
  'orig_type' => 'ucsf_event',
  'module' => 'node',
  'custom' => '1',
  'modified' => '1',
  'locked' => '0',
  'comment' => 0,
  'comment_default_mode' => 4,
  'comment_default_order' => 1,
  'comment_default_per_page' => 50,
  'comment_controls' => 3,
  'comment_anonymous' => 0,
  'comment_subject_field' => 1,
  'comment_preview' => 1,
  'comment_form_location' => 0,
  'print_display' => 1,
  'print_display_comment' => 0,
  'print_display_urllist' => 1,
  'print_mail_display' => 1,
  'print_mail_display_comment' => 0,
  'print_mail_display_urllist' => 1,
);
$content['fields']  = array (
  0 =>
  array (
    'label' => 'Date',
    'field_name' => 'field_date',
    'type' => 'date',
    'widget_type' => 'date_popup_repeat',
    'change' => 'Change basic information',
    'weight' => '-4',
    'default_value' => 'blank',
    'default_value2' => 'blank',
    'default_value_code' => '',
    'default_value_code2' => '',
    'input_format' => 'Y-m-d H:i:s',
    'input_format_custom' => '',
    'year_range' => '-3:+3',
    'increment' => '1',
    'advanced' =>
    array (
      'label_position' => 'above',
      'text_parts' =>
      array (
        'year' => 0,
        'month' => 0,
        'day' => 0,
        'hour' => 0,
        'minute' => 0,
        'second' => 0,
      ),
    ),
    'label_position' => 'above',
    'text_parts' =>
    array (
    ),
    'description' => '',
    'group' => false,
    'required' => 1,
    'multiple' => 1,
    'repeat' => 1,
    'todate' => 'optional',
    'granularity' =>
    array (
      'year' => 'year',
      'month' => 'month',
      'day' => 'day',
      'hour' => 'hour',
      'minute' => 'minute',
    ),
    'default_format' => 'medium',
    'tz_handling' => 'site',
    'timezone_db' => 'UTC',
    'repeat_collapsed' => '1',
    'op' => 'Save field settings',
    'module' => 'date',
    'widget_module' => 'date',
    'columns' =>
    array (
      'value' =>
      array (
        'type' => 'varchar',
        'length' => 20,
        'not null' => false,
        'sortable' => true,
        'views' => true,
      ),
      'value2' =>
      array (
        'type' => 'varchar',
        'length' => 20,
        'not null' => false,
        'sortable' => true,
        'views' => false,
      ),
      'rrule' =>
      array (
        'type' => 'text',
        'not null' => false,
        'sortable' => false,
        'views' => false,
      ),
    ),
    'display_settings' =>
    array (
      'weight' => '-4',
      'parent' => '',
      'label' =>
      array (
        'format' => 'hidden',
      ),
      'teaser' =>
      array (
        'format' => 'default',
        'exclude' => 0,
      ),
      'full' =>
      array (
        'format' => 'default',
        'exclude' => 0,
      ),
      4 =>
      array (
        'format' => 'default',
        'exclude' => 0,
      ),
      2 =>
      array (
        'format' => 'default',
        'exclude' => 0,
      ),
      3 =>
      array (
        'format' => 'default',
        'exclude' => 0,
      ),
      'token' =>
      array (
        'format' => 'default',
        'exclude' => 0,
      ),
    ),
  ),
);
$content['extra']  = array (
  'title' => '-5',
  'body_field' => '-3',
  'menu' => '-1',
  'print' => '-2',
);
