$(function(){

	var name = $('#searchForm [name="name"]').val();
	
	//ページング機能
	$('.page-button').on("click", function(){
		var page = $(this).val();
		var sort = $('#sort-select option:selected').val();
		var name = $('#searchForm [name="name"]').val();
		$('#searchForm [name="page"]').val(page);
		$('#searchForm [name="name"]').val(name);
		$('#searchForm [name="sort"]').val(sort);
		$('#searchForm').submit();
	});
	
	
	
	//商品の並べ替え機能
	$('#sort-select').on('change', function(){
		var name = $('#searchForm [name="name"]').val();
		var sort = $('#sort-select option:selected').val();
		$('#searchForm [name="page"]').val(1);
		$('#searchForm [name="name"]').val(name);
		$('#searchForm [name="sort"]').val(sort);
		$('#searchForm').submit();
		
		
	});
	
	//検索フォームのクリアボタン機能
	 $("#reset").on("click", function () {
	        $("#searchName").val("");
	  
	    });
	
	
	
	
});
