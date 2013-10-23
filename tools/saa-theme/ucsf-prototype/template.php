<?php
// $Id: template.php,v 1.1.2.10 2009/02/13 08:22:38 jwolf Exp $

/**
 * Initialize theme settings
 */
if (is_null(theme_get_setting('user_notverified_display')) || theme_get_setting('rebuild_registry')) {

  // Auto-rebuild the theme registry during theme development.
  if(theme_get_setting('rebuild_registry')) {
    drupal_set_message(t('The theme registry has been rebuilt. <a href="!link">Turn off</a> this feature on production websites.', array('!link' => url('admin/build/themes/settings/' . $GLOBALS['theme']))), 'warning');
  }

  global $theme_key;

  // Get node types
  $node_types = node_get_types('names');

  /**
   * The default values for the theme variables. Make sure $defaults exactly
   * matches the $defaults in the theme-settings.php file.
   */
  $defaults = array(
    'user_notverified_display'              => 1,
    'breadcrumb_display'                    => 0,
    'search_snippet'                        => 1,
    'search_info_type'                      => 1,
    'search_info_user'                      => 1,
    'search_info_date'                      => 1,
    'search_info_comment'                   => 1,
    'search_info_upload'                    => 1,
    'mission_statement_pages'               => 'home',
    'front_page_title_display'              => 'title_slogan',
    'page_title_display_custom'             => '',
    'other_page_title_display'              => 'ptitle_slogan',
    'other_page_title_display_custom'       => '',
    'configurable_separator'                => ' | ',
    'meta_keywords'                         => '',
    'meta_description'                      => '',
    'taxonomy_display_default'              => 'only',
    'taxonomy_format_default'               => 'vocab',
    'taxonomy_enable_content_type'          => 0,
    'submitted_by_author_default'           => 1,
    'submitted_by_date_default'             => 1,
    'submitted_by_enable_content_type'      => 0,
    'readmore_default'                      => t('Read more'),
    'readmore_title_default'                => t('Read the rest of this posting.'),
    'readmore_prefix_default'               => '',
    'readmore_suffix_default'               => '',
    'readmore_enable_content_type'          => 0,
    'comment_singular_default'              => t('1 comment'),
    'comment_plural_default'                => t('@count comments'),
    'comment_title_default'                 => t('Jump to the first comment of this posting.'),
    'comment_prefix_default'                => '',
    'comment_suffix_default'                => '',
    'comment_new_singular_default'          => t('1 new comment'),
    'comment_new_plural_default'            => t('@count new comments'),
    'comment_new_title_default'             => t('Jump to the first new comment of this posting.'),
    'comment_new_prefix_default'            => '',
    'comment_new_suffix_default'            => '',
    'comment_add_default'                   => t('Add new comment'),
    'comment_add_title_default'             => t('Add a new comment to this page.'),
    'comment_add_prefix_default'            => '',
    'comment_add_suffix_default'            => '',
    'comment_node_default'                  => t('Add new comment'),
    'comment_node_title_default'            => t('Share your thoughts and opinions related to this posting.'),
    'comment_node_prefix_default'           => '',
    'comment_node_suffix_default'           => '',
    'comment_enable_content_type'           => 0,
    'rebuild_registry'                      => 0,
  );

  // Make the default content-type settings the same as the default theme settings,
  // so we can tell if content-type-specific settings have been altered.
  $defaults = array_merge($defaults, theme_get_settings());

  // Set the default values for content-type-specific settings
  foreach ($node_types as $type => $name) {
    $defaults["taxonomy_display_{$type}"]         = $defaults['taxonomy_display_default'];
    $defaults["taxonomy_format_{$type}"]          = $defaults['taxonomy_format_default'];
    $defaults["submitted_by_author_{$type}"]      = $defaults['submitted_by_author_default'];
    $defaults["submitted_by_date_{$type}"]        = $defaults['submitted_by_date_default'];
    $defaults["readmore_{$type}"]                 = $defaults['readmore_default'];
    $defaults["readmore_title_{$type}"]           = $defaults['readmore_title_default'];
    $defaults["readmore_prefix_{$type}"]          = $defaults['readmore_prefix_default'];
    $defaults["readmore_suffix_{$type}"]          = $defaults['readmore_suffix_default'];
    $defaults["comment_singular_{$type}"]         = $defaults['comment_singular_default'];
    $defaults["comment_plural_{$type}"]           = $defaults['comment_plural_default'];
    $defaults["comment_title_{$type}"]            = $defaults['comment_title_default'];
    $defaults["comment_prefix_{$type}"]           = $defaults['comment_prefix_default'];
    $defaults["comment_suffix_{$type}"]           = $defaults['comment_suffix_default'];
    $defaults["comment_new_singular_{$type}"]     = $defaults['comment_new_singular_default'];
    $defaults["comment_new_plural_{$type}"]       = $defaults['comment_new_plural_default'];
    $defaults["comment_new_title_{$type}"]        = $defaults['comment_new_title_default'];
    $defaults["comment_new_prefix_{$type}"]       = $defaults['comment_new_prefix_default'];
    $defaults["comment_new_suffix_{$type}"]       = $defaults['comment_new_suffix_default'];
    $defaults["comment_add_{$type}"]              = $defaults['comment_add_default'];
    $defaults["comment_add_title_{$type}"]        = $defaults['comment_add_title_default'];
    $defaults["comment_add_prefix_{$type}"]       = $defaults['comment_add_prefix_default'];
    $defaults["comment_add_suffix_{$type}"]       = $defaults['comment_add_suffix_default'];
    $defaults["comment_node_{$type}"]             = $defaults['comment_node_default'];
    $defaults["comment_node_title_{$type}"]       = $defaults['comment_node_title_default'];
    $defaults["comment_node_prefix_{$type}"]      = $defaults['comment_node_prefix_default'];
    $defaults["comment_node_suffix_{$type}"]      = $defaults['comment_node_suffix_default'];
  }

  // Get default theme settings.
  $settings = theme_get_settings($theme_key);

  // If content type-specifc settings are not enabled, reset the values
  if (!$settings['readmore_enable_content_type']) {
    foreach ($node_types as $type => $name) {
      $settings["readmore_{$type}"]                    = $settings['readmore_default'];
      $settings["readmore_title_{$type}"]              = $settings['readmore_title_default'];
      $settings["readmore_prefix_{$type}"]             = $settings['readmore_prefix_default'];
      $settings["readmore_suffix_{$type}"]             = $settings['readmore_suffix_default'];
    }
  }
  if (!$settings['comment_enable_content_type']) {
    foreach ($node_types as $type => $name) {
      $defaults["comment_singular_{$type}"]         = $defaults['comment_singular_default'];
      $defaults["comment_plural_{$type}"]           = $defaults['comment_plural_default'];
      $defaults["comment_title_{$type}"]            = $defaults['comment_title_default'];
      $defaults["comment_prefix_{$type}"]           = $defaults['comment_prefix_default'];
      $defaults["comment_suffix_{$type}"]           = $defaults['comment_suffix_default'];
      $defaults["comment_new_singular_{$type}"]     = $defaults['comment_new_singular_default'];
      $defaults["comment_new_plural_{$type}"]       = $defaults['comment_new_plural_default'];
      $defaults["comment_new_title_{$type}"]        = $defaults['comment_new_title_default'];
      $defaults["comment_new_prefix_{$type}"]       = $defaults['comment_new_prefix_default'];
      $defaults["comment_new_suffix_{$type}"]       = $defaults['comment_new_suffix_default'];
      $defaults["comment_add_{$type}"]              = $defaults['comment_add_default'];
      $defaults["comment_add_title_{$type}"]        = $defaults['comment_add_title_default'];
      $defaults["comment_add_prefix_{$type}"]       = $defaults['comment_add_prefix_default'];
      $defaults["comment_add_suffix_{$type}"]       = $defaults['comment_add_suffix_default'];
      $defaults["comment_node_{$type}"]             = $defaults['comment_node_default'];
      $defaults["comment_node_title_{$type}"]       = $defaults['comment_node_title_default'];
      $defaults["comment_node_prefix_{$type}"]      = $defaults['comment_node_prefix_default'];
      $defaults["comment_node_suffix_{$type}"]      = $defaults['comment_node_suffix_default'];
    }
  }

  // Don't save the toggle_node_info_ variables
  if (module_exists('node')) {
    foreach (node_get_types() as $type => $name) {
      unset($settings['toggle_node_info_'. $type]);
    }
  }
  // Save default theme settings
  variable_set(
    str_replace('/', '_', 'theme_'. $theme_key .'_settings'),
    array_merge($defaults, $settings)
  );
  // Force refresh of Drupal internals
  theme_get_setting('', TRUE);
}


