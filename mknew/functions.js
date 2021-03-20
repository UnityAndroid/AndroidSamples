
function app_notify(id,pagee,workk,titlee,messagee,delid)
{
	$.ajax({
		url: "miss.php",
		data:{page:pagee,work:workk,title:titlee,message:messagee,did:delid},
		type: "POST",
}).done(function(data) { // data what is sent back by the php page
  $('#'+id).html(data); // display data
});
}