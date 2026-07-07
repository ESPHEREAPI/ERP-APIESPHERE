<?php
/**
 * Application blanche   (http://zf2.biz)
 * utiise Zend Framework (http://framework.zend.com/)
 *
 * @link      http://github.com/zendframework/ZendSkeletonApplication for the canonical source repository
 * @copyright Copyright (c) 2005-2012 Zend Technologies USA Inc. (http://www.zend.com)
 * @license   http://framework.zend.com/license/new-bsd New BSD License
 */

namespace Webservice;

return array(
    'router' => array(
        'routes' => array(
            'webservice' => array(
                'type'    => 'Literal',
                'options' => array(
                    'route'    => '/webservice',
                    'defaults' => array(
                        'controller'    => Controller\IndexController::class,
                        'action'        => 'index',
                    ),
                ),
                'may_terminate' => true,
				'child_routes' => array(
				    
				    'vider-donnees-un-prestataire' => array(
				        'type' => 'Segment',
				        'options' => array(
				            'route'    => '/vider-donnees-un-prestataire/:idPrestataire',
				            'defaults' => array(
				                'action'        => 'viderDonneesUnPrestataire',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
				    
				    'vider-donnees-une-visite' => array(
				        'type' => 'Segment',
				        'options' => array(
				            'route'    => '/vider-donnees-une-visite/:idVisite',
				            'defaults' => array(
				                'action'        => 'viderDonneesUneVisite',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
				    
				    'recuperer-donnees-taux-prestation' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/recuperer-donnees-taux-prestation',
				            'defaults' => array(
				                'action'        => 'recupererDonneesTauxPrestation',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
				    
					'recuperer-donnees-adherent' => array(
                        'type'    => 'Segment',
                        'options' => array(
                            'route'    => '/recuperer-donnees-adherent[/:police]',
                            'defaults' => array(
                                'action'        => 'recupererDonneesAdherent',
                            ),
                        ),
                        'may_terminate' => true,
        				'child_routes' => array(
        					
        				),
        				'verb' => 'get, post',
                    ),
				    'recuperer-donnees-ayant-droit' => array(
				        'type'    => 'Segment',
				        'options' => array(
				            'route'    => '/recuperer-donnees-ayant-droit[/:police]',
				            'defaults' => array(
				                'action'        => 'recupererDonneesAyantDroit',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
					),
					'desactiver-donnees-adherent' => array(
                        'type'    => 'Segment',
                        'options' => array(
                            'route'    => '/desactiver-donnees-adherent[/:police]',
                            'defaults' => array(
                                'action'        => 'desactiverDonneesAdherent',
                            ),
                        ),
                        'may_terminate' => true,
        				'child_routes' => array(
        					
        				),
        				'verb' => 'get, post',
                    ),
				    'desactiver-donnees-ayant-droit' => array(
				        'type'    => 'Segment',
				        'options' => array(
				            'route'    => '/desactiver-donnees-ayant-droit[/:codeAdherent]',
				            'defaults' => array(
				                'action'        => 'desactiverDonneesAyantDroit',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
				    'get-liste-adherent' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/get-liste-adherent',
				            'defaults' => array(
				                'action'        => 'getListeAdherent',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
					'get-liste-adherent-secugen' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/get-liste-adherent-secugen',
				            'defaults' => array(
				                'action'        => 'getListeAdherentSecugen',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
				    'generer-visite' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/generer-visite',
				            'defaults' => array(
				                'action'        => 'genererVisite',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
					'generer-visite-login' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/generer-visite-login',
				            'defaults' => array(
				                'action'        => 'genererVisiteLogin',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
				    'get-liste-ayant-droit' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/get-liste-ayant-droit',
				            'defaults' => array(
				                'action'        => 'getListeAyantDroit',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
				    'set-adherent-enrole' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/set-adherent-enrole',
				            'defaults' => array(
				                'action'        => 'setAdherentEnrole',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
					
					'set-adherent-enrole-secugen' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/set-adherent-enrole-secugen',
				            'defaults' => array(
				                'action'        => 'setAdherentEnroleSecugen',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
					
					'set-ayant-droit-enrole' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/set-ayant-droit-enrole',
				            'defaults' => array(
				                'action'        => 'setAyantDroitEnrole',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
				    'connexion' => array(
				        'type'    => 'Literal',
				        'options' => array(
				            'route'    => '/connexion',
				            'defaults' => array(
				                'action'        => 'connexion',
				            ),
				        ),
				        'may_terminate' => true,
				        'child_routes' => array(
				            
				        ),
				        'verb' => 'get, post',
				    ),
				),
				'verb' => 'get, post',
            ),
        ),
    ),
    'controllers' => array(
        'factories' => array(
			Controller\IndexController::class => Controller\Factory\IndexControllerFactory::class,
		),
    ),
    'view_manager' => array(
        'template_path_stack' => array(
            __DIR__ . '/../view',
        ),
        'strategies' => array('ViewJsonStrategy',), //--- pour de l'Ajax
    ),
);
