<?php
/**
 * Zend Framework
 *
 * LICENSE
 *
 * This source file is subject to the new BSD license that is bundled
 * with this package in the file LICENSE.txt.
 * It is also available through the world-wide-web at this URL:
 * http://framework.zend.com/license/new-bsd
 * If you did not receive a copy of the license and are unable to
 * obtain it through the world-wide-web, please send an email
 * to license@zend.com so we can send you a copy immediately.
 *
 * @category   Zend
 * @package    Zend_Translator
 * @subpackage Resource
 * @copyright  Copyright (c) 2005-2012 Zend Technologies USA Inc. (http://www.zend.com)
 * @license    http://framework.zend.com/license/new-bsd     New BSD License
 */


/**
 * FR-Revision: 09.Sept.2012
 */
return array(
    // Zend_I18n_Validator_Alnum
    "Invalid type given. String, integer or float expected" => "Type invalide. Chaîne, entier ou flottant attendu",
    "The input contains characters which are non alphabetic and no digits" => "L'entree contient des caracteres non alphabetiques et non numeriques",
    "The input is an empty string" => "L'entree est une chaîne vide",

    // Zend_I18n_Validator_Alpha
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",
    "The input contains non alphabetic characters" => "L'entree contient des caracteres non alphabetiques",
    "The input is an empty string" => "L'entree est une chaîne vide",

    // Zend_I18n_Validator_Float
    "Invalid type given. String, integer or float expected" => "Type invalide. Chaîne, entier ou flottant attendu",
    "The input does not appear to be a float" => "L'entree n'est pas un nombre flottant",

    // Zend_I18n_Validator_Int
    "Invalid type given. String or integer expected" => "Type invalide. Chaîne ou entier attendu",
    "The input does not appear to be an integer" => "L'entree n'est pas un entier",

    // Zend_I18n_Validator_PostCode
    "Invalid type given. String or integer expected" => "Type invalid. Chaîne ou entier attendu",
    "The input does not appear to be a postal code" => "L'entree ne semble pas etre un code postal valide",
    "An exception has been raised while validating the input" => "Une exception a ete levee lors de la validation de l'entree",

    // Zend_Validator_Barcode
    "The input failed checksum validation" => "L'entree n'a pas passe la validation de la somme de contrôle",
    "The input contains invalid characters" => "L'entree contient des caracteres invalides",
    "The input should have a length of %length% characters" => "L'entree devrait contenir %length% caracteres",
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",

    // Zend_Validator_Between
    "The input is not between '%min%' and '%max%', inclusively" => "L'entree n'est pas comprise entre '%min%' et '%max%', inclusivement",
    "The input is not strictly between '%min%' and '%max%'" => "L'entree n'est pas strictement comprise entre '%min%' et '%max%'",

    // Zend_Validator_Callback
    "The input is not valid" => "L'entree n'est pas valide",
    "An exception has been raised within the callback" => "Une exception a ete levee dans la fonction de rappel",

    // Zend_Validator_CreditCard
    "The input seems to contain an invalid checksum" => "L'entree semble contenir une somme de contrôle invalide",
    "The input must contain only digits" => "L'entree ne doit contenir que des chiffres",
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",
    "The input contains an invalid amount of digits" => "L'entree contient un nombre invalide de chiffres",
    "The input is not from an allowed institute" => "L'entree ne provient pas d'une institution autorisee",
    "The input seems to be an invalid creditcard number" => "L'entree semble etre un numero de carte bancaire invalide",
    "An exception has been raised while validating the input" => "Une exception a ete levee lors de la validation de l'entree",

    // Zend_Validator_Csrf
    "The form submitted did not originate from the expected site" => "Le formulaire ne provient pas du site attendu",

    // Zend_Validator_Date
    "Invalid type given. String, integer, array or DateTime expected" => "Type invalide. Chaîne, entier, tableau ou DateTime attendu",
    "The input does not appear to be a valid date" => "L'entree ne semble pas etre une date valide",
    "The input does not fit the date format '%format%'" => "L'entree ne correspond pas au format '%format%'",

    // Zend_Validator_DateStep
    "Invalid type given. String, integer, array or DateTime expected" => "Entree invalide. Chaîne, entier, tableau ou DateTime attendu",
    "The input does not appear to be a valid date" => "L'entree ne semble pas etre une date valide",
    "The input is not a valid step" => "L'entree n'est pas un intervalle valide",

    // Zend_Validator_Db_AbstractDb
    "No record matching the input was found" => "Aucun enregistrement trouve",
    "A record matching the input was found" => "Un enregistrement a ete trouve",

    // Zend_Validator_Digits
    "The input must contain only digits" => "L'entree ne doit contenir que des chiffres",
    "The input is an empty string" => "L'entree est une chaîne vide",
    "Invalid type given. String, integer or float expected" => "Type invalide. Chaîne, entier ou flottant attendu",

    // Zend_Validator_EmailAddress
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",
    "The input is not a valid email address. Use the basic format local-part@hostname" => "L'entree n'est pas une adresse email valide. Utilisez le format local-part@hostname",
    "'%hostname%' is not a valid hostname for the email address" => "'%hostname%' n'est pas un nom d'hôte valide pour l'adresse email",
    "'%hostname%' does not appear to have any valid MX or A records for the email address" => "'%hostname%' ne semble pas avoir d'enregistrement MX valide pour l'adresse email",
    "'%hostname%' is not in a routable network segment. The email address should not be resolved from public network" => "'%hostname%' n'est pas dans un segment reseau routable. L'adresse email ne devrait pas etre resolue depuis un reseau public.",
    "'%localPart%' can not be matched against dot-atom format" => "'%localPart%' ne correspond pas au format dot-atom",
    "'%localPart%' can not be matched against quoted-string format" => "'%localPart%' ne correspond pas a une chaîne entre quotes",
    "'%localPart%' is not a valid local part for the email address" => "'%localPart%' n'est pas une partie locale valide pour l'adresse email",
    "The input exceeds the allowed length" => "L'entree depasse la taille autorisee",

    // Zend_Validator_Explode
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",

    // Zend_Validator_File_Count
    "Too many files, maximum '%max%' are allowed but '%count%' are given" => "Trop de fichiers. '%max%' sont autorises au maximum, mais '%count%' reçu(s)",
    "Too few files, minimum '%min%' are expected but '%count%' are given" => "Trop peu de fichiers. '%min%' sont attendus, mais '%count%' reçu(s)",

    // Zend_Validator_File_Crc32
    "File '%value%' does not match the given crc32 hashes" => "Le fichier '%value%' ne correspond pas aux sommes de contrôle CRC32 donnees",
    "A crc32 hash could not be evaluated for the given file" => "Une somme de contrôle CRC32 n'a pas pu etre calculee pour le fichier",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_ExcludeExtension
    "File '%value%' has a false extension" => "Le fichier '%value%' a une mauvaise extension",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_Exists
    "File '%value%' does not exist" => "Le fichier '%value%' n'existe pas",

    // Zend_Validator_File_Extension
    "File '%value%' has a false extension" => "Le fichier '%value%' a une mauvaise extension",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_FilesSize
    "All files in sum should have a maximum size of '%max%' but '%size%' were detected" => "Tous les fichiers devraient avoir une taille maximale de '%max%' mais une taille de '%size%' a ete detectee",
    "All files in sum should have a minimum size of '%min%' but '%size%' were detected" => "Tous les fichiers devraient avoir une taille minimale de '%max%' mais une taille de '%size%' a ete detectee",
    "One or more files can not be read" => "Un ou plusieurs fichiers ne peut pas etre lu",

    // Zend_Validator_File_Hash
    "File '%value%' does not match the given hashes" => "Le fichier '%value%' ne correspond pas aux sommes de contrôle donnees",
    "A hash could not be evaluated for the given file" => "Une somme de contrôle n'a pas pu etre calculee pour le fichier",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_ImageSize
    "Maximum allowed width for image '%value%' should be '%maxwidth%' but '%width%' detected" => "La largeur maximale pour l'image '%value%' devrait etre '%maxwidth%', mais '%width%' detecte",
    "Minimum expected width for image '%value%' should be '%minwidth%' but '%width%' detected" => "La largeur minimale pour l'image '%value%' devrait etre '%minwidth%', mais '%width%' detecte",
    "Maximum allowed height for image '%value%' should be '%maxheight%' but '%height%' detected" => "La hauteur maximale pour l'image '%value%' devrait etre '%maxheight%', mais '%height%' detecte",
    "Minimum expected height for image '%value%' should be '%minheight%' but '%height%' detected" => "La hauteur maximale pour l'image '%value%' devrait etre '%minheight%', mais '%height%' detecte",
    "The size of image '%value%' could not be detected" => "La taille de l'image '%value%' n'a pas pu etre detectee",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_IsCompressed
    "File '%value%' is not compressed, '%type%' detected" => "Le fichier '%value%' n'est pas compresse, '%type%' detecte",
    "The mimetype of file '%value%' could not be detected" => "Le type MIME du fichier '%value%' n'a pas pu etre detecte",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_IsImage
    "File '%value%' is no image, '%type%' detected" => "Le fichier '%value%' n'est pas une image, '%type%' detecte",
    "The mimetype of file '%value%' could not be detected" => "Le type MIME du fichier '%value%' n'a pas pu etre detecte",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_Md5
    "File '%value%' does not match the given md5 hashes" => "Le fichier '%value%' ne correspond pas aux sommes de contrôle MD5 donnees",
    "A md5 hash could not be evaluated for the given file" => "Une somme de contrôle MD5 n'a pas pu etre calculee pour le fichier",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_MimeType
    "File '%value%' has a false mimetype of '%type%'" => "Le fichier '%value%' a un faux type MIME : '%type%'",
    "The mimetype of file '%value%' could not be detected" => "Le type MIME du fichier '%value%' n'a pas pu etre detecte",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_NotExists
    "File '%value%' exists" => "Le fichier '%value%' existe",

    // Zend_Validator_File_Sha1
    "File '%value%' does not match the given sha1 hashes" => "Le fichier '%value%' ne correspond pas aux sommes de contrôle SHA1 donnees",
    "A sha1 hash could not be evaluated for the given file" => "Une somme de contrôle SHA1 n'a pas pu etre calculee pour le fichier",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_Size
    "Maximum allowed size for file '%value%' is '%max%' but '%size%' detected" => "La taille de fichier maximale pour '%value%' est '%max%', mais '%size%' detectee",
    "Minimum expected size for file '%value%' is '%min%' but '%size%' detected" => "La taille de fichier minimale pour '%value%' est '%min%', mais '%size%' detectee",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_File_Upload
    "File '%value%' exceeds the defined ini size" => "Le fichier '%value%' depasse la taille definie dans le fichier INI",
    "File '%value%' exceeds the defined form size" => "Le fichier '%value%' depasse la taille definie dans le formulaire",
    "File '%value%' was only partially uploaded" => "Le fichier '%value%' n'a ete que partiellement envoye",
    "File '%value%' was not uploaded" => "Le fichier '%value%' n'a pas ete envoye",
    "No temporary directory was found for file '%value%'" => "Le dossier temporaire n'a pas ete trouve pour le fichier '%value%'",
    "File '%value%' can't be written" => "Impossible d'ecrire dans le fichier '%value%'",
    "A PHP extension returned an error while uploading the file '%value%'" => "Une extension PHP a retourne une erreur en envoyant le fichier '%value%'",
    "File '%value%' was illegally uploaded. This could be a possible attack" => "Le fichier '%value%' a ete envoye illegalement. Il peut s'agir d'une attaque",
    "File '%value%' was not found" => "Le fichier '%value%' n'a pas ete trouve",
    "Unknown error while uploading file '%value%'" => "Erreur inconnue lors de l'envoi du fichier '%value%'",

    // Zend_Validator_File_WordCount
    "Too much words, maximum '%max%' are allowed but '%count%' were counted" => "Trop de mots. '%max%' sont autorises, '%count%' comptes",
    "Too less words, minimum '%min%' are expected but '%count%' were counted" => "Pas assez de mots. '%min%' sont attendus, '%count%' comptes",
    "File '%value%' is not readable or does not exist" => "Le fichier '%value%' n'est pas lisible ou n'existe pas",

    // Zend_Validator_GreaterThan
    "The input is not greater than '%min%'" => "L'entree n'est pas superieure a '%min%'",
    "The input is not greater or equal than '%min%'" => "L'entree n'est pas superieure ou egale a '%min%'",

    // Zend_Validator_Hex
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",
    "The input contains non-hexadecimal characters" => "L'entree contient des caracteres non-hexadecimaux",

    // Zend_Validator_Hostname
    "The input appears to be a DNS hostname but the given punycode notation cannot be decoded" => "L'entree semble etre un DNS valide mais le code n'a pu etre decode",
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",
    "The input appears to be a DNS hostname but contains a dash in an invalid position" => "L'entree semble etre un nom d'hôte DNS mais il contient un tiret a une position invalide",
    "The input does not match the expected structure for a DNS hostname" => "L'entree ne correspond pas a la structure attendue d'un nom d'hôte DNS",
    "The input appears to be a DNS hostname but cannot match against hostname schema for TLD '%tld%'" => "L'entree semble etre un nom d'hôte DNS valide mais ne correspond pas au schema de l'extension TLD '%tld%'",
    "The input does not appear to be a valid local network name" => "L'entree ne semble pas etre un nom de reseau local valide",
    "The input does not appear to be a valid URI hostname" => "L'entree ne semble pas etre une URI de nom d'hôte valide",
    "The input appears to be an IP address, but IP addresses are not allowed" => "L'entree semble etre une adresse IP valide, mais les adresses IP ne sont pas autorisees",
    "The input appears to be a local network name but local network names are not allowed" => "L'entree semble etre un nom de reseau local, mais les reseaux locaux ne sont pas autorises",
    "The input appears to be a DNS hostname but cannot extract TLD part" => "L'entree semble etre un nom d'hôte DNS mais l'extension TLD ne peut etre extraite",
    "The input appears to be a DNS hostname but cannot match TLD against known list" => "L'entree semble etre un nom d'hôte DNS mais son extension TLD semble inconnue",

    // Zend_Validator_Iban
    "Unknown country within the IBAN" => "Pays inconnu pour l'IBAN",
    "Countries outside the Single Euro Payments Area (SEPA) are not supported" => "Les pays en dehors du Single Euro Payments Area (SEPA) ne sont pas supportes",
    "The input has a false IBAN format" => "L'entree n'a pas un format IBAN valide",
    "The input has failed the IBAN check" => "L'entree n'a pas passe la validation IBAN",

    // Zend_Validator_Identical
    "The two given tokens do not match" => "Les deux jetons passes ne correspondent pas",
    "No token was provided to match against" => "Aucun jeton de correspondance n'a ete donne",

    // Zend_Validator_InArray
    "The input was not found in the haystack" => "L'entree ne fait pas partie des valeurs attendues",

    // Zend_Validator_Ip
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",
    "The input does not appear to be a valid IP address" => "L'entree ne semble pas etre une adresse IP valide",

    // Zend_Validator_Isbn
    "Invalid type given. String or integer expected" => "Type invalide. Chaîne ou entier attendu",
    "The input is not a valid ISBN number" => "L'entree n'est pas un nombre ISBN valide",

    // Zend_Validator_LessThan
    "The input is not less than '%max%'" => "L'entree n'est pas inferieure a '%max%'",
    "The input is not less or equal than '%max%'" => "L'entree n'est pas inferieure ou egale a '%max%'",

    // Zend_Validator_NotEmpty
    "Value is required and can't be empty" => "Une valeur est requise et ne peut etre vide",
    "Invalid type given. String, integer, float, boolean or array expected" => "Type invalide. Chaîne, entier, flottant, booleen ou tableau attendu",

    // Zend_Validator_Regex
    "Invalid type given. String, integer or float expected" => "Type invalide. Chaîne, entier ou flottant attendu",
    "The input does not match against pattern '%pattern%'" => "L'entree n'est pas valide avec l'expression '%pattern%'",
    "There was an internal error while using the pattern '%pattern%'" => "Une erreur interne est survenue avec l'expression '%pattern%'",

    // Zend_Validator_Sitemap_Changefreq
    "The input is not a valid sitemap changefreq" => "L'entree n'est pas une valeur de frequence de changement de sitemap valide",
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",

    // Zend_Validator_Sitemap_Lastmod
    "The input is not a valid sitemap lastmod" => "L'entree n'est pas une date de derniere modification de sitemap valide",
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",

    // Zend_Validator_Sitemap_Loc
    "The input is not a valid sitemap location" => "L'entree n'est pas un emplacement de sitemap valide",
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",

    // Zend_Validator_Sitemap_Priority
    "The input is not a valid sitemap priority" => "L'entree n'est pas une priorite de sitemap valide",
    "Invalid type given. Numeric string, integer or float expected" => "Type invalide. Chaîne numerique, entier ou flottant attendu",

    // Zend_Validator_Step
    "Invalid value given. Scalar expected" => "Type invalide. Scalaire attendu",
    "The input is not a valid step" => "L'entree n'est pas un intervalle valide",

    // Zend_Validator_StringLength
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",
    "The input is less than %min% characters long" => "L'entree contient moins de %min% caracteres",
    "The input is more than %max% characters long" => "L'entree contient plus de %max% caracteres",

    // Zend_Validator_Uri
    "Invalid type given. String expected" => "Type invalide. Chaîne attendue",
    "The input does not appear to be a valid Uri" => "L'entree ne semble pas etre une URI valide",
);
