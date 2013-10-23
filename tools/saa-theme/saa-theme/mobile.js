var B = Builder;
Builder.dump(B);
var ALL_LINKS = new Hash();

function exists(existing){
    return !(Object.isUndefined(existing) || (existing == null));
}

Link = Class.create({

    initialize: function(theLink, ui){

	// check it's a real link
	if(!Object.isElement(theLink) || (theLink.tagName != "A")){
	    throw new Error("Parameter ["+theLink+"] is not an 'A'/anchor DOM element. It is: "+theLink.tagName);
	}

	this.link = theLink;
	this.ui = ui;
	this.link.identify();
	this.parent = $(this.link.parentNode);
	this.parent.identify();
	
	var existing = ALL_LINKS.get(this.link.id);
	if(exists(existing)){
	    alert("Link id "+this.link.id+" is already in the map");
	}else{
	    ALL_LINKS.set(this.link.id, this);
	}
    },

    /**
     * @param links an Array of either DOM A elements
     */
    doMenu: function(links){

	// find the links in the current menu and store them in an array
	var back = new Array();
	$$('#newmenu>li>a').each(function(lnk){
	    back.push(lnk);
	});

//	var back = parent.select("ul.sublinks")[0];
	// empty the menu
	$('newmenu').update(); // was jq .empty()
	var haveBackButton = false;
	var isRoot = false;
	if(links){
	    var thisLink = this;
	    links.each(function(lnk){

		if(lnk.innerHTML == "[back]"){
		    // ignore the old back button, we will recreate below
		    haveBackButton = true;
		}else{
		 
		var child = ALL_LINKS.get(lnk.id);
		if(exists(child)){
		    console.log("child "+child.link.id+" exists!");
		}else{
		    child = new Link(lnk);
		}
		
		$('newmenu').insert({bottom: child.getUI()});
		if(child.isRoot()) isRoot = true;
		console.log("have link: "+lnk+", html: "+child+", root? "+child.isRoot());
		}
	    });
	}
	
	if(!isRoot){    
	    var newitem = B.LI({},[B.A({},["[back]"])]);
	    var doMenuWithArgs = this.doMenu.bind(this, back);
	    newitem.observe("click", doMenuWithArgs); 
	    $('newmenu').insert({bottom:newitem});
	}
    },

    isLeaf: function(){
	var elem = $(this.link.parentNode);
	var hasSub = elem.hasClassName("haschildren");
	var linkAttrib = this.link.getAttribute("link");  
	var lastSet = this.link.getAttribute("lastSet");  
	console.log("decorating ["+elem+"] link? "+linkAttrib+"");
	
	if((!hasSub && (lastSet == null)) || ( (lastSet != null) && linkAttrib == null))
	    return true;
	else
	    return false;
    },

    getUI: function(){

	console.log("getUI");

	//if(exists(this.ui)){
	 //   console.log(" ==> have existing ui");
	  //  return this.ui;
	//}

	var leaf = this.isLeaf();
	if(leaf){
	    // no children: just include the link
	    console.log(" =[leaf]=>"+this.link.innerHTML);
	    this.link.addClassName("isContent");

	    // add a query param for the leaf
	    var qm = this.link.href.indexOf("?");
	    var newHref = null;
	    if(qm > -1){
		var q = $H(this.link.href.toQueryParams());
		q.set("leaf","true");
		newHref = this.link.href.substring(0,qm+1)+q.toQueryString();
	    }else{
		newHref = this.link.href+"?leaf=true";
	    }
	    console.log("original href: "+this.link.href);
	    this.link.setAttribute("href", newHref);
	    console.log("leaf href: "+this.link.href);
	    this.ui = B.LI({},[this.link]);
	}else{

	    console.log(" =[dir]=> id: "+this.link.id+", "+this.link.getAttribute("link"));
	    
	    // find sibling with class sublinks. should only be 1 
	    if(!exists(this.sublinks)){
		this.sublinks = this.link.siblings(".sublinks")[0];
		this.children = this.sublinks.select("li>a");
	    }
	    
//	    if(!exists(this.sublinks)){

	    this.sublinks.setStyle("display:none;");


		// has children, replace the link handler	    
		// move the link (for later use) so the click takes us to the 
		// sub menu rather than the page.
		this.link.setAttribute("link", this.link.href);
		this.link.removeAttribute("href");
		
		var doMenuWithArgs = this.doMenu.bind(this, this.children);
		this.link.observe("click", doMenuWithArgs);
//		this.link.onclick = doMenuWithArgs;
		/*
		  this.link.observe("dblclick", function(){
		  window.location=this.link.href;});
		*/	
	    //}
	    this.ui = B.LI({},[this.link]);
	}
	this.link.setAttribute("lastSet", "test");

	return this.ui;
    },

    setRoot: function(isRoot){
	if(isRoot)
	    this.link.setAttribute("rootLevel", true);
	else
	    this.link.removeAttribute("rootLevel");
    },

    isRoot: function(){
	var isRoot = false;
	var rt = this.link.getAttribute("rootLevel");
	if(rt != null) isRoot = true;
	
	return isRoot;
    }



});

