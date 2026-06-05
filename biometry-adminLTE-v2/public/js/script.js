hrefAfterConnexion = "";

$(document).ready(function() {	
	
	$("body").on("click", ".btn-activer-commun-ajax", function() {

		$this = $(this);

		if($this.hasClass("table_active"))
		{
			nouveauStatut = "-1";
		}
		else if($this.hasClass("table_inactive"))
		{
			nouveauStatut = "1";
		}
		else
		{
			$.confirm({
				icon: 'fa fa-exclamation-triangle',
				title: commonTabLangue["Erreur"],
				content: commonTabLangue["Impossible de touver le statut actuel"],
				type: 'red',
				typeAnimated: true,
				buttons: {
					close: {
						text: commonTabLangue["Fermer"],
					}
				}
			});
			return false;
		}
		
		var formData = new FormData();
		formData.append("nomCamelCaseTable", $this.attr("nomCamelCaseTable"));
		formData.append("idElts", $this.attr("idElts"));
		formData.append("statut", nouveauStatut);
		

		// Launch AJAX request.
		$.ajax({

			//Options signifiant a jQuery de ne pas s'occuper du type de donnees
			
			cache: false,
			
			contentType: false,
			
			processData: false,
			
			type: "post",
			
			url: basePathBackoffice+"/activer_commun_ajax",
			
			data: formData,
			
			beforeSend: function () {
				retirerAlert($this.attr("idConteneur"));
				$('#'+$this.attr("idConteneur")).spinnerQueue({'message' : commonTabLangue["Traitement en cours ..."]}).spinnerQueue('started', 'pageLoad');
			},
			
			success: function (html) {
				if(html.error != "")
				{
					mettreErreurSurDiv($this.attr("idConteneur"), html.error, "");
				}
				else
				{
					if($this.hasClass("table_active"))
					{
						$this.removeClass("table_active").addClass("table_inactive").attr("title", commonTabLangue["Activer"]);
					}
					else if($this.hasClass("table_inactive"))
					{
						$this.removeClass("table_inactive").addClass("table_active").attr("title", commonTabLangue["Desactiver"]);
					}				
				}
			},
			
			error: function () {
				 mettreErreurSurDiv($this.attr("idConteneur"), commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"], "");
				 errorInputId = $this.attr("idConteneur");
			},
			
			complete: function () {
				$('#'+$this.attr("idConteneur")).spinnerQueue('finished', 'pageLoad');
			}
		});
	});
	
	
	
	$("body").on("click", ".btn-supprimer-commun-ajax", function() {
		
		$this = $(this);		
		
		$.confirm({
			icon: 'fa fa-exclamation-triangle',
			title: commonTabLangue["Confirmation"],
			content: commonTabLangue["Voulez vous vraiment supprimer"],
			buttons: {
				confirm: {
					closeIcon: true,
					text: commonTabLangue["Oui"],
					action: function(){
						$.confirm({
							content: function () {
								
								// console.log($("."+$this.attr("classLigne")).length); return false;
								
								if($this.attr("retirerPremier") == "-1" && $("."+$this.attr("classLigne")).length == "1")
								{
									return false;
								}
								
								var self = this;

								var formData = new FormData();
								formData.append("nomCamelCaseTable", $this.attr("nomCamelCaseTable"));
								formData.append("idElts", $this.attr("idElts"));
								formData.append("nomCamelCaseColonnePosition", $this.attr("nomCamelCaseColonnePosition"));
								formData.append("otherCulumnsUniqWithPosition", $this.attr("otherCulumnsUniqWithPosition"));
								formData.append("supprime", $this.attr("newStatutSupprime"));
								
								
								
								return $.ajax({
										cache: false,
										contentType: false,
										processData: false,
										type: "post",
										url: basePathBackoffice+"/supprimer_commun_ajax",
										data: formData,
								}).done(function (response) {
									
									if(response.error != "")
									{
										$.confirm({
											icon: 'fa fa-exclamation-triangle',
											title: commonTabLangue["Erreur"],
											content: response.error,
											type: 'red',
											typeAnimated: true,
											buttons: {
												close: {
													text: commonTabLangue["Fermer"],
												}
											}
										});
									}
									else if(response.info != "")
									{
										$.confirm({
											icon: 'fa fa-exclamation-triangle',
											title: commonTabLangue["Info"],
											content: response.info,
											type: 'orange',
											typeAnimated: true,
											buttons: {
												close: {
													text: commonTabLangue["Fermer"],
													action: function(){
														location.reload();
													}
												}
											}
										});
									}
									else if(response.varRetour != "")
									{
										$.confirm({
											icon: 'fa fa-exclamation-triangle',
											title: commonTabLangue["Succes"],
											content: response.varRetour,
											type: 'green',
											typeAnimated: true,
											buttons: {
												close: {
													text: commonTabLangue["Fermer"],
													action: function(){
														
														if($this.attr("newStatutSupprime") == "1")
														{
															if($this.attr("idHtmlLigne") != "" && $('#'+$this.attr("idHtmlLigne")).length > 0)
																$('#'+$this.attr("idHtmlLigne")).remove();
															
															if($this.attr("callbackSuccesSupprimerCommun") == "1")
															{
																callbackSuccesSupprimerCommun($this, "-1");
															}
														}
														else
														{
															$this.attr("newStatutSupprime", "1");
														}
													}
												},
											}
										});
									}

									self.close();
								}).fail(function() {
									$.confirm({
										icon: 'fa fa-exclamation-triangle',
										title: commonTabLangue["Erreur"],
										content: commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"],
										type: 'red',
										typeAnimated: true,
										buttons: {
											close: {
												text: commonTabLangue["Fermer"],
											}
										}
									});

									self.close();
								});
							}
						});
					}
				},
				cancel: {
					text: commonTabLangue["Non"],
				}
			}
		});
	});
	
	
	
	$(".form_einsurance input[name='email']").on({
	  keydown: function(e) {
	    if (e.which === 32)
	      return false;
	  },
	  change: function() {
	    this.value = this.value.replace(/\s/g, "");
	  }
	});
	
	
	
	
	/* For zebra striping */
    // $("table tr:nth-child(odd)").addClass("odd-row");
	/* For cell text alignment */
	// $("table td:first-child, table th:first-child").addClass("first");
	/* For removing the last border */
	// $("table td:last-child, table th:last-child").addClass("last");
    
    
    $("body").on("click", "div.alert a.toggle-alert", function() {
    	$(this).parent().remove();
    });
    
    $("table.accordion tbody tr.sousTitre .actions").on("click", "span.derouler", function() {
    	$this = $(this);
    	if($this.hasClass("fa-plus"))
    	{
    		$("table.accordion tbody tr.element-"+$this.attr("rel")).show(300);
    		$this.removeClass("fa-plus").addClass("fa-minus");
    	}
    	else
    	{
    		$("table.accordion tbody tr.element-"+$this.attr("rel")).hide(300);
    		$this.removeClass("fa-minus").addClass("fa-plus");
    	}
    });
    
    $("body").on("ifChanged", "table.accordion tbody tr.sousTitre .actions span.choix", function() {
    	$(".primeTtcChoose").html($("#sousTitre-"+$(this).attr("rel")+" span.unePrimeTTC").html());
    	$eltScrolHere = $("#lignePrimeTTC");
    });
    
    $("body").on("click", "table.accordion > tbody > tr.sousTitre", function(e) {
    	$targetElt = $(e.target);
    	if ($targetElt.hasClass("derouler"))
    	    return;
    	
    	$inputRadio = $(this).find("input[type='radio']").first();
    	$inputRadio.iCheck('check');
    });
    
    
    
    var ver = window.navigator.appVersion;
    ver = ver.toLowerCase();
    
    if(ver.indexOf("android") >= 0)
    {
    	// alert("Android");
    	$.each($(":input[type='text'].date.maskDate"), function () {
    		$this = $(this);
    		$this.removeClass("maskDate");
    		$this.addClass("dateAndroid");
    	});
    }
    

	if (ver.indexOf("android 4.1") >= 0)
	{
	    var idMaxLengthMap = {};
	
	    //loop through all input-text and textarea element
	    $.each($(':text, textarea, :password'), function () {
	        var id = $(this).attr('id'),
	            maxlength = $(this).attr('maxlength');
	
	        //element should have id and maxlength attribute
	        if ((typeof id !== 'undefined') && (typeof maxlength !== 'undefined')) {
	            idMaxLengthMap[id] = maxlength;
	
	            //remove maxlength attribute from element
	            $(this).removeAttr('maxlength');
	
	            //replace maxlength attribute with onkeypress event
	            $(this).attr('onkeypress','if(this.value.length >= maxlength ) return false;');
	        }
	    });
	
	    //bind onchange & onkeyup events
	    //This events prevents user from pasting text with length more then maxlength
	    $(':text, textarea, :password').bind('change keyup', function () {
	        var id = $(this).attr('id'),
	            maxlength = '';
	        if (typeof id !== 'undefined' && idMaxLengthMap.hasOwnProperty(id)) {
	            maxlength = idMaxLengthMap[id];
	            if ($(this).val().length > maxlength) {
	
	                //remove extra text which is more then maxlength
	                $(this).val($(this).val().slice(0, maxlength));
	            }
	        }
	    });
	}
});


$(document).ajaxSuccess(function() {
	// Pour le deplacement des elements

	if($.isFunction($.fn.sortable)) 
    {
		$(".msbtSortable").sortable({
		    helper: fixWidthHelper,
		    
		    update: function( event, ui ) {
		    	$this = $(this);
		    	
		    	if($this.attr("table"))
		    	{
		    		compteur = 0;
		    		listIds = "";
			    	$this.children().each(function(index) {
			    		compteur++;
			    		$thisHere = $(this);
			    		
			    		if(typeof $thisHere.attr("idElt") !== "undefined")
			    		{
			    			if(listIds != "")
				    		{
				    			listIds += ",";
				    		}
				    		
				    		listIds += $thisHere.attr("idElt");
				    		
				    		$thisHere.find("td.positionTableau").html(compteur);
			    		}
			    	});
			    	
			    	dataString = "table="+$this.attr("table")+"&listIds="+listIds;
			    	
			    	// Launch AJAX request.
					$.ajax({

						//Options signifiant a jQuery de ne pas s'occuper du type de donnees
						
			            type: "post",
			            
			            url: basePathBackoffice+"/reorganiser_liste",
				            
						data: dataString,
							
			            beforeSend: function() {
			            	retirerAlert("divBodyFormAjaxConnexion");
			            	$this.spinnerQueue({'message' : commonTabLangue["Traitement en cours ..."]}).spinnerQueue('started', 'pageLoad');
						},
				            
				       	success: function( html ) {

				       		if (html.error != "")
				       		{
				       			mettreErreurSurDiv(idDivConteneurPaginHere, html.error, "");
				       		}
				       		else
				       		{
				       			mettreSuccesSurDiv(idDivConteneurPaginHere , commonTabLangue["Operation effectuee avec succes"], "", true)
				       		}
						},
			            
			            error: function() {
			                mettreErreurSurDiv(idDivConteneurPaginHere , commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"], "");
						},
			            
			            complete: function() {
			            	$this.spinnerQueue('finished', 'pageLoad');
						}
					});
		    	}
		    }
		}).disableSelection();
    }
});


$(document).on("focus", "input.datepicker_recurring_start", function() {

	$this = $(this);
	
	$this.datepicker({
         changeYear: true,
         changeMonth: true,
         numberOfMonths: 1,
         dateFormat: "yy-mm-dd"
     }).attr("readonly", "readonly");
});


function get_time_zone_offset()
{
	 var current_date = new Date();
	 return parseInt(-current_date.getTimezoneOffset() / 60);
}

function mettreErreurSurInput(idInput , message, emplacement)
{
	if(emplacement == "AVANT")
    {
        classInput = "alert_input_avant";
    }
    else
    {
    	classInput = "alert_input_avant";
    }

    var contenu = '<span class="alert alert-danger fade in '+classInput+' displayNone" id="alert-message_'+idInput+'">';
    	contenu+= '  <span class="msg">'+message+'</span>';
    	contenu+= '</span>';
    	contenu+= '<div class="clearBoth"></div>';
        
    if(emplacement == "AVANT")
    {
        $("#"+idInput).before(contenu);
    }
    else
    {
        $("#"+idInput).after(contenu);
    }
    
    $('#alert-message_'+idInput).fadeIn(500);
}

function mettreLoaderSurDiv(idDiv , message, emplacement)
{
    var contenu = '<div class="loaderAjax" id="loader_'+idDiv+'">';
    	contenu+= '  <div class="txt-loader">'+message+'</div>';
    	contenu+= '  <div class="ctent-img-loader"><span class="img-loader"></span></div>';
    	contenu+= '</div>';
        
    
    if(emplacement == "AVANT")
    {
        $("#"+idDiv).before(contenu);
    }
    else if(emplacement == "APRES")
    {
        $("#"+idDiv).after(contenu);
    }
    else if(emplacement == "REMPLACER")
    {
        $("#"+idDiv).html(contenu);
    }
    else
    {
        $("#"+idDiv).prepend(contenu);
    }
}

function retirerLoaderSurDiv(idDiv)
{
    $("#loader_"+idDiv).remove();
}

function mettreLoaderSurInput(idInput, emplacement)
{
    var contenu = '<span class="loaderAjax-input" id="loaderAjax-input_'+idInput+'"></span>';
        
    if(emplacement == "AVANT")
    {
        $("#"+idInput).before(contenu);
    }
    else
    {
        $("#"+idInput).after(contenu);
    }
}

function retirerLoaderSurInput(idInput)
{
    $("#loaderAjax-input_"+idInput).remove();
}

function mettreErreurSurDiv(idDiv , message, emplacement)
{
	contenu = formatterMsgEnErreur(message, idDiv);
	
    if(emplacement == "AVANT")
    {
        $("#"+idDiv).before(contenu);
    }
    else if(emplacement == "APRES")
    {
        $("#"+idDiv).after(contenu);
    }
    else if(emplacement == "REMPLACER")
    {
        $("#"+idDiv).html(contenu);
    }
    else
    {
        $("#"+idDiv).prepend(contenu);
    }
    
    $('#alert-message_'+idDiv).fadeIn(500);
}

function mettreInfoSurDiv(idDiv , message, emplacement)
{
	contenu = formatterMsgEnInfo(message, idDiv);
         
    if(emplacement == "AVANT")
    {
        $("#"+idDiv).before(contenu);
    }
    else if(emplacement == "APRES")
    {
        $("#"+idDiv).after(contenu);
    }
    else if(emplacement == "REMPLACER")
    {
        $("#"+idDiv).html(contenu);
    }
    else
    {
        $("#"+idDiv).prepend(contenu);
    }
    
    $('#alert-message_'+idDiv).fadeIn(500);
}

function mettreSuccesSurDiv(idDiv , message, emplacement, masquerApres)
{
	contenu = formatterMsgEnSuccess(message, idDiv);    
        
    if(emplacement == "AVANT")
    {
        $("#"+idDiv).before(contenu);
    }
    else if(emplacement == "APRES")
    {
        $("#"+idDiv).after(contenu);
    }
    else if(emplacement == "REMPLACER")
    {
        $("#"+idDiv).html(contenu);
    }
    else
    {
        $("#"+idDiv).prepend(contenu);
    }
    
    
    if(masquerApres) $('#success-message_'+idDiv).fadeIn(500).fadeOut(5000);
    else $('#success-message_'+idDiv).fadeIn(500);
}

function retirerAlert(idDiv)
{
    $('#success-message_'+idDiv+', #alert-message_'+idDiv).remove();
    
    $("#"+idDiv+" .erreur-btn-choix").remove();
}

function isValidEmailAddress(emailAddress) 
{
    var pattern = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
    return pattern.test(emailAddress);
}

function formatterMsgEnSuccess(message, idDiv)
{
	var contenu = '<div class="alert alert-success fade in displayNone" id="success-message_'+idDiv+'">';
		contenu+= '  <a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">×</a>';
		contenu+= '  <div class="msg">'+message+'</div>';
		contenu+= '</div>';
	    
	return contenu;
}

function formatterMsgEnInfo(message, idDiv)
{
	var contenu = '<div class="alert alert-info fade in displayNone" id="alert-message_'+idDiv+'">';
		contenu+= '  <a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">×</a>';
		contenu+= '  <div class="msg">'+message+'</div>';
		contenu+= '</div>';
	    
	return contenu;
}

function formatterMsgEnErreur(message, idDiv)
{
	var contenu = '<div class="alert alert-danger displayNone" id="alert-message_'+idDiv+'">';
		contenu+= '  <a href="#" class="close" data-dismiss="alert" aria-label="close" title="close">×</a>';
		contenu+= '  <div class="msg">'+message+'</div>';
		contenu+= '</div>';
	
	return contenu;
}


function populateFormFromJson($form, jsonData)
{   
    $.each(jsonData, function(key, value) {  
        var $ctrl = $('[name='+key+']', $form);
        
        switch($ctrl.prop("type")) { 
            case "radio": case "checkbox":   
            	$ctrl.each(function() {
                    if($(this).attr('value') == value) $(this).attr("checked",value);
                });   
                break;
            default:
                $ctrl.val(value); 
        }  
    });  
}

function populateFormElement($form, eltName, value)
{   
	 var $ctrl = $('[name='+eltName+']', $form);
     
     switch($ctrl.prop("type")) { 
         case "radio": case "checkbox":   
         	$ctrl.each(function() {
                 if($(this).attr('value') == value) $(this).attr("checked",value);
             });   
             break;
         default:
             $ctrl.val(value); 
     }  
}

function jsUcfirst(string) 
{
    return string.charAt(0).toUpperCase() + string.slice(1);
}

// Cette fonction convertit une liste de dates du format aaaa-mm-jj au format jj/mm/aaaa
function convertirDateFormatFront($listeElts)
{
	$listeElts.each(function() {
		$this = $(this);

		if($this.val() != "")
		{
			dateSplit = $this.val().split("-");
    		if(Array.isArray(dateSplit))
    		{
    			newDate = dateSplit[2]+'/'+dateSplit[1]+'/'+dateSplit[0];
    			$this.val(newDate);
    		}
		}
	});
}

//Cette fonction convertit une liste de dates du format aaaa-mm-jj au format jj/mm/aaaa
function afficherSmartsupp(smartsupp_key, code_iso_langue)
{
	var _smartsupp = _smartsupp || {};
	_smartsupp.key = smartsupp_key;
	window.smartsupp||(function(d) {
		var s,c,o=smartsupp=function(){ o._.push(arguments)};o._=[];
		s=d.getElementsByTagName('script')[0];c=d.createElement('script');
		c.type='text/javascript';c.charset='utf-8';c.async=true;
		c.src='//www.smartsuppchat.com/loader.js?';s.parentNode.insertBefore(c,s);


		smartsupp('language', code_iso_langue); // set your language
	})(document);
}


// Pour fixer la taille des colones d'une ligne d'un tableau HTML lorsqu'on deplace celle-ci
function fixWidthHelper(e, ui)
{
    ui.children().each(function() {
        $(this).width($(this).width());
    });
    return ui;
}


function testDateValidMsbt(date)
{
	returnValue = true;
	
	if(/((31([-/ ])((0?[13578])|(1[02]))\3(\d\d)?\d\d)|((([012]?[1-9])|([123]0))([-/ ])((0?[13-9])|(1[0-2]))\12(\d\d)?\d\d)|(((2[0-8])|(1[0-9])|(0?[1-9]))([-/ ])0?2\22(\d\d)?\d\d)|(29([-/ ])0?2\25(((\d\d)?(([2468][048])|([13579][26])|(0[48])))|((([02468][048])|([13579][26]))00))))/.test(date))
	{
	    var tokens = date.split('/');  //  text.split('\/');
	    var day    = parseInt(tokens[0], 10);
	    var month  = parseInt(tokens[1], 10);
	    var year   = parseInt(tokens[2], 10);

		if(tokens[0].length != 2 || tokens[1].length != 2 || tokens[2].length != 4)
		{
			returnValue = false;
		}
		else if(day > 31 || (month == 2 && day > 29))
		{
			returnValue = false;
		}
		else if(month > 12)
		{
			returnValue = false;
		}	
	}
	else
	{
		returnValue = false;
	}
	
	return returnValue;
}

function testTimeValidMsbt(time)
{
	returnValue = true;
	
	if(/([0-1][0-9]|2[0-3]):([0-5][0-9])/.test(time))
	{
	    var tokens = time.split(':');  //  text.split('\/');
	    var heure    = parseInt(tokens[0], 10);
	    var minute  = parseInt(tokens[1], 10);

		if(tokens[0].length != 2 || tokens[1].length != 2)
		{
			returnValue = false;
		}
		else if(heure > 24)
		{
			returnValue = false;
		}
		else if(minute > 59)
		{
			returnValue = false;
		}	
	}
	else
	{
		returnValue = false;
	}
	
	return returnValue;
}

function testDatetimeValidMsbt(datetime)
{
	 tokens = datetime.split(' ');
	 date   = tokens[0];
	 time   = tokens[1];
	 
	 return testDateValidMsbt(date) && testTimeValidMsbt(time);
}

function traiterSuccesFormValidation(event, idConteneurForm, urlAction, tabAutresParametres, tabOtherPostValues)
{
	varRetour = false;
	
	// Prevent form submission
	event.preventDefault();

    retirerAlert(idConteneurForm);

    
    var $form = $(event.target),
        fv    = $form.data('formValidation');

	var formData = new FormData($form[0]);
	
	for (var keyTabOtherPostValues in tabOtherPostValues) {
		formData.append(keyTabOtherPostValues, tabOtherPostValues[keyTabOtherPostValues]);
	}
	
	
	// Transformation des dates au format mysql
	$form.find("input[type='text'].date.maskDate, input[type='text'].date.dateAndroid").each(function() {
		$this = $(this);
		
		if($this.val() != "")
		{
			dateSplit = $this.val().split("/");
			if(Array.isArray(dateSplit))
			{
				newDate = dateSplit[2]+'-'+dateSplit[1]+'-'+dateSplit[0];
				formData.append($this.attr("name"), newDate);
			}
		}
	});
	

	errorInputId = "";
    
	if(urlAction == "")
	{
		urlAction = $form.attr("action");
	}
	
    // Launch AJAX request.
	$.ajax({

		//Options signifiant a jQuery de ne pas s'occuper du type de donnees
        
        cache: false,
        
        contentType: false,
        
        processData: false,
        
        // async: false,
        
        type: "post",
        
        url: urlAction,
        
        data: formData,
		
        beforeSend: function () {
        	retirerAlert(idConteneurForm);
        	$('#'+idConteneurForm).spinnerQueue({'message' : commonTabLangue["Traitement en cours ..."]}).spinnerQueue('started', 'pageLoad');
		},
        
        success: function (html) {
        	if(html.error == "")
        	{
        		callbackSuccesFormValidation(html, tabAutresParametres, html.varRetour);
        	}
        	else
        	{
        		if(html.error == "1234")
    			{
    				$.each(html.tabError, function (key, tabMessages) {

    					messageFinal = "";

    					if(errorInputId == "")
    					{
    						errorInputId = key;
    					}

    					// Construction du message
    				    $.each(tabMessages, function (keyMessage, unMessage) {
    						if(unMessage != "")
    						{
    							if(messageFinal != "")
    					        	messageFinal += "<br />";

    							messageFinal += unMessage;
    						}
    				    });

    				    if($form.find("[name='"+key+"']").length > 0 && key != "csrf")
    					{
    						fv
    		                // Show the custom message
    		                .updateMessage(key, 'blank', messageFinal)
    		                // Set the field as invalid
    		                .updateStatus(key, 'INVALID', 'blank');
    					}

    					if(key == "csrf")
    					{
    						mettreErreurSurDiv(idConteneurForm, commonTabLangue["Votre session a exipire, veuillez actualiser la page puis reessayez"], "");
    						errorInputId = idConteneurForm;
    					}
    				});
    			}
    			else
    			{
    				mettreErreurSurDiv(idConteneurForm, html.error, "");
    				errorInputId = idConteneurForm;
    			}
        	}
		},
        
		error: function () {
			 mettreErreurSurDiv(idConteneurForm, commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"], "");
			 errorInputId = idConteneurForm;
		},
        
        complete: function () {
        	$('#'+idConteneurForm).spinnerQueue('finished', 'pageLoad');
        	
        	if(errorInputId == idConteneurForm)
        	{
        		$eltErrorHere = $("#"+errorInputId);
        	}
        	else
        	{
        		$eltErrorHere = $form.find("[name='"+errorInputId+"']")
        	}
        	
        	
			if($eltErrorHere.length > 0)
			{
				offsetTopHere = $eltErrorHere.offset().top;

				$('html, body').animate({
		            scrollTop: offsetTopHere - 80
		        }, 500);
			}

        	errorInputId = "";
		}
	});

	return varRetour;
}

function traiterErreurFormValidation(event, idConteneurForm)
{
	var formValidation = $(event.target).data('formValidation');
    invalidFields = formValidation.getInvalidFields();
	stringElement = invalidFields.get(0);
	$form = $(event.target);

	$eltErrorHere = $form.find("[name='"+$(stringElement).attr("name")+"']");

	if($eltErrorHere.length > 0)
	{
		offsetTopHere = $eltErrorHere.offset().top;

		$('html, body').animate({
            scrollTop: offsetTopHere - 80
        }, 500);
	}
}

function uniqid()
{
	function s4() {
	    return Math.floor((1 + Math.random()) * 0x10000)
	      .toString(16)
	      .substring(1);
	  }
	  return s4() + s4() + '' + s4() + '' + s4() + '' +
	    s4() + '' + s4() + s4() + s4();
	
	
	
	var date = new Date();
	var components = [
	    date.getYear(),
	    date.getMonth(),
	    date.getDate(),
	    date.getHours(),
	    date.getMinutes(),
	    date.getSeconds(),
	    date.getMilliseconds()
	];

	var uniqid = components.join("");
	
	return uniqid;
}

function openAnchorAccordion()
{
	if (window.location.hash)
    {
        var $target = $('body').find(window.location.hash);        
        
        if ($target.hasClass('titreAccordion'))
        {
			offsetTopHere = $target.offset().top;
			$('html, body').animate({
			    scrollTop: offsetTopHere - 90
			}, 500);

			
			$target.click();
        }
    }
}

function afficherInfosMalade(idConteneur, nomAssurePrincipal, nomMalade, nomSouscripteur, natureAffection, groupe, emplacement)
{
	if($("#"+idConteneur).length > 0)
	{
		contenu = '<table class="table dataTable table-striped" style="margin: 0 auto; width: 500px; margin-bottom: 20px;"><tbody>';

		contenu += '  <tr class="">';
		contenu += '     <td width="30%" style="font-weight: bold; ">Assure principal</td>';
		contenu += '     <td width="70%" style="">'+nomAssurePrincipal+'</td>';
		contenu += '  </tr>';

		if(nomMalade != "")
		{
			contenu += '  <tr class="">';
			contenu += '     <td width="30%" style="font-weight: bold; ">Ayant droit</td>';
			contenu += '     <td width="70%" style="">'+nomMalade+'</td>';
			contenu += '  </tr>';
		}
		
		if(natureAffection != "")
		{
			contenu += '  <tr class="">';
			contenu += '     <td width="30%" style="font-weight: bold; ">Groupe</td>';
			contenu += '     <td width="70%" style="">'+groupe+'</td>';
			contenu += '  </tr>';
		}
		

		contenu += '  <tr class="">';
		contenu += '     <td width="30%" style="font-weight: bold; ">Souscripteur</td>';
		contenu += '     <td width="70%" style="">'+nomSouscripteur+'</td>';
		contenu += '  </tr>';
		
		if(natureAffection != "")
		{
			contenu += '  <tr class="">';
			contenu += '     <td width="30%" style="font-weight: bold; ">Nature affection</td>';
			contenu += '     <td width="70%" style="">'+natureAffection+'</td>';
			contenu += '  </tr>';
		}
							
		contenu += '</tbody></table>';
		
		
		
		if(emplacement == "AVANT")
	    {
	        $("#"+idConteneur).before(contenu);
	    }
	    else if(emplacement == "APRES")
	    {
	        $("#"+idConteneur).after(contenu);
	    }
	    else if(emplacement == "REMPLACER")
	    {
	        $("#"+idConteneur).html(contenu);
	    }
	    else
	    {
	        $("#"+idConteneur).prepend(contenu);
	    }
	}
}