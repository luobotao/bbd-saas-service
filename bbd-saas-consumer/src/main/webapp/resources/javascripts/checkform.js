		/*  $('input,textarea').on('change',function(){
			  formData(['name','text','text1','textarea']);
		  })
		  $('button.btn').on('click',function(){
			  formData(['name','text','text1','textarea']);
		  })*/
		  function formData(defaults,btn){
			  var defaults = defaults || [];
			  $.each(defaults,function(i,val){
				  var Class = $('input[name="' + val + '"],textarea[name="' + val + '"]').val();				  
				  if(Class.length == ''){
					  $('.btn').prop('disabled',true);
					  return false;
				  } else {
					  $('.btn').prop('disabled',false);
				  }
		      })
		  }