MobileUI = Class.create({
    initialize: function(){

	var cssId = 'myCss';  // you could encode the css path itself to generate id..
	if (!document.getElementById(cssId))
	{
	    var head  = document.getElementsByTagName('head')[0];
	    var link  = document.createElement('link');
	    link.id   = cssId;
	    link.rel  = 'stylesheet';
	    link.type = 'text/css';
//	    alert("location "+window.location);
	    var a = document.createElement("a");
	    a.href=""+window.location;

//hide it from view when it is added
	    a.style.display="none";
//add it
//	    document.body.appendChild(a);
//read the links "features"
//	    alert(a.protocol);
//	    alert(a.hostname)
//	    alert(a.pathname)
//	    alert(a.port);
//	    alert(a.hash);
	    //link.href = 'http://test4.larry.ucsf.edu/sites/all/themes/saa-template/mobile.css';
	    var cssUrl = a.protocol+'//'+a.hostname+'/sites/all/themes/saa-theme/mobile.css';
	    var cssUrl = a.protocol+'//'+a.hostname+'/sites/saa-webdev.ucsf.edu/themes/saa-theme/mobile.css';
//	    alert("new url is:  "+cssUrl);
	    link.href = cssUrl;

	    link.media = 'all';
	    head.appendChild(link);

/*
	    var link2  = document.createElement('link');
	    link2.id   = cssId+"2";
	    link2.rel  = 'stylesheet';
	    link2.type = 'text/css';
var cssUrl2 = a.protocol+'//'+a.hostname+'/sites/all/themes/saa-theme/configurable-colors.css';
//	    alert("new url is:  "+cssUrl);
	    link2.href = cssUrl2;

	    link2.media = 'all';
	    head.appendChild(link2);
*/
	}  


	document.observe("dom:loaded", 	this.setupUI.bind(this));

    },

    getKeys: function(obj){
	//Object.prototype.keys = function(obj){
	var keys = [];
	for(var key in obj){
	    keys.push(key);
	}
	return keys;
    },

    updateBreadcrumbs: function(thisLink){
	console.log("UPDATING BREADCRUMBS");
//	var thethis.originalOAMenu.select('a[href="'+thisLink.href+'"]')[0];
	// this assumes the link has not been moved out of this.originalMenu
	// 
	var menuId = this.originalMenu.id;
	var ancestors = thisLink.ancestors();
	console.log(" ==> have #"+ancestors.length+" ancestors");
	var links = new Array();
	ancestors.each(function(ancestor){
	    console.log(" ==["+ancestor.id+":"+ancestor.tagName+"]=> ");
	    if(ancestor.id == menuId) throw $break;

	    if(ancestor.tagName != "LI") return;


	    // we have a list item, the anchor should be the only sibling
	    // a roundabout way of getting the only anchor, regardless of posn
	    var alink = ancestor.childElements()[0]; //.siblings("a")[0];
	    var title = alink.innerText.strip();
	    var url = exists(alink.getAttribute("href")) ? 
		alink.getAttribute("href"): alink.getAttribute("link");
	    console.log("Have ancestor "+alink.tagName+": "+title+", href: "+url);
	    var link = B.DIV({className: 'crumb'},[B.A({href: url},[title])]);
	    links.push(link);
	});

	links.reverse();
	$('header').select("div.crumb").each(function(old){old.remove()});
	links.each(function(newCrumb){ $('header').insert({bottom: newCrumb});});
	var title = document.title.gsub("|","");
	$('header').insert({bottom: B.DIV({className: 'crumb'},[B.A({},[title])])});
    },

    setupUI: function(){
	try{

	    if(exists($('ui-menu'))){
		// hide parts of the UI we don't want to see
		$A(["ui-menubottom", "ui-tabs2", "ui-slides", "ucsf", "ui-footer", "ui-content-right", "ui-content-left", "admin-menu"]).each(function(hideId){
		    try{
			$(hideId).setStyle("display: none");
		    }catch(ex){}
		});

		$$("body.admin-menu").each(function(body){
		    body.setStyle("margin-top: 0px !important;");
		});

		// check our current location against the menu items
		// and produce a breadcrumb trail
		var found = null;
		this.originalMenu = $('ui-navigation-menu');
		this.allMenuLinks = $$('#ui-navigation-menu a');
		this.allMenuLinks.each(function(link){
		    var current = ""+window.location;
		    if(current == link.href){
			found = link;
			//alert("found! "+link.href);
		    }
		});
		
		// FIXME: this is not a robust way of doing this but
		// it will do for now
		if(!exists(found)){
		    this.allMenuLinks.each(function(link){
			// test without query parameters
			var current = ""+window.location;
			var qIdx = current.indexOf("?");
			if(qIdx > -1){
			    
			    current = current.substring(0, qIdx);
			}
			
			var href = link.href;
			qIdx = href.indexOf("?");
			if(qIdx > -1){
			    href = href.substring(0, qIdx);
			}

			//console.log("checking "+current+" vs. "+href);
			if(current == href){
			    found = link;
			    console.log("found menu link! "+link.href);
			}
		    });
		
		}

		// set our header
		var logo = $('logo');		
		var header = B.H1({id: "header"},[logo]);
		header.setAttribute("id","header");
		$('ui-header').replace(header);


		// check if we're a leaf
		var params = $H((""+window.location).toQueryParams());
		var isLeaf  = exists(params.get("leaf"));

		// so we did find the exact url. it's still possible that
		// the user was at a leaf node and then dug further down.
		// this we will test this possibility by seeing if
		// the current location starts with any of our menu urls
		// (picking the longest match in the case of multiple matches)
		// this test hinges on the assumptions that the urls
		// are hierarchical.
		// in this case we'd like to continue treating this a 
		// a leaf node.
		if(!exists(found)){
		    this.allMenuLinks.each(function(link){
			// test without query parameters
			var current = ""+window.location;
			var qIdx = current.indexOf("?");
			if(qIdx > -1){
			    
			    current = current.substring(0, qIdx);
			}
			
			var href = link.href;
			qIdx = href.indexOf("?");
			if(qIdx > -1){
			    href = href.substring(0, qIdx);
			}

			//console.log("checking "+current+" vs. "+href);
			if(current.startsWith(href)){
			    found = link;
			    console.log("found menu link that starts with! "+link.href);
			}
		    });
		    // treat like a leaf
		    if(exists(found)) isLeaf = true;
		}

		if(exists(found)){
		    this.updateBreadcrumbs(found);
		}


		// decide which menu items to show (only relevant for non leaf)
		var items = new Array();
		if(exists(found)){
		    var sublinks = found.siblings("ul.sublinks")[0];
		    if(exists(sublinks)){
			sublinks.select('li > a').each(function(elem){
			    var link = new Link(elem); 
			    //link.setRoot(true);
			    items.push(link.getUI());
			});
		    }else{
			// FIXME: should we default to the root list?
		//	alert("no submenu items found");
			isLeaf = true;
		    }
		}else{

		    $$('#ui-navigation-menu > li > a').each(function(elem){
			var link = new Link(elem); 
			link.setRoot(true);
			items.push(link.getUI());
		    });
		}
		
		var showMenu = (isLeaf) ? "none" : "block";
//menu-padded
		var list = "<div class='menu-full menu-detailed' style='display: "+showMenu+";'>"+
		    "<h1 class='light menu-first'>MENU</h1> "+
		    "   <ol id='newmenu'></ol></div>";

		$('ui-menu').update(list); // was jquery .html(list)
		var olist = $('newmenu'); 
		//	olist.addClass("content-elements webkit-transition-slideX");

		items.each(function(item){
		    olist.insert({bottom: item});
		});
	    
		//	mwf.webkit.transitions.init();
		var content = "";
		$$('#ui-content-main .ui-content').each(function(elem){
		    //alert("menu item: "+elem.innerHTML);
		    content += elem.innerHTML;
		});
		content = "<div class='content-full'>"+content+"</div>";

		// show the content if we're a leaf
		if(isLeaf){
		    $('ui-content-main').update(content);
		}else{
		    //		$('ui-content-main').update();
		    $('ui-columns').remove();
		}
	    }
	}catch(ex){
		alert("Unable to create menu: "+ex);
	}
    
    }
		       
});

