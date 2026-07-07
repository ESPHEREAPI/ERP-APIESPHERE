<?php

namespace Webservice\Controller\Factory;

use Webservice\Controller\IndexController;
use Interop\Container\ContainerInterface;
use Zend\ServiceManager\Factory\FactoryInterface;

class IndexControllerFactory implements FactoryInterface
{
    public function __invoke(ContainerInterface $container, $requestedName, array $options = null) 
    {
        $adherentManager        = $container->get('Application\Manager\AdherentManager');
        $ayantDroitManager        = $container->get('Application\Manager\AyantDroitManager');
        $backAuthManager        = $container->get('Application\Manager\BackAuthManager');
         $prestataireManager        = $container->get('Application\Manager\PrestataireManager');
        $menuManager              = $container->get('Application\Manager\MenuManager');
		$employeManager              = $container->get('Application\Manager\EmployeManager');
        // Create an instance of the controller and pass the dependency 
        // to controller's constructor.
        return new IndexController($container, $adherentManager, $ayantDroitManager, $backAuthManager, $prestataireManager,$menuManager, $employeManager);
    }
}