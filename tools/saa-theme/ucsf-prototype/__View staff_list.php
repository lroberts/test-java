<?php
//  August 02, 2009 11:20:08 PM

$view = new view;
$view->name = 'staff_list';
$view->description = 'List of staff';
$view->tag = '';
$view->view_php = '';
$view->base_table = 'node';
$view->is_cacheable = FALSE;
$view->api_version = 2;
$view->disabled = FALSE; /* Edit this to true to make a default view disabled initially */
$handler = $view->new_display('default', 'Defaults', 'default');
$handler->override_option('sorts', array(
  'field_weight_value' => array(
    'order' => 'ASC',
    'delta' => -1,
    'id' => 'field_weight_value',
    'table' => 'node_data_field_weight',
    'field' => 'field_weight_value',
    'relationship' => 'none',
  ),
));
$handler->override_option('arguments', array(
  'field_staff_category_value' => array(
    'default_action' => 'summary asc',
    'style_plugin' => 'default_summary',
    'style_options' => array(
      'count' => 0,
      'override' => 0,
      'items_per_page' => '25',
    ),
    'wildcard' => 'all',
    'wildcard_substitution' => 'All',
    'title' => '%1',
    'breadcrumb' => '',
    'default_argument_type' => 'fixed',
    'default_argument' => '',
    'validate_type' => 'none',
    'validate_fail' => 'not found',
    'add_table' => 0,
    'require_value' => 0,
    'reduce_duplicates' => 0,
    'id' => 'field_staff_category_value',
    'table' => 'node_data_field_staff_category',
    'field' => 'field_staff_category_value',
    'validate_user_argument_type' => 'uid',
    'validate_user_roles' => array(
      '2' => 0,
    ),
    'relationship' => 'none',
    'default_options_div_prefix' => '',
    'default_argument_user' => 0,
    'default_argument_fixed' => '',
    'default_argument_php' => '',
    'validate_argument_node_type' => array(
      'poll' => 0,
      'faq' => 0,
      'box' => 0,
      'page' => 0,
      'quicklinks' => 0,
      'ucsf_event' => 0,
      'ucsf_staff' => 0,
    ),
    'validate_argument_node_access' => 0,
    'validate_argument_nid_type' => 'nid',
    'validate_argument_vocabulary' => array(),
    'validate_argument_type' => 'tid',
    'validate_argument_transform' => 0,
    'validate_user_restrict_roles' => 0,
    'validate_argument_signup_status' => 'any',
    'validate_argument_signup_node_access' => 0,
    'validate_argument_php' => '',
  ),
));
$handler->override_option('filters', array(
  'status' => array(
    'operator' => '=',
    'value' => '1',
    'group' => '0',
    'exposed' => FALSE,
    'expose' => array(
      'operator' => FALSE,
      'label' => '',
    ),
    'id' => 'status',
    'table' => 'node',
    'field' => 'status',
    'relationship' => 'none',
  ),
  'type' => array(
    'operator' => 'in',
    'value' => array(
      'ucsf_staff' => 'ucsf_staff',
    ),
    'group' => '0',
    'exposed' => FALSE,
    'expose' => array(
      'operator' => FALSE,
      'label' => '',
    ),
    'id' => 'type',
    'table' => 'node',
    'field' => 'type',
    'relationship' => 'none',
  ),
));
$handler->override_option('access', array(
  'type' => 'none',
));
$handler->override_option('cache', array(
  'type' => 'none',
));
$handler->override_option('title', 'Staff Profiles');
$handler->override_option('header_format', '2');
$handler->override_option('header_empty', 0);
$handler->override_option('use_pager', 'mini');
$handler->override_option('row_plugin', 'node');
$handler->override_option('row_options', array(
  'relationship' => 'none',
  'build_mode' => 'full',
  'links' => 1,
  'comments' => 0,
));
$handler = $view->new_display('page', 'Page', 'page_1');
$handler->override_option('path', 'healthcare-services/primary-care/staff-profiles');
$handler->override_option('menu', array(
  'type' => 'normal',
  'title' => 'Staff Profiles',
  'description' => '',
  'weight' => '0',
  'name' => 'primary-links',
));
$handler->override_option('tab_options', array(
  'type' => 'none',
  'title' => '',
  'description' => '',
  'weight' => 0,
));
