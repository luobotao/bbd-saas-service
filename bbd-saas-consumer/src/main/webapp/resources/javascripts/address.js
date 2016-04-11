function get_Itemjson(val){
	 var val = JSON.stringify(window.localStorage.getItem(val)); 
	 return $.parseJSON(val);
}
function get_Item(val){
	  var val = val;
	  return window.localStorage.getItem(val);
}
function address(country,address){
	  var country = country,
	     address = address,
	     addresseeAddress,
	     addresseeJSON = get_Itemjson(country);
	     if(addresseeJSON!=null){
	      country = $.parseJSON(addresseeJSON).country; 
	        addresseeAddress = get_Item(address);
	     }
	  return "河北省石家庄" + country + addresseeAddress;
	 }