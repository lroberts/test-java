<?php
//  July 21, 2009 02:25:16 PM

$content['type']  = array (
  'name' => 'Quick Links Tab',
  'type' => 'quicklinks',
  'description' => 'A <em>Quick Links Tab</em> is displayed in <strong>Quick Links</strong> accordion boxes.',
  'title_label' => 'Title',
  'body_label' => 'Body',
  'min_word_count' => '0',
  'help' => '',
  'node_options' =>
  array (
    'status' => true,
    'revision' => true,
    'promote' => false,
    'sticky' => false,
  ),
  'upload' => '0',
  'nodeblock' => '0',
  'nodewords' => 0,
  'old_type' => 'quicklinks',
  'orig_type' => '',
  'module' => 'node',
  'custom' => '1',
  'modified' => '1',
  'locked' => '0',
  'comment' => '0',
  'comment_default_mode' => '4',
  'comment_default_order' => '1',
  'comment_default_per_page' => '50',
  'comment_controls' => '3',
  'comment_anonymous' => 0,
  'comment_subject_field' => '1',
  'comment_preview' => '1',
  'comment_form_location' => '0',
  'print_display' => 0,
  'print_display_comment' => 0,
  'print_display_urllist' => 0,
  'print_mail_display' => 1,
  'print_mail_display_comment' => 0,
  'print_mail_display_urllist' => 1,
);
$content['fields']  = array (
  0 =>
  array (
    'label' => 'Sorting order',
    'field_name' => 'field_sorting',
    'type' => 'number_integer',
    'widget_type' => 'optionwidgets_select',
    'change' => 'Change basic information',
    'weight' => '-4',
    'description' => 'Define where this item is going to be positioned. Smaller the number higher the position.',
    'default_value' =>
    array (
      0 =>
      array (
        'value' => '0',
      ),
    ),
    'default_value_php' => '',
    'default_value_widget' => NULL,
    'group' => false,
    'required' => 1,
    'multiple' => '0',
    'min' => '',
    'max' => '',
    'prefix' => '',
    'suffix' => '',
    'allowed_values' => '0
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20',
    'allowed_values_php' => '',
    'op' => 'Save field settings',
    'module' => 'number',
    'widget_module' => 'optionwidgets',
    'columns' =>
    array (
      'value' =>
      array (
        'type' => 'int',
        'not null' => false,
        'sortable' => true,
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
        'format' => 'hidden',
        'exclude' => 1,
      ),
      'full' =>
      array (
        'format' => 'hidden',
        'exclude' => 1,
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
  'menu' => '-2',
  'print' => '-1',
);
