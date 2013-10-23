<?php
//  August 02, 2009 11:21:22 PM

$content['type']  = array (
  'name' => 'Staff',
  'type' => 'ucsf_staff',
  'description' => '<em>Staff</em> content-type is used to create new staff profiles. Staff profiles are grouped automatically and displayed on designated pages.',
  'title_label' => 'Full name',
  'body_label' => 'Biography',
  'min_word_count' => '0',
  'help' => 'Please include <em>Professional Experience</em>, <em>Special Areas of Interest</em> and <em>Other Interests</em>',
  'node_options' =>
  array (
    'status' => true,
    'promote' => false,
    'sticky' => false,
    'revision' => false,
  ),
  'nodeblock' => '0',
  'nodewords' => 0,
  'old_type' => 'ucsf_staff',
  'orig_type' => '',
  'module' => 'node',
  'custom' => '1',
  'modified' => '1',
  'locked' => '0',
  'signup_node_default_state' => 'disabled',
  'signup_date_field' => 0,
  'comment' => '0',
  'comment_default_mode' => '4',
  'comment_default_order' => '1',
  'comment_default_per_page' => '50',
  'comment_controls' => '3',
  'comment_anonymous' => 0,
  'comment_subject_field' => '1',
  'comment_preview' => '1',
  'comment_form_location' => '0',
  'page_title' =>
  array (
    'show_field' =>
    array (
      0 => 1,
      'show_field' => false,
    ),
    'pattern' => '',
  ),
  'print_display' => 1,
  'print_display_comment' => 0,
  'print_display_urllist' => 1,
  'print_mail_display' => 1,
  'print_mail_display_comment' => 0,
  'print_mail_display_urllist' => 1,
);
$content['groups']  = array (
  0 =>
  array (
    'label' => 'Profile details',
    'group_type' => 'standard',
    'settings' =>
    array (
      'form' =>
      array (
        'style' => 'fieldset',
        'description' => '',
      ),
      'display' =>
      array (
        'description' => '',
        'teaser' =>
        array (
          'format' => 'no_style',
          'exclude' => 0,
        ),
        'full' =>
        array (
          'format' => 'simple',
          'exclude' => 0,
        ),
        4 =>
        array (
          'format' => 'fieldset',
          'exclude' => 0,
        ),
        2 =>
        array (
          'format' => 'fieldset',
          'exclude' => 0,
        ),
        3 =>
        array (
          'format' => 'fieldset',
          'exclude' => 0,
        ),
        'token' =>
        array (
          'format' => 'fieldset',
          'exclude' => 0,
        ),
        'label' => 'hidden',
      ),
    ),
    'weight' => '-4',
    'group_name' => 'group_staff_details',
  ),
);
$content['fields']  = array (
  0 =>
  array (
    'label' => 'Photo',
    'field_name' => 'field_staff_photo',
    'type' => 'imceimage',
    'widget_type' => 'imceimage',
    'change' => 'Change basic information',
    'weight' => '3',
    'imceimage_file_types' => 'png,gif,jpg,jpeg',
    'description' => '',
    'default_value' =>
    array (
      0 =>
      array (
        'imceimage_path' => '/sites/all/files/images/staff_default.png',
        'imceimage_width' => '120',
        'imceimage_height' => '120',
        'imceimage_alt' => '',
      ),
    ),
    'default_value_php' => '',
    'default_value_widget' => NULL,
    'group' => 'group_staff_details',
    'required' => 1,
    'multiple' => '0',
    'op' => 'Save field settings',
    'module' => 'imceimage',
    'widget_module' => 'imceimage',
    'columns' =>
    array (
      'imceimage_path' =>
      array (
        'type' => 'char',
        'length' => 255,
        'not null' => false,
        'default' => '',
      ),
      'imceimage_width' =>
      array (
        'type' => 'int',
        'not null' => true,
        'default' => '0',
      ),
      'imceimage_height' =>
      array (
        'type' => 'int',
        'not null' => true,
        'default' => '0',
      ),
      'imceimage_alt' =>
      array (
        'type' => 'text',
      ),
    ),
    'display_settings' =>
    array (
      'weight' => '3',
      'parent' => 'group_staff_details',
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
  1 =>
  array (
    'label' => 'Category',
    'field_name' => 'field_staff_category',
    'type' => 'text',
    'widget_type' => 'optionwidgets_select',
    'change' => 'Change basic information',
    'weight' => '4',
    'description' => '',
    'default_value' =>
    array (
      0 =>
      array (
        'value' => '',
      ),
    ),
    'default_value_php' => '',
    'default_value_widget' =>
    array (
      'field_staff_category' =>
      array (
        'value' => '',
      ),
    ),
    'group' => 'group_staff_details',
    'required' => 1,
    'multiple' => '0',
    'text_processing' => '0',
    'max_length' => '',
    'allowed_values' => 'Administrative
Mental Health
Nursing
Primary Care
',
    'allowed_values_php' => '',
    'op' => 'Save field settings',
    'module' => 'text',
    'widget_module' => 'optionwidgets',
    'columns' =>
    array (
      'value' =>
      array (
        'type' => 'text',
        'size' => 'big',
        'not null' => false,
        'sortable' => true,
        'views' => true,
      ),
    ),
    'display_settings' =>
    array (
      'weight' => '4',
      'parent' => 'group_staff_details',
      'label' =>
      array (
        'format' => 'inline',
      ),
      'teaser' =>
      array (
        'format' => 'default',
        'exclude' => 1,
      ),
      'full' =>
      array (
        'format' => 'default',
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
  2 =>
  array (
    'label' => 'Title',
    'field_name' => 'field_staff_title',
    'type' => 'text',
    'widget_type' => 'text_textfield',
    'change' => 'Change basic information',
    'weight' => '5',
    'rows' => 5,
    'size' => '60',
    'description' => '',
    'default_value' =>
    array (
      0 =>
      array (
        'value' => '',
        '_error_element' => 'default_value_widget][field_staff_title][0][value',
      ),
    ),
    'default_value_php' => '',
    'default_value_widget' =>
    array (
      'field_staff_title' =>
      array (
        0 =>
        array (
          'value' => '',
          '_error_element' => 'default_value_widget][field_staff_title][0][value',
        ),
      ),
    ),
    'group' => 'group_staff_details',
    'required' => 0,
    'multiple' => '0',
    'text_processing' => '0',
    'max_length' => '',
    'allowed_values' => '',
    'allowed_values_php' => '',
    'op' => 'Save field settings',
    'module' => 'text',
    'widget_module' => 'text',
    'columns' =>
    array (
      'value' =>
      array (
        'type' => 'text',
        'size' => 'big',
        'not null' => false,
        'sortable' => true,
        'views' => true,
      ),
    ),
    'display_settings' =>
    array (
      'weight' => '5',
      'parent' => 'group_staff_details',
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
  3 =>
  array (
    'label' => 'Degree',
    'field_name' => 'field_staff_degree',
    'type' => 'text',
    'widget_type' => 'text_textfield',
    'change' => 'Change basic information',
    'weight' => '6',
    'rows' => 5,
    'size' => '60',
    'description' => '',
    'default_value' =>
    array (
      0 =>
      array (
        'value' => '',
        '_error_element' => 'default_value_widget][field_staff_degree][0][value',
      ),
    ),
    'default_value_php' => '',
    'default_value_widget' =>
    array (
      'field_staff_degree' =>
      array (
        0 =>
        array (
          'value' => '',
          '_error_element' => 'default_value_widget][field_staff_degree][0][value',
        ),
      ),
    ),
    'group' => 'group_staff_details',
    'required' => 0,
    'multiple' => '1',
    'text_processing' => '0',
    'max_length' => '',
    'allowed_values' => '',
    'allowed_values_php' => '',
    'op' => 'Save field settings',
    'module' => 'text',
    'widget_module' => 'text',
    'columns' =>
    array (
      'value' =>
      array (
        'type' => 'text',
        'size' => 'big',
        'not null' => false,
        'sortable' => true,
        'views' => true,
      ),
    ),
    'display_settings' =>
    array (
      'weight' => '6',
      'parent' => 'group_staff_details',
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
  4 =>
  array (
    'label' => 'Weight',
    'field_name' => 'field_weight',
    'type' => 'text',
    'widget_type' => 'optionwidgets_select',
    'change' => 'Change basic information',
    'weight' => '7',
    'description' => 'In the group pages, the heavier items will sink and the lighter items will be positioned nearer the top.',
    'default_value' =>
    array (
      0 =>
      array (
        'value' => '0',
      ),
    ),
    'default_value_php' => '',
    'default_value_widget' =>
    array (
      'field_weight' =>
      array (
        'value' => '0',
      ),
    ),
    'group' => 'group_staff_details',
    'required' => 1,
    'multiple' => '0',
    'text_processing' => '0',
    'max_length' => '',
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
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50',
    'allowed_values_php' => '',
    'op' => 'Save field settings',
    'module' => 'text',
    'widget_module' => 'optionwidgets',
    'columns' =>
    array (
      'value' =>
      array (
        'type' => 'text',
        'size' => 'big',
        'not null' => false,
        'sortable' => true,
        'views' => true,
      ),
    ),
    'display_settings' =>
    array (
      'weight' => '7',
      'parent' => 'group_staff_details',
      'label' =>
      array (
        'format' => 'above',
      ),
      'teaser' =>
      array (
        'format' => 'default',
        'exclude' => 1,
      ),
      'full' =>
      array (
        'format' => 'default',
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
  'body_field' => '-2',
  'menu' => '-1',
  'print' => '0',
);