/**
 * Modify theme variables
 */
function phptemplate_preprocess(&$vars) {
  global $user;                                            // Get the current user
  global $theme_key;                                       // Get the current theme name
  $vars['is_admin'] = in_array('admin', $user->roles);     // Check for Admin, logged in
  $vars['logged_in'] = ($user->uid > 0) ? TRUE : FALSE;

  $vars['current_theme_name'] = $theme_key;


  global $theme_path;
  global $base_path;
  $vars['current_theme_path'] = $base_path . $theme_path . '/';

try{
  if (module_exists('color')) {
    _color_page_alter($vars);
  }
}catch(Exception $e){
echo 'Exception caught: ',  $e->getMessage(), "\n";
}

}


function phptemplate_preprocess_page(&$vars) {
  // Remove sidebars if disabled
  if (!$vars['show_blocks']) {
    $vars['left'] = '';
    $vars['right'] = '';
  }
  // Build array of helpful body classes
  $body_classes = array();
  $body_classes[] = ($vars['logged_in']) ? 'logged-in' : 'not-logged-in';                                 // Page user is logged in
  $body_classes[] = ($vars['is_front']) ? 'front' : 'not-front';                                          // Page is front page
  if (isset($vars['node'])) {
    $body_classes[] = ($vars['node']) ? 'full-node' : '';                                                   // Page is one full node
    $body_classes[] = (($vars['node']->type == 'forum') || (arg(0) == 'forum')) ? 'forum' : '';             // Page is Forum page
    $body_classes[] = ($vars['node']->type) ? 'node-type-'. $vars['node']->type : '';                       // Page has node-type-x, e.g., node-type-page
  }
  else {
    $body_classes[] = (arg(0) == 'forum') ? 'forum' : '';                                                   // Page is Forum page
  }
  $body_classes[] = (module_exists('panels_page') && (panels_page_get_current())) ? 'panels' : '';        // Page is Panels page

  //$body_classes[] = ($vars['is_front']) ? 'front' : 'not-front'. '-layout-'. ((($vars['home_left']) || ($vars['left'])) ? 'left-content' : 'content') . ((($vars['home_right']) || ($vars['right'])) ? '-right' : '');  // Page sidebars are active

  $f = ($vars['is_front']) ? 'front'        : 'not-front';
  $l = ($vars['left'])     ? 'left-content' : 'content';
  $r = ($vars['right'])    ? '-right'       : '';
  $body_classes[] = $f.'-layout-'.$l.$r;

  $body_classes[] = ($vars['left'])  ? 'sidebar-left'  : '';
  $body_classes[] = ($vars['right']) ? 'sidebar-right' : '';
  $body_classes[] = (($vars['left'] && $vars['right'])) ? 'two-sidebars' : '';

  $body_classes = array_filter($body_classes);                                                            // Remove empty elements
  $vars['body_classes'] = implode(' ', $body_classes);                                                    // Create class list separated by spaces

  // body ID
  $body_id = array();
  $body_id[] = $vars['current_theme_name'] .'-'. $f;

  $body_id = array_filter($body_id);
  $vars['body_id'] = implode(' ', $body_id);

  //Current theme path
  $current_theme_path = $vars['current_theme_path'];




  // Generate menu tree from source of primary links
  $vars['primary_links_tree'] = menu_tree(variable_get('menu_primary_links_source', 'primary-links'));

  // TNT THEME SETTINGS SECTION
  // Display mission statement on all pages
  if (theme_get_setting('mission_statement_pages') == 'all') {
    $vars['mission'] = theme_get_setting('mission', false);
  }

  // Hide breadcrumb on all pages
  if (theme_get_setting('breadcrumb_display') == 0) {
    $vars['breadcrumb'] = '';
  }

  // Set site title, slogan, mission, page title & separator (unless using Page Title module)
  if (!module_exists('page_title')) {
    $title = t(variable_get('site_name', ''));
    $slogan = t(variable_get('site_slogan', ''));
    $mission = t(variable_get('site_mission', ''));
    $page_title = t(drupal_get_title());
    $title_separator = theme_get_setting('configurable_separator');
    if (drupal_is_front_page()) {                                                // Front page title settings
      switch (theme_get_setting('front_page_title_display')) {
        case 'title_slogan':
          $vars['head_title'] = drupal_set_title($title . $title_separator . $slogan);
          break;
        case 'slogan_title':
          $vars['head_title'] = drupal_set_title($slogan . $title_separator . $title);
          break;
        case 'title_mission':
          $vars['head_title'] = drupal_set_title($title . $title_separator . $mission);
          break;
        case 'custom':
          if (theme_get_setting('page_title_display_custom') !== '') {
            $vars['head_title'] = drupal_set_title(t(theme_get_setting('page_title_display_custom')));
          }
      }
    }
    else {                                                                       // Non-front page title settings
      switch (theme_get_setting('other_page_title_display')) {
        case 'ptitle_slogan':
          $vars['head_title'] = drupal_set_title($page_title . $title_separator . $slogan);
          break;
        case 'ptitle_stitle':
          $vars['head_title'] = drupal_set_title($page_title . $title_separator . $title);
          break;
        case 'ptitle_smission':
          $vars['head_title'] = drupal_set_title($page_title . $title_separator . $mission);
          break;
        case 'ptitle_custom':
          if (theme_get_setting('other_page_title_display_custom') !== '') {
            $vars['head_title'] = drupal_set_title($page_title . $title_separator . t(theme_get_setting('other_page_title_display_custom')));
          }
          break;
        case 'custom':
          if (theme_get_setting('other_page_title_display_custom') !== '') {
            $vars['head_title'] = drupal_set_title(t(theme_get_setting('other_page_title_display_custom')));
          }
      }
    }
    $vars['head_title'] = strip_tags($vars['head_title']);                       // Remove any potential html tags
  }

  // Set meta keywords and description (unless using Meta tags module)
  if (!module_exists('nodewords')) {
    if (theme_get_setting('meta_keywords') !== '') {
      $keywords = '<meta name="keywords" content="'. theme_get_setting('meta_keywords') .'" />';
      $vars['head'] .= $keywords ."\n";
    }
    if (theme_get_setting('meta_description') !== '') {
      $keywords = '<meta name="description" content="'. theme_get_setting('meta_description') .'" />';
      $vars['head'] .= $keywords ."\n";
    }
  }

  if (drupal_is_front_page()) {
    $vars['closure'] .= '';
  }

  // NEW: 15sept2011 remove prototype incompatible js from modules
  $scripts = drupal_add_js();
  /*
  unset($scripts['module']['sites/all/modules/lightbox2/js/lightbox.js']);
  unset($scripts['module']['sites/all/modules/views_accordion/views-accordion.js']);
  unset($scripts['module']['sites/all/modules/admin_menu/admin_menu.js']);
  unset($scripts['module']['sites/all/modules/google_analytics/googleanalytics.js']);
  // only appears on some pages
  unset($scripts['module']['sites/all/modules/fckeditor/fckeditor.utils.js']);
  unset($scripts['module']['misc/textarea.js']);
  unset($scripts['module']['misc/collapse.js']);
  unset($scripts['module']['misc/tableheader.js']);
  unset($scripts['module']['modules/block/block.js']);
  */
  //  echo "<h2>".print_r($scripts['module']['sites/all/modules/lightbox2/js/lightbox.js'])."</h2>";
  $sub_js = "sites/saa-webdev.ucsf.edu/themes/ucsf-prototype/js-prototype";
  /*
  swap_key($scripts['core'], 
	   'misc/jquery.js',
	   "$sub_js/jquery.js");
  */
  swap_key($scripts['core'], 
	   'misc/drupal.js',
	   "$sub_js/drupal.js");
    
  swap_key($scripts['core'], 
	   'misc/tabledrag.js',
	   "$sub_js/tabledrag.js");

  swap_key($scripts['module'], 
	   'sites/all/modules/lightbox2/js/lightbox.js',
	   "$sub_js/lightbox.js");


  swap_key($scripts['module'], 
	   'sites/all/modules/views_accordion/views-accordion.js',
	   "$sub_js/views-accordion.js");

  swap_key($scripts['module'], 
	   'sites/all/modules/admin_menu/admin_menu.js',
	   "$sub_js/admin_menu.js");

  swap_key($scripts['module'], 
	   'sites/all/modules/google_analytics/googleanalytics.js',
	   "$sub_js/googleanalytics.js");


  // these only appear on some pages
  swap_key($scripts['module'], 
	   'sites/all/modules/fckeditor/fckeditor.utils.js',
	   "$sub_js/fckeditor.utils.js");

  swap_key($scripts['module'], 
	   'misc/textarea.js',
	   "$sub_js/textarea.js");

  swap_key($scripts['module'], 
	   'misc/collapse.js',
	   "$sub_js/collapse.js");

  swap_key($scripts['module'], 
	   'misc/tableheader.js',
	   "$sub_js/tableheader.js");


  swap_key($scripts['module'], 
	   'modules/block/block.js',
	   "$sub_js/block.js");

  swap_key($scripts['module'], 
	   'modules/color/color.js',
	   "$sub_js/color.js");


  swap_key($scripts['module'], 
	   'misc/farbtastic/farbtastic.js',
	   "$sub_js/farbtastic.js");


  /*
  echo "<h2>";
  print_r($scripts);
  echo "</h2>";
  */

  //  unset($scripts['module']['sites/all/modules/lightbox2/js/lightbox.js?Z']);
  $vars['scripts'] = drupal_get_js('header', $scripts);

  // $css = drupal_add_css();
  //unset($css['all']['module']['modules/node/node.css']);   
  //$vars['styles'] = drupal_get_css($css);


}

    /**
     * This function takes an associative array an swaps the given
     * array _key_ for a new value, maintaining both the order of the
     *  associative array AND the value pointed to by the old/new key.
     *
     * @param $array an associatve array
     * @param $needle the array key to be swapped
     * @param $replacement the replacement key
     */
    function swap_key(&$array, $needle, $replacement){

    $keys=array_keys($array); // extract keys
    $values=array_values($array); // extract values
    foreach ($keys as $i=>$key) {
      if($key == $needle){
	$keys[$i]=$replacement; // replace
      }else{
	// do nothing
      }
    }
    $array=array_combine($keys,$values); // map back together

  }

