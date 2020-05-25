
//呼び出し
$(function() {
	console.log("開通しました");
	calc_price();
	
	$('input[name="size"]').on("change",function() {
		calc_price();
	});

	$('input[name="topping"]').on("change",function() {
		calc_price();
	});

	$("#toynum").on("change",function() {
		calc_price();
	});

	// 値段の計算をして変更する関数
	function calc_price() {
		var size = $('input[name="size"]:checked').val();
		var topping_count = $("#topping input:checkbox:checked").length;
		var toynum = $("#toynum option:selected").val();
		
		if (size == "M") {
			var size_price = $("#sizeM").text();
			size_price_no_comma = Number(size_price.split(',').join(''));
			var topping_price = 200 * topping_count;
		} else {
			var size_price = $("#sizeL").text();
			size_price_no_comma = Number(size_price.split(',').join(''));
			var topping_price = 300 * topping_count;
		}
		var price = (parseInt(size_price_no_comma) + topping_price) * toynum;
		$("#totalprice").text(price.toLocaleString());
	};
});