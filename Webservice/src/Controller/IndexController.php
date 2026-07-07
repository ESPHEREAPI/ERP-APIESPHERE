<?php

namespace Webservice\Controller;

use Interop\Container\ContainerInterface;

use Custom\Mvc\Controller\BackOfficeCommonController;

use Application\Core\Utilitaire;
use Application\Manager\AdherentManager;
use Application\Manager\AyantDroitManager;
use Application\Manager\BackAuthManager;
use Application\Manager\PrestataireManager;
use Entity\Adherent;
use Entity\AyantDroit;
use Entity\Prestataire;
use Entity\TauxPrestation;

//from BackjAuth

use Zend\Session\Container;
use Zend\View\Model\ViewModel;
use Zend\View\Model\JsonModel;
use Application\Manager\MenuManager;
use Application\Manager\EmployeManager;


class IndexController extends BackOfficeCommonController
{  
   /**
     * @var \Interop\Container\ContainerInterface
     */
	protected $appliContainer; 
    
    /**
     * @var \Application\Manager\AdherentManager
     */
    protected $adherentManager;
    
    /**
     * @var \Application\Manager\AyantDroitManager
     */
    protected $ayantDroitManager;
    
    /**
     * @var \Application\Manager\BackAuthManager
     */
    protected $backAuthManager;
    
    /**
     * @var \Application\Manager\PrestataireManager
     */
    protected $prestataireManager;
	
	/**
     * @var \Application\Manager\EmployeManager
     */
    protected $employeManager;
	
	//from BackAuth
	/**
     * @var \Application\Manager\MenuManager
     */
    protected $menuManager;
    protected $appliConfig;
	
	protected $tabEquiv = [
                        // Pour les consultations généralistes
                        "CG" => "CS0",
                        "CGJ" => "CS0",
                        "CGN" => "CS0",
        
                        // Pour les consultations spécialistes
                        "CS" => "CS1",
                        "CSJ" => "CS1",
                        "CSN" => "CS1",
        
        
                        // Pour les consultations spécialistes
                        "CP" => "CS2",
                        "CPJ" => "CS2",
                        "CPN" => "CS2",
        
                        // Pour les médicaments
                        "ME01" => "PH02"
                      ];
    
    public function __construct(ContainerInterface $appliContainer, AdherentManager $adherentManager, AyantDroitManager $ayantDroitManager,
                                BackAuthManager $backAuthManager, PrestataireManager $prestataireManager,MenuManager $menuManager, EmployeManager $employeManager)
	{
        $this->appliContainer = $appliContainer;
        
		$this->adherentManager = $adherentManager;
		$this->ayantDroitManager = $ayantDroitManager;
		$this->backAuthManager = $backAuthManager;
		$this->prestataireManager = $prestataireManager;
		$this->menuManager = $menuManager;
		$this->employeManager = $employeManager;
	}
 
    
	
    public function indexAction ()
    {
        var_dump("iiiiiiiii"); exit;
    }
    
    public function recupererDonneesAdherentAction ()
    {
        set_time_limit(0);
        ini_set('memory_limit','256000M');
        
        
        $error = "";
        $utilitaire = new Utilitaire;
        

        $police = $this->params()->fromRoute('police', null);

        $postValues = array_merge_recursive(
            $this->getRequest()->getPost()->toArray(),
            $this->getRequest()->getFiles()->toArray()
            );
        
        $appliConfig =  new \Application\Core\AppliConfig();
        $webserviceServer = $appliConfig->get("webservice_server");
        
        
        $requete_params = array();
        
        // Initialise notre session cURL. On lui donne la requête à exécuter
        $ch = curl_init();
        
        //set option of URL to post to
        curl_setopt($ch, CURLOPT_URL, "http://".$webserviceServer["ip_adress"]."/web_service/public/biometry/get-liste-adherent");
        //set option of request method -----HTTP POST Request
        curl_setopt($ch, CURLOPT_POST, true);
        //The HTTP authentication methods to use
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_ANY);
        //This line sets the parameters to post to the URL
        curl_setopt($ch, CURLOPT_POSTFIELDS, $requete_params);
        //This line makes sure that the response is gotten back to the
        // $response object(see below) and not echoed
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        
        

        // On lance l'exécution de la requête URL.
        if($response = curl_exec($ch)) // Si elle s'est exécutée correctement
        {
        	
        	if(empty($response) || $response == "null")
        	{
        		$error = $this->getTranslator("Transaction non initialisee");
        	}
        	else
        	{
        		// object
        		$returnCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        
        		//Check if there are no errors ie httpresponse == 200 -OK
        		if ($returnCode == 200) {
        			//If there are no errors, the transaction ID is returned
        			$varRetour = json_decode($response, true);
        			$error = $varRetour['error'];
        			$tabAdherent = $varRetour['tabAdherent'];
        			
        			if(!empty($error))
        			{
        			    
        			}
        			else
        			{
        			    $ligneNonInserees = array();
        			    
        			    foreach ($tabAdherent as $unAdherent)
        			    {
        			        $nouveauElement = false;
        			        $unAdherentTravaille = array();
        			        foreach ($unAdherent as $key => $value)
        			        {
        			            if(!empty($value))
        			                $value = trim($value);
        			                
        			            $unAdherentTravaille[$utilitaire->to_camel_case(strtolower($key))] = $value;
                            }
                            
                            if($police && $police != $unAdherentTravaille['police'])
                            {
                                continue;
                            }

        			        
        			        $unAdherentTravaille['codeAdherent'] = $unAdherentTravaille['codeAssure']."_".$unAdherentTravaille['police'];
        			        
							
        			        
        			        if(!empty($unAdherentTravaille['naissance']))
        			        {
        			            $unAdherentTravaille['naissance'] = str_replace(" ", "", $unAdherentTravaille['naissance']);
        			        }
        			        if(!empty($unAdherentTravaille['effetPolice']))
        			        {
        			            $unAdherentTravaille['effetPolice'] = str_replace(" ", "", $unAdherentTravaille['effetPolice']);
        			        }
        			        if(!empty($unAdherentTravaille['echeancePolice']))
        			        {
        			            $unAdherentTravaille['echeancePolice'] = str_replace(" ", "", $unAdherentTravaille['echeancePolice']);
        			        }
        			        
        			        
        			        $adherent = $this->getEntityManager()->find('Entity\Adherent', $unAdherentTravaille['codeAdherent']);
        			        if(!$adherent)
        			        {
        			            $adherent = new \Entity\Adherent;
        			            $nouveauElement = true;
        			        }
        			        
        			        $adherent->exchangeArray($unAdherentTravaille);
							

        			        
        			        if(!empty($unAdherentTravaille['naissance']))
        			        {
        			            $adherent->setNaissance(new \DateTime($unAdherentTravaille['naissance']));
        			        }
        			        if(!empty($unAdherentTravaille['effetPolice']))
        			        {
        			            $adherent->setEffetPolice(new \DateTime($unAdherentTravaille['effetPolice']));
        			        }
        			        if(!empty($unAdherentTravaille['echeancePolice']))
        			        {
        			            $adherent->setEcheancePolice(new \DateTime($unAdherentTravaille['echeancePolice']));
        			        }
							
							
        			        if($adherent->getPolice()=="1017-2130000081")
								{
									$adherent->setSouscripteur("CCIMA");
								}
								
								if($adherent->getPolice()=="1017-2130000101")
								{
									$adherent->setSouscripteur("CIRCB");
								}
								
								if($adherent->getPolice()=="1017-2130000063")
								{
									$adherent->setSouscripteur("PRODEL");
								}
								
								if($adherent->getPolice()=="1017-2130000073")
								{
									$adherent->setSouscripteur("BMN");
								}
								
								if($adherent->getPolice()=="1017-2130000092")
								{
									$adherent->setSouscripteur("CAA-PROJET REGIONAL PCDN");
								}
								
								if($adherent->getPolice()=="1001-2130000020")
								{
									$adherent->setSouscripteur("JICA");
								}
								
								 if($adherent->getPolice()=="1017-2130000100")
								{
									$adherent->setSouscripteur("CAMTEL");
								}
								
								 if($adherent->getPolice()=="1001-2130000032")
								{
									$adherent->setSouscripteur("INTERPOL");
								}
								
								 if($adherent->getPolice()=="1017-2130000131")
								{
									$adherent->setSouscripteur("PDCVEP");
								}
								
							if($adherent->getPolice() == "1017-2130000110")
{
    $codeAdherentsA80 = [
        "39_1017-2130000110",
        "38_1017-2130000110",
        "55_1017-2130000110",
        "56_1017-2130000110",
        "57_1017-2130000110",
        "58_1017-2130000110",
        "59_1017-2130000110",
        "202_1017-2130000110",
        "203_1017-2130000110",
        "258_1017-2130000110"
    ];

    if(in_array($adherent->getCodeAdherent(), $codeAdherentsA80))
    {
        $adherent->setTaux(80);
    }
    else
    {
        $adherent->setTaux(100);
    }
}
								
								
								
        			        try {
        			            if($nouveauElement)
        			            {
									$adherent->setStatut("1");
        			                $this->getEntityManager()->persist($adherent);
        			            }
        			            
        			            $this->getEntityManager()->flush();
        			            
        			        } catch (\Exception $e) {
        			            $unAdherentTravaille["messageErreur"] = $e->getMessage();
        			            $ligneNonInserees[] = $unAdherentTravaille;
        			        }
        			    }
        			    
        			    
        			    if(count($ligneNonInserees))
        			    {
        			        $contenuFichier = "";
        			        foreach ($ligneNonInserees as $uneLigne)
        			        {
        			            if($contenuFichier != "")
        			            {
        			                $contenuFichier .=  "\n";
        			            }
        			               
        			            $contenuFichier .= json_encode($uneLigne);
        			        }
        			            
        			        
        			        $myfile = fopen(__DIR__."/../../../../public/docs/log/echec_insertion/echec_adherent_".date("Y-m-d-H-i-s").".txt", "w");
        			        

        			        fwrite($myfile, $contenuFichier);
        			       
        			        fclose($myfile);
        			    }
        			}
        		}
        		else
        		{
        			
        			$error = $this->getTranslator("Erreur de communication avec le serveur")." : <b>".$returnCode."</b>";
        		}
        	}
        }
        else // S'il y a une erreur, on affiche "Erreur", suivi du détail de l'erreur.
        {
        	$error =  curl_error($ch);
        }
        