/*
function phptemplate_preprocess_block(&$vars) {
  // Add regions with rounded blocks (e.g., left, right) to $rounded_regions array
  // $rounded_regions = array('left','right');
  // $vars['rounded_block'] = (in_array($vars['block']->region, $rounded_regions)) ? TRUE : FALSE;

  if ( user_access('administer blocks') ) {
    if (function_exists('drupal_get_path')){
      $tpath = drupal_get_path('theme', 'ucsf_base');
      drupal_add_js($tpath . '/block_edit.js' );
      drupal_add_css($tpath . '/block_edit.css' );
    }
  }

}
*/

function phptemplate_preprocess_node(&$vars) {
  // Build array of handy node classes
  $node_classes = array();
  $node_classes[] = $vars['zebra'];                                      // Node is odd or even
  $node_classes[] = (!$vars['node']->status) ? 'node-unpublished' : '';  // Node is unpublished
  $node_classes[] = ($vars['sticky']) ? 'sticky' : '';                   // Node is sticky
  $node_classes[] = (isset($vars['node']->teaser)) ? 'teaser' : 'full-node';    // Node is teaser or full-node
  $node_classes[] = 'node-type-'. $vars['node']->type;                   // Node is type-x, e.g., node-type-page
  $node_classes = array_filter($node_classes);                           // Remove empty elements
  $vars['node_classes'] = implode(' ', $node_classes);                   // Implode class list with spaces

  // Add node_bottom region content
  $vars['node_bottom'] = theme('blocks', 'node_bottom');

  // Node Theme Settings

  // Date & author
  $date = t('Posted ') . format_date($vars['node']->created, 'medium');                 // Format date as small, medium, or large
  $author = theme('username', $vars['node']);
  $author_only_separator = t('Posted by ');
  $author_date_separator = t(' by ');
  $submitted_by_content_type = (theme_get_setting('submitted_by_enable_content_type') == 1) ? $vars['node']->type : 'default';
  $date_setting = (theme_get_setting('submitted_by_date_'. $submitted_by_content_type) == 1);
  $author_setting = (theme_get_setting('submitted_by_author_'. $submitted_by_content_type) == 1);
  $author_separator = ($date_setting) ? $author_date_separator : $author_only_separator;
  $date_author = ($date_setting) ? $date : '';
  $date_author .= ($author_setting) ? $author_separator . $author : '';
  $vars['submitted'] = $date_author;

  // Taxonomy
  $taxonomy_content_type = (theme_get_setting('taxonomy_enable_content_type') == 1) ? $vars['node']->type : 'default';
  $taxonomy_display = theme_get_setting('taxonomy_display_'. $taxonomy_content_type);
  $taxonomy_format = theme_get_setting('taxonomy_format_'. $taxonomy_content_type);
  if ((module_exists('taxonomy')) && ($taxonomy_display == 'all' || ($taxonomy_display == 'only' && $vars['page']))) {
    $vocabularies = taxonomy_get_vocabularies($vars['node']->type);
    $output = '';
    $vocab_delimiter = '';
    foreach ($vocabularies as $vocabulary) {
      if (theme_get_setting('taxonomy_vocab_display_'. $taxonomy_content_type .'_'. $vocabulary->vid) == 1) {
        $terms = taxonomy_node_get_terms_by_vocabulary($vars['node'], $vocabulary->vid);
        if ($terms) {
          $output .= ($taxonomy_format == 'vocab') ? '<li class="vocab vocab-'. $vocabulary->vid .'"><span class="vocab-name">'. $vocabulary->name .':</span> <ul class="vocab-list">' : '';
          $links = array();
          foreach ($terms as $term) {
            $links[] = '<li class="vocab-term">'. l($term->name, taxonomy_term_path($term), array('attributes' => array('rel' => 'tag', 'title' => strip_tags($term->description)))) .'</li>';
          }
          if ($taxonomy_format == 'list') {
            $output .= $vocab_delimiter;    // Add comma between vocabularies
            //$vocab_delimiter = ', ';        // Use a comma delimiter after first displayed vocabulary
          }
          $output .= implode(", ", $links);
          $output .= ($taxonomy_format == 'vocab') ? '</ul></li>' : '';
        }
      }
    }
    if ($output != '') {
      $output = '<ul class="taxonomy">'. $output .'</ul>';
    }
    $vars['terms'] = $output;
  }
  else {
    $vars['terms'] = '';
  }

  // Node Links
  if (isset($vars['node']->links['node_read_more'])) {
    $node_content_type = (theme_get_setting('readmore_enable_content_type') == 1) ? $vars['node']->type : 'default';
    $vars['node']->links['node_read_more'] = array(
      'title' => _themesettings_link(
      theme_get_setting('readmore_prefix_'. $node_content_type),
      theme_get_setting('readmore_suffix_'. $node_content_type),
      theme_get_setting('readmore_'. $node_content_type),
      'node/'. $vars['node']->nid,
      array(
        'attributes' => array('title' => theme_get_setting('readmore_title_'. $node_content_type)),
        'query' => NULL, 'fragment' => NULL, 'absolute' => FALSE, 'html' => TRUE
        )
      ),
      'attributes' => array('class' => 'readmore-item'),
      'html' => TRUE,
    );
  }
  if (isset($vars['node']->links['comment_add'])) {
    $node_content_type = (theme_get_setting('comment_enable_content_type') == 1) ? $vars['node']->type : 'default';
    if ($vars['teaser']) {
      $vars['node']->links['comment_add'] = array(
        'title' => _themesettings_link(
        theme_get_setting('comment_add_prefix_'. $node_content_type),
        theme_get_setting('comment_add_suffix_'. $node_content_type),
        theme_get_setting('comment_add_'. $node_content_type),
        "comment/reply/".$vars['node']->nid,
        array(
          'attributes' => array('title' => theme_get_setting('comment_add_title_'. $node_content_type)),
          'query' => NULL, 'fragment' => 'comment-form', 'absolute' => FALSE, 'html' => TRUE
          )
        ),
        'attributes' => array('class' => 'comment-add-item'),
        'html' => TRUE,
      );
    }
    else {
      $vars['node']->links['comment_add'] = array(
        'title' => _themesettings_link(
        theme_get_setting('comment_node_prefix_'. $node_content_type),
        theme_get_setting('comment_node_suffix_'. $node_content_type),
        theme_get_setting('comment_node_'. $node_content_type),
        "comment/reply/".$vars['node']->nid,
        array(
          'attributes' => array('title' => theme_get_setting('comment_node_title_'. $node_content_type)),
          'query' => NULL, 'fragment' => 'comment-form', 'absolute' => FALSE, 'html' => TRUE
          )
        ),
        'attributes' => array('class' => 'comment-node-item'),
        'html' => TRUE,
      );
    }
  }
  if (isset($vars['node']->links['comment_new_comments'])) {
    $node_content_type = (theme_get_setting('comment_enable_content_type') == 1) ? $vars['node']->type : 'default';
    $vars['node']->links['comment_new_comments'] = array(
      'title' => _themesettings_link(
        theme_get_setting('comment_new_prefix_'. $node_content_type),
        theme_get_setting('comment_new_suffix_'. $node_content_type),
        format_plural(
          comment_num_new($vars['node']->nid),
          theme_get_setting('comment_new_singular_'. $node_content_type),
          theme_get_setting('comment_new_plural_'. $node_content_type)
        ),
        "node/".$vars['node']->nid,
        array(
          'attributes' => array('title' => theme_get_setting('comment_new_title_'. $node_content_type)),
          'query' => NULL, 'fragment' => 'new', 'absolute' => FALSE, 'html' => TRUE
        )
      ),
      'attributes' => array('class' => 'comment-new-item'),
      'html' => TRUE,
    );
  }
  if (isset($vars['node']->links['comment_comments'])) {
    $node_content_type = (theme_get_setting('comment_enable_content_type') == 1) ? $vars['node']->type : 'default';
    $vars['node']->links['comment_comments'] = array(
      'title' => _themesettings_link(
        theme_get_setting('comment_prefix_'. $node_content_type),
        theme_get_setting('comment_suffix_'. $node_content_type),
        format_plural(
          comment_num_all($vars['node']->nid),
          theme_get_setting('comment_singular_'. $node_content_type),
          theme_get_setting('comment_plural_'. $node_content_type)
        ),
        "node/".$vars['node']->nid,
        array(
          'attributes' => array('title' => theme_get_setting('comment_title_'. $node_content_type)),
          'query' => NULL, 'fragment' => 'comments', 'absolute' => FALSE, 'html' => TRUE
        )
      ),
      'attributes' => array('class' => 'comment-item'),
      'html' => TRUE,
    );
  }
  $vars['links'] = theme('links', $vars['node']->links, array('class' => 'links inline'));
}


