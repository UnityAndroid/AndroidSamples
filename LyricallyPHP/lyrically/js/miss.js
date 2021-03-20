function get_data(page,work,updown,dir,did){ 
	$("#loading_spinner").show(); 
	 
	$.ajax({   
		type: "POST",
		url: "miss_data.php", 
		data: { 
			"page": page,
			"work": work,
			"updown": updown,
			"dir": dir,
			"did": did
		},            
		dataType: "html",                
		success: function(response){                    
			$("#data").html(response); 
			$("#loading_spinner").hide(); 
	
		}
	}); 
}  


function get_custom_data(page,work,updown,dir,did,cid){ 
	$("#loading_spinner").show(); 
	$.ajax({   
		type: "POST",
		url: "miss_data.php", 
		data: { 
			"page": page,
			"work": work,
			"updown": updown,
			"dir": dir,
			"did": did,
			"cid": cid
		},            
		dataType: "html",                
		success: function(response){                    
			$("#data").html(response); 
			$("#loading_spinner").hide(); 
	
		}
	}); 
}  