(function(a,b){if(/android|avantgo|blackberry|blazer|compal|elaine|fennec|hiptop|iemobile|ip(hone|od|ad)|iris|kindle|lge |maemo|midp|mmp|opera m(ob|in)i|palm( os)?|phone|p(ixi|re)\/|plucker|pocket|psp|symbian|treo|up\.(browser|link)|vodafone|wap|windows (ce|phone)|xda|xiino/i.test(a)||/1207|6310|6590|3gso|4thp|50[1-6]i|770s|802s|a wa|abac|ac(er|oo|s\-)|ai(ko|rn)|al(av|ca|co)|amoi|an(ex|ny|yw)|aptu|ar(ch|go)|as(te|us)|attw|au(di|\-m|r |s )|avan|be(ck|ll|nq)|bi(lb|rd)|bl(ac|az)|br(e|v)w|bumb|bw\-(n|u)|c55\/|capi|ccwa|cdm\-|cell|chtm|cldc|cmd\-|co(mp|nd)|craw|da(it|ll|ng)|dbte|dc\-s|devi|dica|dmob|do(c|p)o|ds(12|\-d)|el(49|ai)|em(l2|ul)|er(ic|k0)|esl8|ez([4-7]0|os|wa|ze)|fetc|fly(\-|_)|g1 u|g560|gene|gf\-5|g\-mo|go(\.w|od)|gr(ad|un)|haie|hcit|hd\-(m|p|t)|hei\-|hi(pt|ta)|hp( i|ip)|hs\-c|ht(c(\-| |_|a|g|p|s|t)|tp)|hu(aw|tc)|i\-(20|go|ma)|i230|iac( |\-|\/)|ibro|idea|ig01|ikom|im1k|inno|ipaq|iris|ja(t|v)a|jbro|jemu|jigs|kddi|keji|kgt( |\/)|klon|kpt |kwc\-|kyo(c|k)|le(no|xi)|lg( g|\/(k|l|u)|50|54|e\-|e\/|\-[a-w])|libw|lynx|m1\-w|m3ga|m50\/|ma(te|ui|xo)|mc(01|21|ca)|m\-cr|me(di|rc|ri)|mi(o8|oa|ts)|mmef|mo(01|02|bi|de|do|t(\-| |o|v)|zz)|mt(50|p1|v )|mwbp|mywa|n10[0-2]|n20[2-3]|n30(0|2)|n50(0|2|5)|n7(0(0|1)|10)|ne((c|m)\-|on|tf|wf|wg|wt)|nok(6|i)|nzph|o2im|op(ti|wv)|oran|owg1|p800|pan(a|d|t)|pdxg|pg(13|\-([1-8]|c))|phil|pire|pl(ay|uc)|pn\-2|po(ck|rt|se)|prox|psio|pt\-g|qa\-a|qc(07|12|21|32|60|\-[2-7]|i\-)|qtek|r380|r600|raks|rim9|ro(ve|zo)|s55\/|sa(ge|ma|mm|ms|ny|va)|sc(01|h\-|oo|p\-)|sdk\/|se(c(\-|0|1)|47|mc|nd|ri)|sgh\-|shar|sie(\-|m)|sk\-0|sl(45|id)|sm(al|ar|b3|it|t5)|so(ft|ny)|sp(01|h\-|v\-|v )|sy(01|mb)|t2(18|50)|t6(00|10|18)|ta(gt|lk)|tcl\-|tdg\-|tel(i|m)|tim\-|t\-mo|to(pl|sh)|ts(70|m\-|m3|m5)|tx\-9|up(\.b|g1|si)|utst|v400|v750|veri|vi(rg|te)|vk(40|5[0-3]|\-v)|vm40|voda|vulc|vx(52|53|60|61|70|80|81|83|85|98)|w3c(\-| )|webc|whit|wi(g |nc|nw)|wmlb|wonu|x700|xda(\-|2|g)|yas\-|your|zeto|zte\-/i.test(a.substr(0,4))){

    new MobileUI();

			}else{
//   alert('not mobile '+navigator.userAgent+", vendor: "+navigator.vendor);
   console.log('not mobile '+navigator.userAgent+", vendor: "+navigator.vendor);
//    alert("name param is: "+$.url.param("name"));
//   new MobileUI();

	}})(navigator.userAgent||navigator.vendor||window.opera,'http://google.com');