function phptemplate_preprocess_comment(&$vars) {
  global $user;
  // Build array of handy comment classes
  $comment_classes = array();
  static $comment_odd = TRUE;                                                                             // Comment is odd or even
  $comment_classes[] = $comment_odd ? 'odd' : 'even';
  $comment_odd = !$comment_odd;
  $comment_classes[] = ($vars['comment']->status == COMMENT_NOT_PUBLISHED) ? 'comment-unpublished' : '';  // Comment is unpublished
  $comment_classes[] = ($vars['comment']->new) ? 'comment-new' : '';                                      // Comment is new
  $comment_classes[] = ($vars['comment']->uid == 0) ? 'comment-by-anon' : '';                             // Comment is by anonymous user
  $comment_classes[] = ($user->uid && $vars['comment']->uid == $user->uid) ? 'comment-mine' : '';         // Comment is by current user
  $node = node_load($vars['comment']->nid);                                                               // Comment is by node author
  $vars['author_comment'] = ($vars['comment']->uid == $node->uid) ? TRUE : FALSE;
  $comment_classes[] = ($vars['author_comment']) ? 'comment-by-author' : '';
  $comment_classes = array_filter($comment_classes);                                                      // Remove empty elements
  $vars['comment_classes'] = implode(' ', $comment_classes);                                              // Create class list separated by spaces
  // Date & author
  $submitted_by = t('by ') .'<span class="comment-name">'.  theme('username', $vars['comment']) .'</span>';
  $submitted_by .= t(' - ') .'<span class="comment-date">'.  format_date($vars['comment']->timestamp, 'small') .'</span>';     // Format date as small, medium, or large
  $vars['submitted'] = $submitted_by;
}


