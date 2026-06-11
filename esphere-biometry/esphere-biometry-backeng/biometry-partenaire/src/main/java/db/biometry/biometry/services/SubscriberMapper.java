/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package db.biometry.biometry.services;

/**
 *
 * @author USER01
 */

import db.biometry.biometry.dtos.CreateSubscriberRequest;
import db.biometry.biometry.dtos.SubscriberResponse;
import db.biometry.biometry.entite.Subscribers;

import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SubscriberMapper {

    @Mapping(target = "id",                   ignore = true)
    @Mapping(target = "passwordHash",         ignore = true)
    @Mapping(target = "activationToken",      ignore = true)
    @Mapping(target = "activationTokenExpiry",ignore = true)
    @Mapping(target = "activationSentAt",     ignore = true)
    @Mapping(target = "accountActivatedAt",   ignore = true)
    @Mapping(target = "createdAt",            ignore = true)
    @Mapping(target = "updatedAt",            ignore = true)
    @Mapping(target = "createdBy",            ignore = true)
    @Mapping(target = "updatedBy",            ignore = true)
    @Mapping(target = "version",              ignore = true)
    @Mapping(target = "activationDurationHrs", source = "activationDurationHours")
    Subscribers toEntity(CreateSubscriberRequest request);

    @Mapping(target = "activationTokenValid", expression = "java(subscriber.isActivationTokenValid())")
    SubscriberResponse toResponse(Subscribers subscriber);
    
}
