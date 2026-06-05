hrefAfterConnexion = "";

$(function() {

	$( "input[type='radio'].cstm-btn-choix-form").each(function() {
		$this = $(this);
		
		if($this.is(":checked") && $this.val() == $this.attr("msbt-value-toggle"))
	    {
			cliquerBoutonChoixIcheck($this);
	    }
		
	});
	
	$( "input[type='radio']").each(function() {
		$this = $(this);
		
		if($this.is(":checked"))
	    {
			$this.parent().removeClass("btn-borders");
	    }
		
	});	
	
	$("body").on("ifChecked", "input[type='radio']", function() {		
		$this = $(this);

		$this.parent().parent().parent().parent().parent().find("label").addClass("btn-borders");
		$this.parent().parent().removeClass("btn-borders");
	});	
	
	$("body").on("click", "#btnSuivant", function() {
		$("#form_einsurance_"+codeEtape).submit();
	});
	
	$("body").on("ifChanged", "input[type='radio'].cstm-btn-choix-form", function() {
		cliquerBoutonChoixIcheck($(this));
	});
    
	$("body").on("click", "#btnPrecedent", function() {

		if(codeEtape == 1)
			return false;

		$this = $(this);

		dataString = "codeEtape="+codeEtape+"&nomSession="+nomSession;
        
        // Launch AJAX request.
		            // Launch AJAX request.
		$.ajax({
			
            type: "post",
            
            url: basePathLangue+"/e-insurance/precedent",
            
            data: dataString,
			
            beforeSend: function () {
            	$('#ctneurFormEInsurance').spinnerQueue({'message' : commonTabLangue["Traitement en cours ..."]}).spinnerQueue('started', 'pageLoad');
			},
            
            success: function (html) {

				if(html.error != "")
				{
					mettreErreurSurDiv("ctneurFormEInsurance" , html.error, "");
				}
				else
				{
					codeEtape = html.codeEtape;
					activerEtapeWizard(codeEtape, commonTabLangue["Etape suivante"]);
				}
			},
            
			error: function () {
				 mettreErreurSurDiv("ctneurFormEInsurance", commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"], "");
			},
            
            complete: function () {
            	$('#ctneurFormEInsurance').spinnerQueue('finished', 'pageLoad');
			}
		});
    });
	
	$("body").on("click", ".reprendreCotationUrl a", function() {
		
		$this = $(this);
		
		// Launch AJAX request.
		$.ajax({
			
            type: "post",
            
            url: basePathLangue+"/e-insurance/reprendre-cotation",
            
            data: "nomProduit="+$this.attr("urlHere"),
			
            beforeSend: function () {
            	retirerAlert($this.attr("idConteneur"));
            	mettreLoaderSurInput($this.attr("id"), "");
			},
            
            success: function (html) {

				if(html.error == "")
				{
					window.location = basePathLangue+"/e-insurance/"+$this.attr("urlHere");
				}
				else
				{
					mettreErreurSurDiv($this.attr("idConteneur"), html.error, "");
				}
			},
            
			error: function () {
				 mettreErreurSurDiv($this.attr("idConteneur"), commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"], "");
			},
            
            complete: function () {
            	retirerLoaderSurInput($this.attr("id"));
			}
		});
    });
	
	$("form.form_einsurance").on("keyup keypress", "input[type='text'].date.maskDate", function(e) {
		
		$this = $(this);
		$formParent = $this.closest("form"); // Le premier parent de type form

		$formParent.data('formValidation')
		    .updateStatus($this.attr("name"), 'NOT_VALIDATED')
		    .validateField($this.attr("name"));
	    
    });
	
	
	$("form.form_einsurance").find("input[type='text'].date.dateAndroid").datepicker({
    	changeYear: true,
        changeMonth: true,
        numberOfMonths: 1,
        dateFormat: "dd/mm/yy",
        onSelect: function(date, inst) {
        	$this = $(this);
        	
            // Revalidate the field when choosing it from the datepicker
        	$this.closest("form").formValidation('revalidateField', $this.attr("name"));
        },
        //comment the beforeShow handler if you want to see the ugly overlay
        beforeShow: function() {
            setTimeout(function() {
                $('.ui-datepicker').css('z-index', 25);
            }, 0);
        }
	});
});

function activerTitreWizard (codeEtape)
{
	for(i=1; i<codeEtape; i++)
	{
		$("#titreWizard"+i).removeClass("current").addClass("completed");
	}

	for(i=codeEtape+1; i<=nbreEtape; i++)
	{
		$("#titreWizard"+i).removeClass("current").removeClass("completed");
	}

	$("#titreWizard"+codeEtape).removeClass("completed").addClass("current");	
	
	listeTtesEtapes = "";
	if(nbreEtape == 1)
	{
		listeTtesEtapes = "1";
	}
	else if(codeEtape < nbreEtape)
	{
		listeTtesEtapes = codeEtape+","+(codeEtape + 1);
	}
	else
	{
		listeTtesEtapes = (codeEtape - 1)+","+codeEtape;
	}

	for(i=1; i<=nbreEtape; i++)
	{
		if (listeTtesEtapes.indexOf(i+"") >= 0)
		{
			$("#titreWizard"+i).show();
		}
		else
		{
			$("#titreWizard"+i).hide();
		}
	}

	
	offsetTopHere = $('#contenuPage').offset().top;

	$('html, body').animate({
        scrollTop: offsetTopHere - 80
    }, 500);
}

function activerEtapeWizard (codeEtape, messageBtnSuivant)
{
	$("#ctneurFormEInsurance form").slideUp(500);
	$("#form_einsurance_"+codeEtape).slideDown(500);

	if(codeEtape != 1)
		$("#btnPrecedent").removeAttr("disabled");
	else
		$("#btnPrecedent").attr("disabled", "disabled");

	$("#btnSuivant").html(messageBtnSuivant);

	activerTitreWizard(codeEtape);
}

function cliquerBoutonChoix ($this, idFormulaire)
{
    var $target = $($this.attr('data-toggle'));

    if($this.attr("rel") == "oui")
    {
    	$target.slideDown(500);
    }
    else if($this.attr("rel") == "non")
    {
    	$target.slideUp(500);
    }
    
    $('#'+$this.parent().parent().attr("id")+' button').addClass("btn-borders");
	$this.removeClass("btn-borders");
	
	
    if (!$target.is(':visible')) {
        // Enable the submit buttons in case additional fields are not valid
        $('#'+idFormulaire).data('formValidation').disableSubmitButtons(false);
    }

    idElementHere = $this.attr("data-toggle");
	idElementHere = idElementHere.replace(".", "");
	
	$("#"+idElementHere).val($this.attr("rel"));
	
	retirerAlert($this.parent().attr("id")); 
}

function cliquerBoutonChoixIcheck($this)
{
	var $target = $($this.attr('data-toggle'));
	idFormulaire = $this.attr("idForm");

    if($this.val() == $this.attr("msbt-value-toggle"))
    {
    	$target.slideDown(500);
    }
    else
    {
    	$target.slideUp(500);
    }
	
    if (!$target.is(':visible')) {
        // Enable the submit buttons in case additional fields are not valid
        // $('#'+idFormulaire).data('formValidation').disableSubmitButtons(false);
    }
	retirerAlert($this.parent().attr("id"));
}

function mettreErreurSurLigne(idLigne , message, emplacement)
{
    var loader = '<small class="help-block erreur-btn-choix" data-fv-validator="notEmpty" data-fv-for="montantCaution" data-fv-result="INVALID" style="display: inline-block;">'+
			     '	<i class="fa fa-info-circle"></i> '+message+
			     '</small>';

        
    if(emplacement == "DEBUT")
    {
        $("#"+idLigne).prepend(loader);
    }
    else
    {
        $("#"+idLigne).append(loader);
    }
    
    $('#alert-message_'+idLigne).fadeIn(500);
}

function appliquerChoixCheck(tabChoixIcheck)
{
	for(var key in tabChoixIcheck) {
	    if(tabChoixIcheck[key] == "on" && $('#'+key).length > 0)
		{
			$('#'+key).iCheck('check');
		}
			
	}
}

function traiterSuccesCotation(event, codeEtapeHere, nomDossierVue, typeEtape, msgEtapeSuivante, lastEtape, idElt)
{
	// Prevent form submission
	event.preventDefault();

    retirerAlert("ctneurFormEInsurance");

    
    var $form = $(event.target),
        fv    = $form.data('formValidation');

	var formData = new FormData($form[0]);

	
	
	// Transformation des dates au format mysql
	$form.find("input[type='text'].date.maskDate, input[type='text'].date.dateAndroid, input[type='text'].mask-date-msbt").each(function() {
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
	
    
    // Launch AJAX request.
	$.ajax({

		//Options signifiant a jQuery de ne pas s'occuper du type de donnees
        
        cache: false,
        
        contentType: false,
        
        processData: false,
        
        type: "post",
        
        url: $form.attr("action"),
        
        data: formData,
		
        beforeSend: function () {
        	retirerAlert("ctneurFormEInsurance");
        	$('#ctneurFormEInsurance').spinnerQueue({'message' : commonTabLangue["Traitement en cours ..."]}).spinnerQueue('started', 'pageLoad');
		},
        
        success: function (html) {

			if((typeEtape == "Cotation" || typeEtape == "Sinistre") && html.error == "")
			{
				if(lastEtape == true)
				{
					if(typeEtape == "Cotation")
					{
						window.location = basePathLangue+"/e-insurance/"+nomDossierVue+"/resultat";
					}
					else if(typeEtape == "Sinistre")
					{
						window.location = basePathLangue+"/e-insurance/client/sinistre/details/"+html.sinistreId;
					}
				}
				else
				{
					window.codeEtape = parseInt(codeEtapeHere)+1;
					activerEtapeWizard(window.codeEtape, msgEtapeSuivante);
				}
			}
			else if(typeEtape == "Sinistre" && html.codeErreur == "CONNEXION_ERROR")
			{
				if($("#connexionModale").length > 0)
				{
					retirerAlert("divBodyFormAjaxConnexion");
					mettreInfoSurDiv("divBodyFormAjaxConnexion", commonTabLangue["Veuillez vous connecter pour declarer votre sinistre"], "");
					$("#connexionModale").click(); // Pour ouvrir la fenetre modale de connexion
	       			hrefAfterConnexion = "#"; // Pour ne pas recharger la page
				}
				else
				{
					location.reload();
				}
			}
			else if(typeEtape == "Souscription" && html.codeErreur == "CONNEXION_ERROR")
			{
				if($("#connexionModale").length > 0)
				{
					retirerAlert("divBodyFormAjaxConnexion");
					mettreInfoSurDiv("divBodyFormAjaxConnexion", commonTabLangue["Veuillez vous connecter pour souscrire a l'assurance"], "");
					$("#connexionModale").click(); // Pour ouvrir la fenetre modale de connexion
	       			hrefAfterConnexion = "#"; // Pour ne pas recharger la page
				}
				else
				{
					location.reload();
				}
			}
			else if(typeEtape == "Souscription" && html.error == "")
			{
        		location.reload();
			}
			else
			{
				if(html.error == "1234")
				{
					$.each(html.tableError, function (key, tabMessages) {

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
							mettreErreurSurDiv("ctneurFormEInsurance", commonTabLangue["Votre session a exipire, veuillez actualiser la page puis reessayez"], "");
							errorInputId = "ctneurFormEInsurance";
						}
					});
				}
				else
				{
					mettreErreurSurDiv("ctneurFormEInsurance", html.error, "");
					errorInputId = "ctneurFormEInsurance";
				}
			}
		},
        
		error: function () {
			 mettreErreurSurDiv("ctneurFormEInsurance", commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"], "");
			 errorInputId = "ctneurFormEInsurance";
		},
        
        complete: function () {
        	$('#ctneurFormEInsurance').spinnerQueue('finished', 'pageLoad');
        	
        	if(errorInputId == "ctneurFormEInsurance")
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

	return false;
}

function traiterErreurCotation(event, codeEtapeHere)
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