/**
 * Set defaults for comments display
 * (Requires comment-wrapper.tpl.php file in theme directory)
 */
function phptemplate_preprocess_comment_wrapper(&$vars) {
  $vars['display_mode']  = COMMENT_MODE_FLAT_EXPANDED;
  $vars['display_order'] = COMMENT_ORDER_OLDEST_FIRST;
  $vars['comment_controls_state'] = COMMENT_CONTROLS_HIDDEN;
}


/**
 * Adds a class for the style of view
 * (e.g., node, teaser, list, table, etc.)
 * (Requires views-view.tpl.php file in theme directory)
 */
function phptemplate_preprocess_views_view(&$vars) {
  $vars['css_name'] = $vars['css_name'] .' view-style-'. views_css_safe(strtolower($vars['view']->type));
}


/**
 * Modify search results based on theme settings
 */
function phptemplate_preprocess_search_result(&$variables) {
  static $search_zebra = 'even';
  $search_zebra = ($search_zebra == 'even') ? 'odd' : 'even';
  $variables['search_zebra'] = $search_zebra;

  $result = $variables['result'];
  $variables['url'] = check_url($result['link']);
  $variables['title'] = check_plain($result['title']);

  // Check for existence. User search does not include snippets.
  $variables['snippet'] = '';
  if (isset($result['snippet']) && theme_get_setting('search_snippet')) {
    $variables['snippet'] = $result['snippet'];
  }

  $info = array();
  if (!empty($result['type']) && theme_get_setting('search_info_type')) {
    $info['type'] = check_plain($result['type']);
  }
  if (!empty($result['user']) && theme_get_setting('search_info_user')) {
    $info['user'] = $result['user'];
  }
  if (!empty($result['date']) && theme_get_setting('search_info_date')) {
    $info['date'] = format_date($result['date'], 'small');
  }
  if (isset($result['extra']) && is_array($result['extra'])) {
    // $info = array_merge($info, $result['extra']);  Drupal bug?  [extra] array not keyed with 'comment' & 'upload'
    if (!empty($result['extra'][0]) && theme_get_setting('search_info_comment')) {
      $info['comment'] = $result['extra'][0];
    }
    if (!empty($result['extra'][1]) && theme_get_setting('search_info_upload')) {
      $info['upload'] = $result['extra'][1];
    }
  }

  // Provide separated and grouped meta information.
  $variables['info_split'] = $info;
  $variables['info'] = implode(' - ', $info);

  // Provide alternate search result template.
  $variables['template_files'][] = 'search-result-'. $variables['type'];
}


