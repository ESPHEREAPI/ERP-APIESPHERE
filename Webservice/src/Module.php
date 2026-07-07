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

use Zend\Mvc\ModuleRouteListener;
use Zend\Mvc\MvcEvent;
use Zend\ModuleManager\Feature\ServiceProviderInterface;

use Entity\Adherent;
use Application\Manager\AdherentManager;
use Application\Manager\AyantDroitManager;
use Application\Manager\BackAuthManager;
use Application\Manager\PrestataireManager;


class Module implements ServiceProviderInterface
{
    public function onBootstrap(MvcEvent $e)
    {
        $e->getApplication()->getServiceManager()->get('translator');
        $eventManager        = $e->getApplication()->getEventManager();
        $moduleRouteListener = new ModuleRouteListener();
        $moduleRouteListener->attach($eventManager);
    }

    public function translate($k)
    {
    	if ($this->_translator && $this->_translator_enabled) {
    		return $this->_translator->translate($k, "application");
    	}
    	return $k;
    }
    
    public function getConfig()
    {
        return include __DIR__ . '/../config/module.config.php';
    }
 	
 	public function getServiceConfig()
 	{
 		return array(
 			'factories' => array(
 			    
 			// Managers
 			    'Application\Manager\AdherentManager' => function ($sm) {
     			    $result = new AdherentManager;
     			    $result->setServiceManager($sm);
     			    return $result;
 			    },
 			    'Application\Manager\AyantDroitManager' => function ($sm) {
     			    $result = new AyantDroitManager;
     			    $result->setServiceManager($sm);
     			    return $result;
 			    },
 			    'Application\Manager\BackAuthManager' => function ($sm) {
     			    $result = new BackAuthManager;
     			    $result->setServiceManager($sm);
     			    return $result;
 			    },
 			    'Application\Manager\PrestataireManager' => function ($sm) {
     			    $result = new PrestataireManager;
     			    $result->setServiceManager($sm);
     			    return $result;
 			    },
 			    
 			    
 			    // Models
	 			'Entity\Adherent' => function ($sm) {
					$result = new Adherent(
						$sm->get('Zend\Db\Adapter\Adapter')
					);
					$result->setEntityManager($sm->get('Doctrine\ORM\EntityManager'));
					return $result;
				},
			),
 		);
 	}
}
