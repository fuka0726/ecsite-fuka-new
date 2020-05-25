$(function(){
	$(".paymentMethod").on("change", function(){
		if($(".paymentMethod:checked").val()==2){
		$("#creditPayMethod").show();
		}else if($(".paymentMethod:checked").val()==1){
			$("#creditPayMethod").hide();
		}
		
	});
	
});