/**
 * Override username theming to display/hide 'not verified' text
 */
function phptemplate_username($object) {
  if ($object->uid && $object->name) {
    // Shorten the name when it is too long or it will break many tables.
    if (drupal_strlen($object->name) > 20) {
      $name = drupal_substr($object->name, 0, 15) .'...';
    }
    else {
      $name = $object->name;
    }
    if (user_access('access user profiles')) {
      $output = l($name, 'user/'. $object->uid, array('attributes' => array('title' => t('View user profile.'))));
    }
    else {
      $output = check_plain($name);
    }
  }
  else if ($object->name) {
    // Sometimes modules display content composed by people who are
    // not registered members of the site (e.g. mailing list or news
    // aggregator modules). This clause enables modules to display
    // the true author of the content.
    if (!empty($object->homepage)) {
      $output = l($object->name, $object->homepage, array('attributes' => array('rel' => 'nofollow')));
    }
    else {
      $output = check_plain($object->name);
    }
    // Display or hide 'not verified' text
    if (theme_get_setting('user_notverified_display') == 1) {
      $output .= ' ('. t('not verified') .')';
    }
  }
  else {
    $output = variable_get('anonymous', t('Anonymous'));
  }
  return $output;
}


/**
 * Set default form file input size
 */
function phptemplate_file($element) {
  $element['#size'] = 40;
  return theme_file($element);
}


/**
 * Creates a link with prefix and suffix text
 *
 * @param $prefix
 *   The text to prefix the link.
 * @param $suffix
 *   The text to suffix the link.
 * @param $text
 *   The text to be enclosed with the anchor tag.
 * @param $path
 *   The Drupal path being linked to, such as "admin/content/node". Can be an external
 *   or internal URL.
 *     - If you provide the full URL, it will be considered an
 *   external URL.
 *     - If you provide only the path (e.g. "admin/content/node"), it is considered an
 *   internal link. In this case, it must be a system URL as the url() function
 *   will generate the alias.
 * @param $options
 *   An associative array that contains the following other arrays and values
 *     @param $attributes
 *       An associative array of HTML attributes to apply to the anchor tag.
 *     @param $query
 *       A query string to append to the link.
 *     @param $fragment
 *       A fragment identifier (named anchor) to append to the link.
 *     @param $absolute
 *       Whether to force the output to be an absolute link (beginning with http:).
 *       Useful for links that will be displayed outside the site, such as in an RSS
 *       feed.
 *     @param $html
 *       Whether the title is HTML or not (plain text)
 * @return
 *   an HTML string containing a link to the given path.
 */
