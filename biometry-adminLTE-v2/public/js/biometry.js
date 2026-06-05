$(document).ready(function() {	
	
	$('[name="check_all_list"]').on('ifChecked', function(event) {
			$this = $(this);
		$tableHere  = $this.closest('table');
		$tableHere.find('input[type="checkbox"][name="elt_check_tableau"]').iCheck('check');
	});
			            	    	
	$('[name="check_all_list"]').on('ifUnchecked', function(event) {
		$this = $(this);
		$tableHere  = $this.closest('table');
		$tableHere.find('input[type="checkbox"][name="elt_check_tableau"]').iCheck('uncheck');
	});

	$("form.form-horizontal-custom-modal input, form.form-horizontal-custom-modal select, form.form-horizontal-custom-modal textarea").addClass("form-control");
	$("form.form-horizontal-custom-modal label").addClass("col-md-3 control-label");
	$("form.form-horizontal-custom-modal label.label-icheck").removeClass("col-md-3 control-label");
	$('form.form-horizontal-custom-modal .box-footer input[type="submit"], form.form-horizontal-custom-modal .box-footer input[type="button"]').addClass("pull-right");
	// $("form.form-horizontal-custom-modal select").select2();
	
	$("form.form-horizontal-custom-modal select:not(.noSelect2)").select2();
	
	
	$("form.form-horizontal-custom-modal input.decimal").inputmask('decimal',
									            	    			{ 
									            	    				'alias': 'numeric',
									            	    				'groupSeparator': ' ',
									            	    				'autoGroup': true,
									            	    				'digits': 8,
									            	    				'radixPoint': ",",
									            	    				'digitsOptional': false,
									            	    				'allowMinus': false,
									            	    				'rightAlign': false
									            	    		  }
																);

	$( "input[type='text'].date" ).datepicker({
        changeYear: true,
        changeMonth: true,
        numberOfMonths: 1,
        dateFormat: "yy-mm-dd"
	});

	$("input[type='text'].time").timepicker({
		hourGrid: 4,
		minuteGrid: 10,
		timeFormat: 'HH:mm'
	});
			            	    	
	$( "input[type='text'].datetime" ).datetimepicker({
		changeYear: true,
		changeMonth: true,
		numberOfMonths: 1,
		dateFormat: "yy-mm-dd",
		timeFormat: 'HH:mm'
	});

	$('.ctneurICheck input, input.elt_check_tableau, input.elt_check_all_tableau').iCheck({
		checkboxClass: 'icheckbox_square-blue',
		radioClass: 'iradio_square-blue',
		increaseArea: '20%' // optional
	});


	$('form.form-horizontal-custom-modal .form-group label[class="msbt-tooltip"]').tooltip({
		"html" : true, 
		"placement": "right", 
		"trigger": "hover",
		// "template": '<div class="tooltip" role="tooltip"><div class="tooltip-arrow"></div><div class="tooltip-inner"></div></div>'
	});

			            	    	

	// Pour permettre à datepicker de s'afficher dans la fenêtre modale
        	    	
	// Since confModal is essentially a nested modal it's enforceFocus method
	// must be no-op'd or the following error results 
	// "Uncaught RangeError: Maximum call stack size exceeded"
	// But then when the nested modal is hidden we reset modal.enforceFocus
	var enforceModalFocusFn = $.fn.modal.Constructor.prototype.enforceFocus;
	$.fn.modal.Constructor.prototype.enforceFocus = function() {};

	// $confModal.on('hidden', function() {
	//				            	    	$.fn.modal.Constructor.prototype.enforceFocus = enforceModalFocusFn;
	// });
	
	// $confModal.modal({ backdrop : false });
	
});