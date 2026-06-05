	
	numPaginActuel = 1;
    cheminControlleurPaginHere = "";
    idDivConteneurPaginHere = "";
    idDivConteneurGrandPaginHere = "";
    idFormPaginHere = "";
    appendPagination = false;
    removeAfterPagin = "";
    idUlCtneurPaginHere = "";
    emplacementLoader = "";
    otherElementsToPost = "";
    nbreMaxLigneTableau = 10;
    nomColoneTriPagination = "";
    typeTriColonePagination = "";
    
	
	$(function() {
		
		$("body").on("click", "#formAjaxImprimer button", function() {
			$this = $(this);
			dataString = $("#formFiltreListeElt").serialize();
			dataString += otherElementsToPost;
			
			if($this.attr("typeImpression") == "csv")
   			{
				window.location = $("#btnImprimerApp").attr("urlAction")+"-csv?"+dataString;
				if($("#ctneurMdleImprimer").length > 0)
            	{
            		$("#ctneurMdleImprimer").modal("hide");
            	}
				return false;
   			}

			// Launch AJAX request.
			$.ajax({

				//Options signifiant a jQuery de ne pas s'occuper du type de donnees
				
	            type: "post",
	            
	            url: $("#btnImprimerApp").attr("urlAction")+"-pdf",
		            
				data: dataString,
					
	            beforeSend: function() {
	            	mettreLoaderSurDiv("cteneurActionList" , commonTabLangue["Traitement en cours, veuillez patienter"]+" ...", "");
				},
		            
		       	success: function( html ) {
		       		if (html.error != "")
		       		{
		       			mettreErreurSurDiv("cteneurActionList" , html.error, "");
		       		}
		       		else
		       		{
		       			$('body').append(html.varRetour);
		       			
		       			$printStuff = $("#cntneurDocExportPdf");
		       			$printStuff.jqprint();
		    			$printStuff.empty().remove();
		       		}
				},
	            
	            error: function() {
	                mettreErreurSurDiv("cteneurActionList" , commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"], "");
				},
	            
	            complete: function() {
	            	retirerLoaderSurDiv("cteneurActionList");
	            	if($("#ctneurMdleImprimer").length > 0)
	            	{
	            		$("#ctneurMdleImprimer").modal("hide");
	            	}
				}
			});

			return false;
        });
		
		
		
		
		
		
		
		
		
		
		
		
		$("body").on("click", "#formAjaxImprimerDetaille button", function() {
			$this = $(this);
			dataString = $("#formFiltreListeElt").serialize();
			dataString += otherElementsToPost;
			
			if($this.attr("typeImpression") == "csv")
   			{
				window.location = $("#btnImprimerDetailleApp").attr("urlAction")+"-csv?"+dataString;
				if($("#ctneurMdleImprimerDetaille").length > 0)
            	{
            		$("#ctneurMdleImprimerDetaille").modal("hide");
            	}
				return false;
   			}

			// Launch AJAX request.
			$.ajax({

				//Options signifiant a jQuery de ne pas s'occuper du type de donnees
				
	            type: "post",
	            
	            url: $("#btnImprimerDetailleApp").attr("urlAction")+"-pdf",
		            
				data: dataString,
					
	            beforeSend: function() {
	            	mettreLoaderSurDiv("cteneurActionList" , commonTabLangue["Traitement en cours, veuillez patienter"]+" ...", "");
				},
		            
		       	success: function( html ) {
		       		if (html.error != "")
		       		{
		       			mettreErreurSurDiv("cteneurActionList" , html.error, "");
		       		}
		       		else
		       		{
		       			$('body').append(html.varRetour);
		       			
		       			$printStuff = $("#cntneurDocExportPdf");
		       			$printStuff.jqprint();
		    			$printStuff.empty().remove();
		       		}
				},
	            
	            error: function() {
	                mettreErreurSurDiv("cteneurActionList" , commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"], "");
				},
	            
	            complete: function() {
	            	retirerLoaderSurDiv("cteneurActionList");
	            	if($("#ctneurMdleImprimerDetaille").length > 0)
	            	{
	            		$("#ctneurMdleImprimerDetaille").modal("hide");
	            	}
				}
			});

			return false;
        });
	});

    function cliquerEltPagination($this)
    {
        if(!$this.hasClass("disabled") && !$this.hasClass("active"))
        {
            if($this.hasClass("next"))
            {
                nroPage = parseInt(numPaginActuel) + 1;
            }
            else if($this.hasClass("previous"))
            {
                nroPage = parseInt(numPaginActuel) - 1;
            }
            else
            {
                nroPage = parseInt($this.attr("rel"));
            }
    
            choisirPage(nroPage);   
        }
    }



    function choisirPage(nroPage)
    {
        if($("#"+idFormPaginHere).length > 0) dataString = $("#"+idFormPaginHere).serialize();
        else dataString = "hytyrlele=''";
        
        dataString += "&idDivConteneurPaginHere="+idDivConteneurPaginHere;
        dataString += otherElementsToPost;
        
        cheminControlleur = cheminControlleurPaginHere+"/"+nroPage;
        
        // Launch AJAX request.
		$.ajax({
			
            type: "post",
            
            url: cheminControlleur,
            
            data: dataString,
			
            beforeSend: function(){
                mettreLoaderSurDiv(idDivConteneurPaginHere , commonTabLangue["Traitement en cours, veuillez patienter"]+" ...", emplacementLoader);
			},
            
            success: function( html ) {
            	
            	if(!appendPagination)
            	{
            		$("#"+idDivConteneurPaginHere).html("");
            	}

            	if($("#nbreMsgNonTraite").length > 0)
        		{
        			$("#nbreMsgNonTraite").html(html.nbreMsgNonTraite);
        		}
        		if($("#nbreMsgTraite").length > 0)
        		{
        			$("#nbreMsgTraite").html(html.nbreMsgTraite);
        		}
        		if($("#nbreMsgAll").length > 0)
        		{
        			$("#nbreMsgAll").html(html.nbreMsgAll);
        		}
        		
        		if($("#nbreCmtActif").length > 0)
        		{
        			$("#nbreCmtActif").html(html.nbreCmtActif);
        		}
        		if($("#nbreCmtInactif").length > 0)
        		{
        			$("#nbreCmtInactif").html(html.nbreCmtInactif);
        		}
        		if($("#nbreCmtAbus").length > 0)
        		{
        			$("#nbreCmtAbus").html(html.nbreCmtAbus);
        		}
        		if($("#nbreCmtAll").length > 0)
        		{
        			$("#nbreCmtAll").html(html.nbreCmtAll);
        		}
            	
            	if(html.error != "")
            	{
            		mettreErreurSurDiv(idDivConteneurPaginHere, html.error, "");
            	}
            	else if(html.info != "")
            	{
            		mettreInfoSurDiv(idDivConteneurPaginHere, html.info, "");
            	}
            	else
            	{
            		removeHere = false;
            		if($(removeAfterPagin).length > 0)
            		{
            			removeHere = true;
            		}
            		
            		
            		if(appendPagination)
            		{
            			$("#"+idDivConteneurPaginHere).append(html.varRetour);
            		}
            		else
            		{
            			$("#"+idDivConteneurPaginHere).html(html.varRetour);
            		}
            		
            		if(removeHere)
            		{
            			$(removeAfterPagin).remove();
            		}
            		
                    numPaginActuel = nroPage;
                    
                    if($.isFunction($.fn.iCheck)) 
                    {
                    	//iCheck for checkbox and radio inputs
                        $("#"+idDivConteneurPaginHere+" input[type='checkbox']").iCheck({
                          checkboxClass: 'icheckbox_minimal-blue',
                          radioClass: 'iradio_minimal-blue'
                        });
                        
                        $('#check_all_list').on('ifChecked', function(event) {
                			$("#liteEltsTab input[name='elt_check_tableau']").iCheck('check');
                		});
                		
                		$('#check_all_list').on('ifUnchecked', function(event) {
                			$("#liteEltsTab input[name='elt_check_tableau']").iCheck('uncheck');
                		});
                    }
                    
                    if($.isFunction($.fn.owlCarousel))
                    {
                    	$(".owl-carousel").owlCarousel({
                    	    items : 1,
                    	    itemsDesktop : [1199,3],
                    	    itemsDesktopSmall : [979,3]
                        });
                    }
            	}
			},
            
            error: function(){
                mettreErreurSurDiv(idDivConteneurPaginHere , commonTabLangue["Probleme d'acces au serveur, veuillez reessayer"], "");
			},
            
            complete: function(){
				retirerLoaderSurDiv(idDivConteneurPaginHere);
			}
		}); 
    }
    