function _themesettings_link($prefix, $suffix, $text, $path, $options) {
  return $prefix . (($text) ? l($text, $path, $options) : '') . $suffix;
}

/**
* Website Search: generic site search located on header
*/
function phptemplate_search_theme_form($form) {
  $output = '';

  //$form['search_theme_form']['#title'] = t('Website Search');
  unset($form['search_theme_form']['#title']);
  $form['search_theme_form']['#value'] = t('Keyword Search');

  $form['submit']['#value'] = t(' ');

  $output .= drupal_render($form);

  return  $output;
}

/**
 * Return a multidimensional array of links for a navigation menu.
 *
 * @param $menu_name
 *   The name of the menu.
 * @param $level
 *   Optional, the depth of the menu to be returned.
 * @return
 *   An array of links of the specified menu and level.
 */
function phptemplate_navigation_links($menu_name, $level = 0) {
  // Don't even bother querying the menu table if no menu is specified.
  if (empty($menu_name)) {
    return array();
  }

  // Get the menu hierarchy for the current page.
  $tree_page = menu_tree_page_data($menu_name);
  // Also get the full menu hierarchy.
  $tree_all = menu_tree_all_data($menu_name);

  // Go down the active trail until the right level is reached.
  while ($level-- > 0 && $tree_page) {
    // Loop through the current level's items until we find one that is in trail.
    while ($item = array_shift($tree_page)) {
      if ($item['link']['in_active_trail']) {
        // If the item is in the active trail, we continue in the subtree.
        $tree_page = empty($item['below']) ? array() : $item['below'];
        break;
      }
    }
  }
  return phptemplate_navigation_links_level($tree_page, $tree_all);
}


/**
 * Helper function for phptemplate_navigation_links to recursively create an array of links.
 * (Both trees are required in order to include every menu item and active trail info.)
 */
function phptemplate_navigation_links_level($tree_page, $tree_all) {
  $links = array();
  foreach ($tree_all as $key => $item) {
    $item_page = $tree_page[$key];
    $item_all = $tree_all[$key];
    if (!$item_all['link']['hidden']) {
      $class = '';
      $l = $item_all['link']['localized_options'];
      $l['href'] = $item_all['link']['href'];
      $l['title'] = $item_all['link']['title'];
      if ($item_page['link']['in_active_trail']) {
        if ($l['href'] != $_GET['q']) {
          $class = ' active-trail';
        }
      }
      if ($item_all['below']) {
        $l['children'] = phptemplate_navigation_links_level($item_page['below'], $item_all['below']);
      }
      // Keyed with the unique mlid to generate classes in theme_links().
      $links['menu-'. $item_all['link']['mlid'] . $class] = $l;
    }
  }
  return $links;
}


/**
 * Helper function to retrieve the primary links using phptemplate_navigation_links().
 */
function phptemplate_primary_links() {
  return phptemplate_navigation_links(variable_get('menu_primary_links_source', 'primary-links'));
}


/**
 * Return a themed set of links. (Extended to support multidimensional arrays of links.)
 *
 * @param $links
 *   A keyed array of links to be themed.
 * @param $attributes
 *   A keyed array of attributes
 * @return
 *   A string containing an unordered list of links.
 */
function phptemplate_links($links, $attributes = array('class' => 'links')) {
  $output = '';

  unset($links['blog_usernames_blog']);

  if (count($links) > 0) {
    $output = '<ul'. drupal_attributes($attributes) .'>';

    $num_links = count($links);
    $i = 1;

    foreach ($links as $key => $link) {
      $class = $key;

      // Add first, last and active classes to the list of links to help out themers.
      if ($i == 1) {
        $class .= ' first';
      }
      if ($i == $num_links) {
        $class .= ' last';
      }
      if (isset($link['href']) && ($link['href'] == $_GET['q'] || ($link['href'] == '<front>' && drupal_is_front_page()))) {
        $class .= ' current';
      }
      // Added: if the link has child items, add a haschildren class
      if (isset($link['children'])) {
        $class .= ' haschildren';
      }
      $output .= '<li'. drupal_attributes(array('class' => $class)) .'>';

      if (isset($link['href'])) {
        // Pass in $link as $options, they share the same keys.
        $output .= l($link['title'], $link['href']);
      }
      else if (!empty($link['title'])) {
        // Some links are actually not links, but we wrap these in <span> for adding title and class attributes
        if (empty($link['html'])) {
          $link['title'] = check_plain($link['title']);
        }
        $span_attributes = '';
        if (isset($link['attributes'])) {
          $span_attributes = drupal_attributes($link['attributes']);
        }
        $output .= '<span'. $span_attributes .'>'. $link['title'] .'</span>';
      }

      // Added: if the link has child items, print them out recursively
      if (isset($link['children'])) {
        $output .= theme('links', $link['children'], array('class' =>'sublinks'));
      }

      $i++;
      $output .= "</li>";
    }

    $output .= '</ul>';
  }
  return $output;
}


/**
 * Format the Printer-friendly link
 *
 * @return
 *   array of formatted attributes
 * @ingroup themeable
 */
function phptemplate_print_format_link() {
  $print_html_link_class = 'toolPrint';
  $print_html_new_window = variable_get('print_html_new_window', PRINT_HTML_NEW_WINDOW_DEFAULT);
  $print_html_show_link = variable_get('print_html_show_link', PRINT_HTML_SHOW_LINK_DEFAULT);
  $print_html_link_text = variable_get('print_html_link_text', t('Printer-friendly version'));

  $img = drupal_get_path('module', 'print') .'/icons/print_icon.gif';
  $title = t('Display a printer-friendly version of this page.');
  $class = strip_tags($print_html_link_class);
  $new_window = $print_html_new_window;
  $format = _print_format_link_aux($print_html_show_link, $print_html_link_text, $img);

  return array('text' => $format['text'],
               'html' => $format['html'],
               'attributes' => print_fill_attributes($title, $class, $new_window),
              );
}