        // On ferme notre session cURL.
        curl_close($ch);
        
        
        var_dump($error);
        exit;
    }
    
    public function recupererDonneesAyantDroitAction ()
    {
        set_time_limit(0);
        ini_set('memory_limit','256000M');
        
        $error = "";
        $utilitaire = new Utilitaire;
        

        $policeFromRoute = $this->params()->fromRoute('police', null);

        $postValues = array_merge_recursive(
            $this->getRequest()->getPost()->toArray(),
            $this->getRequest()->getFiles()->toArray()
            );
        
        $appliConfig =  new \Application\Core\AppliConfig();
        $webserviceServer = $appliConfig->get("webservice_server");
        
        
        $requete_params = array();
        
        // Initialise notre session cURL. On lui donne la requête à exécuter
        $ch = curl_init();
        
        //set option of URL to post to
        curl_setopt($ch, CURLOPT_URL, "http://".$webserviceServer["ip_adress"]."/web_service/public/biometry/get-liste-ayant-droit");
        //set option of request method -----HTTP POST Request
        curl_setopt($ch, CURLOPT_POST, true);
        //The HTTP authentication methods to use
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_ANY);
        //This line sets the parameters to post to the URL
        curl_setopt($ch, CURLOPT_POSTFIELDS, $requete_params);
        //This line makes sure that the response is gotten back to the
        // $response object(see below) and not echoed
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        
        
        
        // On lance l'exécution de la requête URL.
        if($response = curl_exec($ch)) // Si elle s'est exécutée correctement
        {
            
            if(empty($response) || $response == "null")
            {
                $error = $this->getTranslator("Transaction non initialisee");
            }
            else
            {
                // object
                $returnCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
                //Check if there are no errors ie httpresponse == 200 -OK
                if ($returnCode == 200) {
                    //If there are no errors, the transaction ID is returned
                    $varRetour = json_decode($response, true);
                    $error = $varRetour['error'];
                    $tabAyantDroit = $varRetour['tabAyantDroit'];
                    
                    if(!empty($error))
                    {
                        
                    }
                    else
                    {
                        $ligneNonInserees = array();
                        
                        // var_dump(count($tabAyantDroit)); exit;
                        
                        foreach ($tabAyantDroit as $unAyantDroit)
                        {
                            $nouveauElement = false;
                            $unAyantDroitTravaille = array();
                            foreach ($unAyantDroit as $key => $value)
                            {
                                if(!empty($value))
                                    $value = trim($value);
                                    
                                    $unAyantDroitTravaille[$utilitaire->to_camel_case(strtolower($key))] = $value;
                            }
                            
                            
                            $codeAdherent = $unAyantDroitTravaille['codeAssure']."_".$unAyantDroitTravaille['police'];
                            
                            
                            if($policeFromRoute && $policeFromRoute != $unAyantDroitTravaille['police'])
                            {
                                continue;
                            }
                            
                            
                            $adherent = $this->getEntityManager()->find('Entity\Adherent', $codeAdherent);
                            if($adherent)
                            {
                                //var_dump($unAyantDroit);
								
                                
                                $unAyantDroitTravaille['codeAyantDroit'] = $codeAdherent."_".$unAyantDroitTravaille['codeAyantD'];
                                $unAyantDroitTravaille['codeAdherent'] = $codeAdherent;
                                $unAyantDroitTravaille['nom'] = $unAyantDroitTravaille['ayantsDroits'];
		                        $unAyantDroitTravaille['lienpare'] = str_replace(" ", "",$unAyantDroitTravaille['lienpare']);
									
                              
                                
                                
                                if(!empty($unAyantDroitTravaille['naissance']))
                                {
                                    $unAyantDroitTravaille['naissance'] = str_replace(" ", "", $unAyantDroitTravaille['naissance']);
                                }
                                
                                $ayantDroit = $this->getEntityManager()->find('Entity\AyantDroit', $unAyantDroitTravaille['codeAyantDroit']);
									
                                if(!$ayantDroit)
                                {
									
                                    $ayantDroit = new AyantDroit;
                                    $nouveauElement = true;
                                }
                                
                                $ayantDroit->exchangeArray($unAyantDroitTravaille);
                                
                                if(!empty($unAyantDroitTravaille['naissance']))
                                {
                                    $ayantDroit->setNaissance(new \DateTime($unAyantDroitTravaille['naissance']));
                                }
                                
                                $ayantDroit->setCodeAdherent($adherent);
								
								

                                try {
                                    
                                    if($nouveauElement)
                                    {
                                        $this->getEntityManager()->persist($ayantDroit);
																			
                                    }
                                    
                                    $this->getEntityManager()->flush();
									
                                    
                                } catch (\Exception $e) {
                                    $ligneNonInserees[] = $unAyantDroitTravaille;
                                }
                            }
                        }
                        
						
                        
                        if(count($ligneNonInserees))
                        {
                            $contenuFichier = "";
                            foreach ($ligneNonInserees as $uneLigne)
                            {
                                if($contenuFichier != "")
                                {
                                    $contenuFichier .=  "\n";
                                }
                                
                                $contenuFichier .= json_encode($uneLigne);
                            }
                            
                            
                            $myfile = fopen(__DIR__."/../../../../public/docs/log/echec_insertion/echec_ayant_droit_".date("Y-m-d-H-i-s").".txt", "w");
                            
                            
                            fwrite($myfile, $contenuFichier);
                            
                            fclose($myfile);
                        }
                    }
                }
                else
                {
                    
                    $error = $this->getTranslator("Erreur de communication avec le serveur")." : <b>".$returnCode."</b>";
                }
            }
        }
        else // S'il y a une erreur, on affiche "Erreur", suivi du détail de l'erreur.
        {
            $error =  curl_error($ch);
        }
        
        // On ferme notre session cURL.
        curl_close($ch);
        
        
        var_dump($error);
        exit;
    } 
    
    public function recupererDonneesTauxPrestationAction ()
    {
        set_time_limit(0);
        ini_set('memory_limit','256000M');
        
        $utilitaire = new Utilitaire;
        
        $postValues = array_merge_recursive(
            $this->getRequest()->getPost()->toArray(),
            $this->getRequest()->getFiles()->toArray()
            );
        
        $appliConfig =  new \Application\Core\AppliConfig();
        $webserviceServer = $appliConfig->get("webservice_server");
        
        
        $requete_params = array();
        
        // Initialise notre session cURL. On lui donne la requête à exécuter
        $ch = curl_init();
        
        //set option of URL to post to
        curl_setopt($ch, CURLOPT_URL, "http://".$webserviceServer["ip_adress"]."/web_service/public/biometry/get-liste-taux-prestation");
        //set option of request method -----HTTP POST Request
        curl_setopt($ch, CURLOPT_POST, true);
        //The HTTP authentication methods to use
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_ANY);
        //This line sets the parameters to post to the URL
        curl_setopt($ch, CURLOPT_POSTFIELDS, $requete_params);
        //This line makes sure that the response is gotten back to the
        // $response object(see below) and not echoed
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        
        
        // On lance l'exécution de la requête URL.
        if($response = curl_exec($ch)) // Si elle s'est exécutée correctement
        {
            
            if(empty($response) || $response == "null")
            {
                $error = $this->getTranslator("Transaction non initialisee");
            }
            else
            {
                // object
                $returnCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
                //Check if there are no errors ie httpresponse == 200 -OK
                if ($returnCode == 200) {
                    //If there are no errors, the transaction ID is returned
                    $varRetour = json_decode($response, true);
                    $error = $varRetour['error'];
                    $tabTauxPrestation = $varRetour['tabTauxPrestation'];
                    
                    if(!empty($error))
                    {
                        
                    }
                    else
                    {
                        // var_dump($tabTauxPrestation); exit;
                        
                        $tabTypePrestation = array();
                        
                        $ligneNonInserees = array();
                        
                        foreach ($tabTauxPrestation as $unTauxPrestation)
                        {
                            $nouveauElement = false;
                            $unTauxPrestationTravaille = array();
//                             foreach ($unTauxPrestation as $key => $value)
//                             {
//                                 if(!empty($value))
//                                     $value = trim($value);
                                    
//                                     $unTauxPrestationTravaille[$utilitaire->to_camel_case(strtolower($key))] = $value;
//                             }


                            if(!isset($unTauxPrestation['CODEPRES']) || empty($unTauxPrestation['CODEPRES']) ||
                               !isset($unTauxPrestation['CODEINTE']) || empty($unTauxPrestation['CODEINTE']) ||
                               !isset($unTauxPrestation['NUMEPOLI']) || empty($unTauxPrestation['NUMEPOLI']) ||
                               !isset($unTauxPrestation['NUMEGROU']) || empty($unTauxPrestation['NUMEGROU']) ||
                               !isset($unTauxPrestation['TYPCONSU']) || empty($unTauxPrestation['TYPCONSU']))
                            {
                                $ligneNonInserees[] = $unTauxPrestation;
                                continue;
                            }
							
							if(array_key_exists($unTauxPrestation['CODEPRES'], $this->tabEquiv))
                            {
                                $unTauxPrestation['CODEPRES'] = $this->tabEquiv[$unTauxPrestation['CODEPRES']];
                            }                            
                            
                            if(!isset($unTauxPrestation['TAUXCOUV']) || empty($unTauxPrestation['TYPCONSU']))
                            {
                                $unTauxPrestation['TAUXCOUV'] = null;
                            }
                            else
                            {
                                $unTauxPrestation['TAUXCOUV'] = trim($unTauxPrestation['TAUXCOUV']);
                            }
                            
                            if(!isset($unTauxPrestation['VALEPLAF']) || empty($unTauxPrestation['VALEPLAF']))
                            {
                                $unTauxPrestation['VALEPLAF'] = null;
                            }
                            else
                            {
                                $unTauxPrestation['VALEPLAF'] = trim($unTauxPrestation['VALEPLAF']);
                            }
                            
                            $unTauxPrestationTravaille['typePrestation'] = trim($unTauxPrestation['CODEPRES']);
                            $unTauxPrestationTravaille['police'] = trim($unTauxPrestation['CODEINTE']."-".trim($unTauxPrestation['NUMEPOLI']));
                            $unTauxPrestationTravaille['groupe'] = trim($unTauxPrestation['NUMEGROU']);
                            $unTauxPrestationTravaille['taux'] = $unTauxPrestation['TAUXCOUV'];
                            $unTauxPrestationTravaille['plafond'] = $unTauxPrestation['VALEPLAF'];
							
                            
                            
                            
                            $typePrestation = $this->getEntityManager()->find('Entity\TypePrestation', $unTauxPrestationTravaille['typePrestation']);
                            if(!$typePrestation)
                            {
                                $unTypePrestationTravaille = array("id" => $unTauxPrestationTravaille['typePrestation'], "nom" => trim($unTauxPrestation['TYPCONSU']),"affiche" =>"-1", "categorie" =>"non_defini");
                                
                                $typePrestation = new \Entity\TypePrestation();
                                $typePrestation->exchangeArray($unTypePrestationTravaille);
								//var_dump( $typePrestation);exit;
                                
                                try {
                                    
                                    $this->getEntityManager()->persist($typePrestation);
                                    
                                    $this->getEntityManager()->flush();
                                    
                                } catch (\Exception $e) {
										echo "Erreur typetaux capturee: " . $e->getMessage();
                                    $ligneNonInserees[] = $unTypePrestationTravaille;
                                }
                            }
                            
                            
                            $paramsTypePrestation = array('typePrestation' => $unTauxPrestationTravaille['typePrestation'],
                                                          "police" => $unTauxPrestationTravaille['police'],
                                                          "groupe" => $unTauxPrestationTravaille['groupe']);
                            
                            
                            $tauxPrestation = $this->getEntityManager()->getRepository('Entity\TauxPrestation')->findOneBy($paramsTypePrestation);
                            if(!$tauxPrestation)
                            {
                                $tauxPrestation = new \Entity\TauxPrestation;
                                $nouveauElement = true;
                            }
//                             else
//                             {
//                                 var_dump($paramsTypePrestation);
//                             }
                            
                            $tauxPrestation->exchangeArray($unTauxPrestationTravaille);
                            $tauxPrestation->setTypePrestation($typePrestation);
							
							//Pour mettre le taux de oncc a 100%.
							if ($tauxPrestation->getPolice()== "1017-2130000062" || $tauxPrestation->getPolice()== "1017-2130000084" || $tauxPrestation->getPolice()== "1017-2130000110")
							{
								$tauxPrestation->setTaux(100);
							}
                            
                            
                            try {
                                
                                if($nouveauElement)
                                {
                                    $this->getEntityManager()->persist($tauxPrestation);
                                }
                                
                                $this->getEntityManager()->flush();
                                
                            } catch (\Exception $e) {
								echo "Erreur taux capturee: " . $e->getMessage();
                                $ligneNonInserees[] = $unTauxPrestationTravaille;
                            }
                        }
                        
                        
                        if(count($ligneNonInserees))
                        {
                            $contenuFichier = "";
                            foreach ($ligneNonInserees as $uneLigne)
                            {
                                if($contenuFichier != "")
                                {
                                    $contenuFichier .=  "\n";
                                }
                                
                                $contenuFichier .= json_encode($uneLigne);
                            }
                            
                            
                            $myfile = fopen(__DIR__."/../../../../public/docs/log/echec_insertion/echec_tauxPrestation_".date("Y-m-d-H-i-s").".txt", "w");
                            
                            
                            fwrite($myfile, $contenuFichier);
                            
                            fclose($myfile);
                        }
                    }
                }
                else
                {
                    
                    $error = $this->getTranslator("Erreur de communication avec le serveur")." : <b>".$returnCode."</b>";
                }
            }
        }
        else // S'il y a une erreur, on affiche "Erreur", suivi du détail de l'erreur.
        {
            $error =  curl_error($ch);
        }
        
        // On ferme notre session cURL.
        curl_close($ch);
        
        
        var_dump($error);
        exit;
    }
    
    
    public function getListeAdherentAction()
    {
        // header("Content-Type:application/json");
        
        if(!empty($_GET["noms"]))
        {
            $noms = $_GET["noms"];
            $items = $this->getItems($noms);
            if(empty($items))
            {
                $this->jsonResponse(200, "Adherent introuvable", NULL);
            } 
            else 
            {
                $this->jsonResponse(200, "Adherent trouve", $items);
            }
        } 
        else 
        {
            $this->jsonResponse(400, "requete invalide", NULL);
        }
        
        exit;
    }
	
	public function getListeAdherentSecugenAction()
    {
        // header("Content-Type:application/json");
        
        if(!empty($_GET["noms"]))
        {
            $noms = $_GET["noms"];
            $items = $this->getItemsSecugen($noms);
            if(empty($items))
            {
                $this->jsonResponse(200, "Adherent introuvable", NULL);
            } 
            else 
            {
                $this->jsonResponse(200, "Adherent trouve", $items);
            }
        } 
        else 
        {
            $this->jsonResponse(400, "requete invalide", NULL);
        }
        
        exit;
    }
    
    public function genererVisiteAction()
    {
        $utilitaire = new Utilitaire();
        $statusMessage = "";
        $status = 200;
        $visiteArray = null;
		
		$dateServeur= date("Y-m-d");
		//var_dump($dateServeur);exit();
		
        
        if(empty($_GET["codeAdherent"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le code de l'adherent\n");
        }
        if(empty($_GET["prestataire"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le prestataire\n");
        }
        if(empty($_GET["telephone"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le numero de telephone\n");
        }
        
        if($status == 200)
        {
            $ayantDroit = null;
            $adherent = $this->getEntityManager()->find('Entity\Adherent', trim($_GET["codeAdherent"]));
            $prestataire = $this->getEntityManager()->find('Entity\Prestataire', trim($_GET["prestataire"]));
            $telephone = trim($_GET["telephone"]);
            if(!empty($_GET["codeAyantDroit"]))
            {
                if($_GET["codeAyantDroit"] != null && $_GET["codeAyantDroit"] != "null")
                {
                    $ayantDroit = $this->getEntityManager()->find('Entity\AyantDroit', trim($_GET["codeAyantDroit"]));
                    if(!$ayantDroit)
                    {
                        $status = 400;
                        $statusMessage .= $this->getTranslator("Impossible de trouver l'ayant droit ".$_GET["codeAyantDroit"]."\n");
                    }
                }
            }
            
            
            if(!$adherent)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Impossible de trouver l'adherent\n");
            }
            elseif($adherent->getStatut() == "-1")
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Le contrat de l'assure a ete suspendu\nVeuillez le diriger vers Zenithe Insurance\n");
            }
			elseif(!empty($adherent->getEcheancePolice()) && $adherent->getEcheancePolice()->format("Y-m-d") < $dateServeur)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Le contrat de l'assure a expire\n");
            }
			elseif(!empty($adherent->getConsAp()) && $adherent->getConsAp()>=$adherent->getPlafondAssurep()*0.8 && $adherent->getPolice()=="1017-2130000123")
            {
               $status = 400;
               $statusMessage .= $this->getTranslator("Prestations impossible\nVeuillez le diriger vers l'assure Zenithe\n");
            }
            
            if(!$prestataire)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Impossible de trouver le prestataire\n");
            }
            
            
            if($status == 200)
            {
                // Debut du mode transactionnel
                $this->getEntityManager()->getConnection()->beginTransaction(); // On suspend l'auto-commit
                try {
                    $paramsCodeVisite = $this->genererCodeVisite($prestataire->getId());
                    $idVisite = $paramsCodeVisite['idVisite'];
                    $codeCourtVisite = $paramsCodeVisite['codeCourtVisite'];
                    
                    $visite = new \Entity\Visite;
                    $visiteArray = array("id" => $idVisite,
                        "codeAdherent" => $adherent,
                        "codeAyantDroit" => $ayantDroit,
                        "prestataire" => $prestataire,
                        "codeCourt" => $codeCourtVisite,
                        "telephone" => $telephone,
                        "date" => new \DateTime(date("Y-m-d H:i:s")),
                    );
                    $visite->exchangeArray($visiteArray);
                    
                    
                    
                    
                    
                    $this->getEntityManager()->persist($visite);
                    $this->getEntityManager()->flush();
                    
                    $this->getEntityManager()->getConnection()->commit();
                    
                    
                    $codeAyantDroit = null;
                    if($ayantDroit)
                    {
                        $codeAyantDroit = $ayantDroit->getCodeAyantDroit();
                    }
                    
                    $visiteArray = array("id" => $codeCourtVisite,
                        "codeAdherent" => $adherent->getCodeAdherent(),
                        "codeAyantDroit" => $codeAyantDroit,
                        "prestataire" => $prestataire->getId(),
                    );
                    
                    
                } catch (\Exception $e) {
                    $this->getEntityManager()->getConnection()->rollback();
                    $this->getEntityManager()->close();
                    $status = 400;
                    $statusMessage .= $this->getTranslator("Probleme lors de l'enregistrement\n");
                }
                
                if($status == 200)
                {
                    try {
                        
                        $prestataire->afficheChaine();
                        
                        $contenuSms = $this->getTranslator("Centre hospitalier :")." ".$prestataire->getNom()."\n";
                        $contenuSms .= $this->getTranslator("Code de la visite :")." ".$codeCourtVisite."\n";
                        $contenuSms .= $this->getTranslator("Ce code sera utilise tout au long du processus.");
                        
                        $utilitaire->sendSmsHttp(array($telephone), $contenuSms);
                    } catch (\Exception $e) {
                        
                    }
                }
            }
        }
        
        $this->jsonResponse($status, $statusMessage, $visiteArray);
        
        exit;
    }
	
	
    
    public function genererVisiteLoginAction()
    {
        $utilitaire = new Utilitaire();
        $statusMessage = "";
        $status = 200;
        $visiteArray = null;
		
		$dateServeur= date("Y-m-d");
		//var_dump($dateServeur);exit();
		
	
		
        
        if(empty($_GET["codeAdherent"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le code de l'adherent\n");
        }
        if(empty($_GET["prestataire"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le prestataire\n");
        }
		
		 if(empty($_GET["codeAyantDroit"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le code de l'ayantDroit\n");
        }
		
		if(empty($_GET["login"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le login ou l email de l employe\n");
        }
		
        if(empty($_GET["telephone"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le numero de telephone\n");
        }
        
        if($status == 200)
        {
            $ayantDroit = null;
            $adherent = $this->getEntityManager()->find('Entity\Adherent', trim($_GET["codeAdherent"]));
            $prestataire = $this->getEntityManager()->find('Entity\Prestataire', trim($_GET["prestataire"]));
			
			if(isset($_GET["login"]))
            {
            $loginOuEmail=trim($_GET["login"]);
            }
			
            $telephone = trim($_GET["telephone"]);
            if(!empty($_GET["codeAyantDroit"]))
            {
                if($_GET["codeAyantDroit"] != null && $_GET["codeAyantDroit"] != "null")
                {
                    $ayantDroit = $this->getEntityManager()->find('Entity\AyantDroit', trim($_GET["codeAyantDroit"]));
                    if(!$ayantDroit)
                    {
                        $status = 400;
                        $statusMessage .= $this->getTranslator("Impossible de trouver l'ayant droit ".$_GET["codeAyantDroit"]."\n");
                    }
                }
            }
            
            
            if(!$adherent)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Impossible de trouver l'adherent\n");
            }
            elseif($adherent->getStatut() == "-1")
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Le contrat de l'assure a ete suspendu\nVeuillez le diriger vers Zenithe Insurance\n");
            }
			elseif(!empty($adherent->getEcheancePolice()) && $adherent->getEcheancePolice()->format("Y-m-d") < $dateServeur)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Le contrat de l'assure a expire\n");
            }
			elseif(!empty($adherent->getConsAp()) && $adherent->getConsAp()>=$adherent->getPlafondAssurep()*0.8 && $adherent->getPolice()=="1017-2130000123")
            {
               $status = 400;
               $statusMessage .= $this->getTranslator("Prestations impossible pour cet assure\nVeuillez le diriger vers Zenithe Insurance\n pour un bon de prise en charge");
            }
            
            if(!$prestataire)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Impossible de trouver le prestataire\n");
            }
            
            
            if($status == 200)
            {
                // Debut du mode transactionnel
                $this->getEntityManager()->getConnection()->beginTransaction(); // On suspend l'auto-commit
                try {
                    $paramsCodeVisite = $this->genererCodeVisite($prestataire->getId());
                    $idVisite = $paramsCodeVisite['idVisite'];
                    $codeCourtVisite = $paramsCodeVisite['codeCourtVisite'];
                    
                    $visite = new \Entity\Visite;
                    $visiteArray = array("id" => $idVisite,
                        "codeAdherent" => $adherent,
                        "codeAyantDroit" => $ayantDroit,
                        "prestataire" => $prestataire,
                        "codeCourt" => $codeCourtVisite,
                        "telephone" => $telephone,
                        "date" => new \DateTime(date("Y-m-d H:i:s")),
                    );
                    $visite->exchangeArray($visiteArray);
					
					
                    if(isset($_GET["login"]))
                    {
                    $employe = $this->employeManager->getEmployeByLoginOuEmail($_GET["login"]);
					
				    $visite->setEmploye($employe);
					
                    }
					
                    
                    
                    
                    $this->getEntityManager()->persist($visite);
                    $this->getEntityManager()->flush();
                    
                    $this->getEntityManager()->getConnection()->commit();
                    
                    
                    $codeAyantDroit = null;
                    if($ayantDroit)
                    {
                        $codeAyantDroit = $ayantDroit->getCodeAyantDroit();
                    }
                    
                    $visiteArray = array("idVisite" => $idVisite,
										 "id" => $codeCourtVisite,
										 "codeAdherent" => $adherent->getCodeAdherent(),
										 "codeAyantDroit" => $codeAyantDroit,
										 "prestataire" => $prestataire->getId(),
									);
					
					// $this->connexionWebService($loginOuEmail);
                    
                    
                } catch (\Exception $e) {
                    $this->getEntityManager()->getConnection()->rollback();
                    $this->getEntityManager()->close();
                    $status = 400;
                    $statusMessage .= $this->getTranslator("Probleme lors de l'enregistrement\n");
                }
                
                if($status == 200)
                {
                    try {
					if((isset($_GET["prestation"]) && $_GET["prestation"]=="consultation") || !isset($_GET["prestation"] ))
						{
                        
                        $prestataire->afficheChaine();
                        
                        $contenuSms = $this->getTranslator("Centre hospitalier :")." ".$prestataire->getNom()."\n";
                        $contenuSms .= $this->getTranslator("Code de la visite :")." ".$codeCourtVisite."\n";
                        $contenuSms .= $this->getTranslator("Ce code sera utilise tout au long du processus.");
                        
                        $utilitaire->sendSmsHttp(array($telephone), $contenuSms);
						}
                    } catch (\Exception $e) {
                        
                    }
                }
            }
        }
        
        $this->jsonResponse($status, $statusMessage, $visiteArray);
        
        exit;
    }
    
    
    public function getListeAyantDroitAction()
    {
        $statusMessage = "";
        $status = 200;
        $tabAyantDroitRetour = null;
        
        if(empty($_GET["codeAdherent"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le code de l'adherent\n");
        }
        
        if($status == 200)
        {
            $tabParams = array(
                            "codeAdherent" => trim($_GET["codeAdherent"]),
                            "statut" => "1"
                        );
            
            
            $tabAyantDroit = $this->ayantDroitManager->getListeAyantDroitTabParams($tabParams);
            $tabAyantDroitRetour = array();
            if(count($tabAyantDroit) >0)
            {
                foreach ($tabAyantDroit as $unAyantDroit)
                {
                    $dateNaissanceAyantDroit = "";
                    if($unAyantDroit->getNaissance())
                    {
                        $dateNaissanceAyantDroit = $unAyantDroit->getNaissance()->format("d/m/Y");
                    }
					
					if($unAyantDroit->getEnrole()=="-1")
			        {
				       $unAyantDroit->setEnrole("NON ENROLE");
			        }
		            else if($unAyantDroit->getEnrole()=="1")
			        {
				        $unAyantDroit->setEnrole("ENROLE");
			        }
                    
                    $tabAyantDroitRetour[] = array("codeAyantDroit" => $unAyantDroit->getCodeAyantDroit(),
                                                   "codeAdherent" => $unAyantDroit->getCodeAdherent()->getCodeAdherent(),
                                                   "nom" => $unAyantDroit->getNom(),
                                                   "sexe" => $unAyantDroit->getSexe(),
                                                   "naissance" => $dateNaissanceAyantDroit,
												   "police" => $unAyantDroit->getPolice(),
												   //"enrole" => $unAyantDroit->getEnrole(),
												   "telephone" => $unAyantDroit->getTelephone()
                                                    
                    );
                }
            }
			
        }
        
        $this->jsonResponse($status, $statusMessage, $tabAyantDroitRetour);
        
        exit;
    }
    
    public function setAdherentEnroleAction()
    {
		
        $statusMessage = "";
        $status = 200;
        $data = "-1";
        
        if(empty($_GET["codeAdherent"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le code de l'adherent\n");
        }
		 if(empty($_GET["telephone"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le numero de telephone\n");
        }
        
        if($status == 200)
        {
            $adherent = $this->getEntityManager()->find('Entity\Adherent', trim($_GET["codeAdherent"]));
            if(!$adherent)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Impossible de trouver l'adherent\n");
            }
            
            if($status == 200)
            {
                $adherent->setEnrole("1");
			    $adherent->setTelephone($_GET["telephone"]);
				$adherent->setDateEnrole(new \DateTime(date("Y-m-d H:i:s")));

                
                try {
                    
                    $this->getEntityManager()->flush();
                    $data = "1";
                    
                } catch (\Exception $e) {
                    $status = 400;
                    $statusMessage .= $this->getTranslator("Problème lors de la mise à jour du statut de l'enrolement\n");
                }
                
            }
        }
        
        $this->jsonResponse($status, $statusMessage, $data);
        
        exit;
    }
	
	
	public function setAdherentEnroleSecugenAction()
    {
		$utilitaire = new Utilitaire();
        $statusMessage = "";
        $status = 200;
        $data = "-1";
        
        if(empty($_GET["codeAdherent"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le code de l'adherent\n");
        }
		 if(empty($_GET["telephone"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le numero de telephone\n");
        }
        
        if($status == 200)
        {
            $adherent = $this->getEntityManager()->find('Entity\Adherent', trim($_GET["codeAdherent"]));
            if(!$adherent)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Impossible de trouver l'adherent\n");
            }
            
            if($status == 200)
            {
                $adherent->setEnrole("2");
			    $adherent->setTelephone($_GET["telephone"]);
				$adherent->setDateEnrole(new \DateTime(date("Y-m-d H:i:s")));
				
                
                try {
                    
                    $this->getEntityManager()->flush();
                    $data = "2";
                    
                } catch (\Exception $e) {
                    $status = 400;
                    $statusMessage .= $this->getTranslator("Problème lors de la mise à jour du statut de l'enrolement\n");
                }
				
				
				try { 
				       if(isset($_GET["langue"]))
					   {
					
                        if($_GET["langue"]=="1")
						{
                        $adherent->afficheChaine();
                        
                        //$contenuSms =  $adherent->getAssurePrincipal()."\n";
						$contenuSms = $this->getTranslator("Monsieur/Madame\n");
                        $contenuSms .= $this->getTranslator("Pour une prise en charge rapide, trouvez la liste de nos centres de sante agrees en cliquant sur  ce lien\n");
						$contenuSms .= $this->getTranslator("https://zenitheinsurance.com/fr/centres-sante-agrees");
                        
                        $utilitaire->sendSmsHttp(array($adherent->getTelephone()), $contenuSms);
						}
						else if($_GET["langue"]=="2")
						{
						 $adherent->afficheChaine();
                        
                        //$contenuSms =  $adherent->getAssurePrincipal()."\n";
						$contenuSms = $this->getTranslator("Dear sir/Madame\n");
                        $contenuSms .= $this->getTranslator("For quick follow up, find the list of our approved health centers by clicking on this link\n");
						$contenuSms .= $this->getTranslator("https://zenitheinsurance.com/en/centres-sante-agrees");
                        
                        $utilitaire->sendSmsHttp(array($adherent->getTelephone()), $contenuSms);
						}
						
					   }
                    } catch (\Exception $e) {
                        
                    }
				
                
            }
        }
        
        $this->jsonResponse($status, $statusMessage, $data);
        
        exit;
    }
	
	public function setAyantDroitEnroleAction()
    {
		$utilitaire = new Utilitaire();
        $statusMessage = "";
        $status = 200;
        $data = "-1";
        
        if(empty($_GET["codeAyantDroit"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le code de l'ayant droit\n");
        }
        if(empty($_GET["telephone"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez spécifier le numero de telephone\n");
        }
        if($status == 200)
        {
            $ayantDroit = $this->getEntityManager()->find('Entity\AyantDroit', trim($_GET["codeAyantDroit"]));
            if(!$ayantDroit)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Impossible de trouver l'ayant droit\n");
            }
            
            if($status == 200)
            {
                $ayantDroit->setEnrole("1");
				$ayantDroit->setTelephone($_GET["telephone"]);
				$ayantDroit->setDateEnrole(new \DateTime(date("Y-m-d H:i:s")));
				
                
                try {
                    
                    $this->getEntityManager()->flush();
                    $data = "1";
                    
                } catch (\Exception $e) {
                    $status = 400;
                    $statusMessage .= $this->getTranslator("Problème lors de la mise à jour du statut de l'enrolement\n");
                }
				
				
				try{
						if(isset($_GET["langue"]))
					   {
					
                        if($_GET["langue"]=="1")
						{
                        $ayantDroit->afficheChaine();
                        
                        $contenuSms = $this->getTranslator("Monsieur/Madame\n");
                        $contenuSms .= $this->getTranslator("Pour une prise en charge rapide, trouvez la liste de nos centres de sante agrees en cliquant sur  ce lien\n");
						$contenuSms .= $this->getTranslator("https://zenitheinsurance.com/fr/centres-sante-agrees");
                        
                        $utilitaire->sendSmsHttp(array($ayantDroit->getTelephone()), $contenuSms);
						}
						else if($_GET["langue"]=="2")
						{
						 $ayantDroit->afficheChaine();
                        
                        $contenuSms = $this->getTranslator("Dear sir/Madame\n");
                        $contenuSms .= $this->getTranslator("For quick follow up, find the list of our approved health centers by clicking on this link\n");
						$contenuSms .= $this->getTranslator("https://zenitheinsurance.com/en/centres-sante-agrees");
                        
                        $utilitaire->sendSmsHttp(array($ayantDroit->getTelephone()), $contenuSms);
						}
                       }
                    } catch (\Exception $e) {
                        
                    }
					
					
                
            }
        }
        
        $this->jsonResponse($status, $statusMessage, $data);
        
        exit;
    }
    
    public function connexionAction()
    {
        $statusMessage = "";
        $status = 200;
        $data = "-1";
        
        if(empty($_GET["login"]) || empty($_GET["motPasse"]))
        {
            $status = 400;
            $statusMessage .= $this->getTranslator("Veuillez specifier le login (ou l'adresse mail) et le mot de passe");
        }
        
        if($status == 200)
        {
            $userConnect = $this->backAuthManager->connexion(trim($_GET["login"]), trim($_GET["motPasse"]));
            if(!$userConnect)
            {
                $status = 400;
                $statusMessage .= $this->getTranslator("Login or password incorrect");
            }
            else
            {
                $data = "1";
            }
        }
        
        $this->jsonResponse($status, $statusMessage, $data);
        
        exit;
    }  
    
    public function viderDonneesUnPrestataireAction()
    {
        $varRetour = array("succes" => true, "error" => "");
        
        var_dump($varRetour); exit;
        
        $idPrestataire = $this->params()->fromRoute('idPrestataire', null);
        if(empty($idPrestataire))
        {
            $varRetour["succes"] = false;
            $varRetour["error"] = $this->getTranslator("Veuillez renseigner le prestataire dans l'url");
        }
        else
        {
            $prestataire = $this->getEntityManager()->find('Entity\Prestataire', $idPrestataire);
            if(!$prestataire)
            {
                $varRetour["succes"] = false;
                $varRetour["error"] = $this->getTranslator("Impossible de trouver le prestataire, verifiez que le code du prestataire est correct");
            }
            else
            {
                $varRetour = $this->prestataireManager->viderDonneesUnPrestataire($prestataire);
            }
        }
        
        echo json_encode($varRetour);
        exit;
    }

    
    public function viderDonneesUneVisiteAction()
    {
        $varRetour = array("succes" => true, "error" => "");
        
        var_dump($varRetour); exit;
        
        $idVisite = $this->params()->fromRoute('idVisite', null);
        if(empty($idVisite))
        {
            $varRetour["succes"] = false;
            $varRetour["error"] = $this->getTranslator("Veuillez renseigner la visite dans l'url");
        }
        else
        {
            $visite = $this->getEntityManager()->find('Entity\Visite', $idVisite);
            if(!$visite)
            {
                $varRetour["succes"] = false;
                $varRetour["error"] = $this->getTranslator("Impossible de trouver la visite, verifiez que le code de la visite est correct");
            }
            else
            {
                $varRetour = $this->prestataireManager->viderDonneesUneVisite($visite);
            }
        }
        
        echo json_encode($varRetour);
        exit;
    }

    public function desactiverDonneesAdherentAction()
    {
        set_time_limit(0);
        ini_set('memory_limit','256000M');
		$tabAdherentAdesactiver = array();
        
        
        $error = "";
        $utilitaire = new Utilitaire;
        

        $police = $this->params()->fromRoute('police', null);

        $postValues = array_merge_recursive(
            $this->getRequest()->getPost()->toArray(),
            $this->getRequest()->getFiles()->toArray()
            );
        
        $appliConfig =  new \Application\Core\AppliConfig();
        $webserviceServer = $appliConfig->get("webservice_server");
        
        
        $requete_params = array();
        
        // Initialise notre session cURL. On lui donne la requête à exécuter
        $ch = curl_init();
        
        //set option of URL to post to
        curl_setopt($ch, CURLOPT_URL, "http://".$webserviceServer["ip_adress"]."/web_service/public/biometry/get-liste-adherent");
        //set option of request method -----HTTP POST Request
        curl_setopt($ch, CURLOPT_POST, true);
        //The HTTP authentication methods to use
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_ANY);
        //This line sets the parameters to post to the URL
        curl_setopt($ch, CURLOPT_POSTFIELDS, $requete_params);
        //This line makes sure that the response is gotten back to the
        // $response object(see below) and not echoed
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        
        

        // On lance l'exécution de la requête URL.
        if($response = curl_exec($ch)) // Si elle s'est exécutée correctement
        {
        	
        	if(empty($response) || $response == "null")
        	{
        		$error = $this->getTranslator("Transaction non initialisee");
        	}
        	else
        	{
        		// object
        		$returnCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        
        		//Check if there are no errors ie httpresponse == 200 -OK
        		if ($returnCode == 200) {
        			//If there are no errors, the transaction ID is returned
        			$varRetour = json_decode($response, true);
        			$error = $varRetour['error'];
        			$tabAdherent = $varRetour['tabAdherent'];
        			
        			if(empty($error))
        			{
        			    $tabAdherentTravaille = array();
        			    foreach ($tabAdherent as $unAdherent)
        			    {
        			        $unAdherentTravaille = array();
        			        foreach ($unAdherent as $key => $value)
        			        {
        			            if(!empty($value))
        			                $value = trim($value);
        			                
        			            $unAdherentTravaille[$utilitaire->to_camel_case(strtolower($key))] = $value;
                            }
                            
                            if($police && $police != $unAdherentTravaille['police'])
                            {
                                continue;
                            }

        			        
        			        $unAdherentTravaille['codeAdherent'] = $unAdherentTravaille['codeAssure']."_".$unAdherentTravaille['police'];
        			        
        			        
        			        if(!empty($unAdherentTravaille['naissance']))
        			        {
        			            $unAdherentTravaille['naissance'] = str_replace(" ", "", $unAdherentTravaille['naissance']);
        			        }
        			        if(!empty($unAdherentTravaille['effetPolice']))
        			        {
        			            $unAdherentTravaille['effetPolice'] = str_replace(" ", "", $unAdherentTravaille['effetPolice']);
        			        }
        			        if(!empty($unAdherentTravaille['echeancePolice']))
        			        {
        			            $unAdherentTravaille['echeancePolice'] = str_replace(" ", "", $unAdherentTravaille['echeancePolice']);
        			        }
                            
                            $tabAdherentTravaille[$unAdherentTravaille['codeAdherent']] = $unAdherentTravaille;
                        }
                        

                       /*$tabAdherentBd = $this->getEntityManager()->getRepository('Entity\Adherent')->findBy(array(
                            'statut' => '1'
                        ));*/
						$tabAdherentBd = $this->getEntityManager()->getRepository('Entity\Adherent')->findAll();

                      
						
						foreach($tabAdherentBd as $unAdherentBd)
                        {        
                                 //changement de taux employes oncc 1
								 if($unAdherentBd->getCodeAdherent()=="226_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="227_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="228_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="229_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="230_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="3_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="54_1017-2130000110"
									|| $unAdherentBd->getCodeAdherent()=="55_1017-2130000110"
									|| $unAdherentBd->getCodeAdherent()=="56_1017-2130000110"
									|| $unAdherentBd->getCodeAdherent()=="57_1017-2130000110"
									|| $unAdherentBd->getCodeAdherent()=="58_1017-2130000110"
									|| $unAdherentBd->getCodeAdherent()=="59_1017-2130000110")
								{
									$unAdherentBd->setGroupe("22");
									$unAdherentBd->setStatut("1");
								}
								
								//changement de taux employes oncc 2
								 else if($unAdherentBd->getCodeAdherent()=="202_1017-2130000110"
									|| $unAdherentBd->getCodeAdherent()=="203_1017-2130000110"
									|| $unAdherentBd->getCodeAdherent()=="204_1017-2130000110"
									|| $unAdherentBd->getCodeAdherent()=="231_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="267_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="268_1017-2130000084")
								{
									$unAdherentBd->setGroupe("33");
									$unAdherentBd->setStatut("1");
								}
								
								//changement de taux employes oncc 3
								 else if($unAdherentBd->getCodeAdherent()=="232_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="233_1017-2130000084"
									|| $unAdherentBd->getCodeAdherent()=="257_1017-2130000110"
									|| $unAdherentBd->getCodeAdherent()=="258_1017-2130000110")
								{
									$unAdherentBd->setGroupe("44");
									$unAdherentBd->setStatut("1");
								}
								
                                
								
								else
								{
									$unAdherentBd->setStatut("-1");
								}
								
								//activation de la police 1017-2130000084 de oncc 
						if($unAdherentBd->getPolice()=="1017-2130000084" && $unAdherentBd->getEcheancePolice()->format("Y-m-d") > date("Y-m-d") )
								{
									$unAdherentBd->setStatut("1");
								}
								
						}
						 
						
						
						foreach($tabAdherentTravaille as $unAdherentTravaille)
                        {
						foreach($tabAdherentBd as $unAdherentBd)
                        {
                            if($unAdherentBd->getCodeAdherent()== $unAdherentTravaille['codeAdherent'])
                            {
                                $unAdherentBd->setStatut("1");
						
                            }
							
							//desactiver fraudeur de camtel
					        if($unAdherentBd->getCodeAdherent()=="1621_1017-2130000100"
									|| $unAdherentBd->getCodeAdherent()=="2226_1017-2130000100"
									|| $unAdherentBd->getCodeAdherent()=="2135_1017-2130000100"
									|| $unAdherentBd->getCodeAdherent()=="3210_1017-2130000100")
							{
									
									$unAdherentBd->setStatut("-1");
							}
							
						}
                        }

                        $this->getEntityManager()->flush();
        			}
        		}
        		else
        		{
        			$error = $this->getTranslator("Erreur de communication avec le serveur")." : <b>".$returnCode."</b>";
        		}
        	}
        }
        else // S'il y a une erreur, on affiche "Erreur", suivi du détail de l'erreur.
        {
        	$error =  curl_error($ch);
        }
        
        // On ferme notre session cURL.
        curl_close($ch);
        
        
        var_dump($error);
        exit;
    }

    public function desactiverDonneesAyantDroitAction()
    {
        set_time_limit(0);
        ini_set('memory_limit','256000M');
        
        $error = "";
        $utilitaire = new Utilitaire;
        

        $codeAdherentFromRoute = $this->params()->fromRoute('codeAdherent', null);

        $postValues = array_merge_recursive(
            $this->getRequest()->getPost()->toArray(),
            $this->getRequest()->getFiles()->toArray()
            );
        
        $appliConfig =  new \Application\Core\AppliConfig();
        $webserviceServer = $appliConfig->get("webservice_server");
        
        
        $requete_params = array();
        
        // Initialise notre session cURL. On lui donne la requête à exécuter
        $ch = curl_init();
        
        //set option of URL to post to
        curl_setopt($ch, CURLOPT_URL, "http://".$webserviceServer["ip_adress"]."/web_service/public/biometry/get-liste-ayant-droit");
        //set option of request method -----HTTP POST Request
        curl_setopt($ch, CURLOPT_POST, true);
        //The HTTP authentication methods to use
        curl_setopt($ch, CURLOPT_HTTPAUTH, CURLAUTH_ANY);
        //This line sets the parameters to post to the URL
        curl_setopt($ch, CURLOPT_POSTFIELDS, $requete_params);
        //This line makes sure that the response is gotten back to the
        // $response object(see below) and not echoed
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, 0);
        
        
        
        // On lance l'exécution de la requête URL.
        if($response = curl_exec($ch)) // Si elle s'est exécutée correctement
        {
            
            if(empty($response) || $response == "null")
            {
                $error = $this->getTranslator("Transaction non initialisee");
            }
            else
            {
                // object
                $returnCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
                
                //Check if there are no errors ie httpresponse == 200 -OK
                if ($returnCode == 200) {
                    //If there are no errors, the transaction ID is returned
                    $varRetour = json_decode($response, true);
                    $error = $varRetour['error'];
                    $tabAyantDroit = $varRetour['tabAyantDroit'];
                    
                    if(empty($error))
                    {
                        $tabAyantDroitTravaille = array();
                        foreach ($tabAyantDroit as $unAyantDroit)
                        {
                            $unAyantDroitTravaille = array();
                            foreach ($unAyantDroit as $key => $value)
                            {
                                if(!empty($value))
                                    $value = trim($value);
                                    
                                    $unAyantDroitTravaille[$utilitaire->to_camel_case(strtolower($key))] = $value;
                            }
                            
                            
                            $codeAdherent = $unAyantDroitTravaille['codeAssure']."_".$unAyantDroitTravaille['police'];
                            
                            
                            if($codeAdherentFromRoute && $codeAdherentFromRoute != $codeAdherent)
                            {
                                continue;
                            }
                            
                            
                            $adherent = $this->getEntityManager()->find('Entity\Adherent', $codeAdherent);
                            if($adherent)
                            {
                                // var_dump($unAyantDroit);
                                
                                $unAyantDroitTravaille['codeAyantDroit'] = $codeAdherent."_".$unAyantDroitTravaille['codeAyantD'];
                                $unAyantDroitTravaille['codeAdherent'] = $codeAdherent;
                                $unAyantDroitTravaille['nom'] = $unAyantDroitTravaille['ayantsDroits'];
                                
                                
                                if(!empty($unAyantDroitTravaille['naissance']))
                                {
                                    $unAyantDroitTravaille['naissance'] = str_replace(" ", "", $unAyantDroitTravaille['naissance']);
                                }

                                if($adherent->getStatut() == "1")
                                {
                                    $tabAyantDroitTravaille[$unAyantDroitTravaille['codeAyantDroit']] = $unAyantDroitTravaille;
                                }
                            }
                        }



                        $tabAyantDroitBd = $this->getEntityManager()->getRepository('Entity\AyantDroit')->findBy(array(
                            'statut' => '1'
                        ));

                        foreach($tabAyantDroitBd as $unAyantDroitBd)
                        {
                            if(!array_key_exists($unAyantDroitBd->getCodeAyantDroit(), $tabAyantDroitTravaille))
                            {
                                $unAyantDroitBd->setStatut("-1");
                            }
                        }

                        $this->getEntityManager()->flush();
                    }
                }
                else
                {
                    $error = $this->getTranslator("Erreur de communication avec le serveur")." : <b>".$returnCode."</b>";
                }
            }
        }
        else // S'il y a une erreur, on affiche "Erreur", suivi du détail de l'erreur.
        {
            $error =  curl_error($ch);
        }
        
        // On ferme notre session cURL.
        curl_close($ch);
        
        
        var_dump($error);
        exit;
    }



    
    function jsonResponse($status, $status_message, $data)
    {
        $response['status'] = $status;
        $response['status_message'] = $status_message;
        $response['data'] = $data;
        $json_response = json_encode($response,JSON_UNESCAPED_UNICODE);
        //var_dump($json_response);
        echo $json_response;
    }
    
    function getItems($noms)
    {
        if(empty($noms))
            return array();

        $tabParams = array(
                        "assurePrincipal" => $noms,
                        "statut" => "1"
                    );
        
        $tabAdherent = $this->adherentManager->getListeAdherentTabParams($tabParams);
        $tabAdherentTraites = array();
        foreach ($tabAdherent as $unAdherent)
        {
            $adherentTabReturn = array("CODE_ASSURE" => $unAdherent->getCodeAdherent(),
                "ASSURE_PRINCIPAL" => utf8_encode($unAdherent->getAssurePrincipal()),
                "SOUSCRIPTEUR" => utf8_encode($unAdherent->getSouscripteur()),
                "TELEPHONE" => $unAdherent->getTelephone(),
                "POLICE" => $unAdherent->getPolice(),
				"ENROLE" => $unAdherent->getEnrole(),
            );
            
            $tabAdherentTraites[] = $adherentTabReturn;
        }
        
        return $tabAdherentTraites;
    }
	
	
	 function getItemsSecugen($noms)
    {
        if(empty($noms))
            return array();

        $tabParams = array(
                        "assurePrincipal" => $noms,
                        "statut" => "1"
                    );
        
        $tabAdherent = $this->adherentManager->getListeAdherentTabParams($tabParams);
        $tabAdherentTraites = array();
        foreach ($tabAdherent as $unAdherent)
        {   
		    if(strlen($unAdherent->getAssurePrincipal())>42)
			  {
		       $unAdherent->setAssurePrincipal(substr($unAdherent->getAssurePrincipal(), 0, 41));								 
			  }
			  
			if($unAdherent->getNaissance())
              {
                $dateNaissanceAdherent= $unAdherent->getNaissance()->format("d/m/Y");
              }
			  
			if($unAdherent->getEnrole()=="-1" || $unAdherent->getEnrole()=="1")
			{
				$unAdherent->setEnrole("NON ENROLE");
			}
		    else if($unAdherent->getEnrole()=="2")
			{
				$unAdherent->setEnrole("ENROLE");
			}
			
					
            $adherentTabReturn = array("CODE_ASSURE" => $unAdherent->getCodeAdherent(),
                "ASSURE_PRINCIPAL" => $unAdherent->getAssurePrincipal(),
                "SOUSCRIPTEUR" => $unAdherent->getSouscripteur(),
                "MATRICULE" => $unAdherent->getMatricule(),
				//"DATE_NAISS" =>  $dateNaissanceAdherent,
                "POLICE" => $unAdherent->getPolice(),
				"ENROLE" => $unAdherent->getEnrole(),
				"TELEPHONE" => $unAdherent->getTelephone(),
            );
            
            $tabAdherentTraites[] = $adherentTabReturn;
        }
        
        return $tabAdherentTraites;
    }
    
//     function genererCodeVisite()
//     {
//         return uniqid();
//     }
}
