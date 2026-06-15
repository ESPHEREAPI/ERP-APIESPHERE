export interface ProductionRequest {
  certificate_variant_code: string;
  rc: number;
  police_number: string;
  starts_at: string;
  ends_at: string;
  customer_name: string;
  customer_phone: string;
  customer_email: string;
  customer_postal_code: string;
  customer_type: string;
  taxpayer_number: string;
  insured_name: string;
  insured_phone: string;
  insured_email: string;
  insured_postal_code: string;
  insured_birthdate: string;
  licence_plate: string;
  vehicle_chassis: string;
  vehicle_brand: string;
  vehicle_model: string;
  vehicle_category: string;
  vehicle_genre: string;
  vehicle_type: string;
  vehicule_usage: string;
  vehicle_energy: string;
  vehicle_gross_weight: number;
  vehicle_first_registration_date: string;
  driver_permis_categorie: string;
  nb_of_seats: number;
  fiscal_power: number;
  circulation_zone: string;
  driver_permis: string;
  vehicle_has_trailer: boolean;
  trailer_licence_plate?: string;
  driver_name: string;
  driver_birthdate: string;
  driver_licence_issued_at: string;
  insured_code: string;
  insured_profession: string;
  insured_city: string;



}