/**
 * Format the send by e-mail link
 *
 * @return
 *   array of formatted attributes
 * @ingroup themeable
 */
function phptemplate_print_mail_format_link() {
  $print_mail_link_class  = 'toolMail';
  $print_mail_show_link = variable_get('print_mail_show_link', PRINT_MAIL_SHOW_LINK_DEFAULT);
  $print_mail_link_text = variable_get('print_mail_link_text', t('Send to friend'));

  $img = drupal_get_path('module', 'print') .'/icons/mail_icon.gif';
  $title = t('Send this page by e-mail.');
  $class = strip_tags($print_mail_link_class);
  $new_window = FALSE;
  $format = _print_format_link_aux($print_mail_show_link, $print_mail_link_text, $img);

  return array('text' => $format['text'],
               'html' => $format['html'],
               'attributes' => print_fill_attributes($title, $class, $new_window),
              );
}






// $Id: signup_form.inc,v 1.3.2.2 2009/01/22 19:00:55 dww Exp $


/**
 * @file
 * Site-specific code related to the form when users signup for a node.
 */

/**
 * Return the site-specific custom fields for the signup user form.
 *
 * To customize this for your site, copy this entire function into
 * your theme's template.php file, rename the function to
 * phptemplate_signup_user_form(), and modify to taste.  Feel free to
 * alter any elements in this section, remove them, or add any others.
 *
 * WARNING: If your site allows anonymous signups and you alter the
 * 'Name' field in this function, you will probably have to implement a
 * version of theme_signup_anonymous_username() for your site.
 *
 * In order for the form to be rendered properly and for the custom
 * fields to be fully translatable when printed in other parts of the
 * Signup module (displayed in signup lists, emails, etc), the name of
 * the form element must be $form['signup_form_data']['NameOfDataField'],
 * where NameOfDataField is replaced with the actual name of the data
 * field.  For translation to work, the displayed name of the field
 * (the '#title' property) be the same as the name of the data field,
 * but wrapped in t().  See below for examples.
 *
 * Fieldsets are not currently supported in this form.  Any
 * '#default_value' will be filled in by default when the form is
 * presented to the user.  Any field marked '#required' must be filled
 * in before the user can sign up.
 *
 * If you do not want any additional fields, the function can simply
 * return an empty array: "return array();"
 *
 * @param $node
 *   The fully loaded node object where this signup form is appearing.
 *
 * @return
 *   Array defining the form to present to the user to signup for a node.
 *
 * @see theme_signup_anonymous_username()
 */
function phptemplate_signup_user_form($node) {
  global $user;
  $form = array();

  // If this function is providing any extra fields at all, the following
  // line is required for form form to work -- DO NOT EDIT OR REMOVE.
  $form['signup_form_data']['#tree'] = TRUE;

  $form['signup_form_data']['Name'] = array(
    '#type' => 'textfield',
    '#title' => t('Name'),
    '#size' => 40, '#maxlength' => 64,
    '#required' => TRUE,
  );
  $form['signup_form_data']['Phone'] = array(
    '#type' => 'textfield',
    '#title' => t('Phone'),
    '#size' => 40, '#maxlength' => 64,
  );


  //$options = array('select' => 'Select...');
  $options = array();
  $list    = array();
  $list[]  = $node->field_date;

  foreach ($list[0] as $item) {
    $date1 = $item['value'];
    $date2 = $item['value2'];
    // [value] => 2009-07-20T17:00:00

    if ($date1 == $date2) {
      list($year, $month, $day, $hour, $minute, $second) = split('[-T:]', $date1);

      $timestamp = gmmktime($hour, $minute, $second, $month, $day, $year);

      $options[] = format_date($timestamp, $type='large');

    } else {

      $options[] = t('Spanning dates...');
    }
  }

  $form['signup_form_data']['Notes'] = array(
    '#type' => 'textarea',
    '#title' => t('Notes'),
    '#cols' => 50,
    '#rows' => 5,
    '#required' => FALSE,
  );


  if (count($options)>1):
    $form['signup_form_data']['Date'] = array(
      '#type' => 'select',
      '#title' => t('Date/Time'),
      '#size' => 1,
      '#options' =>  array_combine($options, $options),
      '#required' => TRUE,
    );
  endif;

  // If the user is logged in, fill in their name by default.
  if ($user->uid) {
    $form['signup_form_data']['Name']['#default_value'] = $user->name;
  }

  return $form;
}







/**
 * Returns the value to use for the user name for anonymous signups.
 *
 * WARNING: If you implemented your own version of theme_signup_form_data()
 * that changed or removed the custom 'Name' field and your site
 * allows anonymous signups, you will need to modify this, too.
 *
 * This value is used for the %user_name email token for anonymous users, and
 * also to identify a particular anonymous signup in various places in the UI.
 *
 * @param $form_data
 *   Array of custom signup form values for the current signup.
 * @param $email
 *   E-mail address of the anonymous user who signed up.
 * @return
 *   A string with the proper value for the %user_name email token.
 *
 * @see theme_signup_user_form()
 */
function phptemplate_signup_anonymous_username($form_data, $email) {
  // In some cases, the best you can do is to use the anonymous user's
  // supplied email address, in which case, you should uncomment this:
  //return $email;

  // WARNING: This line is only valid if you left the 'Name' field in
  // your site's version of theme_signup_user_form().
  return $form_data['Name'];
}

